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

    /**
     * Sets this coordinate to the specified coordinates.
     * @param x new x coordinate.
     * @param y new y coordinate.
     */
    public void setLocation(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Returns the distance from this <code>Point2D</code> to
     * a specified point.
     * @param x the X coordinate of the specified point to be measured
     *           against this {@code Point2D}.
     * @param y the Y coordinate of the specified point to be measured
     *           against this {@code Point2D}.
     * @return  the distance between this {@code Point2D}.
     */
    public double distance(double x, double y) {
        x -= this.x;
        y -= this.y;
        return Math.sqrt(x * x + y * y);
    }

    /**
     * Returns the distance from this <code>Point2D</code> to
     * a specified point.
     * @param p the specified {@code Point2D} to be measured
     *           against this {@code Point2D}.
     * @return
     */
    public double distance(Point2D p) {
        return distance(p.getX(), p.getY());
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
