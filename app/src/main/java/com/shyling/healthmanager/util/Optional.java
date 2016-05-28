package com.shyling.healthmanager.util;

/**
 * Optional
 *
 * @author shyling
 */
public class Optional<T> {
    public final static Optional<?> Empty = Optional.of(null);
    T save;

    private Optional(T t) {
        save = t;
    }

    public T get() {
        if (save == null) {
            throw new RuntimeException("Empty Optional");
        }
        return save;
    }

    public T getOrElse(T el) {
        if (save == null) {
            return el;
        } else {
            return save;
        }
    }

    /*
    M a -> boolean
     */
    public boolean isPresent() {
        return save != null;
    }

    /*
    a -> M a
     */
    public static <U> Optional<U> of(U from) {
        return new Optional<>(from);
    }

    /*
    M (M a) -> M a
    */
    @SuppressWarnings("unchecked")
    public static <U> Optional<U> of(Optional<U> from) {
        if (from == null) {
            return (Optional<U>) Optional.Empty;
        } else {
            return Optional.of(from.get());
        }
    }

    public static boolean every(Optional... args) {
        for (Optional optional : args) {
            if (!optional.isPresent()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public String toString() {
        return String.format("Option[%s]", save);
    }

    @Override
    public int hashCode() {
        try {
            Class<?> clazz = Class.forName("java.util.Objects");
            return (Integer) clazz.getMethod("hashCode", Object.class).invoke(null, save);
        } catch (Exception e) {
            return save.hashCode();
        }
    }

    @Override
    public boolean equals(Optional other){
        //todo
        return false;
    }

}
