package net.stuxcrystal.airblock.commands.contrib.locales;

import net.stuxcrystal.airblock.commands.backend.ExecutorHandle;
import net.stuxcrystal.airblock.commands.core.components.Component;
import net.stuxcrystal.airblock.commands.core.components.Components;

import java.util.Locale;

/**
 * Implementation for containers.
 */
@Components(LocaleResolver.class)
public class SystemDefaultLocaleResolver {

    /**
     * Returns the default locale of the system.
     * @param executor The executor of the resolver.
     * @return The locale of the system.
     */
    @Component(thread = Component.ExecutionThread.SAME_THREAD, strategy = Component.ExecutionStrategy.NONE)
    public Locale getLocale(ExecutorHandle executor) {
        return Locale.getDefault();
    }

}
