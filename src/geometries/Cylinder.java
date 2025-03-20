package geometries;

import primitives.Point;
import primitives.Vector;
import primitives.Ray;
public class Cylinder extends Tube{
    private double height;

    public Cylinder(Ray axis, double radius, double height) {
        super(axis, radius);
        this.height = height;
    }
    @Override
    public Vector getNormal(Point point) {
        return null;
    }
}
