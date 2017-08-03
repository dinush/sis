package org.apache.sis.internal.referencing.j2d;

import java.util.NoSuchElementException;

/**
 * Rectangle2D impl. This should be used instead of Rectangle2D from {@code java.awt}
 * when working with {@code org.apache.sis.geometry} classes.
 */
public abstract class Rectangle2D extends RectangularShape {

    /**
     * The bitmask that indicates that a point lies to the left of
     * this <code>Rectangle2D</code>.
     */
    public static final int OUT_LEFT = 1;
    /**
     * The bitmask that indicates that a point lies above
     * this <code>Rectangle2D</code>.
     */
    public static final int OUT_TOP = 2;

    /**
     * The bitmask that indicates that a point lies to the right of
     * this <code>Rectangle2D</code>.
     */
    public static final int OUT_RIGHT = 4;

    /**
     * The bitmask that indicates that a point lies below
     * this <code>Rectangle2D</code>.
     */
    public static final int OUT_BOTTOM = 8;

    public Rectangle2D() {}

    public Rectangle2D clone() {
        return this.clone();
    }

    /**
     * Sets this rectangle to the given rectangle.
     * @param rect  the rectangle to copy coordinates from.
     */
    public void setRect(Rectangle2D rect) {
        setRect(rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight());
    }

    public abstract void setRect(double x, double y, double width, double height);

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
     * Determines whether any part of the line segment between (and including)
     * the two given points touches any part of the rectangle, including its
     * boundary.
     * @param x1 the x coordinate of one of the points that determines the line
     *            segment to test.
     * @param y1 the y coordinate of one of the points that determines the line
     *            segment to test.
     * @param x2 the x coordinate of one of the points that determines the line
     *            segment to test.
     * @param y2 the y coordinate of one of the points that determines the line
     *            segment to test.
     * @return true, if at least one point of the line segment between the two
     *         points matches any point of the interior of the rectangle or the
     *         rectangle's boundary.
     */
    public boolean intersectsLine(double x1, double y1, double x2, double y2) {
        double rx1 = getX();
        double ry1 = getY();
        double rx2 = rx1 + getWidth();
        double ry2 = ry1 + getHeight();
        return (rx1 <= x1 && x1 <= rx2 && ry1 <= y1 && y1 <= ry2)
                || (rx1 <= x2 && x2 <= rx2 && ry1 <= y2 && y2 <= ry2)
                || Line2D.linesIntersect(rx1, ry1, rx2, ry2, x1, y1, x2, y2)
                || Line2D.linesIntersect(rx2, ry1, rx1, ry2, x1, y1, x2, y2);
    }

    public Rectangle2D getBounds2D() {
        return (Rectangle2D)clone();
    }

    public PathIterator getPathIterator(AffineTransform t) {
        return new Iterator(this, t);
    }

    /**
     * Rectangle2D.Double impl. This should be used instead of Rectangle2D.Double from
     * {@code java.awt} when working with {@code org.apache.sis.geometry} classes.
     */
    public static class Double extends Rectangle2D {

        public double x, y, width, height;

        public Double() {}

        public Double(final double x, final double y, final double width, final double height) {
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

    }

    /**
     * Rectangle2D.Float impl. This should be used instead of Rectangle2D.Float from
     * {@code java.awt} when working with {@code org.apache.sis.geometry} classes.
     */
    public static class Float extends Rectangle2D {
        public float x, y, width, height;

        public Float() {}

        public Float(final float x, final float y, final float width, final float height) {
            this.x      = x;
            this.y      = y;
            this.width  = width;
            this.height = height;
        }

        public double getX() {
            return (double) x;
        }

        public double getY() {
            return (double) y;
        }

        public double getWidth() {
            return (double) width;
        }

        public double getHeight() {
            return (double) height;
        }

        /**
         * Determines whether this rectangle is empty.
         * @return {@code true} if this rectangle is empty; {@code false} otherwise.
         */
        public boolean isEmpty() {
            return (this.width <= 0.0f) || (this.height <= 0.0f);
        }


        /**
         * Sets this rectangle to the given coordinates.
         * @param x         the x coordinate.
         * @param y         the y coordinate.
         * @param width     the width.
         * @param height    the height.
         */
        public void setRect(double x, double y, double width, double height) {
            this.x      = (float) x;
            this.y      = (float) y;
            this.width  = (float) width;
            this.height = (float) height;
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
    }

    /**
     * The Class Iterator provides access to the coordinates of the
     * Rectangle2D's boundary modified by an AffineTransform.
     */
    class Iterator implements PathIterator {

        /**
         * The x coordinate of the rectangle's upper left corner.
         */
        double x;

        /**
         * The y coordinate of the rectangle's upper left corner.
         */
        double y;

        /**
         * The width of the rectangle.
         */
        double width;

        /**
         * The height of the rectangle.
         */
        double height;

        /**
         * The AffineTransform that is used to modify the coordinates that are
         * returned by the path iterator.
         */
        AffineTransform t;

        /**
         * The current segment index.
         */
        int index;

        /**
         * Constructs a new Rectangle2D.Iterator for given rectangle and
         * transformation.
         *
         * @param r the source Rectangle2D object.
         * @param at the AffineTransform object to apply to the coordinates
         *            before returning them.
         */
        Iterator(Rectangle2D r, AffineTransform at) {
            this.x = r.getX();
            this.y = r.getY();
            this.width = r.getWidth();
            this.height = r.getHeight();
            this.t = at;
            if (width < 0.0 || height < 0.0) {
                index = 6;
            }
        }

        public int getWindingRule() {
            return WIND_NON_ZERO;
        }

        public boolean isDone() {
            return index > 5;
        }

        public void next() {
            index++;
        }

        public int currentSegment(double[] coords) {
            if (isDone()) {
                throw new NoSuchElementException();
            }
            if (index == 5) {
                return SEG_CLOSE;
            }
            int type;
            if (index == 0) {
                type = SEG_MOVETO;
                coords[0] = x;
                coords[1] = y;
            } else {
                type = SEG_LINETO;
                switch (index) {
                    case 1:
                        coords[0] = x + width;
                        coords[1] = y;
                        break;
                    case 2:
                        coords[0] = x + width;
                        coords[1] = y + height;
                        break;
                    case 3:
                        coords[0] = x;
                        coords[1] = y + height;
                        break;
                    case 4:
                        coords[0] = x;
                        coords[1] = y;
                        break;
                }
            }
            if (t != null) {
                t.transform(coords, 0, coords, 0, 1);
            }
            return type;
        }

        public int currentSegment(float[] coords) {
            if (isDone()) {
                throw new NoSuchElementException();
            }
            if (index == 5) {
                return SEG_CLOSE;
            }
            int type;
            if (index == 0) {
                coords[0] = (float)x;
                coords[1] = (float)y;
                type = SEG_MOVETO;
            } else {
                type = SEG_LINETO;
                switch (index) {
                    case 1:
                        coords[0] = (float)(x + width);
                        coords[1] = (float)y;
                        break;
                    case 2:
                        coords[0] = (float)(x + width);
                        coords[1] = (float)(y + height);
                        break;
                    case 3:
                        coords[0] = (float)x;
                        coords[1] = (float)(y + height);
                        break;
                    case 4:
                        coords[0] = (float)x;
                        coords[1] = (float)y;
                        break;
                }
            }
            if (t != null) {
                t.transform(coords, 0, coords, 0, 1);
            }
            return type;
        }

    }
}
