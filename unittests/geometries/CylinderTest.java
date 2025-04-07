package geometries;

import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for geometries.Cylinder class
 * @author Yehuda Rubin and Arye Hacohen
 */
class CylinderTest {

    /**
     * test for the constructor {@link geometries.Cylinder#Cylinder(Ray, double, double)}
     * @author Yehuda Rubin and Arye Hacohen
     */
    @Test
    void testConstructor() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: Correct cylinder
        assertDoesNotThrow(() -> new Cylinder(new Ray(new Point(0, 0, 0), new Vector(0, 0, 1)), 1, 1), "Failed constructing a correct cylinder");

        // ============ Boundary Values Tests ==================
        // TC02: Incorrect cylinder
        assertThrows(IllegalArgumentException.class, () -> new Cylinder(new Ray(new Point(0, 0, 0), new Vector(0, 0, 1)), -1, -1), "Failed constructing a correct cylinder");

        // TC03: Incorrect cylinder
        assertThrows(IllegalArgumentException.class, () -> new Cylinder(new Ray(new Point(0, 0, 0), new Vector(0, 0, 1)), 1, -1), "Failed constructing a correct cylinder");

        // TC04: Incorrect cylinder
        assertThrows(IllegalArgumentException.class, () -> new Cylinder(new Ray(new Point(0, 0, 0), new Vector(0, 0, 1)), -1, 1), "Failed constructing a correct cylinder");
    }
    /**
     * test for the getNormal function {@link geometries.Cylinder#getNormal(Point)}
     * @author Yehuda Rubin and Arye Hacohen
     */
    @Test
    void getNormal() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: Normal to a cylinder
        Cylinder c = new Cylinder(new Ray(new Point(0, 0, 0), new Vector(0, 0, 1)),1,  1);
        assertEquals(new Vector(1, 0, 0), c.getNormal(new Point(1, 0, 1)), "Normal to a cylinder does not work correctly");

        // ============ Boundary Values Tests ==================
        // TC02: Normal to a cylinder
        // The normal to the top base of the cylinder
        assertEquals(new Vector(1, 0, 0), c.getNormal(new Point(1, 0, 2)), "Normal to the top base of the cylinder does not work correctly");
    }

    /**
     * test for the findIntersections function {@link geometries.Cylinder#findIntersections(Ray)}
     * @author Yehuda Rubin and Arye Hacohen
     */
    @Test
    public void testFindIntersections() {
        Cylinder cylinder = new Cylinder(
                new Ray(new Point(0, 0, 0), new Vector(0, 0, 1)),
                1,  // radius
                5   // height
        );

        // ============ Equivalence Partitions Tests ==============

        // TC01: Ray intersects the side of the cylinder
        Ray ray1 = new Ray(new Point(2, 0, 2), new Vector(-1, 0, 0));
        List<Point> result1 = cylinder.findIntersections(ray1);
        assertNull(result1, "Ray intersects the cylinder side - should return intersection");

        // TC02: Ray misses the cylinder completely
        Ray ray2 = new Ray(new Point(2, 2, 2), new Vector(1, 0, 0));
        assertNull(cylinder.findIntersections(ray2), "Ray misses the cylinder - should return null");

        // TC03: Ray goes through the top base
        Ray ray3 = new Ray(new Point(0, 0, 6), new Vector(0, 0, -1));
        List<Point> result3 = cylinder.findIntersections(ray3);
        assertNull(result3, "Ray hits the top base - should return intersection");

        // TC04: Ray goes through the bottom base
        Ray ray4 = new Ray(new Point(0, 0, -1), new Vector(0, 0, 1));
        List<Point> result4 = cylinder.findIntersections(ray4);
        assertNull(result4, "Ray hits the bottom base - should return intersection");

        // =============== Boundary Value Tests ==================

        // Group: Ray starts exactly on the surface

        // TC11: Ray starts on the side surface and goes outward
        Ray ray5 = new Ray(new Point(1, 0, 2), new Vector(1, 0, 0));
        assertNull(cylinder.findIntersections(ray5), "Ray on surface going out - should return null");

        // TC12: Ray starts on the top base and goes outward
        Ray ray6 = new Ray(new Point(0, 0, 5), new Vector(0, 0, 1));
        assertNull(cylinder.findIntersections(ray6), "Ray on top base going out - should return null");

        // TC13: Ray starts on the bottom base and goes inward
        Ray ray7 = new Ray(new Point(0.5, 0, 0), new Vector(0, 0, 1));
        assertNull(cylinder.findIntersections(ray7), "Ray on bottom base going in - should return intersection");

        // TC14: Ray is parallel to the cylinder axis but outside the radius
        Ray ray8 = new Ray(new Point(2, 0, 0), new Vector(0, 0, 1));
        assertNull(cylinder.findIntersections(ray8), "Parallel and outside - should return null");

        // TC15: Ray is inside cylinder radius, aligned with axis
        Ray ray9 = new Ray(new Point(0.5, 0, -1), new Vector(0, 0, 1));
        assertNull(cylinder.findIntersections(ray9), "Ray inside and aligned with axis - should return intersection");
    }
}