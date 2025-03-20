package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

/**
 * Tube class represents a tube in 3D Cartesian coordinate
 * extends RadialGeometry
 *  axis - the axis of the tube
 *  radius - the radius of the tube
 * @author Yehuda rubin and arye hacohen
 */

public class Tube extends RadialGeometry{
    protected Ray axis;

    /**
     * constructor
     * @param axis - the axis of the tube
     * @param radius - the radius of the tube
     */

    public Tube(Ray axis, double radius) {
        super(radius);
        this.axis = axis;
    }

    /**
     * @return normal to the tube
     */

    @Override
    public Vector getNormal(Point point) {
        return null;
    }
}
