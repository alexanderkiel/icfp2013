package icfp2013.generator.syntax;

import static icfp2013.generator.syntax.Constant.C0;
import static icfp2013.generator.syntax.Constant.C1;

/**
 * @author <a href="mailto:jonas.wagner@life.uni-leipzig.de">Jonas Wagner</a>
 */
public abstract class Expressions {

    public static Expression c0() {
        return C0;
    }

    public static Expression c1() {
        return C1;
    }

    public static Variable var(String name) {
        return new Variable(name);
    }

    public static Expression if0(Expression condition, Expression then, Expression elseE) {
        return new If(condition, then, elseE);
    }

    public static Expression fold(Expression vector, Expression start, Variable x, Variable y, Expression e) {
        return new Fold(vector, start, x, y, e);
    }

    public static Program program(Variable x, Expression e) {
        return new Program(x, e);
    }

}
