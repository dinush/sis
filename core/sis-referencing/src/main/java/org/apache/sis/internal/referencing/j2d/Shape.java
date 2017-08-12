package org.apache.sis.internal.referencing.j2d;

public interface Shape {
    public PathIterator getPathIterator(AffineTransform at);

    public PathIterator getPathIterator(AffineTransform at, double flatness);

    public Rectangle getBounds();

    public Rectangle2D getBounds2D();

    public boolean intersects(double x, double y, double w, double h);

    public boolean intersects(Rectangle2D r);
}
