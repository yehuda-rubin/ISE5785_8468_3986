package lighting;

import primitives.Color;

/**
 * The Light abstract class represents a light source in the scene.
 * It contains the color intensity of the light source.
 *
 * @author Yehuda Rubin and Arye Hacohen
 */
public abstract class Light {
    /**
     * Variable for the color intensity of light source
     */
    protected final Color intensity;

    /**
     * Constructor fot the intensity
     * @param intensity the intensity value for initialization
     */
    protected Light(Color intensity) {
        this.intensity = intensity;
    }

    /**
     * A getter for the intensity
     * @return the intensity of the light
     */
    public Color getIntensity() {
        return this.intensity;
    }
}
