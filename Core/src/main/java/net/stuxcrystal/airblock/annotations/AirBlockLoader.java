package net.stuxcrystal.airblock.annotations;


import java.lang.annotation.*;

/**
 * The basic interface for the AirBlock-Loader interface.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface AirBlockLoader {

    /**
     * Contains the entry-point class.
     * @return The entry-point class.
     */
    public Class<?> value();

    /**
     * Contains the components.
     * @return The components.
     */
    public ComponentList[] components() default {};

    /**
     * Contains all argument types that should be supported additionally to the default types.
     * @return All argument types that should be supported additionally to the default types.
     */
    public Class<?>[] argumentTypes() default {};

}
