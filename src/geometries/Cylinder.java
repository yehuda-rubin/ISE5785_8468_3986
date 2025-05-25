package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.List;

/**
 * The Cylinder class extends the Tube class and represents a cylinder in 3D space.
 *
 * @author Yehuda Rubin and Arye Hacohen
 */
public class Cylinder extends Tube {
    private final double height;

    /**
     * constructor
     *
     * @param axis   will be the axis of the new cylinder
     * @param radius will be the radius of the new cylinder
     * @param height will be the height of the new cylinder
     */

    public Cylinder(Ray axis, double radius, double height) {
        super(axis, radius);
        this.height = height;
        if (height <= 0) {
            throw new IllegalArgumentException("Height must be positive");
        }
    }

    @Override
    public Vector getNormal(Point point) {
        Point p0 = axis.getPoint(0);
        Vector v = axis.getDirection();
        Vector p0_p = point.subtract(p0);
        double t = v.dotProduct(p0_p);
        Point o = p0.add(v.scale(t));
        // Check if the point is on the top or bottom base of the cylinder
        if (point.subtract(o).length() == radius) {
            return point.subtract(o).normalize();
        }
        // Check if the point is on the side of the cylinder
        if (point.subtract(p0).length() == radius) {
            return p0.subtract(point).normalize();
        }
        // Check if the point is on the top or bottom base of the cylinder
        if (point.subtract(p0.add(v.scale(height))).length() == radius) {
            return point.subtract(p0.add(v.scale(height))).normalize();
        }
        return null;
    }

    /**
     * @param ray the ray to check for intersections with the cylinder
     * @return null since the method is not implemented
     */
    @Override
    public List<Intersection> calculateIntersectionsHelper(Ray ray) {
        return null;
    }
}
