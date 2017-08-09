package org.apache.sis.internal.referencing.j2d;

/**
 * Replacement class for {@code java.awt.geom.PathIterator}
 */
public interface PathIterator {

    public static final int WIND_EVEN_ODD = 0;
    public static final int WIND_NON_ZERO = 1;
    public static final int SEG_MOVETO = 0;
    public static final int SEG_LINETO = 1;
    public static final int SEG_QUADTO = 2;
    public static final int SEG_CUBICTO = 3;
    public static final int SEG_CLOSE = 4;

    boolean isDone();

    void next();

    int currentSegment(double[] coords);

    int currentSegment(float[] coords);

    int getWindingRule();
}
