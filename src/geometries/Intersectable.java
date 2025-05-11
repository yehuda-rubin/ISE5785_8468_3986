package geometries;

import primitives.Point;
import primitives.Ray;

import java.util.List;

/**
 * The Intersectable interface defines the method for finding intersections
 * between a ray and geometries.
 *
 * @author Yehuda Rubin and Arye Hacohen
 */
public interface Intersectable {
    /**
     * Finds intersections of the given ray with the geometry.
     *
     * @param ray The ray to find intersections with.
     * @return A list of intersection points, or null if no intersections are found.
     */
    List<Point> findIntersections(Ray ray);
}
