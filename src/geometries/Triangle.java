package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import static primitives.Util.isZero;
import java.util.List;

/**
 * Triangle class represents a triangle in 3D Cartesian coordinate
 * @author Yehuda rubin and arye hacohen
 */
public class Triangle extends Polygon{
    /**
     * constructor
     * @param p1 will be the first point of the new triangle
     * @param p2 will be the second point of the new triangle
     * @param p3 will be the third point of the new triangle
     */
    public Triangle(Point p1, Point p2, Point p3) {
        super(p1, p2, p3);
    }

    /**
     * findIntersections method
     * @param ray the ray to check for intersections with the triangle
     * @return a list of intersection points or null if there are no intersections
     */
    @Override
    public List<Point> findIntersections(Ray ray) {
        // Step 1: Use the plane of the triangle to find the intersection point
        List<Point> planeIntersections = plane.findIntersections(ray);
        if (planeIntersections == null) {
            return null; // No intersection with the plane
        }

        Point p0 = ray.getPoint(0); // Ray origin
        Vector v = ray.getDirection(); // Ray direction

        // Step 2: Create vectors from the ray origin to the vertices of the triangle
        Vector v1 = vertices.get(0).subtract(p0);
        Vector v2 = vertices.get(1).subtract(p0);
        Vector v3 = vertices.get(2).subtract(p0);

        // Step 3: Calculate the normal of the planes formed by the edges of the triangle and the ray
        Vector n1 = v1.crossProduct(v2).normalize();
        Vector n2 = v2.crossProduct(v3).normalize();
        Vector n3 = v3.crossProduct(v1).normalize();

        // Step 4: Check if the intersection point is inside the triangle
        boolean positive1 = n1.dotProduct(v) > 0;
        boolean positive2 = n2.dotProduct(v) > 0;
        boolean positive3 = n3.dotProduct(v) > 0;

        if ((positive1 == positive2) && (positive2 == positive3)) {
            return planeIntersections; // The intersection point is inside the triangle
        }

        return null; // The intersection point is outside the triangle
    }
}
