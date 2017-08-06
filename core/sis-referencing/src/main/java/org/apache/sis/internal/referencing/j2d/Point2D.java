package org.apache.sis.internal.referencing.j2d;

import android.graphics.PointF;

/**
 * Android {@link PointF} wrapper class.
 */
public abstract class Point2D {

    private PointF pointF;

    public Point2D() {
        pointF = new PointF();
    }

    public Point2D(int x, int y) {
        pointF = new PointF(x, y);
    }

    public Point2D(double x, double y) {
        pointF = new PointF((float) x, (float) y);
    }

    public Point2D(Point2D p) {
        pointF = new PointF();
        pointF.set(p.pointF);
    }

    public double getX() {
        return pointF.x;
    }

    public double getY() {
        return pointF.y;
    }

    public void setLocation(double x, double y) {
        pointF.set((float) x, (float) y);
    }

    public void setLocation(int x, int y) {
        pointF.set(x, y);
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
