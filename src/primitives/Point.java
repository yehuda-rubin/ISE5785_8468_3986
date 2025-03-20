package primitives;

public class Point {
    protected Double3 xyz;
    public static Point ZERO = (0,0,0);

    public Point(double x, double y, double z) {
        xyz = new Double3(x, y, z);
    }

    public Point(Double3 xyz) {
        this.xyz = xyz;
    }

    public Point subtract(Point p) {
        return new Point(xyz.d1 - p.xyz.d1, xyz.d2 - p.xyz.d2, xyz.d3 - p.xyz.d3);
    }

    public Point add(Vector v) {
        return new Point(xyz.d1 + v.xyz.d1, xyz.d2 + v.xyz.d2, xyz.d3 + v.xyz.d3);
    }

    public Point distanceSquared(Point p) {
        return new Point((xyz.d1 - p.xyz.d1) * (xyz.d1 - p.xyz.d1) +
                (xyz.d2 - p.xyz.d2) * (xyz.d2 - p.xyz.d2) +
                (xyz.d3 - p.xyz.d3) * (xyz.d3 - p.xyz.d3));
    }

    public Point distance(Point p) {
        return new Point(Math.sqrt(distanceSquared(p)));
    }



}


