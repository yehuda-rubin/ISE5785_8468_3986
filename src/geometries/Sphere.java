package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Util;
import primitives.Vector;

import java.util.List;

import static primitives.Util.alignZero;
import static primitives.Util.isZero;

/**
 * The Sphere class extends the RadialGeometry class
 *
 * @author Yehuda Rubin and Arye Hacohen
 */
public class Sphere extends RadialGeometry {
    /**
     * The center of the sphere
     */
    private final Point center;

    /**
     * constructor
     *
     * @param radius           will be the radius of the new sphere
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
     *
     * @return the center of the sphere
     */
    @Override
    public Vector getNormal(Point point) {
        return point.subtract(center).normalize();
    }

    /**
     * @param ray the ray to check for intersections with the sphere
     * @return a list of intersection points or null if there are no intersections
     */
    @Override
    public List<Intersection> calculateIntersectionsHelper(Ray ray, double maxDistance) {
        // Point that represents the ray's head
        final Point rayPoint = ray.getPoint(0);

        // in case the ray's head is the sphere's center, we calculate the one intersection directly
        if(rayPoint.equals(center))
            return List.of(new Intersection(this, ray.getPoint(radius)));

        final Vector u = center.subtract(rayPoint);
        final double tm = ray.getDirection().dotProduct(u);
        final double d = Math.sqrt(u.lengthSquared() - tm * tm);
        // if (d ≥ r) there are no intersections
        if( alignZero(d - radius) > 0)
            return null;

        final double th = Math.sqrt(radius * radius - d * d);
        // in case the ray is tangent to the sphere, there are no intersections
        if(isZero(th))
            return null;
        final double t1 = alignZero(tm - th);
        final double t2 = alignZero(tm + th);

        // 2 intersections
        if(t1 > 0 && t2 > 0 && alignZero(t1 - maxDistance) <= 0 && alignZero(t2 - maxDistance) <= 0) {
            Intersection intersection1 = new Intersection(this, ray.getPoint(t1));
            Intersection intersection2 = new Intersection(this, ray.getPoint(t2));
            return List.of(intersection1, intersection2);
        }
        // 1 intersection
        else if(t1 > 0 && alignZero(t1 - maxDistance) <= 0)
            return List.of(new Intersection(this, ray.getPoint(t1)));
            // 1 intersection
        else if(t2 > 0 && alignZero(t2 - maxDistance) <= 0)
            return List.of(new Intersection(this, ray.getPoint(t2)));
            // 0 intersections
        else
            return null;
    }
}
