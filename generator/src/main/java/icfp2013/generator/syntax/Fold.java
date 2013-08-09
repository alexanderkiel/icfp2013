package icfp2013.generator.syntax;

import com.google.common.collect.ImmutableSet;

import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;
import static icfp2013.generator.syntax.Operators.FOLD;
import static java.lang.String.format;

/**
 * @author <a href="mailto:jonas.wagner@life.uni-leipzig.de">Jonas Wagner</a>
 */
public class Fold implements Expression {

    public final Expression vector, start;
    public final Variable x, y;
    public final Expression e;

    public Fold(Expression vector, Expression start, Variable x, Variable y, Expression e) {
        this.vector = checkNotNull(vector);
        this.start = checkNotNull(start);
        this.x = checkNotNull(x);
        this.y = checkNotNull(y);
        this.e = checkNotNull(e);
    }

    @Override
    public Set<Operators> op() {
        return ImmutableSet.<Operators>builder()
                .add(FOLD).addAll(vector.op()).addAll(start.op()).addAll(e.op()).build();
    }

    @Override
    public int size() {
        return 2 + vector.size() + start.size() + e.size();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Fold fold = (Fold) o;

        if (!e.equals(fold.e)) return false;
        if (!start.equals(fold.start)) return false;
        if (!vector.equals(fold.vector)) return false;
        if (!x.equals(fold.x)) return false;
        if (!y.equals(fold.y)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = vector.hashCode();
        result = 31 * result + start.hashCode();
        result = 31 * result + x.hashCode();
        result = 31 * result + y.hashCode();
        result = 31 * result + e.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return format("(fold %s %s (lambda (%s %s) %s))",
                vector.toString(), start.toString(), x.toString(), y.toString(), e.toString());
    }
}
