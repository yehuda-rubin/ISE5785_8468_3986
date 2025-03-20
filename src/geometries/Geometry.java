package geometries;
import primitives.Point;
import primitives.Vector;

/**
 * The Geometry class is the base
 * @author Yehuda Rubin and Arye Hacohen
 */
public abstract class Geometry {
    public abstract Vector getNormal(Point point);
}
