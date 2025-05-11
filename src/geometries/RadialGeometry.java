package geometries;

/**
 * The RadialGeometry class extends the Geometry class
 * and represents a radial geometry object in the 3D space
 * with a radius value
 *
 * @author Yehuda Rubin and Arye Hacohen
 * @see Geometry
 * @see Sphere
 * @see Tube
 * @see Cylinder
 */

public abstract class RadialGeometry extends Geometry {
    /**
     * radius - the radius of the radial geometry object
     */
    protected final double radius;

    /**
     * constructor
     *
     * @param radius will be the radius of the new radial geometry object
     */
    public RadialGeometry(double radius) {
        this.radius = radius;
    }
}
