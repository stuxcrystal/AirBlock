package net.stuxcrystal.airblock.commands.core.components;

import java.lang.annotation.*;

/**
 * Implements a component
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface Components {

    /**
     * The interfaces that this component is implementing.
     */
    public Class<?>[] value();

}
