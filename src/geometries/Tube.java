package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;
import static primitives.Util.isZero;
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
        Point p0 = axis.getPoint(0);
        Vector v = axis.getDirection();
        Vector p0_p = point.subtract(p0);
        double t = v.dotProduct(p0_p);
        Point o = p0.add(v.scale(t));
        return point.subtract(o).normalize();
    }

    /**
     * @param ray the ray to check for intersections with the tube
     * @return a list of intersection points or null if there are no intersections
     */
    @Override
    public List<Point> findIntersections(Ray ray) {
        Vector v = axis.getDirection();
        Point p0 = axis.getPoint(0);
        Point p1 = ray.getPoint(0);
        Vector d = ray.getDirection();

        // Calculate coefficients for the quadratic equation
        double a = d.dotProduct(d) - Math.pow(v.dotProduct(d), 2);
        if (isZero(a)) {
            return null; // The ray is parallel to the tube
        }

        Vector deltaP = p1.subtract(p0);
        double b = 2 * (d.dotProduct(deltaP) - v.dotProduct(d) * v.dotProduct(deltaP));
        double c = deltaP.dotProduct(deltaP) - Math.pow(v.dotProduct(deltaP), 2) - radius * radius;

        double discriminant = b * b - 4 * a * c;
        if (discriminant < 0) {
            return null; // No intersection
        }

        double t1 = (-b + Math.sqrt(discriminant)) / (2 * a);
        double t2 = (-b - Math.sqrt(discriminant)) / (2 * a);

        if (discriminant == 0) {
            return List.of(ray.getPoint(t1)); // One intersection point
        }

        if (t1 > 0 && t2 > 0) {
            return List.of(ray.getPoint(t1), ray.getPoint(t2)); // Two intersection points
        } else if (t1 > 0) {
            return List.of(ray.getPoint(t1)); // One intersection point
        } else if (t2 > 0) {
            return List.of(ray.getPoint(t2)); // One intersection point
        }

        return null; // No intersection
    }
}
