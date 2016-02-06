package moe.encode.airblock.commands.core.services;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Manager for services.<p />
 *
 * Used for registering singletons.
 */
public class ServiceManager {

    /**
     * Service support.
     */
    private final Map<Class<?>, Object> services = Collections.synchronizedMap(new HashMap<Class<?>, Object>());

    /**
     * Retrieves a service.
     *
     * @param cls  The class.
     * @param <T>  The type.
     * @return  The service.
     */
    @SuppressWarnings("unchecked")
    public <T> T getService(Class<T> cls) {
        return (T) this.services.get(cls);
    }

    /**
     * Registers a service to the service manager.
     *
     * @param cls               The class.
     * @param implementation    The implementation.
     * @param <T> The class.
     */
    public <T> void registerService(Class<? super T> cls, T implementation) {
        this.services.put(cls, implementation);
    }

    /**
     * <strong>Do not use unless you know what you're doing.</strong>
     *
     * @param cls               The class.
     * @param implementation    The implementation.
     */
    public void registerServiceRaw(Class<?> cls, Object implementation) {
        this.services.put(cls, implementation);
    }

    /**
     * Removes a service from the manager.
     *
     * @param cls    The class.
     * @param <T>    The type.
     */
    public <T> void unregisterService(Class<T> cls) {
        this.services.remove(cls);
    }

}
