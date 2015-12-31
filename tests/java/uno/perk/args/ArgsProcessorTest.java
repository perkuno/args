package uno.perk.args;

import java.io.File;
import java.util.List;

import org.junit.Test;

import uno.perk.args.Options.Arguments;
import uno.perk.args.Options.Option;

public class ArgsProcessorTest {

  public static class Thing {
    /**
     * This is some {@code ArgsProcessorTest} class doc.
     */
    @Options
    interface Config {
      default String url() {
        return "http://localhost:8080";
      }

      /**
       * The number of threads to use.
       *
       * If unspecified,
       * 1 {@link java.lang.Thread} is used.
       * <ul>
       *   <li>unspecified: 1</li>
       *   <li>otherwise: N</li>
       * </ul>
       *
       * @return The number of threads to use.
       */
      @Option("t")
      int threads();

      /**
       * @return The configured file paths.
       */
      @Arguments
      List<File> paths();
    }

    public Thing(Config config) {
      this(config.url(), config.paths(), config.threads());
    }

    public Thing(String url, List<File> paths, int threads) {
      // TODO(John Sirois): XXX
    }
  }

  @Test
  public void test() {
    // TODO(John Sirois): Add real tests.

    // TODO(John Sirois): Support plain old bean/builder.
    Thing.Config configPlain = null;

    // TODO(John Sirois): Support creation from env/config files/command line arguments.
    Thing.Config configSources = null;
  }
}
