package renderer;

import geometries.Intersectable.Intersection;
import lighting.LightSource;
import primitives.*;
import scene.Scene;

import static primitives.Util.alignZero;
import static primitives.Util.isZero;

/**
 * Simple ray tracer that calculates the color of a pixel based on ray intersections
 * with geometries in the scene, considering local and global effects such as light sources,
 * reflections, and refractions.
 * @author Yehuda Rubin and Arye Hacohen
 */
public class SimpleRayTracer extends RayTracerBase {
    /**
     * Maximum recursion level for color calculations.
     * This limits the depth of reflection and refraction calculations.
     */
    private static final int MAX_CALC_COLOR_LEVEL = 10;
    /**
     * Minimum value for color calculations to avoid unnecessary computations.
     * If the product of the color components is below this threshold, it is considered negligible.
     */
    private static final double MIN_CALC_COLOR_K = 0.001;
    /**
     * Initial color factor for global effects.
     * This is used to scale the contributions of reflections and refractions.
     */
    private static final Double3 INITIAL_K = Double3.ONE;

    /**
     * Constructor for SimpleRayTracer.
     * Initializes the ray tracer with the given scene.
     *
     * @param scene The scene containing geometries and light sources to be rendered.
     */
    public SimpleRayTracer(Scene scene) {
        super(scene);
    }

    /**
     * Calculates the color of a pixel based on the ray's intersection with the scene.
     * It considers both local and global effects such as light sources, reflections, and refractions.
     *
     * @param intersection The intersection point of the ray with the scene.
     * @return The calculated color at the intersection point.
     */
    private boolean unshaded(Intersection intersection) {
        // Check if the intersection point is illuminated by a light source
        Ray shadowRay = new Ray(
                intersection.point,
                intersection.l.scale(-1),
                intersection.normal);

        // Calculate intersections with other geometries along the shadow ray
        var blockingObjects = scene.geometries
                .calculateIntersections(shadowRay, intersection.light.getDistance(intersection.point));

        // If there are no blocking objects, the point is unshaded
        if (blockingObjects == null)
            return true;

        // Check if all blocking objects have a transparency coefficient (kT) above the minimum threshold
        for (Intersection block : blockingObjects) {
            if (block.material.kT.lowerThan(MIN_CALC_COLOR_K))
                return false;
        }
        return true;
    }

    /**
     * Calculates the transparency factor at the intersection point.
     * It checks if the point is in shadow by casting a shadow ray towards the light source.
     *
     * @param intersection The intersection point of the ray with the scene.
     * @return The transparency factor at the intersection point.
     */
    private Double3 transparency(Intersection intersection) {
        // Create a shadow ray from the intersection point towards the light source
        Ray shadowRay = new Ray(
                intersection.point,
                intersection.l.scale(-1),
                intersection.normal);

        // Calculate intersections with other geometries along the shadow ray
        var objects = scene.geometries
                .calculateIntersections(shadowRay, intersection.light.getDistance(intersection.point));

        // If there are no intersections, the point is fully transparent
        Double3 ktr = Double3.ONE;

        // If the point is unshaded, return the transparency factor
        if (objects != null) {
            for (Intersection i : objects) {
                if (ktr.lowerThan(MIN_CALC_COLOR_K))
                    return Double3.ZERO;
                ktr = ktr.product(i.material.kT);
            }
        }

        return ktr;
    }

    /**
     * Preprocesses the intersection by calculating the normal vector and the dot product
     * of the ray direction with the normal.
     *
     * @param intersection The intersection point of the ray with the scene.
     * @param rayDirection The direction of the ray.
     * @return True if the intersection is valid (not perpendicular), false otherwise.
     */
    public boolean preprocessIntersection(Intersection intersection, Vector rayDirection) {
        intersection.v = rayDirection;
        intersection.normal = intersection.geometry.getNormal(intersection.point);
        intersection.vNormal = rayDirection.dotProduct(intersection.normal);
        return !isZero(intersection.vNormal);
    }

    /**
     * Sets the light source for the intersection point and calculates the light vector
     * and its dot product with the normal vector.
     *
     * @param intersection The intersection point of the ray with the scene.
     * @param light The light source to be set for the intersection.
     * @return True if the light source is valid (not in shadow), false otherwise.
     */
    public boolean setLightSource(Intersection intersection, LightSource light) {
        intersection.light = light;
        intersection.l = light.getL(intersection.point);
        intersection.lNormal = intersection.l.dotProduct(intersection.normal);
        return alignZero(intersection.lNormal * intersection.vNormal) > 0;
    }

    /**
     * Calculates the color at the intersection point considering both local and global effects.
     * It combines the contributions from local light sources and global effects like reflections and refractions.
     *
     * @param intersection The intersection point of the ray with the scene.
     * @param level The recursion level for global effects.
     * @param k The initial color factor for global effects.
     * @return The calculated color at the intersection point.
     */
    private Color calcColorGlobalEffects(Intersection intersection, int level, Double3 k) {
        // If the recursion level is zero or the color factor is negligible, return black
        Ray refracted = constructRefractedRay(intersection);
        Ray reflected = constructReflectedRay(intersection);

        Color refractedColor = calcColorGlobalEffect(refracted, level, k, intersection.material.kT);
        Color reflectedColor = calcColorGlobalEffect(reflected, level, k, intersection.material.kR);

        return refractedColor.add(reflectedColor);
    }

    /**
     * Calculates the color at the intersection point considering global effects like reflections and refractions.
     * It scales the contributions based on the material properties of the intersection.
     *
     * @param ray The ray that intersects with the scene.
     * @param level The recursion level for global effects.
     * @param k The initial color factor for global effects.
     * @param kx The scaling factor for the specific effect (reflection or refraction).
     * @return The calculated color at the intersection point.
     */
    private Color calcColorGlobalEffect(Ray ray, int level, Double3 k, Double3 kx) {
        Double3 kkx = k.product(kx);

        // If the recursion level is zero or the color factor is negligible, return black
        if (kkx.lowerThan(MIN_CALC_COLOR_K))
            return Color.BLACK;

        // Find the closest intersection for the ray
        Intersection intersection = findClosestIntersection(ray);

        // If there is no intersection, return the background color scaled by the color factor
        if (intersection == null)
            return scene.background.scale(kx);

        // Preprocess the intersection and calculate the color based on the intersection properties
        return preprocessIntersection(intersection, ray.getDirection())
                ? calcColor(intersection, level - 1, kkx).scale(kx)
                : Color.BLACK;
    }

    /**
     * Constructs a refracted ray based on the intersection point and the normal vector.
     * The refracted ray is directed along the normal vector at the intersection point.
     *
     * @param intersection The intersection point of the ray with the scene.
     * @return The constructed refracted ray.
     */
    private Ray constructRefractedRay(Intersection intersection) {
        return new Ray(intersection.point, intersection.v, intersection.normal);
    }

    /**
     * Constructs a reflected ray based on the intersection point and the normal vector.
     * The reflected ray is directed away from the surface, reflecting off the normal vector.
     *
     * @param intersection The intersection point of the ray with the scene.
     * @return The constructed reflected ray.
     */
    private Ray constructReflectedRay(Intersection intersection) {
        Vector reflection = intersection.v
                .subtract(intersection.normal.scale(2 * intersection.vNormal));
        return new Ray(intersection.point, reflection, intersection.normal);
    }

    /**
     * Finds the closest intersection point for a given ray in the scene.
     * It calculates intersections with all geometries and returns the closest one.
     *
     * @param ray The ray to check for intersections.
     * @return The closest intersection point, or null if there are no intersections.
     */
    private Intersection findClosestIntersection(Ray ray) {
        var intersections = scene.geometries.calculateIntersections(ray);
        return intersections == null ? null : ray.findClosestIntersection(intersections);
    }

    /**
     * Calculates the color at the intersection point considering local effects such as light sources.
     * It combines the contributions from local light sources with the material properties of the geometry.
     *
     * @param intersection The intersection point of the ray with the scene.
     * @param k The initial color factor for local effects.
     * @return The calculated color at the intersection point.
     */
    private Color calcColorLocalEffects(Intersection intersection, Double3 k) {
        Color result = intersection.geometry.getEmission();

        // If the intersection point is not illuminated by any light source, return the emission color
        for (LightSource light : scene.lights) {
            if (!setLightSource(intersection, light))
                continue;

            // If the point is in shadow, skip further calculations
            Double3 ktr = transparency(intersection);

            // If the transparency factor is negligible, skip the light source
            if (!ktr.product(k).lowerThan(MIN_CALC_COLOR_K)) {
                Color intensity = light.getIntensity(intersection.point).scale(ktr);
                result = result.add(
                        intensity.scale(
                                calcDiffusive(intersection).add(
                                        calcSpecular(intersection))));
            }
        }

        return result;
    }

    /**
     * Calculates the specular and diffusive components of the color at the intersection point.
     * The specular component is based on the reflection of the light vector, while the diffusive
     * component is based on the angle between the light vector and the normal vector.
     *
     * @param intersection The intersection point of the ray with the scene.
     * @return The calculated specular color component.
     */
    private Double3 calcSpecular(Intersection intersection) {
        // Calculate the reflection vector based on the light vector and the normal vector
        Vector r = intersection.l
                .subtract(intersection.normal.scale(2 * intersection.lNormal));

        // Calculate the dot product of the reflection vector and the ray direction
        double vr = -intersection.v.dotProduct(r);

        // If the dot product is zero or negative, return zero for the specular component
        return intersection.material.kS
                .scale(Math.pow(Math.max(0, vr), intersection.material.nSh));
    }

    /**
     * Calculates the diffusive component of the color at the intersection point.
     * The diffusive component is based on the angle between the light vector and the normal vector.
     *
     * @param intersection The intersection point of the ray with the scene.
     * @return The calculated diffusive color component.
     */
    private Double3 calcDiffusive(Intersection intersection) {
        return intersection.material.kD
                .scale(Math.abs(intersection.lNormal));
    }

    /**
     * Calculates the color at the intersection point by combining ambient light,
     * local effects from light sources, and global effects such as reflections and refractions.
     *
     * @param intersection The intersection point of the ray with the scene.
     * @param ray The ray that intersects with the scene.
     * @return The calculated color at the intersection point.
     */
    private Color calcColor(Intersection intersection, Ray ray) {
        // If the intersection is not valid, return black
        if (!preprocessIntersection(intersection, ray.getDirection()))
            return Color.BLACK;

        // Calculate the ambient light contribution based on the material's ambient coefficient
        Color ambient = scene.ambientLight.getIntensity()
                .scale(intersection.geometry.getMaterial().kA);

        // Calculate the local and global color contributions
        Color localAndGlobal = calcColor(intersection, MAX_CALC_COLOR_LEVEL, INITIAL_K);

        return ambient.add(localAndGlobal);
    }

    /**
     * Calculates the color at the intersection point by combining local and global effects.
     * It recursively calculates the contributions from reflections and refractions based on the material properties.
     *
     * @param intersection The intersection point of the ray with the scene.
     * @param level The recursion level for global effects.
     * @param k The initial color factor for global effects.
     * @return The calculated color at the intersection point.
     */
    private Color calcColor(Intersection intersection, int level, Double3 k) {
        Color local = calcColorLocalEffects(intersection, k);
        if (level == 1)
            return local;

        return local.add(calcColorGlobalEffects(intersection, level, k));
    }

    /**
     * Traces a ray through the scene and calculates the color at the intersection point.
     * If there is no intersection, it returns the background color of the scene.
     *
     * @param ray The ray to be traced through the scene.
     * @return The calculated color at the intersection point or the background color if no intersection occurs.
     */
    @Override
    public Color traceRay(Ray ray) {
        Intersection intersection = findClosestIntersection(ray);
        return intersection == null ? scene.background : calcColor(intersection, ray);
    }
}