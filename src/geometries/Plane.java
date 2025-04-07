package geometries;

import primitives.Point;
import primitives.Vector;
import primitives.Ray;
import java.util.List;

import static primitives.Util.isZero;

/**
 * The Plane class extends the Geometry class
 * @author Yehuda Rubin and Arye Hacohen
 */

public class Plane extends Geometry{
    private final Point q0;
    private final Vector normal;

    /**
     * constructor
     * @param q0 will be the point of the new plane
     * @param normal will be the normal of the new plane
     */

    public Plane(Point q0, Vector normal) {
        this.q0 = q0;
        this.normal = normal.normalize();
    }

    /**
     * constructor
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

    @Override
    public List<Point> findIntersections(Ray ray) {
        Vector direction = ray.getDirection();
        Point p0 = ray.getHead(0);
        if(isZero(direction.dotProduct(normal)) || q0.equals(p0)) {
            return null; // the ray is parallel to the plane
        }
        double t = normal.dotProduct(q0.subtract(p0)) / normal.dotProduct(direction);
        if (t <= 0) {
            return null; // the ray is pointing away from the plane
        }

        Point intersectionPoint = p0.add(direction.scale(t));
        if (isZero(intersectionPoint.distance(q0))) {
            return null; // the ray is on the plane
        }

        return List.of(intersectionPoint); // the ray intersects the plane
    }
}
