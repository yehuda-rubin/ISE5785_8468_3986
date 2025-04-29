package geometries;

import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GeometriesTest {
    //Create a list of geometries
    private final Geometries geometries = new Geometries(new Sphere(1, new Point(0, 0, 1)),
            new Triangle(new Point(1, 0, 0), new Point(1, 1, 0), new Point(0, 1, 0)),
            new Plane(new Point(0, 0, 3), new Vector(0, 0, 1))
    );

    /**
     * Test for the constructor {@link geometries.Geometries#Geometries(Intersectable...)}
     * @author Yehuda Rubin and Arye Hacohen
     */
    @Test
    void testConstructor() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: Correct geometries
        assertDoesNotThrow(() -> new Geometries(new Sphere(1, new Point(0, 0, 0)), new Triangle(new Point(1, 0, 0), new Point(1, 1, 0), new Point(0, 1, 0))), "Failed constructing a correct geometries");

        // ============ Boundary Values Tests ==================
        // TC02: Incorrect geometries
        assertThrows(IllegalArgumentException.class, () -> new Geometries(new Sphere(-1, new Point(0, 0, 0))), "Failed constructing a correct geometries");
    }

    /**
     * Test for the add method {@link geometries.Geometries#add(Intersectable)}
     * @author Yehuda Rubin and Arye Hacohen
     */
    @Test
    void add() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: Correct geometries
        assertDoesNotThrow(() -> {
            Geometries geometries = new Geometries();
            geometries.add(new Sphere(1, new Point(0, 0, 0)));
            geometries.add(new Triangle(new Point(1, 0, 0), new Point(1, 1, 0), new Point(0, 1, 0)));
        }, "Failed adding a correct geometry");

        // ============ Boundary Values Tests ==================
        // TC02: Incorrect geometries
        assertThrows(IllegalArgumentException.class, () -> {
            Geometries geometries = new Geometries();
            geometries.add(new Sphere(-1, new Point(0, 0, 0)));
        }, "Failed adding an incorrect geometry");
    }

    /**
     * Test for the findIntersections method {@link geometries.Geometries#findIntersections(Ray)}
     * @author Yehuda Rubin and Arye Hacohen
     */
    @Test
    void findIntersections() {
        // ================= Boundary Values Tests =================

        // TC01: empty geometries list
        assertNull(new Geometries().findIntersections(new Ray(new Point(1,1,1), new Vector(1,1,1))), "empty geometries list");

        // TC02: no geometry is intersected
        assertNull(geometries.findIntersections(new Ray(new Point(1,1,2.5), new Vector(1,0,0))), "no geometry is intersected");

        // TC03: one geometry is intersected
        assertEquals(2, geometries.findIntersections(new Ray(new Point(0, -2, 1), new Vector(0, 1, 0))).size(), "one geometry is intersected");

        // TC04: some geometries are intersected
        assertEquals(3, geometries.findIntersections(new Ray(new Point(0, -2, 0), new Vector(0, 1, 1))).size(), "some geometries are intersected");

        // ================= Equivalence Partitions Tests =================
        // TC05: all geometries are intersected
        assertEquals(4, geometries.findIntersections(new Ray(new Point(0.6, 0.6, -2), new Vector(0, 0, 1))).size(), "all geometries are intersected");

    }
}