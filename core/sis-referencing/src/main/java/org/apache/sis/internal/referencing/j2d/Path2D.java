package org.apache.sis.internal.referencing.j2d;

import android.graphics.Path;

/**
 * {@link Path} wrapper class.
 */
public class Path2D extends Path {

    public Path2D() {
        super();
    }

    public void lineTo(double x, double y) {
        super.lineTo((float) x, (float) y);
    }

    public void moveTo(double x, double y) {
        super.moveTo((float) x, (float) y);
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
}
