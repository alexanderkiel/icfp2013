package icfp2013.generator.syntax;

/**
 * @author <a href="mailto:jonas.wagner@life.uni-leipzig.de">Jonas Wagner</a>
 */
public enum Operators {
    NOT(1, 2), SHL1(1, 2), SHR1(1, 2), SHR4(1, 2), SHR16(1, 2),
    AND(2, 3), OR(2, 3), XOR(2, 3), PLUS(2, 3),
    IF0(1, 4),
    FOLD(2, 5), TFOLD(2, 5);

    public final int operatorSize;
    public final int minimumSize;

    Operators(int operatorSize, int minimumSize) {
        this.operatorSize = operatorSize;
        this.minimumSize = minimumSize;
    }
}
