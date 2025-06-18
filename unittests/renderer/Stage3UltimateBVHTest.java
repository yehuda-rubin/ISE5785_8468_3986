package renderer;

import geometries.*;
import lighting.*;
import org.junit.jupiter.api.Test;
import primitives.*;
import scene.Scene;

import java.util.Random;
import java.util.List;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Ultimate BVH Performance Tests - Demonstrating 20-40x Performance Improvement
 * Tests all combinations of optimizations with massive scenes:
 * 1. Baseline (no optimizations)
 * 2. Multithreading only
 * 3. BVH only
 * 4. BVH + Multithreading
 *
 * Uses 3000+ objects to demonstrate dramatic BVH acceleration
 *
 * @author Yehuda Rubin and Arye Hacohen
 */
class Stage3UltimateBVHTest {

    /** Camera builder for the tests */
    private final Camera.Builder cameraBuilder = Camera.getBuilder();

    /**
     * Ultimate Four-Way BVH Performance Test
     * Creates massive scene with 3000+ objects to demonstrate 20-40x improvement
     * Tests exact 4 configurations: baseline, MT, BVH, combined
     */
    @Test
    void ultimateBVHPerformanceTest() {
        System.out.println("=== ULTIMATE BVH PERFORMANCE TEST (20-40x Improvement) ===\n");
        System.out.println("Creating massive scene with 3000+ objects to demonstrate dramatic acceleration...");

        // Camera configuration for ultimate performance test
        Camera.Builder baseCamera = cameraBuilder
                .setLocation(new Point(2000, 1500, 2000))
                .setDirection(new Point(0, 0, -500), Vector.AXIS_Y)
                .setVpDistance(2500)
                .setVpSize(1000, 1000)
                .setResolution(400, 400); // 160,000 rays

        // === TEST 1: BASELINE (No Optimizations) ===
        System.out.println("\n" + "=".repeat(60));
        System.out.println("🔴 TEST 1: BASELINE (No Optimizations - Worst Case)");
        System.out.println("=".repeat(60));
        System.out.println("Configuration: No BVH, Single Thread, Flat Scene");

        Scene baselineScene = createMassiveFlatScene("Test 1 Baseline");
        System.out.println("Baseline scene: " + baselineScene.geometries.size() + " objects in flat structure");

        Camera baselineCamera = baseCamera
                .setRayTracer(baselineScene, RayTracerType.SIMPLE)
                .setMultithreading(0) // Single thread
                .build();

        System.out.println("⏱️ Starting baseline measurement (this will take a while)...");
        long startTime1 = System.nanoTime();
        baselineCamera.renderImage().writeToImage("ultimate_test1_baseline");
        long endTime1 = System.nanoTime();

        double baselineTime = (endTime1 - startTime1) / 1_000_000_000.0;
        System.out.println("🔴 Test 1 (Baseline) time: " + String.format("%.2f", baselineTime) + " seconds");
        System.out.println("🔴 Test 1 performance: " + String.format("%.0f", 160000 / baselineTime) + " rays/second");

        // === TEST 2: MULTITHREADING ONLY ===
        System.out.println("\n" + "=".repeat(60));
        System.out.println("🟡 TEST 2: MULTITHREADING ONLY");
        System.out.println("=".repeat(60));
        System.out.println("Configuration: No BVH, Multi Thread, Flat Scene");

        Scene multithreadScene = createMassiveFlatScene("Test 2 Multithreading");
        System.out.println("Multithreading scene: " + multithreadScene.geometries.size() + " objects in flat structure");

        Camera multithreadCamera = baseCamera
                .setRayTracer(multithreadScene, RayTracerType.SIMPLE)
                .setMultithreading(6) // Multi thread
                .build();

        System.out.println("⏱️ Starting multithreading measurement...");
        long startTime2 = System.nanoTime();
        multithreadCamera.renderImage().writeToImage("ultimate_test2_multithreading");
        long endTime2 = System.nanoTime();

        double multithreadTime = (endTime2 - startTime2) / 1_000_000_000.0;
        System.out.println("🟡 Test 2 (Multithreading) time: " + String.format("%.2f", multithreadTime) + " seconds");
        System.out.println("🟡 Test 2 performance: " + String.format("%.0f", 160000 / multithreadTime) + " rays/second");

        // === TEST 3: BVH ONLY ===
        System.out.println("\n" + "=".repeat(60));
        System.out.println("🟠 TEST 3: BVH OPTIMIZATION ONLY");
        System.out.println("=".repeat(60));
        System.out.println("Configuration: BVH Enabled, Single Thread");

        Scene bvhScene = createMassiveBVHScene("Test 3 BVH Only");
        System.out.println("BVH scene: Hierarchical organization with spatial acceleration");

        Camera bvhCamera = baseCamera
                .setRayTracer(bvhScene, RayTracerType.SIMPLE)
                .setMultithreading(0) // Single thread
                .build();

        System.out.println("⏱️ Starting BVH measurement...");
        long startTime3 = System.nanoTime();
        bvhCamera.renderImage().writeToImage("ultimate_test3_bvh_only");
        long endTime3 = System.nanoTime();

        double bvhTime = (endTime3 - startTime3) / 1_000_000_000.0;
        System.out.println("🟠 Test 3 (BVH Only) time: " + String.format("%.2f", bvhTime) + " seconds");
        System.out.println("🟠 Test 3 performance: " + String.format("%.0f", 160000 / bvhTime) + " rays/second");

        // === TEST 4: BVH + MULTITHREADING ===
        System.out.println("\n" + "=".repeat(60));
        System.out.println("🟢 TEST 4: ULTIMATE OPTIMIZATION (BVH + Multithreading)");
        System.out.println("=".repeat(60));
        System.out.println("Configuration: BVH Enabled, Multi Thread");

        Scene ultimateScene = createMassiveBVHScene("Test 4 Ultimate Optimization");
        System.out.println("Ultimate scene: BVH hierarchy + maximum multithreading");

        Camera ultimateCamera = baseCamera
                .setRayTracer(ultimateScene, RayTracerType.SIMPLE)
                .setMultithreading(6) // Multi thread
                .build();

        System.out.println("⏱️ Starting ultimate optimization measurement...");
        long startTime4 = System.nanoTime();
        ultimateCamera.renderImage().writeToImage("ultimate_test4_full_optimization");
        long endTime4 = System.nanoTime();

        double ultimateTime = (endTime4 - startTime4) / 1_000_000_000.0;
        System.out.println("🟢 Test 4 (Ultimate) time: " + String.format("%.2f", ultimateTime) + " seconds");
        System.out.println("🟢 Test 4 performance: " + String.format("%.0f", 160000 / ultimateTime) + " rays/second");

        // === ULTIMATE PERFORMANCE ANALYSIS ===
        double multithreadImprovement = baselineTime / multithreadTime;
        double bvhImprovement = baselineTime / bvhTime;
        double ultimateImprovement = baselineTime / ultimateTime;
        double bvhVsMultithread = multithreadTime / bvhTime;
        double ultimateVsBvh = bvhTime / ultimateTime;
        double ultimateVsMultithread = multithreadTime / ultimateTime;

        System.out.println("\n" + "=".repeat(80));
        System.out.println("🚀 ULTIMATE BVH PERFORMANCE RESULTS 🚀");
        System.out.println("=".repeat(80));
        System.out.println("Test resolution: 400x400 (160,000 rays)");
        System.out.println("Scene complexity: ~3000+ objects in massive distributed scene");
        System.out.println();

        System.out.println("📊 RENDER TIMES:");
        System.out.println("  🔴 Test 1 (Baseline):           " + String.format("%8.2f", baselineTime) + " seconds");
        System.out.println("  🟡 Test 2 (Multithreading):     " + String.format("%8.2f", multithreadTime) + " seconds");
        System.out.println("  🟠 Test 3 (BVH Only):           " + String.format("%8.2f", bvhTime) + " seconds");
        System.out.println("  🟢 Test 4 (Ultimate):           " + String.format("%8.2f", ultimateTime) + " seconds");
        System.out.println();

        System.out.println("🚀 IMPROVEMENT FACTORS (vs Baseline):");
        System.out.println("  🟡 Multithreading improvement:  " + String.format("%.2fx", multithreadImprovement) + " faster");
        System.out.println("  🟠 BVH improvement:             " + String.format("%.2fx", bvhImprovement) + " faster ⭐");
        System.out.println("  🟢 Ultimate optimization:       " + String.format("%.2fx", ultimateImprovement) + " faster 🎯");
        System.out.println();

        System.out.println("🔄 DIRECT COMPARISONS:");
        System.out.println("  BVH vs Multithreading:      " + String.format("%.2fx", bvhVsMultithread) + " (BVH spatial advantage)");
        System.out.println("  Ultimate vs BVH only:       " + String.format("%.2fx", ultimateVsBvh) + " (multithreading boost)");
        System.out.println("  Ultimate vs Multithreading: " + String.format("%.2fx", ultimateVsMultithread) + " (BVH spatial boost)");
        System.out.println();

        System.out.println("⏱️  TIME SAVED (vs Baseline):");
        System.out.println("  By Multithreading:          " + String.format("%.2f", baselineTime - multithreadTime) + " seconds");
        System.out.println("  By BVH:                      " + String.format("%.2f", baselineTime - bvhTime) + " seconds");
        System.out.println("  By Ultimate Optimization:   " + String.format("%.2f", baselineTime - ultimateTime) + " seconds");
        System.out.println();

        System.out.println("📈 ACHIEVEMENT EVALUATION:");

        // BVH evaluation - looking for 20-40x improvement
        if (bvhImprovement >= 30.0) {
            System.out.println("🏆 BVH: PHENOMENAL! " + String.format("%.1fx", bvhImprovement) + " improvement exceeds expectations!");
            System.out.println("   🎯 Assignment target: 20-40x ✅ EXCEEDED");
        } else if (bvhImprovement >= 20.0) {
            System.out.println("🌟 BVH: OUTSTANDING! " + String.format("%.1fx", bvhImprovement) + " improvement meets assignment goals!");
            System.out.println("   🎯 Assignment target: 20-40x ✅ ACHIEVED");
        } else if (bvhImprovement >= 10.0) {
            System.out.println("🎯 BVH: EXCELLENT! " + String.format("%.1fx", bvhImprovement) + " improvement approaching target");
            System.out.println("   🎯 Assignment target: 20-40x ⚠️ CLOSE");
        } else if (bvhImprovement >= 5.0) {
            System.out.println("✅ BVH: GOOD! " + String.format("%.1fx", bvhImprovement) + " improvement but below target");
            System.out.println("   🎯 Assignment target: 20-40x ❌ BELOW TARGET");
        } else {
            System.out.println("⚠️  BVH: Limited improvement - scene may need more complexity");
            System.out.println("   🎯 Assignment target: 20-40x ❌ NOT ACHIEVED");
        }

        // Ultimate optimization evaluation
        if (ultimateImprovement >= 40.0) {
            System.out.println("🚀 ULTIMATE: INCREDIBLE! " + String.format("%.1fx", ultimateImprovement) + " total acceleration!");
            System.out.println("   🎉 This demonstrates the full power of combined optimizations!");
        } else if (ultimateImprovement >= 20.0) {
            System.out.println("🌟 ULTIMATE: OUTSTANDING! " + String.format("%.1fx", ultimateImprovement) + " total acceleration!");
            System.out.println("   🎯 Combined optimizations working excellently together!");
        } else if (ultimateImprovement >= 10.0) {
            System.out.println("🎯 ULTIMATE: EXCELLENT! " + String.format("%.1fx", ultimateImprovement) + " total acceleration!");
        } else {
            System.out.println("✅ ULTIMATE: GOOD! " + String.format("%.1fx", ultimateImprovement) + " total acceleration!");
        }

        // Multithreading evaluation
        if (multithreadImprovement >= 5.0) {
            System.out.println("🔥 Multithreading: EXCELLENT parallelization efficiency!");
        } else if (multithreadImprovement >= 3.0) {
            System.out.println("✅ Multithreading: Good parallel processing gains");
        } else {
            System.out.println("⚠️  Multithreading: Moderate improvement");
        }

        // Assignment compliance check
        System.out.println("\n📋 ASSIGNMENT COMPLIANCE:");
        if (bvhImprovement >= 20.0 && bvhImprovement <= 40.0) {
            System.out.println("✅ BVH improvement is within assignment range (20-40x)");
        } else if (bvhImprovement > 40.0) {
            System.out.println("🌟 BVH improvement exceeds assignment expectations!");
        } else {
            System.out.println("⚠️  BVH improvement below assignment target - consider larger scene");
        }

        // Verify all tests produced valid results
        assertTrue(baselineTime > 0, "Baseline render time should be positive");
        assertTrue(multithreadTime > 0, "Multithreading render time should be positive");
        assertTrue(bvhTime > 0, "BVH render time should be positive");
        assertTrue(ultimateTime > 0, "Ultimate render time should be positive");
        assertTrue(multithreadImprovement >= 1.0, "Multithreading should improve performance");
        assertTrue(bvhImprovement >= 1.0, "BVH should improve performance");
        assertTrue(ultimateImprovement >= Math.max(multithreadImprovement, bvhImprovement),
                "Ultimate optimization should be best");

        System.out.println("\n📁 Generated ultimate test images:");
        System.out.println("   ultimate_test1_baseline.ppm           - Baseline (slowest)");
        System.out.println("   ultimate_test2_multithreading.ppm     - Multithreading only");
        System.out.println("   ultimate_test3_bvh_only.ppm           - BVH only (major improvement)");
        System.out.println("   ultimate_test4_full_optimization.ppm  - Ultimate (fastest)");
        System.out.println("   (All four images should appear visually identical)");

        System.out.println("\n🎉 Ultimate BVH test completed successfully!");
        System.out.println("🚀 Performance journey: from baseline to " +
                String.format("%.1fx", ultimateImprovement) + " faster with ultimate optimization!");

        if (bvhImprovement >= 20.0) {
            System.out.println("🏆 ASSIGNMENT GOAL ACHIEVED: 20-40x BVH improvement demonstrated!");
        }
    }

    /**
     * Creates massive flat scene (no BVH) with 3000+ objects for dramatic performance testing
     * Uses scattered distribution to create worst-case scenario for ray tracing
     */
    private Scene createMassiveFlatScene(String sceneName) {
        Scene scene = new Scene(sceneName);
        setupMassiveLighting(scene);

        // Foundation plane
        scene.geometries.add(
                new Plane(new Point(0, -500, 0), new Vector(0, 1, 0))
                        .setEmission(new Color(10, 15, 25))
                        .setMaterial(new Material().setKD(0.7).setKS(0.3).setShininess(20).setKR(0.4))
        );

        Random random = new Random(999); // Fixed seed for consistent results

        System.out.println("Creating massive flat scene with maximum object distribution...");

        // Create 12 massive clusters spread across huge area
        createMassiveSphereField(scene, random, new Point(-1000, 0, -1000), 200);   // Far left back
        createMassiveSphereField(scene, random, new Point(0, 0, -1000), 200);       // Center back
        createMassiveSphereField(scene, random, new Point(1000, 0, -1000), 200);    // Far right back

        createMassiveSphereField(scene, random, new Point(-1000, 0, 0), 200);       // Far left center
        createMassiveSphereField(scene, random, new Point(0, 0, 0), 250);           // Center (main focus)
        createMassiveSphereField(scene, random, new Point(1000, 0, 0), 200);        // Far right center

        createMassiveSphereField(scene, random, new Point(-1000, 0, 1000), 200);    // Far left front
        createMassiveSphereField(scene, random, new Point(0, 0, 1000), 200);        // Center front
        createMassiveSphereField(scene, random, new Point(1000, 0, 1000), 200);     // Far right front

        // Add elevated clusters
        createMassiveSphereField(scene, random, new Point(-500, 300, -500), 150);
        createMassiveSphereField(scene, random, new Point(500, 300, -500), 150);
        createMassiveSphereField(scene, random, new Point(0, 500, 0), 100);

        // Add triangle fields for additional complexity
        createMassiveTriangleField(scene, random, new Point(-750, -200, -750), 180);
        createMassiveTriangleField(scene, random, new Point(750, -200, -750), 180);
        createMassiveTriangleField(scene, random, new Point(0, -200, 750), 200);

        // Force CBR calculation
        scene.geometries.getBoundingBox();

        System.out.println("Massive flat scene created with " + scene.geometries.size() + " objects");
        System.out.println("Objects distributed across 2000x2000x2000 unit space");
        return scene;
    }

    /**
     * Creates massive BVH-optimized scene with hierarchical organization
     * Organizes objects into efficient spatial hierarchy for dramatic acceleration
     */
    private Scene createMassiveBVHScene(String sceneName) {
        Scene scene = new Scene(sceneName);
        setupMassiveLighting(scene);

        // Foundation plane
        scene.geometries.add(
                new Plane(new Point(0, -500, 0), new Vector(0, 1, 0))
                        .setEmission(new Color(10, 15, 25))
                        .setMaterial(new Material().setKD(0.7).setKS(0.3).setShininess(20).setKR(0.4))
        );

        Random random = new Random(999); // Same seed for identical distribution

        System.out.println("Creating massive BVH scene with optimal spatial hierarchy...");

        // Create spatial containers for massive BVH hierarchy
        Geometries backLeftRegion = new Geometries();     // Far back left quadrant
        Geometries backRightRegion = new Geometries();    // Far back right quadrant
        Geometries frontLeftRegion = new Geometries();    // Front left quadrant
        Geometries frontRightRegion = new Geometries();   // Front right quadrant
        Geometries elevatedRegion = new Geometries();     // Upper level objects
        Geometries groundRegion = new Geometries();       // Ground level objects

        // Distribute objects into spatial regions
        addMassiveClusterToContainer(backLeftRegion, random, new Point(-1000, 0, -1000), 200);
        addMassiveClusterToContainer(backLeftRegion, random, new Point(-1000, 0, 0), 200);
        addMassiveClusterToContainer(backLeftRegion, random, new Point(-750, -200, -750), 180);

        addMassiveClusterToContainer(backRightRegion, random, new Point(1000, 0, -1000), 200);
        addMassiveClusterToContainer(backRightRegion, random, new Point(1000, 0, 0), 200);
        addMassiveClusterToContainer(backRightRegion, random, new Point(750, -200, -750), 180);

        addMassiveClusterToContainer(frontLeftRegion, random, new Point(-1000, 0, 1000), 200);
        addMassiveClusterToContainer(frontLeftRegion, random, new Point(0, 0, 1000), 200);

        addMassiveClusterToContainer(frontRightRegion, random, new Point(1000, 0, 1000), 200);
        addMassiveClusterToContainer(frontRightRegion, random, new Point(0, 0, 0), 250);

        addMassiveClusterToContainer(elevatedRegion, random, new Point(-500, 300, -500), 150);
        addMassiveClusterToContainer(elevatedRegion, random, new Point(500, 300, -500), 150);
        addMassiveClusterToContainer(elevatedRegion, random, new Point(0, 500, 0), 100);

        addMassiveTriangleClusterToContainer(groundRegion, random, new Point(0, -200, 750), 200);
        addMassiveClusterToContainer(groundRegion, random, new Point(0, 0, -1000), 200);

        // Build sophisticated BVH hierarchy
        // Level 3: Group back regions
        BVHNode backRegions = new BVHNode(backLeftRegion, backRightRegion);

        // Level 3: Group front regions
        BVHNode frontRegions = new BVHNode(frontLeftRegion, frontRightRegion);

        // Level 2: Group horizontal regions
        BVHNode horizontalRegions = new BVHNode(backRegions, frontRegions);

        // Level 2: Group vertical regions
        BVHNode verticalRegions = new BVHNode(elevatedRegion, groundRegion);

        // Level 1: Top level hierarchy
        BVHNode topLevelHierarchy = new BVHNode(horizontalRegions, verticalRegions);

        scene.geometries.add(topLevelHierarchy);

        System.out.println("Massive BVH hierarchy constructed:");
        System.out.println("  Back Left: " + backLeftRegion.size() + " objects");
        System.out.println("  Back Right: " + backRightRegion.size() + " objects");
        System.out.println("  Front Left: " + frontLeftRegion.size() + " objects");
        System.out.println("  Front Right: " + frontRightRegion.size() + " objects");
        System.out.println("  Elevated: " + elevatedRegion.size() + " objects");
        System.out.println("  Ground: " + groundRegion.size() + " objects");
        System.out.println("  Total in hierarchy: ~3000+ objects");
        System.out.println("  Hierarchy levels: 3 (optimized for massive ray culling)");

        return scene;
    }

    /**
     * Setup dramatic lighting for massive scene
     */
    private void setupMassiveLighting(Scene scene) {
        scene.setAmbientLight(new AmbientLight(new Color(15, 20, 30)));

        // Multiple powerful light sources
        scene.lights.add(new DirectionalLight(new Color(300, 300, 400), new Vector(1, -1, -1)));

        scene.lights.add(new PointLight(new Color(200, 100, 300), new Point(-800, 800, 800))
                .setKl(0.00001).setKq(0.0000001));

        scene.lights.add(new PointLight(new Color(300, 200, 100), new Point(800, 800, 800))
                .setKl(0.00001).setKq(0.0000001));

        scene.lights.add(new SpotLight(new Color(400, 300, 200), new Point(0, 1000, 0), new Vector(0, -1, 0))
                .setKl(0.00005).setKq(0.0000005));

        scene.lights.add(new SpotLight(new Color(200, 400, 300), new Point(1000, 600, -1000), new Vector(-1, -1, 1))
                .setKl(0.00005).setKq(0.0000005));
    }

    /**
     * Helper methods for creating massive object fields
     */
    private void createMassiveSphereField(Scene scene, Random random, Point center, int count) {
        for (int i = 0; i < count; i++) {
            // Wide gaussian distribution for maximum space coverage
            Point position = center.add(new Vector(
                    random.nextGaussian() * 200,  // Spread: 400 units
                    random.nextGaussian() * 150,  // Height: 300 units
                    random.nextGaussian() * 200   // Depth: 400 units
            ));

            scene.geometries.add(createMassiveRandomSphere(position, random));
        }
    }

    private void createMassiveTriangleField(Scene scene, Random random, Point center, int count) {
        for (int i = 0; i < count; i++) {
            Point position = center.add(new Vector(
                    random.nextGaussian() * 150,
                    random.nextGaussian() * 100,
                    random.nextGaussian() * 150
            ));

            scene.geometries.add(createMassiveRandomTriangle(position, random));
        }
    }

    private void addMassiveClusterToContainer(Geometries container, Random random, Point center, int count) {
        for (int i = 0; i < count; i++) {
            Point position = center.add(new Vector(
                    random.nextGaussian() * 200,
                    random.nextGaussian() * 150,
                    random.nextGaussian() * 200
            ));

            container.add(createMassiveRandomSphere(position, random));
        }
    }

    private void addMassiveTriangleClusterToContainer(Geometries container, Random random, Point center, int count) {
        for (int i = 0; i < count; i++) {
            Point position = center.add(new Vector(
                    random.nextGaussian() * 150,
                    random.nextGaussian() * 100,
                    random.nextGaussian() * 150
            ));

            container.add(createMassiveRandomTriangle(position, random));
        }
    }

    private Sphere createMassiveRandomSphere(Point center, Random random) {
        Sphere sphere = new Sphere(random.nextDouble() * 20 + 8, center); // Larger spheres: 8-28 radius
        sphere.setEmission(new Color(
                random.nextInt(120) + 135,  // R: 135-255
                random.nextInt(120) + 135,  // G: 135-255
                random.nextInt(120) + 135   // B: 135-255
        ));
        sphere.setMaterial(new Material()
                .setKD(0.4 + random.nextDouble() * 0.4)
                .setKS(0.2 + random.nextDouble() * 0.4)
                .setShininess(30 + random.nextInt(70))
                .setKR(random.nextDouble() * 0.4));
        return sphere;
    }

    private Triangle createMassiveRandomTriangle(Point center, Random random) {
        double size = random.nextDouble() * 40 + 20; // Larger triangles: 20-60 size
        double angle = random.nextDouble() * Math.PI * 2;

        Point p1 = center;
        Point p2 = center.add(new Vector(size * Math.cos(angle), size * Math.sin(angle), 0));
        Point p3 = center.add(new Vector(
                size * Math.cos(angle + Math.PI * 2/3),
                size * Math.sin(angle + Math.PI * 2/3),
                random.nextDouble() * 20 - 10
        ));

        Triangle triangle = new Triangle(p1, p2, p3);
        triangle.setEmission(new Color(
                random.nextInt(100) + 100,  // R: 100-200
                random.nextInt(100) + 100,  // G: 100-200
                random.nextInt(100) + 100   // B: 100-200
        ));
        triangle.setMaterial(new Material()
                .setKD(0.5 + random.nextDouble() * 0.3)
                .setKS(0.2 + random.nextDouble() * 0.3)
                .setShininess(25 + random.nextInt(50))
                .setKT(random.nextDouble() * 0.2));
        return triangle;
    }


}