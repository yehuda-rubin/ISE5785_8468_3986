package geometries;

import primitives.Point;
import primitives.Vector;

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
        normal = null;
    }

    /**
     * @return the normal of the plane
     */
    @Override
    public Vector getNormal(Point point) {
        return normal;
    }
}
