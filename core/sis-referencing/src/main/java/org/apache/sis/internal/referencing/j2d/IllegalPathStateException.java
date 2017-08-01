package org.apache.sis.geometry;

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
