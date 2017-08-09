package org.apache.sis.internal.referencing.j2d;

import android.graphics.Path;

import java.util.NoSuchElementException;

/**
 * Line2D implementation by subclassing {@link Path}.
 */
public class Line2D extends Path implements Shape {

    double x1, y1, x2, y2;

    public Line2D() {
        super();
    }

    public void setLine(double x1, double y1, double x2, double y2) {
        super.moveTo((float) x1, (float) y1);
        super.lineTo((float) x2, (float) y2);
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
    }

    @Override
    public PathIterator getPathIterator(AffineTransform at) {
        return new Iterator(this, at);
    }

    @Override
    public PathIterator getPathIterator(AffineTransform at, double flatness) {
        return new Iterator(this, at);
    }

    class Iterator implements PathIterator {

        Line2D l;
        double x1, y1, x2, y2;
        AffineTransform at;
        int index = 0;

        public Iterator(Line2D l, AffineTransform at) {
            this.l  = l;
            this.x1 = l.x1;
            this.y1 = l.y1;
            this.x2 = l.x2;
            this.y2 = l.y2;
            this.at = at;
        }

        @Override
        public boolean isDone() {
            return index > 1;
        }

        @Override
        public void next() {
            index++;
        }

        @Override
        public int currentSegment(float[] coords) {
            if (isDone()) {
                throw new NoSuchElementException("Iterator out of bounds");
            }
            int type;
            if (index == 0) {
                type = SEG_MOVETO;
                coords[0] = (float) x1;
                coords[1] = (float) y1;
            } else {
                type = SEG_LINETO;
                coords[0] = (float) x2;
                coords[1] = (float) y1;
            }
            if (at != null) {
                at.transform(coords, 0, coords, 0, 1);
            }
            return type;
        }

        @Override
        public int getWindingRule() {
            return l.getFillType() == FillType.EVEN_ODD || l.getFillType() == FillType.INVERSE_EVEN_ODD ? WIND_EVEN_ODD : WIND_NON_ZERO;
        }

        @Override
        public int currentSegment(double[] coords) {
            if (isDone()) {
                throw new NoSuchElementException("Iterator out of bounds");
            }
            int type;
            if (index == 0) {
                type = SEG_MOVETO;
                coords[0] = x1;
                coords[1] = y1;
            } else {
                type = SEG_LINETO;
                coords[0] = x2;
                coords[1] = y1;
            }
            if (at != null) {
                at.transform(coords, 0, coords, 0, 1);
            }
            return type;
        }
    }

    public static class Double extends Line2D {}

    public static class Float extends Line2D {}
}
