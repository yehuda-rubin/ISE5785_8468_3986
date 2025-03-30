package geometries;

import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;

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
}