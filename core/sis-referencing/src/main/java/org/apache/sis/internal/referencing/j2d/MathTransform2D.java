package org.apache.sis.internal.referencing.j2d;

import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.Matrix;
import org.opengis.referencing.operation.NoninvertibleTransformException;
import org.opengis.referencing.operation.TransformException;

public interface MathTransform2D extends MathTransform {

    Point2D transform(final Point2D ptSrc, final Point2D ptDst) throws TransformException;

    Shape createTransformedShape(final Shape shape) throws TransformException;

    Matrix derivative(final Point2D point2D) throws TransformException;

    MathTransform2D inverse() throws NoninvertibleTransformException;
}
