package uno.perk.args;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks an interface as a source of program options.
 */
@Documented
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.TYPE)
public @interface Options {

  // TODO(John Sirois): add an option to mark entry-point doc?

  /**
   * Customises various aspects of an option's parsing and presentation.
   */
  @Documented
  @Retention(RetentionPolicy.SOURCE)
  @Target(ElementType.METHOD)
  @interface Option {

    /**
     * The names the option is exposed via.
     *
     * By default, the method name is used as the basis and transformed for the setting.  For
     * example, 'firstName' would be transformed to kebab-case for the command line and present as
     * '--first-name'.  It would be transformed to upper-snake-case for setting via environment
     * variables as '${APP_NAME}_FIRST_NAME', etc.
     */
    String[] value() default {};

    /**
     * The parser to use to parse this option's raw string value with.
     */
    Class<? extends Parser<?>> parser() default Parser.Default.class;
  }

  /**
   * Marks an option method as receiving the positional non-option arguments.
   */
  @Documented
  @Retention(RetentionPolicy.SOURCE)
  @Target(ElementType.METHOD)
  @interface Arguments {

    /**
     * The parser to use to parse the raw positional arguments with.
     */
    Class<? extends Parser<?>> parser() default Parser.Default.class;
  }
}
