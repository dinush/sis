package org.apache.sis.geometry;

/**
 * Replacement class for {@code java.awt.geom.RectangularShape}
 */
public abstract class RectangularShape implements Shape {

    public abstract double getX();

    public abstract double getY();

    public abstract double getWidth();

    public abstract double getHeight();

    public abstract void setFrame(double x, double y, double w, double h);

    public void setFrame(Rectangle2D r) {
        setFrame(r.getX(), r.getY(), r.getWidth(), r.getHeight());
    }

    public Rectangle2D getFrame() {
        return new Rectangle2D.Double(getX(), getY(), getWidth(), getHeight());
    }

    public double getMinX() {
        return getX();
    }

    public double getMaxX() {
        return getX() + getWidth();
    }

    public double getMinY() {
        return getY();
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

    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
            throw new InternalError(e.getMessage());
        }
    }
}
