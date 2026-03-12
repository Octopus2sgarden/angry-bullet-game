/**
 * FILE: Bullet.java
 * AUTHOR: SEDA GUNES
 * DATE: 19.10.2025
 * AI ASSISTANTS: OpenAI's ChatGPT (GPT-5) , Google Gemini , Claude
 * DESCRIPTION:
 * This class represents the projectile (bullet) in the game. It stores the initial launch
 * parameters (velocity and angle), tracks its current position (xCur, yCur), and manages
 * the projectile motion calculation over time (t) using physics formulas.
 */

public class Bullet {
    private double x0;      // Initial X position
    private double y0;      // Initial Y position
    private double xCur;    // Current X position
    private double yCur;    // Current Y position
    private double v0;      // Initial velocity magnitude
    private double vX;      // X-component of velocity (constant)
    private double vY0;     // Initial Y-component of velocity
    private double time;    // Time elapsed since launch
    private double theta;   // Launch angle in degrees

    public static final double g = 9.81; // Gravity constant (m/s^2)
    private final double RADIUS = 4.0;  // Bullet radius for drawing and collision checks (based on user's draw call)

    /**
     * Constructor for the Bullet.
     * @param x0 Initial X coordinate.
     * @param y0 Initial Y coordinate.
     * @param v0 Initial velocity magnitude.
     * @param theta Launch angle in degrees.
     */
    public Bullet(double x0, double y0, double v0, double theta) {
        this.x0 = x0;
        this.y0 = y0;
        this.xCur = x0;
        this.yCur = y0;
        this.v0 = v0;
        this.theta = theta; // angle in degrees

        // Calculate velocity components immediately using the parameter 'theta'
        double thetaRad = Math.toRadians(theta);
        this.vX = v0 * Math.cos(thetaRad);
        this.vY0 = v0 * Math.sin(thetaRad);

        this.time = 0.0;
    }

    // --- GETTERS ---

    public double getX0() {
        return x0;
    }

    public double getY0() {
        return y0;
    }

    public double getxCur() {
        return xCur;
    }

    public double getyCur() {
        return yCur;
    }

    public double getV0() {
        return v0;
    }

    public double getTheta() {
        return theta;
    }

    /**
     * Returns the radius of the bullet. Essential for collision detection.
     */
    public double getRadius() {
        return RADIUS;
    }

    /**
     * Updates the bullet's position using the projectile motion formulas.
     * x_t = x_0 + V_x * t
     * y_t = y_0 + V_y0 * t - 0.5 * g * t^2
     * @param deltaTime The time elapsed since the last update.
     */
    public void updatePosition(double deltaTime){
        this.time += deltaTime;
        xCur = x0 + vX * time;
        yCur = y0 + vY0 * time - 0.5 * g * time * time;
    }

    /**
     * Draws the bullet as a small filled circle on the current screen position (xCur, yCur).
     */
    public void draw() {
        // Set color and shape parameters.
        StdDraw.setPenColor(StdDraw.BLACK);

        // Draw the bullet as a filled circle using the RADIUS constant.
        StdDraw.filledCircle(this.xCur, this.yCur, RADIUS);
    }

    public double getvX() {
        return vX;
    }

    public double getvY0() {
        return vY0;
    }

    public double getRADIUS() {
        return RADIUS;
    }

    public double getTime() {
        return time;
    }
}
