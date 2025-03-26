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
     * test for the constructor {@link geometries.Plane#Plane(Point, Point, Point)}.
     * @throws Exception if the test fails
     * @see geometries.Plane#Plane(primitives.Point, primitives.Vector)
     * @author Yehuda Rubin and Arye Hacohen
     */
    @Test
    void testConstructor() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: Test for a proper result
        assertDoesNotThrow(() -> new Plane(new Point(1, 0, 0), new Point(0, 1, 0), new Point(0, 0, 1)), "Failed to create a proper plane");

        // =============== Boundary Values Tests =================
        // TC02: Test for a plane that the points are on the same line
        assertThrows(IllegalArgumentException.class, () -> new Plane(new Point(1, 0, 0), new Point(2, 0, 0), new Point(3, 0, 0)), "Failed to throw an exception when creating a plane with points on the same line");

        // TC03: Test for a plane that the points are converge
        assertThrows(IllegalArgumentException.class, () -> new Plane(new Point(0, 0 ,0), new Point(1, 1, 1), new Point(1, 1, 1)), "Failed to throw an exception when creating a plane with points that converge");

        // TC04: Test for a plane that the points are converge
        assertThrows(IllegalArgumentException.class, () -> new Plane(new Point(0, 0, 0), new Point(1, 1, 1), new Point(0, 0, 0)), "Failed to throw an exception when creating a plane with points on the same plane");

        // TC05: Test for a plane that the points are converge
        assertThrows(IllegalArgumentException.class, () -> new Plane(new Point(0, 0, 0), new Point(0, 0, 0), new Point(4, 2, 1)), "Failed to throw an exception when creating a plane with points on the same line");

        // TC06: Test for a plane that the points are converge
        assertThrows(IllegalArgumentException.class, () -> new Plane(new Point(0, 0, 0), new Point(0, 0, 0), new Point(0, 0, 0)), "Failed to throw an exception when creating a plane with points on the same point");

    }

    /**
     * test for the getNormal function {@link geometries.Plane#getNormal(Point)}
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