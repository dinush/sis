package org.apache.sis.internal.referencing.j2d;

/**
 * Replacement class for {@code java.awt.geom.IllegalPathStateException}
 */
public class IllegalPathStateException extends RuntimeException {

    public IllegalPathStateException() {
    }
    public IllegalPathStateException(String s) {
        super (s);
    }
}
