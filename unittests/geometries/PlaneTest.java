package geometries;

import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Vector;
import primitives.Ray;
import java.util.List;

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

    @Test
    public void testFindIntersections() {
        Plane plane = new Plane(new Point(0, 0, 1), new Vector(0, 0, 1));

        // ============ Equivalence Partitions Tests ==============

        // TC01: Ray intersects the plane
        Ray ray1 = new Ray(new Point(0, 0, 0), new Vector(0, 0, 1));
        List<Point> result1 = plane.findIntersections(ray1);
        assertNotNull(result1, "Ray intersects the plane - must not be null");
        assertEquals(1, result1.size(), "Wrong number of intersection points");
        assertEquals(new Point(0, 0, 1), result1.get(0), "Wrong intersection point");

        // TC02: Ray does not intersect the plane
        Ray ray2 = new Ray(new Point(0, 0, 0), new Vector(0, 0, -1));
        assertEquals(new Point(0, 0, 1), result1.get(0), "Wrong intersection point");

        // =============== Boundary Values Tests ==================

        // Group: Ray is parallel to the plane

        // TC11: Ray is parallel and included in the plane
        Ray ray3 = new Ray(new Point(0, 0, 1), new Vector(1, 0, 0));
        assertNull(plane.findIntersections(ray3), "Ray in plane - must return null");

        // TC12: Ray is parallel and not included in the plane
        Ray ray4 = new Ray(new Point(0, 0, 2), new Vector(1, 0, 0));
        assertNull(plane.findIntersections(ray4), "Ray parallel not in plane - must return null");

        // Group: Ray is orthogonal to the plane

        // TC13: Ray starts before the plane
        Ray ray5 = new Ray(new Point(0, 0, 0), new Vector(0, 0, 1));
        result1 = plane.findIntersections(ray5);
        assertNotNull(result1, "Orthogonal and before plane - must not return null");
        assertEquals(new Point(0, 0, 1), result1.get(0), "Wrong intersection point");

        // Group: Ray is neither orthogonal nor parallel

        // TC15: Ray starts at the plane
        Ray ray8 = new Ray(new Point(1, 1, 1), new Vector(1, -1, -1));
        assertNull(plane.findIntersections(ray8), "Ray starts at plane - must return null");

        // TC16: Ray starts after the plane in direction of the plane
        Ray ray9 = new Ray(new Point(0, 0, 2), new Vector(0, 0, -1));
        List<Point> result2 = plane.findIntersections(ray9);
        assertNotNull(result2, "Ray from above plane - must not be null");
        assertEquals(1, result2.size(), "Wrong number of intersection points");
        assertEquals(new Point(0, 0, 1), result2.get(0), "Wrong intersection point");
    }
}