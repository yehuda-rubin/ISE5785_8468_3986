package geometries;

import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for geometries.Tube class
 * @author Yehuda Rubin and Arye Hacohen
 */

class TubeTest {

    /**
     * test for the constructor {@link geometries.Tube#Tube(Ray, double)}
     */

    @Test
    void testConstructor() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: Correct tube
        assertDoesNotThrow(() -> new Tube(new Ray(new Point(0, 0, 0), new Vector(0, 0, 1)), 1), "Failed constructing a correct tube");

        // ============ Boundary Values Tests ==================
        // TC02: Incorrect tube
        assertThrows(IllegalArgumentException.class, () -> new Tube(new Ray(new Point(0, 0, 0), new Vector(0, 0, 1)), -1), "Failed constructing a correct tube");
    }
    /**
     * test for the getNormal function {@link geometries.Tube#getNormal(Point)}
     */

    @Test
    void getNormal() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: Normal to a tube
        Tube t = new Tube(new Ray(new Point(0, 0, 0), new Vector(0, 0, 1)), 1);
        assertEquals(new Vector(1, 0, 0), t.getNormal(new Point(1, 0, 1)), "Normal to a tube does not work correctly");

        // ============ Boundary Values Tests ==================
        // TC02: Normal to a tube
        // The normal to the top base of the tube
        assertEquals(new Vector(1, 0, 0), t.getNormal(new Point(1, 0, 2)), "Normal to the top base of the tube does not work correctly");

        // TC03: Normal to a tube
        // The normal to the bottom base of the tube
        assertEquals(new Vector(1, 0, 0), t.getNormal(new Point(1, 0, -1)), "Normal to the bottom base of the tube does not work correctly");

        // TC04: Normal to a tube
        // The normal to the bottom base of the tube
        assertEquals(new Vector(1, 0, 0), t.getNormal(new Point(1, 0, -2)), "Normal to the bottom base of the tube does not work correctly");

    }

    /**
     * test for the findIntersections function {@link geometries.Tube#findIntersections(Ray)}
     */
    @Test
    void testFindIntersections() {
        Tube tube = new Tube(new Ray(new Point(0, 0, 0), new Vector(0, 0, 1)), 1);

        // ============ Equivalence Partitions Tests ==============

        // TC01: Ray intersects the tube at two points
        Ray ray1 = new Ray(new Point(-2, 0, 1), new Vector(2, 0, 0));
        assertEquals(List.of(new Point(1, 0, 1), new Point(-1, 0, 1)),
                tube.findIntersections(ray1), "Ray intersects the tube at two points");

        // TC02: Ray starts inside the tube and intersects it at one point
        Ray ray2 = new Ray(new Point(0.5, 0, 1), new Vector(1, 0, 0));
        assertEquals(List.of(new Point(1, 0, 1)),
                tube.findIntersections(ray2), "Ray starts inside the tube and intersects it at one point");

        // TC03: Ray does not intersect the tube
        Ray ray3 = new Ray(new Point(2, 2, 1), new Vector(1, 1, 0));
        assertNull(tube.findIntersections(ray3), "Ray does not intersect the tube");

        // =============== Boundary Values Tests ==================

        // TC04: Ray is tangent to the tube
        Ray ray4 = new Ray(new Point(1, -1, 1), new Vector(0, 1, 0));
        assertEquals(List.of(new Point(1, 0, 1)),
                tube.findIntersections(ray4), "Ray is tangent to the tube");

        // TC05: Ray is parallel to the tube and does not intersect
        Ray ray5 = new Ray(new Point(2, 0, 1), new Vector(0, 0, 1));
        assertNull(tube.findIntersections(ray5), "Ray is parallel to the tube and does not intersect");

        // TC06: Ray starts on the surface of the tube and goes outward
        Ray ray6 = new Ray(new Point(1, 0, 1), new Vector(1, 0, 0));
        assertNull(tube.findIntersections(ray6), "Ray starts on the surface of the tube and goes outward");
    }
}