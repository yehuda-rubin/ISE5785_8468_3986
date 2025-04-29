package geometries;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import primitives.Point;
import primitives.Vector;
import primitives.Ray;
import java.util.List;

/**
 * Unit tests for geometries.Triangle class
 * @author Yehuda Rubin and Arye Hacohen
 */
class TriangleTest {
    @Test
    public void testFindIntersections() {
        Triangle triangle = new Triangle(
                new Point(0, 0, 0),
                new Point(2, 0, 0),
                new Point(1, 2, 0)
        );

        // ============ Equivalence Partitions Tests ==============

        // TC01: Ray intersects inside the triangle
        Ray ray1 = new Ray(new Point(1, 1, 1), new Vector(0, 0, -1));
        List<Point> result1 = triangle.findIntersections(ray1);
        assertEquals(1, result1.size(), "ERROR: findIntersections() did not return the right number of points");
        assertEquals(List.of(new Point(1, 1, 0)), result1, "Incorrect intersection points");


        // TC02: Ray outside against edge
        assertNull(triangle.findIntersections(new Ray(new Point(0.5, 0.5, 1), new Vector(-2, -0.5, -1))),
                "ERROR: findIntersections() did not return null");

        // TC03: Ray outside against vertex
        assertNull(triangle.findIntersections(new Ray(new Point(0.5, 0.5, 1), new Vector(1, -0.5, -1))),
                "ERROR: findIntersections() did not return null");

        // =============== Boundary Values Tests ==================
        // TC04: Ray on edge
        assertNull(triangle.findIntersections(new Ray(new Point(0.5, 0.5, 1), new Vector(-0.5, -0.1, -0.4))),
                "ERROR: findIntersections() did not return null");

        // TC05: Ray on vertex
        assertNull(triangle.findIntersections(new Ray(new Point(0.5, 0.5, 1), new Vector(-0.5, 0.5, -1))),
                "ERROR: findIntersections() did not return null");

        // TC06: Ray on edge's continuation
        assertNull(triangle.findIntersections(new Ray(new Point(0.5, 0.5, 1), new Vector(-0.5, -1, 0.5))),
                "ERROR: findIntersections() did not return null");
    }
}