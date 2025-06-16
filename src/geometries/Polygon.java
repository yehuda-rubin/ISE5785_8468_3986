package geometries;

import primitives.AABB;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static primitives.Util.alignZero;
import static primitives.Util.isZero;

/**
 * Polygon class represents two-dimensional polygon in 3D Cartesian coordinate
 * system
 *
 * @author Dan
 */
public class Polygon extends Geometry {
    /**
     * List of polygon's vertices
     */
    protected final List<Point> vertices;
    /**
     * Associated plane in which the polygon lays
     */
    protected final Plane plane;
    /**
     * The size of the polygon - the amount of the vertices in the polygon
     */
    private final int size;

    /**
     * Polygon constructor based on vertices list. The list must be ordered by edge
     * path. The polygon must be convex.
     *
     * @param vertices list of vertices according to their order by
     *                 edge path
     * @throws IllegalArgumentException in any case of illegal combination of
     *                                  vertices:
     *                                  <ul>
     *                                  <li>Less than 3 vertices</li>
     *                                  <li>Consequent vertices are in the same
     *                                  point
     *                                  <li>The vertices are not in the same
     *                                  plane</li>
     *                                  <li>The order of vertices is not according
     *                                  to edge path</li>
     *                                  <li>Three consequent vertices lay in the
     *                                  same line (180&#176; angle between two
     *                                  consequent edges)
     *                                  <li>The polygon is concave (not convex)</li>
     *                                  </ul>
     */
    public Polygon(Point... vertices) {
        if (vertices.length < 3)
            throw new IllegalArgumentException("A polygon can't have less than 3 vertices");
        this.vertices = List.of(vertices);
        size = vertices.length;

        // Generate the plane according to the first three vertices and associate the
        // polygon with this plane.
        // The plane holds the invariant normal (orthogonal unit) vector to the polygon
        plane = new Plane(vertices[0], vertices[1], vertices[2]);
        if (size == 3) return; // no need for more tests for a Triangle

        Vector n = plane.getNormal(vertices[0]);
        // Subtracting any subsequent points will throw an IllegalArgumentException
        // because of Zero Vector if they are in the same point
        Vector edge1 = vertices[size - 1].subtract(vertices[size - 2]);
        Vector edge2 = vertices[0].subtract(vertices[size - 1]);

        // Cross Product of any subsequent edges will throw an IllegalArgumentException
        // because of Zero Vector if they connect three vertices that lay in the same
        // line.
        // Generate the direction of the polygon according to the angle between last and
        // first edge being less than 180deg. It is hold by the sign of its dot product
        // with the normal. If all the rest consequent edges will generate the same sign
        // - the polygon is convex ("kamur" in Hebrew).
        boolean positive = edge1.crossProduct(edge2).dotProduct(n) > 0;
        for (var i = 1; i < size; ++i) {
            // Test that the point is in the same plane as calculated originally
            if (!isZero(vertices[i].subtract(vertices[0]).dotProduct(n)))
                throw new IllegalArgumentException("All vertices of a polygon must lay in the same plane");
            // Test the consequent edges have
            edge1 = edge2;
            edge2 = vertices[i].subtract(vertices[i - 1]);
            if (positive != (edge1.crossProduct(edge2).dotProduct(n) > 0))
                throw new IllegalArgumentException("All vertices must be ordered and the polygon must be convex");
        }
    }

    @Override
    public Vector getNormal(Point point) {
        return plane.getNormal(point);
    }

    /**
     * Return the list of vertices of the polygon
     *
     * @return the list of vertices of the polygon
     */
    @Override
    protected List<Intersection> calculateIntersectionsHelper(Ray ray, double maxDistance) {
        // test the intersections with polygon's plane
        // we prefer to use the helper method so that we already check the distance
        final var intersections = plane.calculateIntersections(ray, maxDistance);
        if (intersections == null)
            return null;

        // Point that represents the ray's head
        final Point rayPoint = ray.getPoint(0);
        // Vector that represents the ray's axis
        final Vector rayVector = ray.getDirection();
        // number that represents the size of vertices
        final int size_vertices = vertices.size();

        // Array of the v vectors in the formula
        List<Vector> vectorsV = new ArrayList<>();
        // Array of the dot-product of the n vectors with the vector of ray in the formula
        double[] s = new double[size_vertices];

        // These vectors can't be the ZERO Vector because it happens only if rayPoint is one of the vertices,
        // which means the ray begins at the plane and there are no intersections with the plane at all,
        // so we would have exit this method already because of the first condition
        for (Point vertex : vertices)
            vectorsV.add(vertex.subtract(rayPoint));

        // These vectors can't be the ZERO Vector because it happens only if two of vectorsV
        // are on the same line, which means rayPoint is on one of the triangle's edges,
        // which means the ray begins at the plane and there are no intersections with the plane at all,
        // so we would have exit this method already because of the first condition
        Vector vector1;
        Vector vector2;
        Vector normal;
        // Calculate the dot product of the ray vector with the normal vector of the polygon
        for (int i=0; i < size_vertices; ++i) {
            // If we are at the last vertex, we need to connect it with the first vertex
            if (i == size_vertices - 1) {
                vector1 = vectorsV.getFirst();
                vector2 = vectorsV.getLast();
            }
            else {
                vector1 = vectorsV.get(i + 1);
                vector2 = vectorsV.get(i);
            }

            // Calculate the normal vector of the polygon using the cross product of two edges
            normal = vector1.crossProduct(vector2);
            s[i] = alignZero(rayVector.dotProduct(normal));

            // If the dot product is zero, it means the ray is parallel to the polygon's plane
            if (i != 0 && s[i] * s[i-1] <= 0)
                return null;
        }
        // Check if the ray intersects the polygon
        Intersection intersection = intersections.getFirst();
        return List.of(new Intersection(this, intersection.point));
    }

    /**
     * Get the vertices of the polygon
     *
     * @return the vertices of the polygon
     */
    @Override
    protected AABB calculateBoundingBox() {
        if (vertices.isEmpty()) {
            return null;
        }

        double minX = vertices.get(0).getX();
        double maxX = vertices.get(0).getX();
        double minY = vertices.get(0).getY();
        double maxY = vertices.get(0).getY();
        double minZ = vertices.get(0).getZ();
        double maxZ = vertices.get(0).getZ();

        for (Point vertex : vertices) {
            minX = Math.min(minX, vertex.getX());
            maxX = Math.max(maxX, vertex.getX());
            minY = Math.min(minY, vertex.getY());
            maxY = Math.max(maxY, vertex.getY());
            minZ = Math.min(minZ, vertex.getZ());
            maxZ = Math.max(maxZ, vertex.getZ());
        }

        return new AABB(new Point(minX, minY, minZ), new Point(maxX, maxY, maxZ));
    }
}
