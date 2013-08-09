package icfp2013.generator.syntax;

import com.google.common.collect.ImmutableSet;

import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;
import static icfp2013.generator.syntax.Operators.IF0;
import static java.lang.String.format;

/**
 * @author <a href="mailto:jonas.wagner@life.uni-leipzig.de">Jonas Wagner</a>
 */
public class If implements Expression {
    public final Expression condition, then, elseExp;

    @Override
    public int hashCode() {
        int result = condition.hashCode();
        result = 31 * result + then.hashCode();
        result = 31 * result + elseExp.hashCode();
        return result;
    }

    @Override
    public Set<Operators> op() {
        return ImmutableSet.<Operators>builder()
                .add(IF0).addAll(condition.op()).addAll(then.op()).addAll(elseExp.op()).build();
    }

    @Override
    public int size() {
        return 1 + condition.size() + then.size() + elseExp.size();
    }

    public If(Expression condition, Expression then, Expression elseExp) {
        this.condition = checkNotNull(condition);
        this.then = checkNotNull(then);
        this.elseExp = checkNotNull(elseExp);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        If anIf = (If) o;

        if (!condition.equals(anIf.condition)) return false;
        if (!elseExp.equals(anIf.elseExp)) return false;
        if (!then.equals(anIf.then)) return false;

        return true;
    }

    @Override
    public String toString() {
        return format("(if0 %s %s %s)", condition, then, elseExp);
    }
}
