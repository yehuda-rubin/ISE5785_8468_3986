package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.List;

/**
 * Tube class represents a tube in 3D Cartesian coordinate
 * extends RadialGeometry
 *  axis - the axis of the tube
 *  radius - the radius of the tube
 * @author Yehuda rubin and arye hacohen
 */

public class Tube extends RadialGeometry{
    /**
     * axis - the axis of the tube
     */
    protected Ray axis;

    /**
     * constructor
     * @param axis - the axis of the tube
     * @param radius - the radius of the tube
     */

    public Tube(Ray axis, double radius) {
        super(radius);
        this.axis = axis;
        if (radius <= 0) {
            throw new IllegalArgumentException("Radius must be positive");
        }
    }

    @Override
    public Vector getNormal(Point point) {
        Point p0 = axis.getHead();
        Vector v = axis.getDirection();
        Vector p0_p = point.subtract(p0);
        double t = v.dotProduct(p0_p);
        Point o = p0.add(v.scale(t));
        return point.subtract(o).normalize();
    }
    @Override
    public List<Point> findIntersections(Ray ray) {
        return null;
    }
}
