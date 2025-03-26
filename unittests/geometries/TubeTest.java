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
     * test for the constructor {@link geometries.Tube#Tube(primitives.Ray, double)}.
     * @throws Exception if the test fails
     * @see geometries.Tube#Tube(primitives.Ray, double)
     * @author Yehuda Rubin and Arye Hacohen
     */
    @Test
    void testConstructor() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: Correct tube
        assertDoesNotThrow(() -> new Tube(new Ray(new Point(0, 0, 0), new Vector(0, 0, 1)), 1), "Failed constructing a correct tube");
    }
    /**
     * test for the getNormal function {@link geometries.Tube#getNormal(geometries.Tube)}
     * @author Yehuda Rubin and Arye Hacohen
     */
    @Test
    void getNormal() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: Normal to a tube
        Tube t = new Tube(new Ray(new Point(0, 0, 0), new Vector(0, 0, 1)), 1);
        assertEquals(new Vector(0, 0, 1), t.getNormal(new Point(0, 0, 1)), "Normal to a tube does not work correctly");
    }
}