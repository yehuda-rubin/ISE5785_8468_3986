package geometries;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import primitives.Point;
import primitives.Vector;
import primitives.Ray;
import java.util.List;

/**
 * Unit tests for geometries.Triangle class
 * @author
 * Yehuda Rubin and Arye Hacohen
 */
class TriangleTest {

    @Test
    void testFindIntersections() {
        Triangle triangle = new Triangle( new Point(-1,0,0), new Point(2,0,0), new Point(0,3,0));

        // ============ Equivalence Partitions Tests ==============
        // TC01: The point of intersection inside the triangle (1 point)
        var result = triangle.findIntersections(new Ray(new Point(0,1, -1), new Vector(0,1, 1)));
        assertEquals(1, result.size(), "Wrong number of points");
        assertEquals(List.of(new Point(0,2,0)), result, "Ray crosses in triangle does not work correctly");

        assertEquals(List.of(new Point(0,2,0)), result, "Ray crosses in triangle does not work correctly");

        // TC03: The point of intersection is outside the triangle opposite a vertex (0 points)
        assertNull(triangle.findIntersections(new Ray(new Point(-1,0.5,-1), new Vector(-0.2,-0.6, 1))),
                "Ray does not cross triangle and point is opposite vertex - does not work correctly");

        // =============== Boundary Values Tests ==================
        // TC11: The intersection point is on an edge (0 points)
        assertNull(triangle.findIntersections(new Ray(new Point(-1,-1.5,-1), new Vector(0.5,1.5,1))),
                "Ray does not cross triangle and point is on edge - does not work correctly");

        // TC12: The intersection point is on a vertex (0 points)
        assertNull(triangle.findIntersections(new Ray(new Point(-1,-1,-1), new Vector(3,1,1))),
                "Ray does not cross triangle and point is on vertex - does not work correctly");

        // TC13: The intersection point is on the continuation of an edge (0 points)
        assertNull(triangle.findIntersections(new Ray(new Point(-2,-2,-0.5), new Vector(5,2,0.5))),
                "Ray does not cross triangle and point is on the continuation of an edge - does not work correctly");
    }
}
