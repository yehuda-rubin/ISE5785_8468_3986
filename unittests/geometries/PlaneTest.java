package geometries;

import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Vector;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for geometries.Plane class
 * @author Yehuda Rubin and Arye Hacohen
 */
class PlaneTest {

    /**
     * test for the constructor {@link geometries.Plane#Plane(primitives.Point, primitives.Vector)}.
     * @throws Exception if the test fails
     * @see geometries.Plane#Plane(primitives.Point, primitives.Vector)
     * @author Yehuda Rubin and Arye Hacohen
     */
    @Test
    void testConstructor() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: Correct plane
        assertDoesNotThrow(() -> new Plane(new Point(0, 0, 0), new Vector(0, 0, 1)), "Failed constructing a correct plane");
    }

    /**
     * test for the getNormal function {@link geometries.Plane#getNormal(geometries.Plane)}
     * @author Yehuda Rubin and Arye Hacohen
     */
    @Test
    void getNormal() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: Normal to a plane
        Plane p = new Plane(new Point(0, 0, 0), new Vector(0, 0, 1));
        assertEquals(new Vector(0, 0, 1), p.getNormal(new Point(0, 0, 1)), "Normal to a plane does not work correctly");
    }
}