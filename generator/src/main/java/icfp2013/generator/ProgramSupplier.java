package icfp2013.generator;

import com.google.common.base.Function;
import com.google.common.base.Supplier;
import icfp2013.generator.syntax.Program;

import java.util.Random;

/**
 * @author <a href="mailto:jonas.wagner@life.uni-leipzig.de">Jonas Wagner</a>
 */
public class ProgramSupplier implements Supplier<Program> {
    private final Random rng;
    private final Function<Random, Program> generator;

    public ProgramSupplier(Random rng, Function<Random, Program> generator) {
        this.rng = rng;
        this.generator = generator;
    }

    @Override
    public Program get() {
        return generator.apply(rng);
    }
}
