package org.apache.sis.internal.referencing.j2d;

import android.graphics.PointF;

/**
 * Android {@link PointF} wrapper class.
 */
public abstract class Point2D extends PointF {

    public Point2D() {
        super();
    }

    public Point2D(int x, int y) {
        super(x, y);
    }

    public Point2D(double x, double y) {
        super((float) x, (float) y);
    }

    public Point2D(Point2D p) {
        super();
        super.set(p);
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public void setX(double x) {
        super.x = (float) x;
    }

    public void setY(double y) {
        super.y = (float) y;
    }

    public void setLocation(double x, double y) {
        super.set((float) x, (float) y);
    }

    public void setLocation(int x, int y) {
        super.set(x, y);
    }

    public static class Double extends Point2D {

        public Double() {
            super();
        }

        public Double(double x, double y) {
            super(x, y);
        }
    }

    public static class Float extends Point2D {

        public Float() {
            super();
        }

        public Float(float x, float y) {
            super(x, y);
        }
    }
}
