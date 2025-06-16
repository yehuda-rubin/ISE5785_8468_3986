package geometries;

import primitives.AABB;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.List;

/**
 * Tube class represents a tube in 3D Cartesian coordinate
 * extends RadialGeometry
 * axis - the axis of the tube
 * radius - the radius of the tube
 *
 * @author Yehuda rubin and arye hacohen
 */

public class Tube extends RadialGeometry {
    /**
     * axis - the axis of the tube
     */
    protected Ray axis;

    /**
     * constructor
     *
     * @param axis   - the axis of the tube
     * @param radius - the radius of the tube
     */

    public Tube(double radius,Ray axis) {
        super(radius);
        this.axis = axis;
        if (radius <= 0) {
            throw new IllegalArgumentException("Radius must be positive");
        }
    }

    @Override
    public Vector getNormal(Point point) {
        Point p0 = axis.getPoint(0);
        Vector v = axis.getDirection();
        Vector p0_p = point.subtract(p0);
        double t = v.dotProduct(p0_p);
        Point o = p0.add(v.scale(t));
        return point.subtract(o).normalize();
    }

    /**
     * @param ray the ray to check for intersections with the tube
     * @param maxDistance the maximum distance to check for intersections
     * @return a list of intersection points or null if there are no intersections
     */
    @Override
    public List<Intersection> calculateIntersectionsHelper(Ray ray, double maxDistance) {
        return null; // No intersection
    }

    @Override
    protected AABB calculateBoundingBox() {
        return null;
    }
}