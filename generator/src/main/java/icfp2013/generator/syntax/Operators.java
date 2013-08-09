package icfp2013.generator.syntax;

/**
 * @author <a href="mailto:jonas.wagner@life.uni-leipzig.de">Jonas Wagner</a>
 */
public abstract class Operators {

    public static Operator not(Expression e) {
        return new Unary("not", e);
    }

    public static Operator shl1(Expression e) {
        return new Unary("shl1", e);
    }

    public static Operator shr1(Expression e) {
        return new Unary("shr1", e);
    }

    public static Operator shr4(Expression e) {
        return new Unary("shr4", e);
    }

    public static Operator shr16(Expression e) {
        return new Unary("shr16", e);
    }

    public static Operator and(Expression e1, Expression e2) {
        return new Binary("and", e1, e2);
    }

    public static Operator or(Expression e1, Expression e2) {
        return new Binary("or", e1, e2);
    }

    public static Operator xor(Expression e1, Expression e2) {
        return new Binary("xor", e1, e2);
    }

    public static Operator plus(Expression e1, Expression e2) {
        return new Binary("plus", e1, e2);
    }
}
