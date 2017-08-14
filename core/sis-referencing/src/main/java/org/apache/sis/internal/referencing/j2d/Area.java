package org.apache.sis.internal.referencing.j2d;

import java.util.NoSuchElementException;

public class Area implements Shape, Cloneable {

    /**
     * The source Shape object.
     */
    Shape s;

    /**
     * The Class NullIterator.
     */
    private static class NullIterator implements PathIterator {

        /**
         * Instantiates a new null iterator.
         */
        NullIterator() {
        }

        public int getWindingRule() {
            return WIND_NON_ZERO;
        }

        public boolean isDone() {
            return true;
        }

        public void next() {
            // nothing
        }

        public int currentSegment(double[] coords) {
            throw new NoSuchElementException("Iterator out of bounds");
        }

        public int currentSegment(float[] coords) {
            throw new NoSuchElementException("Iterator out of bounds");
        }

    }

    /**
     * New area with no data.
     */
    public Area() {
    }

    /**
     * New area with data.
     *
     * @param s the shape that gives the data for this Area.
     */
    public Area(Shape s) {
        if (s == null) {
            throw new NullPointerException();
        }
        this.s = s;
    }

    /**
     * Tests whether the object is equal to this Area.
     *
     * @param obj the object to compare.
     * @return true, if successful.
     * @throws NotImplementedException if this method is not implemented.
     */
    public boolean equals(Area obj) throws NotImplementedException {
        throw new RuntimeException("Not implemented");
    }

    public boolean intersects(double x, double y, double width, double height) {
        return s == null ? false : s.intersects(x, y, width, height);
    }

    public boolean intersects(Rectangle2D r) {
        if (r == null) {
            throw new NullPointerException();
        }
        return s == null ? false : s.intersects(r);
    }

    public Rectangle getBounds() {
        return s == null ? new Rectangle() : s.getBounds();
    }

    public Rectangle2D getBounds2D() {
        return s == null ? new Rectangle2D.Double() : s.getBounds2D();
    }

    public PathIterator getPathIterator(AffineTransform t) {
        return s == null ? new NullIterator() : s.getPathIterator(t);
    }

    public PathIterator getPathIterator(AffineTransform t, double flatness) {
        return s == null ? new NullIterator() : s.getPathIterator(t, flatness);
    }

    /**
     * Adds the specified area to this area.
     *
     * @param area
     *            the area to add to this area.
     * @throws NotImplementedException
     *             if this method is not implemented.
     */
    public void add(Area area) throws NotImplementedException {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Performs an exclusive or operation between this shape and the specified
     * shape.
     *
     * @param area the area to XOR against this area.
     * @throws NotImplementedException if this method is not implemented.
     */
    public void exclusiveOr(Area area) throws NotImplementedException {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Extracts a Rectangle2D from the source shape if the underlying shape data
     * describes a rectangle.
     *
     * @return a Rectangle2D object if the source shape is rectangle, or null if
     *         shape is empty or not rectangle.
     */
    Rectangle2D extractRectangle() {
        if (s == null) {
            return null;
        }
        float[] points = new float[12];
        int count = 0;
        PathIterator p = s.getPathIterator(null);
        float[] coords = new float[6];
        while (!p.isDone()) {
            int type = p.currentSegment(coords);
            if (count > 12 || type == PathIterator.SEG_QUADTO || type == PathIterator.SEG_CUBICTO) {
                return null;
            }
            points[count++] = coords[0];
            points[count++] = coords[1];
            p.next();
        }
        if (points[0] == points[6] && points[6] == points[8] && points[2] == points[4]
                && points[1] == points[3] && points[3] == points[9] && points[5] == points[7]) {
            return new Rectangle2D.Float(points[0], points[1], points[2] - points[0], points[7]
                    - points[1]);
        }
        return null;
    }

    /**
     * Reduces the size of this Area by intersecting it with the specified Area
     * if they are both rectangles.
     *
     * @see java.awt.geom.Rectangle2D#intersect(Rectangle2D, Rectangle2D,
     *      Rectangle2D)
     * @param area
     *            the area.
     */
    public void intersect(Area area) {
        Rectangle2D src1 = extractRectangle();
        Rectangle2D src2 = area.extractRectangle();
        if (src1 != null && src2 != null) {
            Rectangle2D.intersect(src1, src2, (Rectangle2D)s);
        }
    }

    /**
     * Subtract the specified area from this area.
     *
     * @param area
     *            the area to subtract.
     * @throws NotImplementedException
     *             if this method is not implemented.
     */
    public void subtract(Area area) throws NotImplementedException {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Checks if this Area is empty.
     *
     * @return true, if this Area is empty.
     * @throws NotImplementedException
     *             if this method is not implemented.
     */
    public boolean isEmpty() throws NotImplementedException {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Checks if this Area is polygonal.
     *
     * @return true, if this Area is polygonal.
     * @throws NotImplementedException
     *             if this method is not implemented.
     */
    public boolean isPolygonal() throws NotImplementedException {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Checks if this Area is rectangular.
     *
     * @return true, if this Area is rectangular.
     * @throws NotImplementedException
     *             if this method is not implemented.
     */
    public boolean isRectangular() throws NotImplementedException {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Checks if this Area is singular.
     *
     * @return true, if this Area is singular.
     * @throws NotImplementedException
     *             if this method is not implemented.
     */
    public boolean isSingular() throws NotImplementedException {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Resets the data of this Area.
     *
     * @throws NotImplementedException
     *             if this method is not implemented.
     */
    public void reset() throws NotImplementedException {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Transforms the data of this Area according to the specified
     * AffineTransform.
     *
     * @param t
     *            the transform to use to transform the data.
     */
    public void transform(AffineTransform t) {
        s = t.createTransformedShape(s);
    }

    /**
     * Creates a new Area that is the result of transforming the data of this
     * Area according to the specified AffineTransform.
     *
     * @param t
     *            the transform to use to transform the data.
     * @return the new Area that is the result of transforming the data of this
     *         Area according to the specified AffineTransform.
     */
    public Area createTransformedArea(AffineTransform t) {
        return s == null ? new Area() : new Area(t.createTransformedShape(s));
    }

    @Override
    public Object clone() {
        return new Area(this);
    }

}