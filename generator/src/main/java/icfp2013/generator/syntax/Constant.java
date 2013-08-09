package icfp2013.generator.syntax;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @author <a href="mailto:jonas.wagner@life.uni-leipzig.de">Jonas Wagner</a>
 */
class Constant implements Expression {

    static final Constant C1 = new Constant("1");
    static final Constant C0 = new Constant("0");

    public final String value;

    Constant(String value) {
        this.value = checkNotNull(value);
    }

    @Override
    public String toString() {
        return value;
    }
}
