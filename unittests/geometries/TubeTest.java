package geometries;

import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for geometries.Tube class
 * @author Yehuda Rubin and Arye Hacohen
 */

class TubeTest {

    /**
     * test for the constructor {@link Tube#Tube(double, Ray)}
     */

    @Test
    void testConstructor() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: Correct tube
        assertDoesNotThrow(() -> new Tube(1, new Ray(new Point(0, 0, 0), new Vector(0, 0, 1))), "Failed constructing a correct tube");

        // ============ Boundary Values Tests ==================
        // TC02: Incorrect tube
        assertThrows(IllegalArgumentException.class, () -> new Tube(-1, new Ray(new Point(0, 0, 0), new Vector(0, 0, 1))), "Failed constructing a correct tube");
    }
    /**
     * test for the getNormal function {@link geometries.Tube#getNormal(Point)}
     */

    @Test
    void getNormal() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: Normal to a tube
        Tube t = new Tube(1, new Ray(new Point(0, 0, 0), new Vector(0, 0, 1)));
        assertEquals(new Vector(1, 0, 0), t.getNormal(new Point(1, 0, 1)), "Normal to a tube does not work correctly");

        // ============ Boundary Values Tests ==================
        // TC02: Normal to a tube
        // The normal to the top base of the tube
        assertEquals(new Vector(1, 0, 0), t.getNormal(new Point(1, 0, 2)), "Normal to the top base of the tube does not work correctly");

        // TC03: Normal to a tube
        // The normal to the bottom base of the tube
        assertEquals(new Vector(1, 0, 0), t.getNormal(new Point(1, 0, -1)), "Normal to the bottom base of the tube does not work correctly");

        // TC04: Normal to a tube
        // The normal to the bottom base of the tube
        assertEquals(new Vector(1, 0, 0), t.getNormal(new Point(1, 0, -2)), "Normal to the bottom base of the tube does not work correctly");

    }

}