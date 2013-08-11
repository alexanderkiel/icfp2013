package icfp2013.generator;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import icfp2013.generator.syntax.Binary;
import icfp2013.generator.syntax.Expression;
import icfp2013.generator.syntax.Expressions;
import icfp2013.generator.syntax.Fold;
import icfp2013.generator.syntax.If;
import icfp2013.generator.syntax.Operators;
import icfp2013.generator.syntax.Program;
import icfp2013.generator.syntax.Unary;
import icfp2013.generator.syntax.Variable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Lists.transform;
import static icfp2013.generator.syntax.Expressions.c0;
import static icfp2013.generator.syntax.Expressions.c1;
import static icfp2013.generator.syntax.Expressions.program;
import static icfp2013.generator.syntax.Expressions.var;
import static icfp2013.generator.syntax.Operators.FOLD;
import static icfp2013.generator.syntax.Operators.TFOLD;
import static java.util.Collections.min;
import static java.util.Collections.singletonList;

/**
 * @author <a href="mailto:jonas.wagner@life.uni-leipzig.de">Jonas Wagner</a>
 */
public class ProgramGenerator implements Function<Random, Program> {
    private final List<Operators> allowedOperators;
    private final int size;

    public ProgramGenerator(List<Operators> allowedOperators, int size) {
        this.allowedOperators = allowedOperators;
        this.size = size;
    }

    @Override
    public Program apply(Random rng) {
        return new InternalGenerator(rng, allowedOperators, size).generateProgram();
    }

    private static class InternalGenerator {

        public static final Function<Operators, Integer> OPERATORS_MINIMAL_SIZE = new Function<Operators, Integer>() {
            @Override
            public Integer apply(Operators input) {
                return input.minimumSize;
            }
        };
        private final Random rng;
        private final List<Operators> stillAllowedOperators;
        private final int size;

        private int varCounter;

        private InternalGenerator(Random rng, List<Operators> stillAllowedOperators, int size) {
            this.rng = rng;
            this.stillAllowedOperators = newArrayList(stillAllowedOperators);
            this.size = size;
        }

        private Program generateProgram() {
            Variable var = var("x_" + (varCounter++));
            if (stillAllowedOperators.contains(Operators.TFOLD)) {
                return program(var, tFold(size - 1, var));
            } else {
                return program(var, recursiveExpression(size - 1, singletonList(var)));
            }
        }

        private Expression recursiveExpression(int remainingSize, List<Variable> availableVariables) {
            if (remainingSize < minimumOperatorsMinSize()) {
                return createMinSizeExpression(availableVariables);
            } else {
                return createExpression(remainingSize, availableVariables, getRndOperator(remainingSize));
            }
        }

        private Integer minimumOperatorsMinSize() {
            return min(transform(stillAllowedOperators, OPERATORS_MINIMAL_SIZE));
        }

        private Expression createMinSizeExpression(List<Variable> availableVariables) {
            ImmutableList<Expression> possibilities = ImmutableList.<Expression>builder()
                    .add(c0(), c1()).addAll(availableVariables).build();

            return possibilities.get(nextRndInt(possibilities.size()));
        }

        private Expression unary(Operators op, int remainingSize, List<Variable> availableVariables) {
            return new Unary(op, recursiveExpression(remainingSize, availableVariables));
        }

        private Expression binary(Operators op, int remainingSize, List<Variable> availableVariables) {
            List<Integer> branchSizes = randomizeBranchSizes(2, remainingSize);
            return new Binary(op,
                    recursiveExpression(branchSizes.get(0), availableVariables),
                    recursiveExpression(branchSizes.get(1), availableVariables));
        }

        private Expression if0(int remainingSize, List<Variable> availableVariables) {
            List<Integer> branchSizes = randomizeBranchSizes(3, remainingSize);

            return new If(recursiveExpression(branchSizes.get(0), availableVariables),
                    recursiveExpression(branchSizes.get(1), availableVariables),
                    recursiveExpression(branchSizes.get(2), availableVariables));
        }

        private Expression fold(int remainingSize, List<Variable> availableVariables) {
            Variable x = var("x_" + varCounter++), y = var("x_" + varCounter++);
            List<Integer> branchSizes = randomizeBranchSizes(3, remainingSize);

            stillAllowedOperators.remove(FOLD);
            return new Fold(
                    recursiveExpression(branchSizes.get(0), availableVariables),
                    recursiveExpression(branchSizes.get(1), availableVariables),
                    x, y,
                    recursiveExpression(branchSizes.get(2), availableVariables));
        }

        private Expression tFold(int remainingSize, Variable x) {
            Variable xNew = var("x_" + varCounter++), y = var("x_" + varCounter++);


            stillAllowedOperators.remove(TFOLD);
            return new Fold(x, Expressions.c0(), xNew, y,
                    recursiveExpression(remainingSize - 4, newArrayList(xNew, y)));
        }

        private List<Integer> randomizeBranchSizes(int numBranches, int remainingSize) {
            List<Integer> branchSizes = newArrayList();
            for (int i = 0; i < numBranches - 1; i++) {
                int branchSize;
                do {
                    branchSize = nextRndInt(remainingSize - (numBranches - i)) + 1;
                } while (transform(stillAllowedOperators, OPERATORS_MINIMAL_SIZE).contains(branchSize));
                branchSizes.add(branchSize);
                remainingSize -= branchSize;
            }
            branchSizes.add(remainingSize);
            return branchSizes;
        }

        private Expression createExpression(int remainingSize, List<Variable> availableVariables, Operators op) {
            switch (op) {
                case NOT:
                case SHL1:
                case SHR1:
                case SHR4:
                case SHR16:
                    return unary(op, remainingSize - 1, availableVariables);
                case AND:
                case OR:
                case XOR:
                case PLUS:
                    return binary(op, remainingSize - 1, availableVariables);
                case IF0:
                    return if0(remainingSize - 1, availableVariables);
                case FOLD:
                    return fold(remainingSize - 2, availableVariables);
                default:
                    throw new AssertionError("Unreachable");
            }
        }

        private int nextRndInt(int n) {
            if (n == 0) {
                return 0;
            } else {
                return rng.nextInt(n);
            }
        }

        private Operators getRndOperator(final int remainingSize) {
            ArrayList<Operators> operators = newArrayList(
                    Iterables.filter(stillAllowedOperators, new Predicate<Operators>() {
                        @Override
                        public boolean apply(Operators op) {
                            return op.minimumSize <= remainingSize;
                        }
                    }));
            return operators.get(nextRndInt(operators.size()));
        }
    }
}
