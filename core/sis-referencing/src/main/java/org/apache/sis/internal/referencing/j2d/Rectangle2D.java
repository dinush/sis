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

    class Iterator implements PathIterator {

        double x;   // x coordinate of the rectangle's upper left corner.
        double y;   // y coordinate of the rectangle's upper left corner.
        double width, height;
        AffineTransform at;
        int index = 0;

        Iterator(Rectangle2D rect, AffineTransform at) {
            this.x      = rect.getX();
            this.y      = rect.getY();
            this.width  = rect.getWidth();
            this.height = rect.getHeight();
            this.at     = at;
        }

        @Override
        public boolean isDone() {
            return index > 5;
        }

        @Override
        public void next() {
            index++;
        }

        @Override
        public int currentSegment(double[] coords) {
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
            if (at != null) {
                at.transform(coords, 0, coords, 0, 1);
            }
            return type;
        }

        @Override
        public int currentSegment(float[] coords) {
            if (index == 5) {
                return SEG_CLOSE;
            }
            int type;
            if (index == 0) {
                type = SEG_MOVETO;
                coords[0] = (float) x;
                coords[1] = (float) y;
            } else {
                type = SEG_LINETO;
                switch (index) {
                    case 1:
                        coords[0] = (float) (x + width);
                        coords[1] = (float) y;
                        break;
                    case 2:
                        coords[0] = (float) (x + width);
                        coords[1] = (float) (y + height);
                        break;
                    case 3:
                        coords[0] = (float) x;
                        coords[1] = (float) (y + height);
                        break;
                    case 4:
                        coords[0] = (float) x;
                        coords[1] = (float) y;
                        break;
                }
            }
            if (at != null) {
                at.transform(coords, 0, coords, 0, 1);
            }
            return type;
        }
    }
}
