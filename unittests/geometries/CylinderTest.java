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
     * test for the constructor {@link geometries.Cylinder#Cylinder(primitives.Ray, double, double)}.
     * @throws Exception if the test fails
     * @see geometries.Cylinder#Cylinder(primitives.Ray, double, double)
     * @author Yehuda Rubin and Arye Hacohen
     */
    @Test
    void testConstructor() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: Correct cylinder
        assertDoesNotThrow(() -> new Cylinder(new Ray(new Point(0, 0, 0), new Vector(0, 0, 1)), 1, 1), "Failed constructing a correct cylinder");
    }
    /**
     * test for the getNormal function {@link geometries.Cylinder#getNormal(geometries.Cylinder)}
     * @author Yehuda Rubin and Arye Hacohen
     */
    @Test
    void getNormal() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: Normal to a cylinder
        Cylinder c = new Cylinder(new Ray(new Point(0, 0, 0), new Vector(0, 0, 1)),1,  1);
        assertEquals(new Vector(0, 0, 1), c.getNormal(new Point(0, 0, 1)), "Normal to a cylinder does not work correctly");
    }
}