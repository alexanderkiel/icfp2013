package icfp2013.generator.syntax;

import java.util.Set;

/**
 * @author <a href="mailto:jonas.wagner@life.uni-leipzig.de">Jonas Wagner</a>
 */
public interface Expression {

    public Set<Operators> op();

    public int size();
}
