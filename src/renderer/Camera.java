package renderer;
import primitives.*;
import scene.Scene;

import java.util.MissingResourceException;

/**
 * The Camera class represents a virtual camera in 3D space.
 * It is defined by its position (p0), direction vector (vTo),
 * up vector (vUp), and right vector (vRight).
 * The camera also holds the view plane's distance, width, and height.
 */
public class Camera implements Cloneable {
    private Point p0;
    private Vector vTo;
    private Vector vUp;
    private Vector vRight;
    private double viewPlaneDistance = 0.0;
    private double viewPlaneWidth = 0.0;
    private double viewPlaneHeight = 0.0;

    private RayTracerBase rayTracer;
    private ImageWriter imageWriter;

    private int nX = 1;
    private int nY = 1;

    /**
     * Private empty constructor – used only by the Builder.
     */
    private Camera() {}

    /**
     * Casts a ray for a specific pixel and traces it to determine the color, then writes the color to the image.
     * @param nX The width of the image.
     * @param nY The height of the image.
     * @param j The x-coordinate of the pixel.
     * @param i The y-coordinate of the pixel.
     */
    private void castRay(int nX, int nY, int j, int i){
        Ray ray = this.constructRay(nX, nY, j, i);
        Color color = this.rayTracer.traceRay(ray);
        this.imageWriter.writePixel(j, i, color);
    }

    /**
     * Creates a new Builder for constructing a Camera.
     * @return a new Builder instance.
     */
    public static Builder getBuilder() {
        return new Builder();
    }

    /**
     * Prints a grid on the image with a specified interval and color.
     * @param interval The interval for the grid lines.
     * @param color The color of the grid lines.
     * @return The camera object after printing the grid.
     */
    public Camera printGrid(int interval, Color color){
        for (int i = 0; i < nY; i++) {
            for (int j = 0; j < nX; j++) {
                if (i % interval == 0 || j % interval == 0){
                    this.imageWriter.writePixel(j, i, color);
                }
            }
        }
        return this;
    }

    /**
     * Writes the rendered image to a file or display.
     */
    public Camera writeToImage(String fileName){
        if (imageWriter == null) {
            throw new IllegalStateException("ImageWriter is not initialized");
        }
        imageWriter.writeToImage(fileName);
        return this;
    }

    public Camera renderImage(){
        if (imageWriter == null) {
            throw new IllegalStateException("ImageWriter is not initialized");
        }
        for (int i = 0; i < nY; i++) {
            for (int j = 0; j < nX; j++) {
                castRay(nX, nY, j, i);
            }
        }
        return this;
    }

    /**
     * Builder class for constructing a Camera instance.
     * Allows step-by-step configuration of the camera properties.
     */
    public static class Builder {
        private final Camera camera = new Camera();

        /**
         * Sets the position of the camera.
         * @param location the 3D point representing the camera location
         * @return this Builder instance
         */
        public Builder setLocation(Point location) {
            camera.p0 = location;
            return this;
        }

        /**
         * Sets the distance from the camera to the view plane.
         * @param distance the positive distance to the view plane
         * @return this Builder instance
         */
        public Builder setVpDistance(double distance) {
            if (distance <= 0) {
                throw new IllegalArgumentException("Distance must be positive");
            }

            camera.viewPlaneDistance = distance;
            return this;
        }

        /**
         * Sets the size of the view plane (width and height).
         * @param width the width of the view plane
         * @param height the height of the view plane
         * @return this Builder instance
         */
        public Builder setVpSize(int width, int height) {
            if (width <= 0 || height <= 0) {
                throw new IllegalArgumentException("Width and viewPlaneHeight must be positive");
            }

            camera.viewPlaneWidth = width;
            camera.viewPlaneHeight = height;
            return this;
        }

        /**
         * Sets the direction of the camera using orthogonal vTo and vUp vectors.
         * @param to the viewing direction vector
         * @param up the up direction vector
         * @return this Builder instance
         */
        public Builder setDirection(Vector to, Vector up) {
            if (!Util.isZero(to.dotProduct(up))) {
                throw new IllegalArgumentException("vTo and vUp must be orthogonal");
            }
            camera.vTo = to.normalize();
            camera.vUp = up.normalize();
            return this;
        }

        /**
         * Sets the camera direction using a target point and an up vector.
         * Calculates vTo, vRight, and vUp accordingly.
         * @param target1 the target point the camera should face
         * @param up the up direction vector
         * @return this Builder instance
         */
        public Builder setDirection(Point target1, Vector up) {
            if (target1.equals(camera.p0))
                throw new IllegalArgumentException("Target point cannot be the same as camera location");

            camera.vTo = target1.subtract(camera.p0).normalize();
            camera.vRight = camera.vTo.crossProduct(up).normalize();
            camera.vUp = camera.vRight.crossProduct(camera.vTo).normalize();

            return this;
        }

        public Builder setRayTracer(Scene scene, RayTracerType type) {
            if ( type == RayTracerType.SIMPLE)
                camera.rayTracer = new SimpleRayTracer(scene);
            else
                camera.rayTracer = null;
            return this;
        }

        /**
         * Sets the camera direction using a target point only.
         * Uses Vector.AXIS_Y as a default up vector and recalculates vRight and vUp.
         * @param target1 the point the camera looks at
         * @return this Builder instance
         */
        public Builder setDirection(Point target1) {
            if (target1.equals(camera.p0))
                throw new IllegalArgumentException("Target point cannot be the same as camera location");

            camera.vTo = target1.subtract(camera.p0).normalize();
            camera.vUp = Vector.AXIS_Y; // Default up vector
            camera.vRight = camera.vTo.crossProduct(camera.vUp).normalize();
            camera.vUp = camera.vRight.crossProduct(camera.vTo).normalize(); // Adjust vUp
            return this;
        }

        /**
         * Sets the resolution (number of pixels) in x and y directions.
         * This method is currently not implemented.
         * @param nX number of horizontal pixels
         * @param nY number of vertical pixels
         * @return null (not implemented)
         */
        public Builder setResolution(int nX, int nY) {
            if (nX <= 0 || nY <= 0) {
                throw new IllegalArgumentException("Number of pixels must be positive");
            }
            camera.nX = nX;
            camera.nY = nY;
            return this;
        }

        /**
         * Builds the Camera object after validating required values.
         * @return a clone of the fully constructed Camera object
         * @throws MissingResourceException if required fields are missing
         */
        public Camera build() {
            final String className = "Camera";
            final String description = "missing values: ";

            if (camera.p0 == null) throw new MissingResourceException(description, className, "p0");
            if (camera.vUp == null) throw new MissingResourceException(description, className, "vUp");
            if (camera.vTo == null) throw new MissingResourceException(description, className, "vTo");
            if (Util.alignZero(camera.viewPlaneWidth) <= 0) throw new IllegalArgumentException("Width must be positive");
            if (Util.alignZero(camera.viewPlaneHeight) <= 0) throw new IllegalArgumentException("Height must be positive");
            if (Util.alignZero(camera.viewPlaneDistance) <= 0) throw new IllegalArgumentException("Distance must be positive");
            if (!Util.isZero(camera.vTo.dotProduct(camera.vUp))) throw new IllegalArgumentException("vTo and vUp must be orthogonal");
            if (Util.alignZero(camera.nX) <= 0) throw new IllegalArgumentException("nX must be positive");
            if (Util.alignZero(camera.nY) <= 0) throw new IllegalArgumentException("nY must be positive");
            camera.imageWriter = new ImageWriter(camera.nX, camera.nY);

            if(camera.rayTracer == null) {
                camera.rayTracer = new SimpleRayTracer(null);
            }

            camera.vRight = camera.vTo.crossProduct(camera.vUp).normalize();

            return camera.clone(); // Return a full clone of the camera
        }
    }

    @Override
    public Camera clone() {
        try {
            return (Camera) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError(); // Should not happen
        }
    }

    /**
     * Constructs a ray from the camera to a specific pixel on the view plane.
     * Each pixel is addressed by its (i, j) index.
     * @param nX number of horizontal pixels
     * @param nY number of vertical pixels
     * @param j column index (x-axis)
     * @param i row index (y-axis)
     * @return a Ray from the camera to the pixel
     */
    public Ray constructRay(int nX, int nY, int j, int i) {
        double Xj = (j - (nX - 1) / 2d) * (viewPlaneWidth / nX);
        double Yi = -(i - (nY - 1) / 2d) * (viewPlaneHeight / nY);

        // Compute the center point of the view plane
        Point pCenter = p0.add(vTo.scale(viewPlaneDistance));
        Point pIJ = pCenter;

        // Offset the point based on the pixel's location
        if (Xj != 0) pIJ = pIJ.add(vRight.scale(Xj));
        if (Yi != 0) pIJ = pIJ.add(vUp.scale(Yi));

        return new Ray(p0, pIJ.subtract(p0).normalize());
    }
}
