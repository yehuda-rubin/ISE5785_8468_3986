package lighting;

import primitives.Color;
import primitives.Point;
import primitives.Vector;

/**
 * The DirectionalLight class represents a directional light source in the scene.
 * It extends the Light class and implements the LightSource interface.
 * * A directional light emits light rays that are parallel and have a specific direction.
 * * @author Yehuda Rubin and Arye Hacohen
 */
public class DirectionalLight extends Light implements LightSource {
    /**
     * The direction vector of the light rays (normalized)
     */
    private final Vector direction;

    /**
     * Constructs a directional light source with the specified intensity and direction.
     * @param intensity the color intensity of the light
     * @param direction the direction of the light
     */
    public DirectionalLight(Color intensity, Vector direction) {
        super(intensity);
        this.direction = direction.normalize();
    }

    @Override
    public Color getIntensity(Point p) {
        return intensity;
    }

    @Override
    public Vector getL(Point p) {
        return direction;
    }

    @Override
    public double getDistance(Point point) {
        return Double.POSITIVE_INFINITY; // Directional light is considered to be infinitely far away
    }
}