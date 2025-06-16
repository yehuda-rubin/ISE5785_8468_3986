package renderer;

import static java.awt.Color.*;

import org.junit.jupiter.api.Test;

import geometries.*;
import lighting.*;
import primitives.*;
import scene.Scene;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Tests for reflection and transparency functionality, test for partial
 * shadows
 * (with transparency)
 * @author Dan Zilberstein
 */
class ReflectionRefractionTests {
   /** Default constructor to satisfy JavaDoc generator */
   ReflectionRefractionTests() { /* to satisfy JavaDoc generator */ }

   /** Scene for the tests */
   private final Scene          scene         = new Scene("Test scene");
   /** Camera builder for the tests with triangles */
   private final Camera.Builder cameraBuilder = Camera.getBuilder()     //
           .setRayTracer(scene, RayTracerType.SIMPLE);

   /** Produce a picture of a sphere lighted by a spot light */
   @Test
   void twoSpheres() {
      scene.geometries.add( //
              new Sphere(50d,new Point(0, 0, -50)).setEmission(new Color(BLUE)) //
                      .setMaterial(new Material().setKD(0.4).setKS(0.3).setShininess(100).setKT(0.3)), //
              new Sphere(25d,new Point(0, 0, -50)).setEmission(new Color(RED)) //
                      .setMaterial(new Material().setKD(0.5).setKS(0.5).setShininess(100))); //
      scene.lights.add( //
              new SpotLight(new Color(1000, 600, 0), new Point(-100, -100, 500), new Vector(-1, -1, -2)) //
                      .setKl(0.0004).setKq(0.0000006));

      cameraBuilder
              .setLocation(new Point(0, 0, 1000)) //
              .setDirection(Point.ZERO, Vector.AXIS_Y) //
              .setVpDistance(1000).setVpSize(150, 150) //
              .setResolution(500, 500) //
              .build() //
              .renderImage() //
              .writeToImage("refractionTwoSpheres");
   }

   /** Produce a picture of a sphere lighted by a spot light */
   @Test
   void twoSpheresOnMirrors() {
      scene.geometries.add( //
              new Sphere(400d, new Point(-950, -900, -1000)).setEmission(new Color(0, 50, 100)) //
                      .setMaterial(new Material().setKD(0.25).setKS(0.25).setShininess(20) //
                              .setKT(new Double3(0.5, 0, 0))), //
              new Sphere(200d,new Point(-950, -900, -1000)).setEmission(new Color(100, 50, 20)) //
                      .setMaterial(new Material().setKD(0.25).setKS(0.25).setShininess(20)), //
              new Triangle(new Point(1500, -1500, -1500), new Point(-1500, 1500, -1500), //
                      new Point(670, 670, 3000)) //
                      .setEmission(new Color(20, 20, 20)) //
                      .setMaterial(new Material().setKR(1)), //
              new Triangle(new Point(1500, -1500, -1500), new Point(-1500, 1500, -1500), //
                      new Point(-1500, -1500, -2000)) //
                      .setEmission(new Color(20, 20, 20)) //
                      .setMaterial(new Material().setKR(new Double3(0.5, 0, 0.4))));
      scene.setAmbientLight(new AmbientLight(new Color(26, 26, 26)));
      scene.lights.add(new SpotLight(new Color(1020, 400, 400), new Point(-750, -750, -150), new Vector(-1, -1, -4)) //
              .setKl(0.00001).setKq(0.000005));

      cameraBuilder
              .setLocation(new Point(0, 0, 10000)) //
              .setDirection(Point.ZERO, Vector.AXIS_Y) //
              .setVpDistance(10000).setVpSize(2500, 2500) //
              .setResolution(500, 500) //
              .build() //
              .renderImage() //
              .writeToImage("reflectionTwoSpheresMirrored");
   }

   /**
    * Produce a picture of a two triangles lighted by a spot light with a
    * partially
    * transparent Sphere producing partial shadow
    */
   @Test
   void trianglesTransparentSphere() {
      scene.geometries.add(
              new Triangle(new Point(-150, -150, -115), new Point(150, -150, -135),
                      new Point(75, 75, -150))
                      .setMaterial(new Material().setKD(0.5).setKS(0.5).setShininess(60)),
              new Triangle(new Point(-150, -150, -115), new Point(-70, 70, -140), new Point(75, 75, -150))
                      .setMaterial(new Material().setKD(0.5).setKS(0.5).setShininess(60)),
              new Sphere(30d,new Point(60, 50, -50)).setEmission(new Color(BLUE))
                      .setMaterial(new Material().setKD(0.2).setKS(0.2).setShininess(30).setKT(0.6)));
      scene.setAmbientLight(new AmbientLight(new Color(38, 38, 38)));
      scene.lights.add(
              new SpotLight(new Color(700, 400, 400), new Point(60, 50, 0), new Vector(0, 0, -1))
                      .setKl(4E-5).setKq(2E-7));

      cameraBuilder
              .setLocation(new Point(0, 0, 1000)) //
              .setDirection(Point.ZERO, Vector.AXIS_Y) //
              .setVpDistance(1000).setVpSize(200, 200) //
              .setResolution(600, 600) //
              .build() //
              .renderImage() //
              .writeToImage("refractionShadow");
   }

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
              .setDepthOfField(1200, 20, 16)  // ערכים מתאימים יותר
              .build()
              .renderImage()
              .writeToImage("complexArtisticScene");
   }

   private final Scene scene1 = new Scene("Complex Shapes Test Scene");

   /**
    * Camera builder for the tests
    */
   private final Camera.Builder cameraBuilderInstance = Camera.getBuilder()
           .setRayTracer(scene1, RayTracerType.SIMPLE);

   /**
    * Produce a picture of a scene with multiple shapes including planes and polygons.
    */
   @Test
   void complexShapesScene() {
      // Adding various geometries to the scene
      scene1.geometries.add(
              // Planes
              new Plane(new Point(0, -500, 0), new Vector(0, 1, 0))
                      .setEmission(new Color(70, 70, 70))
                      .setMaterial(new Material().setKD(0.5).setKS(0.5).setShininess(50)),
              new Plane(new Point(0, 0, -1000), new Vector(0, 0, 1))
                      .setEmission(new Color(40, 40, 40))
                      .setMaterial(new Material().setKD(0.6).setKS(0.4).setShininess(30)),

              // Spheres
              new Sphere(30, new Point(-100, -100, -600))
                      .setEmission(new Color(255, 0, 0))
                      .setMaterial(new Material().setKD(0.4).setKS(0.6).setShininess(80).setKT(0.5)), // שקופה
              new Sphere(40, new Point(100, -80, -600))
                      .setEmission(new Color(0, 255, 0))
                      .setMaterial(new Material().setKD(0.3).setKS(0.7).setShininess(100) // לא שקופה, אך עם תנאים רגילים
                      ),

              // Triangles
              new Triangle(new Point(-150, -150, -500), new Point(150, -150, -500), new Point(0, 150, -700))
                      .setEmission(new Color(0, 0, 255))
                      .setMaterial(new Material().setKD(0.5).setKS(0.5).setShininess(30)),

              // Polygons with reflection
              new Polygon(new Point(-200, -200, -200), new Point(-100, -200, -100), new Point(-100, -100, -100), new Point(-200, -100, -200))
                      .setEmission(new Color(255, 255, 0))
                      .setMaterial(new Material().setKD(0.3).setKS(0.5).setShininess(50).setKR(0.6)), // מראה

              new Polygon(new Point(200, 200, -200), new Point(100, 200, -100), new Point(100, 100, -100), new Point(200, 100, -200))
                      .setEmission(new Color(255, 0, 255))
                      .setMaterial(new Material().setKD(0.3).setKS(0.5).setShininess(50))
      );

      // Adding lights
      scene1.setAmbientLight(new AmbientLight(new Color(20, 20, 20)));
      scene1.lights.add(new SpotLight(new Color(1000, 600, 0), new Point(-200, -200, 500), new Vector(-1, -1, -2))
              .setKl(0.0004).setKq(0.0000006));

      cameraBuilderInstance
              .setLocation(new Point(0, 0, 1000))
              .setDirection(Point.ZERO, Vector.AXIS_Y)
              .setVpDistance(1000).setVpSize(300, 300)
              .setResolution(500, 500)
              .build()
              .renderImage()
              .writeToImage("complexShapesScene");
   }

   @Test
   void complexArtisticScene1() {
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

      // ADDED POLYGONS FOR ARTISTIC ENHANCEMENT - ALL CONVEX WITH ORDERED VERTICES

      // Hexagonal platform/altar under central sphere (ordered clockwise from top view)
      scene.geometries.add(
              new Polygon(
                      new Point(-80, -380, -320),
                      new Point(-120, -380, -400),
                      new Point(-80, -380, -480),
                      new Point(80, -380, -480),
                      new Point(120, -380, -400),
                      new Point(80, -380, -320)
              ).setEmission(new Color(15, 10, 25))
                      .setMaterial(new Material().setKD(0.4).setKS(0.6).setShininess(90).setKR(0.3))
      );

      // Square decorative element on the floor
      scene.geometries.add(
              new Polygon(
                      new Point(280, -490, -180),
                      new Point(320, -490, -180),
                      new Point(320, -490, -220),
                      new Point(280, -490, -220)
              ).setEmission(new Color(35, 25, 15))
                      .setMaterial(new Material().setKD(0.5).setKS(0.5).setShininess(60).setKR(0.4))
      );

      // Floating pentagonal mirror (ordered counter-clockwise)
      scene.geometries.add(
              new Polygon(
                      new Point(-100, 180, -600),
                      new Point(-60, 150, -580),
                      new Point(-80, 100, -590),
                      new Point(-120, 110, -610),
                      new Point(-140, 150, -620)
              ).setEmission(new Color(20, 20, 30))
                      .setMaterial(new Material().setKD(0.1).setKS(0.9).setShininess(140).setKR(0.8))
      );

      // Diamond-shaped translucent panel (convex rhombus)
      scene.geometries.add(
              new Polygon(
                      new Point(200, 50, -100),
                      new Point(250, 0, -120),
                      new Point(200, -50, -140),
                      new Point(150, 0, -120)
              ).setEmission(new Color(45, 25, 45))
                      .setMaterial(new Material().setKD(0.2).setKS(0.8).setShininess(110).setKT(0.6).setKR(0.2))
      );

      // Rectangular ceiling panel
      scene.geometries.add(
              new Polygon(
                      new Point(-50, 350, -280),
                      new Point(50, 350, -280),
                      new Point(50, 350, -380),
                      new Point(-50, 350, -380)
              ).setEmission(new Color(50, 40, 15))
                      .setMaterial(new Material().setKD(0.3).setKS(0.7).setShininess(100).setKR(0.5))
      );

      // Triangular panel on the side
      scene.geometries.add(
              new Polygon(
                      new Point(400, -50, -350),
                      new Point(450, -100, -300),
                      new Point(400, -150, -400)
              ).setEmission(new Color(25, 40, 35))
                      .setMaterial(new Material().setKD(0.4).setKS(0.6).setShininess(80).setKT(0.4))
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

      // Build and configure camera
      cameraBuilder
              .setLocation(new Point(300, 200, 800))
              .setDirection(new Point(-50, -100, -300), Vector.AXIS_Y)
              .setVpDistance(1000).setVpSize(400, 400)
              .setResolution(800, 800)
              .build()
              .renderImage()
              .writeToImage("complexArtisticScene1");
   }

   @Test
   void footballFieldScene() {
      // מגרש - הדשא הירוק
      scene.geometries.add(
              new Plane(new Point(0, -10, 0), new Vector(0, 1, 0))
                      .setEmission(new Color(34, 139, 34))
                      .setMaterial(new Material().setKD(0.8).setKS(0.2).setShininess(5))
      );

      // קירות האצטדיון
      scene.geometries.add(
              // קיר אחורי
              new Plane(new Point(0, 0, -300), new Vector(0, 0, 1))
                      .setEmission(new Color(60, 60, 60))
                      .setMaterial(new Material().setKD(0.9).setKS(0.1).setShininess(5)),

              // קיר צד שמאל
              new Plane(new Point(-300, 0, 0), new Vector(1, 0, 0))
                      .setEmission(new Color(65, 65, 65))
                      .setMaterial(new Material().setKD(0.9).setKS(0.1).setShininess(5)),

              // קיר צד ימין
              new Plane(new Point(300, 0, 0), new Vector(-1, 0, 0))
                      .setEmission(new Color(65, 65, 65))
                      .setMaterial(new Material().setKD(0.9).setKS(0.1).setShininess(5))
      );

      // שער שמאל - עם יותר כדורים ומיקום טוב יותר
      scene.geometries.add(
              // עמוד שמאל של השער - יותר כדורים לחיבור טוב יותר
              new Sphere(4d, new Point(-200, -5, -50))
                      .setEmission(new Color(255, 255, 255))
                      .setMaterial(new Material().setKD(0.8).setKS(0.2).setShininess(15)),
              new Sphere(4d, new Point(-200, 5, -50))
                      .setEmission(new Color(255, 255, 255))
                      .setMaterial(new Material().setKD(0.8).setKS(0.2).setShininess(15)),
              new Sphere(4d, new Point(-200, 15, -50))
                      .setEmission(new Color(255, 255, 255))
                      .setMaterial(new Material().setKD(0.8).setKS(0.2).setShininess(15)),
              new Sphere(4d, new Point(-200, 25, -50))
                      .setEmission(new Color(255, 255, 255))
                      .setMaterial(new Material().setKD(0.8).setKS(0.2).setShininess(15)),
              new Sphere(4d, new Point(-200, 35, -50))
                      .setEmission(new Color(255, 255, 255))
                      .setMaterial(new Material().setKD(0.8).setKS(0.2).setShininess(15)),
              new Sphere(4d, new Point(-200, 45, -50))
                      .setEmission(new Color(255, 255, 255))
                      .setMaterial(new Material().setKD(0.8).setKS(0.2).setShininess(15)),

              // עמוד ימין של השער - יותר כדורים לחיבור טוב יותר
              new Sphere(4d, new Point(-200, -5, 50))
                      .setEmission(new Color(255, 255, 255))
                      .setMaterial(new Material().setKD(0.8).setKS(0.2).setShininess(15)),
              new Sphere(4d, new Point(-200, 5, 50))
                      .setEmission(new Color(255, 255, 255))
                      .setMaterial(new Material().setKD(0.8).setKS(0.2).setShininess(15)),
              new Sphere(4d, new Point(-200, 15, 50))
                      .setEmission(new Color(255, 255, 255))
                      .setMaterial(new Material().setKD(0.8).setKS(0.2).setShininess(15)),
              new Sphere(4d, new Point(-200, 25, 50))
                      .setEmission(new Color(255, 255, 255))
                      .setMaterial(new Material().setKD(0.8).setKS(0.2).setShininess(15)),
              new Sphere(4d, new Point(-200, 35, 50))
                      .setEmission(new Color(255, 255, 255))
                      .setMaterial(new Material().setKD(0.8).setKS(0.2).setShininess(15)),
              new Sphere(4d, new Point(-200, 45, 50))
                      .setEmission(new Color(255, 255, 255))
                      .setMaterial(new Material().setKD(0.8).setKS(0.2).setShininess(15)),

              // קורה אופקית עליונה - יותר כדורים לחיבור רצוף
              new Sphere(3.5d, new Point(-200, 45, -40))
                      .setEmission(new Color(255, 255, 255))
                      .setMaterial(new Material().setKD(0.8).setKS(0.2).setShininess(15)),
              new Sphere(3.5d, new Point(-200, 45, -30))
                      .setEmission(new Color(255, 255, 255))
                      .setMaterial(new Material().setKD(0.8).setKS(0.2).setShininess(15)),
              new Sphere(3.5d, new Point(-200, 45, -20))
                      .setEmission(new Color(255, 255, 255))
                      .setMaterial(new Material().setKD(0.8).setKS(0.2).setShininess(15)),
              new Sphere(3.5d, new Point(-200, 45, -10))
                      .setEmission(new Color(255, 255, 255))
                      .setMaterial(new Material().setKD(0.8).setKS(0.2).setShininess(15)),
              new Sphere(3.5d, new Point(-200, 45, 0))
                      .setEmission(new Color(255, 255, 255))
                      .setMaterial(new Material().setKD(0.8).setKS(0.2).setShininess(15)),
              new Sphere(3.5d, new Point(-200, 45, 10))
                      .setEmission(new Color(255, 255, 255))
                      .setMaterial(new Material().setKD(0.8).setKS(0.2).setShininess(15)),
              new Sphere(3.5d, new Point(-200, 45, 20))
                      .setEmission(new Color(255, 255, 255))
                      .setMaterial(new Material().setKD(0.8).setKS(0.2).setShininess(15)),
              new Sphere(3.5d, new Point(-200, 45, 30))
                      .setEmission(new Color(255, 255, 255))
                      .setMaterial(new Material().setKD(0.8).setKS(0.2).setShininess(15)),
              new Sphere(3.5d, new Point(-200, 45, 40))
                      .setEmission(new Color(255, 255, 255))
                      .setMaterial(new Material().setKD(0.8).setKS(0.2).setShininess(15))
      );

      // שער ימין - עם יותר כדורים ומיקום טוב יותר
      scene.geometries.add(
              // עמוד שמאל של השער - יותר כדורים לחיבור טוב יותר
              new Sphere(4d, new Point(200, -5, -50))
                      .setEmission(new Color(255, 255, 255))
                      .setMaterial(new Material().setKD(0.8).setKS(0.2).setShininess(15)),
              new Sphere(4d, new Point(200, 5, -50))
                      .setEmission(new Color(255, 255, 255))
                      .setMaterial(new Material().setKD(0.8).setKS(0.2).setShininess(15)),
              new Sphere(4d, new Point(200, 15, -50))
                      .setEmission(new Color(255, 255, 255))
                      .setMaterial(new Material().setKD(0.8).setKS(0.2).setShininess(15)),
              new Sphere(4d, new Point(200, 25, -50))
                      .setEmission(new Color(255, 255, 255))
                      .setMaterial(new Material().setKD(0.8).setKS(0.2).setShininess(15)),
              new Sphere(4d, new Point(200, 35, -50))
                      .setEmission(new Color(255, 255, 255))
                      .setMaterial(new Material().setKD(0.8).setKS(0.2).setShininess(15)),
              new Sphere(4d, new Point(200, 45, -50))
                      .setEmission(new Color(255, 255, 255))
                      .setMaterial(new Material().setKD(0.8).setKS(0.2).setShininess(15)),

              // עמוד ימין של השער - יותר כדורים לחיבור טוב יותר
              new Sphere(4d, new Point(200, -5, 50))
                      .setEmission(new Color(255, 255, 255))
                      .setMaterial(new Material().setKD(0.8).setKS(0.2).setShininess(15)),
              new Sphere(4d, new Point(200, 5, 50))
                      .setEmission(new Color(255, 255, 255))
                      .setMaterial(new Material().setKD(0.8).setKS(0.2).setShininess(15)),
              new Sphere(4d, new Point(200, 15, 50))
                      .setEmission(new Color(255, 255, 255))
                      .setMaterial(new Material().setKD(0.8).setKS(0.2).setShininess(15)),
              new Sphere(4d, new Point(200, 25, 50))
                      .setEmission(new Color(255, 255, 255))
                      .setMaterial(new Material().setKD(0.8).setKS(0.2).setShininess(15)),
              new Sphere(4d, new Point(200, 35, 50))
                      .setEmission(new Color(255, 255, 255))
                      .setMaterial(new Material().setKD(0.8).setKS(0.2).setShininess(15)),
              new Sphere(4d, new Point(200, 45, 50))
                      .setEmission(new Color(255, 255, 255))
                      .setMaterial(new Material().setKD(0.8).setKS(0.2).setShininess(15)),

              // קורה אופקית עליונה - יותר כדורים לחיבור רצוף
              new Sphere(3.5d, new Point(200, 45, -40))
                      .setEmission(new Color(255, 255, 255))
                      .setMaterial(new Material().setKD(0.8).setKS(0.2).setShininess(15)),
              new Sphere(3.5d, new Point(200, 45, -30))
                      .setEmission(new Color(255, 255, 255))
                      .setMaterial(new Material().setKD(0.8).setKS(0.2).setShininess(15)),
              new Sphere(3.5d, new Point(200, 45, -20))
                      .setEmission(new Color(255, 255, 255))
                      .setMaterial(new Material().setKD(0.8).setKS(0.2).setShininess(15)),
              new Sphere(3.5d, new Point(200, 45, -10))
                      .setEmission(new Color(255, 255, 255))
                      .setMaterial(new Material().setKD(0.8).setKS(0.2).setShininess(15)),
              new Sphere(3.5d, new Point(200, 45, 0))
                      .setEmission(new Color(255, 255, 255))
                      .setMaterial(new Material().setKD(0.8).setKS(0.2).setShininess(15)),
              new Sphere(3.5d, new Point(200, 45, 10))
                      .setEmission(new Color(255, 255, 255))
                      .setMaterial(new Material().setKD(0.8).setKS(0.2).setShininess(15)),
              new Sphere(3.5d, new Point(200, 45, 20))
                      .setEmission(new Color(255, 255, 255))
                      .setMaterial(new Material().setKD(0.8).setKS(0.2).setShininess(15)),
              new Sphere(3.5d, new Point(200, 45, 30))
                      .setEmission(new Color(255, 255, 255))
                      .setMaterial(new Material().setKD(0.8).setKS(0.2).setShininess(15)),
              new Sphere(3.5d, new Point(200, 45, 40))
                      .setEmission(new Color(255, 255, 255))
                      .setMaterial(new Material().setKD(0.8).setKS(0.2).setShininess(15))
      );

      // כדור כדורגל - במרכז המגרש - גדול וגבוה
      scene.geometries.add(
              new Sphere(15d, new Point(0, 5, 0))
                      .setEmission(new Color(255, 255, 255))
                      .setMaterial(new Material().setKD(0.6).setKS(0.4).setShininess(30))
      );

      // קווי המגרש - רחבים יותר
      scene.geometries.add(
              // קו האמצע
              new Polygon(
                      new Point(-5, -9.5, -200),
                      new Point(5, -9.5, -200),
                      new Point(5, -9.5, 200),
                      new Point(-5, -9.5, 200)
              ).setEmission(new Color(255, 255, 255))
                      .setMaterial(new Material().setKD(0.8).setKS(0.2).setShininess(10))
      );

      // קווי שער
      scene.geometries.add(
              // קו השער השמאלי
              new Polygon(
                      new Point(-205, -9.5, -55),
                      new Point(-195, -9.5, -55),
                      new Point(-195, -9.5, 55),
                      new Point(-205, -9.5, 55)
              ).setEmission(new Color(255, 255, 255))
                      .setMaterial(new Material().setKD(0.8).setKS(0.2).setShininess(10)),

              // קו השער הימני
              new Polygon(
                      new Point(195, -9.5, -55),
                      new Point(205, -9.5, -55),
                      new Point(205, -9.5, 55),
                      new Point(195, -9.5, 55)
              ).setEmission(new Color(255, 255, 255))
                      .setMaterial(new Material().setKD(0.8).setKS(0.2).setShininess(10))
      );

      // קווי גבול המגרש
      scene.geometries.add(
              // קו עליון
              new Polygon(
                      new Point(-250, -9.5, 195),
                      new Point(250, -9.5, 195),
                      new Point(250, -9.5, 205),
                      new Point(-250, -9.5, 205)
              ).setEmission(new Color(255, 255, 255))
                      .setMaterial(new Material().setKD(0.8).setKS(0.2).setShininess(10)),

              // קו תחתון
              new Polygon(
                      new Point(-250, -9.5, -205),
                      new Point(250, -9.5, -205),
                      new Point(250, -9.5, -195),
                      new Point(-250, -9.5, -195)
              ).setEmission(new Color(255, 255, 255))
                      .setMaterial(new Material().setKD(0.8).setKS(0.2).setShininess(10)),

              // קו שמאל
              new Polygon(
                      new Point(-250, -9.5, -200),
                      new Point(-240, -9.5, -200),
                      new Point(-240, -9.5, 200),
                      new Point(-250, -9.5, 200)
              ).setEmission(new Color(255, 255, 255))
                      .setMaterial(new Material().setKD(0.8).setKS(0.2).setShininess(10)),

              // קו ימין
              new Polygon(
                      new Point(240, -9.5, -200),
                      new Point(250, -9.5, -200),
                      new Point(250, -9.5, 200),
                      new Point(240, -9.5, 200)
              ).setEmission(new Color(255, 255, 255))
                      .setMaterial(new Material().setKD(0.8).setKS(0.2).setShininess(10))
      );

      // תיבות עונשין
      scene.geometries.add(
              // תיבת עונשין שמאל
              new Polygon(
                      new Point(-200, -9.3, -90),
                      new Point(-120, -9.3, -90),
                      new Point(-120, -9.3, -85),
                      new Point(-200, -9.3, -85)
              ).setEmission(new Color(255, 255, 255))
                      .setMaterial(new Material().setKD(0.8).setKS(0.2).setShininess(10)),

              new Polygon(
                      new Point(-200, -9.3, 85),
                      new Point(-120, -9.3, 85),
                      new Point(-120, -9.3, 90),
                      new Point(-200, -9.3, 90)
              ).setEmission(new Color(255, 255, 255))
                      .setMaterial(new Material().setKD(0.8).setKS(0.2).setShininess(10)),

              new Polygon(
                      new Point(-125, -9.3, -85),
                      new Point(-120, -9.3, -85),
                      new Point(-120, -9.3, 85),
                      new Point(-125, -9.3, 85)
              ).setEmission(new Color(255, 255, 255))
                      .setMaterial(new Material().setKD(0.8).setKS(0.2).setShininess(10))
      );

      scene.geometries.add(
              // תיבת עונשין ימין
              new Polygon(
                      new Point(200, -9.3, -90),
                      new Point(120, -9.3, -90),
                      new Point(120, -9.3, -85),
                      new Point(200, -9.3, -85)
              ).setEmission(new Color(255, 255, 255))
                      .setMaterial(new Material().setKD(0.8).setKS(0.2).setShininess(10)),

              new Polygon(
                      new Point(200, -9.3, 85),
                      new Point(120, -9.3, 85),
                      new Point(120, -9.3, 90),
                      new Point(200, -9.3, 90)
              ).setEmission(new Color(255, 255, 255))
                      .setMaterial(new Material().setKD(0.8).setKS(0.2).setShininess(10)),

              new Polygon(
                      new Point(120, -9.3, -85),
                      new Point(125, -9.3, -85),
                      new Point(125, -9.3, 85),
                      new Point(120, -9.3, 85)
              ).setEmission(new Color(255, 255, 255))
                      .setMaterial(new Material().setKD(0.8).setKS(0.2).setShininess(10))
      );

      // עיגול האמצע
      scene.geometries.add(
              new Polygon(
                      new Point(-60, -9.3, -5),
                      new Point(60, -9.3, -5),
                      new Point(60, -9.3, 5),
                      new Point(-60, -9.3, 5)
              ).setEmission(new Color(255, 255, 255))
                      .setMaterial(new Material().setKD(0.8).setKS(0.2).setShininess(10)),

              new Polygon(
                      new Point(-5, -9.3, -60),
                      new Point(5, -9.3, -60),
                      new Point(5, -9.3, 60),
                      new Point(-5, -9.3, 60)
              ).setEmission(new Color(255, 255, 255))
                      .setMaterial(new Material().setKD(0.8).setKS(0.2).setShininess(10))
      );

      // נקודות פנדל
      scene.geometries.add(
              // נקודת פנדל שמאל
              new Sphere(3d, new Point(-160, -8, 0))
                      .setEmission(new Color(255, 255, 255))
                      .setMaterial(new Material().setKD(0.8).setKS(0.2).setShininess(10)),

              // נקודת פנדל ימין
              new Sphere(3d, new Point(160, -8, 0))
                      .setEmission(new Color(255, 255, 255))
                      .setMaterial(new Material().setKD(0.8).setKS(0.2).setShininess(10))
      );

      // תאורת האצטדיון - תאורה מאוזנת עם תאורה נוספת על השערים
      scene.setAmbientLight(new AmbientLight(new Color(20, 20, 20)));

      // אורות האצטדיון
      scene.lights.add(
              // תאורה ראשית מלמעלה
              new SpotLight(new Color(100, 100, 90), new Point(0, 250, 0), new Vector(0, -1, 0))
                      .setKl(0.0001).setKq(0.000001));

      scene.lights.add(
              // תאורה מהצד השמאלי
              new SpotLight(new Color(60, 60, 50), new Point(-150, 120, 80), new Vector(1, -1, -0.5))
                      .setKl(0.0002).setKq(0.000002));

      scene.lights.add(
              // תאורה מהצד הימני
              new SpotLight(new Color(60, 60, 50), new Point(150, 120, 80), new Vector(-1, -1, -0.5))
                      .setKl(0.0002).setKq(0.000002));

      // תאורה מיוחדת לשערים
      scene.lights.add(
              // תאורה על השער השמאלי
              new SpotLight(new Color(80, 80, 70), new Point(-200, 100, 0), new Vector(0, -1, 0))
                      .setKl(0.0003).setKq(0.000003));

      scene.lights.add(
              // תאורה על השער הימני
              new SpotLight(new Color(80, 80, 70), new Point(200, 100, 0), new Vector(0, -1, 0))
                      .setKl(0.0003).setKq(0.000003));

      scene.lights.add(
              // תאורה כללית עדינה
              new DirectionalLight(new Color(25, 25, 22), new Vector(0.3, -1, -0.5)));

      // הגדרת המצלמה - מיקום אופטימלי לראות את כל השערים
      cameraBuilder
              .setLocation(new Point(0, 120, 180))
              .setDirection(new Point(0, -10, 0), Vector.AXIS_Y)
              .setVpDistance(120).setVpSize(450, 300)
              .setResolution(1200, 800)
              .build()
              .renderImage()
              .writeToImage("footballFieldScene");
   }

   /**
    * Complex scene test with multiple geometries and all light types
    * Creates a beautiful artistic composition
    */
   @Test
   void complexArtisticSceneSUPER() {
      // 🚀 Create list to collect all geometries for BVH building
      List<Intersectable> allGeometries = new ArrayList<>();

      // Floor - large reflective plane
      allGeometries.add(
              new Plane(new Point(0, -500, 0), new Vector(0, 1, 0))
                      .setEmission(new Color(10, 10, 30))
                      .setMaterial(new Material().setKD(0.3).setKS(0.7).setShininess(80).setKR(0.6))
      );

      // Back wall - slightly reflective plane
      allGeometries.add(
              new Plane(new Point(0, 0, -1500), new Vector(0, 0, 1))
                      .setEmission(new Color(40, 20, 20))
                      .setMaterial(new Material().setKD(0.6).setKS(0.4).setShininess(30).setKR(0.2))
      );

      // Side mirror wall - highly reflective plane
      allGeometries.add(
              new Plane(new Point(-800, 0, 0), new Vector(1, 0, 0))
                      .setEmission(new Color(20, 20, 40))
                      .setMaterial(new Material().setKD(0.1).setKS(0.9).setShininess(120).setKR(0.8))
      );

      // Central large glass sphere
      allGeometries.add(
              new Sphere(120d, new Point(0, -200, -400))
                      .setEmission(new Color(5, 5, 25))
                      .setMaterial(new Material().setKD(0.1).setKS(0.9).setShininess(100).setKT(0.8).setKR(0.1))
      );

      // Smaller colored spheres around the central one
      allGeometries.addAll(Arrays.asList(
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
      ));

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

      allGeometries.addAll(Arrays.asList(
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
      ));

      // BLUE OCTAHEDRON - using blue sphere color (20, 20, 150)
      // Octahedron center near the large central sphere at (80, -120, -350)
      Point octCenter = new Point(80, -120, -350);
      Point octTop = new Point(80, -80, -350);     // top
      Point octBottom = new Point(80, -160, -350); // bottom
      Point octFront = new Point(80, -120, -310);   // front
      Point octBack = new Point(80, -120, -390);    // back
      Point octLeft = new Point(40, -120, -350);    // left
      Point octRight = new Point(120, -120, -350);   // right

      allGeometries.addAll(Arrays.asList(
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
      ));

      // Floating triangular prisms
      allGeometries.addAll(Arrays.asList(
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
      ));

      // PYRAMID - Floating pyramid 40 units above the central sphere
      // Pyramid base corners (positioned 40 units above central sphere)
      Point pyramidBase1 = new Point(-60, -40, -380);   // Front-right
      Point pyramidBase2 = new Point(60, -40, -380);    // Front-left
      Point pyramidBase3 = new Point(60, -40, -420);    // Back-left
      Point pyramidBase4 = new Point(-60, -40, -420);   // Back-right
      Point pyramidApex = new Point(0, 20, -400);       // Top point

      // Pyramid base (using two triangles to form a square base)
      allGeometries.addAll(Arrays.asList(
              // Base triangle 1
              new Triangle(pyramidBase1, pyramidBase2, pyramidBase3)
                      .setEmission(new Color(0, 400, 0))
                      .setMaterial(new Material().setKD(0.1).setKS(0.9).setShininess(100).setKR(0.8).setKT(0.3)),

              // Base triangle 2
              new Triangle(pyramidBase1, pyramidBase3, pyramidBase4)
                      .setEmission(new Color(0, 400, 0))
                      .setMaterial(new Material().setKD(0.1).setKS(0.9).setShininess(100).setKR(0.8).setKT(0.3))
      ));

      // Pyramid faces (4 triangular faces)
      allGeometries.addAll(Arrays.asList(
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
      ));

      // 🚀 BUILD AUTOMATIC BVH WITH SAH ALGORITHM
      Intersectable bvhRoot = BVHBuilder.buildBVH(allGeometries);
      BVHBuilder.printBVHStatistics(bvhRoot);
      scene.geometries.add(bvhRoot);

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
              .setDepthOfField(1200, 20, 16)  // ערכים מתאימים יותר
              .build()
              .renderImage()
              .writeToImage("complexArtisticSceneSUPER");
   }
}