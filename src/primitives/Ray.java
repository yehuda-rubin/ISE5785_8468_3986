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
}
