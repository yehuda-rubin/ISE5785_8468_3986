package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Util;
import primitives.Vector;

import java.util.ArrayList;
import java.util.List;

/**
 * The Sphere class extends the RadialGeometry class
 * @author Yehuda Rubin and Arye Hacohen
 */
public class Sphere extends RadialGeometry{
    private final Point center;

    /**
     * constructor
     * @param radius will be the radius of the new sphere
     * @param center_of_sphere will be the center of the new sphere
     */

    public Sphere(double radius, Point center_of_sphere) {
        super(radius);
        this.center = center_of_sphere;
        if (radius <= 0) {
            throw new IllegalArgumentException("The radius of the sphere must be positive");
        }
    }

    /**
     * getter for the center of the sphere
     * @return the center of the sphere
     */
    @Override
    public Vector getNormal(Point point) {
        return point.subtract(center).normalize();
    }

    @Override
    public List<Point> findIntersections(Ray ray) {
        Point p0 = ray.getHead(0);
        Vector v = ray.getDirection();
        // Direction from ray origin to the sphere center
        Vector L = center.subtract(p0);
        double t_ca = v.dotProduct(L);
        double d2 = L.dotProduct(L) - t_ca * t_ca;
        double r2 = radius * radius;
        // no intersections if distance from center to ray > radius
        if (d2 > r2)
            return null;
        double t_hc = Math.sqrt(r2 - d2);
        double t1 = t_ca - t_hc;
        double t2 = t_ca + t_hc;

        List<Point> intersections = new ArrayList<>();
        // Only consider intersections in front of the ray (t > 0)
        if (Util.isZero(t1) || t1 > 0)
            intersections.add(p0.add(v.scale(t1)));
        if (!Util.isZero(t2) && t2 > 0) {
            // If t1 and t2 are nearly equal (tangent) return one point only
            if (intersections.isEmpty() || !Util.isZero(t2 - t1))
                intersections.add(p0.add(v.scale(t2)));
        }
        return intersections.isEmpty() ? null : intersections;
    }
}
