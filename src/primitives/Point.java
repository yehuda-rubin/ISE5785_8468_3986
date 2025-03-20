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
        return new Vector(xyz.d1 - p.xyz.d1, xyz.d2 - p.xyz.d2, xyz.d3 - p.xyz.d3);
    }



}


