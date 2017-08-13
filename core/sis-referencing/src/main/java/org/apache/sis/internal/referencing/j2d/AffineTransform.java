package org.apache.sis.internal.referencing.j2d;

import android.graphics.Matrix;

/**
 * Wrapper class for Android {@link Matrix}.
 */
public class AffineTransform extends Matrix {

    /**
     * Wrapper for {@link Matrix()}.
     */
    public AffineTransform() {
        super();
    }

    /**
     * Wrapper for {@link Matrix()} and {@link Matrix#setValues(float[])}.
     * @param m00
     * @param m10
     * @param m01
     * @param m11
     * @param m02
     * @param m12
     */
    public AffineTransform(double m00, double m10, double m01, double m11, double m02, double m12) {
        super();
        super.setValues(new float[]{(float) m00, (float) m10, 0F, (float) m01, (float) m11, 0F, (float) m02, (float) m12, 1F });
    }

    /**
     * Wrapper for {@link Matrix(Matrix)}.
     * @param at
     */
    public AffineTransform(AffineTransform at) {
        super(at);
    }

    /**
     * Wrapper for {@link Matrix#mapVectors(float[], int, float[], int, int)}.
     * @param srcPts
     * @param srcOff
     * @param dstPts
     * @param dstOff
     * @param numPts
     */
    public void transform(double[] srcPts, int srcOff, double[] dstPts, int dstOff, int numPts) {
        float[] srcPtsF = new float[srcPts.length];
        for (int i=0; i < srcPts.length; i++) {
            srcPtsF[i] = (float) srcPts[i];
        }

        float[] dstPtsF = new float[dstPts.length];
        for (int i=0; i < dstPts.length; i++) {
            dstPtsF[i] = (float) dstPts[i];
        }

        super.mapVectors(srcPtsF, srcOff, dstPtsF, dstOff, numPts);
    }

    /**
     * Wrapper for {@link Matrix#mapVectors(float[], int, float[], int, int)}.
     * @param srcPts
     * @param srcOff
     * @param dstPts
     * @param dstOff
     * @param numPts
     */
    public void transform(float[] srcPts, int srcOff, float[] dstPts, int dstOff, int numPts) {
        super.mapPoints(srcPts, srcOff, dstPts, dstOff, numPts);
    }

    /**
     * Wrapper for {@link Matrix#getValues(float[])}.
     * @param matrix
     */
    public void getMatrix(double[] matrix) {
        float[] values = new float[9];
        super.getValues(values);

        matrix[0] = values[0];
        matrix[1] = values[1];
        matrix[2] = values[3];
        matrix[3] = values[4];
        matrix[4] = values[6];
        matrix[5] = values[7];
    }

    /**
     * Wrapper of {@link Matrix#postTranslate(float, float)}.
     * @param tx
     * @param ty
     */
    public void translate(double tx, double ty) {
        super.postTranslate((float) tx, (float) ty);
    }

    /**
     * Wrapper of {@link Matrix#postRotate(float)}.
     * @param theta
     */
    public void rotate(double theta) {
        super.postRotate((float) theta);
    }

    /**
     * Wrapper of {@link Matrix#postRotate(float, float, float)}.
     * @param theta
     * @param anchorx
     * @param anchory
     */
    public void rotate(double theta, double anchorx, double anchory) {
        super.postRotate((float) theta, (float) anchorx, (float) anchory);
    }

    /**
     * Wrapper of {@link Matrix#postScale(float, float)}.
     * @param sx
     * @param sy
     */
    public void scale(double sx, double sy) {
        super.postScale((float) sx, (float) sy);
    }

    /**
     * Wrapper of {@link Matrix#postSkew(float, float)}.
     * @param shx
     * @param shy
     */
    public void shear(double shx, double shy) {
        super.postSkew((float) shx, (float) shy);
    }

    /**
     * Wrapper of {@link Matrix#set(Matrix)}.
     * @param at
     */
    public void setTransform(AffineTransform at) {
        super.set(at);
    }

    /**
     * Wrapper of {@link Matrix#setValues(float[])}.
     * @param m00
     * @param m10
     * @param m01
     * @param m11
     * @param m02
     * @param m12
     */
    public void setTransform(double m00, double m10,
                                   double m01, double m11,
                                   double m02, double m12) {
        super.setValues(new float[]{(float) m00, (float) m10, 0F, (float) m01, (float) m11, 0F, (float) m02, (float) m12, 1F});
    }

    /**
     * Implementation of {@code java.awt.geom.AffineTransform.getScaleX()}
     * @return
     */
    public double getScaleX() {
        float[] matrix = new float[9];
        super.getValues(matrix);
        return matrix[0];
    }

    /**
     * Implementation of {@code java.awt.geom.AffineTransform.getScaleY()}
     * @return
     */
    public double getScaleY() {
        float[] matrix = new float[9];
        super.getValues(matrix);
        return matrix[4];
    }

    /**
     * Implementation of {@code java.awt.geom.AffineTransform.getShearX()}
     * @return
     */
    public double getShearX() {
        float[] matrix = new float[9];
        super.getValues(matrix);
        return matrix[3];
    }

    /**
     * Implementation of {@code java.awt.geom.AffineTransform.getShearY()}
     * @return
     */
    public double getShearY() {
        float[] matrix = new float[9];
        super.getValues(matrix);
        return matrix[1];
    }

    /**
     * Implementation of {@code java.awt.geom.AffineTransform.getTranslateX()}
     * @return
     */
    public double getTranslateX() {
        float[] matrix = new float[9];
        super.getValues(matrix);
        return matrix[6];
    }

    /**
     * Implementation of {@code java.awt.geom.AffineTransform.getTranslateY()}
     * @return
     */
    public double getTranslateY() {
        float[] matrix = new float[9];
        super.getValues(matrix);
        return matrix[7];
    }

    /**
     * Wraps {@link Matrix#mapPoints(float[], float[])}
     * @param ptSrc
     * @param ptDst
     */
    public Point2D transform(Point2D ptSrc, Point2D ptDst) {
        if (ptDst == null) {
            if (ptSrc instanceof Point2D.Double) {
                ptDst = new Point2D.Double();
            } else {
                ptDst = new Point2D.Float();
            }
        }

        float[] src = new float[]{(float) ptSrc.getX(), (float) ptSrc.getY()};
        float[] dst = new float[2];
        super.mapPoints(dst, src);

        ptDst.setLocation(dst[0], dst[1]);
        return ptDst;
    }

    /**
     * Wraps {@link Matrix#mapPoints(float[], int, float[], int, int)}
     * @param srcPts
     * @param srcOff
     * @param dstPts
     * @param dstOff
     * @param numPts
     */
    public void transform(double[] srcPts, int srcOff, float[] dstPts, int dstOff, int numPts) {
        transform(srcPts, srcOff, dstPts, dstOff, numPts);
    }

    /**
     * Wraps {@link Matrix#mapPoints(float[], int, float[], int, int)}
     * @param srcPts
     * @param srcOff
     * @param dstPts
     * @param dstOff
     * @param numPts
     */
    public void transform(float[] srcPts, int srcOff, double[] dstPts, int dstOff, int numPts) {
        transform(srcPts, srcOff, dstPts, dstOff, numPts);
    }

    /**
     * Implementation of {@code java.awt.geom.AffineTransform.deltaTransform(double[], int, double[], int, int)}
     * @param srcPts
     * @param srcOff
     * @param dstPts
     * @param dstOff
     * @param numPts
     */
    public void deltaTransform(double[] srcPts, int srcOff, double[] dstPts, int dstOff, int numPts) {
        float[] matrix = new float[9];
        super.getValues(matrix);

        while (--numPts >= 0) {
            double x = srcPts[srcOff++];
            double y = srcPts[srcOff++];
            dstPts[dstOff++] = x * matrix[0] + y * matrix[3];
            dstPts[dstOff++] = x * matrix[1] + y * matrix[4];
        }
    }

    public double getDeterminant() {
        float[] matrix = new float[9];
        super.getValues(matrix);
        return matrix[0] * matrix[4] - matrix[3] * matrix[1];
    }

    /**
     * Inverse transform {@link Point2D}
     * @param srcPt
     * @param dstPt
     * @return
     */
    public Point2D inverseTransform(Point2D srcPt, Point2D dstPt) {
        if (dstPt == null) {
            if (srcPt instanceof Point2D.Double) {
                dstPt = new Point2D.Double();
            } else {
                dstPt = new Point2D.Float();
            }
        }

        float[] matrix = new float[9];
        super.getValues(matrix);

        double x = srcPt.getX() - matrix[6];
        double y = srcPt.getY() - matrix[7];
        double det = getDeterminant();

        dstPt.setLocation((x * matrix[4] - y * matrix[3]) / det, (y * matrix[0] - x * matrix[1]) / det);
        return dstPt;
    }

    public Shape createTransformedShape(Shape src) {
        if (src == null)
            return null;

        if (src instanceof Path2D) {
            return ((Path2D) src).createTransformedShape(this);
        }

        PathIterator path = src.getPathIterator(this);
        Path2D dst = new Path2D(path.getWindingRule());
        dst.append(path, false);
        return dst;
    }

    public void setToScale(double scx, double scy) {
        super.setValues(new float[]{(float) scx, 0.0f, 0F, 0.0f, (float) scy, 0F, 0.0f, 0.0f, 1f });
    }

    public static AffineTransform getRotateInstance(double angle, double x, double y) {
        Matrix matrix = new Matrix();
        matrix.postRotate((float) angle, (float) x, (float) y);
        return (AffineTransform) matrix;
    }
}
