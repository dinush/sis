package org.apache.sis.internal.referencing.j2d;

import android.graphics.Path;

/**
 * Line2D implementation by subclassing {@link Path}.
 */
public class Line2D extends Path {

    public Line2D() {
        super();
    }

    public void setLine(double x1, double y1, double x2, double y2) {
        super.moveTo((float) x1, (float) y1);
        super.lineTo((float) x2, (float) y2);
    }

    public static class Double extends Line2D {}

    public static class Float extends Line2D {}
}
