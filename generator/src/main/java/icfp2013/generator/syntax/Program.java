package icfp2013.generator.syntax;

import static java.lang.String.format;

/**
 * @author <a href="mailto:jonas.wagner@life.uni-leipzig.de">Jonas Wagner</a>
 */
public class Program {
    public final Variable x;
    public final Expression e;

    Program(Variable x, Expression e) {
        this.x = x;
        this.e = e;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Program program = (Program) o;

        if (!e.equals(program.e)) return false;
        if (!x.equals(program.x)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = x.hashCode();
        result = 31 * result + e.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return format("(lambda (%s) %s)", x.toString(), e.toString());
    }
}
