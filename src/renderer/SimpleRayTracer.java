package renderer;

import primitives.Color;
import primitives.Ray;
import scene.Scene;

public class SimpleRayTracer extends RayTracerBase{
    /**
     * Constructor for SimpleRayTracer
     * @param scene the scene to be rendered
     */
    public SimpleRayTracer(Scene scene) {
        super(scene);
    }

    @Override
    public Color traceRay(Ray ray) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
