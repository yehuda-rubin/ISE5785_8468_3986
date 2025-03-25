package primitives;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * unit tests for primitives.Point class
 * @author Yehuda Rubin and Arye Hacohen.
 */
class PointTest {
    private final double DELTA = 0.00001;
    private final Vector v1 = new Vector(1, 2, 3);
    private final Point p1 = new Point(1, 2, 3);

    /**
     * test for the constructor {@link primitives.Point#Point(double, double, double)} and {@link primitives.Point#Point(Double3)}
     */
    @Test
    void testConstructor() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: Correct point
        assertDoesNotThrow(() -> p1, "Failed constructing a correct point");

        // TC02: Correct point
        assertDoesNotThrow(() -> new Point(new Double3(1, 2, 3)), "Failed constructing a correct point");
    }

    /**
     * test for the subtract function {@link primitives.Point#subtract(primitives.Point)}
     * @author Yehuda Rubin and Arye Hacohen
     */
    @Test
    void subtract() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: Subtract two points
        assertEquals(new Vector(1, 2, 3), new Point(2, 4, 6).subtract(new Point(1, 2, 3)), "Subtract two points does not work correctly");

        // ============ Boundary Values Tests ==================
        // TC02: Subtract equal points
        assertThrows(IllegalArgumentException.class, () -> new Point(1, 2, 3).subtract(new Point(1, 2, 3)), "Subtract equal points does not work correctly");
    }

    /**
     * test for the add function {@link primitives.Point#add(primitives.Vector)}
     * @author Yehuda Rubin and Arye Hacohen
     */
    @Test
    void add() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: Add a point and a vector
        assertEquals(new Point(2, 4, 6), new Point(1, 2, 3).add(new Vector(1, 2, 3)), "Add a point and a vector does not work correctly");
    }

    /**
     * test for the distanceSquared function {@link primitives.Point#distanceSquared(primitives.Point)}
     * @author Yehuda Rubin and Arye Hacohen
     */
    @Test
    void distanceSquared() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: Distance squared between two points
        assertEquals(14, new Point(1, 2, 3).distanceSquared(new Point(2, 4, 6)), DELTA, "Distance squared between two points does not work correctly");

        // ============ Boundary Values Tests ==================
        // TC02: Distance squared between two equal points
        assertEquals(0, new Point(1, 2, 3).distanceSquared(new Point(1, 2, 3)), DELTA, "Distance squared between two equal points does not work correctly");
    }

    /**
     * test for the distance function {@link primitives.Point#distance(primitives.Point)}
     * @author Yehuda Rubin and Arye Hacohen
     */
    @Test
    void distance() {
    }

    /**
     * test for the equals function {@link primitives.Point#equals(Object)}
     * @author Yehuda Rubin and Arye Hacohen
     */
    @Test
    void testEquals() {
    }

    /**
     * test for the toString function {@link primitives.Point#toString()}
     * @author Yehuda Rubin and Arye Hacohen
     */
    @Test
    void testToString() {
    }
}