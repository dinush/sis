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

    List<double[]> pathmap = new ArrayList();

    public Path2D() {
        super();
    }

    public Path2D(int windingRule) {
        super();
        setWindingRule(windingRule);
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
        Rectangle2D rect = new Rectangle2D.Float();
        super.computeBounds(rect, true);
        return rect;
    }

    public int getWindingRule() {
        return getFillType() == FillType.EVEN_ODD || getFillType() == FillType.INVERSE_EVEN_ODD ? WIND_EVEN_ODD : WIND_NON_ZERO;
    }

    public void setWindingRule(int windingRule) {
        setFillType(windingRule == WIND_EVEN_ODD ? FillType.EVEN_ODD : FillType.WINDING);
    }

    public void closePath() {
        super.close();
    }

    public void quadTo(double x1, double y1, double x2, double y2) {
        super.quadTo((float) x1, (float) y1, (float) x2, (float) y2);
    }

    public void append(Path line, boolean connect) {
        super.addPath(line);
    }

    public boolean contains(double x, double y) {
        return false;
    }

    public Shape createTransformedShape(AffineTransform at) {
        Path2D pathClone = null;
        try {
            pathClone = (Path2D) clone();
            if (at != null) {
                pathClone.transform(at);
            }
            return pathClone;
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return this;
    }

    public void transform(AffineTransform at) {
        float[] coords = new float[pathmap.size() * 2];
        for (int i=0, j=0; i < pathmap.size(); i++) {
            coords[j++] = (float) pathmap.get(i)[0];
            coords[j++] = (float) pathmap.get(i)[1];
        }
        at.transform(coords, 0, coords, 0, coords.length / 2);

        pathmap.clear();
        for (int i=0; i < coords.length;) {
            pathmap.add(new double[]{coords[i++], coords[i++]});
        }
    }

    public static class Double extends Path2D {
        public Double() {
            super();
        }

        public Double(int windingRule) {
            super(windingRule);
        }
    }

    public static class Float extends Path2D {
        public Float() {
            super();
        }

        public Float(int windingRule) {
            super(windingRule);
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
