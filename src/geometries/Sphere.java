package geometries;

import primitives.Point;
import primitives.Vector;

/**
 * The Sphere class extends the RadialGeometry class
 * @author Yehuda Rubin and Arye Hacohen
 */
public class Sphere extends RadialGeometry{
    public Point center;

    /**
     * constructor
     * @param radius will be the radius of the new sphere
     * @param center will be the center of the new sphere
     */
    public Sphere(double radius, Point center) {
        super(radius);
        this.center = center;
    }

    /**
     * @return the normal of the sphere
     */
    @Override
    public Vector getNormal(Point point) {
        return null;
    }
}
