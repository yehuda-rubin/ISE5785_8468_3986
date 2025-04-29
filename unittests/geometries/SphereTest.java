package geometries;

import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Vector;
import primitives.Ray;

import java.util.Comparator;
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
        final double sqrt075 = Math.sqrt(0.75);
        final Point sphereCenter = new Point(0, 0, 1);
        final Sphere sphere = new Sphere(1, sphereCenter);

        final Vector directionUp = new Vector(0, 1, 0);
        final Vector directionDown = new Vector(0, -1, 0);
        final Vector directionDiagonal = new Vector(1, 1, 1);

        final Point pointOnSphere = new Point(0, 1, 1);
        final Point pointInsideSphere = new Point(0, 0.5, 1);
        final Point pointOutsideSphere = new Point(0, 2, 1);
        final Point pointIntersection1 = new Point(0, sqrt075, 1.5);
        final Point pointIntersection2 = new Point(0, -sqrt075, 1.5);

        // ============ Equivalence Partitions Tests ==============

        // TC01: Ray starts inside the sphere
        assertEquals(List.of(pointIntersection1), sphere.findIntersections(new Ray(new Point(0, 0, 1.5), directionUp)),
                "Failed to find the intersection point when the ray starts inside the sphere");

        // TC02: Ray does not intersect the sphere
        assertNull(sphere.findIntersections(new Ray(new Point(0, 0, 3), directionDiagonal)),
                "Failed to find the intersection point when the ray does not intersect the sphere");

        // TC03: Ray starts outside the sphere and intersects it twice
        assertEquals(List.of(pointIntersection1, pointIntersection2),
                sphere.findIntersections(new Ray(new Point(0, 2, 1.5), directionDown)),
                "Failed to find the intersection points when the ray starts outside the sphere and intersects it twice");

        // TC04: Ray starts outside the sphere and does not intersect it
        assertNull(sphere.findIntersections(new Ray(new Point(0, -2, 1.5), directionDown)),
                "Failed to find the intersection points when the ray starts outside the sphere and does not intersect it");

        // =============== Boundary Values Tests =================

        // TC05: Ray is orthogonal to the sphere and starts before the sphere
        assertNull(sphere.findIntersections(new Ray(pointOutsideSphere, new Vector(0, 0, 1))),
                "Failed to find the intersection point when the ray does not intersect the sphere");

        // TC06: Ray is orthogonal to the sphere and starts inside the sphere
        assertEquals(List.of(new Point(0, 0.5, 1 - sqrt075)),
                sphere.findIntersections(new Ray(pointInsideSphere, new Vector(0, 0, -1))),
                "Failed to find the intersection point when the ray starts inside the sphere");

        // TC07: Ray is tangential to the sphere and starts before the sphere
        assertNull(sphere.findIntersections(new Ray(new Point(-1, 1, 0), new Vector(1, 0, 1))),
                "Failed to find the intersection point when the ray does not intersect the sphere");

        // TC08: Ray is tangential to the sphere and starts on the sphere
        assertNull(sphere.findIntersections(new Ray(pointOnSphere, new Vector(1, 0, 1))),
                "Failed to find the intersection point when the ray does not intersect the sphere");

        // TC09: Ray is tangential to the sphere and starts after the sphere
        assertNull(sphere.findIntersections(new Ray(new Point(1, 1, 2), new Vector(1, 0, 1))),
                "Failed to find the intersection point when the ray does not intersect the sphere");

        // TC10: Ray starts on the sphere and intersects it
        assertEquals(List.of(new Point(-2.0 / 3, 1.0 / 3, 1.0 / 3)),
                sphere.findIntersections(new Ray(pointOnSphere, new Vector(-1, -1, -1))),
                "Failed to find the intersection point when the ray starts on the sphere and intersects it");

        // TC11: Ray starts on the sphere and does not intersect it
        assertNull(sphere.findIntersections(new Ray(pointOnSphere, directionDiagonal)),
                "Failed to find the intersection point when the ray starts on the sphere and does not intersect it");

        // TC12: Ray starts on the sphere and reaches the center of the sphere
        assertEquals(List.of(new Point(0, -1, 1)), sphere.findIntersections(new Ray(pointOnSphere, directionDown)),
                "Failed to find the intersection point when the ray starts on the sphere and reaches the center of the sphere");

        // TC13: Ray starts before the sphere and reaches the center of the sphere
        assertEquals(List.of(pointOnSphere, new Point(0, -1, 1)),
                sphere.findIntersections(new Ray(pointOutsideSphere, directionDown)).stream()
                        .sorted(Comparator.comparingDouble(p -> p.distance(new Point(-1, 0, 0))))
                        .toList(),
                "Failed to find the intersection points when the ray starts before the sphere and reaches the center of the sphere");

        // TC14: Ray starts at the center of the sphere
        assertEquals(List.of(pointOnSphere), sphere.findIntersections(new Ray(sphereCenter, directionUp)),
                "Failed to find the intersection point when the ray starts at the center of the sphere");

        // TC15: Ray runs on the sphere but does not reach the center due to opposite direction
        assertNull(sphere.findIntersections(new Ray(pointOnSphere, directionUp)),
                "Failed to find the intersection point when the ray starts on the sphere and does not reach the center");

        // TC16: Ray starts after the sphere and does not reach the center due to opposite direction
        assertNull(sphere.findIntersections(new Ray(pointOutsideSphere, directionUp)),
                "Failed to find the intersection point when the ray starts after the sphere and does not reach the center");

        // TC17: Ray starts inside the sphere and does not reach the center due to opposite direction
        assertEquals(List.of(pointOnSphere), sphere.findIntersections(new Ray(pointInsideSphere, directionUp)),
                "Failed to find the intersection point when the ray starts inside the sphere and does not reach the center");
    }
}