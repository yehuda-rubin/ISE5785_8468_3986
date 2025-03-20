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

    public add(Vector v) {
        return new Vector(xyz.d1 + v.xyz.d1, xyz.d2 + v.xyz.d2, xyz.d3 + v.xyz.d3);
    }
}
