package geometries;
import java.util.ArrayList;
import java.util.List;
import primitives.Point;
import primitives.Ray;

public class Geometries {
    private List<Intersectable> geometries = new ArrayList<>();

    /**
     * Constructor for the Geometries class.
     * @author Yehuda Rubin and Arye Hacohen
     */
    public Geometries() {}

    /**
     * Constructor for the Geometries class with a list of geometries.
     *
     * @param geometries The list of geometries to be added.
     * @author Yehuda Rubin and Arye Hacohen
     */
    public Geometries(Intersectable... geometries) {
        for (Intersectable geometry : geometries) {
            this.geometries.add(geometry);
        }
    }

    /**
     * Adds a new geometry to the list of geometries.
     *
     * @param geometry The geometry to be added.
     */
    public void add(Intersectable geometry) {
        this.geometries.add(geometry);
    }

    /**
     * Finds intersections of the given ray with all geometries in the list.
     *
     * @param ray The ray to find intersections with.
     * @return A list of intersection points, or null if no intersections are found.
     */
    public List<Point> findIntersections(Ray ray) {
        List<Point> intersections = null;
        for (Intersectable geometry : geometries) {
            List<Point> tempIntersections = geometry.findIntersections(ray);
            if (tempIntersections != null) {
                if (intersections == null) {
                    intersections = new ArrayList<>();
                }
                intersections.addAll(tempIntersections);
            }
        }
        return intersections;
    }
}
