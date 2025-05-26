package scene;

import geometries.Geometries;
import lighting.AmbientLight;
import lighting.Light;
import lighting.LightSource;
import primitives.Color;

import java.util.LinkedList;
import java.util.List;

/**
 * Class representing a scene in a 3D environment
 * The scene contains a name, background color, ambient light, and geometries
 */
public class Scene {
    /**
     * The name of the scene
     */
    public String name;
    /**
     * The background color of the scene
     */
    public Color background = Color.BLACK;
    /**
     * The ambient light in the scene
     */
    public AmbientLight ambientLight = AmbientLight.NONE;
    /**
     * The geometries in the scene
     */
    public Geometries geometries = new Geometries();

    /**
     * The list of light sources in the scene
     */
    public List<LightSource> lights = new LinkedList<>();

    /**
     * Scene setter
     * @param lights the lights of the scene
     * @return the scene
     */
    public Scene setLights(List<LightSource> lights) {
        this.lights = lights;
        return this;
    }

    /**
     * Constructor for Scene
     * @param name the name of the scene
     */
    public Scene(String name) {
        this.name = name;
    }

    /**
     * Scene getter
     * @param background the background color of the scene
     * @return the scene
     */
    public Scene setBackground(Color background) {
        this.background = background;
        return this;
    }

    /**
     * Scene getter
     * @param ambientLight the ambient light of the scene
     * @return the scene
     */
    public Scene setAmbientLight(AmbientLight ambientLight) {
        this.ambientLight = ambientLight;
        return this;
    }

    /**
     * Scene getter
     * @param geometries the geometries in the scene
     * @return the scene
     */
    public Scene setGeometries(Geometries geometries){
        this.geometries = geometries;
        return this;
    }
}
