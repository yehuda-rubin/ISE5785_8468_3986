package renderer;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;

public class Camera implements Cloneable {
    Vector vUp;
    Vector vTo;
    Vector vRight;
    Point p0;

    double distance = 0;
    double width = 0;
    double height = 0;

    /**
     * Constructor for Camera
     * @author Yehuda Rubin and Arye Hacohen
     */
    private Camera() {}

    public static Builder getBuilder() {
        return new Builder();
    }

    public Ray constructRay(int nX, int nY, int j, int i) {
        // Calculate the pixel size
        double pixelWidth = width / nX;
        double pixelHeight = height / nY;

        // Calculate the center of the pixel
        double xJ = (j + 0.5) * pixelWidth - width / 2;
        double yI = (i + 0.5) * pixelHeight - height / 2;

        // Calculate the ray direction
        Vector rayDirection = vTo.add(vRight.scale(xJ)).add(vUp.scale(-yI)).normalize();

        // Create and return the ray
        return new Ray(p0, rayDirection);
    }

    public static class Builder{
        private final Camera camera = new Camera();

        public Builder setLocation(Point p0) {
            camera.p0 = p0;
            return this;
        }

        public Builder setDirection(Vector vTo, Vector vUp) {
            camera.vTo = vTo.normalize();
            camera.vUp = vUp.normalize();
            camera.vRight =  camera.vTo.crossProduct( camera.vUp);
            return this;
        }

        public Builder setDirection(Point target, Vector vUp) {
            if (camera.p0 == null) {
                camera.p0 = Point.ZERO;
            }
            if (target.equals(camera.p0)) {
                throw new IllegalArgumentException("Target point cannot be the same as camera location");
            }
             // Calculate the direction vector from camera location to target
            camera.vTo = target.subtract(camera.p0).normalize();
            camera.vUp = vUp.normalize();
            camera.vRight =  camera.vTo.crossProduct( camera.vUp);
            return this;
        }

        public Builder setDirection(Point target) {
            if (camera.p0 == null) {
                camera.p0 = Point.ZERO;
            }
            if (target.equals(camera.p0)) {
                throw new IllegalArgumentException("Target point cannot be the same as camera location");
            }
             // Calculate the direction vector from camera location to target
            camera.vUp = Vector.AXIS_Y;
            camera.vRight = Vector.AXIS_X;
            camera.vTo = target.subtract(camera.p0).normalize();
            camera.vUp = camera.vTo.crossProduct(camera.vUp).crossProduct(camera.vTo).normalize();
            camera.vRight =  camera.vTo.crossProduct( camera.vUp);
            return this;
        }

        public Builder setVpDistance(double distance) {
            camera.distance = distance;
            return this;
        }

        public Builder setVpSize(double width, double height) {
            camera.width = width;
            camera.height = height;
            return this;
        }

        public Camera build() {
            return camera;
        }

        public void setResolution(int nx, int ny) {
            if (nx <= 0 || ny <= 0) {
                throw new IllegalArgumentException("Resolution must be positive");
            }
            camera.width = nx;
            camera.height = ny;
        }
    }
}
