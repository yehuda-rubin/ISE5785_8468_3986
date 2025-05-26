package renderer;

import static java.awt.Color.BLUE;

import org.junit.jupiter.api.Test;

import geometries.*;
import lighting.*;
import primitives.*;
import scene.Scene;

/**
 * Test rendering a basic image
 * @author Dan Zilberstein
 */
class LightsTests {
   /** Default constructor to satisfy JavaDoc generator */
   LightsTests() { /* to satisfy JavaDoc generator */ }

   /** First scene for some of tests */
   private final Scene          scene1                  = new Scene("Test scene");
   /** Second scene for some of tests */
   private final Scene          scene2                  = new Scene("Test scene")
      .setAmbientLight(new AmbientLight(new Color(38, 38, 38)));

   /** First camera builder for some of tests */
   private final Camera.Builder camera1                 = Camera.getBuilder()                                          //
      .setRayTracer(scene1, RayTracerType.SIMPLE)                                                                      //
      .setLocation(new Point(0, 0, 1000))                                                                              //
      .setDirection(Point.ZERO, Vector.AXIS_Y)                                                                         //
      .setVpSize(150, 150).setVpDistance(1000);

   /** Second camera builder for some of tests */
   private final Camera.Builder camera2                 = Camera.getBuilder()                                          //
      .setRayTracer(scene2, RayTracerType.SIMPLE)                                                                      //
      .setLocation(new Point(0, 0, 1000))                                                                              //
      .setDirection(Point.ZERO, Vector.AXIS_Y)                                                                         //
      .setVpSize(200, 200).setVpDistance(1000);

   /** Shininess value for most of the geometries in the tests */
   private static final int     SHININESS               = 301;
   /** Diffusion attenuation factor for some of the geometries in the tests */
   private static final double  KD                      = 0.5;
   /** Diffusion attenuation factor for some of the geometries in the tests */
   private static final Double3 KD3                     = new Double3(0.2, 0.6, 0.4);

   /** Specular attenuation factor for some of the geometries in the tests */
   private static final double  KS                      = 0.5;
   /** Specular attenuation factor for some of the geometries in the tests */
   private static final Double3 KS3                     = new Double3(0.2, 0.4, 0.3);

   /** Material for some of the geometries in the tests */
   private final Material       material                = new Material().setKD(KD3).setKS(KS3).setShininess(SHININESS);
   /** Light color for tests with triangles */
   private final Color          trianglesLightColor     = new Color(800, 500, 250);
   /** Light color for tests with sphere */
   private final Color          sphereLightColor        = new Color(800, 500, 0);
   /** Color of the sphere */
   private final Color          sphereColor             = new Color(BLUE).reduce(2);

   /** Center of the sphere */
   private final Point          sphereCenter            = new Point(0, 0, -50);
   /** Radius of the sphere */
   private static final double  SPHERE_RADIUS           = 50d;

   /** The triangles' vertices for the tests with triangles */
   private final Point[]        vertices                =
      {
        // the shared left-bottom:
        new Point(-110, -110, -150),
        // the shared right-top:
        new Point(95, 100, -150),
        // the right-bottom
        new Point(110, -110, -150),
        // the left-top
        new Point(-75, 78, 100)
      };
   /** Position of the light in tests with sphere */
   private final Point          sphereLightPosition     = new Point(-50, -50, 25);
   /** Light direction (directional and spot) in tests with sphere */
   private final Vector         sphereLightDirection    = new Vector(1, 1, -0.5);
   /** Position of the light in tests with triangles */
   private final Point          trianglesLightPosition  = new Point(30, 10, -100);
   /** Light direction (directional and spot) in tests with triangles */
   private final Vector         trianglesLightDirection = new Vector(-2, -2, -2);

   /** The sphere in appropriate tests */
   private final Geometry       sphere                  = new Sphere(SPHERE_RADIUS, sphereCenter)
      .setEmission(sphereColor).setMaterial(new Material().setKD(KD).setKS(KS).setShininess(SHININESS));
   /** The first triangle in appropriate tests */
   private final Geometry       triangle1               = new Triangle(vertices[0], vertices[1], vertices[2])
      .setMaterial(material);
   /** The first triangle in appropriate tests */
   private final Geometry       triangle2               = new Triangle(vertices[0], vertices[1], vertices[3])
      .setMaterial(material);

   /** Produce a picture of a sphere lighted by a directional light */
   @Test
   void sphereDirectional() {
      scene1.geometries.add(sphere);
      scene1.lights.add(new DirectionalLight(sphereLightColor, sphereLightDirection));

      camera1 //
         .setResolution(500, 500) //
         .build() //
         .renderImage() //
         .writeToImage("lightSphereDirectional");
   }

   /** Produce a picture of a sphere lighted by a point light */
   @Test
   void spherePoint() {
      scene1.geometries.add(sphere);
      scene1.lights.add(new PointLight(sphereLightColor, sphereLightPosition) //
         .setKl(0.001).setKq(0.0002));

      camera1 //
         .setResolution(500, 500) //
         .build() //
         .renderImage() //
         .writeToImage("lightSpherePoint");
   }

   /** Produce a picture of a sphere lighted by a spotlight */
   @Test
   void sphereSpot() {
      scene1.geometries.add(sphere);
      scene1.lights.add(new SpotLight(sphereLightColor, sphereLightPosition, sphereLightDirection) //
         .setKl(0.001).setKq(0.0001));

      camera1 //
         .setResolution(500, 500) //
         .build() //
         .renderImage() //
         .writeToImage("lightSphereSpot");
   }

   /** Produce a picture of two triangles lighted by a directional light */
   @Test
   void trianglesDirectional() {
      scene2.geometries.add(triangle1, triangle2);
      scene2.lights.add(new DirectionalLight(trianglesLightColor, trianglesLightDirection));

      camera2.setResolution(500, 500) //
         .build() //
         .renderImage() //
         .writeToImage("lightTrianglesDirectional");
   }

   /** Produce a picture of two triangles lighted by a point light */
   @Test
   void trianglesPoint() {
      scene2.geometries.add(triangle1, triangle2);
      scene2.lights.add(new PointLight(trianglesLightColor, trianglesLightPosition) //
         .setKl(0.001).setKq(0.0002));

      camera2.setResolution(500, 500) //
         .build() //
         .renderImage() //
         .writeToImage("lightTrianglesPoint");
   }

   /** Produce a picture of two triangles lighted by a spotlight */
   @Test
   void trianglesSpot() {
      scene2.geometries.add(triangle1, triangle2);
      scene2.lights.add(new SpotLight(trianglesLightColor, trianglesLightPosition, trianglesLightDirection) //
         .setKl(0.001).setKq(0.0001));

      camera2.setResolution(500, 500) //
         .build() //
         .renderImage() //
         .writeToImage("lightTrianglesSpot");
   }

   /** Produce a picture of a sphere lighted by a narrow spotlight */
   @Test
   void sphereSpotSharp() {
      scene1.geometries.add(sphere);
      scene1.lights
         .add(new SpotLight(sphereLightColor, sphereLightPosition, new Vector(1, 1, -0.5)) //
            .setKl(0.001).setKq(0.00004).setNarrowBeam(10));

      camera1.setResolution(500, 500) //
         .build() //
         .renderImage() //
         .writeToImage("lightSphereSpotSharp");
   }

   /** Produce a picture of two triangles lighted by a narrow spotlight */
   @Test
   void trianglesSpotSharp() {
      scene2.geometries.add(triangle1, triangle2);
      scene2.lights.add(new SpotLight(trianglesLightColor, trianglesLightPosition, trianglesLightDirection) //
         .setKl(0.001).setKq(0.00004).setNarrowBeam(10));

      camera2.setResolution(500, 500) //
         .build() //
         .renderImage() //
         .writeToImage("lightTrianglesSpotSharp");
   }

    /** Produce a picture of two triangles lighted by a narrow spotlight with a different direction */
    @Test
    void trianglesSpotSharp2() {
       scene1.geometries.add(sphere);
       // adding the directional light
       scene1.lights.add(new DirectionalLight(
                       new Color(400, 400, 400), // intensity
                       new Vector(-1, -1, 0.5) // direction
               )
       );
       // adding the point light
       scene1.lights.add(new PointLight(
                       new Color(200, 600, 0), // intensity
                       new Point(0, 0, 50) // position
               ).setKl(0.001).setKq(0.00015) // attenuation coefficients
       );
       // adding the spotlight
       scene1.lights.add(new SpotLight(
                       new Color(800, 0, 0), // intensity
                       new Point(-70, 20, 0), // position
                       new Vector(0, -0.5, -1) // direction
               ).setKl(0.01).setKq(0.000001) // attenuation coefficients
       );
       // adding the narrow spotlight
       scene1.lights.add(new SpotLight(
                       new Color(650, 300, 0), // intensity
                       new Point(-50, -20, 25), // position
                       new Vector(0.2, 0, -0.3) // direction
               ).setKl(0.001).setKq(0.00004) // attenuation coefficients
                       .setNarrowBeam(60)
       );

       camera1 //
               .setResolution(500, 500) //
               .build() //
               .renderImage() //
               .writeToImage("lightSphereAll");
    }

    /** Produce a picture of two triangles lighted by a narrow spotlight with a different direction */
    @Test
    void trianglesSpotSharp2Scene2() {
       scene2.geometries.add(triangle1, triangle2);
       // adding the directional light
       scene2.lights.add(new DirectionalLight(
                       new Color(400, 400, 400), // intensity
                       new Vector(-1, -1, 0.5) // direction
               )
       );
       // adding the point light
       scene2.lights.add(new PointLight(
                       new Color(200, 600, 0), // intensity
                       new Point(0, 0, 50) // position
               ).setKl(0.001).setKq(0.00015) // attenuation coefficients
       );
       // adding the spotlight
       scene2.lights.add(new SpotLight(
                       new Color(800, 100, 0), // intensity
                       new Point(-70, 20, 0), // position
                       new Vector(0, -0.5, -1) // direction
               ).setKl(0.01).setKq(0.000001) // attenuation coefficients
       );
       // adding the narrow spotlight
       scene2.lights.add(new SpotLight(
                       new Color(650, 300, 0), // intensity
                       new Point(0, -20, 25), // position
                       new Vector(0.2, 0, -0.3) // direction
               ).setKl(0.001).setKq(0.00004) // attenuation coefficients
                       .setNarrowBeam(60)
       );

       camera2 //
               .setResolution(500, 500) //
               .build() //
               .renderImage() //
               .writeToImage("lightTrianglesAll");
    }

    /** Produce a picture of two triangles lighted by a narrow spotlight with a different direction */
    @Test
    void trianglesSpotSharp2Scene1() {
       scene1.geometries.add(triangle1, triangle2);
       // adding the directional light
       scene1.lights.add(new DirectionalLight(
                       new Color(500, 500, 500), // intensity
                       new Vector(-1, -1, 0.5) // direction
               )
       );
       // adding the point light
       scene1.lights.add(new PointLight(
                       new Color(400, 400, 0), // intensity
                       new Point(0, 0, 50) // position
               ).setKl(0.001).setKq(0.00015) // attenuation coefficients
       );
       // adding the spotlight
       scene1.lights.add(new SpotLight(
                       new Color(800, 100, 100), // intensity
                       new Point(-70, 20, 0), // position
                       new Vector(0, -0.5, -1) // direction
               ).setKl(0.01).setKq(0.000001) // attenuation coefficients
       );
       // adding the narrow spotlight
       scene1.lights.add(new SpotLight(
                       new Color(650, 300, 0), // intensity
                       new Point(-50, -20, 25), // position
                       new Vector(0.2, 0, -0.3) // direction
               ).setKl(0.001).setKq(0.00004) // attenuation coefficients
                       .setNarrowBeam(60)
       );

       camera1 //
               .setResolution(500, 500) //
               .build() //
               .renderImage() //
               .writeToImage("lightTrianglesAllScene1");
    }

   /** Produce a picture of a sphere with two triangles on its sides lighted by two lights */
   @Test
   void sphereWithTrianglesAndTwoLights() {
      // Add sphere and triangles to the scene
      scene1.geometries.add(
              sphere,
              new Triangle(new Point(-80, 0, -25), new Point(-25, 25, -25), new Point(-25, -25, -25))
                      .setMaterial(material),
              new Triangle(new Point(80, 0, -25), new Point(25, 25, -25), new Point(25, -25, -25))
                      .setMaterial(material)
      );

      scene1.lights.add(new DirectionalLight(
                      new Color(300, 300, 300), // intensity
                      new Vector(-3, -3, -3) // direction
              )
      );

      // adding the point light
      scene2.lights.add(new PointLight(
                      new Color(200, 600, 0), // intensity
                      new Point(0, 0, 50) // position
              ).setKl(0.001).setKq(0.00015) // attenuation coefficients
      );

      // adding the narrow spotlight
      scene1.lights.add(new SpotLight(
                      new Color(650, 300, 0), // intensity
                      new Point(-50, -20, 25), // position
                      new Vector(0.2, 0, -0.3) // direction
              ).setKl(0.001).setKq(0.00004) // attenuation coefficients
                      .setNarrowBeam(60)
      );

      // Render the image
      camera1.setResolution(800, 800)
              .build()
              .renderImage()
              .writeToImage("sphereWithTrianglesAndTwoLights");
   }

   /** Produce a picture of two stars colliding */
   @Test
   void collidingStars() {
      // Add two spheres representing the stars
      scene1.geometries.add(
              new Sphere(50, new Point(-50, 0, -100)) // First star
                      .setEmission(new Color(255, 200, 0)) // Bright yellow
                      .setMaterial(new Material().setKD(0.5).setKS(0.8).setShininess(300)),
              new Sphere(50, new Point(50, 0, -100)) // Second star
                      .setEmission(new Color(255, 0, 0)) // Bright red
                      .setMaterial(new Material().setKD(0.5).setKS(0.8).setShininess(300))
      );

      // Add a point light to simulate the glow of the collision
      scene1.lights.add(new PointLight(new Color(800, 400, 200), new Point(0, 0, -50))
              .setKl(0.0001).setKq(0.00005));

      // Add a spotlight to enhance the collision effect
      scene1.lights.add(new SpotLight(new Color(1000, 500, 300), new Point(0, 0, -50), new Vector(0, 0, -1))
              .setKl(0.0001).setKq(0.00005).setNarrowBeam(30));

      // Render the image
      camera1.setResolution(800, 800)
              .build()
              .renderImage()
              .writeToImage("collidingStars");
   }
}
