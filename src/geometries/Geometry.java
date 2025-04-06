package geometries;
import primitives.Point;
import primitives.Vector;

/**
 * The Geometry class is the base
 * @author Yehuda Rubin and Arye Hacohen
 */
public abstract class Geometry implements Intersectable {
    /**
     * getNormal function returns the normal vector to the geometry object
     * @param point - the point on the geometry object
     * @return the normal vector to the geometry object
     */
    public abstract Vector getNormal(Point point);

}
