package net.stuxcrystal.airblock.annotations;

import net.stuxcrystal.airblock.commands.backend.Handle;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Lists components.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface ComponentList {

    /**
     * Contains the component type that should be attached.
     * @return The component type that should be attached.
     */
    public Class<? extends Handle<?>> type();

    /**
     * Contains the component instances that should be registered.
     * @return The component instances that should be registered.
     */
    public Class<?>[] components();

}
