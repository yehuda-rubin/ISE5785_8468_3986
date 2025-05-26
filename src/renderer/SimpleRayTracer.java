package renderer;

import geometries.Intersectable. Intersection;
import lighting.LightSource;
import primitives.*;
import scene.Scene;

import java.util.List;

import static primitives.Util.alignZero;
import static primitives.Util.isZero;

/**
 * SimpleRayTracer class is a basic implementation of the RayTracerBase.
 * It provides methods to trace rays through a scene,
 * calculate intersections,
 * and compute the color at intersection points based on local lighting effects.
 * This class extends the RayTracerBase and implements the traceRay method
 * to handle ray tracing logic.
 * @author Yehuda Rubin and Arye Hacohen
 */
public class SimpleRayTracer extends RayTracerBase {

    /**
     * A small value used to determine if a dot product is effectively zero.
     */
    private static final double DELTA = 0.1;

    /**
     * Checks if the intersection point is unshaded by the light source.
     * It creates a ray from the intersection point towards the light source
     * and checks if there are any intersections with other geometries.
     * If there are no intersections, it means the point is unshaded.
     * @param intersection the intersection to check for shading
     * @return true if the point is unshaded, false otherwise
     */
    private boolean unshaded(Intersection intersection){
        Vector pointToLight = intersection.l.scale(-1); // from the point to the light source
        Vector deltaVector = intersection.normal;
        // if the light is behind the normal, move in the opposite direction
        if(pointToLight.dotProduct(deltaVector) < 0)
            deltaVector = deltaVector.scale(-DELTA);
            // if the light is in front of the normal, move in the same direction
        else
            deltaVector = deltaVector.scale(DELTA);

        Point point = intersection.point.add(deltaVector); // move the point slightly in the direction of deltaVector
        Ray ray = new Ray(point, pointToLight); // create a ray from the point to the light source
        var intersections = scene.geometries.calculateIntersections(ray, intersection.light.getDistance(point));
        return intersections == null;
    }

    /**
     * Constructs a new SimpleRayTracer with the given scene.
     * @param scene the scene that will be rendered using this ray tracer
     */
    public SimpleRayTracer(Scene scene) {
        super(scene);
    }

    /**
     * Initializes the fields of the ray direction vector, the normal vector, and their dot product,
     * and returns false if the dot product is equal to zero, and true otherwise
     * @param intersection the intersection to update
     * @param rayDirection the ray direction vector
     * @return false if the dot product is equal to zero, and true otherwise
     */
    public boolean preprocessIntersection(Intersection intersection, Vector rayDirection) {
        intersection.v = rayDirection.normalize();
        intersection.normal = intersection.geometry.getNormal(intersection.point);
        intersection.vNormal = intersection.v.dotProduct(intersection.normal);

        return !isZero(intersection.vNormal);
    }

    /**
     * Initializes the fields related to the light source at intersection,
     * and returns false if both dot products are equal to zero, and true otherwise
     * @param intersection the intersection to update
     * @param light the source light
     * @return false if both dot products are equal to zero, and true otherwise
     */
    public boolean setLightSource(Intersection intersection, LightSource light) {
        // Assumes that preprocessIntersection has already been executed, otherwise there may be
        // problems running the code. But it is only called internally, so there are no checks.
        intersection.light = light;
        intersection.l = light.getL(intersection.point);
        intersection.lNormal = intersection.l.dotProduct(intersection.normal);

        return !(isZero(intersection.vNormal) && isZero(intersection.lNormal));
    }

    /**
     * Calculates the local lighting effects at a given intersection point.
     * This includes the object's emission and contributions from all light sources
     * that affect the point (diffuse and specular reflections).
     * @param intersection the intersection point between a ray and a geometry
     * @return the resulting color from local light effects at the intersection
     */
    private Color calcColorLocalEffects(Intersection intersection) {
        Color color = intersection.geometry.getEmission();
        for (LightSource lightSource : scene.lights) {
            // also checks if sign(lNormal) == sign(vNormal)) and if the intersection is unshaded
            if (!setLightSource(intersection, lightSource) || !unshaded(intersection))
                continue;

            Color iL = lightSource.getIntensity(intersection.point);
            color = color
                    .add(iL.scale(calcDiffusive(intersection)
                            .add(calcSpecular(intersection))));
        }
        return color;
    }

    /**
     * Calculates the specular reflection component at the intersection point
     * based on the Phong reflection model.
     * @param intersection the intersection data including vectors and material
     * @return the specular reflection as a Double3 coefficient
     */
    private Double3 calcSpecular(Intersection intersection) {
        Vector r = intersection.l.subtract(intersection.normal.scale(2 * intersection.lNormal));
        double vr = -1 * intersection.v.dotProduct(r);

        return intersection.material.kS.scale(Math.pow(Math.max(0, vr), intersection.material.nSh));
    }

    /**
     * Calculates the diffuse reflection component at the intersection point
     * based on the Phong reflection model.
     * @param intersection the intersection data including normal and material
     * @return the diffuse reflection as a Double3 coefficient
     */
    private Double3 calcDiffusive(Intersection intersection) {
        return intersection.material.kD.scale(Math.abs(intersection.lNormal));
    }

    /**
     * Calculates the total color at the intersection point, combining ambient light,
     * emission, and local lighting effects (diffuse + specular).
     * @param intersection the intersection for calculating the color
     * @param ray the viewing ray that hit the geometry
     * @return the resulting color at the intersection point
     */
    private Color calcColor(Intersection intersection, Ray ray) {
        // If there is no intersection, return the background color of the scene
        if (!preprocessIntersection(intersection, ray.getDirection()))
            return Color.BLACK;

        Color ambientLightIntensity = scene.ambientLight.getIntensity();
        Double3 attenuationCoefficient = intersection.geometry.getMaterial().kA;

        // Calculate the ambient light contribution
        Color intensity = ambientLightIntensity.scale(attenuationCoefficient);
        return intensity.add(calcColorLocalEffects(intersection));
    }

    @Override
    public Color traceRay(Ray ray) {
        List<Intersection> intersections = scene.geometries.calculateIntersections(ray);

        // If there are no intersections, return the background color of the scene
        if (intersections == null)
            return scene.background;
        else {
            Intersection closestPoint = ray.findClosestIntersection(intersections);
            return calcColor(closestPoint, ray);
        }
    }
}