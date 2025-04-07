package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import static primitives.Util.isZero;
import java.util.List;

/**
 * Triangle class represents a triangle in 3D Cartesian coordinate
 * @author Yehuda rubin and arye hacohen
 */
public class Triangle extends Polygon{
    /**
     * constructor
     * @param p1 will be the first point of the new triangle
     * @param p2 will be the second point of the new triangle
     * @param p3 will be the third point of the new triangle
     */
    public Triangle(Point p1, Point p2, Point p3) {
        super(p1, p2, p3);
    }
    @Override
    public List<Point> findIntersections(Ray ray) {
        return null;
    }
}
