package com.pi4j.registry;

import com.pi4j.io.IO;
import com.pi4j.io.exception.IOAlreadyExistsException;
import com.pi4j.io.exception.IOInvalidIDException;

/**
 * A mutable registry explicitly allows for dynamic registration and unregistration of {@link IO} instances.
 * <p>
 * The core API does not expose a mutable registry instance, encouraging users to treat the runtime as immutable.
 * <p>
 * In exceptional cases, runtime mutations of the registry may be required. Providing this abstraction
 * allows extensions of the framework to either implement this functionality or access existing functionality
 * by explicitly casting a compatible instance.
 */
public interface MutableRegistry extends Registry {

    /**
     * Add an {@link IO} instance to be managed by the registry.
     * @param instance the IO instance to register
     * @throws IOInvalidIDException if the IO instance has an invalid ID
     * @throws IOAlreadyExistsException if an IO instance with the same ID already exists
     */
    void register(IO instance) throws IOInvalidIDException, IOAlreadyExistsException;

    /**
     * Remove an {@link IO} instance from the registry by its ID.
     * @param id the ID of the IO instance to unregister
     * @throws IOInvalidIDException if the ID is invalid or does not exist
     */
    void unregister(String id) throws IOInvalidIDException;
}
