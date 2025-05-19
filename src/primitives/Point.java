package primitives;

/**
 * This class make point in 3D space
 * xyz coordinates of a point
 *
 * @author Yehuda rubin and arye hacohen
 */

public class Point {
    /**
     * ZERO point
     */
    public static final Point ZERO = new Point(0, 0, 0);
    /**
     * xyz coordinates of a point
     */
    protected final Double3 xyz;

    /**
     * constructor
     *
     * @param x will be d1 value
     * @param y will be d2 value
     * @param z will be d3 value
     */

    public Point(double x, double y, double z) {
        xyz = new Double3(x, y, z);
    }

    /**
     * constructor
     *
     * @param xyz will be the coordinates of the new point
     */

    public Point(Double3 xyz) {
        this.xyz = xyz;
    }

    /**
     * make subtraction between two points
     *
     * @param p is the point that we subtract from the current point
     * @return the result of the subtraction
     */

    public Vector subtract(Point p) {
        return new Vector(xyz.d1() - p.xyz.d1(), xyz.d2() - p.xyz.d2(), xyz.d3() - p.xyz.d3());
    }

    /**
     * make addition between two points
     *
     * @param v is the vector that we add to the current point
     * @return the result of the addition
     */

    public Point add(Vector v) {
        return new Point(xyz.d1() + v.xyz.d1(), xyz.d2() + v.xyz.d2(), xyz.d3() + v.xyz.d3());
    }

    /**
     * make distanceSquared between two points
     *
     * @param p is the point that we distanceSquared from the current point
     * @return the result of the distanceSquared
     */

    public double distanceSquared(Point p) {
        return ((xyz.d1() - p.xyz.d1()) * (xyz.d1() - p.xyz.d1()) + (xyz.d2() - p.xyz.d2()) * (xyz.d2() - p.xyz.d2()) + (xyz.d3() - p.xyz.d3()) * (xyz.d3() - p.xyz.d3()));
    }

    /**
     * make distance between two points
     *
     * @param p is the point that we distance from the current point
     * @return the result of the distance
     */
    public double distance(Point p) {
        return Math.sqrt(distanceSquared(p));
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        return obj instanceof Point other && xyz.equals(other.xyz);
    }

    @Override
    public String toString() {
        return "" + xyz;
    }
}


