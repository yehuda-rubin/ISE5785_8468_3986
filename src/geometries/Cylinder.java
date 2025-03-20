package geometries;

import primitives.Point;
import primitives.Vector;
import primitives.Ray;

/**
 * The Cylinder class extends the
 * @author Yehuda Rubin and Arye Hacohen
 */
public class Cylinder extends Tube{
    private double height;

    /**
     * constructor
     * @param axis will be the axis of the new cylinder
     * @param radius will be the radius of the new cylinder
     * @param height will be the height of the new cylinder
     */
    public Cylinder(Ray axis, double radius, double height) {
        super(axis, radius);
        this.height = height;
    }

    /**
     * @return the normal of the cylinder
     */
    @Override
    public Vector getNormal(Point point) {
        return null;
    }
}
