package lighting;

import primitives.Color;

public class AmbientLight {
    public final static AmbientLight NONE = new AmbientLight(Color.BLACK);
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
