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
    }

    /**
     * test for the add function {@link primitives.Point#add(primitives.Vector)}
     * @author Yehuda Rubin and Arye Hacohen
     */
    @Test
    void add() {
    }

    /**
     * test for the distanceSquared function {@link primitives.Point#distanceSquared(primitives.Point)}
     * @author Yehuda Rubin and Arye Hacohen
     */
    @Test
    void distanceSquared() {
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