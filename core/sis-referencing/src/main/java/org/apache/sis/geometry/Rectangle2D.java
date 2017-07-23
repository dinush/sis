package org.apache.sis.geometry;

/**
 * Rectangle2D impl. This should be used instead of Rectangle2D from {@code java.awt}
 * when working with {@code org.apache.sis.geometry} classes.
 */
public class Rectangle2D {

    /**
     * The bitmask that indicates that a point lies to the left of
     * this <code>Rectangle2D</code>.
     */
    public static final int OUT_LEFT = 1;
    /**
     * The bitmask that indicates that a point lies above
     * this <code>Rectangle2D</code>.
     * @since 1.2
     */
    public static final int OUT_TOP = 2;

    /**
     * The bitmask that indicates that a point lies to the right of
     * this <code>Rectangle2D</code>.
     * @since 1.2
     */
    public static final int OUT_RIGHT = 4;

    /**
     * The bitmask that indicates that a point lies below
     * this <code>Rectangle2D</code>.
     * @since 1.2
     */
    public static final int OUT_BOTTOM = 8;

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
     * Determines whether this rectangle is empty.
     * @return {@code true} if this rectangle is empty; {@code false} otherwise.
     */
    public boolean isEmpty() {
        return (this.width <= 0.0f) || (this.height <= 0.0f);
    }

    /**
     * Adds a point, specified by the {@code newx} and {@code newy}, to this
     * {@code Rectangle2D}.  The resulting {@code Rectangle2D}
     * is the smallest {@code Rectangle2D} that
     * contains both the original {@code Rectangle2D} and the
     * specified point.
     * @param newx  newx the X coordinate of the new point
     * @param newy  newy the Y coordinate of the new point
     */
    public void add(double newx, double newy) {
        double x1 = Math.min(getMinX(), newx);
        double x2 = Math.max(getMaxX(), newx);
        double y1 = Math.min(getMinY(), newy);
        double y2 = Math.max(getMaxY(), newy);
        setRect(x1, y1, x2 - x1, y2 - y1);
    }

    /**
     * Adds a point, specified by the {@code p}, to this
     * {@code Rectangle2D}.  The resulting {@code Rectangle2D}
     * is the smallest {@code Rectangle2D} that
     * contains both the original {@code Rectangle2D} and the
     * specified point.
     * @param p new point to add.
     */
    public void add(Point2D p) {
        add(p.getX(), p.getY());
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
     * Sets this rectangle to the given coordinates.
     * @param x         the x coordinate.
     * @param y         the y coordinate.
     * @param width     the width.
     * @param height    the height.
     */
    public void setRect(double x, double y, double width, double height) {
        this.x      = x;
        this.y      = y;
        this.width  = width;
        this.height = height;
    }

    /**
     * Sets the coordinates of this rectangle to the specified values
     * @param x         the <var>x</var> ordinate of the lower left corner of the rectangle
     * @param y         the <var>y</var> ordinate of the lower left corner of the rectangle
     * @param width     the width of the rectangle
     * @param height    the height of the rectangle
     */
    public void setFrame(double x, double y, double width, double height) {
        setRect(x, y, width, height);
    }

    /**
     * Sets the framing rectangle to the given rectangle.
     * @param rect  rectangle to get the values from
     */
    public void setFrame(Rectangle2D rect) {
        setRect(rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight());
    }

    /**
     * Tests if a specified coordinate is inside the boundary of this rectangle. If it least one
     * of the given ordinate value is {@link java.lang.Double#NaN NaN}, then this method returns
     * {@code false}.
     * @param x         the <var>x</var> ordinate of the lower left corner of the rectangle to test for inclusion.
     * @param y         the <var>y</var> ordinate of the lower left corner of the rectangle to test for inclusion.
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
