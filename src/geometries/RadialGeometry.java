package geometries;

/**
 * The RadialGeometry class extends the Geometry class
 * and represents a radial geometry object in the 3D space
 * with a radius value
 * @see Geometry
 * @see Sphere
 * @see Tube
 * @see Cylinder
 * @author Yehuda Rubin and Arye Hacohen
 */
public abstract class RadialGeometry extends Geometry{
    protected double radius;

    /**
     * constructor
     * @param r will be the radius of the new radial geometry object
     */
    public RadialGeometry(double r) {
        radius = r;
    }

}
