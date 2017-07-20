package org.apache.sis.geometry;

/**
 * Rectangle2D impl. This should be used instead of Rectangle2D from {@code java.awt}
 * when working with {@code org.apache.sis.geometry} classes.
 */
public class Rectangle2D {

    public double x, y, width, height;

    public Rectangle2D() {}

    public Rectangle2D(final double x, final double y, final double width, final double height) {
        this.x      = x;
        this.y      = y;
        this.width  = width;
        this.height = height;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }

    public double getMinX() {
        return getX();
    }

    public double getMinY() {
        return getY();
    }

    public double getMaxX() {
        return getX() + getWidth();
    }

    public double getMaxY() {
        return getY() + getHeight();
    }

    public double getCenterX() {
        return getX() + getWidth() / 2.0;
    }

    public double getCenterY() {
        return getY() + getHeight() / 2.0;
    }

    /**
     * Sets this rectangle to the given rectangle.
     * @param rect  the rectangle to copy coordinates from.
     */
    public void setRect(Rectangle2D rect) {
        this.x      = rect.getX();
        this.y      = rect.getY();
        this.width  = rect.getWidth();
        this.height = rect.getHeight();
    }

    /**
     * Tests if a specified coordinate is inside the boundary of this rectangle. If it least one
     * of the given ordinate value is {@link java.lang.Double#NaN NaN}, then this method returns
     * {@code false}.
     * @param x         the <var>x</var> ordinate of the lower corner of the rectangle to test for inclusion.
     * @param y         the <var>y</var> ordinate of the lower corner of the rectangle to test for inclusion.
     * @param width     the width of the rectangle to test for inclusion. May be negative if the rectangle spans the anti-meridian.
     * @param height    the height of the rectangle to test for inclusion. May be negative.
     * @return          {@code true} if this rectangle completely encloses the specified one.
     */
    public boolean contains(double x, double y, double width, double height) {
        if (java.lang.Double.isNaN(x) || java.lang.Double.isNaN(y) ||
                java.lang.Double.isNaN(width) || java.lang.Double.isNaN(height)) {
            return false;
        }
        return x >= this.x && y >= this.y &&
                x + width <= this.x + this.width &&
                y + height <= this.y + this.height;
    }

    /**
     * Tests if a specified coordinate is inside the boundary of this rectangle.
     * @param x the <var>x</var> ordinate of the point
     * @param y the <var>y</var> ordinate of the point
     * @return  {@code true} if this point inside the rectangle
     */
    public boolean contains(double x, double y) {
        return x >= this.x && y >= this.y && x < this.x + this.width && y < this.y + this.height;
    }

    /**
     * Tests if a specified coordinate is inside the boundary of this rectangle.
     * @param p the point to test for
     * @return  {@code true} if this point inside the rectangle
     */
    public boolean contains(Point2D p) {
        return contains(p.getX(), p.getY());
    }

    /**
     * Tests if a specified coordinate is inside the boundary of this rectangle. If it least one
     * of the given ordinate value is {@link java.lang.Double#NaN NaN}, then this method returns
     * {@code false}.
     * @param rect  the rectangle to test for intersection.
     * @return      {@code true} if this rectangle completely encloses the specified one.
     */
    public boolean contains(Rectangle2D rect) {
        return contains(rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight());
    }

    /**
     * Returns {@code true} if this rectangle intersects the specified rectangle. If this rectangle
     * or the given rectangle have at least one {@link java.lang.Double#NaN NaN} value, then this
     * method returns {@code false}.
     * @param x         the <var>x</var> ordinate of the lower corner of the rectangle to test for inclusion.
     * @param y         the <var>y</var> ordinate of the lower corner of the rectangle to test for inclusion.
     * @param width     the width of the rectangle to test for inclusion. May be negative if the rectangle spans the anti-meridian.
     * @param height    the height of the rectangle to test for inclusion. May be negative.
     * @return          {@code true} if this rectangle intersects the specified rectangle.
     */
    public boolean intersects(double x, double y, double width, double height) {
        if (java.lang.Double.isNaN(x) || java.lang.Double.isNaN(y) ||
                java.lang.Double.isNaN(width) || java.lang.Double.isNaN(height)) {
            return false;
        }
        return x + width > this.x && y + height > this.y &&
                x < this.x + this.width && y < this.y + this.height;
    }

    /**
     * Returns {@code true} if this rectangle intersects the specified rectangle. If this rectangle
     * or the given rectangle have at least one {@link java.lang.Double#NaN NaN} value, then this
     * method returns {@code false}.
     * @param rect  the rectangle to test for intersection.
     * @return      {@code true} if this rectangle intersects the specified rectangle.
     */
    public boolean intersects(Rectangle2D rect) {
        return intersects(rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight());
    }

    public Rectangle2D clone() {
        return this.clone();
    }

    /**
     * Rectangle2D.Double impl. This should be used instead of Rectangle2D.Double from
     * {@code java.awt} when working with {@code org.apache.sis.geometry} classes.
     */
    public static class Double extends Rectangle2D {

        public Double() {}

        public Double(final double x, final double y, final double width, final double height) {
            super(x, y, width, height);
        }

    }
}
