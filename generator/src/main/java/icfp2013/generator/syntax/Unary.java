package icfp2013.generator.syntax;

import com.google.common.collect.ImmutableSet;

import java.util.Set;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static java.lang.String.format;

/**
 * @author <a href="mailto:jonas.wagner@life.uni-leipzig.de">Jonas Wagner</a>
 */
public class Unary implements Operator {
    public final Expression e;

    private final Operators op;

    public Unary(Operators op, Expression e) {
        checkArgument(op.operatorSize == 1);
        this.e = checkNotNull(e);
        this.op = checkNotNull(op);
    }

    @Override
    public Set<Operators> op() {
        return ImmutableSet.<Operators>builder().add(op).addAll(e.op()).build();
    }

    @Override
    public int size() {
        return 1 + e.size();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Unary unary = (Unary) o;

        if (!e.equals(unary.e)) return false;
        if (!op.equals(unary.op)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = e.hashCode();
        result = 31 * result + op.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return format("(%s %s)", op.toString().toLowerCase(), e.toString());
    }
}
