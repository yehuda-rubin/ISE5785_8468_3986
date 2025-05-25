package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.List;

import static primitives.Util.isZero;

/**
 * The Plane class extends the Geometry class
 *
 * @author Yehuda Rubin and Arye Hacohen
 */

public class Plane extends Geometry {
    private final Point q0;
    private final Vector normal;

    /**
     * constructor
     *
     * @param q0     will be the point of the new plane
     * @param normal will be the normal of the new plane
     */

    public Plane(Point q0, Vector normal) {
        this.q0 = q0;
        this.normal = normal.normalize();
    }

    /**
     * constructor
     *
     * @param p1 will be the first point of the new plane
     * @param p2 will be the second point of the new plane
     * @param p3 will be the third point of the new plane
     */

    public Plane(Point p1, Point p2, Point p3) {
        q0 = p1;
        Vector v1 = p2.subtract(p1);
        Vector v2 = p3.subtract(p1);
        normal = v1.crossProduct(v2).normalize();
    }

    @Override
    public Vector getNormal(Point point) {
        return normal;
    }

    /**
     * @param ray the ray to check for intersections with the plane
     * @return a list of intersection points or null if there are no intersections
     */
    @Override
    public List<Intersection> calculateIntersectionsHelper(Ray ray) {
        Vector v = ray.getDirection();
        Point p0 = ray.getPoint(0);
        double nv = normal.dotProduct(v);

        // Check if the ray is parallel to the plane
        if (isZero(nv)) {
            return null; // No intersection
        }

        // Calculate the distance from the ray's head to the plane
        double t = (normal.dotProduct(q0.subtract(p0))) / nv;

        // Check if the intersection point is behind the ray's head
        if (t <= 0) {
            return null; // No intersection
        }

        // Calculate the intersection point
        Point intersectionPoint = p0.add(v.scale(t));

        // Return a list containing the intersection point
        return List.of(new Intersection(this, intersectionPoint));
    }
}
