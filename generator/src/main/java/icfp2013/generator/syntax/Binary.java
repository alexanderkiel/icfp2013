package icfp2013.generator.syntax;

import com.google.common.collect.ImmutableSet;

import java.util.Set;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static java.lang.String.format;

/**
 * @author <a href="mailto:jonas.wagner@life.uni-leipzig.de">Jonas Wagner</a>
 */
public class Binary implements Operator {
    public final Expression e1, e2;

    private final Operators op;

    public Binary(Operators op, Expression e1, Expression e2) {
        checkArgument(op.operatorSize == 2);
        this.e1 = checkNotNull(e1);
        this.e2 = checkNotNull(e2);
        this.op = checkNotNull(op);
    }

    @Override
    public Set<Operators> op() {
        return ImmutableSet.<Operators>builder().add(op).addAll(e1.op()).addAll(e2.op()).build();
    }

    @Override
    public int size() {
        return 1 + e1.size() + e2.size();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Binary binary = (Binary) o;

        if (!e1.equals(binary.e1)) return false;
        if (!e2.equals(binary.e2)) return false;
        if (!op.equals(binary.op)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = e1.hashCode();
        result = 31 * result + e2.hashCode();
        result = 31 * result + op.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return format("(%s %s %s)", op.toString().toLowerCase(), e1.toString(), e2.toString());
    }
}
