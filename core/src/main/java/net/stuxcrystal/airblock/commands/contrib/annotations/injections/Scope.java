package net.stuxcrystal.airblock.commands.contrib.annotations.injections;

import lombok.Data;
import lombok.Getter;
import lombok.NonNull;
import net.stuxcrystal.airblock.commands.Executor;
import net.stuxcrystal.airblock.commands.arguments.list.ArgumentList;
import net.stuxcrystal.airblock.commands.contrib.annotations.AnnotationCommand;

import java.lang.reflect.Method;

/**
 * Contains the scope of the current pass.
 */
@Data
public class Scope {

    /**
     * The current method.
     */
    @Getter
    @NonNull
    private final Method method;

    @Getter
    @NonNull
    private final Object instance;

    @Getter
    @NonNull
    private final AnnotationCommand command;

    @Getter
    @NonNull
    private final ArgumentList argumentList;

    @Getter
    @NonNull
    private final Executor executor;

}
