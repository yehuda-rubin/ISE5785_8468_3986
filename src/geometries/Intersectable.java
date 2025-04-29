package geometries;

import primitives.Ray;
import primitives.Point;
import java.util.List;

/**
 * The Intersectable interface defines the method for finding intersections
 * between a ray and geometries.
 * @author Yehuda Rubin and Arye Hacohen
 */
public interface Intersectable  {
    List<Point> findIntersections(Ray ray);
}
