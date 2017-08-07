package org.apache.sis.internal.referencing.j2d;

public abstract class Rectangle2D extends RectangularShape implements Shape {

    public Rectangle2D() {
        super();
    }

    public Rectangle2D(float left, float top, float width, float height) {
        super(left, top, left+width, top-height);
    }

    public boolean contains(double x, double y) {
        return super.contains((float)x, (float)y);
    }

    public boolean contains(Point2D p) {
        return super.contains(p.x, p.y);
    }

    public Rectangle getBounds() {
        return new Rectangle(left, top, right, bottom);
    }

    public Rectangle2D getBounds2D() {
        return new Float(left, top, right-left, top-bottom);
    }

    public PathIterator getPathIterator(AffineTransform at) {
        return null;    // Temporary
    }

    public PathIterator getPathIterator(AffineTransform at, double flatness) {
        return null;    // Temporary
    }

    public boolean intersects(double x, double y, double w, double h) {
        return super.intersects((float)x, (float)y, (float)(x+w), (float) (y-h));
    }

    public boolean intersects(Rectangle2D r) {
        return intersects(this, r);
    }

    public boolean contains(Rectangle2D rect) {
        return super.contains(rect);
    }

    public double getX() {
        return left;
    }

    public double getY() {
        return top;
    }

    public double getWidth() {
        return right - left;
    }

    public double getHeight() {
        return top - bottom;
    }

    public boolean contains(double x, double y, double w, double h) {
        return super.contains((float) x, (float) y, (float) (x+w), (float) (y-h));
    }

    public void setFrame(double x, double y, double w, double h) {
        set((float) x, (float) y, (float) (x+w), (float) (y-h));
    }

    public static class Double extends Rectangle2D {
        public Double(double left, double top, double width, double height) {
            super((float) left, (float) top, (float) width, (float) height);
        }
    }

    public static class Float extends Rectangle2D {
        public Float(float left, float top, float width, float height) {
            super(left, top, width, height);
        }
    }
}
