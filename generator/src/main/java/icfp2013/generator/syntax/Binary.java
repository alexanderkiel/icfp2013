package icfp2013.generator.syntax;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.lang.String.format;

/**
 * @author <a href="mailto:jonas.wagner@life.uni-leipzig.de">Jonas Wagner</a>
 */
class Binary implements Operator {
    public final Expression e1, e2;

    private final String name;

    protected Binary(String name, Expression e1, Expression e2) {
        this.e1 = checkNotNull(e1);
        this.e2 = checkNotNull(e2);
        this.name = checkNotNull(name);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Binary binary = (Binary) o;

        if (!e1.equals(binary.e1)) return false;
        if (!e2.equals(binary.e2)) return false;
        if (!name.equals(binary.name)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = e1.hashCode();
        result = 31 * result + e2.hashCode();
        result = 31 * result + name.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return format("(%s %s %s)", name, e1.toString(), e2.toString());
    }
}
