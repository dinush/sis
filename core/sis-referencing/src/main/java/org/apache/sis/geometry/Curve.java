package org.apache.sis.geometry;

/**
 * Replacement class for {@code sun.awt.geom.Curve}
 */
public class Curve {

    public static final int RECT_INTERSECTS = 0x80000000;

    public static int pointCrossingsForLine(double px, double py,
                                                double x0, double y0,
                                                double x1, double y1)
    {
        if (py <  y0 && py <  y1) return 0;
        if (py >= y0 && py >= y1) return 0;
        if (px >= x0 && px >= x1) return 0;
        if (px <  x0 && px <  x1) return (y0 < y1) ? 1 : -1;
        double xintercept = x0 + (py - y0) * (x1 - x0) / (y1 - y0);
        if (px >= xintercept) return 0;
        return (y0 < y1) ? 1 : -1;
    }

    public static int pointCrossingsForQuad(double px, double py,
                                            double x0, double y0,
                                            double xc, double yc,
                                            double x1, double y1, int level) {
        if (py <  y0 && py <  yc && py <  y1) return 0;
        if (py >= y0 && py >= yc && py >= y1) return 0;
        if (px >= x0 && px >= xc && px >= x1) return 0;
        if (px <  x0 && px <  xc && px <  x1) {
            if (py >= y0) {
                if (py < y1) return 1;
                } else {
                    if (py >= y1) return -1;
                }
            return 0;
        }
        if (level > 52) return pointCrossingsForLine(px, py, x0, y0, x1, y1);
        double x0c = (x0 + xc) / 2;
        double y0c = (y0 + yc) / 2;
        double xc1 = (xc + x1) / 2;
        double yc1 = (yc + y1) / 2;
        xc = (x0c + xc1) / 2;
        yc = (y0c + yc1) / 2;
        if (Double.isNaN(xc) || Double.isNaN(yc)) {
            return 0;
        }
        return (pointCrossingsForQuad(px, py, x0, y0, x0c, y0c, xc, yc,level+1) +
                pointCrossingsForQuad(px, py, xc, yc, xc1, yc1, x1, y1,level+1));
    }

    public static int pointCrossingsForCubic(double px, double py, double x0, double y0, double xc0, double yc0,
                                             double xc1, double yc1, double x1, double y1, int level) {
        if (py <  y0 && py <  yc0 && py <  yc1 && py <  y1) return 0;
        if (py >= y0 && py >= yc0 && py >= yc1 && py >= y1) return 0;
        // Note y0 could equal yc0...
        if (px >= x0 && px >= xc0 && px >= xc1 && px >= x1) return 0;
        if (px <  x0 && px <  xc0 && px <  xc1 && px <  x1) {
            if (py >= y0) {
                if (py < y1) return 1;
            } else {
                if (py >= y1) return -1;
            }
            return 0;
        }
        if (level > 52) return pointCrossingsForLine(px, py, x0, y0, x1, y1);
        double xmid = (xc0 + xc1) / 2;
        double ymid = (yc0 + yc1) / 2;
        xc0 = (x0 + xc0) / 2;
        yc0 = (y0 + yc0) / 2;
        xc1 = (xc1 + x1) / 2;
        yc1 = (yc1 + y1) / 2;
        double xc0m = (xc0 + xmid) / 2;
        double yc0m = (yc0 + ymid) / 2;
        double xmc1 = (xmid + xc1) / 2;
        double ymc1 = (ymid + yc1) / 2;
        xmid = (xc0m + xmc1) / 2;
        ymid = (yc0m + ymc1) / 2;
        if (Double.isNaN(xmid) || Double.isNaN(ymid)) {
            return 0;
        }
        return (pointCrossingsForCubic(px, py, x0, y0, xc0, yc0, xc0m, yc0m, xmid, ymid, level+1) +
                pointCrossingsForCubic(px, py, xmid, ymid, xmc1, ymc1, xc1, yc1, x1, y1, level+1));
    }

    public static int rectCrossingsForPath(PathIterator pi, double rxmin, double rymin,
                                                            double rxmax, double rymax) {
        if (rxmax <= rxmin || rymax <= rymin) {
            return 0;
        }
        if (pi.isDone()) {
            return 0;
        }
        double coords[] = new double[6];
        if (pi.currentSegment(coords) != PathIterator.SEG_MOVETO) {
            throw new IllegalPathStateException("missing initial moveto "+
                    "in path definition");
        }
        pi.next();
        double curx, cury, movx, movy, endx, endy;
        curx = movx = coords[0];
        cury = movy = coords[1];
        int crossings = 0;
        while (crossings != RECT_INTERSECTS && !pi.isDone()) {
            switch (pi.currentSegment(coords)) {
                case PathIterator.SEG_MOVETO:
                    if (curx != movx || cury != movy) {
                        crossings = rectCrossingsForLine(crossings,
                                rxmin, rymin,
                                rxmax, rymax,
                                curx, cury,
                                movx, movy);
                    }
                    movx = curx = coords[0];
                    movy = cury = coords[1];
                    break;
                case PathIterator.SEG_LINETO:
                    endx = coords[0];
                    endy = coords[1];
                    crossings = rectCrossingsForLine(crossings,
                            rxmin, rymin,
                            rxmax, rymax,
                            curx, cury,
                            endx, endy);
                    curx = endx;
                    cury = endy;
                    break;
                case PathIterator.SEG_QUADTO:
                    endx = coords[2];
                    endy = coords[3];
                    crossings = rectCrossingsForQuad(crossings,
                            rxmin, rymin,
                            rxmax, rymax,
                            curx, cury,
                            coords[0], coords[1],
                            endx, endy, 0);
                    curx = endx;
                    cury = endy;
                    break;
                case PathIterator.SEG_CUBICTO:
                    endx = coords[4];
                    endy = coords[5];
                    crossings = rectCrossingsForCubic(crossings,
                            rxmin, rymin,
                            rxmax, rymax,
                            curx, cury,
                            coords[0], coords[1],
                            coords[2], coords[3],
                            endx, endy, 0);
                    curx = endx;
                    cury = endy;
                    break;
                case PathIterator.SEG_CLOSE:
                    if (curx != movx || cury != movy) {
                        crossings = rectCrossingsForLine(crossings,
                                rxmin, rymin,
                                rxmax, rymax,
                                curx, cury,
                                movx, movy);
                        }
                    curx = movx;
                    cury = movy;
                    break;
            }
            pi.next();
        }
        if (crossings != RECT_INTERSECTS && (curx != movx || cury != movy)) {
            crossings = rectCrossingsForLine(crossings,
                    rxmin, rymin,
                    rxmax, rymax,
                    curx, cury,
                    movx, movy);
        }
        return crossings;
    }

    public static int rectCrossingsForLine(int crossings, double rxmin, double rymin, double rxmax, double rymax,
                                            double x0, double y0, double x1, double y1) {
        if (y0 >= rymax && y1 >= rymax) return crossings;
        if (y0 <= rymin && y1 <= rymin) return crossings;
        if (x0 <= rxmin && x1 <= rxmin) return crossings;
        if (x0 >= rxmax && x1 >= rxmax) {
            if (y0 < y1) {
                if (y0 <= rymin) crossings++;
                if (y1 >= rymax) crossings++;
            } else if (y1 < y0) {
                if (y1 <= rymin) crossings--;
                if (y0 >= rymax) crossings--;
            }
            return crossings;
        }
        if ((x0 > rxmin && x0 < rxmax && y0 > rymin && y0 < rymax) ||
                (x1 > rxmin && x1 < rxmax && y1 > rymin && y1 < rymax)) {
            return RECT_INTERSECTS;
        }
        double xi0 = x0;
        if (y0 < rymin) {
            xi0 += ((rymin - y0) * (x1 - x0) / (y1 - y0));
        } else if (y0 > rymax) {
            xi0 += ((rymax - y0) * (x1 - x0) / (y1 - y0));
        }
        double xi1 = x1;
        if (y1 < rymin) {
            xi1 += ((rymin - y1) * (x0 - x1) / (y0 - y1));
        } else if (y1 > rymax) {
            xi1 += ((rymax - y1) * (x0 - x1) / (y0 - y1));
        }
        if (xi0 <= rxmin && xi1 <= rxmin) return crossings;
        if (xi0 >= rxmax && xi1 >= rxmax) {
            if (y0 < y1) {
                if (y0 <= rymin) crossings++;
                if (y1 >= rymax) crossings++;
            } else if (y1 < y0) {
                if (y1 <= rymin) crossings--;
                if (y0 >= rymax) crossings--;
            }
            return crossings;
        }
        return RECT_INTERSECTS;
    }

    public static int rectCrossingsForQuad(int crossings, double rxmin, double rymin, double rxmax, double rymax,
                                           double x0, double y0, double xc, double yc, double x1, double y1, int level) {
        if (y0 >= rymax && yc >= rymax && y1 >= rymax) return crossings;
        if (y0 <= rymin && yc <= rymin && y1 <= rymin) return crossings;
        if (x0 <= rxmin && xc <= rxmin && x1 <= rxmin) return crossings;
        if (x0 >= rxmax && xc >= rxmax && x1 >= rxmax) {
            if (y0 < y1) {
                if (y0 <= rymin && y1 >  rymin) crossings++;
                if (y0 <  rymax && y1 >= rymax) crossings++;
            } else if (y1 < y0) {
                if (y1 <= rymin && y0 >  rymin) crossings--;
                if (y1 <  rymax && y0 >= rymax) crossings--;
            }
            return crossings;
        }
        if ((x0 < rxmax && x0 > rxmin && y0 < rymax && y0 > rymin) ||
                (x1 < rxmax && x1 > rxmin && y1 < rymax && y1 > rymin)) {
            return RECT_INTERSECTS;
        }
        if (level > 52) {
            return rectCrossingsForLine(crossings, rxmin, rymin, rxmax, rymax, x0, y0, x1, y1);
        }
        double x0c = (x0 + xc) / 2;
        double y0c = (y0 + yc) / 2;
        double xc1 = (xc + x1) / 2;
        double yc1 = (yc + y1) / 2;
        xc = (x0c + xc1) / 2;
        yc = (y0c + yc1) / 2;
        if (Double.isNaN(xc) || Double.isNaN(yc)) {
            return 0;
        }
        crossings = rectCrossingsForQuad(crossings, rxmin, rymin, rxmax, rymax, x0, y0, x0c, y0c, xc, yc, level+1);
        if (crossings != RECT_INTERSECTS) {
            crossings = rectCrossingsForQuad(crossings, rxmin, rymin, rxmax, rymax, xc, yc, xc1, yc1, x1, y1, level+1);
        }
        return crossings;
    }

    public static int rectCrossingsForCubic(int crossings, double rxmin, double rymin, double rxmax, double rymax,
                                            double x0,  double y0, double xc0, double yc0, double xc1, double yc1,
                                            double x1,  double y1, int level) {
        if (y0 >= rymax && yc0 >= rymax && yc1 >= rymax && y1 >= rymax) {
            return crossings;
        }
        if (y0 <= rymin && yc0 <= rymin && yc1 <= rymin && y1 <= rymin) {
            return crossings;
        }
        if (x0 <= rxmin && xc0 <= rxmin && xc1 <= rxmin && x1 <= rxmin) {
            return crossings;
        }
        if (x0 >= rxmax && xc0 >= rxmax && xc1 >= rxmax && x1 >= rxmax) {
            if (y0 < y1) {
                if (y0 <= rymin && y1 >  rymin) crossings++;
                if (y0 <  rymax && y1 >= rymax) crossings++;
            } else if (y1 < y0) {
                if (y1 <= rymin && y0 >  rymin) crossings--;
                if (y1 <  rymax && y0 >= rymax) crossings--;
            }
            return crossings;
        }
        if ((x0 > rxmin && x0 < rxmax && y0 > rymin && y0 < rymax) ||
                (x1 > rxmin && x1 < rxmax && y1 > rymin && y1 < rymax)) {
            return RECT_INTERSECTS;
        }
        if (level > 52) {
            return rectCrossingsForLine(crossings, rxmin, rymin, rxmax, rymax, x0, y0, x1, y1);
        }
        double xmid = (xc0 + xc1) / 2;
        double ymid = (yc0 + yc1) / 2;
        xc0 = (x0 + xc0) / 2;
        yc0 = (y0 + yc0) / 2;
        xc1 = (xc1 + x1) / 2;
        yc1 = (yc1 + y1) / 2;
        double xc0m = (xc0 + xmid) / 2;
        double yc0m = (yc0 + ymid) / 2;
        double xmc1 = (xmid + xc1) / 2;
        double ymc1 = (ymid + yc1) / 2;
        xmid = (xc0m + xmc1) / 2;
        ymid = (yc0m + ymc1) / 2;
        if (Double.isNaN(xmid) || Double.isNaN(ymid)) {
            return 0;
        }
        crossings = rectCrossingsForCubic(crossings, rxmin, rymin, rxmax, rymax, x0, y0, xc0, yc0,
                                                             xc0m, yc0m, xmid, ymid, level+1);
        if (crossings != RECT_INTERSECTS) {
            crossings = rectCrossingsForCubic(crossings, rxmin, rymin, rxmax, rymax,
                                              xmid, ymid, xmc1, ymc1, xc1, yc1, x1, y1, level+1);
        }
        return crossings;
    }

    public static int pointCrossingsForPath(PathIterator pi, double px, double py) {
        if (pi.isDone()) {
            return 0;
        }
        double coords[] = new double[6];
        if (pi.currentSegment(coords) != PathIterator.SEG_MOVETO) {
            throw new IllegalPathStateException("missing initial moveto in path definition");
        }
        pi.next();
        double movx = coords[0];
        double movy = coords[1];
        double curx = movx;
        double cury = movy;
        double endx, endy;
        int crossings = 0;
        while (!pi.isDone()) {
            switch (pi.currentSegment(coords)) {
                case PathIterator.SEG_MOVETO:
                    if (cury != movy) {
                        crossings += pointCrossingsForLine(px, py, curx, cury, movx, movy);
                    }
                    movx = curx = coords[0];
                    movy = cury = coords[1];
                    break;
                case PathIterator.SEG_LINETO:
                    endx = coords[0];
                    endy = coords[1];
                    crossings += pointCrossingsForLine(px, py, curx, cury, endx, endy);
                    curx = endx;
                    cury = endy;
                    break;
                case PathIterator.SEG_QUADTO:
                    endx = coords[2];
                    endy = coords[3];
                    crossings += pointCrossingsForQuad(px, py, curx, cury, coords[0], coords[1], endx, endy, 0);
                    curx = endx;
                    cury = endy;
                    break;
                case PathIterator.SEG_CUBICTO:
                    endx = coords[4];
                    endy = coords[5];
                    crossings += pointCrossingsForCubic(px, py, curx, cury, coords[0], coords[1], coords[2], coords[3],
                                                                                               endx, endy, 0);
                    curx = endx;
                    cury = endy;
                    break;
                case PathIterator.SEG_CLOSE:
                    if (cury != movy) {
                        crossings += pointCrossingsForLine(px, py, curx, cury, movx, movy);
                    }
                    curx = movx;
                    cury = movy;
                    break;
            }
            pi.next();
        }
        if (cury != movy) {
            crossings += pointCrossingsForLine(px, py, curx, cury, movx, movy);
        }
        return crossings;
    }
}
