package renderer;

import lighting.LightSource;
import primitives.*;
import scene.Scene;

import java.util.List;
import geometries.Intersectable. Intersection;

import static primitives.Util.alignZero;
import static primitives.Util.isZero;


/**
 * Simple ray tracer implementation that calculates the color at a point in the scene
 * based on local effects such as diffuse and specular reflections.
 * This class extends the RayTracerBase class and implements the traceRay method.
 * It processes intersections, calculates normals, light directions, and computes the resulting color
 * at the intersection point.
 * @author Yehuda Rubin and Arye Hacohen
 */
public class SimpleRayTracer extends RayTracerBase{
    /**
     * Constructor for SimpleRayTracer
     * @param scene the scene to be rendered
     */
    public SimpleRayTracer(Scene scene) {
        super(scene);
    }


    /**
     * Preprocesses the intersection to calculate the normal, light direction, and other necessary values.
     *
     * @param intersection The intersection point.
     * @param rayDirection The direction of the ray.
     * @return true if preprocessing was successful, false otherwise.
     */
    private boolean preprocessIntersection(Intersection intersection, Vector rayDirection) {
        // Check if the intersection point is valid
        intersection.v = rayDirection.normalize();
        intersection.normal = intersection.geometry.getNormal(intersection.point);
        intersection.vNormal = intersection.v.dotProduct(intersection.normal);
        return !isZero(intersection.vNormal);
    }

    /**
     * Sets the light source for the intersection and calculates the direction from the light source to the intersection point.
     *
     * @param intersection The intersection point.
     * @param light The light source.
     * @return true if the light source is in the same direction as the normal, false otherwise.
     */
    public boolean setLightSource(Intersection intersection, LightSource light) {
        // Calculate the direction from the light source to the intersection point
        intersection.light = light;
        intersection.l = light.getL(intersection.point);
        intersection.lNormal = intersection.l.dotProduct(intersection.normal);

        // Check if the light source is in the same direction as the normal
        return !isZero(intersection.lNormal);
    }

    /**
     * Calculates the local effects of the intersection point, such as diffuse and specular reflections.
     *
     * @param intersection The intersection point.
     * @return The color resulting from local effects.
     */
    private Color calcColorLocalEffects(Intersection intersection) {
        Color color = intersection.geometry.getEmission();
        for (LightSource lightSource : scene.lights) {
            // also checks if sign(nl) == sign(nv))
            if (!setLightSource(intersection, lightSource) ||
                    alignZero(intersection.lNormal * intersection.vNormal) <= 0 )
                continue;

            Color iL = lightSource.getIntensity(intersection.point);
            color = color.add(
                    iL.scale(
                            calcDiffusive(intersection).add(calcSpecular(intersection))
                    )
            );
        }
        return color;
    }

    /**
     * Calculates the diffuse reflection component at the intersection point.
     *
     * @param intersection The intersection point.
     * @return The diffuse reflection color.
     */
    private Double3 calcDiffusive(Intersection intersection)
    {
        // Calculate the diffuse reflection color based on the material's diffuse coefficient and the light intensity
        return intersection.material.kD.scale(Math.abs(intersection.lNormal));
    }

    /**
     * Calculates the specular reflection component at the intersection point.
     *
     * @param intersection The intersection point.
     * @return The specular reflection color.
     */
    private Double3 calcSpecular(Intersection intersection) {
        return intersection.material.kD.scale(Math.abs(intersection.lNormal));
    }

    /**
     * Calculates the color at a specified point/pixel in the scene based on a geometry.
     *
     * @return The Color of the point.
     */
    private Color calcColor(Intersection intersection, Ray ray) {
        // If the intersection point is null, return black
        if (!preprocessIntersection(intersection, ray.getDirection()))
            return Color.BLACK;

        // If the intersection point is not valid, return black
        Color ambientLightIntensity = scene.ambientLight.getIntensity();
        Double3 attenuationCoefficient = intersection.geometry.getMaterial().kA;

        // If the material is not defined, return the ambient light color
        Color intensity = ambientLightIntensity.scale(attenuationCoefficient);
        return intensity.add(calcColorLocalEffects(intersection));
    }

    @Override
    public Color traceRay(Ray ray) {
        List<Intersection> intersections = scene.geometries.calculateIntersections(ray);

        // If there are no intersections, return the background color of the scene
        if (intersections == null)
            return scene.background;

        // If there are no intersections, return the background color of the scene
        else {
            Intersection closestPoint = ray.findClosestIntersection(intersections);
            return calcColor(closestPoint, ray);
        }
    }
}
