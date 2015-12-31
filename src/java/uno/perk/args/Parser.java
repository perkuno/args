package uno.perk.args;

import java.util.function.Function;

/**
 * A function that can transform raw string representations to objects of a specified type.
 *
 * @param <T> The type parsed from raw strings.
 */
public interface Parser<T> extends Function<String, T> {

  /**
   * Represents the default parser.
   *
   * NB: The default parser is a marker only and is substituted with the actual default parser when
   * options are bound.
   */
  abstract class Default implements Parser<Object> {
    private Default() {
      // Non-instantiable marker type.
    }
  }
}
