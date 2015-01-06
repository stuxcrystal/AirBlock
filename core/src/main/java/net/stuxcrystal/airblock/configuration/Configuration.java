package net.stuxcrystal.airblock.configuration;

import java.lang.annotation.*;

/**
 * Denotes the support for a configuration object.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Configuration {
}
