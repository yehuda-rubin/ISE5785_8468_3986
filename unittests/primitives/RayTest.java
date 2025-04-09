package primitives;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RayTest {

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
}