package lighting;

import primitives.Color;

/**
 * Class representing ambient light in a 3D environment
 * The ambient light is a constant light that illuminates all objects equally
 */
public class AmbientLight {
    /**
     * Default ambient light intensity
     */
    public final static AmbientLight NONE = new AmbientLight(Color.BLACK);
    /**
     * The color intensity of the ambient light
     */
    private final Color intensity;

    /**
     * Constructor for AmbientLight
     * @param intensity the color intensity of the light
     */
    public AmbientLight(Color intensity) {
        this.intensity = intensity;
    }

    /**
     * Getter for the intensity of the light
     * @return the color intensity of the light
     */
    public Color getIntensity() {
        return intensity;
    }
}
