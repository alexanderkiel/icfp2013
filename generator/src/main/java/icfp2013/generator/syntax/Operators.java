package icfp2013.generator.syntax;

/**
 * @author <a href="mailto:jonas.wagner@life.uni-leipzig.de">Jonas Wagner</a>
 */
public enum Operators {
    NOT(1, 2, 1),
    SHL1(1, 2, 1),
    SHR1(1, 2, 1),
    SHR4(1, 2, 1),
    SHR16(1, 2, 1),

    AND(2, 3, 2),
    OR(2, 3, 2),
    XOR(2, 3, 2),
    PLUS(2, 3, 2),

    IF0(1, 4, 3),

    FOLD(2, 5, 4),
    TFOLD(2, 5, 1);

    public final int operatorSize;
    public final int minimumSize;
    public final int numArgs;

    Operators(int operatorSize, int minimumSize, int numArgs) {
        this.operatorSize = operatorSize;
        this.minimumSize = minimumSize;
        this.numArgs = numArgs;
    }
}
