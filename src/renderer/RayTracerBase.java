package renderer;

import primitives.Color;
import primitives.Ray;
import scene.Scene;

public abstract class RayTracerBase {
    protected final Scene scene;
    abstract public Color traceRay(Ray ray);
    /**
     * Constructor for RayTracerBase
     * @param scene the scene to be rendered
     */
    public RayTracerBase(Scene scene) {
        this.scene = scene;
    }
}
