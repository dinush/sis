package org.apache.sis.internal.referencing.j2d;

/**
 * Point2D impl. This should be used instead of Point2D from {@code java.awt}
 * when working with {@code org.apache.sis.geometry} classes.
 */
public abstract class Point2D {

    public Point2D() {}

    public abstract double getX();

    public abstract double getY();

    public abstract void setLocation(double x, double y);

    public void setLocation(Point2D p) {
        setLocation(p.getX(), p.getY());
    }

    public static double distance(double x1, double y1,
                                  double x2, double y2)
    {
        x1 -= x2;
        y1 -= y2;
        return Math.sqrt(x1 * x1 + y1 * y1);
    }

    public double distance(double px, double py) {
        px -= getX();
        py -= getY();
        return Math.sqrt(px * px + py * py);
    }

    public double distance(Point2D pt) {
        double px = pt.getX() - this.getX();
        double py = pt.getY() - this.getY();
        return Math.sqrt(px * px + py * py);
    }

    /**
     * Point2D.Double impl. This should be used instead of Point2D.Double from
     * {@code java.awt} when working with {@code org.apache.sis.geometry} classes.
     */
    public static class Double extends Point2D {

        public double x, y;

        public Double() {}

        public Double(final double x, final double y) {
            this.x = x;
            this.y = y;
        }

        public double getX() {
            return x;
        }

        public double getY() {
            return y;
        }

        /**
         * Sets this coordinate to the specified point.
         * @param position  the new position for this point.
         */
        public void setLocation(Point2D position) {
            this.x = position.getX();
            this.y = position.getY();
        }

        /**
         * Sets this coordinate to the specified coordinates.
         * @param x new x coordinate.
         * @param y new y coordinate.
         */
        public void setLocation(double x, double y) {
            this.x = x;
            this.y = y;
        }

        /**
         * Returns the distance from this <code>Point2D</code> to
         * a specified point.
         * @param x the X coordinate of the specified point to be measured
         *           against this {@code Point2D}.
         * @param y the Y coordinate of the specified point to be measured
         *           against this {@code Point2D}.
         * @return  the distance between this {@code Point2D}.
         */
        public double distance(double x, double y) {
            x -= this.x;
            y -= this.y;
            return Math.sqrt(x * x + y * y);
        }

        /**
         * Returns the distance from this <code>Point2D</code> to
         * a specified point.
         * @param p the specified {@code Point2D} to be measured
         *           against this {@code Point2D}.
         * @return
         */
        public double distance(Point2D p) {
            return distance(p.getX(), p.getY());
        }

        public Point2D clone() {
            return this.clone();
        }
    }

    /**
     * Point2D.Float impl. This should be used instead of Point2D.Float from
     * {@code java.awt} when working with {@code org.apache.sis.geometry} classes.
     */
    public static class Float extends Point2D {

        /**
         * The x coordinate.
         */
        public float x;

        /**
         * The y coordinate.
         */
        public float y;

        /**
         * Instantiates a new float-valued Point2D with its data set to zero.
         */
        public Float() {
        }

        /**
         * Instantiates a new float-valued Point2D with the specified
         * coordinates.
         *
         * @param x the x coordinate.
         * @param y the y coordinate.
         */
        public Float(float x, float y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public double getX() {
            return x;
        }

        @Override
        public double getY() {
            return y;
        }

        /**
         * Sets the point's coordinates.
         *
         * @param x the x coordinate.
         * @param y the y coordinate.
         */
        public void setLocation(float x, float y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public void setLocation(double x, double y) {
            this.x = (float)x;
            this.y = (float)y;
        }

        @Override
        public String toString() {
            return getClass().getName() + "[x=" + x + ",y=" + y + "]";
        }
    }

}
