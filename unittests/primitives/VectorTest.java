package primitives;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * uint tests for primitives.Vector class
 * @author Yehuda Rubin and Arye Hacohen.
 */
class VectorTest {
    private final double DELTA = 0.00001;
    final Vector v1 = new Vector(1, 2, 3);
    final Vector v2 = new Vector(-2, -4, -6);

    /**
     * test for the constructor {@link primitives.Vector#Vector(double, double, double)} and {@link primitives.Vector#Vector(Double3)}.
     */
    @Test
    void testConstructor() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: Correct vector
        assertDoesNotThrow(() -> v1, "Failed constructing a correct vector");

        // TC02: Correct vector
        assertDoesNotThrow(() -> new Vector(new Double3(1,2,3)), "Failed constructing a correct vector");
    }
    /**
     * test for the subtract function {@link primitives.Point#subtract(primitives.Point)}
     * @author Yehuda Rubin and Arye Hacohen
     */
    @Test
    void subtract() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: Subtract two vectors
        assertEquals(new Vector(3, 6, 9), v1.subtract(v2), "Subtract two vectors does not work correctly");

        // ============ Boundary Values Tests ==================
        // TC02: Subtract equal vectors
        assertThrows(IllegalArgumentException.class, () -> v1.subtract(v1), "Subtract equal vectors does not work correctly");
    }
    /**
     * test for the add function {@link primitives.Vector#add(primitives.Vector)}
     * @author Yehuda Rubin and Arye Hacohen
     */
    @Test
    void add() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: Add two vectors
        assertEquals(new Vector(-1, -2, -3), v1.add(v2), "Add two vectors does not work correctly");
    }

    /**
     * test for the scale function {@link primitives.Vector#scale(double)}.
     * @author Yehuda Rubin and Arye Hacohen
     */
    @Test
    void scale() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: Scale a vector
        assertEquals(new Vector(2, 4, 6), v1.scale(2), "Scale a vector does not work correctly");
    }

    /**
     * test for the dotProduct function {@link primitives.Vector#dotProduct(primitives.Vector)}.
     * @author Yehuda Rubin and Arye Hacohen
     */
    @Test
    void dotProduct() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: Dot product of two vectors
        assertEquals(-28, v1.dotProduct(v2), "Dot product of two vectors does not work correctly");

        // ============ Boundary Values Tests ==================
        // TC02: Dot product of two orthogonal vectors
        assertEquals(0, v1.dotProduct(new Vector(0, 0, 1)), "Dot product of two orthogonal vectors does not work correctly");
    }

    /**
     * test for the crossProduct function {@link primitives.Vector#crossProduct(primitives.Vector)}.
     * @author Yehuda Rubin and Arye Hacohen
     */
    @Test
    void crossProduct() {
        // ============ Boundary Values Tests ==================
        // TC01: Cross product of two orthogonal vectors
        assertEquals(new Vector(0, 0, 14), v1.crossProduct(new Vector(0, 0, 1)), "Cross product of two orthogonal vectors does not work correctly");
    }

    /**
     * test for the lengthSquared function {@link primitives.Vector#lengthSquared()}.
     * @author Yehuda Rubin and Arye Hacohen
     */
    @Test
    void lengthSquared() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: Length squared of a vector
        assertEquals(14, v1.lengthSquared(), DELTA, "Length squared of a vector does not work correctly");

        // ============ Boundary Values Tests ==================

        // TC02: Length squared of a negative vector
        assertEquals(14, v1.scale(-1).lengthSquared(), DELTA, "Length squared of a negative vector does not work correctly");
    }

    /**
     * test for the length function {@link primitives.Vector#length()}
     * @author Yehuda Rubin and Arye Hacohen
     */
    @Test
    void length() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: Length of a vector
        assertEquals(Math.sqrt(14), v1.length(), DELTA, "Length of a vector does not work correctly");

        // ============ Boundary Values Tests ==================

        // TC02: Length of a negative vector
        assertEquals(Math.sqrt(14), v1.scale(-1).length(), DELTA, "Length of a negative vector does not work correctly");
    }

    /**
     * test for the normalize function {@link primitives.Vector#normalize()}.
     * @author Yehuda Rubin and Arye Hacohen
     */
    @Test
    void normalize() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: Normalize a vector
        assertEquals(new Vector(1 / Math.sqrt(14), 2 / Math.sqrt(14), 3 / Math.sqrt(14)), v1.normalize(), "Normalize a vector does not work correctly");

        // ============ Boundary Values Tests ==================

        // TC02: Normalize a negative vector
        assertEquals(new Vector(-1 / Math.sqrt(14), -2 / Math.sqrt(14), -3 / Math.sqrt(14)), v1.scale(-1).normalize(), "Normalize a negative vector does not work correctly");
    }
}