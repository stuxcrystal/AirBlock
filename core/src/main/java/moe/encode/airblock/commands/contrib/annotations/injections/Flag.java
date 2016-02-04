package moe.encode.airblock.commands.contrib.annotations.injections;

import java.lang.annotation.*;

/**
 * Add support for flags.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface Flag {

    /**
     * The flag that should be checked.
     * @return The flag.
     */
    public char flag() ;

}
