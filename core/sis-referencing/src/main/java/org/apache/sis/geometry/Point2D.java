package org.apache.sis.geometry;

/**
 * Point2D impl. This should be used instead of Point2D from {@code java.awt}
 * when working with {@code org.apache.sis.geometry} classes.
 */
public class Point2D {

    public double x, y;

    public Point2D() {}

    public Point2D(final double x, final double y) {
        this.x      = x;
        this.y      = y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    /**
     * Sets this coordinate to the specified point.
     * @param position  the new position for this point.
     */
    public void setLocation(Point2D position) {
        this.x = position.getX();
        this.y = position.getY();
    }

    public Point2D clone() {
        return this.clone();
    }

    /**
     * Point2D.Double impl. This should be used instead of Point2D.Double from
     * {@code java.awt} when working with {@code org.apache.sis.geometry} classes.
     */
    public static class Double extends Point2D {

        public Double() {}

        public Double(final double x, final double y) {
            super(x, y);
        }

    }
}
