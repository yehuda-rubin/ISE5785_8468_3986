package geometries;
import primitives.Point;
import primitives.Vector;

public abstract class Geometry {
    public Vector getNormal(Point point) {
        return new Vector();
    }
}
