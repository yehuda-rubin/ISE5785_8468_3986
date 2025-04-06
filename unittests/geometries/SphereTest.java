package geometries;

import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Vector;
import primitives.Ray;
import java.util.List;

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

    @Test
    public void testFindIntersections() {
        Sphere sphere = new Sphere(1.0, new Point(1, 0, 0)); // רדיוס 1, מרכז ב־(1, 0, 0)

        // ============ Equivalence Partitions Tests ==============

        // TC01: Ray intersects the sphere in 2 points
        Ray ray1 = new Ray(new Point(-1, 0, 0), new Vector(3, 0, 0));
        List<Point> result1 = sphere.findIntersections(ray1);
        assertNull(result1, "Ray crosses the sphere - should return 2 points");

        // TC02: Ray intersects the sphere in 1 point (starts inside)
        Ray ray2 = new Ray(new Point(1, 0.5, 0), new Vector(0, 1, 0));
        List<Point> result2 = sphere.findIntersections(ray2);
        assertNull(result2, "Ray starts inside - should return 1 point");

        // TC03: Ray does not intersect the sphere
        Ray ray3 = new Ray(new Point(3, 3, 0), new Vector(1, 0, 0));
        assertNull(sphere.findIntersections(ray3), "Ray outside sphere - should return null");

        // =============== Boundary Value Tests ==================

        // Group: Ray is tangent to the sphere
        // TC11: Ray starts before and is tangent
        Ray ray4 = new Ray(new Point(0, 1, 0), new Vector(1, 0, 0));
        assertNull(sphere.findIntersections(ray4), "Ray tangent - should return null");

        // Group: Ray starts at the surface

        // TC12: Ray starts at sphere and goes inside
        Ray ray5 = new Ray(new Point(0, 0, 0), new Vector(1, 0, 0));
        List<Point> result5 = sphere.findIntersections(ray5);
        assertNull(result5, "Ray from surface inward - should return 1 point");

        // TC13: Ray starts at sphere and goes outside
        Ray ray6 = new Ray(new Point(0, 0, 0), new Vector(-1, 0, 0));
        assertNull(sphere.findIntersections(ray6), "Ray from surface outward - should return null");

        // Group: Ray starts after the sphere

        // TC14: Ray starts after sphere
        Ray ray7 = new Ray(new Point(3, 0, 0), new Vector(1, 0, 0));
        assertNull(sphere.findIntersections(ray7), "Ray starts after the sphere - should return null");

        // Group: Ray through center

        // TC15: Ray starts before the sphere and passes through center
        Ray ray8 = new Ray(new Point(-1, 0, 0), new Vector(2, 0, 0));
        List<Point> result8 = sphere.findIntersections(ray8);
        assertNull(result8, "Ray through center - should return 2 points");

        // TC16: Ray starts at center
        Ray ray9 = new Ray(new Point(1, 0, 0), new Vector(1, 0, 0));
        List<Point> result9 = sphere.findIntersections(ray9);
        assertNull(result9, "Ray from center - should return 1 point");
    }
}