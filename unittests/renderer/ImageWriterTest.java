package renderer;

import geometries.Plane;
import geometries.Sphere;
import geometries.Triangle;
import lighting.AmbientLight;
import lighting.DirectionalLight;
import lighting.PointLight;
import lighting.SpotLight;
import org.junit.jupiter.api.Test;
import primitives.Color;
import primitives.Material;
import primitives.Point;
import primitives.Vector;

import geometries.*;
import lighting.*;
import primitives.*;
import scene.Scene;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the ImageWriter class.
 * This class tests the functionality of the ImageWriter class, which is responsible for writing images to files.
 * The tests include creating an image with a specific pattern and verifying that the image is created successfully.
 */
class ImageWriterTest {
    /**
     * A table of 16 by 10 squares, each square measuring 50 by 50 pixels, with the inside of the square colored yellow and the outside red.
     */
    @Test
    void testWriteToImage() {
        assertDoesNotThrow(() -> {
            Color yellow = new Color(java.awt.Color.YELLOW);
            Color red = new Color(java.awt.Color.RED);
            ImageWriter imageWriter = new ImageWriter(801, 501);
            for (int i = 0; i < 801; i++) {
                for (int j = 0; j < 501; j++) {
                    if (i % 50 == 0 || j % 50 == 0) {
                        imageWriter.writePixel(i, j, red);
                    } else {
                        imageWriter.writePixel(i, j, yellow);
                    }
                }
            }
            imageWriter.writeToImage("testImage");
        }, "Failed to create image");
    }

    /** Scene for the tests */
    private final Scene          scene         = new Scene("Test scene");
    /** Camera builder for the tests with triangles */
    private final Camera.Builder cameraBuilder = Camera.getBuilder()     //
            .setRayTracer(scene, RayTracerType.SIMPLE);

    /**
     * Complex scene test with multiple geometries and all light types
     * Creates a beautiful artistic composition
     */
    @Test
    void complexArtisticScene() {
        // Floor - large reflective plane
        scene.geometries.add(
                new Plane(new Point(0, -500, 0), new Vector(0, 1, 0))
                        .setEmission(new Color(10, 10, 30))
                        .setMaterial(new Material().setKD(0.3).setKS(0.7).setShininess(80).setKR(0.6))
        );

        // Back wall - slightly reflective plane
        scene.geometries.add(
                new Plane(new Point(0, 0, -1500), new Vector(0, 0, 1))
                        .setEmission(new Color(40, 20, 20))
                        .setMaterial(new Material().setKD(0.6).setKS(0.4).setShininess(30).setKR(0.2))
        );

        // Side mirror wall - highly reflective plane
        scene.geometries.add(
                new Plane(new Point(-800, 0, 0), new Vector(1, 0, 0))
                        .setEmission(new Color(20, 20, 40))
                        .setMaterial(new Material().setKD(0.1).setKS(0.9).setShininess(120).setKR(0.8))
        );

        // Central large glass sphere
        scene.geometries.add(
                new Sphere(120d, new Point(0, -200, -400))
                        .setEmission(new Color(5, 5, 25))
                        .setMaterial(new Material().setKD(0.1).setKS(0.9).setShininess(100).setKT(0.8).setKR(0.1))
        );

        // Smaller colored spheres around the central one
        scene.geometries.add(
                // Red sphere
                new Sphere(60d, new Point(-200, -300, -300))
                        .setEmission(new Color(100, 20, 20))
                        .setMaterial(new Material().setKD(0.4).setKS(0.6).setShininess(80).setKT(0.3)),

                // Green sphere
                new Sphere(50d, new Point(180, -320, -250))
                        .setEmission(new Color(20, 100, 20))
                        .setMaterial(new Material().setKD(0.4).setKS(0.6).setShininess(60).setKR(0.4)),

                // Blue translucent sphere
                new Sphere(40d, new Point(-100, -100, -200))
                        .setEmission(new Color(20, 20, 150))
                        .setMaterial(new Material().setKD(0.3).setKS(0.7).setShininess(90).setKT(0.6)),

                // Golden metallic sphere
                new Sphere(35d, new Point(120, -150, -350))
                        .setEmission(new Color(150, 120, 30))
                        .setMaterial(new Material().setKD(0.2).setKS(0.8).setShininess(120).setKR(0.7)),

                // Purple glass sphere
                new Sphere(45d, new Point(-80, -250, -500))
                        .setEmission(new Color(80, 20, 120))
                        .setMaterial(new Material().setKD(0.2).setKS(0.5).setShininess(70).setKT(0.7))
        );

        // Floating triangular prisms
        scene.geometries.add(
                // Crystal 1
                new Triangle(new Point(-300, 0, -200), new Point(-250, -50, -180), new Point(-280, 50, -220))
                        .setEmission(new Color(200, 200, 255))
                        .setMaterial(new Material().setKD(0.1).setKS(0.9).setShininess(150).setKT(0.8).setKR(0.2)),

                // Crystal 2
                new Triangle(new Point(250, 100, -300), new Point(300, 50, -280), new Point(275, 150, -320))
                        .setEmission(new Color(255, 200, 200))
                        .setMaterial(new Material().setKD(0.1).setKS(0.9).setShininess(150).setKT(0.8).setKR(0.2)),

                // Crystal 3
                new Triangle(new Point(50, 200, -150), new Point(100, 150, -130), new Point(75, 250, -170))
                        .setEmission(new Color(200, 255, 200))
                        .setMaterial(new Material().setKD(0.1).setKS(0.9).setShininess(150).setKT(0.8).setKR(0.2))
        );

        // PYRAMID - Floating pyramid 40 units above the central sphere
        // Pyramid base corners (positioned 40 units above central sphere)
        Point pyramidBase1 = new Point(-60, -40, -380);   // Front-right
        Point pyramidBase2 = new Point(60, -40, -380);    // Front-left
        Point pyramidBase3 = new Point(60, -40, -420);    // Back-left
        Point pyramidBase4 = new Point(-60, -40, -420);   // Back-right
        Point pyramidApex = new Point(0, 20, -400);       // Top point

        // Pyramid base (using two triangles to form a square base)
        scene.geometries.add(
                // Base triangle 1
                new Triangle(pyramidBase1, pyramidBase2, pyramidBase3)
                        .setEmission(new Color(0, 400, 0))
                        .setMaterial(new Material().setKD(0.1).setKS(0.9).setShininess(100).setKR(0.8).setKT(0.3)),

                // Base triangle 2
                new Triangle(pyramidBase1, pyramidBase3, pyramidBase4)
                        .setEmission(new Color(0, 400, 0))
                        .setMaterial(new Material().setKD(0.1).setKS(0.9).setShininess(100).setKR(0.8).setKT(0.3))
        );

        // Pyramid faces (4 triangular faces)
        scene.geometries.add(
                // Front face
                new Triangle(pyramidBase1, pyramidBase2, pyramidApex)
                        .setEmission(new Color(0, 400, 0))
                        .setMaterial(new Material().setKD(0.1).setKS(0.9).setShininess(100).setKR(0.8).setKT(0.4)),

                // Right face
                new Triangle(pyramidBase2, pyramidBase3, pyramidApex)
                        .setEmission(new Color(0, 400, 0))
                        .setMaterial(new Material().setKD(0.1).setKS(0.9).setShininess(100).setKR(0.8).setKT(0.4)),

                // Back face
                new Triangle(pyramidBase3, pyramidBase4, pyramidApex)
                        .setEmission(new Color(0, 400, 0))
                        .setMaterial(new Material().setKD(0.1).setKS(0.9).setShininess(100).setKR(0.8).setKT(0.4)),

                // Left face
                new Triangle(pyramidBase4, pyramidBase1, pyramidApex)
                        .setEmission(new Color(0, 400,0 ))
                        .setMaterial(new Material().setKD(0.1).setKS(0.9).setShininess(100).setKR(0.8).setKT(0.4))
        );

        // Set ambient light for overall illumination
        scene.setAmbientLight(new AmbientLight(new Color(15, 15, 20)));

        // Multiple light sources for dramatic effect
        scene.lights.add(
                // Main spotlight from above-left
                new SpotLight(new Color(800, 600, 400), new Point(-500, 500, 200), new Vector(1, -1, -1))
                        .setKl(0.0001).setKq(0.000001));

        scene.lights.add(
                // Directional light for general illumination
                new DirectionalLight(new Color(300, 400, 500), new Vector(0.5, -1, -0.5)));

        scene.lights.add(
                // Point light for highlighting central sphere
                new PointLight(new Color(600, 600, 800), new Point(0, 200, -200))
                        .setKl(0.0005).setKq(0.000002));

        scene.lights.add(
                // Colored spotlight from right
                new SpotLight(new Color(400, 200, 600), new Point(400, 300, 100), new Vector(-1, -0.5, -1))
                        .setKl(0.0002).setKq(0.000003));

        scene.lights.add(
                // Soft point light from behind
                new PointLight(new Color(200, 300, 400), new Point(0, 0, 500))
                        .setKl(0.001).setKq(0.000005)
        );

        cameraBuilder
                .setLocation(new Point(300, 200, 800))
                .setDirection(new Point(-50, -100, -300), Vector.AXIS_Y)
                .setVpDistance(1000).setVpSize(400, 400)
                .setResolution(800, 800)
                //.setDepthOfField(15.0, 4, 8)
                .build()
                .renderImage()
                .writeToImage("complexArtisticScene2");
    }

    /**
     * Complex scene test with multiple geometries and all light types
     * Creates a beautiful artistic composition
     */
    @Test
    void complexArtisticScene3() {
        // Floor - large reflective plane
        scene.geometries.add(
                new Plane(new Point(0, -500, 0), new Vector(0, 1, 0))
                        .setEmission(new Color(10, 10, 30))
                        .setMaterial(new Material().setKD(0.3).setKS(0.7).setShininess(80).setKR(0.6))
        );

        // Back wall - slightly reflective plane
        scene.geometries.add(
                new Plane(new Point(0, 0, -1500), new Vector(0, 0, 1))
                        .setEmission(new Color(40, 20, 20))
                        .setMaterial(new Material().setKD(0.6).setKS(0.4).setShininess(30).setKR(0.2))
        );

        // Side mirror wall - highly reflective plane
        scene.geometries.add(
                new Plane(new Point(-800, 0, 0), new Vector(1, 0, 0))
                        .setEmission(new Color(20, 20, 40))
                        .setMaterial(new Material().setKD(0.1).setKS(0.9).setShininess(120).setKR(0.8))
        );

        // Central large glass sphere
        scene.geometries.add(
                new Sphere(120d, new Point(0, -200, -400))
                        .setEmission(new Color(5, 5, 25))
                        .setMaterial(new Material().setKD(0.1).setKS(0.9).setShininess(100).setKT(0.8).setKR(0.1))
        );

        // Smaller colored spheres around the central one
        scene.geometries.add(
                // Red sphere
                new Sphere(60d, new Point(-200, -300, -300))
                        .setEmission(new Color(100, 20, 20))
                        .setMaterial(new Material().setKD(0.4).setKS(0.6).setShininess(80).setKT(0.3)),

                // Green sphere
                new Sphere(50d, new Point(180, -320, -250))
                        .setEmission(new Color(20, 100, 20))
                        .setMaterial(new Material().setKD(0.4).setKS(0.6).setShininess(60).setKR(0.4)),

                // Blue translucent sphere
                new Sphere(40d, new Point(-100, -100, -200))
                        .setEmission(new Color(20, 20, 150))
                        .setMaterial(new Material().setKD(0.3).setKS(0.7).setShininess(90).setKT(0.6)),

                // Golden metallic sphere
                new Sphere(35d, new Point(120, -150, -350))
                        .setEmission(new Color(150, 120, 30))
                        .setMaterial(new Material().setKD(0.2).setKS(0.8).setShininess(120).setKR(0.7)),

                // Purple glass sphere
                new Sphere(45d, new Point(-80, -250, -500))
                        .setEmission(new Color(80, 20, 120))
                        .setMaterial(new Material().setKD(0.2).setKS(0.5).setShininess(70).setKT(0.7))
        );

        // RED CUBE - using red sphere color (100, 20, 20)
        // Cube center near red sphere at (-150, -250, -320), size 50x50x50
        Point c1 = new Point(-175, -225, -295); // front-bottom-left
        Point c2 = new Point(-125, -225, -295); // front-bottom-right
        Point c3 = new Point(-125, -275, -295); // front-top-right
        Point c4 = new Point(-175, -275, -295); // front-top-left
        Point c5 = new Point(-175, -225, -345); // back-bottom-left
        Point c6 = new Point(-125, -225, -345); // back-bottom-right
        Point c7 = new Point(-125, -275, -345); // back-top-right
        Point c8 = new Point(-175, -275, -345); // back-top-left

        scene.geometries.add(
                // Front face
                new Triangle(c1, c2, c3).setEmission(new Color(100, 20, 20))
                        .setMaterial(new Material().setKD(0.4).setKS(0.6).setShininess(80).setKR(0.3)),
                new Triangle(c1, c3, c4).setEmission(new Color(100, 20, 20))
                        .setMaterial(new Material().setKD(0.4).setKS(0.6).setShininess(80).setKR(0.3)),

                // Back face
                new Triangle(c6, c5, c8).setEmission(new Color(100, 20, 20))
                        .setMaterial(new Material().setKD(0.4).setKS(0.6).setShininess(80).setKR(0.3)),
                new Triangle(c6, c8, c7).setEmission(new Color(100, 20, 20))
                        .setMaterial(new Material().setKD(0.4).setKS(0.6).setShininess(80).setKR(0.3)),

                // Left face
                new Triangle(c5, c1, c4).setEmission(new Color(100, 20, 20))
                        .setMaterial(new Material().setKD(0.4).setKS(0.6).setShininess(80).setKR(0.3)),
                new Triangle(c5, c4, c8).setEmission(new Color(100, 20, 20))
                        .setMaterial(new Material().setKD(0.4).setKS(0.6).setShininess(80).setKR(0.3)),

                // Right face
                new Triangle(c2, c6, c7).setEmission(new Color(100, 20, 20))
                        .setMaterial(new Material().setKD(0.4).setKS(0.6).setShininess(80).setKR(0.3)),
                new Triangle(c2, c7, c3).setEmission(new Color(100, 20, 20))
                        .setMaterial(new Material().setKD(0.4).setKS(0.6).setShininess(80).setKR(0.3)),

                // Top face
                new Triangle(c4, c3, c7).setEmission(new Color(100, 20, 20))
                        .setMaterial(new Material().setKD(0.4).setKS(0.6).setShininess(80).setKR(0.3)),
                new Triangle(c4, c7, c8).setEmission(new Color(100, 20, 20))
                        .setMaterial(new Material().setKD(0.4).setKS(0.6).setShininess(80).setKR(0.3)),

                // Bottom face
                new Triangle(c1, c5, c6).setEmission(new Color(100, 20, 20))
                        .setMaterial(new Material().setKD(0.4).setKS(0.6).setShininess(80).setKR(0.3)),
                new Triangle(c1, c6, c2).setEmission(new Color(100, 20, 20))
                        .setMaterial(new Material().setKD(0.4).setKS(0.6).setShininess(80).setKR(0.3))
        );

        // BLUE OCTAHEDRON - using blue sphere color (20, 20, 150)
        // Octahedron center near the large central sphere at (80, -120, -350)
        Point octCenter = new Point(80, -120, -350);
        Point octTop = new Point(80, -80, -350);     // top
        Point octBottom = new Point(80, -160, -350); // bottom
        Point octFront = new Point(80, -120, -310);   // front
        Point octBack = new Point(80, -120, -390);    // back
        Point octLeft = new Point(40, -120, -350);    // left
        Point octRight = new Point(120, -120, -350);   // right

        scene.geometries.add(
                // Top pyramid faces
                new Triangle(octTop, octFront, octRight).setEmission(new Color(20, 20, 150))
                        .setMaterial(new Material().setKD(0.3).setKS(0.7).setShininess(90).setKT(0.5)),
                new Triangle(octTop, octRight, octBack).setEmission(new Color(20, 20, 150))
                        .setMaterial(new Material().setKD(0.3).setKS(0.7).setShininess(90).setKT(0.5)),
                new Triangle(octTop, octBack, octLeft).setEmission(new Color(20, 20, 150))
                        .setMaterial(new Material().setKD(0.3).setKS(0.7).setShininess(90).setKT(0.5)),
                new Triangle(octTop, octLeft, octFront).setEmission(new Color(20, 20, 150))
                        .setMaterial(new Material().setKD(0.3).setKS(0.7).setShininess(90).setKT(0.5)),

                // Bottom pyramid faces
                new Triangle(octBottom, octRight, octFront).setEmission(new Color(20, 20, 150))
                        .setMaterial(new Material().setKD(0.3).setKS(0.7).setShininess(90).setKT(0.5)),
                new Triangle(octBottom, octBack, octRight).setEmission(new Color(20, 20, 150))
                        .setMaterial(new Material().setKD(0.3).setKS(0.7).setShininess(90).setKT(0.5)),
                new Triangle(octBottom, octLeft, octBack).setEmission(new Color(20, 20, 150))
                        .setMaterial(new Material().setKD(0.3).setKS(0.7).setShininess(90).setKT(0.5)),
                new Triangle(octBottom, octFront, octLeft).setEmission(new Color(20, 20, 150))
                        .setMaterial(new Material().setKD(0.3).setKS(0.7).setShininess(90).setKT(0.5))
        );

        // Floating triangular prisms
        scene.geometries.add(
                // Crystal 1
                new Triangle(new Point(-300, 0, -200), new Point(-250, -50, -180), new Point(-280, 50, -220))
                        .setEmission(new Color(200, 200, 255))
                        .setMaterial(new Material().setKD(0.1).setKS(0.9).setShininess(150).setKT(0.8).setKR(0.2)),

                // Crystal 2
                new Triangle(new Point(250, 100, -300), new Point(300, 50, -280), new Point(275, 150, -320))
                        .setEmission(new Color(255, 200, 200))
                        .setMaterial(new Material().setKD(0.1).setKS(0.9).setShininess(150).setKT(0.8).setKR(0.2)),

                // Crystal 3
                new Triangle(new Point(50, 200, -150), new Point(100, 150, -130), new Point(75, 250, -170))
                        .setEmission(new Color(200, 255, 200))
                        .setMaterial(new Material().setKD(0.1).setKS(0.9).setShininess(150).setKT(0.8).setKR(0.2))
        );

        // PYRAMID - Floating pyramid 40 units above the central sphere
        // Pyramid base corners (positioned 40 units above central sphere)
        Point pyramidBase1 = new Point(-60, -40, -380);   // Front-right
        Point pyramidBase2 = new Point(60, -40, -380);    // Front-left
        Point pyramidBase3 = new Point(60, -40, -420);    // Back-left
        Point pyramidBase4 = new Point(-60, -40, -420);   // Back-right
        Point pyramidApex = new Point(0, 20, -400);       // Top point

        // Pyramid base (using two triangles to form a square base)
        scene.geometries.add(
                // Base triangle 1
                new Triangle(pyramidBase1, pyramidBase2, pyramidBase3)
                        .setEmission(new Color(0, 400, 0))
                        .setMaterial(new Material().setKD(0.1).setKS(0.9).setShininess(100).setKR(0.8).setKT(0.3)),

                // Base triangle 2
                new Triangle(pyramidBase1, pyramidBase3, pyramidBase4)
                        .setEmission(new Color(0, 400, 0))
                        .setMaterial(new Material().setKD(0.1).setKS(0.9).setShininess(100).setKR(0.8).setKT(0.3))
        );

        // Pyramid faces (4 triangular faces)
        scene.geometries.add(
                // Front face
                new Triangle(pyramidBase1, pyramidBase2, pyramidApex)
                        .setEmission(new Color(0, 400, 0))
                        .setMaterial(new Material().setKD(0.1).setKS(0.9).setShininess(100).setKR(0.8).setKT(0.4)),

                // Right face
                new Triangle(pyramidBase2, pyramidBase3, pyramidApex)
                        .setEmission(new Color(0, 400, 0))
                        .setMaterial(new Material().setKD(0.1).setKS(0.9).setShininess(100).setKR(0.8).setKT(0.4)),

                // Back face
                new Triangle(pyramidBase3, pyramidBase4, pyramidApex)
                        .setEmission(new Color(0, 400, 0))
                        .setMaterial(new Material().setKD(0.1).setKS(0.9).setShininess(100).setKR(0.8).setKT(0.4)),

                // Left face
                new Triangle(pyramidBase4, pyramidBase1, pyramidApex)
                        .setEmission(new Color(0, 400,0 ))
                        .setMaterial(new Material().setKD(0.1).setKS(0.9).setShininess(100).setKR(0.8).setKT(0.4))
        );

        // Set ambient light for overall illumination
        scene.setAmbientLight(new AmbientLight(new Color(15, 15, 20)));

        // Multiple light sources for dramatic effect
        scene.lights.add(
                // Main spotlight from above-left
                new SpotLight(new Color(800, 600, 400), new Point(-500, 500, 200), new Vector(1, -1, -1))
                        .setKl(0.0001).setKq(0.000001));

        scene.lights.add(
                // Directional light for general illumination
                new DirectionalLight(new Color(300, 400, 500), new Vector(0.5, -1, -0.5)));

        scene.lights.add(
                // Point light for highlighting central sphere
                new PointLight(new Color(600, 600, 800), new Point(0, 200, -200))
                        .setKl(0.0005).setKq(0.000002));

        scene.lights.add(
                // Colored spotlight from right
                new SpotLight(new Color(400, 200, 600), new Point(400, 300, 100), new Vector(-1, -0.5, -1))
                        .setKl(0.0002).setKq(0.000003));

        scene.lights.add(
                // Soft point light from behind
                new PointLight(new Color(200, 300, 400), new Point(0, 0, 500))
                        .setKl(0.001).setKq(0.000005)
        );

        cameraBuilder
                .setLocation(new Point(300, 200, 800))
                .setDirection(new Point(-50, -100, -300), Vector.AXIS_Y)
                .setVpDistance(1000).setVpSize(400, 400)
                .setResolution(800, 800)
                //.setDepthOfField(1200, 20, 16)  // ערכים מתאימים יותר
                .build()
                .renderImage()
                .writeToImage("complexArtisticScene3");
    }
}