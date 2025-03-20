package geometries;

import primitives.Point;
import primitives.Vector;

public class Plane extends Geometry{
    private final Point q0;
    private final Vector normal;

    public Plane(Point q0, Vector normal) {
        this.q0 = q0;
        this.normal = normal.normalize();
    }

    public Plane(Point p1, Point p2, Point p3) {
        q0 = p1;
        normal = null;
    }

    @Override
    public Vector getNormal(Point point) {
        return normal;
    }
}
