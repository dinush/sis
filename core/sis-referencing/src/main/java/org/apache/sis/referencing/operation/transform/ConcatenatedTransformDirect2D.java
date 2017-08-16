/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.sis.referencing.operation.transform;

import org.apache.sis.internal.referencing.j2d.Shape;
import org.apache.sis.internal.referencing.j2d.Point2D;
import org.apache.sis.internal.referencing.j2d.MathTransform2D;
import org.opengis.geometry.DirectPosition;
import org.opengis.referencing.operation.Matrix;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;
import org.opengis.referencing.operation.NoninvertibleTransformException;
import org.apache.sis.referencing.operation.matrix.Matrices;


/**
 * Concatenated transform where both transforms are two-dimensional.
 *
 * @author  Martin Desruisseaux (IRD, Geomatys)
 * @version 0.5
 * @since   0.5
 * @module
 */
final class ConcatenatedTransformDirect2D extends ConcatenatedTransformDirect implements MathTransform2D {
    /**
     * Serial number for inter-operability with different versions.
     */
    private static final long serialVersionUID = 6009454091075588885L;

    /**
     * Constructs a concatenated transform.
     */
    public ConcatenatedTransformDirect2D(final MathTransform transform1,
                                         final MathTransform transform2)
    {
        super(transform1, transform2);
    }

    /**
     * Checks if transforms are compatibles with this implementation.
     */
    @Override
    boolean isValid() {
        return super.isValid() && (getSourceDimensions() == 2) && (getTargetDimensions() == 2);
    }

    /**
     * Transforms the specified {@code ptSrc} and stores the result in {@code ptDst}.
     */
    public Point2D transform(final Point2D ptSrc, Point2D ptDst) throws TransformException {
        assert isValid();
        final MathTransform transform1 = (MathTransform) this.transform1;
        final MathTransform transform2 = (MathTransform) this.transform2;
        ptDst = (Point2D) transform1.transform((DirectPosition) ptSrc, (DirectPosition) ptDst);
        return (Point2D) transform2.transform((DirectPosition) ptDst, (DirectPosition) ptDst);
    }

    /**
     * Transforms the specified shape.
     *
     * @param  shape  shape to transform.
     * @return transformed shape.
     * @throws TransformException if a transform failed.
     */
    public Shape createTransformedShape(final Shape shape) throws TransformException {
        return AbstractMathTransform2D.createTransformedShape(this, shape, null, null, false);
    }

    /**
     * Gets the derivative of this transform at a point.
     *
     * @param  point  the coordinate point where to evaluate the derivative.
     * @return the derivative at the specified point (never {@code null}).
     * @throws TransformException if the derivative can't be evaluated at the specified point.
     */
    public Matrix derivative(final Point2D point) throws TransformException {
        final MathTransform transform1 = (MathTransform) this.transform1;
        final MathTransform transform2 = (MathTransform) this.transform2;
        final Matrix matrix1 = transform1.derivative((DirectPosition) point);
        final Matrix matrix2 = transform2.derivative(transform1.transform((DirectPosition) point,null));
        return Matrices.multiply(matrix2, matrix1);
    }

    /**
     * Creates the inverse transform of this object.
     */
    @Override
    public MathTransform2D inverse() throws NoninvertibleTransformException {
        return (MathTransform2D) super.inverse();
    }
}
