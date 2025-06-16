package geometries;

import primitives.AABB;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.List;

import static primitives.Util.alignZero;
import static primitives.Util.isZero;

/**
 * The Plane class extends the Geometry class
 *
 * @author Yehuda Rubin and Arye Hacohen
 */

public class Plane extends Geometry {
    /**
     * The point on the plane
     */
    private final Point q0;
    /**
     * The normal vector of the plane
     */
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
    public List<Intersection> calculateIntersectionsHelper(Ray ray, double maxDistance) {
        // Point that represents the ray's head
        final Point rayPoint = ray.getPoint(0);
        // Vector that represents the ray's axis
        final Vector rayVector = ray.getDirection();

        // in case the ray's head is the reference point in the plane, there are no intersections
        if(rayPoint.equals(q0))
            return null;

        // numerator for the formula
        final double numerator = normal.dotProduct(q0.subtract(rayPoint));
        // denominator for the formula
        final double denominator = normal.dotProduct(rayVector);
        // in case ray is parallel to the plane
        if (isZero(denominator))
            return null;

        final double t = numerator / denominator;

        // if (0 ≥ t) or (maxDistance < t) there are no intersections
        return (alignZero(t) > 0 && alignZero(t - maxDistance) <= 0) ?
                List.of(new Intersection(this, ray.getPoint(t))) : null;
    }


    @Override
    protected AABB calculateBoundingBox() {
        // A plane is infinite in size, so it does not have a bounding box
        return null;
    }
}