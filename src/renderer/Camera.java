package renderer;
import primitives.*;
import scene.Scene;

import java.util.MissingResourceException;
import java.util.Random;

/**
 * The Camera class represents a virtual camera in 3D space.
 * It is defined by its position (p0), direction vector (vTo),
 * up vector (vUp), and right vector (vRight).
 * The camera also holds the view plane's distance, width, and height.
 */
public class Camera implements Cloneable {
    /**
     * The position of the camera in 3D space.
     */
    private Point p0;
    /**
     * The direction vector of the camera, indicating where it is pointing.
     */
    private Vector vTo;
    /**
     * The up vector of the camera, indicating the upward direction.
     */
    private Vector vUp;
    /**
     * The right vector of the camera, indicating the rightward direction.
     */
    private Vector vRight;
    /**
     * The distance from the camera to the view plane.
     */
    private double viewPlaneDistance = 0.0;
    /**
     * The width of the view plane.
     */
    private double viewPlaneWidth = 0.0;
    /**
     * The height of the view plane.
     */
    private double viewPlaneHeight = 0.0;

    /**
     * The ray tracer used to trace rays in the scene.
     */
    private RayTracerBase rayTracer;
    /**
     * The image writer used to write the rendered image.
     */
    private ImageWriter imageWriter;

    /**
     * The number of pixels in the x direction.
     */
    private int nX = 1;
    /**
     * The number of pixels in the y direction.
     */
    private int nY = 1;

    // Depth of Field parameters
    /**
     * Focal distance for depth of field. If 0, DoF is disabled.
     */
    private double focalDistance = 0.0;
    /**
     * Aperture radius for depth of field effect.
     */
    private double apertureRadius = 0.0;
    /**
     * Number of samples for depth of field (1-64).
     */
    private int dofSamples = 1;
    /**
     * Random number generator for aperture sampling.
     */
    private Random random = new Random();

    /**
     * Private empty constructor – used only by the Builder.
     */
    private Camera() {}

    /**
     * Casts a single ray for a specific pixel and returns the color.
     * @param j The x-coordinate of the pixel.
     * @param i The y-coordinate of the pixel.
     * @return The color traced by the ray.
     */
    private Color castSingleRay(int j, int i) {
        Ray ray = constructRay(nX, nY, j, i);
        return rayTracer.traceRay(ray);
    }

    /**
     * Casts multiple rays with depth of field for a specific pixel and returns the averaged color.
     * @param j The x-coordinate of the pixel.
     * @param i The y-coordinate of the pixel.
     * @return The averaged color from all DoF samples.
     */
    private Color castRaysWithDoF(int j, int i) {
        if (focalDistance <= 0 || dofSamples == 1 || apertureRadius == 0) {
            return castSingleRay(j, i);
        }

        // Get the primary ray through this pixel
        Ray primaryRay = constructRay(nX, nY, j, i);

        // Find where the primary ray intersects the focal plane
        Point focalPlaneHit = primaryRay.getPoint(0).add(primaryRay.getDirection().scale(focalDistance));

        Color accumulatedColor = Color.BLACK;

        for (int sample = 0; sample < dofSamples; sample++) {
            // Sample a point on the aperture disk
            Point aperturePoint = sampleApertureDisk();

            // Create ray from aperture point to focal plane intersection
            Vector focusDirection = focalPlaneHit.subtract(aperturePoint).normalize();
            Ray dofRay = new Ray(aperturePoint, focusDirection);

            // Trace the ray and accumulate color
            Color sampleColor = rayTracer.traceRay(dofRay);
            accumulatedColor = accumulatedColor.add(sampleColor);
        }

        // Return averaged color
        return accumulatedColor.scale(1.0 / dofSamples);
    }

    /**
     * Samples a random point on the aperture disk using concentric mapping.
     * @return A point on the aperture relative to camera position.
     */
    private Point sampleApertureDisk() {
        if (apertureRadius == 0.0) {
            return p0;
        }

        // Generate random point in unit square [-1,1]^2
        double u = 2.0 * random.nextDouble() - 1.0;
        double v = 2.0 * random.nextDouble() - 1.0;

        // Apply concentric mapping to get uniform distribution on disk
        double r, theta;
        if (Math.abs(u) > Math.abs(v)) {
            r = u;
            theta = (Math.PI / 4.0) * (v / u);
        } else {
            r = v;
            theta = (Math.PI / 2.0) - (Math.PI / 4.0) * (u / v);
        }

        // Convert to Cartesian coordinates on aperture
        double x = apertureRadius * r * Math.cos(theta);
        double y = apertureRadius * r * Math.sin(theta);

        // Transform to camera coordinate system
        return p0.add(vRight.scale(x)).add(vUp.scale(y));
    }

    /**
     * Gets the 3D point on the image plane for a specific pixel.
     * @param nX number of horizontal pixels
     * @param nY number of vertical pixels
     * @param j column index (x-axis)
     * @param i row index (y-axis)
     * @return The 3D point on the image plane
     */
    private Point getPixelPoint(int nX, int nY, int j, int i) {
        double Xj = (j - (nX-1) / 2d) * (viewPlaneWidth / nX);
        double Yi = -(i - (nY-1) / 2d) * (viewPlaneHeight / nY);

        Point pCenter = p0.add(vTo.scale(viewPlaneDistance));
        Point pIJ = pCenter;

        if (Xj != 0) pIJ = pIJ.add(vRight.scale(Xj));
        if (Yi != 0) pIJ = pIJ.add(vUp.scale(Yi));

        return pIJ;
    }

    /**
     * Casts a ray for a specific pixel and traces it to determine the color, then writes the color to the image.
     * @param j The x-coordinate of the pixel.
     * @param i The y-coordinate of the pixel.
     */
    private void castRay(int j, int i) {
        Color color;
        if (focalDistance > 0 && dofSamples > 1 && apertureRadius > 0) {
            color = castRaysWithDoF(j, i);
        } else {
            color = castSingleRay(j, i);
        }
        imageWriter.writePixel(j, i, color);
    }

    /**
     * Sets the depth of field parameters.
     * @param focalDistance Distance to the focal plane
     * @param apertureRadius The radius of the aperture
     * @param samples Number of samples (1-64)
     * @return This camera instance
     */
    public Camera setDepthOfField(double focalDistance, double apertureRadius, int samples) {
        if (samples < 1 || samples > 64) {
            throw new IllegalArgumentException("Number of samples must be between 1 and 64");
        }
        if (apertureRadius < 0) {
            throw new IllegalArgumentException("Aperture radius must be non-negative");
        }
        if (focalDistance <= 0) {
            throw new IllegalArgumentException("Focal distance must be positive");
        }

        this.focalDistance = focalDistance;
        this.apertureRadius = apertureRadius;
        this.dofSamples = samples;
        return this;
    }

    /**
     * Disables depth of field.
     * @return This camera instance
     */
    public Camera disableDepthOfField() {
        this.focalDistance = 0.0;
        this.apertureRadius = 0.0;
        this.dofSamples = 1;
        return this;
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
        imageWriter.writeToImage(fileName);
        return this;
    }

    public Camera renderImage(){
        for (int i = 0; i < nY; i++) {
            for (int j = 0; j < nX; j++) {
                castRay(j,i);
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
         * Sets the depth of field parameters in the builder.
         * @param focalDistance Distance to the focal plane
         * @param apertureRadius Radius of the aperture
         * @param samples Number of samples (1-64)
         * @return this Builder instance
         */
        public Builder setDepthOfField(double focalDistance, double apertureRadius, int samples) {
            if (samples < 1 || samples > 64) {
                throw new IllegalArgumentException("Number of samples must be between 1 and 64");
            }
            if (apertureRadius < 0) {
                throw new IllegalArgumentException("Aperture radius must be non-negative");
            }
            if (focalDistance <= 0) {
                throw new IllegalArgumentException("Focal distance must be positive");
            }

            camera.focalDistance = focalDistance;
            camera.apertureRadius = apertureRadius;
            camera.dofSamples = samples;
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
            Camera cloned = (Camera) super.clone();
            cloned.random = new Random(); // Create new random instance for thread safety
            return cloned;
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
        Point pIJ = getPixelPoint(nX, nY, j, i);
        return new Ray(p0, pIJ.subtract(p0).normalize());
    }
}