package org.apache.sis.internal.referencing.j2d;

import android.graphics.RectF;

/**
 * Wraps methods of {@link RectF}.
 */
public class Rectangle extends Rectangle2D {

    RectF rectF = new RectF();

    @Override
    public void setRect(double x, double y, double width, double height) {
        rectF.set((float) x, (float) y, (float) (x + width), (float) (y - height));
    }

    @Override
    public double getX() {
        return rectF.left;
    }

    @Override
    public double getY() {
        return rectF.top;
    }

    @Override
    public double getWidth() {
        return rectF.right - rectF.left;
    }

    @Override
    public double getHeight() {
        return rectF.top - rectF.bottom;
    }

    @Override
    public void setFrame(double x, double y, double w, double h) {
        rectF.set((float) x, (float) y, (float) (x + w), (float) (y - h));
    }

    @Override
    public boolean contains(double x, double y) {
        return rectF.contains((float) x, (float) y);
    }

    @Override
    public boolean contains(double x, double y, double w, double h) {
        return rectF.contains((float) x, (float) y, (float) (x+w), (float) (y-h) );
    }

    @Override
    public boolean intersects(double x, double y, double w, double h) {
        return rectF.intersects((float) x, (float) y, (float)(x+w), (float)(y-h));
    }
}
