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
    void findIntersections() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: Ray intersects inside the tube
        Tube tube = new Tube(new Ray(new Point(0, 0, 0), new Vector(0, 0, 1)), 1);
        Ray ray1 = new Ray(new Point(0, 0, 2), new Vector(1, 0, -1));
        assertEquals(List.of(new Point(1, 0, 1)), tube.findIntersections(ray1), "Ray intersects inside the tube - should return 1 point");

        // TC02: Ray intersects outside against edge
        Ray ray2 = new Ray(new Point(2, 0, 2), new Vector(0, 0, -1));
        assertNull(tube.findIntersections(ray2), "Ray outside against edge - should return null");

        // TC03: Ray intersects outside against vertex
        Ray ray3 = new Ray(new Point(-1, -1, 2), new Vector(0, 0, -1));
        assertNull(tube.findIntersections(ray3), "Ray outside against vertex - should return null");

        // =============== Boundary Value Tests ==================

        // TC11: Ray intersects on edge
        Ray ray4 = new Ray(new Point(1, 0, 2), new Vector(0, 0, -1));
        assertNull(tube.findIntersections(ray4), "Ray on edge - should return null");

        // TC12: Ray intersects on vertex
        Ray ray5 = new Ray(new Point(0, 0, 2), new Vector(0, 0, -1));
        assertNull(tube.findIntersections(ray5), "Ray on vertex - should return null");

        // TC13: Ray intersects on edge extension
        Ray ray6 = new Ray(new Point(3, 0, 2), new Vector(0, 0, -1));
        assertNull(tube.findIntersections(ray6), "Ray on edge extension - should return null");

        // TC14: Ray intersects on edge extension
        Ray ray7 = new Ray(new Point(0, 0, 2), new Vector(1, 0, -1));
        assertEquals(List.of(new Point(1, 0, 1)), tube.findIntersections(ray7), "Ray on edge extension - should return 1 point");

        // TC15: Ray intersects on edge extension
        Ray ray8 = new Ray(new Point(0, 0, 2), new Vector(-1, 0, -1));
        assertEquals(List.of(new Point(-1, 0, 1)), tube.findIntersections(ray8), "Ray on edge extension - should return 1 point");

        // TC16: Ray intersects on edge extension
        Ray ray9 = new Ray(new Point(0, 0, 2), new Vector(0, 1, -1));
        assertEquals(List.of(new Point(0, 1, 1)), tube.findIntersections(ray9), "Ray on edge extension - should return 1 point");

        // TC17: Ray intersects on edge extension
        Ray ray10 = new Ray(new Point(0, 0, 2), new Vector(0, -1, -1));
        assertEquals(List.of(new Point(0, -1, 1)), tube.findIntersections(ray10), "Ray on edge extension - should return 1 point");

    }
}