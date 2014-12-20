package net.stuxcrystal.airblock.commands.contrib.annotations.injections;

import java.lang.annotation.*;

/**
 * Parses an argument.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface Argument {

    /**
     * The actual type of the argument.
     * @return The actual type.
     */
    public Class<?> type() default Void.class;


    public int value();

    /**
     * If we want to use lists.
     * @return The range value.
     */
    public int to() default Integer.MIN_VALUE;

    /**
     * In which step should every step be shown.
     * @return The step.
     */
    public int step() default 0;

}
