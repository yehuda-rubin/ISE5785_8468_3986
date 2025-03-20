package primitives;

/**
 * This class make ray in 3D space
 * head point and direction vector
 * @author Yehuda Rubin and Arye Hacohen
 */

public class Ray {
    private final Point head;
    private final Vector direction;

    /**
     * constructor
     * @param head will be the head of the new ray
     * @param direction will be the direction of the new ray
     */

    public Ray(Point head, Vector direction) {
        this.head = head;
        this.direction = direction.normalize();
    }

    /**
     * Compares the resulting object to the object on which the function is invoked.
     * @return true or false
     */

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        return obj instanceof Ray other
                && head.equals(other.head)
                && direction.equals(other.direction);
    }

    /**
     * print
     */

    @Override
    public String toString() {
        return "" + head + direction;
    }
}
