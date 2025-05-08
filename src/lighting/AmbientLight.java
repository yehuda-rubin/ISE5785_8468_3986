package lighting;

import primitives.Color;

public class AmbientLight {
    public final static AmbientLight NONE = new AmbientLight(Color.BLACK, 0.0);
    private final Color intensity;

    /**
     * Constructor for AmbientLight
     * @param intensity the color intensity of the light
     * @param kC the constant attenuation factor
     */
    public AmbientLight(Color intensity, double kC) {
        this.intensity = intensity.scale(kC);
    }

    /**
     * Getter for the intensity of the light
     * @return the color intensity of the light
     */
    public Color getIntensity() {
        return intensity;
    }
}
