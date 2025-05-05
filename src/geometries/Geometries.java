package geometries;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import primitives.Point;
import primitives.Ray;

/**
 * The Geometries class is a collection of intersectable geometries.
 * It provides methods to add geometries and find intersections with a ray.
 * This class acts as a composite for multiple geometries, allowing operations
 * to be performed on all geometries in the collection.
 *
 * @author Yehuda Rubin and Arye Hacohen
 */
public class Geometries {
    // List to store all geometries in the collection
    private List<Intersectable> geometries = new ArrayList<>();

    /**
     * Default constructor for the Geometries class.
     * Initializes an empty list of geometries.
     */
    public Geometries() {}

    /**
     * Constructor for the Geometries class with a list of geometries.
     * Allows initializing the collection with predefined geometries.
     *
     * @param geometries The list of geometries to be added.
     */
    public Geometries(Intersectable... geometries) {
        // Add each geometry from the provided list to the internal collection
        for (Intersectable geometry : geometries) {
            this.geometries.add(geometry);
        }
    }

    /**
     * Adds a new geometry to the list of geometries.
     * This method allows dynamically adding geometries to the collection.
     *
     * @param geometry The geometry to be added.
     */
    public void add(Intersectable geometry) {
        // Add the provided geometry to the internal list
        this.geometries.add(geometry);
    }

    /**
     * Finds intersections of the given ray with all geometries in the list.
     * Iterates through all geometries and collects intersection points.
     *
     * @param ray The ray to find intersections with.
     * @return A list of intersection points, or null if no intersections are found.
     */
    public List<Point> findIntersections(Ray ray) {
        // Initialize a list to store all intersection points
        List<Point> intersections = new LinkedList<>();

        // Iterate through each geometry in the collection
        for (Intersectable geometry : geometries) {
            // Find intersections of the ray with the current geometry
            List<Point> tempIntersections = geometry.findIntersections(ray);

            // If intersections are found, add them to the main list
            if (tempIntersections != null) {
                // Add all found intersections to the main list
                intersections.addAll(tempIntersections);
            }
        }

        // Return the list of intersections, or null if no intersections were found
        return intersections.isEmpty() ? null : intersections;
    }
}