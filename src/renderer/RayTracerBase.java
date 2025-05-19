package renderer;

import primitives.Color;
import primitives.Ray;
import scene.Scene;

/**
 * Abstract class for ray tracing in a scene
 * This class serves as a base for different ray tracing algorithms
 */
public abstract class RayTracerBase {
    /**
     * The scene to be rendered
     */
    protected final Scene scene;
    /**
     * Traces a ray through the scene and returns the color at the intersection point
     * @param ray the ray to be traced
     * @return the color at the intersection point
     */
    abstract public Color traceRay(Ray ray);
    /**
     * Constructor for RayTracerBase
     * @param scene the scene to be rendered
     */
    public RayTracerBase(Scene scene) {
        this.scene = scene;
    }
}
