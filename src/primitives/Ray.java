package primitives;

import java.util.List;

import static primitives.Util.isZero;

/**
 * This class make ray in 3D space
 * head point and direction vector
 *
 * @author Yehuda Rubin and Arye Hacohen
 */

public class Ray {
    private final Point head;
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
     * Find the closest point to the ray's head from a list of points
     *
     * @param points list of points to check
     * @return the closest point to the ray's head
     */
    public Point findClosestPoint(List<Point> points) {
        // Check if the list is null or empty
        if (points == null || points.isEmpty()) {
            return null;
        }
        Point closestPoint = points.get(0);
        double minDistance = head.distanceSquared(closestPoint);

        // Iterate through the list of points to find the closest one
        for (int i = 1; i < points.size(); i++) {
            Point currentPoint = points.get(i);
            double currentDistance = head.distanceSquared(currentPoint);
            if (currentDistance < minDistance) {
                minDistance = currentDistance;
                closestPoint = currentPoint;
            }
        }
        return closestPoint;
    }
}
