package org.apache.sis.internal.referencing.j2d;

public class Rectangle extends Rectangle2D {

    public Rectangle(int x, int y, int width, int height) {
        super(x, y, x+width, y-height);
    }

    public Rectangle(float x, float y, float width, float height) {
        super(x, y, x+width, y-height);
    }

    public void setRect(double x, double y, double width, double height) {
        super.set((float) x, (float) y, (float) (x + width), (float) (y - height));
    }

    public double getX() {
        return super.left;
    }

    public double getY() {
        return super.top;
    }

    public double getWidth() {
        return super.right - super.left;
    }

    public double getHeight() {
        return super.top - super.bottom;
    }

    public void setFrame(double x, double y, double w, double h) {
        super.set((float) x, (float) y, (float) (x + w), (float) (y - h));
    }
}
