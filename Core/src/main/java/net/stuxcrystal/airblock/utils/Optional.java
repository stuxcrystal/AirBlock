package net.stuxcrystal.airblock.utils;

import org.apache.commons.lang3.ObjectUtils;

import java.util.Objects;

/**
 * Represents an object which may be null.
 * @param <T>
 */
public class Optional<T> {

    /**
     * Implementation of an empty optional value.
     * @param <T> The empty optional value.
     */
    private static class EmptyOptional<T> extends Optional<T> {

        /**
         * Creates a new optional value.
         */
        private EmptyOptional() {
            super();
        }

        /**
         * Checks if the other object is also an empty optional value.
         * @param other The other value.
         * @return The returned value.
         */
        @Override
        public boolean equals(Object other) {
            return other instanceof EmptyOptional;
        }

        /**
         * Returns the string representation of the optional value.
         * @return The string representation of the optional value.
         */
        @Override
        public String toString() {
            return "<Optional Empty>";
        }

        /**
         * Returns 0 as hashcode.
         * @return 0.
         */
        @Override
        public int hashCode() {
            return 0;
        }
    }

    /**
     * Contains an optional object with a value.
     * @param <T> The optional object with a value.
     */
    private static class ValueOptional<T> extends Optional<T> {

        /**
         * Returns the value of the optional object.
         */
        protected final T value;

        /**
         * Creates a new optional value.
         *
         * @param value The optional value.
         */
        private ValueOptional(T value) {
            super();
            this.value = value;
        }

        /**
         * Checks if the two optional values are equal.
         * @param other The other value.
         * @return {@code true} if they are equal.
         */
        @Override
        public boolean equals(Object other) {
            // We use the deprecated equals function as we use Java 6.
            return other instanceof ValueOptional && (
                    ObjectUtils.equals(this.value, ((ValueOptional) other).value)
            );
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(this.value) ^ ValueOptional.class.hashCode()*31;
        }

        /**
         * Returns the string representation of the optional value.
         * @return The string representation.
         */
        @Override
        public String toString() {
            return "<Optional " + this.value.toString() + ">";
        }
    }

    /**
     * Force the call of the default constructor.
     */
    @SuppressWarnings("unused")
    private final Object forceCall;

    /**
     * The the default constructor.
     */
    private Optional() {
        this.forceCall = null;
    }

    /**
     * Returns the empty optional value.
     */
    public static Optional<?> EMPTY_OPTIONAL = new EmptyOptional<Object>();

    /**
     * Checks if this is an empty optional object.
     * @param value The value
     * @param <T>   The type of the value.
     * @return {@code true} if this optional object is empty.
     */
    public static <T> boolean isEmpty(Optional<T> value) {
        return value instanceof EmptyOptional;
    }

    /**
     * Creates a new empty optional object.
     * @param <T> The type of the optional object.
     * @return The new optional object.
     */
    @SuppressWarnings("unchecked")
    public static <T> Optional<T> empty() {
        return (Optional<T>) Optional.EMPTY_OPTIONAL;
    }

    /**
     * Creates a new optional object.
     * @param value The optional object.
     * @param <T>   The type of the optional object.
     * @return The optional object.
     */
    public static <T> Optional<T> from(T value) {
        return new ValueOptional<T>(value);
    }

    /**
     * Returns the optional value.
     * @param value The optional value.
     * @param <T>   The type of the value.
     * @return The optional value.
     */
    public static <T> T get(Optional<T> value) {
        if (Optional.isEmpty(value))
            throw new IllegalArgumentException("Empty value");
        return ((ValueOptional<T>)value).value;
    }
}
