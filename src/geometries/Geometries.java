package geometries;

import primitives.AABB;
import primitives.Point;
import primitives.Ray;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * The Geometries class is a Composite class that represents a collection of geometries.
 * It provides methods to add geometries and find intersections with a ray.
 * This class acts as a composite for multiple geometries, allowing operations
 * to be performed on all geometries in the collection.
 *
 * @author Yehuda Rubin and Arye Hacohen
 */
public class Geometries extends Intersectable {
    // List to store all geometries in the collection
    private final List<Intersectable> geometries = new LinkedList<>();

    /**
     * Default constructor for the Geometries class.
     * Initializes an empty list of geometries.
     */
    public Geometries() {
    }

    /**
     * Constructor for the Geometries class with a list of geometries.
     * Allows initializing the collection with predefined geometries.
     *
     * @param geometries The list of geometries to be added.
     */
    public Geometries(Intersectable... geometries) {
        // Add each geometry from the provided list to the internal collection
        add(geometries);
    }

    /**
     * Adds a new geometry to the list of geometries.
     * This method allows dynamically adding geometries to the collection.
     *
     * @param geometries The geometry to be added.
     */
    public void add(Intersectable... geometries) {
        // Add each geometry from the provided list to the internal collection
        Collections.addAll(this.geometries, geometries);
    }

    /**
     * Finds intersections of the given ray with all geometries in the list.
     * Iterates through all geometries and collects intersection points.
     *
     * @param ray The ray to find intersections with.
     * @return A list of intersection points, or null if no intersections are found.
     */
    @Override
    public List<Intersection> calculateIntersectionsHelper(Ray ray, double maxDistance) {
        // List that contains all the intersections
        List<Intersection> intersections = null;

        // Loop that goes threw all the geometries and found the intersections
        for (Intersectable geometry : geometries) {
            var geometryIntersections = geometry.calculateIntersections(ray, maxDistance);
            if (geometryIntersections != null)
                if (intersections == null)
                    intersections = new LinkedList<>(geometryIntersections);
                else
                    intersections.addAll(geometryIntersections);
        }
        return intersections;
    }

    /**
     * Calculate the bounding box that contains all geometries in this collection.
     * Combines all individual bounding boxes into one that encompasses all geometries.
     *
     * @return AABB that contains all geometries, or null if collection is empty or no valid boxes
     */
    @Override
    protected AABB calculateBoundingBox() {
        if (geometries.isEmpty()) {
            return null;
        }

        AABB combinedBox = null;

        // עבור על כל הגיאומטריות וחבר את ה-bounding boxes שלהן
        for (Intersectable geometry : geometries) {
            AABB currentBox = geometry.getBoundingBox();
            if (currentBox != null) {
                if (combinedBox == null) {
                    combinedBox = currentBox;
                } else {
                    combinedBox = AABB.combine(combinedBox, currentBox);
                }
            }
        }

        return combinedBox;
    }

    /**
     * Get the number of geometries in this collection.
     * Useful for performance analysis and debugging.
     *
     * @return the number of geometries in the collection
     */
    public int size() {
        return geometries.size();
    }

    /**
     * Check if the collection is empty.
     *
     * @return true if no geometries are in the collection
     */
    public boolean isEmpty() {
        return geometries.isEmpty();
    }

    /**
     * Clear all geometries from the collection.
     * Useful for resetting the scene.
     */
    public void clear() {
        geometries.clear();
        invalidateBoundingBox(); // Force recalculation of bounding box
    }
}