package geometries;

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
public class Geometries implements Intersectable {
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