package lighting;

import primitives.*;

/**
 * The LightSource interface represents a light source in the scene.
 * It provides methods to get the intensity of the light at a specific point
 * and the direction from the light source to that point.
 *
 * @author Yehuda Rubin and Arye Hacohen
 */
public interface LightSource {
    /**
     * Gets the intensity of the light at a specific point.
     *
     * @param p the point in space where the intensity is to be calculated
     * @return the color intensity of the light at that point
     */
    Color getIntensity(Point p);
    /**
     * Gets the direction vector from the light source to a specific point.
     *
     * @param p the point in space where the direction is to be calculated
     * @return the vector pointing from the light source to the point
     */
    Vector getL(Point p);
}
