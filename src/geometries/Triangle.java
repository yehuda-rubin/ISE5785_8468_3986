package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import static primitives.Util.alignZero;
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
        List<Point> intersectionPoints = plane.findIntersections(ray);
        if (intersectionPoints == null) {
            return null;
        }
        Point intersectionPoint = intersectionPoints.get(0);

        Point vertexA = vertices.get(0);
        Point vertexB = vertices.get(1);
        Point vertexC = vertices.get(2);

        Vector triangleNormal = plane.getNormal(vertexA);

        try {
            if (isZero(vertexA.subtract(vertexB).crossProduct(intersectionPoint.subtract(vertexB)).dotProduct(triangleNormal))
                    || isZero(vertexC.subtract(vertexB).crossProduct(intersectionPoint.subtract(vertexB)).dotProduct(triangleNormal))
                    || isZero(vertexA.subtract(vertexC).crossProduct(intersectionPoint.subtract(vertexC)).dotProduct(triangleNormal))) {
                return null;
            }
        } catch (IllegalArgumentException e) {
            return null;
        }

        double totalArea = vertexA.subtract(vertexB).crossProduct(vertexA.subtract(vertexC)).dotProduct(triangleNormal);

        double areaAlpha = vertexC.subtract(vertexB).crossProduct(intersectionPoint.subtract(vertexB)).dotProduct(triangleNormal);
        double areaBeta = vertexA.subtract(vertexC).crossProduct(intersectionPoint.subtract(vertexC)).dotProduct(triangleNormal);
        double areaGamma = vertexB.subtract(vertexA).crossProduct(intersectionPoint.subtract(vertexA)).dotProduct(triangleNormal);

        double alpha = areaAlpha / totalArea;
        double beta = areaBeta / totalArea;
        double gamma = areaGamma / totalArea;

        if (alignZero(alpha) > 0 && alignZero(beta) > 0 && alignZero(gamma) > 0) {
            return List.of(intersectionPoint);
        }

        return null;
    }
}
