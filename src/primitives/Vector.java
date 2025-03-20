package primitives;

public class Vector extends Point {

    public  Vector(double x, double y, double z) {
        super(x, y, z);
        if(x==0 && y==0 && z==0) {
            throw new IllegalArgumentException("Vector cannot be (0,0,0)");
        }
    }

    public Vector(Double3 xyz) {
        super(xyz);
        if(xyz.d1==0 && xyz.d2==0 && xyz.d3==0) {
            throw new IllegalArgumentException("Vector cannot be (0,0,0)");
        }
    }

    public Vector add(Vector v) {
        return new Vector(xyz.d1 + v.xyz.d1, xyz.d2 + v.xyz.d2, xyz.d3 + v.xyz.d3);
    }

    public Vector scale(double scalar) {
        return new Vector(xyz.d1 * scalar, xyz.d2 * scalar, xyz.d3 * scalar);
    }

    public Vector dotProduct(Vector v) {
        return new Vector(xyz.d1 * v.xyz.d1, xyz.d2 * v.xyz.d2, xyz.d3 * v.xyz.d3);
    }

    public Vector crossProduct(Vector v) {
        return new Vector(xyz.d2 * v.xyz.d3 - xyz.d3 * v.xyz.d2,
                xyz.d3 * v.xyz.d1 - xyz.d1 * v.xyz.d3,
                xyz.d1 * v.xyz.d2 - xyz.d2 * v.xyz.d1);
    }

    public Vector lengthSquared() {
        return new Vector(xyz.d1 * xyz.d1 + xyz.d2 * xyz.d2 + xyz.d3 * xyz.d3);
    }

    public Vector length() {
        return new Vector(Math.sqrt(lengthSquared()));
    }

}
