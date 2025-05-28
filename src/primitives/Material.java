package primitives;

/**
 * The Material class represents the material properties of a 3D object.
 * It includes coefficients for ambient, specular, and diffusive light attenuation,
 * as well as the material's shininess.
 *
 * @author Yehuda Rubin and Arye Hacohen
 */
public class Material {
    /**
     * The Ambient light attenuation coefficient, initialized with (1,1,1)
     */
    public Double3 kA = Double3.ONE;
    /**
     * The Ambient light specular attenuation coefficient, initialized with (0,0,0)
     */
    public Double3 kS = Double3.ZERO;
    /**
     * The Ambient light diffusive attenuation coefficient, initialized with (0,0,0)
     */
    public Double3 kD = Double3.ZERO;
    /**
     * the material’s shininess, initialized with 0
     */
    public int nSh = 0;

    /**
     * The default material is initialized with kA = (1,1,1), kS = (0,0,0), kD = (0,0,0), nSh = 0
     */
    public Double3 kT = Double3.ZERO; // Transparency coefficient, initialized with (0,0,0)

    /**
     * The reflection coefficient, initialized with (0,0,0)
     */
    public Double3 kR = Double3.ZERO; // Reflection coefficient, initialized with (0,0,0)

    /**
     * Default constructor
     */
    public Material() {}

    /**
     * Setter for the Ambient light attenuation coefficient
     * @param kA the Ambient light attenuation coefficient
     * @return the Material
     */
    public Material setMaterial(Double3 kA) {
        this.kA = kA;
        return this;
    }

    /**
     * Setter for the Ambient light attenuation coefficient
     * @param kA the Ambient light attenuation coefficient
     * @return the Material
     */
    public Material setMaterial(double kA) {
        this.kA = new Double3(kA);
        return this;
    }

    /**
     * Setter for the Ambient light specular attenuation coefficient
     * @param kS the Ambient light specular attenuation coefficient
     * @return the Material
     */
    public Material setKS(Double3 kS) {
        this.kS = kS;
        return this;
    }

    /**
     * Setter for the Ambient light specular attenuation coefficient
     * @param kS the Ambient light specular attenuation coefficient
     * @return the Material
     */
    public Material setKS(double kS) {
        this.kS = new Double3(kS);
        return this;
    }

    /**
     * Setter for the Ambient light diffusive attenuation coefficient
     * @param kD the Ambient light diffusive attenuation coefficient
     * @return the Material
     */
    public Material setKD(Double3 kD) {
        this.kD = kD;
        return this;
    }

    /**
     * Setter for the Ambient light diffusive attenuation coefficient
     * @param kD the Ambient light diffusive attenuation coefficient
     * @return the Material
     */
    public Material setKD(double kD) {
        this.kD = new Double3(kD);
        return this;
    }

    /**
     * Setter for the material’s shininess
     * @param nSh the material’s shininess
     * @return the Material
     */
    public Material setShininess(int nSh) {
        this.nSh = nSh;
        return this;
    }

    /**
     * Setter for the transparency coefficient
     * @param kT the transparency coefficient
     * @return the Material
     */
    public Material setKT(Double3 kT) {
        this.kT = kT;
        return this;
    }

    /**
     * Setter for the transparency coefficient
     * @param kT the transparency coefficient
     * @return the Material
     */
    public Material setKT(double kT) {
        this.kT = new Double3(kT);
        return this;
    }

    /**
     * Setter for the reflection coefficient
     * @param kR the reflection coefficient
     * @return the Material
     */
    public Material setKR(double kR) {
        this.kR = new Double3(kR);
        return this;
    }

    /**
     * Setter for the reflection coefficient
     * @param kR the reflection coefficient
     * @return the Material
     */
    public Material setKR(Double3 kR) {
        this.kR = kR;
        return this;
    }
}
