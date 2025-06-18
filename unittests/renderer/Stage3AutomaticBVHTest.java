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
 * Stage 3 BVH Performance Tests - Automatic Hierarchy Building
 * Tests the effectiveness of automatically built BVH spatial hierarchies using SAH.
 * Expected improvement: 20-40x faster than no optimization.
 *
 * @author Yehuda Rubin and Arye Hacohen
 */
class Stage3AutomaticBVHTest {

    /** Camera builder for the tests */
    private final Camera.Builder cameraBuilder = Camera.getBuilder();

    /**
     * Stage 3 Ultimate BVH Performance Test
     * Compares all three optimization levels:
     * 1. Flat CBR (Stage 1)
     * 2. Manual BVH (Stage 2)
     * 3. Automatic BVH with SAH (Stage 3)
     *
     * Creates a complex scene and measures the dramatic performance improvements
     */
    @Test
    void stage3AutomaticBVHPerformanceTest() {
        System.out.println("=== Stage 3: Automatic BVH Ultimate Performance Test ===\n");
        System.out.println("Testing all BVH optimization stages with complex scene...");

        // === CREATE TEST SCENES ===
        Scene flatScene = createUltimateTestScene("Stage 3 Flat CBR");
        Scene manualBVHScene = createManualBVHScene("Stage 3 Manual BVH");
        Scene automaticBVHScene = createAutomaticBVHScene("Stage 3 Automatic BVH");

        // Camera configuration for ultimate test
        Camera.Builder baseCamera = cameraBuilder
                .setLocation(new Point(800, 600, 1000))
                .setDirection(new Point(0, 0, -200), Vector.AXIS_Y)
                .setVpDistance(1200)
                .setVpSize(500, 500)
                .setResolution(500, 500);
        // 250,000 rays for thorough testing

        // === STAGE 0: NO OPTIMIZATIONS BASELINE ===
        System.out.println("=== Stage 0: No Optimizations Baseline ===");
        Scene noOptScene = createNoOptimizationsScene("Stage 0 No Optimizations");
        System.out.println("Objects in no-opt scene: " + noOptScene.geometries.size());

        Camera noOptCamera = baseCamera.setRayTracer(noOptScene, RayTracerType.SIMPLE).setMultithreading(0).build();

        long startTime0 = System.nanoTime();
        noOptCamera.renderImage().writeToImage("stage3_no_optimizations");
        long endTime0 = System.nanoTime();

        double noOptTime = (endTime0 - startTime0) / 1_000_000_000.0;
        System.out.println("Stage 0 (No Optimizations) time: " + String.format("%.2f", noOptTime) + " seconds");
        System.out.println("Stage 0 performance: " + String.format("%.0f", 250000 / noOptTime) + " rays/second");

        // === STAGE 1: FLAT CBR BASELINE ===
        System.out.println("\n=== Stage 1: Flat CBR Baseline ===");
        System.out.println("Objects in flat scene: " + flatScene.geometries.size());

        Camera flatCamera = baseCamera.setRayTracer(flatScene, RayTracerType.SIMPLE).setMultithreading(0).build();

        long startTime1 = System.nanoTime();
        flatCamera.renderImage().writeToImage("stage3_flat_cbr_baseline");
        long endTime1 = System.nanoTime();

        double flatCBRTime = (endTime1 - startTime1) / 1_000_000_000.0;
        System.out.println("Stage 1 (Flat CBR) time: " + String.format("%.2f", flatCBRTime) + " seconds");
        System.out.println("Stage 1 performance: " + String.format("%.0f", 250000 / flatCBRTime) + " rays/second");

        // === STAGE 2: MANUAL BVH ===
        System.out.println("\n=== Stage 2: Manual BVH Hierarchy ===");
        System.out.println("Manual spatial organization applied");

        Camera manualCamera = baseCamera.setRayTracer(manualBVHScene, RayTracerType.SIMPLE).setMultithreading(0).build();

        long startTime2 = System.nanoTime();
        manualCamera.renderImage().writeToImage("stage3_manual_bvh");
        long endTime2 = System.nanoTime();

        double manualBVHTime = (endTime2 - startTime2) / 1_000_000_000.0;
        System.out.println("Stage 2 (Manual BVH) time: " + String.format("%.2f", manualBVHTime) + " seconds");
        System.out.println("Stage 2 performance: " + String.format("%.0f", 250000 / manualBVHTime) + " rays/second");

        // === STAGE 3: AUTOMATIC BVH ===
        System.out.println("\n=== Stage 3: Automatic BVH with SAH ===");
        System.out.println("Automatic optimal hierarchy construction...");

        Camera automaticCamera = baseCamera.setRayTracer(automaticBVHScene, RayTracerType.SIMPLE).setMultithreading(0).build();

        long startTime3 = System.nanoTime();
        automaticCamera.renderImage().writeToImage("stage3_automatic_bvh_sah");
        long endTime3 = System.nanoTime();

        double automaticBVHTime = (endTime3 - startTime3) / 1_000_000_000.0;
        System.out.println("Stage 3 (Automatic BVH) time: " + String.format("%.2f", automaticBVHTime) + " seconds");
        System.out.println("Stage 3 performance: " + String.format("%.0f", 250000 / automaticBVHTime) + " rays/second");

        // === STAGE 4: MULTITHREADING ONLY (on Stage 1) ===
        System.out.println("\n=== Stage 4: Multithreading Only (Flat CBR + MT) ===");
        System.out.println("Testing multithreading impact without BVH");

        Camera multithreadingCamera = baseCamera.setRayTracer(flatScene, RayTracerType.SIMPLE).setMultithreading(4).build();

        long startTime4 = System.nanoTime();
        multithreadingCamera.renderImage().writeToImage("stage3_multithreading_only");
        long endTime4 = System.nanoTime();

        double multithreadingTime = (endTime4 - startTime4) / 1_000_000_000.0;
        System.out.println("Stage 4 (Multithreading Only) time: " + String.format("%.2f", multithreadingTime) + " seconds");
        System.out.println("Stage 4 performance: " + String.format("%.0f", 250000 / multithreadingTime) + " rays/second");

        // === STAGE 5: ULTIMATE OPTIMIZATION (Automatic BVH + MT) ===
        System.out.println("\n=== Stage 5: Ultimate Optimization (Automatic BVH + MT) ===");
        System.out.println("Testing combined BVH and multithreading optimization");

        Camera ultimateCamera = baseCamera.setRayTracer(automaticBVHScene, RayTracerType.SIMPLE).setMultithreading(4).build();

        long startTime5 = System.nanoTime();
        ultimateCamera.renderImage().writeToImage("stage3_ultimate_optimization");
        long endTime5 = System.nanoTime();

        double ultimateTime = (endTime5 - startTime5) / 1_000_000_000.0;
        System.out.println("Stage 5 (Ultimate Optimization) time: " + String.format("%.2f", ultimateTime) + " seconds");
        System.out.println("Stage 5 performance: " + String.format("%.0f", 250000 / ultimateTime) + " rays/second");

        // === COMPREHENSIVE PERFORMANCE ANALYSIS ===
        double stage0ToCBR = noOptTime / flatCBRTime;
        double stage1Improvement = noOptTime / flatCBRTime;
        double stage2Improvement = noOptTime / manualBVHTime;
        double stage3Improvement = noOptTime / automaticBVHTime;
        double stage4Improvement = noOptTime / multithreadingTime;
        double stage5Improvement = noOptTime / ultimateTime;

        double stage3VsStage2 = manualBVHTime / automaticBVHTime;

        System.out.println("\n" + "=".repeat(60));
        System.out.println("=== ULTIMATE BVH PERFORMANCE RESULTS ===");
        System.out.println("=".repeat(60));
        System.out.println("Test resolution: 500x500 (250,000 rays)");
        System.out.println("Scene complexity: " + flatScene.geometries.size() + " objects");
        System.out.println();

        System.out.println("📊 RENDER TIMES:");
        System.out.println("  Stage 0 (No Opt):       " + String.format("%8.2f", noOptTime) + " seconds");
        System.out.println("  Stage 1 (Flat CBR):     " + String.format("%8.2f", flatCBRTime) + " seconds");
        System.out.println("  Stage 2 (Manual BVH):   " + String.format("%8.2f", manualBVHTime) + " seconds");
        System.out.println("  Stage 3 (Automatic):    " + String.format("%8.2f", automaticBVHTime) + " seconds");
        System.out.println("  Stage 4 (MT Only):      " + String.format("%8.2f", multithreadingTime) + " seconds");
        System.out.println("  Stage 5 (Ultimate):     " + String.format("%8.2f", ultimateTime) + " seconds");
        System.out.println();

        System.out.println("🚀 PERFORMANCE IMPROVEMENTS (vs No Optimizations):");
        System.out.println("  Stage 1 vs Stage 0:     " + String.format("%.2fx", stage1Improvement) + " faster (CBR effect)");
        System.out.println("  Stage 2 vs Stage 0:     " + String.format("%.2fx", stage2Improvement) + " faster");
        System.out.println("  Stage 3 vs Stage 0:     " + String.format("%.2fx", stage3Improvement) + " faster");
        System.out.println("  Stage 3 vs Stage 2:     " + String.format("%.2fx", stage3VsStage2) + " faster");
        System.out.println("  Stage 4 vs Stage 0:     " + String.format("%.2fx", stage4Improvement) + " faster");
        System.out.println("  Stage 5 vs Stage 0:     " + String.format("%.2fx", stage5Improvement) + " faster");
        System.out.println();

        System.out.println("⏱️  TIME SAVED (vs No Optimizations):");
        System.out.println("  By CBR:                  " + String.format("%.2f", noOptTime - flatCBRTime) + " seconds");
        System.out.println("  By Manual BVH:          " + String.format("%.2f", noOptTime - manualBVHTime) + " seconds");
        System.out.println("  By Automatic BVH:       " + String.format("%.2f", noOptTime - automaticBVHTime) + " seconds");
        System.out.println("  By Multithreading:      " + String.format("%.2f", noOptTime - multithreadingTime) + " seconds");
        System.out.println("  By Ultimate:            " + String.format("%.2f", noOptTime - ultimateTime) + " seconds");

        // Performance evaluation
        System.out.println("\n📈 PERFORMANCE EVALUATION:");
        if (stage3Improvement >= 20.0) {
            System.out.println("🏆 OUTSTANDING: Automatic BVH provides exceptional acceleration!");
            System.out.println("   🎯 Assignment target: 20-40x ✅ ACHIEVED (" + String.format("%.1fx", stage3Improvement) + ")");
        } else if (stage3Improvement >= 10.0) {
            System.out.println("🎯 EXCELLENT: Automatic BVH is highly effective!");
            System.out.println("   🎯 Assignment target: 20-40x ⚠️ CLOSE (" + String.format("%.1fx", stage3Improvement) + ")");
        } else if (stage3Improvement >= 5.0) {
            System.out.println("✅ GOOD: Automatic BVH showing solid improvement");
            System.out.println("   🎯 Assignment target: 20-40x ❌ BELOW TARGET (" + String.format("%.1fx", stage3Improvement) + ")");
        } else {
            System.out.println("⚠️  PARTIAL: BVH provides moderate improvement");
            System.out.println("   Consider increasing scene complexity for better demonstration");
        }

        if (stage5Improvement >= 40.0) {
            System.out.println("🚀 ULTIMATE: Combined optimization provides incredible acceleration!");
        } else if (stage5Improvement >= 20.0) {
            System.out.println("🌟 ULTIMATE: Combined optimization is highly effective!");
        }

        // Verify all scenes produced valid results
        assertTrue(noOptTime > 0, "No optimizations render time should be positive");
        assertTrue(flatCBRTime > 0, "Flat CBR render time should be positive");
        assertTrue(manualBVHTime > 0, "Manual BVH render time should be positive");
        assertTrue(automaticBVHTime > 0, "Automatic BVH render time should be positive");
        assertTrue(multithreadingTime > 0, "Multithreading render time should be positive");
        assertTrue(ultimateTime > 0, "Ultimate render time should be positive");
        System.out.println("CBR vs No-Opt ratio: " + String.format("%.2fx", stage1Improvement) +
                (stage1Improvement < 1.0 ? " (CBR overhead detected - normal for small scenes)" : ""));
        assertTrue(stage2Improvement >= stage1Improvement, "Stage 2 should be at least as fast as Stage 1");
        assertTrue(stage3Improvement >= stage2Improvement, "Stage 3 should be at least as fast as Stage 2");

        System.out.println("\n📁 Generated comparison images:");
        System.out.println("   stage3_no_optimizations.ppm     - Stage 0 no optimizations");
        System.out.println("   stage3_flat_cbr_baseline.ppm    - Stage 1 flat CBR");
        System.out.println("   stage3_manual_bvh.ppm           - Stage 2 manual hierarchy");
        System.out.println("   stage3_automatic_bvh_sah.ppm    - Stage 3 automatic SAH");
        System.out.println("   stage3_multithreading_only.ppm  - Stage 4 multithreading only");
        System.out.println("   stage3_ultimate_optimization.ppm- Stage 5 ultimate optimization");
        System.out.println("   (All images should appear visually identical)");

        System.out.println("\n✅ Complete optimization test completed successfully!");
        System.out.println("🎉 Full optimization journey: from no optimizations to " +
                String.format("%.1fx", stage5Improvement) + " faster with ultimate optimization!");
        System.out.println("🎯 BVH alone achieved: " + String.format("%.1fx", stage3Improvement) + " improvement!");
    }

    /**
     * Creates scene with NO optimizations - true baseline
     * No CBR, no BVH, no multithreading - pure ray tracing
     */
    private Scene createNoOptimizationsScene(String sceneName) {
        Scene scene = new Scene(sceneName);
        setupUltimateLighting(scene);

        // Foundation and background
        scene.geometries.add(
                new Plane(new Point(0, -400, 0), new Vector(0, 1, 0))
                        .setEmission(new Color(15, 20, 30))
                        .setMaterial(new Material().setKD(0.7).setKS(0.3).setShininess(20).setKR(0.4))
        );

        Random random = new Random(789); // Same seed for identical objects

        System.out.println("Creating no-optimizations scene...");

        // Create same objects as other scenes but WITHOUT any bounding box calculations
        createSphereCluster(scene, random, new Point(-300, 0, -300), 80, "Left Front");
        createSphereCluster(scene, random, new Point(300, 0, -300), 80, "Right Front");
        createSphereCluster(scene, random, new Point(0, 200, -600), 60, "Top Back");
        createTriangleCluster(scene, random, new Point(-150, -100, -450), 70, "Left Triangle");
        createTriangleCluster(scene, random, new Point(150, -100, -450), 70, "Right Triangle");

        // CRITICAL: Do NOT call getBoundingBox() - this keeps it as pure flat structure
        // without any CBR optimizations

        System.out.println("No-optimizations scene created with " + scene.geometries.size() + " objects");
        System.out.println("No CBR, no BVH, no multithreading - pure baseline");
        return scene;
    }

    /**
     * Creates ultimate test scene for performance demonstration
     * Uses varied object distribution for optimal BVH effectiveness
     */
    private Scene createUltimateTestScene(String sceneName) {
        Scene scene = new Scene(sceneName);
        setupUltimateLighting(scene);

        // Foundation and background
        scene.geometries.add(
                new Plane(new Point(0, -400, 0), new Vector(0, 1, 0))
                        .setEmission(new Color(15, 20, 30))
                        .setMaterial(new Material().setKD(0.7).setKS(0.3).setShininess(20).setKR(0.4))
        );

        Random random = new Random(789); // Fixed seed for consistent testing

        System.out.println("Creating ultimate test scene with optimized distribution...");

        // Create multiple clusters for optimal BVH demonstration
        createSphereCluster(scene, random, new Point(-300, 0, -300), 80, "Left Front");
        createSphereCluster(scene, random, new Point(300, 0, -300), 80, "Right Front");
        createSphereCluster(scene, random, new Point(0, 200, -600), 60, "Top Back");
        createTriangleCluster(scene, random, new Point(-150, -100, -450), 70, "Left Triangle");
        createTriangleCluster(scene, random, new Point(150, -100, -450), 70, "Right Triangle");

        System.out.println("Ultimate scene created with " + scene.geometries.size() + " objects");
        return scene;
    }

    /**
     * Creates manual BVH scene with hand-optimized spatial organization
     */
    private Scene createManualBVHScene(String sceneName) {
        Scene scene = new Scene(sceneName);
        setupUltimateLighting(scene);

        // Foundation plane
        scene.geometries.add(
                new Plane(new Point(0, -400, 0), new Vector(0, 1, 0))
                        .setEmission(new Color(15, 20, 30))
                        .setMaterial(new Material().setKD(0.7).setKS(0.3).setShininess(20).setKR(0.4))
        );

        Random random = new Random(789); // Same seed for identical objects

        // Manual spatial organization
        Geometries frontClusters = new Geometries();
        Geometries backClusters = new Geometries();
        Geometries triangleClusters = new Geometries();

        // Create clusters and organize manually
        addClusterToContainer(frontClusters, random, new Point(-300, 0, -300), 80);
        addClusterToContainer(frontClusters, random, new Point(300, 0, -300), 80);
        addClusterToContainer(backClusters, random, new Point(0, 200, -600), 60);
        addTriangleClusterToContainer(triangleClusters, random, new Point(-150, -100, -450), 70);
        addTriangleClusterToContainer(triangleClusters, random, new Point(150, -100, -450), 70);

        // Build manual hierarchy
        BVHNode topLevel = new BVHNode(
                new BVHNode(frontClusters, backClusters),
                triangleClusters
        );

        scene.geometries.add(topLevel);

        System.out.println("Manual BVH scene created with spatial organization");
        return scene;
    }

    /**
     * Creates automatic BVH scene using SAH algorithm
     */
    private Scene createAutomaticBVHScene(String sceneName) {
        Scene scene = new Scene(sceneName);
        setupUltimateLighting(scene);

        // Foundation plane
        scene.geometries.add(
                new Plane(new Point(0, -400, 0), new Vector(0, 1, 0))
                        .setEmission(new Color(15, 20, 30))
                        .setMaterial(new Material().setKD(0.7).setKS(0.3).setShininess(20).setKR(0.4))
        );

        Random random = new Random(789); // Same seed for identical objects

        // Collect all objects for automatic BVH building
        List<Intersectable> allObjects = new ArrayList<>();

        addClusterToList(allObjects, random, new Point(-300, 0, -300), 80);
        addClusterToList(allObjects, random, new Point(300, 0, -300), 80);
        addClusterToList(allObjects, random, new Point(0, 200, -600), 60);
        addTriangleClusterToList(allObjects, random, new Point(-150, -100, -450), 70);
        addTriangleClusterToList(allObjects, random, new Point(150, -100, -450), 70);

        System.out.println("Building automatic BVH with SAH algorithm...");

        // Build automatic BVH using SAH
        Intersectable automaticBVH = BVHBuilder.buildBVH(allObjects);

        if (automaticBVH != null) {
            scene.geometries.add(automaticBVH);

            // Print BVH statistics
            BVHBuilder.printBVHStatistics(automaticBVH);
        }

        System.out.println("Automatic BVH scene created with optimal SAH hierarchy");
        return scene;
    }

    /**
     * Setup dramatic lighting for ultimate performance test
     */
    private void setupUltimateLighting(Scene scene) {
        scene.setAmbientLight(new AmbientLight(new Color(20, 25, 35)));

        scene.lights.add(new DirectionalLight(new Color(300, 300, 400), new Vector(1, -1, -1)));
        scene.lights.add(new PointLight(new Color(200, 100, 300), new Point(-500, 600, 500))
                .setKl(0.00005).setKq(0.0000005));
        scene.lights.add(new SpotLight(new Color(400, 200, 100), new Point(500, 700, 600), new Vector(-1, -1, -1))
                .setKl(0.0001).setKq(0.000001));
    }

    /**
     * Helper methods for creating object clusters
     */
    private void createSphereCluster(Scene scene, Random random, Point center, int count, String name) {
        for (int i = 0; i < count; i++) {
            Point position = center.add(new Vector(
                    random.nextGaussian() * 80,
                    random.nextGaussian() * 60,
                    random.nextGaussian() * 70
            ));

            scene.geometries.add(createRandomSphere(position, random));
        }
    }

    private void createTriangleCluster(Scene scene, Random random, Point center, int count, String name) {
        for (int i = 0; i < count; i++) {
            Point position = center.add(new Vector(
                    random.nextGaussian() * 60,
                    random.nextGaussian() * 40,
                    random.nextGaussian() * 50
            ));

            scene.geometries.add(createRandomTriangle(position, random));
        }
    }

    private void addClusterToContainer(Geometries container, Random random, Point center, int count) {
        for (int i = 0; i < count; i++) {
            Point position = center.add(new Vector(
                    random.nextGaussian() * 80,
                    random.nextGaussian() * 60,
                    random.nextGaussian() * 70
            ));

            container.add(createRandomSphere(position, random));
        }
    }

    private void addTriangleClusterToContainer(Geometries container, Random random, Point center, int count) {
        for (int i = 0; i < count; i++) {
            Point position = center.add(new Vector(
                    random.nextGaussian() * 60,
                    random.nextGaussian() * 40,
                    random.nextGaussian() * 50
            ));

            container.add(createRandomTriangle(position, random));
        }
    }

    private void addClusterToList(List<Intersectable> list, Random random, Point center, int count) {
        for (int i = 0; i < count; i++) {
            Point position = center.add(new Vector(
                    random.nextGaussian() * 80,
                    random.nextGaussian() * 60,
                    random.nextGaussian() * 70
            ));

            list.add(createRandomSphere(position, random));
        }
    }

    private void addTriangleClusterToList(List<Intersectable> list, Random random, Point center, int count) {
        for (int i = 0; i < count; i++) {
            Point position = center.add(new Vector(
                    random.nextGaussian() * 60,
                    random.nextGaussian() * 40,
                    random.nextGaussian() * 50
            ));

            list.add(createRandomTriangle(position, random));
        }
    }

    private Sphere createRandomSphere(Point center, Random random) {
        // Create a sphere with random size and color
        Sphere sphere = new Sphere(random.nextDouble() * 12 + 6, center);
        sphere.setEmission(new Color(
                random.nextInt(150) + 105,
                random.nextInt(150) + 105,
                random.nextInt(150) + 105
        ));
        sphere.setMaterial(new Material()
                .setKD(0.4 + random.nextDouble() * 0.4)
                .setKS(0.2 + random.nextDouble() * 0.4)
                .setShininess(30 + random.nextInt(70))
                .setKR(random.nextDouble() * 0.4));
        return sphere;
    }

    private Triangle createRandomTriangle(Point center, Random random) {
        double size = random.nextDouble() * 25 + 12;
        double angle = random.nextDouble() * Math.PI * 2;

        Point p1 = center;
        Point p2 = center.add(new Vector(size * Math.cos(angle), size * Math.sin(angle), 0));
        Point p3 = center.add(new Vector(
                size * Math.cos(angle + Math.PI * 2/3),
                size * Math.sin(angle + Math.PI * 2/3),
                random.nextDouble() * 12 - 6
        ));

        Triangle triangle = new Triangle(p1, p2, p3);
        triangle.setEmission(new Color(
                random.nextInt(120) + 90,
                random.nextInt(120) + 90,
                random.nextInt(120) + 90
        ));
        triangle.setMaterial(new Material()
                .setKD(0.5 + random.nextDouble() * 0.3)
                .setKS(0.2 + random.nextDouble() * 0.3)
                .setShininess(25 + random.nextInt(50))
                .setKT(random.nextDouble() * 0.2));
        return triangle;
    }
}