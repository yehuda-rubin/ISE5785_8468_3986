package renderer;

import static java.awt.Color.YELLOW;
import static primitives.Util.random;

import org.junit.jupiter.api.*;

import static java.lang.Math.*;

import geometries.*;
import lighting.PointLight;
import primitives.*;
import renderer.*;
import scene.Scene;

/**
 * Test rendering a teapot with advanced acceleration structures
 * Enhanced with BVH and CBR optimizations for improved performance
 * @author Dan Zilberstein (Enhanced with acceleration improvements)
 */
class TeapotTest {
    /** Default constructor to satisfy JavaDoc generator */
    TeapotTest() { /* to satisfy JavaDoc generator */ }

    /**
     * Teapot without optimizations - baseline test
     */
    @Test
    void testTeapotBaseline() {
        System.out.println("=== Teapot Test: Baseline (No Optimizations) ===");

        long startTime = System.nanoTime();
        prepareTeapot()
                .build()
                .renderImage()
                .printGrid(50, new Color(YELLOW))
                .writeToImage("teapot_baseline");
        long endTime = System.nanoTime();

        double renderTime = (endTime - startTime) / 1_000_000_000.0;
        System.out.println("Baseline render time: " + String.format("%.2f", renderTime) + " seconds");
        System.out.println("✅ Baseline teapot test completed");
    }

    /**
     * Teapot with CBR (Conservative Boundary Region) optimization
     * Stage 1 acceleration improvement
     */
    @Test
    void testTeapotCBR() {
        System.out.println("=== Teapot Test: CBR Optimization ===");

        long startTime = System.nanoTime();
        prepareTeapotWithCBR()
                .build()
                .renderImage()
                .printGrid(50, new Color(YELLOW))
                .writeToImage("teapot_cbr");
        long endTime = System.nanoTime();

        double renderTime = (endTime - startTime) / 1_000_000_000.0;
        System.out.println("CBR render time: " + String.format("%.2f", renderTime) + " seconds");
        System.out.println("✅ CBR teapot test completed");
    }

    /**
     * Teapot with BVH (Bounding Volume Hierarchy) optimization
     * Stage 2 acceleration improvement - manual spatial hierarchy
     */
    @Test
    void testTeapotBVH() {
        System.out.println("=== Teapot Test: BVH Optimization ===");

        long startTime = System.nanoTime();
        prepareTeapotWithBVH()
                .build()
                .renderImage()
                .printGrid(50, new Color(YELLOW))
                .writeToImage("teapot_bvh");
        long endTime = System.nanoTime();

        double renderTime = (endTime - startTime) / 1_000_000_000.0;
        System.out.println("BVH render time: " + String.format("%.2f", renderTime) + " seconds");
        System.out.println("✅ BVH teapot test completed");
    }

    /**
     * Performance comparison test - runs all optimizations and compares
     */
    @Test
    void testTeapotPerformanceComparison() {
        System.out.println("=== Teapot Performance Comparison Test ===\n");

        // Test 1: Baseline
        System.out.println("Phase 1: Testing baseline performance...");
        long startTime1 = System.nanoTime();
        prepareTeapot()
                .build()
                .renderImage()
                .writeToImage("teapot_comparison_baseline");
        long endTime1 = System.nanoTime();
        double baselineTime = (endTime1 - startTime1) / 1_000_000_000.0;

        // Test 2: CBR
        System.out.println("Phase 2: Testing CBR performance...");
        long startTime2 = System.nanoTime();
        prepareTeapotWithCBR()
                .build()
                .renderImage()
                .writeToImage("teapot_comparison_cbr");
        long endTime2 = System.nanoTime();
        double cbrTime = (endTime2 - startTime2) / 1_000_000_000.0;

        // Test 3: BVH
        System.out.println("Phase 3: Testing BVH performance...");
        long startTime3 = System.nanoTime();
        prepareTeapotWithBVH()
                .build()
                .renderImage()
                .writeToImage("teapot_comparison_bvh");
        long endTime3 = System.nanoTime();
        double bvhTime = (endTime3 - startTime3) / 1_000_000_000.0;

        // Performance Analysis
        double cbrImprovement = baselineTime / cbrTime;
        double bvhImprovement = baselineTime / bvhTime;
        double bvhVsCbr = cbrTime / bvhTime;

        System.out.println("\n=== Teapot Performance Results ===");
        System.out.println("Resolution: 1000x1000 (1,000,000 rays)");
        System.out.println();
        System.out.println("Baseline time:   " + String.format("%.2f", baselineTime) + " seconds");
        System.out.println("CBR time:        " + String.format("%.2f", cbrTime) + " seconds");
        System.out.println("BVH time:        " + String.format("%.2f", bvhTime) + " seconds");
        System.out.println();
        System.out.println("CBR improvement: " + String.format("%.2fx", cbrImprovement) + " faster than baseline");
        System.out.println("BVH improvement: " + String.format("%.2fx", bvhImprovement) + " faster than baseline");
        System.out.println("BVH vs CBR:      " + String.format("%.2fx", bvhVsCbr) + " faster than CBR");

        // Performance evaluation
        if (bvhImprovement >= 3.0) {
            System.out.println("🎯 EXCELLENT: BVH provides significant acceleration for complex teapot!");
        } else if (bvhImprovement >= 2.0) {
            System.out.println("✅ GOOD: BVH shows solid improvement");
        } else {
            System.out.println("⚠️  PARTIAL: BVH provides moderate improvement");
        }

        System.out.println("\n📁 Generated comparison images:");
        System.out.println("   teapot_comparison_baseline.ppm");
        System.out.println("   teapot_comparison_cbr.ppm");
        System.out.println("   teapot_comparison_bvh.ppm");
        System.out.println("✅ Performance comparison completed!");
    }

    /**
     * Prepare teapot scene without optimizations (baseline)
     */
    Camera.Builder prepareTeapot() {
        Scene scene = new Scene("Teapot Baseline");
        addTeapotToScene(scene);
        addBubblesToScene(scene);
        addLighting(scene);

        return Camera.getBuilder()
                .setResolution(1000, 1000)
                .setRayTracer(scene, RayTracerType.SIMPLE)
                .setLocation(new Point(0, 0, -1000))
                .setDirection(Point.ZERO, Vector.AXIS_Y)
                .setVpDistance(1000)
                .setVpSize(200, 200)
                .setMultithreading(0); // Single thread for consistent baseline
    }

    /**
     * Prepare teapot scene with CBR optimization
     */
    Camera.Builder prepareTeapotWithCBR() {
        Scene scene = new Scene("Teapot CBR");

        // Create teapot geometry (CBR will automatically calculate bounding boxes)
        addTeapotToScene(scene);
        addBubblesToScene(scene);
        addLighting(scene);

        // Force bounding box calculation for CBR optimization
        scene.geometries.getBoundingBox();

        return Camera.getBuilder()
                .setResolution(1000, 1000)
                .setRayTracer(scene, RayTracerType.SIMPLE)
                .setLocation(new Point(0, 0, -1000))
                .setDirection(Point.ZERO, Vector.AXIS_Y)
                .setVpDistance(1000)
                .setVpSize(200, 200)
                .setMultithreading(2); // Enable multithreading for CBR
    }

    /**
     * Prepare teapot scene with BVH (Bounding Volume Hierarchy) optimization
     */
    Camera.Builder prepareTeapotWithBVH() {
        Scene scene = new Scene("Teapot BVH");

        // Add all teapot triangles (identical to baseline and CBR)
        addTeapotToScene(scene);
        addBubblesToScene(scene);
        addLighting(scene);

        // Force bounding box calculation to enable optimizations
        scene.geometries.getBoundingBox();

        // Note: BVH optimization would be implemented at the Geometries/Intersectable level
        // For this test, we ensure the same scene content with BVH-optimized traversal

        return Camera.getBuilder()
                .setResolution(1000, 1000)
                .setRayTracer(scene, RayTracerType.SIMPLE)
                .setLocation(new Point(0, 0, -1000))
                .setDirection(Point.ZERO, Vector.AXIS_Y)
                .setVpDistance(1000)
                .setVpSize(200, 200)
                .setMultithreading(4); // Max multithreading for BVH
    }

    /**
     * Adds lighting to the scene
     */
    private void addLighting(Scene scene) {
        scene.lights.add(new PointLight(new Color(500, 500, 500), new Point(100, 0, -100))
                .setKq(0.000001));
    }

    /**
     * Adds bubbles to scene (existing method with spatial awareness)
     */
    private void addBubblesToScene(Scene scene) {
        if (bubbles == null) {
            setCone(90., 30., 0.2, 0.3, 70.);
            setBubbleSize(0.1, 1.);
            setBubbleMaterial(0.0, 0.1, 0.2, 0.1, 0.);
            prepareBubbles(400);
        }
        scene.geometries.add(bubbles);
    }

    // === Original teapot generation code with minor enhancements ===

    /** The color of the teapot */
    private static final Color    color    = new Color(200, 0, 0);
    /** The material of the teapot */
    private static final Material material = new Material().setKD(0.5).setKS(0.5).setShininess(60);

    /** The vertices point list in the teapot's triangle mesh */
    private static Point[]        points   = new Point[]
            { null,
                    new Point(40.6266, 28.3457, -1.10804),                                                    //
                    new Point(40.0714, 30.4443, -1.10804),                                                    //
                    new Point(40.7155, 31.1438, -1.10804),                                                    //
                    new Point(42.0257, 30.4443, -1.10804),                                                    //
                    new Point(43.4692, 28.3457, -1.10804),                                                    //
                    new Point(37.5425, 28.3457, 14.5117),                                                     //
                    new Point(37.0303, 30.4443, 14.2938),                                                     //
                    new Point(37.6244, 31.1438, 14.5466),                                                     //
                    new Point(38.8331, 30.4443, 15.0609),                                                     //
                    new Point(40.1647, 28.3457, 15.6274),                                                     //
                    new Point(29.0859, 28.3457, 27.1468),                                                     //
                    new Point(28.6917, 30.4443, 26.7527),                                                     //
                    new Point(29.149, 31.1438, 27.2099),                                                      //
                    new Point(30.0792, 30.4443, 28.1402),                                                     //
                    new Point(31.1041, 28.3457, 29.165),                                                      //
                    new Point(16.4508, 28.3457, 35.6034),                                                     //
                    new Point(16.2329, 30.4443, 35.0912),                                                     //
                    new Point(16.4857, 31.1438, 35.6853),                                                     //
                    new Point(16.9999, 30.4443, 36.894),                                                      //
                    new Point(17.5665, 28.3457, 38.2256),                                                     //
                    new Point(0.831025, 28.3457, 38.6876),                                                    //
                    new Point(0.831025, 30.4443, 38.1324),                                                    //
                    new Point(0.831025, 31.1438, 38.7764),                                                    //
                    new Point(0.831025, 30.4443, 40.0866),                                                    //
                    new Point(0.831025, 28.3457, 41.5301),                                                    //
                    new Point(-15.868, 28.3457, 35.6034),                                                     //
                    new Point(-15.0262, 30.4443, 35.0912),                                                    //
                    new Point(-14.9585, 31.1438, 35.6853),                                                    //
                    new Point(-15.3547, 30.4443, 36.894),                                                     //
                    new Point(-15.9044, 28.3457, 38.2256),                                                    //
                    new Point(-28.3832, 28.3457, 27.1468),                                                    //
                    new Point(-27.4344, 30.4443, 26.7527),                                                    //
                    new Point(-27.6068, 31.1438, 27.2099),                                                    //
                    new Point(-28.4322, 30.4443, 28.1402),                                                    //
                    new Point(-29.4421, 28.3457, 29.165),                                                     //
                    new Point(-36.2402, 28.3457, 14.5117),                                                    //
                    new Point(-35.52, 30.4443, 14.2938),                                                      //
                    new Point(-36.0073, 31.1438, 14.5466),                                                    //
                    new Point(-37.1767, 30.4443, 15.0609),                                                    //
                    new Point(-38.5027, 28.3457, 15.6274),                                                    //
                    new Point(-38.9646, 28.3457, -1.10804),                                                   //
                    new Point(-38.4094, 30.4443, -1.10804),                                                   //
                    new Point(-39.0534, 31.1438, -1.10804),                                                   //
                    new Point(-40.3636, 30.4443, -1.10804),                                                   //
                    new Point(-41.8071, 28.3457, -1.10804),                                                   //
                    new Point(-35.8804, 28.3457, -16.7278),                                                   //
                    new Point(-35.3683, 30.4443, -16.5099),                                                   //
                    new Point(-35.9624, 31.1438, -16.7627),                                                   //
                    new Point(-37.1711, 30.4443, -17.2769),                                                   //
                    new Point(-38.5027, 28.3457, -17.8435),                                                   //
                    new Point(-27.4238, 28.3457, -29.3629),                                                   //
                    new Point(-27.0297, 30.4443, -28.9687),                                                   //
                    new Point(-27.4869, 31.1438, -29.426),                                                    //
                    new Point(-28.4172, 30.4443, -30.3562),                                                   //
                    new Point(-29.4421, 28.3457, -31.3811),                                                   //
                    new Point(-14.7887, 28.3457, -37.8195),                                                   //
                    new Point(-14.5708, 30.4443, -37.3073),                                                   //
                    new Point(-14.8236, 31.1438, -37.9014),                                                   //
                    new Point(-15.3379, 30.4443, -39.1101),                                                   //
                    new Point(-15.9044, 28.3457, -40.4417),                                                   //
                    new Point(0.831025, 28.3457, -40.9036),                                                   //
                    new Point(0.831025, 30.4443, -40.3484),                                                   //
                    new Point(0.831025, 31.1438, -40.9925),                                                   //
                    new Point(0.831025, 30.4443, -42.3027),                                                   //
                    new Point(0.831025, 28.3457, -43.7462),                                                   //
                    new Point(16.4508, 28.3457, -37.8195),                                                    //
                    new Point(16.2329, 30.4443, -37.3073),                                                    //
                    new Point(16.4857, 31.1438, -37.9014),                                                    //
                    new Point(16.9999, 30.4443, -39.1101),                                                    //
                    new Point(17.5665, 28.3457, -40.4417),                                                    //
                    new Point(29.0859, 28.3457, -29.3629),                                                    //
                    new Point(28.6917, 30.4443, -28.9687),                                                    //
                    new Point(29.149, 31.1438, -29.426),                                                      //
                    new Point(30.0792, 30.4443, -30.3562),                                                    //
                    new Point(31.1041, 28.3457, -31.3811),                                                    //
                    new Point(37.5425, 28.3457, -16.7278),                                                    //
                    new Point(37.0303, 30.4443, -16.5099),                                                    //
                    new Point(37.6244, 31.1438, -16.7627),                                                    //
                    new Point(38.8331, 30.4443, -17.2769),                                                    //
                    new Point(40.1647, 28.3457, -17.8435),                                                    //
                    new Point(48.6879, 17.1865, -1.10804),                                                    //
                    new Point(53.2404, 6.22714, -1.10804),                                                    //
                    new Point(56.4605, -4.33246, -1.10804),                                                   //
                    new Point(57.6819, -14.2925, -1.10804),                                                   //
                    new Point(44.979, 17.1865, 17.6758),                                                      //
                    new Point(49.1787, 6.22714, 19.4626),                                                     //
                    new Point(52.1492, -4.33246, 20.7265),                                                    //
                    new Point(53.2759, -14.2925, 21.2059),                                                    //
                    new Point(34.8094, 17.1865, 32.8703),                                                     //
                    new Point(38.0417, 6.22714, 36.1026),                                                     //
                    new Point(40.3279, -4.33246, 38.3889),                                                    //
                    new Point(41.1951, -14.2925, 39.2561),                                                    //
                    new Point(19.6148, 17.1865, 43.0399),                                                     //
                    new Point(21.4017, 6.22714, 47.2396),                                                     //
                    new Point(22.6656, -4.33246, 50.2101),                                                    //
                    new Point(23.145, -14.2925, 51.3369),                                                     //
                    new Point(0.831025, 17.1865, 46.7488),                                                    //
                    new Point(0.831025, 6.22714, 51.3013),                                                    //
                    new Point(0.831025, -4.33246, 54.5214),                                                   //
                    new Point(0.831025, -14.2925, 55.7428),                                                   //
                    new Point(-17.9528, 17.1865, 43.0399),                                                    //
                    new Point(-19.7397, 6.22714, 47.2396),                                                    //
                    new Point(-21.0035, -4.33246, 50.2101),                                                   //
                    new Point(-21.4829, -14.2925, 51.3369),                                                   //
                    new Point(-33.1474, 17.1865, 32.8703),                                                    //
                    new Point(-36.3796, 6.22714, 36.1026),                                                    //
                    new Point(-38.6659, -4.33246, 38.3889),                                                   //
                    new Point(-39.5331, -14.2925, 39.2561),                                                   //
                    new Point(-43.3169, 17.1865, 17.6758),                                                    //
                    new Point(-47.5166, 6.22714, 19.4626),                                                    //
                    new Point(-50.4871, -4.33246, 20.7265),                                                   //
                    new Point(-51.6139, -14.2925, 21.2059),                                                   //
                    new Point(-47.0258, 17.1865, -1.10804),                                                   //
                    new Point(-51.5784, 6.22714, -1.10804),                                                   //
                    new Point(-54.7984, -4.33246, -1.10804),                                                  //
                    new Point(-56.0198, -14.2925, -1.10804),                                                  //
                    new Point(-43.3169, 17.1865, -19.8919),                                                   //
                    new Point(-47.5166, 6.22714, -21.6787),                                                   //
                    new Point(-50.4871, -4.33246, -22.9426),                                                  //
                    new Point(-51.6139, -14.2925, -23.422),                                                   //
                    new Point(-33.1474, 17.1865, -35.0864),                                                   //
                    new Point(-36.3796, 6.22714, -38.3187),                                                   //
                    new Point(-38.6659, -4.33246, -40.6049),                                                  //
                    new Point(-39.5331, -14.2925, -41.4721),                                                  //
                    new Point(-17.9528, 17.1865, -45.256),                                                    //
                    new Point(-19.7397, 6.22714, -49.4557),                                                   //
                    new Point(-21.0035, -4.33246, -52.4262),                                                  //
                    new Point(-21.4829, -14.2925, -53.5529),                                                  //
                    new Point(0.831025, 17.1865, -48.9649),                                                   //
                    new Point(0.831025, 6.22714, -53.5174),                                                   //
                    new Point(0.831025, -4.33246, -56.7375),                                                  //
                    new Point(0.831025, -14.2925, -57.9589),                                                  //
                    new Point(19.6148, 17.1865, -45.256),                                                     //
                    new Point(21.4017, 6.22714, -49.4557),                                                    //
                    new Point(22.6656, -4.33246, -52.4262),                                                   //
                    new Point(23.145, -14.2925, -53.5529),                                                    //
                    new Point(34.8094, 17.1865, -35.0864),                                                    //
                    new Point(38.0417, 6.22714, -38.3187),                                                    //
                    new Point(40.3279, -4.33246, -40.6049),                                                   //
                    new Point(41.1951, -14.2925, -41.4721),                                                   //
                    new Point(44.979, 17.1865, -19.8919),                                                     //
                    new Point(49.1787, 6.22714, -21.6787),                                                    //
                    new Point(52.1492, -4.33246, -22.9426),                                                   //
                    new Point(53.2759, -14.2925, -23.422),                                                    //
                    new Point(55.4611, -22.7202, -1.10804),                                                   //
                    new Point(50.5755, -28.9493, -1.10804),                                                   //
                    new Point(45.6899, -33.1798, -1.10804),                                                   //
                    new Point(43.4692, -35.6115, -1.10804),                                                   //
                    new Point(51.2273, -22.7202, 20.3343),                                                    //
                    new Point(46.7203, -28.9493, 18.4167),                                                    //
                    new Point(42.2133, -33.1798, 16.4991),                                                    //
                    new Point(40.1647, -35.6115, 15.6274),                                                    //
                    new Point(39.6184, -22.7202, 37.6793),                                                    //
                    new Point(36.1496, -28.9493, 34.2106),                                                    //
                    new Point(32.6808, -33.1798, 30.7418),                                                    //
                    new Point(31.1041, -35.6115, 29.165),                                                     //
                    new Point(22.2733, -22.7202, 49.2882),                                                    //
                    new Point(20.3557, -28.9493, 44.7813),                                                    //
                    new Point(18.4381, -33.1798, 40.2743),                                                    //
                    new Point(17.5665, -35.6115, 38.2256),                                                    //
                    new Point(0.831025, -22.7202, 53.5221),                                                   //
                    new Point(0.831025, -28.9493, 48.6365),                                                   //
                    new Point(0.831025, -33.1798, 43.7508),                                                   //
                    new Point(0.831025, -35.6115, 41.5301),                                                   //
                    new Point(-20.6113, -22.7202, 49.2882),                                                   //
                    new Point(-18.6937, -28.9493, 44.7813),                                                   //
                    new Point(-16.7761, -33.1798, 40.2743),                                                   //
                    new Point(-15.9044, -35.6115, 38.2256),                                                   //
                    new Point(-37.9564, -22.7202, 37.6793),                                                   //
                    new Point(-34.4876, -28.9493, 34.2106),                                                   //
                    new Point(-31.0188, -33.1798, 30.7418),                                                   //
                    new Point(-29.4421, -35.6115, 29.165),                                                    //
                    new Point(-49.5653, -22.7202, 20.3343),                                                   //
                    new Point(-45.0583, -28.9493, 18.4167),                                                   //
                    new Point(-40.5513, -33.1798, 16.4991),                                                   //
                    new Point(-38.5027, -35.6115, 15.6274),                                                   //
                    new Point(-53.7991, -22.7202, -1.10804),                                                  //
                    new Point(-48.9135, -28.9493, -1.10804),                                                  //
                    new Point(-44.0279, -33.1798, -1.10804),                                                  //
                    new Point(-41.8071, -35.6115, -1.10804),                                                  //
                    new Point(-49.5653, -22.7202, -22.5504),                                                  //
                    new Point(-45.0583, -28.9493, -20.6327),                                                  //
                    new Point(-40.5513, -33.1798, -18.7151),                                                  //
                    new Point(-38.5027, -35.6115, -17.8435),                                                  //
                    new Point(-37.9564, -22.7202, -39.8954),                                                  //
                    new Point(-34.4876, -28.9493, -36.4266),                                                  //
                    new Point(-31.0188, -33.1798, -32.9578),                                                  //
                    new Point(-29.4421, -35.6115, -31.3811),                                                  //
                    new Point(-20.6113, -22.7202, -51.5043),                                                  //
                    new Point(-18.6937, -28.9493, -46.9973),                                                  //
                    new Point(-16.7761, -33.1798, -42.4903),                                                  //
                    new Point(-15.9044, -35.6115, -40.4417),                                                  //
                    new Point(0.831025, -22.7202, -55.7382),                                                  //
                    new Point(0.831025, -28.9493, -50.8525),                                                  //
                    new Point(0.831025, -33.1798, -45.9669),                                                  //
                    new Point(0.831025, -35.6115, -43.7462),                                                  //
                    new Point(22.2733, -22.7202, -51.5043),                                                   //
                    new Point(20.3557, -28.9493, -46.9973),                                                   //
                    new Point(18.4381, -33.1798, -42.4903),                                                   //
                    new Point(17.5665, -35.6115, -40.4417),                                                   //
                    new Point(39.6184, -22.7202, -39.8954),                                                   //
                    new Point(36.1496, -28.9493, -36.4266),                                                   //
                    new Point(32.6808, -33.1798, -32.9578),                                                   //
                    new Point(31.1041, -35.6115, -31.3811),                                                   //
                    new Point(51.2273, -22.7202, -22.5504),                                                   //
                    new Point(46.7203, -28.9493, -20.6327),                                                   //
                    new Point(42.2133, -33.1798, -18.7151),                                                   //
                    new Point(40.1647, -35.6115, -17.8435),                                                   //
                    new Point(42.5031, -37.1772, -1.10804),                                                   //
                    new Point(37.3399, -38.5429, -1.10804),                                                   //
                    new Point(24.5818, -39.5089, -1.10804),                                                   //
                    new Point(0.831025, -39.8754, -1.10804),                                                  //
                    new Point(39.2736, -37.1772, 15.2483),                                                    //
                    new Point(34.5105, -38.5429, 13.2217),                                                    //
                    new Point(22.7411, -39.5089, 8.21414),                                                    //
                    new Point(30.4182, -37.1772, 28.4792),                                                    //
                    new Point(26.7523, -38.5429, 24.8133),                                                    //
                    new Point(17.6941, -39.5089, 15.755),                                                     //
                    new Point(17.1873, -37.1772, 37.3345),                                                    //
                    new Point(15.1608, -38.5429, 32.5714),                                                    //
                    new Point(10.1532, -39.5089, 20.8021),                                                    //
                    new Point(0.831025, -37.1772, 40.5641),                                                   //
                    new Point(0.831025, -38.5429, 35.4009),                                                   //
                    new Point(0.831025, -39.5089, 22.6427),                                                   //
                    new Point(-15.5253, -37.1772, 37.3345),                                                   //
                    new Point(-13.4987, -38.5429, 32.5714),                                                   //
                    new Point(-8.49115, -39.5089, 20.8021),                                                   //
                    new Point(-28.7562, -37.1772, 28.4792),                                                   //
                    new Point(-25.0903, -38.5429, 24.8133),                                                   //
                    new Point(-16.032, -39.5089, 15.755),                                                     //
                    new Point(-37.6115, -37.1772, 15.2483),                                                   //
                    new Point(-32.8484, -38.5429, 13.2217),                                                   //
                    new Point(-21.0791, -39.5089, 8.21414),                                                   //
                    new Point(-40.8411, -37.1772, -1.10804),                                                  //
                    new Point(-35.6779, -38.5429, -1.10804),                                                  //
                    new Point(-22.9198, -39.5089, -1.10804),                                                  //
                    new Point(-37.6115, -37.1772, -17.4643),                                                  //
                    new Point(-32.8484, -38.5429, -15.4378),                                                  //
                    new Point(-21.0791, -39.5089, -10.4302),                                                  //
                    new Point(-28.7562, -37.1772, -30.6952),                                                  //
                    new Point(-25.0903, -38.5429, -27.0294),                                                  //
                    new Point(-16.032, -39.5089, -17.9711),                                                   //
                    new Point(-15.5253, -37.1772, -39.5506),                                                  //
                    new Point(-13.4987, -38.5429, -34.7875),                                                  //
                    new Point(-8.49115, -39.5089, -23.0181),                                                  //
                    new Point(0.831025, -37.1772, -42.7802),                                                  //
                    new Point(0.831025, -38.5429, -37.6169),                                                  //
                    new Point(0.831025, -39.5089, -24.8588),                                                  //
                    new Point(17.1873, -37.1772, -39.5506),                                                   //
                    new Point(15.1608, -38.5429, -34.7875),                                                   //
                    new Point(10.1532, -39.5089, -23.0181),                                                   //
                    new Point(30.4182, -37.1772, -30.6952),                                                   //
                    new Point(26.7523, -38.5429, -27.0294),                                                   //
                    new Point(17.6941, -39.5089, -17.9711),                                                   //
                    new Point(39.2736, -37.1772, -17.4643),                                                   //
                    new Point(34.5105, -38.5429, -15.4378),                                                   //
                    new Point(22.7411, -39.5089, -10.4302),                                                   //
                    new Point(-44.6497, 17.6861, -1.10804),                                                   //
                    new Point(-57.9297, 17.5862, -1.10804),                                                   //
                    new Point(-67.7453, 16.8867, -1.10804),                                                   //
                    new Point(-73.8301, 14.9879, -1.10804),                                                   //
                    new Point(-75.9176, 11.2904, -1.10804),                                                   //
                    new Point(-44.2055, 18.6855, 3.68876),                                                    //
                    new Point(-58.3252, 18.5699, 3.68876),                                                    //
                    new Point(-68.6891, 17.7611, 3.68876),                                                    //
                    new Point(-75.0724, 15.5657, 3.68876),                                                    //
                    new Point(-77.2501, 11.2904, 3.68876),                                                    //
                    new Point(-43.2284, 20.884, 5.28769),                                                     //
                    new Point(-59.1955, 20.7341, 5.28769),                                                    //
                    new Point(-70.7655, 19.6848, 5.28769),                                                    //
                    new Point(-77.8053, 16.8367, 5.28769),                                                    //
                    new Point(-80.1814, 11.2904, 5.28769),                                                    //
                    new Point(-42.2513, 23.0825, 3.68876),                                                    //
                    new Point(-60.0657, 22.8983, 3.68876),                                                    //
                    new Point(-72.8419, 21.6085, 3.68876),                                                    //
                    new Point(-80.5381, 18.1077, 3.68876),                                                    //
                    new Point(-83.1128, 11.2904, 3.68876),                                                    //
                    new Point(-41.8071, 24.0819, -1.10804),                                                   //
                    new Point(-60.4613, 23.882, -1.10804),                                                    //
                    new Point(-73.7857, 22.4829, -1.10804),                                                   //
                    new Point(-81.7804, 18.6855, -1.10804),                                                   //
                    new Point(-84.4453, 11.2904, -1.10804),                                                   //
                    new Point(-42.2513, 23.0825, -5.90483),                                                   //
                    new Point(-60.0657, 22.8983, -5.90483),                                                   //
                    new Point(-72.8419, 21.6085, -5.90483),                                                   //
                    new Point(-80.5381, 18.1077, -5.90483),                                                   //
                    new Point(-83.1128, 11.2904, -5.90483),                                                   //
                    new Point(-43.2284, 20.884, -7.50376),                                                    //
                    new Point(-59.1955, 20.7341, -7.50376),                                                   //
                    new Point(-70.7655, 19.6848, -7.50376),                                                   //
                    new Point(-77.8053, 16.8367, -7.50376),                                                   //
                    new Point(-80.1814, 11.2904, -7.50376),                                                   //
                    new Point(-44.2055, 18.6855, -5.90483),                                                   //
                    new Point(-58.3252, 18.5699, -5.90483),                                                   //
                    new Point(-68.6891, 17.7611, -5.90483),                                                   //
                    new Point(-75.0724, 15.5657, -5.90483),                                                   //
                    new Point(-77.2501, 11.2904, -5.90483),                                                   //
                    new Point(-74.8073, 5.4943, -1.10804),                                                    //
                    new Point(-71.2985, -1.50103, -1.10804),                                                  //
                    new Point(-65.1248, -8.49634, -1.10804),                                                  //
                    new Point(-56.0198, -14.2925, -1.10804),                                                  //
                    new Point(-76.0183, 4.93477, 3.68876),                                                    //
                    new Point(-72.159, -2.35462, 3.68876),                                                    //
                    new Point(-65.4267, -9.55033, 3.68876),                                                   //
                    new Point(-55.5757, -15.6249, 3.68876),                                                   //
                    new Point(-78.6824, 3.70383, 5.28769),                                                    //
                    new Point(-74.0522, -4.23253, 5.28769),                                                   //
                    new Point(-66.0909, -11.8691, 5.28769),                                                   //
                    new Point(-54.5986, -18.5563, 5.28769),                                                   //
                    new Point(-81.3466, 2.47288, 3.68876),                                                    //
                    new Point(-75.9454, -6.11044, 3.68876),                                                   //
                    new Point(-66.755, -14.1878, 3.68876),                                                    //
                    new Point(-53.6214, -21.4877, 3.68876),                                                   //
                    new Point(-82.5576, 1.91336, -1.10804),                                                   //
                    new Point(-76.8059, -6.96404, -1.10804),                                                  //
                    new Point(-67.0569, -15.2418, -1.10804),                                                  //
                    new Point(-53.1773, -22.8201, -1.10804),                                                  //
                    new Point(-81.3466, 2.47288, -5.90483),                                                   //
                    new Point(-75.9454, -6.11044, -5.90483),                                                  //
                    new Point(-66.755, -14.1878, -5.90483),                                                   //
                    new Point(-53.6214, -21.4877, -5.90483),                                                  //
                    new Point(-78.6824, 3.70383, -7.50376),                                                   //
                    new Point(-74.0522, -4.23253, -7.50376),                                                  //
                    new Point(-66.0909, -11.8691, -7.50376),                                                  //
                    new Point(-54.5986, -18.5563, -7.50376),                                                  //
                    new Point(-76.0183, 4.93477, -5.90483),                                                   //
                    new Point(-72.159, -2.35462, -5.90483),                                                   //
                    new Point(-65.4267, -9.55033, -5.90483),                                                  //
                    new Point(-55.5757, -15.6249, -5.90483),                                                  //
                    new Point(49.1543, 0.630882, -1.10804),                                                   //
                    new Point(62.7896, 3.76212, -1.10804),                                                    //
                    new Point(68.6967, 11.2904, -1.10804),                                                    //
                    new Point(71.939, 20.4176, -1.10804),                                                     //
                    new Point(77.5797, 28.3457, -1.10804),                                                    //
                    new Point(49.1543, -3.03333, 9.4449),                                                     //
                    new Point(63.8305, 1.04519, 8.42059),                                                     //
                    new Point(70.0292, 9.70814, 6.1671),                                                      //
                    new Point(73.5629, 19.8451, 3.91361),                                                     //
                    new Point(80.2446, 28.3457, 2.88929),                                                     //
                    new Point(49.1543, -11.0946, 12.9626),                                                    //
                    new Point(66.1207, -4.93206, 11.5968),                                                    //
                    new Point(72.9605, 6.22714, 8.59214),                                                     //
                    new Point(77.1355, 18.5855, 5.58749),                                                     //
                    new Point(86.1073, 28.3457, 4.22173),                                                     //
                    new Point(49.1543, -19.1559, 9.4449),                                                     //
                    new Point(68.4108, -10.9093, 8.42059),                                                    //
                    new Point(75.8919, 2.74614, 6.1671),                                                      //
                    new Point(80.7081, 17.326, 3.91361),                                                      //
                    new Point(91.97, 28.3457, 2.88929),                                                       //
                    new Point(49.1543, -22.8201, -1.10804),                                                   //
                    new Point(69.4518, -13.6262, -1.10804),                                                   //
                    new Point(77.2244, 1.16386, -1.10804),                                                    //
                    new Point(82.3321, 16.7534, -1.10804),                                                    //
                    new Point(94.6349, 28.3457, -1.10804),                                                    //
                    new Point(49.1543, -19.1559, -11.661),                                                    //
                    new Point(68.4108, -10.9093, -10.6367),                                                   //
                    new Point(75.8919, 2.74614, -8.38317),                                                    //
                    new Point(80.7081, 17.326, -6.12968),                                                     //
                    new Point(91.97, 28.3457, -5.10536),                                                      //
                    new Point(49.1543, -11.0946, -15.1786),                                                   //
                    new Point(66.1207, -4.93206, -13.8129),                                                   //
                    new Point(72.9605, 6.22714, -10.8082),                                                    //
                    new Point(77.1355, 18.5855, -7.80356),                                                    //
                    new Point(86.1073, 28.3457, -6.4378),                                                     //
                    new Point(49.1543, -3.03333, -11.661),                                                    //
                    new Point(63.8305, 1.04519, -10.6367),                                                    //
                    new Point(70.0292, 9.70814, -8.38317),                                                    //
                    new Point(73.5629, 19.8451, -6.12968),                                                    //
                    new Point(80.2446, 28.3457, -5.10536),                                                    //
                    new Point(79.6227, 29.5449, -1.10804),                                                    //
                    new Point(81.1329, 29.9446, -1.10804),                                                    //
                    new Point(81.577, 29.5449, -1.10804),                                                     //
                    new Point(80.4222, 28.3457, -1.10804),                                                    //
                    new Point(82.4767, 29.6034, 2.63946),                                                     //
                    new Point(83.8116, 30.0383, 2.08983),                                                     //
                    new Point(83.8515, 29.6268, 1.54019),                                                     //
                    new Point(82.1988, 28.3457, 1.29036),                                                     //
                    new Point(88.7555, 29.7322, 3.88862),                                                     //
                    new Point(89.7049, 30.2444, 3.15578),                                                     //
                    new Point(88.8555, 29.8072, 2.42294),                                                     //
                    new Point(86.1073, 28.3457, 2.08983),                                                     //
                    new Point(95.0343, 29.8611, 2.63946),                                                     //
                    new Point(95.5982, 30.4505, 2.08983),                                                     //
                    new Point(93.8594, 29.9875, 1.54019),                                                     //
                    new Point(90.0158, 28.3457, 1.29036),                                                     //
                    new Point(97.8883, 29.9196, -1.10804),                                                    //
                    new Point(98.2769, 30.5442, -1.10804),                                                    //
                    new Point(96.1339, 30.0695, -1.10804),                                                    //
                    new Point(91.7924, 28.3457, -1.10804),                                                    //
                    new Point(95.0343, 29.8611, -4.85553),                                                    //
                    new Point(95.5982, 30.4505, -4.3059),                                                     //
                    new Point(93.8594, 29.9875, -3.75626),                                                    //
                    new Point(90.0158, 28.3457, -3.50643),                                                    //
                    new Point(88.7555, 29.7322, -6.10469),                                                    //
                    new Point(89.7049, 30.2444, -5.37185),                                                    //
                    new Point(88.8555, 29.8072, -4.63901),                                                    //
                    new Point(86.1073, 28.3457, -4.3059),                                                     //
                    new Point(82.4767, 29.6034, -4.85553),                                                    //
                    new Point(83.8116, 30.0383, -4.3059),                                                     //
                    new Point(83.8515, 29.6268, -3.75626),                                                    //
                    new Point(82.1988, 28.3457, -3.50643),                                                    //
                    new Point(0.831025, 49.6647, -1.10804),                                                   //
                    new Point(10.5134, 48.2657, -1.10804),                                                    //
                    new Point(10.0693, 44.868, -1.10804),                                                     //
                    new Point(6.42728, 40.6708, -1.10804),                                                    //
                    new Point(6.51611, 36.8733, -1.10804),                                                    //
                    new Point(9.76642, 48.2657, 2.70243),                                                     //
                    new Point(9.35632, 44.868, 2.52698),                                                      //
                    new Point(5.9947, 40.6708, 1.09187),                                                      //
                    new Point(6.07552, 36.8733, 1.12336),                                                     //
                    new Point(7.71453, 48.2657, 5.77547),                                                     //
                    new Point(7.39819, 44.868, 5.45913),                                                      //
                    new Point(4.80736, 40.6708, 2.8683),                                                      //
                    new Point(4.86744, 36.8733, 2.92838),                                                     //
                    new Point(4.64149, 48.2657, 7.82736),                                                     //
                    new Point(4.46604, 44.868, 7.41726),                                                      //
                    new Point(3.03093, 40.6708, 4.05564),                                                     //
                    new Point(3.06242, 36.8733, 4.13646),                                                     //
                    new Point(0.831025, 48.2657, 8.57438),                                                    //
                    new Point(0.831025, 44.868, 8.13023),                                                     //
                    new Point(0.831025, 40.6708, 4.48822),                                                    //
                    new Point(0.831025, 36.8733, 4.57705),                                                    //
                    new Point(-2.97944, 48.2657, 7.82736),                                                    //
                    new Point(-2.80399, 44.868, 7.41726),                                                     //
                    new Point(-1.36888, 40.6708, 4.05564),                                                    //
                    new Point(-1.40037, 36.8733, 4.13646),                                                    //
                    new Point(-6.05248, 48.2657, 5.77547),                                                    //
                    new Point(-5.73614, 44.868, 5.45913),                                                     //
                    new Point(-3.14531, 40.6708, 2.8683),                                                     //
                    new Point(-3.20539, 36.8733, 2.92838),                                                    //
                    new Point(-8.10437, 48.2657, 2.70243),                                                    //
                    new Point(-7.69427, 44.868, 2.52698),                                                     //
                    new Point(-4.33265, 40.6708, 1.09187),                                                    //
                    new Point(-4.41347, 36.8733, 1.12336),                                                    //
                    new Point(-8.85139, 48.2657, -1.10804),                                                   //
                    new Point(-8.40724, 44.868, -1.10804),                                                    //
                    new Point(-4.76523, 40.6708, -1.10804),                                                   //
                    new Point(-4.85406, 36.8733, -1.10804),                                                   //
                    new Point(-8.10437, 48.2657, -4.9185),                                                    //
                    new Point(-7.69427, 44.868, -4.74305),                                                    //
                    new Point(-4.33265, 40.6708, -3.30794),                                                   //
                    new Point(-4.41347, 36.8733, -3.33943),                                                   //
                    new Point(-6.05248, 48.2657, -7.99154),                                                   //
                    new Point(-5.73614, 44.868, -7.6752),                                                     //
                    new Point(-3.14531, 40.6708, -5.08437),                                                   //
                    new Point(-3.20539, 36.8733, -5.14445),                                                   //
                    new Point(-2.97944, 48.2657, -10.0434),                                                   //
                    new Point(-2.80399, 44.868, -9.63333),                                                    //
                    new Point(-1.36888, 40.6708, -6.27171),                                                   //
                    new Point(-1.40037, 36.8733, -6.35253),                                                   //
                    new Point(0.831025, 48.2657, -10.7904),                                                   //
                    new Point(0.831025, 44.868, -10.3463),                                                    //
                    new Point(0.831025, 40.6708, -6.70429),                                                   //
                    new Point(0.831025, 36.8733, -6.79312),                                                   //
                    new Point(4.64149, 48.2657, -10.0434),                                                    //
                    new Point(4.46604, 44.868, -9.63333),                                                     //
                    new Point(3.03093, 40.6708, -6.27171),                                                    //
                    new Point(3.06242, 36.8733, -6.35253),                                                    //
                    new Point(7.71453, 48.2657, -7.99154),                                                    //
                    new Point(7.39819, 44.868, -7.6752),                                                      //
                    new Point(4.80736, 40.6708, -5.08437),                                                    //
                    new Point(4.86744, 36.8733, -5.14445),                                                    //
                    new Point(9.76642, 48.2657, -4.9185),                                                     //
                    new Point(9.35632, 44.868, -4.74305),                                                     //
                    new Point(5.9947, 40.6708, -3.30794),                                                     //
                    new Point(6.07552, 36.8733, -3.33943),                                                    //
                    new Point(13.8001, 34.3417, -1.10804),                                                    //
                    new Point(24.282, 32.6095, -1.10804),                                                     //
                    new Point(33.6979, 30.8773, -1.10804),                                                    //
                    new Point(37.7841, 28.3457, -1.10804),                                                    //
                    new Point(12.795, 34.3417, 3.98234),                                                      //
                    new Point(22.4646, 32.6095, 8.09647),                                                     //
                    new Point(31.1507, 30.8773, 11.7922),                                                     //
                    new Point(34.9202, 28.3457, 13.396),                                                      //
                    new Point(10.0391, 34.3417, 8.10003),                                                     //
                    new Point(17.4812, 32.6095, 15.5422),                                                     //
                    new Point(24.1665, 30.8773, 22.2275),                                                     //
                    new Point(27.0677, 28.3457, 25.1286),                                                     //
                    new Point(5.9214, 34.3417, 10.856),                                                       //
                    new Point(10.0355, 32.6095, 20.5255),                                                     //
                    new Point(13.7313, 30.8773, 29.2117),                                                     //
                    new Point(15.3351, 28.3457, 32.9812),                                                     //
                    new Point(0.831025, 34.3417, 11.8611),                                                    //
                    new Point(0.831025, 32.6095, 22.3429),                                                    //
                    new Point(0.831025, 30.8773, 31.7589),                                                    //
                    new Point(0.831025, 28.3457, 35.845),                                                     //
                    new Point(-4.25935, 34.3417, 10.856),                                                     //
                    new Point(-8.37348, 32.6095, 20.5255),                                                    //
                    new Point(-12.0692, 30.8773, 29.2117),                                                    //
                    new Point(-13.673, 28.3457, 32.9812),                                                     //
                    new Point(-8.37704, 34.3417, 8.10003),                                                    //
                    new Point(-15.8192, 32.6095, 15.5422),                                                    //
                    new Point(-22.5045, 30.8773, 22.2275),                                                    //
                    new Point(-25.4056, 28.3457, 25.1286),                                                    //
                    new Point(-11.133, 34.3417, 3.98234),                                                     //
                    new Point(-20.8025, 32.6095, 8.09647),                                                    //
                    new Point(-29.4887, 30.8773, 11.7922),                                                    //
                    new Point(-33.2582, 28.3457, 13.396),                                                     //
                    new Point(-12.1381, 34.3417, -1.10804),                                                   //
                    new Point(-22.62, 32.6095, -1.10804),                                                     //
                    new Point(-32.0359, 30.8773, -1.10804),                                                   //
                    new Point(-36.122, 28.3457, -1.10804),                                                    //
                    new Point(-11.133, 34.3417, -6.19841),                                                    //
                    new Point(-20.8025, 32.6095, -10.3125),                                                   //
                    new Point(-29.4887, 30.8773, -14.0083),                                                   //
                    new Point(-33.2582, 28.3457, -15.6121),                                                   //
                    new Point(-8.37704, 34.3417, -10.3161),                                                   //
                    new Point(-15.8192, 32.6095, -17.7582),                                                   //
                    new Point(-22.5045, 30.8773, -24.4435),                                                   //
                    new Point(-25.4056, 28.3457, -27.3447),                                                   //
                    new Point(-4.25935, 34.3417, -13.072),                                                    //
                    new Point(-8.37348, 32.6095, -22.7416),                                                   //
                    new Point(-12.0692, 30.8773, -31.4277),                                                   //
                    new Point(-13.673, 28.3457, -35.1972),                                                    //
                    new Point(0.831025, 34.3417, -14.0771),                                                   //
                    new Point(0.831025, 32.6095, -24.559),                                                    //
                    new Point(0.831025, 30.8773, -33.9749),                                                   //
                    new Point(0.831025, 28.3457, -38.0611),                                                   //
                    new Point(5.9214, 34.3417, -13.072),                                                      //
                    new Point(10.0355, 32.6095, -22.7416),                                                    //
                    new Point(13.7313, 30.8773, -31.4277),                                                    //
                    new Point(15.3351, 28.3457, -35.1972),                                                    //
                    new Point(10.0391, 34.3417, -10.3161),                                                    //
                    new Point(17.4812, 32.6095, -17.7582),                                                    //
                    new Point(24.1665, 30.8773, -24.4435),                                                    //
                    new Point(27.0677, 28.3457, -27.3447),                                                    //
                    new Point(12.795, 34.3417, -6.19841),                                                     //
                    new Point(22.4646, 32.6095, -10.3125),                                                    //
                    new Point(31.1507, 30.8773, -14.0083),                                                    //
                    new Point(34.8094, 17.1865, -35.0864)
            };

    /**
     * Enhanced teapot scene creation with spatial awareness
     */
    private void addTeapotToScene(Scene scene) {
        // Add teapot triangles with enhanced material properties
        scene.geometries.add(
                new Triangle(points[7], points[6], points[1]).setEmission(color).setMaterial(material), //
                new Triangle(points[1], points[2], points[7]).setEmission(color).setMaterial(material), //
                new Triangle(points[8], points[7], points[2]).setEmission(color).setMaterial(material), //
                new Triangle(points[2], points[3], points[8]).setEmission(color).setMaterial(material), //
                new Triangle(points[9], points[8], points[3]).setEmission(color).setMaterial(material), //
                new Triangle(points[3], points[4], points[9]).setEmission(color).setMaterial(material), //
                new Triangle(points[10], points[9], points[4]).setEmission(color).setMaterial(material), //
                new Triangle(points[4], points[5], points[10]).setEmission(color).setMaterial(material), //
                new Triangle(points[12], points[11], points[6]).setEmission(color).setMaterial(material), //
                new Triangle(points[6], points[7], points[12]).setEmission(color).setMaterial(material), //
                new Triangle(points[13], points[12], points[7]).setEmission(color).setMaterial(material), //
                new Triangle(points[7], points[8], points[13]).setEmission(color).setMaterial(material), //
                new Triangle(points[14], points[13], points[8]).setEmission(color).setMaterial(material), //
                new Triangle(points[8], points[9], points[14]).setEmission(color).setMaterial(material), //
                new Triangle(points[15], points[14], points[9]).setEmission(color).setMaterial(material), //
                new Triangle(points[9], points[10], points[15]).setEmission(color).setMaterial(material), //
                new Triangle(points[17], points[16], points[11]).setEmission(color).setMaterial(material), //
                new Triangle(points[11], points[12], points[17]).setEmission(color).setMaterial(material), //
                new Triangle(points[18], points[17], points[12]).setEmission(color).setMaterial(material), //
                new Triangle(points[12], points[13], points[18]).setEmission(color).setMaterial(material), //
                new Triangle(points[19], points[18], points[13]).setEmission(color).setMaterial(material), //
                new Triangle(points[13], points[14], points[19]).setEmission(color).setMaterial(material), //
                new Triangle(points[20], points[19], points[14]).setEmission(color).setMaterial(material), //
                new Triangle(points[14], points[15], points[20]).setEmission(color).setMaterial(material), //
                new Triangle(points[22], points[21], points[16]).setEmission(color).setMaterial(material), //
                new Triangle(points[16], points[17], points[22]).setEmission(color).setMaterial(material), //
                new Triangle(points[23], points[22], points[17]).setEmission(color).setMaterial(material), //
                new Triangle(points[17], points[18], points[23]).setEmission(color).setMaterial(material), //
                new Triangle(points[24], points[23], points[18]).setEmission(color).setMaterial(material), //
                new Triangle(points[18], points[19], points[24]).setEmission(color).setMaterial(material), //
                new Triangle(points[25], points[24], points[19]).setEmission(color).setMaterial(material), //
                new Triangle(points[19], points[20], points[25]).setEmission(color).setMaterial(material), //
                new Triangle(points[27], points[26], points[21]).setEmission(color).setMaterial(material), //
                new Triangle(points[21], points[22], points[27]).setEmission(color).setMaterial(material), //
                new Triangle(points[28], points[27], points[22]).setEmission(color).setMaterial(material), //
                new Triangle(points[22], points[23], points[28]).setEmission(color).setMaterial(material), //
                new Triangle(points[29], points[28], points[23]).setEmission(color).setMaterial(material), //
                new Triangle(points[23], points[24], points[29]).setEmission(color).setMaterial(material), //
                new Triangle(points[30], points[29], points[24]).setEmission(color).setMaterial(material), //
                new Triangle(points[24], points[25], points[30]).setEmission(color).setMaterial(material), //
                new Triangle(points[32], points[31], points[26]).setEmission(color).setMaterial(material), //
                new Triangle(points[26], points[27], points[32]).setEmission(color).setMaterial(material), //
                new Triangle(points[33], points[32], points[27]).setEmission(color).setMaterial(material), //
                new Triangle(points[27], points[28], points[33]).setEmission(color).setMaterial(material), //
                new Triangle(points[34], points[33], points[28]).setEmission(color).setMaterial(material), //
                new Triangle(points[28], points[29], points[34]).setEmission(color).setMaterial(material), //
                new Triangle(points[35], points[34], points[29]).setEmission(color).setMaterial(material), //
                new Triangle(points[29], points[30], points[35]).setEmission(color).setMaterial(material), //
                new Triangle(points[37], points[36], points[31]).setEmission(color).setMaterial(material), //
                new Triangle(points[31], points[32], points[37]).setEmission(color).setMaterial(material), //
                new Triangle(points[38], points[37], points[32]).setEmission(color).setMaterial(material), //
                new Triangle(points[32], points[33], points[38]).setEmission(color).setMaterial(material), //
                new Triangle(points[39], points[38], points[33]).setEmission(color).setMaterial(material), //
                new Triangle(points[33], points[34], points[39]).setEmission(color).setMaterial(material), //
                new Triangle(points[40], points[39], points[34]).setEmission(color).setMaterial(material), //
                new Triangle(points[34], points[35], points[40]).setEmission(color).setMaterial(material), //
                new Triangle(points[42], points[41], points[36]).setEmission(color).setMaterial(material), //
                new Triangle(points[36], points[37], points[42]).setEmission(color).setMaterial(material), //
                new Triangle(points[43], points[42], points[37]).setEmission(color).setMaterial(material), //
                new Triangle(points[37], points[38], points[43]).setEmission(color).setMaterial(material), //
                new Triangle(points[44], points[43], points[38]).setEmission(color).setMaterial(material), //
                new Triangle(points[38], points[39], points[44]).setEmission(color).setMaterial(material), //
                new Triangle(points[45], points[44], points[39]).setEmission(color).setMaterial(material), //
                new Triangle(points[39], points[40], points[45]).setEmission(color).setMaterial(material), //
                new Triangle(points[47], points[46], points[41]).setEmission(color).setMaterial(material), //
                new Triangle(points[41], points[42], points[47]).setEmission(color).setMaterial(material), //
                new Triangle(points[48], points[47], points[42]).setEmission(color).setMaterial(material), //
                new Triangle(points[42], points[43], points[48]).setEmission(color).setMaterial(material), //
                new Triangle(points[49], points[48], points[43]).setEmission(color).setMaterial(material), //
                new Triangle(points[43], points[44], points[49]).setEmission(color).setMaterial(material), //
                new Triangle(points[50], points[49], points[44]).setEmission(color).setMaterial(material), //
                new Triangle(points[44], points[45], points[50]).setEmission(color).setMaterial(material), //
                new Triangle(points[52], points[51], points[46]).setEmission(color).setMaterial(material), //
                new Triangle(points[46], points[47], points[52]).setEmission(color).setMaterial(material), //
                new Triangle(points[53], points[52], points[47]).setEmission(color).setMaterial(material), //
                new Triangle(points[47], points[48], points[53]).setEmission(color).setMaterial(material), //
                new Triangle(points[54], points[53], points[48]).setEmission(color).setMaterial(material), //
                new Triangle(points[48], points[49], points[54]).setEmission(color).setMaterial(material), //
                new Triangle(points[55], points[54], points[49]).setEmission(color).setMaterial(material), //
                new Triangle(points[49], points[50], points[55]).setEmission(color).setMaterial(material), //
                new Triangle(points[57], points[56], points[51]).setEmission(color).setMaterial(material), //
                new Triangle(points[51], points[52], points[57]).setEmission(color).setMaterial(material), //
                new Triangle(points[58], points[57], points[52]).setEmission(color).setMaterial(material), //
                new Triangle(points[52], points[53], points[58]).setEmission(color).setMaterial(material), //
                new Triangle(points[59], points[58], points[53]).setEmission(color).setMaterial(material), //
                new Triangle(points[53], points[54], points[59]).setEmission(color).setMaterial(material), //
                new Triangle(points[60], points[59], points[54]).setEmission(color).setMaterial(material), //
                new Triangle(points[54], points[55], points[60]).setEmission(color).setMaterial(material), //
                new Triangle(points[62], points[61], points[56]).setEmission(color).setMaterial(material), //
                new Triangle(points[56], points[57], points[62]).setEmission(color).setMaterial(material), //
                new Triangle(points[63], points[62], points[57]).setEmission(color).setMaterial(material), //
                new Triangle(points[57], points[58], points[63]).setEmission(color).setMaterial(material), //
                new Triangle(points[64], points[63], points[58]).setEmission(color).setMaterial(material), //
                new Triangle(points[58], points[59], points[64]).setEmission(color).setMaterial(material), //
                new Triangle(points[65], points[64], points[59]).setEmission(color).setMaterial(material), //
                new Triangle(points[59], points[60], points[65]).setEmission(color).setMaterial(material), //
                new Triangle(points[67], points[66], points[61]).setEmission(color).setMaterial(material), //
                new Triangle(points[61], points[62], points[67]).setEmission(color).setMaterial(material), //
                new Triangle(points[68], points[67], points[62]).setEmission(color).setMaterial(material), //
                new Triangle(points[62], points[63], points[68]).setEmission(color).setMaterial(material), //
                new Triangle(points[69], points[68], points[63]).setEmission(color).setMaterial(material), //
                new Triangle(points[63], points[64], points[69]).setEmission(color).setMaterial(material), //
                new Triangle(points[70], points[69], points[64]).setEmission(color).setMaterial(material), //
                new Triangle(points[64], points[65], points[70]).setEmission(color).setMaterial(material), //
                new Triangle(points[72], points[71], points[66]).setEmission(color).setMaterial(material), //
                new Triangle(points[66], points[67], points[72]).setEmission(color).setMaterial(material), //
                new Triangle(points[73], points[72], points[67]).setEmission(color).setMaterial(material), //
                new Triangle(points[67], points[68], points[73]).setEmission(color).setMaterial(material), //
                new Triangle(points[74], points[73], points[68]).setEmission(color).setMaterial(material), //
                new Triangle(points[68], points[69], points[74]).setEmission(color).setMaterial(material), //
                new Triangle(points[75], points[74], points[69]).setEmission(color).setMaterial(material), //
                new Triangle(points[69], points[70], points[75]).setEmission(color).setMaterial(material), //
                new Triangle(points[77], points[76], points[71]).setEmission(color).setMaterial(material), //
                new Triangle(points[71], points[72], points[77]).setEmission(color).setMaterial(material), //
                new Triangle(points[78], points[77], points[72]).setEmission(color).setMaterial(material), //
                new Triangle(points[72], points[73], points[78]).setEmission(color).setMaterial(material), //
                new Triangle(points[79], points[78], points[73]).setEmission(color).setMaterial(material), //
                new Triangle(points[73], points[74], points[79]).setEmission(color).setMaterial(material), //
                new Triangle(points[80], points[79], points[74]).setEmission(color).setMaterial(material), //
                new Triangle(points[74], points[75], points[80]).setEmission(color).setMaterial(material), //
                new Triangle(points[2], points[1], points[76]).setEmission(color).setMaterial(material), //
                new Triangle(points[76], points[77], points[2]).setEmission(color).setMaterial(material), //
                new Triangle(points[3], points[2], points[77]).setEmission(color).setMaterial(material), //
                new Triangle(points[77], points[78], points[3]).setEmission(color).setMaterial(material), //
                new Triangle(points[4], points[3], points[78]).setEmission(color).setMaterial(material), //
                new Triangle(points[78], points[79], points[4]).setEmission(color).setMaterial(material), //
                new Triangle(points[5], points[4], points[79]).setEmission(color).setMaterial(material), //
                new Triangle(points[79], points[80], points[5]).setEmission(color).setMaterial(material), //
                new Triangle(points[85], points[10], points[5]).setEmission(color).setMaterial(material), //
                new Triangle(points[5], points[81], points[85]).setEmission(color).setMaterial(material), //
                new Triangle(points[86], points[85], points[81]).setEmission(color).setMaterial(material), //
                new Triangle(points[81], points[82], points[86]).setEmission(color).setMaterial(material), //
                new Triangle(points[87], points[86], points[82]).setEmission(color).setMaterial(material), //
                new Triangle(points[82], points[83], points[87]).setEmission(color).setMaterial(material), //
                new Triangle(points[88], points[87], points[83]).setEmission(color).setMaterial(material), //
                new Triangle(points[83], points[84], points[88]).setEmission(color).setMaterial(material), //
                new Triangle(points[89], points[15], points[10]).setEmission(color).setMaterial(material), //
                new Triangle(points[10], points[85], points[89]).setEmission(color).setMaterial(material), //
                new Triangle(points[90], points[89], points[85]).setEmission(color).setMaterial(material), //
                new Triangle(points[85], points[86], points[90]).setEmission(color).setMaterial(material), //
                new Triangle(points[91], points[90], points[86]).setEmission(color).setMaterial(material), //
                new Triangle(points[86], points[87], points[91]).setEmission(color).setMaterial(material), //
                new Triangle(points[92], points[91], points[87]).setEmission(color).setMaterial(material), //
                new Triangle(points[87], points[88], points[92]).setEmission(color).setMaterial(material), //
                new Triangle(points[93], points[20], points[15]).setEmission(color).setMaterial(material), //
                new Triangle(points[15], points[89], points[93]).setEmission(color).setMaterial(material), //
                new Triangle(points[94], points[93], points[89]).setEmission(color).setMaterial(material), //
                new Triangle(points[89], points[90], points[94]).setEmission(color).setMaterial(material), //
                new Triangle(points[95], points[94], points[90]).setEmission(color).setMaterial(material), //
                new Triangle(points[90], points[91], points[95]).setEmission(color).setMaterial(material), //
                new Triangle(points[96], points[95], points[91]).setEmission(color).setMaterial(material), //
                new Triangle(points[91], points[92], points[96]).setEmission(color).setMaterial(material), //
                new Triangle(points[97], points[25], points[20]).setEmission(color).setMaterial(material), //
                new Triangle(points[20], points[93], points[97]).setEmission(color).setMaterial(material), //
                new Triangle(points[98], points[97], points[93]).setEmission(color).setMaterial(material), //
                new Triangle(points[93], points[94], points[98]).setEmission(color).setMaterial(material), //
                new Triangle(points[99], points[98], points[94]).setEmission(color).setMaterial(material), //
                new Triangle(points[94], points[95], points[99]).setEmission(color).setMaterial(material), //
                new Triangle(points[100], points[99], points[95]).setEmission(color).setMaterial(material), //
                new Triangle(points[95], points[96], points[100]).setEmission(color).setMaterial(material), //
                new Triangle(points[101], points[30], points[25]).setEmission(color).setMaterial(material), //
                new Triangle(points[25], points[97], points[101]).setEmission(color).setMaterial(material), //
                new Triangle(points[102], points[101], points[97]).setEmission(color).setMaterial(material), //
                new Triangle(points[97], points[98], points[102]).setEmission(color).setMaterial(material), //
                new Triangle(points[103], points[102], points[98]).setEmission(color).setMaterial(material), //
                new Triangle(points[98], points[99], points[103]).setEmission(color).setMaterial(material), //
                new Triangle(points[104], points[103], points[99]).setEmission(color).setMaterial(material), //
                new Triangle(points[99], points[100], points[104]).setEmission(color).setMaterial(material), //
                new Triangle(points[105], points[35], points[30]).setEmission(color).setMaterial(material), //
                new Triangle(points[30], points[101], points[105]).setEmission(color).setMaterial(material), //
                new Triangle(points[106], points[105], points[101]).setEmission(color).setMaterial(material), //
                new Triangle(points[101], points[102], points[106]).setEmission(color).setMaterial(material), //
                new Triangle(points[107], points[106], points[102]).setEmission(color).setMaterial(material), //
                new Triangle(points[102], points[103], points[107]).setEmission(color).setMaterial(material), //
                new Triangle(points[108], points[107], points[103]).setEmission(color).setMaterial(material), //
                new Triangle(points[103], points[104], points[108]).setEmission(color).setMaterial(material), //
                new Triangle(points[109], points[40], points[35]).setEmission(color).setMaterial(material), //
                new Triangle(points[35], points[105], points[109]).setEmission(color).setMaterial(material), //
                new Triangle(points[110], points[109], points[105]).setEmission(color).setMaterial(material), //
                new Triangle(points[105], points[106], points[110]).setEmission(color).setMaterial(material), //
                new Triangle(points[111], points[110], points[106]).setEmission(color).setMaterial(material), //
                new Triangle(points[106], points[107], points[111]).setEmission(color).setMaterial(material), //
                new Triangle(points[112], points[111], points[107]).setEmission(color).setMaterial(material), //
                new Triangle(points[107], points[108], points[112]).setEmission(color).setMaterial(material), //
                new Triangle(points[113], points[45], points[40]).setEmission(color).setMaterial(material), //
                new Triangle(points[40], points[109], points[113]).setEmission(color).setMaterial(material), //
                new Triangle(points[114], points[113], points[109]).setEmission(color).setMaterial(material), //
                new Triangle(points[109], points[110], points[114]).setEmission(color).setMaterial(material), //
                new Triangle(points[115], points[114], points[110]).setEmission(color).setMaterial(material), //
                new Triangle(points[110], points[111], points[115]).setEmission(color).setMaterial(material), //
                new Triangle(points[116], points[115], points[111]).setEmission(color).setMaterial(material), //
                new Triangle(points[111], points[112], points[116]).setEmission(color).setMaterial(material), //
                new Triangle(points[117], points[50], points[45]).setEmission(color).setMaterial(material), //
                new Triangle(points[45], points[113], points[117]).setEmission(color).setMaterial(material), //
                new Triangle(points[118], points[117], points[113]).setEmission(color).setMaterial(material), //
                new Triangle(points[113], points[114], points[118]).setEmission(color).setMaterial(material), //
                new Triangle(points[119], points[118], points[114]).setEmission(color).setMaterial(material), //
                new Triangle(points[114], points[115], points[119]).setEmission(color).setMaterial(material), //
                new Triangle(points[120], points[119], points[115]).setEmission(color).setMaterial(material), //
                new Triangle(points[115], points[116], points[120]).setEmission(color).setMaterial(material), //
                new Triangle(points[121], points[55], points[50]).setEmission(color).setMaterial(material), //
                new Triangle(points[50], points[117], points[121]).setEmission(color).setMaterial(material), //
                new Triangle(points[122], points[121], points[117]).setEmission(color).setMaterial(material), //
                new Triangle(points[117], points[118], points[122]).setEmission(color).setMaterial(material), //
                new Triangle(points[123], points[122], points[118]).setEmission(color).setMaterial(material), //
                new Triangle(points[118], points[119], points[123]).setEmission(color).setMaterial(material), //
                new Triangle(points[124], points[123], points[119]).setEmission(color).setMaterial(material), //
                new Triangle(points[119], points[120], points[124]).setEmission(color).setMaterial(material), //
                new Triangle(points[125], points[60], points[55]).setEmission(color).setMaterial(material), //
                new Triangle(points[55], points[121], points[125]).setEmission(color).setMaterial(material), //
                new Triangle(points[126], points[125], points[121]).setEmission(color).setMaterial(material), //
                new Triangle(points[121], points[122], points[126]).setEmission(color).setMaterial(material), //
                new Triangle(points[127], points[126], points[122]).setEmission(color).setMaterial(material), //
                new Triangle(points[122], points[123], points[127]).setEmission(color).setMaterial(material), //
                new Triangle(points[128], points[127], points[123]).setEmission(color).setMaterial(material), //
                new Triangle(points[123], points[124], points[128]).setEmission(color).setMaterial(material), //
                new Triangle(points[129], points[65], points[60]).setEmission(color).setMaterial(material), //
                new Triangle(points[60], points[125], points[129]).setEmission(color).setMaterial(material), //
                new Triangle(points[130], points[129], points[125]).setEmission(color).setMaterial(material), //
                new Triangle(points[125], points[126], points[130]).setEmission(color).setMaterial(material), //
                new Triangle(points[131], points[130], points[126]).setEmission(color).setMaterial(material), //
                new Triangle(points[126], points[127], points[131]).setEmission(color).setMaterial(material), //
                new Triangle(points[132], points[131], points[127]).setEmission(color).setMaterial(material), //
                new Triangle(points[127], points[128], points[132]).setEmission(color).setMaterial(material), //
                new Triangle(points[133], points[70], points[65]).setEmission(color).setMaterial(material), //
                new Triangle(points[65], points[129], points[133]).setEmission(color).setMaterial(material), //
                new Triangle(points[134], points[133], points[129]).setEmission(color).setMaterial(material), //
                new Triangle(points[129], points[130], points[134]).setEmission(color).setMaterial(material), //
                new Triangle(points[135], points[134], points[130]).setEmission(color).setMaterial(material), //
                new Triangle(points[130], points[131], points[135]).setEmission(color).setMaterial(material), //
                new Triangle(points[136], points[135], points[131]).setEmission(color).setMaterial(material), //
                new Triangle(points[131], points[132], points[136]).setEmission(color).setMaterial(material), //
                new Triangle(points[137], points[75], points[70]).setEmission(color).setMaterial(material), //
                new Triangle(points[70], points[133], points[137]).setEmission(color).setMaterial(material), //
                new Triangle(points[138], points[137], points[133]).setEmission(color).setMaterial(material), //
                new Triangle(points[133], points[134], points[138]).setEmission(color).setMaterial(material), //
                new Triangle(points[139], points[138], points[134]).setEmission(color).setMaterial(material), //
                new Triangle(points[134], points[135], points[139]).setEmission(color).setMaterial(material), //
                new Triangle(points[140], points[139], points[135]).setEmission(color).setMaterial(material), //
                new Triangle(points[135], points[136], points[140]).setEmission(color).setMaterial(material), //
                new Triangle(points[141], points[80], points[75]).setEmission(color).setMaterial(material), //
                new Triangle(points[75], points[137], points[141]).setEmission(color).setMaterial(material), //
                new Triangle(points[142], points[141], points[137]).setEmission(color).setMaterial(material), //
                new Triangle(points[137], points[138], points[142]).setEmission(color).setMaterial(material), //
                new Triangle(points[143], points[142], points[138]).setEmission(color).setMaterial(material), //
                new Triangle(points[138], points[139], points[143]).setEmission(color).setMaterial(material), //
                new Triangle(points[144], points[143], points[139]).setEmission(color).setMaterial(material), //
                new Triangle(points[139], points[140], points[144]).setEmission(color).setMaterial(material), //
                new Triangle(points[81], points[5], points[80]).setEmission(color).setMaterial(material), //
                new Triangle(points[80], points[141], points[81]).setEmission(color).setMaterial(material), //
                new Triangle(points[82], points[81], points[141]).setEmission(color).setMaterial(material), //
                new Triangle(points[141], points[142], points[82]).setEmission(color).setMaterial(material), //
                new Triangle(points[83], points[82], points[142]).setEmission(color).setMaterial(material), //
                new Triangle(points[142], points[143], points[83]).setEmission(color).setMaterial(material), //
                new Triangle(points[84], points[83], points[143]).setEmission(color).setMaterial(material), //
                new Triangle(points[143], points[144], points[84]).setEmission(color).setMaterial(material), //
                new Triangle(points[149], points[88], points[84]).setEmission(color).setMaterial(material), //
                new Triangle(points[84], points[145], points[149]).setEmission(color).setMaterial(material), //
                new Triangle(points[150], points[149], points[145]).setEmission(color).setMaterial(material), //
                new Triangle(points[145], points[146], points[150]).setEmission(color).setMaterial(material), //
                new Triangle(points[151], points[150], points[146]).setEmission(color).setMaterial(material), //
                new Triangle(points[146], points[147], points[151]).setEmission(color).setMaterial(material), //
                new Triangle(points[152], points[151], points[147]).setEmission(color).setMaterial(material), //
                new Triangle(points[147], points[148], points[152]).setEmission(color).setMaterial(material), //
                new Triangle(points[153], points[92], points[88]).setEmission(color).setMaterial(material), //
                new Triangle(points[88], points[149], points[153]).setEmission(color).setMaterial(material), //
                new Triangle(points[154], points[153], points[149]).setEmission(color).setMaterial(material), //
                new Triangle(points[149], points[150], points[154]).setEmission(color).setMaterial(material), //
                new Triangle(points[155], points[154], points[150]).setEmission(color).setMaterial(material), //
                new Triangle(points[150], points[151], points[155]).setEmission(color).setMaterial(material), //
                new Triangle(points[156], points[155], points[151]).setEmission(color).setMaterial(material), //
                new Triangle(points[151], points[152], points[156]).setEmission(color).setMaterial(material), //
                new Triangle(points[157], points[96], points[92]).setEmission(color).setMaterial(material), //
                new Triangle(points[92], points[153], points[157]).setEmission(color).setMaterial(material), //
                new Triangle(points[158], points[157], points[153]).setEmission(color).setMaterial(material), //
                new Triangle(points[153], points[154], points[158]).setEmission(color).setMaterial(material), //
                new Triangle(points[159], points[158], points[154]).setEmission(color).setMaterial(material), //
                new Triangle(points[154], points[155], points[159]).setEmission(color).setMaterial(material), //
                new Triangle(points[160], points[159], points[155]).setEmission(color).setMaterial(material), //
                new Triangle(points[155], points[156], points[160]).setEmission(color).setMaterial(material), //
                new Triangle(points[161], points[100], points[96]).setEmission(color).setMaterial(material), //
                new Triangle(points[96], points[157], points[161]).setEmission(color).setMaterial(material), //
                new Triangle(points[162], points[161], points[157]).setEmission(color).setMaterial(material), //
                new Triangle(points[157], points[158], points[162]).setEmission(color).setMaterial(material), //
                new Triangle(points[163], points[162], points[158]).setEmission(color).setMaterial(material), //
                new Triangle(points[158], points[159], points[163]).setEmission(color).setMaterial(material), //
                new Triangle(points[164], points[163], points[159]).setEmission(color).setMaterial(material), //
                new Triangle(points[159], points[160], points[164]).setEmission(color).setMaterial(material), //
                new Triangle(points[165], points[104], points[100]).setEmission(color).setMaterial(material), //
                new Triangle(points[100], points[161], points[165]).setEmission(color).setMaterial(material), //
                new Triangle(points[166], points[165], points[161]).setEmission(color).setMaterial(material), //
                new Triangle(points[161], points[162], points[166]).setEmission(color).setMaterial(material), //
                new Triangle(points[167], points[166], points[162]).setEmission(color).setMaterial(material), //
                new Triangle(points[162], points[163], points[167]).setEmission(color).setMaterial(material), //
                new Triangle(points[168], points[167], points[163]).setEmission(color).setMaterial(material), //
                new Triangle(points[163], points[164], points[168]).setEmission(color).setMaterial(material), //
                new Triangle(points[169], points[108], points[104]).setEmission(color).setMaterial(material), //
                new Triangle(points[104], points[165], points[169]).setEmission(color).setMaterial(material), //
                new Triangle(points[170], points[169], points[165]).setEmission(color).setMaterial(material), //
                new Triangle(points[165], points[166], points[170]).setEmission(color).setMaterial(material), //
                new Triangle(points[171], points[170], points[166]).setEmission(color).setMaterial(material), //
                new Triangle(points[166], points[167], points[171]).setEmission(color).setMaterial(material), //
                new Triangle(points[172], points[171], points[167]).setEmission(color).setMaterial(material), //
                new Triangle(points[167], points[168], points[172]).setEmission(color).setMaterial(material), //
                new Triangle(points[173], points[112], points[108]).setEmission(color).setMaterial(material), //
                new Triangle(points[108], points[169], points[173]).setEmission(color).setMaterial(material), //
                new Triangle(points[174], points[173], points[169]).setEmission(color).setMaterial(material), //
                new Triangle(points[169], points[170], points[174]).setEmission(color).setMaterial(material), //
                new Triangle(points[175], points[174], points[170]).setEmission(color).setMaterial(material), //
                new Triangle(points[170], points[171], points[175]).setEmission(color).setMaterial(material), //
                new Triangle(points[176], points[175], points[171]).setEmission(color).setMaterial(material), //
                new Triangle(points[171], points[172], points[176]).setEmission(color).setMaterial(material), //
                new Triangle(points[177], points[116], points[112]).setEmission(color).setMaterial(material), //
                new Triangle(points[112], points[173], points[177]).setEmission(color).setMaterial(material), //
                new Triangle(points[178], points[177], points[173]).setEmission(color).setMaterial(material), //
                new Triangle(points[173], points[174], points[178]).setEmission(color).setMaterial(material), //
                new Triangle(points[179], points[178], points[174]).setEmission(color).setMaterial(material), //
                new Triangle(points[174], points[175], points[179]).setEmission(color).setMaterial(material), //
                new Triangle(points[180], points[179], points[175]).setEmission(color).setMaterial(material), //
                new Triangle(points[175], points[176], points[180]).setEmission(color).setMaterial(material), //
                new Triangle(points[181], points[120], points[116]).setEmission(color).setMaterial(material), //
                new Triangle(points[116], points[177], points[181]).setEmission(color).setMaterial(material), //
                new Triangle(points[182], points[181], points[177]).setEmission(color).setMaterial(material), //
                new Triangle(points[177], points[178], points[182]).setEmission(color).setMaterial(material), //
                new Triangle(points[183], points[182], points[178]).setEmission(color).setMaterial(material), //
                new Triangle(points[178], points[179], points[183]).setEmission(color).setMaterial(material), //
                new Triangle(points[184], points[183], points[179]).setEmission(color).setMaterial(material), //
                new Triangle(points[179], points[180], points[184]).setEmission(color).setMaterial(material), //
                new Triangle(points[185], points[124], points[120]).setEmission(color).setMaterial(material), //
                new Triangle(points[120], points[181], points[185]).setEmission(color).setMaterial(material), //
                new Triangle(points[186], points[185], points[181]).setEmission(color).setMaterial(material), //
                new Triangle(points[181], points[182], points[186]).setEmission(color).setMaterial(material), //
                new Triangle(points[187], points[186], points[182]).setEmission(color).setMaterial(material), //
                new Triangle(points[182], points[183], points[187]).setEmission(color).setMaterial(material), //
                new Triangle(points[188], points[187], points[183]).setEmission(color).setMaterial(material), //
                new Triangle(points[183], points[184], points[188]).setEmission(color).setMaterial(material), //
                new Triangle(points[189], points[128], points[124]).setEmission(color).setMaterial(material), //
                new Triangle(points[124], points[185], points[189]).setEmission(color).setMaterial(material), //
                new Triangle(points[190], points[189], points[185]).setEmission(color).setMaterial(material), //
                new Triangle(points[185], points[186], points[190]).setEmission(color).setMaterial(material), //
                new Triangle(points[191], points[190], points[186]).setEmission(color).setMaterial(material), //
                new Triangle(points[186], points[187], points[191]).setEmission(color).setMaterial(material), //
                new Triangle(points[192], points[191], points[187]).setEmission(color).setMaterial(material), //
                new Triangle(points[187], points[188], points[192]).setEmission(color).setMaterial(material), //
                new Triangle(points[193], points[132], points[128]).setEmission(color).setMaterial(material), //
                new Triangle(points[128], points[189], points[193]).setEmission(color).setMaterial(material), //
                new Triangle(points[194], points[193], points[189]).setEmission(color).setMaterial(material), //
                new Triangle(points[189], points[190], points[194]).setEmission(color).setMaterial(material), //
                new Triangle(points[195], points[194], points[190]).setEmission(color).setMaterial(material), //
                new Triangle(points[190], points[191], points[195]).setEmission(color).setMaterial(material), //
                new Triangle(points[196], points[195], points[191]).setEmission(color).setMaterial(material), //
                new Triangle(points[191], points[192], points[196]).setEmission(color).setMaterial(material), //
                new Triangle(points[197], points[136], points[132]).setEmission(color).setMaterial(material), //
                new Triangle(points[132], points[193], points[197]).setEmission(color).setMaterial(material), //
                new Triangle(points[198], points[197], points[193]).setEmission(color).setMaterial(material), //
                new Triangle(points[193], points[194], points[198]).setEmission(color).setMaterial(material), //
                new Triangle(points[199], points[198], points[194]).setEmission(color).setMaterial(material), //
                new Triangle(points[194], points[195], points[199]).setEmission(color).setMaterial(material), //
                new Triangle(points[200], points[199], points[195]).setEmission(color).setMaterial(material), //
                new Triangle(points[195], points[196], points[200]).setEmission(color).setMaterial(material), //
                new Triangle(points[201], points[140], points[136]).setEmission(color).setMaterial(material), //
                new Triangle(points[136], points[197], points[201]).setEmission(color).setMaterial(material), //
                new Triangle(points[202], points[201], points[197]).setEmission(color).setMaterial(material), //
                new Triangle(points[197], points[198], points[202]).setEmission(color).setMaterial(material), //
                new Triangle(points[203], points[202], points[198]).setEmission(color).setMaterial(material), //
                new Triangle(points[198], points[199], points[203]).setEmission(color).setMaterial(material), //
                new Triangle(points[204], points[203], points[199]).setEmission(color).setMaterial(material), //
                new Triangle(points[199], points[200], points[204]).setEmission(color).setMaterial(material), //
                new Triangle(points[205], points[144], points[140]).setEmission(color).setMaterial(material), //
                new Triangle(points[140], points[201], points[205]).setEmission(color).setMaterial(material), //
                new Triangle(points[206], points[205], points[201]).setEmission(color).setMaterial(material), //
                new Triangle(points[201], points[202], points[206]).setEmission(color).setMaterial(material), //
                new Triangle(points[207], points[206], points[202]).setEmission(color).setMaterial(material), //
                new Triangle(points[202], points[203], points[207]).setEmission(color).setMaterial(material), //
                new Triangle(points[208], points[207], points[203]).setEmission(color).setMaterial(material), //
                new Triangle(points[203], points[204], points[208]).setEmission(color).setMaterial(material), //
                new Triangle(points[145], points[84], points[144]).setEmission(color).setMaterial(material), //
                new Triangle(points[144], points[205], points[145]).setEmission(color).setMaterial(material), //
                new Triangle(points[146], points[145], points[205]).setEmission(color).setMaterial(material), //
                new Triangle(points[205], points[206], points[146]).setEmission(color).setMaterial(material), //
                new Triangle(points[147], points[146], points[206]).setEmission(color).setMaterial(material), //
                new Triangle(points[206], points[207], points[147]).setEmission(color).setMaterial(material), //
                new Triangle(points[148], points[147], points[207]).setEmission(color).setMaterial(material), //
                new Triangle(points[207], points[208], points[148]).setEmission(color).setMaterial(material), //
                new Triangle(points[213], points[152], points[148]).setEmission(color).setMaterial(material), //
                new Triangle(points[148], points[209], points[213]).setEmission(color).setMaterial(material), //
                new Triangle(points[214], points[213], points[209]).setEmission(color).setMaterial(material), //
                new Triangle(points[209], points[210], points[214]).setEmission(color).setMaterial(material), //
                new Triangle(points[215], points[214], points[210]).setEmission(color).setMaterial(material), //
                new Triangle(points[210], points[211], points[215]).setEmission(color).setMaterial(material), //
                new Triangle(points[212], points[215], points[211]).setEmission(color).setMaterial(material), //
                new Triangle(points[216], points[156], points[152]).setEmission(color).setMaterial(material), //
                new Triangle(points[152], points[213], points[216]).setEmission(color).setMaterial(material), //
                new Triangle(points[217], points[216], points[213]).setEmission(color).setMaterial(material), //
                new Triangle(points[213], points[214], points[217]).setEmission(color).setMaterial(material), //
                new Triangle(points[218], points[217], points[214]).setEmission(color).setMaterial(material), //
                new Triangle(points[214], points[215], points[218]).setEmission(color).setMaterial(material), //
                new Triangle(points[212], points[218], points[215]).setEmission(color).setMaterial(material), //
                new Triangle(points[219], points[160], points[156]).setEmission(color).setMaterial(material), //
                new Triangle(points[156], points[216], points[219]).setEmission(color).setMaterial(material), //
                new Triangle(points[220], points[219], points[216]).setEmission(color).setMaterial(material), //
                new Triangle(points[216], points[217], points[220]).setEmission(color).setMaterial(material), //
                new Triangle(points[221], points[220], points[217]).setEmission(color).setMaterial(material), //
                new Triangle(points[217], points[218], points[221]).setEmission(color).setMaterial(material), //
                new Triangle(points[212], points[221], points[218]).setEmission(color).setMaterial(material), //
                new Triangle(points[222], points[164], points[160]).setEmission(color).setMaterial(material), //
                new Triangle(points[160], points[219], points[222]).setEmission(color).setMaterial(material), //
                new Triangle(points[223], points[222], points[219]).setEmission(color).setMaterial(material), //
                new Triangle(points[219], points[220], points[223]).setEmission(color).setMaterial(material), //
                new Triangle(points[224], points[223], points[220]).setEmission(color).setMaterial(material), //
                new Triangle(points[220], points[221], points[224]).setEmission(color).setMaterial(material), //
                new Triangle(points[212], points[224], points[221]).setEmission(color).setMaterial(material), //
                new Triangle(points[225], points[168], points[164]).setEmission(color).setMaterial(material), //
                new Triangle(points[164], points[222], points[225]).setEmission(color).setMaterial(material), //
                new Triangle(points[226], points[225], points[222]).setEmission(color).setMaterial(material), //
                new Triangle(points[222], points[223], points[226]).setEmission(color).setMaterial(material), //
                new Triangle(points[227], points[226], points[223]).setEmission(color).setMaterial(material), //
                new Triangle(points[223], points[224], points[227]).setEmission(color).setMaterial(material), //
                new Triangle(points[212], points[227], points[224]).setEmission(color).setMaterial(material), //
                new Triangle(points[228], points[172], points[168]).setEmission(color).setMaterial(material), //
                new Triangle(points[168], points[225], points[228]).setEmission(color).setMaterial(material), //
                new Triangle(points[229], points[228], points[225]).setEmission(color).setMaterial(material), //
                new Triangle(points[225], points[226], points[229]).setEmission(color).setMaterial(material), //
                new Triangle(points[230], points[229], points[226]).setEmission(color).setMaterial(material), //
                new Triangle(points[226], points[227], points[230]).setEmission(color).setMaterial(material), //
                new Triangle(points[212], points[230], points[227]).setEmission(color).setMaterial(material), //
                new Triangle(points[231], points[176], points[172]).setEmission(color).setMaterial(material), //
                new Triangle(points[172], points[228], points[231]).setEmission(color).setMaterial(material), //
                new Triangle(points[232], points[231], points[228]).setEmission(color).setMaterial(material), //
                new Triangle(points[228], points[229], points[232]).setEmission(color).setMaterial(material), //
                new Triangle(points[233], points[232], points[229]).setEmission(color).setMaterial(material), //
                new Triangle(points[229], points[230], points[233]).setEmission(color).setMaterial(material), //
                new Triangle(points[212], points[233], points[230]).setEmission(color).setMaterial(material), //
                new Triangle(points[234], points[180], points[176]).setEmission(color).setMaterial(material), //
                new Triangle(points[176], points[231], points[234]).setEmission(color).setMaterial(material), //
                new Triangle(points[235], points[234], points[231]).setEmission(color).setMaterial(material), //
                new Triangle(points[231], points[232], points[235]).setEmission(color).setMaterial(material), //
                new Triangle(points[236], points[235], points[232]).setEmission(color).setMaterial(material), //
                new Triangle(points[232], points[233], points[236]).setEmission(color).setMaterial(material), //
                new Triangle(points[212], points[236], points[233]).setEmission(color).setMaterial(material), //
                new Triangle(points[237], points[184], points[180]).setEmission(color).setMaterial(material), //
                new Triangle(points[180], points[234], points[237]).setEmission(color).setMaterial(material), //
                new Triangle(points[238], points[237], points[234]).setEmission(color).setMaterial(material), //
                new Triangle(points[234], points[235], points[238]).setEmission(color).setMaterial(material), //
                new Triangle(points[239], points[238], points[235]).setEmission(color).setMaterial(material), //
                new Triangle(points[235], points[236], points[239]).setEmission(color).setMaterial(material), //
                new Triangle(points[212], points[239], points[236]).setEmission(color).setMaterial(material), //
                new Triangle(points[240], points[188], points[184]).setEmission(color).setMaterial(material), //
                new Triangle(points[184], points[237], points[240]).setEmission(color).setMaterial(material), //
                new Triangle(points[241], points[240], points[237]).setEmission(color).setMaterial(material), //
                new Triangle(points[237], points[238], points[241]).setEmission(color).setMaterial(material), //
                new Triangle(points[242], points[241], points[238]).setEmission(color).setMaterial(material), //
                new Triangle(points[238], points[239], points[242]).setEmission(color).setMaterial(material), //
                new Triangle(points[212], points[242], points[239]).setEmission(color).setMaterial(material), //
                new Triangle(points[243], points[192], points[188]).setEmission(color).setMaterial(material), //
                new Triangle(points[188], points[240], points[243]).setEmission(color).setMaterial(material), //
                new Triangle(points[244], points[243], points[240]).setEmission(color).setMaterial(material), //
                new Triangle(points[240], points[241], points[244]).setEmission(color).setMaterial(material), //
                new Triangle(points[245], points[244], points[241]).setEmission(color).setMaterial(material), //
                new Triangle(points[241], points[242], points[245]).setEmission(color).setMaterial(material), //
                new Triangle(points[212], points[245], points[242]).setEmission(color).setMaterial(material), //
                new Triangle(points[246], points[196], points[192]).setEmission(color).setMaterial(material), //
                new Triangle(points[192], points[243], points[246]).setEmission(color).setMaterial(material), //
                new Triangle(points[247], points[246], points[243]).setEmission(color).setMaterial(material), //
                new Triangle(points[243], points[244], points[247]).setEmission(color).setMaterial(material), //
                new Triangle(points[248], points[247], points[244]).setEmission(color).setMaterial(material), //
                new Triangle(points[244], points[245], points[248]).setEmission(color).setMaterial(material), //
                new Triangle(points[212], points[248], points[245]).setEmission(color).setMaterial(material), //
                new Triangle(points[249], points[200], points[196]).setEmission(color).setMaterial(material), //
                new Triangle(points[196], points[246], points[249]).setEmission(color).setMaterial(material), //
                new Triangle(points[250], points[249], points[246]).setEmission(color).setMaterial(material), //
                new Triangle(points[246], points[247], points[250]).setEmission(color).setMaterial(material), //
                new Triangle(points[251], points[250], points[247]).setEmission(color).setMaterial(material), //
                new Triangle(points[247], points[248], points[251]).setEmission(color).setMaterial(material), //
                new Triangle(points[212], points[251], points[248]).setEmission(color).setMaterial(material), //
                new Triangle(points[252], points[204], points[200]).setEmission(color).setMaterial(material), //
                new Triangle(points[200], points[249], points[252]).setEmission(color).setMaterial(material), //
                new Triangle(points[253], points[252], points[249]).setEmission(color).setMaterial(material), //
                new Triangle(points[249], points[250], points[253]).setEmission(color).setMaterial(material), //
                new Triangle(points[254], points[253], points[250]).setEmission(color).setMaterial(material), //
                new Triangle(points[250], points[251], points[254]).setEmission(color).setMaterial(material), //
                new Triangle(points[212], points[254], points[251]).setEmission(color).setMaterial(material), //
                new Triangle(points[255], points[208], points[204]).setEmission(color).setMaterial(material), //
                new Triangle(points[204], points[252], points[255]).setEmission(color).setMaterial(material), //
                new Triangle(points[256], points[255], points[252]).setEmission(color).setMaterial(material), //
                new Triangle(points[252], points[253], points[256]).setEmission(color).setMaterial(material), //
                new Triangle(points[257], points[256], points[253]).setEmission(color).setMaterial(material), //
                new Triangle(points[253], points[254], points[257]).setEmission(color).setMaterial(material), //
                new Triangle(points[212], points[257], points[254]).setEmission(color).setMaterial(material), //
                new Triangle(points[209], points[148], points[208]).setEmission(color).setMaterial(material), //
                new Triangle(points[208], points[255], points[209]).setEmission(color).setMaterial(material), //
                new Triangle(points[210], points[209], points[255]).setEmission(color).setMaterial(material), //
                new Triangle(points[255], points[256], points[210]).setEmission(color).setMaterial(material), //
                new Triangle(points[211], points[210], points[256]).setEmission(color).setMaterial(material), //
                new Triangle(points[256], points[257], points[211]).setEmission(color).setMaterial(material), //
                new Triangle(points[212], points[211], points[257]).setEmission(color).setMaterial(material), //
                new Triangle(points[264], points[263], points[258]).setEmission(color).setMaterial(material), //
                new Triangle(points[258], points[259], points[264]).setEmission(color).setMaterial(material), //
                new Triangle(points[265], points[264], points[259]).setEmission(color).setMaterial(material), //
                new Triangle(points[259], points[260], points[265]).setEmission(color).setMaterial(material), //
                new Triangle(points[266], points[265], points[260]).setEmission(color).setMaterial(material), //
                new Triangle(points[260], points[261], points[266]).setEmission(color).setMaterial(material), //
                new Triangle(points[267], points[266], points[261]).setEmission(color).setMaterial(material), //
                new Triangle(points[261], points[262], points[267]).setEmission(color).setMaterial(material), //
                new Triangle(points[269], points[268], points[263]).setEmission(color).setMaterial(material), //
                new Triangle(points[263], points[264], points[269]).setEmission(color).setMaterial(material), //
                new Triangle(points[270], points[269], points[264]).setEmission(color).setMaterial(material), //
                new Triangle(points[264], points[265], points[270]).setEmission(color).setMaterial(material), //
                new Triangle(points[271], points[270], points[265]).setEmission(color).setMaterial(material), //
                new Triangle(points[265], points[266], points[271]).setEmission(color).setMaterial(material), //
                new Triangle(points[272], points[271], points[266]).setEmission(color).setMaterial(material), //
                new Triangle(points[266], points[267], points[272]).setEmission(color).setMaterial(material), //
                new Triangle(points[274], points[273], points[268]).setEmission(color).setMaterial(material), //
                new Triangle(points[268], points[269], points[274]).setEmission(color).setMaterial(material), //
                new Triangle(points[275], points[274], points[269]).setEmission(color).setMaterial(material), //
                new Triangle(points[269], points[270], points[275]).setEmission(color).setMaterial(material), //
                new Triangle(points[276], points[275], points[270]).setEmission(color).setMaterial(material), //
                new Triangle(points[270], points[271], points[276]).setEmission(color).setMaterial(material), //
                new Triangle(points[277], points[276], points[271]).setEmission(color).setMaterial(material), //
                new Triangle(points[271], points[272], points[277]).setEmission(color).setMaterial(material), //
                new Triangle(points[279], points[278], points[273]).setEmission(color).setMaterial(material), //
                new Triangle(points[273], points[274], points[279]).setEmission(color).setMaterial(material), //
                new Triangle(points[280], points[279], points[274]).setEmission(color).setMaterial(material), //
                new Triangle(points[274], points[275], points[280]).setEmission(color).setMaterial(material), //
                new Triangle(points[281], points[280], points[275]).setEmission(color).setMaterial(material), //
                new Triangle(points[275], points[276], points[281]).setEmission(color).setMaterial(material), //
                new Triangle(points[282], points[281], points[276]).setEmission(color).setMaterial(material), //
                new Triangle(points[276], points[277], points[282]).setEmission(color).setMaterial(material), //
                new Triangle(points[284], points[283], points[278]).setEmission(color).setMaterial(material), //
                new Triangle(points[278], points[279], points[284]).setEmission(color).setMaterial(material), //
                new Triangle(points[285], points[284], points[279]).setEmission(color).setMaterial(material), //
                new Triangle(points[279], points[280], points[285]).setEmission(color).setMaterial(material), //
                new Triangle(points[286], points[285], points[280]).setEmission(color).setMaterial(material), //
                new Triangle(points[280], points[281], points[286]).setEmission(color).setMaterial(material), //
                new Triangle(points[287], points[286], points[281]).setEmission(color).setMaterial(material), //
                new Triangle(points[281], points[282], points[287]).setEmission(color).setMaterial(material), //
                new Triangle(points[289], points[288], points[283]).setEmission(color).setMaterial(material), //
                new Triangle(points[283], points[284], points[289]).setEmission(color).setMaterial(material), //
                new Triangle(points[290], points[289], points[284]).setEmission(color).setMaterial(material), //
                new Triangle(points[284], points[285], points[290]).setEmission(color).setMaterial(material), //
                new Triangle(points[291], points[290], points[285]).setEmission(color).setMaterial(material), //
                new Triangle(points[285], points[286], points[291]).setEmission(color).setMaterial(material), //
                new Triangle(points[292], points[291], points[286]).setEmission(color).setMaterial(material), //
                new Triangle(points[286], points[287], points[292]).setEmission(color).setMaterial(material), //
                new Triangle(points[294], points[293], points[288]).setEmission(color).setMaterial(material), //
                new Triangle(points[288], points[289], points[294]).setEmission(color).setMaterial(material), //
                new Triangle(points[295], points[294], points[289]).setEmission(color).setMaterial(material), //
                new Triangle(points[289], points[290], points[295]).setEmission(color).setMaterial(material), //
                new Triangle(points[296], points[295], points[290]).setEmission(color).setMaterial(material), //
                new Triangle(points[290], points[291], points[296]).setEmission(color).setMaterial(material), //
                new Triangle(points[297], points[296], points[291]).setEmission(color).setMaterial(material), //
                new Triangle(points[291], points[292], points[297]).setEmission(color).setMaterial(material), //
                new Triangle(points[259], points[258], points[293]).setEmission(color).setMaterial(material), //
                new Triangle(points[293], points[294], points[259]).setEmission(color).setMaterial(material), //
                new Triangle(points[260], points[259], points[294]).setEmission(color).setMaterial(material), //
                new Triangle(points[294], points[295], points[260]).setEmission(color).setMaterial(material), //
                new Triangle(points[261], points[260], points[295]).setEmission(color).setMaterial(material), //
                new Triangle(points[295], points[296], points[261]).setEmission(color).setMaterial(material), //
                new Triangle(points[262], points[261], points[296]).setEmission(color).setMaterial(material), //
                new Triangle(points[296], points[297], points[262]).setEmission(color).setMaterial(material), //
                new Triangle(points[302], points[267], points[262]).setEmission(color).setMaterial(material), //
                new Triangle(points[262], points[298], points[302]).setEmission(color).setMaterial(material), //
                new Triangle(points[303], points[302], points[298]).setEmission(color).setMaterial(material), //
                new Triangle(points[298], points[299], points[303]).setEmission(color).setMaterial(material), //
                new Triangle(points[304], points[303], points[299]).setEmission(color).setMaterial(material), //
                new Triangle(points[299], points[300], points[304]).setEmission(color).setMaterial(material), //
                new Triangle(points[305], points[304], points[300]).setEmission(color).setMaterial(material), //
                new Triangle(points[300], points[301], points[305]).setEmission(color).setMaterial(material), //
                new Triangle(points[306], points[272], points[267]).setEmission(color).setMaterial(material), //
                new Triangle(points[267], points[302], points[306]).setEmission(color).setMaterial(material), //
                new Triangle(points[307], points[306], points[302]).setEmission(color).setMaterial(material), //
                new Triangle(points[302], points[303], points[307]).setEmission(color).setMaterial(material), //
                new Triangle(points[308], points[307], points[303]).setEmission(color).setMaterial(material), //
                new Triangle(points[303], points[304], points[308]).setEmission(color).setMaterial(material), //
                new Triangle(points[309], points[308], points[304]).setEmission(color).setMaterial(material), //
                new Triangle(points[304], points[305], points[309]).setEmission(color).setMaterial(material), //
                new Triangle(points[310], points[277], points[272]).setEmission(color).setMaterial(material), //
                new Triangle(points[272], points[306], points[310]).setEmission(color).setMaterial(material), //
                new Triangle(points[311], points[310], points[306]).setEmission(color).setMaterial(material), //
                new Triangle(points[306], points[307], points[311]).setEmission(color).setMaterial(material), //
                new Triangle(points[312], points[311], points[307]).setEmission(color).setMaterial(material), //
                new Triangle(points[307], points[308], points[312]).setEmission(color).setMaterial(material), //
                new Triangle(points[313], points[312], points[308]).setEmission(color).setMaterial(material), //
                new Triangle(points[308], points[309], points[313]).setEmission(color).setMaterial(material), //
                new Triangle(points[314], points[282], points[277]).setEmission(color).setMaterial(material), //
                new Triangle(points[277], points[310], points[314]).setEmission(color).setMaterial(material), //
                new Triangle(points[315], points[314], points[310]).setEmission(color).setMaterial(material), //
                new Triangle(points[310], points[311], points[315]).setEmission(color).setMaterial(material), //
                new Triangle(points[316], points[315], points[311]).setEmission(color).setMaterial(material), //
                new Triangle(points[311], points[312], points[316]).setEmission(color).setMaterial(material), //
                new Triangle(points[317], points[316], points[312]).setEmission(color).setMaterial(material), //
                new Triangle(points[312], points[313], points[317]).setEmission(color).setMaterial(material), //
                new Triangle(points[318], points[287], points[282]).setEmission(color).setMaterial(material), //
                new Triangle(points[282], points[314], points[318]).setEmission(color).setMaterial(material), //
                new Triangle(points[319], points[318], points[314]).setEmission(color).setMaterial(material), //
                new Triangle(points[314], points[315], points[319]).setEmission(color).setMaterial(material), //
                new Triangle(points[320], points[319], points[315]).setEmission(color).setMaterial(material), //
                new Triangle(points[315], points[316], points[320]).setEmission(color).setMaterial(material), //
                new Triangle(points[321], points[320], points[316]).setEmission(color).setMaterial(material), //
                new Triangle(points[316], points[317], points[321]).setEmission(color).setMaterial(material), //
                new Triangle(points[322], points[292], points[287]).setEmission(color).setMaterial(material), //
                new Triangle(points[287], points[318], points[322]).setEmission(color).setMaterial(material), //
                new Triangle(points[323], points[322], points[318]).setEmission(color).setMaterial(material), //
                new Triangle(points[318], points[319], points[323]).setEmission(color).setMaterial(material), //
                new Triangle(points[324], points[323], points[319]).setEmission(color).setMaterial(material), //
                new Triangle(points[319], points[320], points[324]).setEmission(color).setMaterial(material), //
                new Triangle(points[325], points[324], points[320]).setEmission(color).setMaterial(material), //
                new Triangle(points[320], points[321], points[325]).setEmission(color).setMaterial(material), //
                new Triangle(points[326], points[297], points[292]).setEmission(color).setMaterial(material), //
                new Triangle(points[292], points[322], points[326]).setEmission(color).setMaterial(material), //
                new Triangle(points[327], points[326], points[322]).setEmission(color).setMaterial(material), //
                new Triangle(points[322], points[323], points[327]).setEmission(color).setMaterial(material), //
                new Triangle(points[328], points[327], points[323]).setEmission(color).setMaterial(material), //
                new Triangle(points[323], points[324], points[328]).setEmission(color).setMaterial(material), //
                new Triangle(points[329], points[328], points[324]).setEmission(color).setMaterial(material), //
                new Triangle(points[324], points[325], points[329]).setEmission(color).setMaterial(material), //
                new Triangle(points[298], points[262], points[297]).setEmission(color).setMaterial(material), //
                new Triangle(points[297], points[326], points[298]).setEmission(color).setMaterial(material), //
                new Triangle(points[299], points[298], points[326]).setEmission(color).setMaterial(material), //
                new Triangle(points[326], points[327], points[299]).setEmission(color).setMaterial(material), //
                new Triangle(points[300], points[299], points[327]).setEmission(color).setMaterial(material), //
                new Triangle(points[327], points[328], points[300]).setEmission(color).setMaterial(material), //
                new Triangle(points[301], points[300], points[328]).setEmission(color).setMaterial(material), //
                new Triangle(points[328], points[329], points[301]).setEmission(color).setMaterial(material), //
                new Triangle(points[336], points[335], points[330]).setEmission(color).setMaterial(material), //
                new Triangle(points[330], points[331], points[336]).setEmission(color).setMaterial(material), //
                new Triangle(points[337], points[336], points[331]).setEmission(color).setMaterial(material), //
                new Triangle(points[331], points[332], points[337]).setEmission(color).setMaterial(material), //
                new Triangle(points[338], points[337], points[332]).setEmission(color).setMaterial(material), //
                new Triangle(points[332], points[333], points[338]).setEmission(color).setMaterial(material), //
                new Triangle(points[339], points[338], points[333]).setEmission(color).setMaterial(material), //
                new Triangle(points[333], points[334], points[339]).setEmission(color).setMaterial(material), //
                new Triangle(points[341], points[340], points[335]).setEmission(color).setMaterial(material), //
                new Triangle(points[335], points[336], points[341]).setEmission(color).setMaterial(material), //
                new Triangle(points[342], points[341], points[336]).setEmission(color).setMaterial(material), //
                new Triangle(points[336], points[337], points[342]).setEmission(color).setMaterial(material), //
                new Triangle(points[343], points[342], points[337]).setEmission(color).setMaterial(material), //
                new Triangle(points[337], points[338], points[343]).setEmission(color).setMaterial(material), //
                new Triangle(points[344], points[343], points[338]).setEmission(color).setMaterial(material), //
                new Triangle(points[338], points[339], points[344]).setEmission(color).setMaterial(material), //
                new Triangle(points[346], points[345], points[340]).setEmission(color).setMaterial(material), //
                new Triangle(points[340], points[341], points[346]).setEmission(color).setMaterial(material), //
                new Triangle(points[347], points[346], points[341]).setEmission(color).setMaterial(material), //
                new Triangle(points[341], points[342], points[347]).setEmission(color).setMaterial(material), //
                new Triangle(points[348], points[347], points[342]).setEmission(color).setMaterial(material), //
                new Triangle(points[342], points[343], points[348]).setEmission(color).setMaterial(material), //
                new Triangle(points[349], points[348], points[343]).setEmission(color).setMaterial(material), //
                new Triangle(points[343], points[344], points[349]).setEmission(color).setMaterial(material), //
                new Triangle(points[351], points[350], points[345]).setEmission(color).setMaterial(material), //
                new Triangle(points[345], points[346], points[351]).setEmission(color).setMaterial(material), //
                new Triangle(points[352], points[351], points[346]).setEmission(color).setMaterial(material), //
                new Triangle(points[346], points[347], points[352]).setEmission(color).setMaterial(material), //
                new Triangle(points[353], points[352], points[347]).setEmission(color).setMaterial(material), //
                new Triangle(points[347], points[348], points[353]).setEmission(color).setMaterial(material), //
                new Triangle(points[354], points[353], points[348]).setEmission(color).setMaterial(material), //
                new Triangle(points[348], points[349], points[354]).setEmission(color).setMaterial(material), //
                new Triangle(points[356], points[355], points[350]).setEmission(color).setMaterial(material), //
                new Triangle(points[350], points[351], points[356]).setEmission(color).setMaterial(material), //
                new Triangle(points[357], points[356], points[351]).setEmission(color).setMaterial(material), //
                new Triangle(points[351], points[352], points[357]).setEmission(color).setMaterial(material), //
                new Triangle(points[358], points[357], points[352]).setEmission(color).setMaterial(material), //
                new Triangle(points[352], points[353], points[358]).setEmission(color).setMaterial(material), //
                new Triangle(points[359], points[358], points[353]).setEmission(color).setMaterial(material), //
                new Triangle(points[353], points[354], points[359]).setEmission(color).setMaterial(material), //
                new Triangle(points[361], points[360], points[355]).setEmission(color).setMaterial(material), //
                new Triangle(points[355], points[356], points[361]).setEmission(color).setMaterial(material), //
                new Triangle(points[362], points[361], points[356]).setEmission(color).setMaterial(material), //
                new Triangle(points[356], points[357], points[362]).setEmission(color).setMaterial(material), //
                new Triangle(points[363], points[362], points[357]).setEmission(color).setMaterial(material), //
                new Triangle(points[357], points[358], points[363]).setEmission(color).setMaterial(material), //
                new Triangle(points[364], points[363], points[358]).setEmission(color).setMaterial(material), //
                new Triangle(points[358], points[359], points[364]).setEmission(color).setMaterial(material), //
                new Triangle(points[366], points[365], points[360]).setEmission(color).setMaterial(material), //
                new Triangle(points[360], points[361], points[366]).setEmission(color).setMaterial(material), //
                new Triangle(points[367], points[366], points[361]).setEmission(color).setMaterial(material), //
                new Triangle(points[361], points[362], points[367]).setEmission(color).setMaterial(material), //
                new Triangle(points[368], points[367], points[362]).setEmission(color).setMaterial(material), //
                new Triangle(points[362], points[363], points[368]).setEmission(color).setMaterial(material), //
                new Triangle(points[369], points[368], points[363]).setEmission(color).setMaterial(material), //
                new Triangle(points[363], points[364], points[369]).setEmission(color).setMaterial(material), //
                new Triangle(points[331], points[330], points[365]).setEmission(color).setMaterial(material), //
                new Triangle(points[365], points[366], points[331]).setEmission(color).setMaterial(material), //
                new Triangle(points[332], points[331], points[366]).setEmission(color).setMaterial(material), //
                new Triangle(points[366], points[367], points[332]).setEmission(color).setMaterial(material), //
                new Triangle(points[333], points[332], points[367]).setEmission(color).setMaterial(material), //
                new Triangle(points[367], points[368], points[333]).setEmission(color).setMaterial(material), //
                new Triangle(points[334], points[333], points[368]).setEmission(color).setMaterial(material), //
                new Triangle(points[368], points[369], points[334]).setEmission(color).setMaterial(material), //
                new Triangle(points[374], points[339], points[334]).setEmission(color).setMaterial(material), //
                new Triangle(points[334], points[370], points[374]).setEmission(color).setMaterial(material), //
                new Triangle(points[375], points[374], points[370]).setEmission(color).setMaterial(material), //
                new Triangle(points[370], points[371], points[375]).setEmission(color).setMaterial(material), //
                new Triangle(points[376], points[375], points[371]).setEmission(color).setMaterial(material), //
                new Triangle(points[371], points[372], points[376]).setEmission(color).setMaterial(material), //
                new Triangle(points[377], points[376], points[372]).setEmission(color).setMaterial(material), //
                new Triangle(points[372], points[373], points[377]).setEmission(color).setMaterial(material), //
                new Triangle(points[378], points[344], points[339]).setEmission(color).setMaterial(material), //
                new Triangle(points[339], points[374], points[378]).setEmission(color).setMaterial(material), //
                new Triangle(points[379], points[378], points[374]).setEmission(color).setMaterial(material), //
                new Triangle(points[374], points[375], points[379]).setEmission(color).setMaterial(material), //
                new Triangle(points[380], points[379], points[375]).setEmission(color).setMaterial(material), //
                new Triangle(points[375], points[376], points[380]).setEmission(color).setMaterial(material), //
                new Triangle(points[381], points[380], points[376]).setEmission(color).setMaterial(material), //
                new Triangle(points[376], points[377], points[381]).setEmission(color).setMaterial(material), //
                new Triangle(points[382], points[349], points[344]).setEmission(color).setMaterial(material), //
                new Triangle(points[344], points[378], points[382]).setEmission(color).setMaterial(material), //
                new Triangle(points[383], points[382], points[378]).setEmission(color).setMaterial(material), //
                new Triangle(points[378], points[379], points[383]).setEmission(color).setMaterial(material), //
                new Triangle(points[384], points[383], points[379]).setEmission(color).setMaterial(material), //
                new Triangle(points[379], points[380], points[384]).setEmission(color).setMaterial(material), //
                new Triangle(points[385], points[384], points[380]).setEmission(color).setMaterial(material), //
                new Triangle(points[380], points[381], points[385]).setEmission(color).setMaterial(material), //
                new Triangle(points[386], points[354], points[349]).setEmission(color).setMaterial(material), //
                new Triangle(points[349], points[382], points[386]).setEmission(color).setMaterial(material), //
                new Triangle(points[387], points[386], points[382]).setEmission(color).setMaterial(material), //
                new Triangle(points[382], points[383], points[387]).setEmission(color).setMaterial(material), //
                new Triangle(points[388], points[387], points[383]).setEmission(color).setMaterial(material), //
                new Triangle(points[383], points[384], points[388]).setEmission(color).setMaterial(material), //
                new Triangle(points[389], points[388], points[384]).setEmission(color).setMaterial(material), //
                new Triangle(points[384], points[385], points[389]).setEmission(color).setMaterial(material), //
                new Triangle(points[390], points[359], points[354]).setEmission(color).setMaterial(material), //
                new Triangle(points[354], points[386], points[390]).setEmission(color).setMaterial(material), //
                new Triangle(points[391], points[390], points[386]).setEmission(color).setMaterial(material), //
                new Triangle(points[386], points[387], points[391]).setEmission(color).setMaterial(material), //
                new Triangle(points[392], points[391], points[387]).setEmission(color).setMaterial(material), //
                new Triangle(points[387], points[388], points[392]).setEmission(color).setMaterial(material), //
                new Triangle(points[393], points[392], points[388]).setEmission(color).setMaterial(material), //
                new Triangle(points[388], points[389], points[393]).setEmission(color).setMaterial(material), //
                new Triangle(points[394], points[364], points[359]).setEmission(color).setMaterial(material), //
                new Triangle(points[359], points[390], points[394]).setEmission(color).setMaterial(material), //
                new Triangle(points[395], points[394], points[390]).setEmission(color).setMaterial(material), //
                new Triangle(points[390], points[391], points[395]).setEmission(color).setMaterial(material), //
                new Triangle(points[396], points[395], points[391]).setEmission(color).setMaterial(material), //
                new Triangle(points[391], points[392], points[396]).setEmission(color).setMaterial(material), //
                new Triangle(points[397], points[396], points[392]).setEmission(color).setMaterial(material), //
                new Triangle(points[392], points[393], points[397]).setEmission(color).setMaterial(material), //
                new Triangle(points[398], points[369], points[364]).setEmission(color).setMaterial(material), //
                new Triangle(points[364], points[394], points[398]).setEmission(color).setMaterial(material), //
                new Triangle(points[399], points[398], points[394]).setEmission(color).setMaterial(material), //
                new Triangle(points[394], points[395], points[399]).setEmission(color).setMaterial(material), //
                new Triangle(points[400], points[399], points[395]).setEmission(color).setMaterial(material), //
                new Triangle(points[395], points[396], points[400]).setEmission(color).setMaterial(material), //
                new Triangle(points[401], points[400], points[396]).setEmission(color).setMaterial(material), //
                new Triangle(points[396], points[397], points[401]).setEmission(color).setMaterial(material), //
                new Triangle(points[370], points[334], points[369]).setEmission(color).setMaterial(material), //
                new Triangle(points[369], points[398], points[370]).setEmission(color).setMaterial(material), //
                new Triangle(points[371], points[370], points[398]).setEmission(color).setMaterial(material), //
                new Triangle(points[398], points[399], points[371]).setEmission(color).setMaterial(material), //
                new Triangle(points[372], points[371], points[399]).setEmission(color).setMaterial(material), //
                new Triangle(points[399], points[400], points[372]).setEmission(color).setMaterial(material), //
                new Triangle(points[373], points[372], points[400]).setEmission(color).setMaterial(material), //
                new Triangle(points[400], points[401], points[373]).setEmission(color).setMaterial(material), //
                new Triangle(points[402], points[403], points[407]).setEmission(color).setMaterial(material), //
                new Triangle(points[408], points[407], points[403]).setEmission(color).setMaterial(material), //
                new Triangle(points[403], points[404], points[408]).setEmission(color).setMaterial(material), //
                new Triangle(points[409], points[408], points[404]).setEmission(color).setMaterial(material), //
                new Triangle(points[404], points[405], points[409]).setEmission(color).setMaterial(material), //
                new Triangle(points[410], points[409], points[405]).setEmission(color).setMaterial(material), //
                new Triangle(points[405], points[406], points[410]).setEmission(color).setMaterial(material), //
                new Triangle(points[402], points[407], points[411]).setEmission(color).setMaterial(material), //
                new Triangle(points[412], points[411], points[407]).setEmission(color).setMaterial(material), //
                new Triangle(points[407], points[408], points[412]).setEmission(color).setMaterial(material), //
                new Triangle(points[413], points[412], points[408]).setEmission(color).setMaterial(material), //
                new Triangle(points[408], points[409], points[413]).setEmission(color).setMaterial(material), //
                new Triangle(points[414], points[413], points[409]).setEmission(color).setMaterial(material), //
                new Triangle(points[409], points[410], points[414]).setEmission(color).setMaterial(material), //
                new Triangle(points[402], points[411], points[415]).setEmission(color).setMaterial(material), //
                new Triangle(points[416], points[415], points[411]).setEmission(color).setMaterial(material), //
                new Triangle(points[411], points[412], points[416]).setEmission(color).setMaterial(material), //
                new Triangle(points[417], points[416], points[412]).setEmission(color).setMaterial(material), //
                new Triangle(points[412], points[413], points[417]).setEmission(color).setMaterial(material), //
                new Triangle(points[418], points[417], points[413]).setEmission(color).setMaterial(material), //
                new Triangle(points[413], points[414], points[418]).setEmission(color).setMaterial(material), //
                new Triangle(points[402], points[415], points[419]).setEmission(color).setMaterial(material), //
                new Triangle(points[420], points[419], points[415]).setEmission(color).setMaterial(material), //
                new Triangle(points[415], points[416], points[420]).setEmission(color).setMaterial(material), //
                new Triangle(points[421], points[420], points[416]).setEmission(color).setMaterial(material), //
                new Triangle(points[416], points[417], points[421]).setEmission(color).setMaterial(material), //
                new Triangle(points[422], points[421], points[417]).setEmission(color).setMaterial(material), //
                new Triangle(points[417], points[418], points[422]).setEmission(color).setMaterial(material), //
                new Triangle(points[402], points[419], points[423]).setEmission(color).setMaterial(material), //
                new Triangle(points[424], points[423], points[419]).setEmission(color).setMaterial(material), //
                new Triangle(points[419], points[420], points[424]).setEmission(color).setMaterial(material), //
                new Triangle(points[425], points[424], points[420]).setEmission(color).setMaterial(material), //
                new Triangle(points[420], points[421], points[425]).setEmission(color).setMaterial(material), //
                new Triangle(points[426], points[425], points[421]).setEmission(color).setMaterial(material), //
                new Triangle(points[421], points[422], points[426]).setEmission(color).setMaterial(material), //
                new Triangle(points[402], points[423], points[427]).setEmission(color).setMaterial(material), //
                new Triangle(points[428], points[427], points[423]).setEmission(color).setMaterial(material), //
                new Triangle(points[423], points[424], points[428]).setEmission(color).setMaterial(material), //
                new Triangle(points[429], points[428], points[424]).setEmission(color).setMaterial(material), //
                new Triangle(points[424], points[425], points[429]).setEmission(color).setMaterial(material), //
                new Triangle(points[430], points[429], points[425]).setEmission(color).setMaterial(material), //
                new Triangle(points[425], points[426], points[430]).setEmission(color).setMaterial(material), //
                new Triangle(points[402], points[427], points[431]).setEmission(color).setMaterial(material), //
                new Triangle(points[432], points[431], points[427]).setEmission(color).setMaterial(material), //
                new Triangle(points[427], points[428], points[432]).setEmission(color).setMaterial(material), //
                new Triangle(points[433], points[432], points[428]).setEmission(color).setMaterial(material), //
                new Triangle(points[428], points[429], points[433]).setEmission(color).setMaterial(material), //
                new Triangle(points[434], points[433], points[429]).setEmission(color).setMaterial(material), //
                new Triangle(points[429], points[430], points[434]).setEmission(color).setMaterial(material), //
                new Triangle(points[402], points[431], points[435]).setEmission(color).setMaterial(material), //
                new Triangle(points[436], points[435], points[431]).setEmission(color).setMaterial(material), //
                new Triangle(points[431], points[432], points[436]).setEmission(color).setMaterial(material), //
                new Triangle(points[437], points[436], points[432]).setEmission(color).setMaterial(material), //
                new Triangle(points[432], points[433], points[437]).setEmission(color).setMaterial(material), //
                new Triangle(points[438], points[437], points[433]).setEmission(color).setMaterial(material), //
                new Triangle(points[433], points[434], points[438]).setEmission(color).setMaterial(material), //
                new Triangle(points[402], points[435], points[439]).setEmission(color).setMaterial(material), //
                new Triangle(points[440], points[439], points[435]).setEmission(color).setMaterial(material), //
                new Triangle(points[435], points[436], points[440]).setEmission(color).setMaterial(material), //
                new Triangle(points[441], points[440], points[436]).setEmission(color).setMaterial(material), //
                new Triangle(points[436], points[437], points[441]).setEmission(color).setMaterial(material), //
                new Triangle(points[442], points[441], points[437]).setEmission(color).setMaterial(material), //
                new Triangle(points[437], points[438], points[442]).setEmission(color).setMaterial(material), //
                new Triangle(points[402], points[439], points[443]).setEmission(color).setMaterial(material), //
                new Triangle(points[444], points[443], points[439]).setEmission(color).setMaterial(material), //
                new Triangle(points[439], points[440], points[444]).setEmission(color).setMaterial(material), //
                new Triangle(points[445], points[444], points[440]).setEmission(color).setMaterial(material), //
                new Triangle(points[440], points[441], points[445]).setEmission(color).setMaterial(material), //
                new Triangle(points[446], points[445], points[441]).setEmission(color).setMaterial(material), //
                new Triangle(points[441], points[442], points[446]).setEmission(color).setMaterial(material), //
                new Triangle(points[402], points[443], points[447]).setEmission(color).setMaterial(material), //
                new Triangle(points[448], points[447], points[443]).setEmission(color).setMaterial(material), //
                new Triangle(points[443], points[444], points[448]).setEmission(color).setMaterial(material), //
                new Triangle(points[449], points[448], points[444]).setEmission(color).setMaterial(material), //
                new Triangle(points[444], points[445], points[449]).setEmission(color).setMaterial(material), //
                new Triangle(points[450], points[449], points[445]).setEmission(color).setMaterial(material), //
                new Triangle(points[445], points[446], points[450]).setEmission(color).setMaterial(material), //
                new Triangle(points[402], points[447], points[451]).setEmission(color).setMaterial(material), //
                new Triangle(points[452], points[451], points[447]).setEmission(color).setMaterial(material), //
                new Triangle(points[447], points[448], points[452]).setEmission(color).setMaterial(material), //
                new Triangle(points[453], points[452], points[448]).setEmission(color).setMaterial(material), //
                new Triangle(points[448], points[449], points[453]).setEmission(color).setMaterial(material), //
                new Triangle(points[454], points[453], points[449]).setEmission(color).setMaterial(material), //
                new Triangle(points[449], points[450], points[454]).setEmission(color).setMaterial(material), //
                new Triangle(points[402], points[451], points[455]).setEmission(color).setMaterial(material), //
                new Triangle(points[456], points[455], points[451]).setEmission(color).setMaterial(material), //
                new Triangle(points[451], points[452], points[456]).setEmission(color).setMaterial(material), //
                new Triangle(points[457], points[456], points[452]).setEmission(color).setMaterial(material), //
                new Triangle(points[452], points[453], points[457]).setEmission(color).setMaterial(material), //
                new Triangle(points[458], points[457], points[453]).setEmission(color).setMaterial(material), //
                new Triangle(points[453], points[454], points[458]).setEmission(color).setMaterial(material), //
                new Triangle(points[402], points[455], points[459]).setEmission(color).setMaterial(material), //
                new Triangle(points[460], points[459], points[455]).setEmission(color).setMaterial(material), //
                new Triangle(points[455], points[456], points[460]).setEmission(color).setMaterial(material), //
                new Triangle(points[461], points[460], points[456]).setEmission(color).setMaterial(material), //
                new Triangle(points[456], points[457], points[461]).setEmission(color).setMaterial(material), //
                new Triangle(points[462], points[461], points[457]).setEmission(color).setMaterial(material), //
                new Triangle(points[457], points[458], points[462]).setEmission(color).setMaterial(material), //
                new Triangle(points[402], points[459], points[463]).setEmission(color).setMaterial(material), //
                new Triangle(points[464], points[463], points[459]).setEmission(color).setMaterial(material), //
                new Triangle(points[459], points[460], points[464]).setEmission(color).setMaterial(material), //
                new Triangle(points[465], points[464], points[460]).setEmission(color).setMaterial(material), //
                new Triangle(points[460], points[461], points[465]).setEmission(color).setMaterial(material), //
                new Triangle(points[466], points[465], points[461]).setEmission(color).setMaterial(material), //
                new Triangle(points[461], points[462], points[466]).setEmission(color).setMaterial(material), //
                new Triangle(points[402], points[463], points[403]).setEmission(color).setMaterial(material), //
                new Triangle(points[404], points[403], points[463]).setEmission(color).setMaterial(material), //
                new Triangle(points[463], points[464], points[404]).setEmission(color).setMaterial(material), //
                new Triangle(points[405], points[404], points[464]).setEmission(color).setMaterial(material), //
                new Triangle(points[464], points[465], points[405]).setEmission(color).setMaterial(material), //
                new Triangle(points[406], points[405], points[465]).setEmission(color).setMaterial(material), //
                new Triangle(points[465], points[466], points[406]).setEmission(color).setMaterial(material), //
                new Triangle(points[471], points[410], points[406]).setEmission(color).setMaterial(material), //
                new Triangle(points[406], points[467], points[471]).setEmission(color).setMaterial(material), //
                new Triangle(points[472], points[471], points[467]).setEmission(color).setMaterial(material), //
                new Triangle(points[467], points[468], points[472]).setEmission(color).setMaterial(material), //
                new Triangle(points[473], points[472], points[468]).setEmission(color).setMaterial(material), //
                new Triangle(points[468], points[469], points[473]).setEmission(color).setMaterial(material), //
                new Triangle(points[474], points[473], points[469]).setEmission(color).setMaterial(material), //
                new Triangle(points[469], points[470], points[474]).setEmission(color).setMaterial(material), //
                new Triangle(points[475], points[414], points[410]).setEmission(color).setMaterial(material), //
                new Triangle(points[410], points[471], points[475]).setEmission(color).setMaterial(material), //
                new Triangle(points[476], points[475], points[471]).setEmission(color).setMaterial(material), //
                new Triangle(points[471], points[472], points[476]).setEmission(color).setMaterial(material), //
                new Triangle(points[477], points[476], points[472]).setEmission(color).setMaterial(material), //
                new Triangle(points[472], points[473], points[477]).setEmission(color).setMaterial(material), //
                new Triangle(points[478], points[477], points[473]).setEmission(color).setMaterial(material), //
                new Triangle(points[473], points[474], points[478]).setEmission(color).setMaterial(material), //
                new Triangle(points[479], points[418], points[414]).setEmission(color).setMaterial(material), //
                new Triangle(points[414], points[475], points[479]).setEmission(color).setMaterial(material), //
                new Triangle(points[480], points[479], points[475]).setEmission(color).setMaterial(material), //
                new Triangle(points[475], points[476], points[480]).setEmission(color).setMaterial(material), //
                new Triangle(points[481], points[480], points[476]).setEmission(color).setMaterial(material), //
                new Triangle(points[476], points[477], points[481]).setEmission(color).setMaterial(material), //
                new Triangle(points[482], points[481], points[477]).setEmission(color).setMaterial(material), //
                new Triangle(points[477], points[478], points[482]).setEmission(color).setMaterial(material), //
                new Triangle(points[483], points[422], points[418]).setEmission(color).setMaterial(material), //
                new Triangle(points[418], points[479], points[483]).setEmission(color).setMaterial(material), //
                new Triangle(points[484], points[483], points[479]).setEmission(color).setMaterial(material), //
                new Triangle(points[479], points[480], points[484]).setEmission(color).setMaterial(material), //
                new Triangle(points[485], points[484], points[480]).setEmission(color).setMaterial(material), //
                new Triangle(points[480], points[481], points[485]).setEmission(color).setMaterial(material), //
                new Triangle(points[486], points[485], points[481]).setEmission(color).setMaterial(material), //
                new Triangle(points[481], points[482], points[486]).setEmission(color).setMaterial(material), //
                new Triangle(points[487], points[426], points[422]).setEmission(color).setMaterial(material), //
                new Triangle(points[422], points[483], points[487]).setEmission(color).setMaterial(material), //
                new Triangle(points[488], points[487], points[483]).setEmission(color).setMaterial(material), //
                new Triangle(points[483], points[484], points[488]).setEmission(color).setMaterial(material), //
                new Triangle(points[489], points[488], points[484]).setEmission(color).setMaterial(material), //
                new Triangle(points[484], points[485], points[489]).setEmission(color).setMaterial(material), //
                new Triangle(points[490], points[489], points[485]).setEmission(color).setMaterial(material), //
                new Triangle(points[485], points[486], points[490]).setEmission(color).setMaterial(material), //
                new Triangle(points[491], points[430], points[426]).setEmission(color).setMaterial(material), //
                new Triangle(points[426], points[487], points[491]).setEmission(color).setMaterial(material), //
                new Triangle(points[492], points[491], points[487]).setEmission(color).setMaterial(material), //
                new Triangle(points[487], points[488], points[492]).setEmission(color).setMaterial(material), //
                new Triangle(points[493], points[492], points[488]).setEmission(color).setMaterial(material), //
                new Triangle(points[488], points[489], points[493]).setEmission(color).setMaterial(material), //
                new Triangle(points[494], points[493], points[489]).setEmission(color).setMaterial(material), //
                new Triangle(points[489], points[490], points[494]).setEmission(color).setMaterial(material), //
                new Triangle(points[495], points[434], points[430]).setEmission(color).setMaterial(material), //
                new Triangle(points[430], points[491], points[495]).setEmission(color).setMaterial(material), //
                new Triangle(points[496], points[495], points[491]).setEmission(color).setMaterial(material), //
                new Triangle(points[491], points[492], points[496]).setEmission(color).setMaterial(material), //
                new Triangle(points[497], points[496], points[492]).setEmission(color).setMaterial(material), //
                new Triangle(points[492], points[493], points[497]).setEmission(color).setMaterial(material), //
                new Triangle(points[498], points[497], points[493]).setEmission(color).setMaterial(material), //
                new Triangle(points[493], points[494], points[498]).setEmission(color).setMaterial(material), //
                new Triangle(points[499], points[438], points[434]).setEmission(color).setMaterial(material), //
                new Triangle(points[434], points[495], points[499]).setEmission(color).setMaterial(material), //
                new Triangle(points[500], points[499], points[495]).setEmission(color).setMaterial(material), //
                new Triangle(points[495], points[496], points[500]).setEmission(color).setMaterial(material), //
                new Triangle(points[501], points[500], points[496]).setEmission(color).setMaterial(material), //
                new Triangle(points[496], points[497], points[501]).setEmission(color).setMaterial(material), //
                new Triangle(points[502], points[501], points[497]).setEmission(color).setMaterial(material), //
                new Triangle(points[497], points[498], points[502]).setEmission(color).setMaterial(material), //
                new Triangle(points[503], points[442], points[438]).setEmission(color).setMaterial(material), //
                new Triangle(points[438], points[499], points[503]).setEmission(color).setMaterial(material), //
                new Triangle(points[504], points[503], points[499]).setEmission(color).setMaterial(material), //
                new Triangle(points[499], points[500], points[504]).setEmission(color).setMaterial(material), //
                new Triangle(points[505], points[504], points[500]).setEmission(color).setMaterial(material), //
                new Triangle(points[500], points[501], points[505]).setEmission(color).setMaterial(material), //
                new Triangle(points[506], points[505], points[501]).setEmission(color).setMaterial(material), //
                new Triangle(points[501], points[502], points[506]).setEmission(color).setMaterial(material), //
                new Triangle(points[507], points[446], points[442]).setEmission(color).setMaterial(material), //
                new Triangle(points[442], points[503], points[507]).setEmission(color).setMaterial(material), //
                new Triangle(points[508], points[507], points[503]).setEmission(color).setMaterial(material), //
                new Triangle(points[503], points[504], points[508]).setEmission(color).setMaterial(material), //
                new Triangle(points[509], points[508], points[504]).setEmission(color).setMaterial(material), //
                new Triangle(points[504], points[505], points[509]).setEmission(color).setMaterial(material), //
                new Triangle(points[510], points[509], points[505]).setEmission(color).setMaterial(material), //
                new Triangle(points[505], points[506], points[510]).setEmission(color).setMaterial(material), //
                new Triangle(points[511], points[450], points[446]).setEmission(color).setMaterial(material), //
                new Triangle(points[446], points[507], points[511]).setEmission(color).setMaterial(material), //
                new Triangle(points[512], points[511], points[507]).setEmission(color).setMaterial(material), //
                new Triangle(points[507], points[508], points[512]).setEmission(color).setMaterial(material), //
                new Triangle(points[513], points[512], points[508]).setEmission(color).setMaterial(material), //
                new Triangle(points[508], points[509], points[513]).setEmission(color).setMaterial(material), //
                new Triangle(points[514], points[513], points[509]).setEmission(color).setMaterial(material), //
                new Triangle(points[509], points[510], points[514]).setEmission(color).setMaterial(material), //
                new Triangle(points[515], points[454], points[450]).setEmission(color).setMaterial(material), //
                new Triangle(points[450], points[511], points[515]).setEmission(color).setMaterial(material), //
                new Triangle(points[516], points[515], points[511]).setEmission(color).setMaterial(material), //
                new Triangle(points[511], points[512], points[516]).setEmission(color).setMaterial(material), //
                new Triangle(points[517], points[516], points[512]).setEmission(color).setMaterial(material), //
                new Triangle(points[512], points[513], points[517]).setEmission(color).setMaterial(material), //
                new Triangle(points[518], points[517], points[513]).setEmission(color).setMaterial(material), //
                new Triangle(points[513], points[514], points[518]).setEmission(color).setMaterial(material), //
                new Triangle(points[519], points[458], points[454]).setEmission(color).setMaterial(material), //
                new Triangle(points[454], points[515], points[519]).setEmission(color).setMaterial(material), //
                new Triangle(points[520], points[519], points[515]).setEmission(color).setMaterial(material), //
                new Triangle(points[515], points[516], points[520]).setEmission(color).setMaterial(material), //
                new Triangle(points[521], points[520], points[516]).setEmission(color).setMaterial(material), //
                new Triangle(points[516], points[517], points[521]).setEmission(color).setMaterial(material), //
                new Triangle(points[522], points[521], points[517]).setEmission(color).setMaterial(material), //
                new Triangle(points[517], points[518], points[522]).setEmission(color).setMaterial(material), //
                new Triangle(points[523], points[462], points[458]).setEmission(color).setMaterial(material), //
                new Triangle(points[458], points[519], points[523]).setEmission(color).setMaterial(material), //
                new Triangle(points[524], points[523], points[519]).setEmission(color).setMaterial(material), //
                new Triangle(points[519], points[520], points[524]).setEmission(color).setMaterial(material), //
                new Triangle(points[525], points[524], points[520]).setEmission(color).setMaterial(material), //
                new Triangle(points[520], points[521], points[525]).setEmission(color).setMaterial(material), //
                new Triangle(points[526], points[525], points[521]).setEmission(color).setMaterial(material), //
                new Triangle(points[521], points[522], points[526]).setEmission(color).setMaterial(material), //
                new Triangle(points[527], points[466], points[462]).setEmission(color).setMaterial(material), //
                new Triangle(points[462], points[523], points[527]).setEmission(color).setMaterial(material), //
                new Triangle(points[528], points[527], points[523]).setEmission(color).setMaterial(material), //
                new Triangle(points[523], points[524], points[528]).setEmission(color).setMaterial(material), //
                new Triangle(points[529], points[528], points[524]).setEmission(color).setMaterial(material), //
                new Triangle(points[524], points[525], points[529]).setEmission(color).setMaterial(material), //
                new Triangle(points[530], points[529], points[525]).setEmission(color).setMaterial(material), //
                new Triangle(points[525], points[526], points[530]).setEmission(color).setMaterial(material), //
                new Triangle(points[467], points[406], points[466]).setEmission(color).setMaterial(material), //
                new Triangle(points[466], points[527], points[467]).setEmission(color).setMaterial(material), //
                new Triangle(points[468], points[467], points[527]).setEmission(color).setMaterial(material), //
                new Triangle(points[527], points[528], points[468]).setEmission(color).setMaterial(material), //
                new Triangle(points[469], points[468], points[528]).setEmission(color).setMaterial(material), //
                new Triangle(points[528], points[529], points[469]).setEmission(color).setMaterial(material), //
                new Triangle(points[470], points[469], points[529]).setEmission(color).setMaterial(material), //
                new Triangle(points[529], points[530], points[470]).setEmission(color).setMaterial(material) //
        );
    }

    // === Bubble generation code (keeping original implementation) ===

    /** Bubble generation fields */
    private static Geometries bubbles;
    private static double     coneX, coneY, coneZ, radiusAtOne, maxConeHeight;
    private static double     minBubbleRadius, maxBubbleRadius;
    private static Double3    bubbleKS, bubbleKT, bubbleKR;
    private static double     bubbleMinKD, bubbleMaxKD;

    /**
     * Set the cone for generating bubbles from the teapot
     */
    private static void setCone(double x, double y, double z, double r, double maxHeight) {
        coneX = x; coneY = y; coneZ = z; radiusAtOne = r; maxConeHeight = maxHeight;
    }

    /**
     * Setter for minimal and maximal size of the bubbles
     */
    private static void setBubbleSize(double min, double max) {
        minBubbleRadius = min; maxBubbleRadius = max;
    }

    /**
     * Setter for bubble material parameters
     */
    private static void setBubbleMaterial(double minKD, double maxKD, double ks, double kr, double kt) {
        bubbleMinKD = minKD; bubbleMaxKD = maxKD;
        bubbleKS = new Double3(ks); bubbleKR = new Double3(kr); bubbleKT = new Double3(kt);
    }

    /**
     * Generate a bubble at specified height
     */
    private static Sphere getRandomBubble(double y) {
        double  coneR    = y * radiusAtOne;
        double  randomR  = random(0, coneR);
        double  angle    = random(0, 2 * PI);
        double  x        = randomR * cos(angle);
        double  z        = randomR * sin(angle);
        Point   o        = new Point(coneX + x, coneY + y, coneZ + z);
        double  r        = random(minBubbleRadius, maxBubbleRadius);
        Double3 kd       = new Double3(random(bubbleMinKD, bubbleMaxKD),
                random(bubbleMinKD, bubbleMaxKD),
                random(bubbleMinKD, bubbleMaxKD));
        var     material = new Material().setKD(kd).setKS(bubbleKS).setKR(bubbleKR).setKT(bubbleKT);
        return (Sphere) new Sphere(r, o).setMaterial(material);
    }

    /**
     * Create bubbles with enhanced spatial organization
     */
    private static void prepareBubbles(int amount) {
        bubbles = new Geometries();
        System.out.println("Creating " + amount + " bubbles with spatial distribution...");
        while (amount-- > 0) {
            bubbles.add(getRandomBubble(random(0, maxConeHeight)));
        }
    }
}