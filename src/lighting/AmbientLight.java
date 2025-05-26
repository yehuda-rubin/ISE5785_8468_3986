package lighting;

import primitives.Color;

/**
 * Class representing ambient light in a 3D environment
 * The ambient light is a constant light that illuminates all objects equally
 * regardless of their position or orientation.
 * It is typically used to simulate the general illumination
 * in a scene, providing a base level of light
 * that is not affected by shadows or other light sources.
 * * @author Yehuda Rubin and Arye Hacohen
 */
public class AmbientLight extends Light{
    /**
     * Default ambient light intensity
     */
    public final static AmbientLight NONE = new AmbientLight(Color.BLACK);

    /**
     * Constructor for AmbientLight
     * @param intensity the intensity of the ambient light
     */
    public AmbientLight(Color intensity) {
        super(intensity);
    }
}
