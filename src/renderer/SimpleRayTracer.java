package renderer;

import primitives.Color;
import primitives.Double3;
import primitives.Point;
import primitives.Ray;
import scene.Scene;

import java.util.List;
import geometries.Intersectable. Intersection;


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
     * @return The Color of the point.
     */
    private Color calcColor(Intersection intersection, Ray ray) {
        if (!preprocessIntersection(intersection, ray.getDirection()))
            return Color.BLACK;

        Color ambientLightIntensity = scene.ambientLight.getIntensity();
        Double3 attenuationCoefficient = intersection.geometry.getMaterial().kA;

        Color intensity = ambientLightIntensity.scale(attenuationCoefficient);
        return intensity.add(calcColorLocalEffects(intersection));
    }

    @Override
    public Color traceRay(Ray ray) {
        List<Intersection> intersections = scene.geometries.calculateIntersections(ray);

        if (intersections == null)
            return scene.background;
        else {
            Intersection closestPoint = ray.findClosestIntersection(intersections);
            return calcColor(closestPoint, ray);
        }
    }
}
