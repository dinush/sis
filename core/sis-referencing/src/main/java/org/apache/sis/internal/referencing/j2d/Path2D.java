package org.apache.sis.internal.referencing.j2d;

import android.graphics.Path;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * {@link Path} wrapper class.
 */
public class Path2D extends Path implements Shape {

    public static final int WIND_EVEN_ODD = PathIterator.WIND_EVEN_ODD;
    public static final int WIND_NON_ZERO = PathIterator.WIND_NON_ZERO;

    List pathmap = new ArrayList();

    public Path2D() {
        super();
    }

    public void lineTo(double x, double y) {
        super.lineTo((float) x, (float) y);
        pathmap.add(new double[]{x, y});
    }

    public void moveTo(double x, double y) {
        super.moveTo((float) x, (float) y);
        pathmap.add(new double[]{x, y});
    }

    @Override
    public PathIterator getPathIterator(AffineTransform at) {
        return new Iterator(this);
    }

    @Override
    public PathIterator getPathIterator(AffineTransform at, double flatness) {
        return new Iterator(this);
    }

    public Rectangle2D getBounds2D() {
        return null;
    }

    public static class Double extends Path2D {
        public Double() {
            super();
        }
    }

    public static class Float extends Path2D {
        public Float() {
            super();
        }
    }

    public class Iterator implements PathIterator {

        Path2D path;
        int index;
        AffineTransform at;

        public Iterator(Path2D path) {
            this.path = path;
            at = null;
        }

        public Iterator(Path2D path, AffineTransform at) {
            this.path = path;
            this.at = at;
        }

        @Override
        public boolean isDone() {
            return index >= path.pathmap.size();
        }

        @Override
        public void next() {
            index++;
        }

        @Override
        public int currentSegment(double[] coords) {
            if (isDone()) {
                throw new NoSuchElementException("Iterator out of bounds");
            }
            int type;
            if (index == 0) {
                type = SEG_MOVETO;
                coords[0] = ((double[]) path.pathmap.get(index))[0];
                coords[1] = ((double[]) path.pathmap.get(index))[1];
            } else {
                type = SEG_LINETO;
                coords[0] = ((double[]) path.pathmap.get(index))[0];
                coords[1] = ((double[]) path.pathmap.get(index))[1];
            }
            if (at != null) {
                at.transform(coords, 0, coords, 0, 1);
            }
            return type;
        }

        @Override
        public int currentSegment(float[] coords) {
            if (isDone()) {
                throw new NoSuchElementException("Iterator out of bounds");
            }
            int type;
            if (index == 0) {
                type = SEG_MOVETO;
                coords[0] = (float) ((double[]) path.pathmap.get(index))[0];
                coords[1] = (float) ((double[]) path.pathmap.get(index))[1];
            } else {
                type = SEG_LINETO;
                coords[0] = (float) ((double[]) path.pathmap.get(index))[0];
                coords[1] = (float) ((double[]) path.pathmap.get(index))[1];
            }
            if (at != null) {
                at.transform(coords, 0, coords, 0, 1);
            }
            return type;
        }

        @Override
        public int getWindingRule() {
            return path.getFillType() == FillType.EVEN_ODD || path.getFillType() == FillType.INVERSE_EVEN_ODD ? WIND_EVEN_ODD : WIND_NON_ZERO;
        }
    }
}
