package lighting;

import primitives.Color;

/**
 * Class representing ambient light in a 3D environment
 * The ambient light is a constant light that illuminates all objects equally
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
