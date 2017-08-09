package org.apache.sis.internal.referencing.j2d;

public interface Shape {
    public PathIterator getPathIterator(AffineTransform at);

    public PathIterator getPathIterator(AffineTransform at, double flatness);

    public Rectangle2D getBounds2D();
}
