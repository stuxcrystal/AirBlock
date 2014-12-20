package net.stuxcrystal.airblock.commands.contrib.annotations.injections;

import java.lang.annotation.*;

/**
 * <p>Loads the component.</p>
 * <p>
 *     If we didn't retrieve an object before the framework will use a
 *     backend component.
 * </p>
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface Component {

    /**
     * The type of the component.
     * @return The component.
     */
    public Class<?> value();

}
