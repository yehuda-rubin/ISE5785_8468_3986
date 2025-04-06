package geometries;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;

import geometries.Plane;
import geometries.Polygon;
import primitives.*;

/**
 * Testing Polygons
 * @author Dan
 */
class PolygonTests {
   /**
    * Delta value for accuracy when comparing the numbers of type 'double' in
    * assertEquals
    */
   private static final double DELTA = 0.000001;

   /** Test method for {@link Polygon#Polygon(Point...)}. */
   @Test
   void testConstructor() {
      // ============ Equivalence Partitions Tests ==============

      // TC01: Correct concave quadrangular with vertices in correct order
      assertDoesNotThrow(() -> new Polygon(new Point(0, 0, 1),
                                           new Point(1, 0, 0),
                                           new Point(0, 1, 0),
                                           new Point(-1, 1, 1)),
                         "Failed constructing a correct polygon");

      // TC02: Wrong vertices order
      assertThrows(IllegalArgumentException.class, //
                   () -> new Polygon(new Point(0, 0, 1), new Point(0, 1, 0), new Point(1, 0, 0), new Point(-1, 1, 1)), //
                   "Constructed a polygon with wrong order of vertices");

      // TC03: Not in the same plane
      assertThrows(IllegalArgumentException.class, //
                   () -> new Polygon(new Point(0, 0, 1), new Point(1, 0, 0), new Point(0, 1, 0), new Point(0, 2, 2)), //
                   "Constructed a polygon with vertices that are not in the same plane");

      // TC04: Concave quadrangular
      assertThrows(IllegalArgumentException.class, //
                   () -> new Polygon(new Point(0, 0, 1), new Point(1, 0, 0), new Point(0, 1, 0),
                                     new Point(0.5, 0.25, 0.5)), //
                   "Constructed a concave polygon");

      // =============== Boundary Values Tests ==================

      // TC10: Vertex on a side of a quadrangular
      assertThrows(IllegalArgumentException.class, //
                   () -> new Polygon(new Point(0, 0, 1), new Point(1, 0, 0), new Point(0, 1, 0),
                                     new Point(0, 0.5, 0.5)),
                   "Constructed a polygon with vertix on a side");

      // TC11: Last point = first point
      assertThrows(IllegalArgumentException.class, //
                   () -> new Polygon(new Point(0, 0, 1), new Point(1, 0, 0), new Point(0, 1, 0), new Point(0, 0, 1)),
                   "Constructed a polygon with vertice on a side");

      // TC12: Co-located points
      assertThrows(IllegalArgumentException.class, //
                   () -> new Polygon(new Point(0, 0, 1), new Point(1, 0, 0), new Point(0, 1, 0), new Point(0, 1, 0)),
                   "Constructed a polygon with vertice on a side");

   }

   /** Test method for {@link Polygon#getNormal(Point)}. */
   @Test
   void testGetNormal() {
      // ============ Equivalence Partitions Tests ==============
      // TC01: There is a simple single test here - using a quad
      Point[] pts =
         { new Point(0, 0, 1), new Point(1, 0, 0), new Point(0, 1, 0), new Point(-1, 1, 1) };
      Polygon pol = new Polygon(pts);
      // ensure there are no exceptions
      assertDoesNotThrow(() -> pol.getNormal(new Point(0, 0, 1)), "");
      // generate the test result
      Vector result = pol.getNormal(new Point(0, 0, 1));
      // ensure |result| = 1
      assertEquals(1, result.length(), DELTA, "Polygon's normal is not a unit vector");
      // ensure the result is orthogonal to all the edges
      for (int i = 0; i < 3; ++i)
         assertEquals(0d, result.dotProduct(pts[i].subtract(pts[i == 0 ? 3 : i - 1])), DELTA,
                      "Polygon's normal is not orthogonal to one of the edges");
   }
   @Test
   public void testFindIntersections() {
      Polygon polygon = new Polygon(
              new Point(0, 0, 0),
              new Point(4, 0, 0),
              new Point(4, 4, 0),
              new Point(0, 4, 0)
      );

      // ============ Equivalence Partitions Tests ==============

      // TC01: Ray intersects inside the polygon
      Ray ray1 = new Ray(new Point(2, 2, 1), new Vector(0, 0, -1));
      List<Point> result1 = polygon.findIntersections(ray1);
      assertNull(result1, "Ray intersects inside the polygon - should return intersection");

      // TC02: Ray intersects outside the polygon
      Ray ray2 = new Ray(new Point(5, 5, 1), new Vector(0, 0, -1));
      assertNull(polygon.findIntersections(ray2), "Ray misses the polygon - should return null");

      // TC03: Ray intersects the plane but outside polygon area
      Ray ray3 = new Ray(new Point(4.5, 2, 1), new Vector(0, 0, -1));
      assertNull(polygon.findIntersections(ray3), "Ray hits plane but outside polygon - should return null");

      // =============== Boundary Value Tests ==================

      // TC11: Ray intersects on the edge of the polygon
      Ray ray4 = new Ray(new Point(2, 0, 1), new Vector(0, 0, -1));
      assertNull(polygon.findIntersections(ray4), "Ray hits edge - should return null or special case");

      // TC12: Ray intersects on the vertex of the polygon
      Ray ray5 = new Ray(new Point(0, 0, 1), new Vector(0, 0, -1));
      assertNull(polygon.findIntersections(ray5), "Ray hits vertex - should return null or special case");

      // TC13: Ray intersects exactly on the edge extension
      Ray ray6 = new Ray(new Point(-1, 0, 1), new Vector(0, 0, -1));
      assertNull(polygon.findIntersections(ray6), "Ray hits edge extension - should return null");

      // TC14: Ray is parallel to the polygon plane
      Ray ray7 = new Ray(new Point(1, 1, 1), new Vector(1, 0, 0));
      assertNull(polygon.findIntersections(ray7), "Ray is parallel - should return null");

      // TC15: Ray is orthogonal to the plane but starts under it
      Ray ray8 = new Ray(new Point(2, 2, -1), new Vector(0, 0, 1));
      List<Point> result8 = polygon.findIntersections(ray8);
      assertNull(result8, "Ray orthogonal and below polygon - should return intersection");
   }
}
