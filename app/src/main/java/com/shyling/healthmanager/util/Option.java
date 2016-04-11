package com.shyling.healthmanager.util;

/**
 * Option monad
 *
 * @author shyling
 */
public class Option<T> {
    T save;

    private Option(T t) {
        save = t;
    }

    public T get() {
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
    public static <U> Option<U> of(U from) {
        return new Option<>(from);
    }

    /*
    M (M a) -> M a
    */
    public static <U> Option<U> of(Option<U> from) {
        if (from == null) {
            return new Option<>(null);
        } else {
            return Option.of(from.get());
        }
    }

}
