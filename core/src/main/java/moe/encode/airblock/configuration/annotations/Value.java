package moe.encode.airblock.configuration.annotations;

import java.lang.annotation.*;

/**
 * Contains metadata about the value object.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Value {

    /**
     * The name of the field.
     * @return The name of the field.
     */
    public String name() default "";

    /**
     * Contains the comment.
     * @return The comment.
     */
    public String[] comment() default {};

}
