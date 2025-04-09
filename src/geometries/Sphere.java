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
        Point p0 = ray.getPoint(0);
        Vector dir = ray.getDirection();

        // if the ray starts at the center of the sphere
        if (center.equals(p0))
            return List.of(p0.add(dir.scale(radius)));

        Vector u = (center.subtract(p0));
        double tm = dir.dotProduct(u);
        double d = Util.alignZero(Math.sqrt(u.lengthSquared() - tm * tm));
        if (d >= radius)
            return null;

        double th = Math.sqrt(radius * radius - d * d);
        double t1 = Util.alignZero(tm - th);
        double t2 = Util.alignZero(tm + th);

        // if the ray starts before the sphere
        if (t1 > 0 && t2 > 0)
            return List.of(p0.add(dir.scale(t1)), p0.add(dir.scale(t2)));

        // if the ray starts inside the sphere
        if (t1 > 0)
            return List.of(p0.add(dir.scale(t1)));
        if (t2 > 0)
            return List.of(p0.add(dir.scale(t2)));

        return null;
    }
}
