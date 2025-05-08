package primitives;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RayTest {
    private final Point p1 = new Point(1, 2, 3);
    private final Point p2 = new Point(2, 2, 3);
    private final Point p3 = new Point(3, 2, 3);
    private final Point p4 = new Point(4, 2, 3);
    private final Ray ray = new Ray(p1, new Vector(1, 0, 0));

    @Test
    void getPoint() {
        // ============ Equivalence Partitions Tests ==============
        Ray ray = new Ray(new Point(1, 2, 3), new Vector(1, 0, 0));

        // TC01: negative distance
        assertEquals(new Point(0, 2, 3), ray.getPoint(-1), "getPoint() with negative distance does not work correctly");

        // TC02: zero distance
        assertEquals(new Point(1, 2, 3), ray.getPoint(0), "getPoint() with zero distance does not work correctly");

        // TC03: positive distance
        assertEquals(new Point(2, 2, 3), ray.getPoint(1), "getPoint() with positive distance does not work correctly");
    }

    @Test
    void FindClosestPoint() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: the point in the middle of the list is the closest
        assertEquals(new Point(2, 2, 3), ray.findClosestPoint(List.of(p3, p2, p4)), "Bad findClosestPoint");

        // ================= Boundary Values Tests =================
        // TC02: the list is empty
        assertNull(ray.findClosestPoint(List.of()), "Bad findClosestPoint with empty list");

        // TC03: the point in the beginning of the list is the closest
        assertEquals(p2, ray.findClosestPoint(List.of(p2, p3, p4)), "Bad findClosestPoint");

        // TC04: the point in the end of the list is the closest
        assertEquals(p2, ray.findClosestPoint(List.of(p4, p3, p2)), "Bad findClosestPoint");
    }
}