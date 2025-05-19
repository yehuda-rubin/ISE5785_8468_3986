package geometries;

import primitives.Color;
import primitives.Point;
import primitives.Vector;

/**
 * The Geometry class is the base
 *
 * @author Yehuda Rubin and Arye Hacohen
 */
public abstract class Geometry implements Intersectable {

    /**
     * The emission color of the geometry object
     */
    protected Color emission = Color.BLACK;
    /**
     * getNormal function returns the normal vector to the geometry object
     *
     * @param point - the point on the geometry object
     * @return the normal vector to the geometry object
     */
    public abstract Vector getNormal(Point point);

    /**
     * getEmission function returns the emission color of the geometry object
     *
     * @return the emission color of the geometry object
     */
    public Color getEmission() {
        return emission;
    }

    /**
     * setEmission function sets the emission color of the geometry object
     *
     * @param emission - the emission color of the geometry object
     * @return the geometry object
     */
    public Geometry setEmission(Color emission) {
        this.emission = emission;
        return this;
    }
}
