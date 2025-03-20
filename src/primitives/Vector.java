package primitives;

/**
 * The Vector class extends the Point class to represent a vector in 3D space.
 * Unlike a Point, a Vector has the operations and properties associated with vector mathematics.
 * Vectors cannot represent the zero vector (0, 0, 0).
 */

public class Vector extends Point {

    /**
     * constructor
     * @param x will be d1 value
     * @param y will be d2 value
     * @param z will be d3 value
     */

    public Vector(double x, double y, double z) {
        super(x, y, z);
        if (x == 0 && y == 0 && z == 0) {
            throw new IllegalArgumentException("Vector cannot be (0,0,0)");
        }
    }

    /**
     * constructor
     * @param xyz will be the vector
     */

    public Vector(Double3 xyz) {
        super(xyz);
        if (xyz.d1() == 0 && xyz.d2() == 0 && xyz.d3() == 0) {
            throw new IllegalArgumentException("Vector cannot be (0,0,0)");
        }
    }

    /**
     * make add between two vectors
     * @param vector is the vector that we add from the current vector
     * @return the result of the add
     */

    public Vector add(Vector vector) {
        return new Vector(xyz.d1() + vector.xyz.d1(), xyz.d2() + vector.xyz.d2(), xyz.d3() + vector.xyz.d3());
    }

    /**
     * make multiplying a vector by a scalar
     * @param scalar is the number that will be multiplied by the vector.
     * @return the result of the multiplying
     */

    public Vector scale(double scalar) {
        return new Vector(xyz.d1() * scalar, xyz.d2() * scalar, xyz.d3() * scalar);
    }

    /**
     * make multiplying between two vectors
     * @param vector is the vector that multiplying with the current vector
     * @return number of the product of the product
     */

    public double dotProduct(Vector vector) {
        return xyz.d1() * vector.xyz.d1() + xyz.d2() * vector.xyz.d2() + xyz.d3() * vector.xyz.d3();
    }

    /**
     * make cross product between two vectors
     * @param vector is the vector that we cross product with the current vector
     * @return new vector that is the result of the multiplication
     */

    public Vector crossProduct(Vector vector) {
        return new Vector(xyz.d2() * vector.xyz.d3() - xyz.d3() * vector.xyz.d2(),
                xyz.d3() * vector.xyz.d1() - xyz.d1() * vector.xyz.d3(),
                xyz.d1() * vector.xyz.d2() - xyz.d2() * vector.xyz.d1());
    }

    /**
     * Calculates the length of the vector squared.
     * @return the length of the vector squared.
     */

    public double lengthSquared() {
        return (xyz.d1() * xyz.d1() + xyz.d2() * xyz.d2() + xyz.d3() * xyz.d3());
    }

    /**
     * Calculates the length of the vector
     * @return the length of the vector
     */

    public double length() {
        return Math.sqrt(lengthSquared());
    }

    /**
     * Calculates the length of the vector
     * @return the length of the vector
     */

    public Vector normalize() {
        return scale(1 / length());
    }


    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        return obj instanceof Vector other && super.equals(other);
    }

    @Override
    public String toString() {
        return "->" + super.toString();
    }

}
