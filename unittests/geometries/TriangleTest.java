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
        assertNull(result1, "Ray intersects inside triangle - should return 1 point");

        // TC02: Ray intersects outside against edge
        Ray ray2 = new Ray(new Point(2, 1, 1), new Vector(0, 0, -1));
        assertNull(triangle.findIntersections(ray2), "Ray outside against edge - should return null");

        // TC03: Ray intersects outside against vertex
        Ray ray3 = new Ray(new Point(-1, -1, 1), new Vector(0, 0, -1));
        assertNull(triangle.findIntersections(ray3), "Ray outside against vertex - should return null");

        // =============== Boundary Value Tests ==================

        // TC11: Ray intersects on edge
        Ray ray4 = new Ray(new Point(1, 0, 1), new Vector(0, 0, -1));
        assertNull(triangle.findIntersections(ray4), "Ray on edge - should return null");

        // TC12: Ray intersects on vertex
        Ray ray5 = new Ray(new Point(0, 0, 1), new Vector(0, 0, -1));
        assertNull(triangle.findIntersections(ray5), "Ray on vertex - should return null");

        // TC13: Ray intersects on edge extension
        Ray ray6 = new Ray(new Point(3, 0, 1), new Vector(0, 0, -1));
        assertNull(triangle.findIntersections(ray6), "Ray on edge extension - should return null");
    }
}
}