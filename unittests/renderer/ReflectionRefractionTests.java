package renderer;

import static java.awt.Color.*;

import org.junit.jupiter.api.Test;

import geometries.*;
import lighting.*;
import primitives.*;
import scene.Scene;

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
}
