package org.apache.sis.internal.referencing.j2d;

import android.graphics.Path;

public class QuadCurve2D {

    Path path;
    Point2D p1, p2, ctrlPt;

    protected QuadCurve2D(float x1, float y1, float ctrlx, float ctrly, float x2, float y2) {
        path = new Path();
        path.moveTo(x1, y1);
        path.quadTo(ctrlx, ctrly, x2, y2);

        p1 = new Point2D.Double(x1, y1);
        p2 = new Point2D.Double(x2, y2);
        ctrlPt = new Point2D.Double(ctrlx, ctrly);
    }

    public Point2D getP1() {
        return p1;
    }

    public Point2D getP2() {
        return p2;
    }

    public Point2D getCtrlPt() {
        return ctrlPt;
    }

    public static class Double extends QuadCurve2D {
        public Double(double x1, double y1, double ctrlx, double ctrly, double x2, double y2) {
            super((float) x1, (float) y1, (float) ctrlx, (float) ctrly, (float) x2, (float) y2);
        }
    }
}
