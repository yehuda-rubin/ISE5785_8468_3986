package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Util;
import primitives.Vector;

import java.util.List;

import static primitives.Util.alignZero;
import static primitives.Util.isZero;

/**
 * Triangle class represents a triangle in 3D Cartesian coordinate
 *
 * @author Yehuda rubin and arye hacohen
 */
public class Triangle extends Polygon {
    /**
     * constructor
     *
     * @param p1 will be the first point of the new triangle
     * @param p2 will be the second point of the new triangle
     * @param p3 will be the third point of the new triangle
     */
    public Triangle(Point p1, Point p2, Point p3) {
        super(p1, p2, p3);
    }

    /**
     * findIntersections method
     *
     * @param ray the ray to check for intersections with the triangle
     * @return a list of intersection points or null if there are no intersections
     */
    @Override
    protected List<Intersection> calculateIntersectionsHelper(Ray ray, double maxDistance) {
        // test the intersections with triangle’s plane
        // we prefer to use the helper method so that we already check the distance
        final var intersections = plane.calculateIntersections(ray, maxDistance);
        if (intersections == null)
            return null;

        // Point that represents the ray's head
        final Point rayPoint = ray.getPoint(0);
        // Vector that represents the ray's axis
        final Vector rayVector = ray.getDirection();

        // vector1, vector2, vector3 can't be the ZERO Vector because it happens only if rayPoint = P1/P2/P3,
        // which means the ray begins at the plane and there are no intersections with the plane at all,
        // so we would have exit this method already because of the first condition
        final Vector vector1 = vertices.get(0).subtract(rayPoint);
        final Vector vector2 = vertices.get(1).subtract(rayPoint);
        final Vector vector3 = vertices.get(2).subtract(rayPoint);

        // normal1, normal2, normal3 can't be the ZERO Vector because it happens only if:
        // vector1 and vector2 or vector2 and vector3 or vector3 and vector1
        // are on the same line, which means rayPoint is on one of the triangle's edges,
        // which means the ray begins at the plane and there are no intersections with the plane at all,
        // so we would have exit this method already because of the first condition
        final Vector normal1 = vector1.crossProduct(vector2).normalize();
        final Vector normal2 = vector2.crossProduct(vector3).normalize();
        final Vector normal3 = vector3.crossProduct(vector1).normalize();

        final double s1 = alignZero(rayVector.dotProduct(normal1));
        final double s2 = alignZero(rayVector.dotProduct(normal2));
        final double s3 = alignZero(rayVector.dotProduct(normal3));

        // the point is inside the triangle only if s1, s2 and s3 have the same sign and none of them is 0
        if ((s1>0 && s2>0 && s3>0) || (s1<0 && s2<0 && s3<0)) {
            Intersection intersection = intersections.getFirst();
            return List.of(new Intersection(this, intersection.point));
        }

        return null;
    }
}
