package icfp2013.generator.syntax;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.lang.String.format;

/**
 * @author <a href="mailto:jonas.wagner@life.uni-leipzig.de">Jonas Wagner</a>
 */
class If implements Expression {
    public final Expression condition, then, elseE;

    If(Expression condition, Expression then, Expression elseE) {
        this.condition = checkNotNull(condition);
        this.then = checkNotNull(then);
        this.elseE = checkNotNull(elseE);
    }

    @Override
    public String toString() {
        return format("(if0 %s %s %s)", condition, then, elseE);
    }
}
