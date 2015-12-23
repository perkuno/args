package uno.perk.args;

import java.io.File;
import java.util.List;

import org.junit.Test;

public class ArgsProcessorTest {

  /**
   * This is some {@code ArgsProcessorTest} class doc.
   */
  @Optionable
  interface Options {
    default String url() {
      return "http://localhost:8080";
    }

    /**
     * The number of threads to use.
     *
     * If unspecified,
     * 1 thread is used.
     *
     * @return The number of threads to use.
     */
    int threads();

    /**
     * @return The configured file paths.
     */
    List<File> paths();
  }

  @Test
  public void test() {
    // TODO(John Sirois): Add real tests.
  }
}
