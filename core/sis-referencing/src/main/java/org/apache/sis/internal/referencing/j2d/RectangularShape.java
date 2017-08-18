package org.apache.sis.internal.referencing.j2d;

import android.graphics.RectF;

/**
 * {@link RectF} wrapper class.
 */
public abstract class RectangularShape extends RectF implements Shape {

    /**
     * Instantiates a new rectangular shape.
     */
    protected RectangularShape() {
    }

    public RectangularShape(float x, float y, float width, float height) {
        super(x, y + height, x+width, y);
    }

    /**
     * Gets the x coordinate of the upper left corner of the rectangle.
     *
     * @return the x coordinate of the upper left corner of the rectangle.
     */
    public abstract double getX();

    /**
     * Gets the y coordinate of the upper left corner of the rectangle.
     *
     * @return the y coordinate of the upper left corner of the rectangle.
     */
    public abstract double getY();

    /**
     * Gets the width of the rectangle.
     *
     * @return the width of the rectangle.
     */
    public abstract double getWidth();

    /**
     * Gets the height of the rectangle.
     *
     * @return the height of the rectangle.
     */
    public abstract double getHeight();

    /**
     * Sets the data for the bounding rectangle in terms of double values.
     *
     * @param x the x coordinate of the upper left corner of the rectangle.
     * @param y the y coordinate of the upper left corner of the rectangle.
     * @param w the width of the rectangle.
     * @param h the height of the rectangle.
     */
    public abstract void setFrame(double x, double y, double w, double h);

    /**
     * Gets the minimum x value of the bounding rectangle (the x coordinate of
     * the upper left corner of the rectangle).
     *
     * @return the minimum x value of the bounding rectangle.
     */
    public double getMinX() {
        return getX();
    }

    /**
     * Gets the minimum y value of the bounding rectangle (the y coordinate of
     * the upper left corner of the rectangle).
     *
     * @return the minimum y value of the bounding rectangle.
     */
    public double getMinY() {
        return getY();
    }

    /**
     * Gets the maximum x value of the bounding rectangle (the x coordinate of
     * the upper left corner of the rectangle plus the rectangle's width).
     *
     * @return the maximum x value of the bounding rectangle.
     */
    public double getMaxX() {
        return getX() + getWidth();
    }

    /**
     * Gets the maximum y value of the bounding rectangle (the y coordinate of
     * the upper left corner of the rectangle plus the rectangle's height).
     *
     * @return the maximum y value of the bounding rectangle.
     */
    public double getMaxY() {
        return getY() + getHeight();
    }

    /**
     * Gets the x coordinate of the center of the rectangle.
     *
     * @return the x coordinate of the center of the rectangle.
     */
    public double getCenterX() {
        return getX() + getWidth() / 2.0;
    }

    /**
     * Gets the y coordinate of the center of the rectangle.
     *
     * @return the y coordinate of the center of the rectangle.
     */
    public double getCenterY() {
        return getY() + getHeight() / 2.0;
    }

    /**
     * Places the rectangle's size and location data in a new Rectangle2D object
     * and returns it.
     *
     * @return the bounding rectangle as a new Rectangle2D object.
     */
    public Rectangle2D getFrame() {
            return new Rectangle.Double(getX(), getY(), getWidth(), getHeight());
    }

    /**
     * Sets the bounding rectangle to match the data contained in the specified
     * Rectangle2D.
     *
     * @param r the rectangle that gives the new frame data.
     */
    public void setFrame(Rectangle2D r) {
        setFrame(r.getX(), r.getY(), r.getWidth(), r.getHeight());
    }

    /**
     * Sets the framing rectangle given the center point and one corner. Any
     * corner may be used.
     *
     * @param centerX the x coordinate of the center point.
     * @param centerY the y coordinate of the center point.
     * @param cornerX the x coordinate of one of the corner points.
     * @param cornerY the y coordinate of one of the corner points.
     */
    public void setFrameFromCenter(double centerX, double centerY, double cornerX, double cornerY) {
        double width = Math.abs(cornerX - centerX);
        double height = Math.abs(cornerY - centerY);
        setFrame(centerX - width, centerY - height, width * 2.0, height * 2.0);
    }

    /**
     * Sets the framing rectangle given the center point and one corner. Any
     * corner may be used.
     *
     * @param center the center point.
     * @param corner a corner point.
     */
    public void setFrameFromCenter(Point2D center, Point2D corner) {
        setFrameFromCenter(center.getX(), center.getY(), corner.getX(), corner.getY());
    }

    public boolean contains(float x, float y) {
        return left <= x && x < right && bottom <= y && y < top;
    }

    public boolean contains(Point2D point) {
        return contains((float) point.getX(), (float) point.getY());
    }

    public boolean intersects(Rectangle2D rect) {
        return intersects((float) rect.getX(), (float) rect.getY(), (float) rect.getWidth(), (float) rect.getHeight());
    }

    public boolean contains(Rectangle2D rect) {
        return contains((float) rect.getX(), (float) rect.getY(), (float) rect.getWidth(), (float) rect.getHeight());
    }

    public Rectangle getBounds() {
        int x1 = (int)Math.floor(getMinX());
        int y1 = (int)Math.floor(getMinY());
        int x2 = (int)Math.ceil(getMaxX());
        int y2 = (int)Math.ceil(getMaxY());
        return new Rectangle(x1, y1, x2 - x1, y2 - y1);
    }

//    public PathIterator getPathIterator(AffineTransform t, double flatness) {
//        return new FlatteningPathIterator(getPathIterator(t), flatness);
//    }
}
