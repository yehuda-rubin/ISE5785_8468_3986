package primitives;

/**
 * This class make vector in 3D space
 *  xyz is a vector
 * @author    Yehuda rubin and arye hacohen
 */
public class Vector extends Point {

    /**
     * constructor
     * @param x will be d1 value
     * @param y will be d2 value
     * @param z will be d3 value
     */
    public  Vector(double x, double y, double z) {
        super(x, y, z);
        if(x==0 && y==0 && z==0) {
            throw new IllegalArgumentException("Vector cannot be (0,0,0)");
        }
    }

    /**
     * constructor
     * @param xyz will be the vector
     */
    public Vector(Double3 xyz) {
        super(xyz);
        if(xyz.d1()==0 && xyz.d2()==0 && xyz.d3()==0) {
            throw new IllegalArgumentException("Vector cannot be (0,0,0)");
        }
    }

    /**
     * make add between two vectors
     * @param v is the vector that we add from the current vector
     * @return the result of the add
     */
    public Vector add(Vector v) {
        return new Vector(xyz.d1() + v.xyz.d1(), xyz.d2() + v.xyz.d2(), xyz.d3() + v.xyz.d3());
    }

    public Vector scale(double scalar) {
        return new Vector(xyz.d1() * scalar, xyz.d2() * scalar, xyz.d3() * scalar);
    }

    public Vector dotProduct(Vector v) {
        return new Vector(xyz.d1() * v.xyz.d1(), xyz.d2() * v.xyz.d2(), xyz.d3() * v.xyz.d3());
    }

    public Vector crossProduct(Vector v) {
        return new Vector(xyz.d2() * v.xyz.d3() - xyz.d3() * v.xyz.d2(),
                xyz.d3() * v.xyz.d1() - xyz.d1() * v.xyz.d3(),
                xyz.d1() * v.xyz.d2() - xyz.d2() * v.xyz.d1());
    }

    public double lengthSquared() {
        return (xyz.d1() * xyz.d1() + xyz.d2() * xyz.d2() + xyz.d3() * xyz.d3());
    }

    public double length() {
        return Math.sqrt(lengthSquared());
    }

    /**
     * normalize the vector
     * @return the result of the normalize
     */
    public Vector normalize() {
        return scale(1/length());
    }
}
