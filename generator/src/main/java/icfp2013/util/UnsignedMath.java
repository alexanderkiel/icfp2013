package icfp2013.util;

/**
 * @author <a href="mailto:alexander.kiel@life.uni-leipzig.de">Alexander Kiel</a>
 */
public class UnsignedMath {

    public static long bitShiftRight(long x, int n) {
        return x >>> n;
    }

    public static long plus(long x, long y) {
        return x + y;
    }
}
