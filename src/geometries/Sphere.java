package geometries;

import primitives.Point;
import primitives.Vector;

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
}
