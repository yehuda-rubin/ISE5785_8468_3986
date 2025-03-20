package primitives;

public class Ray {
    private final Point head;
    private final Vector direction;

    public Ray(Point head, Vector direction) {
        this.head = head;
        this.direction = direction.normalize();
    }
}
