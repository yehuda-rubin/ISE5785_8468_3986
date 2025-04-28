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
   void testFindIntersections() {
      Polygon polygon = new Polygon(
              new Point(0, 0, 0),
              new Point(1, 0, 0),
              new Point(1, 1, 0),
              new Point(0, 1, 0)
      );

      // ============ Equivalence Partitions Tests ==============

      // TC01: Ray intersects inside the polygon
      Ray ray1 = new Ray(new Point(0.5, 0.5, 1), new Vector(0, 0, -1));
      assertEquals(List.of(new Point(0.5, 0.5, 0)), polygon.findIntersections(ray1), "TC01: Ray intersects inside the polygon");

      // TC02: the intersection point is outside the Polygon and against an edge
      assertNull(polygon.findIntersections(
                      new Ray(new Point(0.5, 2, 1), new Vector(0, 0, -1))),
              "Failed to find the intersection point when the intersection point is outside the Polygon and against an edge");

      // TC03: the intersection point is outside the Polygon and against a vertex
      assertNull(polygon.findIntersections(
                      new Ray(new Point(2, 2, 1), new Vector(0, 0, -1))),
              "Failed to find the intersection point when the intersection point is outside the Polygon and against a vertex");


      // TC04: Ray is on the plane but outside the polygon
      Ray ray4 = new Ray(new Point(2, 0.5, 0), new Vector(-1, 0, 0));
      assertNull(polygon.findIntersections(ray4), "Failed to find the intersection point when the ray is on the plane but outside the polygon");

      // =============== Boundary Values Tests ==================
      // TC04: the intersection point is on the edge of the Polygon
      assertNull(polygon.findIntersections(
                      new Ray(new Point(0.5, 1, -1), new Vector(0, 0, 1))),
              "Failed to find the intersection point when the intersection point is on the edge of the Polygon");

      // TC05: the intersection point is on the vertex of the Polygon
      assertNull(polygon.findIntersections(
                      new Ray(new Point(1, 1, 1), new Vector(0, 0, -1))),
              "Failed to find the intersection point when the intersection point is on the vertex of the Polygon");

      // TC06: the intersection point is outside the Polygon but in the path of the edge
      assertNull(polygon.findIntersections(
                      new Ray(new Point(2, 1, -1), new Vector(0, 0, 1))),
              "Failed to find the intersection point when the intersection point is outside the Polygon but in the path of the edge");
   }
}
