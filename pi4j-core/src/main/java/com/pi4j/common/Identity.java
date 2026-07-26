package com.pi4j.common;

/**
 * Common identity contract for Pi4J components, exposing a unique id, a name, and a description.
 * Extends {@link Describable} so any identifiable component can also produce a {@link Descriptor} of itself.
 */
public interface Identity extends Describable {
    /**
     * Returns the unique identifier of this component.
     *
     * @return the component's identifier
     */
    String id();

    /**
     * Returns the human-readable name of this component.
     *
     * @return the component's name
     */
    String name();

    /**
     * Returns the description of this component.
     *
     * @return the component's description
     */
    String description();

    /**
     * Bean-style alias for {@link #id()}.
     *
     * @return the component's identifier
     */
    default String getId() {
        return id();
    }

    /**
     * Bean-style alias for {@link #name()}.
     *
     * @return the component's name
     */
    default String getName() {
        return name();
    }

    /**
     * Bean-style alias for {@link #description()}.
     *
     * @return the component's description
     */
    default String getDescription() {
        return description();
    }

    @Override
    default Descriptor describe() {
        return Descriptor.create()
            .id(id())
            .name(name())
            .description(description());
    }
}
