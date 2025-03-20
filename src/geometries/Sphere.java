package geometries;

import primitives.Point;
import primitives.Vector;

public class Sphere extends RadialGeometry{
    public Point center;

    public Sphere(double radius, Point center) {
        super(radius);
        this.center = center;
    }

    @Override
    public Vector getNormal(Vector point) {
        return null;
    }
}
