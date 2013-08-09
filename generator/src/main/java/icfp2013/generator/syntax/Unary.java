package icfp2013.generator.syntax;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.lang.String.format;

/**
 * @author <a href="mailto:jonas.wagner@life.uni-leipzig.de">Jonas Wagner</a>
 */
class Unary implements Operator {
    public final Expression e;

    private final String name;

    protected Unary(String name, Expression e) {
        this.e = checkNotNull(e);
        this.name = checkNotNull(name);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Unary unary = (Unary) o;

        if (!e.equals(unary.e)) return false;
        if (!name.equals(unary.name)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = e.hashCode();
        result = 31 * result + name.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return format("(%s %s)", name, e.toString());
    }
}
