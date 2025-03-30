package geometries;

import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Vector;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for geometries.Sphere class
 * @author Yehuda Rubin and Arye Hacohen
 */
class SphereTest {

    /**
     * test for the constructor {@link geometries.Sphere#Sphere(double, Point)}
     */
    @Test
    void testConstructor() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: Correct sphere
        assertDoesNotThrow(() -> new Sphere(1, new Point(0, 0, 0)), "Failed constructing a correct sphere");

        // ============ Boundary Values Tests ==================
        // TC02: Incorrect sphere
        assertThrows(IllegalArgumentException.class, () -> new Sphere(-1, new Point(0, 0, 0)), "Failed constructing a correct sphere");
    }

    /**
     * test for the getNormal function {@link geometries.Sphere#getNormal(Point)}
     */
    @Test
    void getNormal() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: Normal to a sphere
        Sphere s = new Sphere(1, new Point(0, 0, 0));
        assertEquals(new Vector(1, 0, 0), s.getNormal(new Point(1, 0, 0)), "Normal to a sphere does not work correctly");

        // ============ Boundary Values Tests ==================
        // TC02: Normal to a sphere
        // The normal to the sphere at the point (0, 1, 0)
        assertEquals(new Vector(0, 1, 0), s.getNormal(new Point(0, 1, 0)), "Normal to a sphere does not work correctly");

        // TC03: Normal to a sphere
        // The normal to the sphere at the point (0, 0, 1)
        assertEquals(new Vector(0, 0, 1), s.getNormal(new Point(0, 0, 1)), "Normal to a sphere does not work correctly");

        // TC04: Normal to a sphere
        // The normal to the sphere at the point (0, -1, 0)
        assertEquals(new Vector(0, -1, 0), s.getNormal(new Point(0, -1, 0)), "Normal to a sphere does not work correctly");

        // TC05: Normal to a sphere
        // The normal to the sphere at the point (0, 0, -1)
        assertEquals(new Vector(0, 0, -1), s.getNormal(new Point(0, 0, -1)), "Normal to a sphere does not work correctly");

        // TC06: Normal to a sphere
        // The normal to the sphere at the point (-1, 0, 0)
        assertEquals(new Vector(-1, 0, 0), s.getNormal(new Point(-1, 0, 0)), "Normal to a sphere does not work correctly");

        // TC07: Normal to a sphere
        // The normal to the sphere at the point (0, 0, 0)
        // The normal to the sphere at the point (0, 0, 0) is not defined
        // because the point is the center of the sphere
        assertThrows(IllegalArgumentException.class, () -> s.getNormal(new Point(0, 0, 0)), "Failed to throw an exception when getting the normal to a sphere at the center");

    }
}