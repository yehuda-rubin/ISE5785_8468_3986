package primitives;

import java.util.List;
import geometries.Intersectable.Intersection;

import static primitives.Util.isZero;

/**
 * This class make ray in 3D space
 * head point and direction vector
 *
 * @author Yehuda Rubin and Arye Hacohen
 */

public class Ray {
    /*
    * head point of the ray
    */
    private final Point head;
    /**
     * direction vector of the ray
     * it is normalized to ensure it has a length of 1
     */
    private final Vector direction;

    /**
     * constructor
     *
     * @param head      will be the head of the new ray
     * @param direction will be the direction of the new ray
     */

    public Ray(Point head, Vector direction) {
        this.head = head;
        this.direction = direction.normalize();
    }

    /**
     * getter for the head of the ray
     *
     * @return the head of the ray
     */
    public Point getPoint(double distance) {
        if (isZero(distance))
            return head;
        return head.add(direction.scale(distance));
    }

    /**
     * getter for the direction of the ray
     *
     * @return the direction of the ray
     */
    public Vector getDirection() {
        return direction;
    }

    @Override
    public int hashCode() {
        int result = head.hashCode();
        result = 31 * result + getDirection().hashCode();
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        return obj instanceof Ray other
                && head.equals(other.head)
                && direction.equals(other.direction);
    }

    @Override
    public String toString() {
        return "" + head + direction;
    }

    /**
     * Find the closest intersection point from a list of intersections
     *
     * @param intersections list of intersections to check
     * @return the closest intersection point to the ray's head
     */
    public Intersection findClosestIntersection(List<Intersection> intersections) {
        // Check if the list is null or empty
        if (intersections == null || intersections.isEmpty()) {
            return null;
        }
        Intersection closestIntersection = intersections.get(0);
        double minDistance = head.distanceSquared(closestIntersection.point);

        // Iterate through the list of intersections to find the closest one
        for (int i = 1; i < intersections.size(); i++) {
            Intersection currentIntersection = intersections.get(i);
            double currentDistance = head.distanceSquared(currentIntersection.point);
            if (currentDistance < minDistance) {
                minDistance = currentDistance;
                closestIntersection = currentIntersection;
            }
        }
        return closestIntersection;
    }

    /**
     * Find the closest point from a list of points
     *
     * @param points list of points to check
     * @return the closest point to the ray's head
     */
    public Point findClosestPoint(List<Point> points) {
        return points == null ? null
                : findClosestIntersection(points.stream().map(p -> new Intersection(null, p)).toList()).point;
    }
}
