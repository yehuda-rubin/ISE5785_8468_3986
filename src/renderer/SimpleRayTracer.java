package renderer;

import primitives.Color;
import primitives.Point;
import primitives.Ray;
import scene.Scene;

import java.util.List;

public class SimpleRayTracer extends RayTracerBase{
    /**
     * Constructor for SimpleRayTracer
     * @param scene the scene to be rendered
     */
    public SimpleRayTracer(Scene scene) {
        super(scene);
    }

    /**
     * Calculates the color at a specified point/pixel in the scene based on a geometry.
     *
     * @param point The Point in the scene for which to calculate the color.
     * @return The Color of the point.
     */
    private Color calcColor(Point point){
        // In the meantime we are sending the color of the ambient light in the scene.
        return this.scene.ambientLight.getIntensity();
    }

    @Override
    public Color traceRay(Ray ray) {
        List<Point> intersections = this.scene.geometries.findIntersections(ray);
        if (intersections == null || intersections.isEmpty()){
            return this.scene.background;
        }
        Point closestP = ray.findClosestPoint(intersections);
        return calcColor(closestP);
    }
}
