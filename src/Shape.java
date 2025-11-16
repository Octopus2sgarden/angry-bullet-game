/**
 * FILE: Shape.java
 * AUTHOR: SEDA GUNES
 * STUDENT ID: 2025719036
 * DATE: 19.10.2025
 * AI ASSISTANTS: OpenAI's ChatGPT (GPT-5) , Google Gemini , Claude
 * DESCRIPTION:
 * This base class provides common fields (position and dimensions) and default,
 * non-functional implementations for the 'draw' and 'isColliding' methods.
 * It serves as the parent for concrete game objects like Obstacle and Target,
 * which must override the draw and collision logic.
 */

public class Shape {

    // Protected fields for coordinates (bottom-left corner) and dimensions.
    // 'protected' allows subclasses (Target/Obstacle) direct access.
    protected double x;
    protected double y;
    protected double width;
    protected double height;

    // Constructor: Initializes the position and size of the shape.
    public Shape(double x, double y, double width, double height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    // Getters for accessing private/protected fields
    public double getX() { return x; }
    public double getY() { return y; }
    public double getWidth() { return width; }
    public double getHeight() { return height; }

    /**
     * Checks for collision between the shape and the Bullet.
     * This implementation is a placeholder and should be overridden by subclasses
     * (Obstacle, Target) to include actual collision logic.
     * @param bullet The projectile to check collision against.
     * @return Always returns false in the base class.
     */
    public boolean isColliding(Bullet bullet) {
        // Base class collision check is not implemented and returns false.
        return false;
    }

    /**
     * Draw method.
     * This implementation is a placeholder and should be overridden by subclasses
     * to draw the specific shape on the StdDraw canvas.
     */
    public void draw() {
        // Nothing to draw in the base class.
    }
}