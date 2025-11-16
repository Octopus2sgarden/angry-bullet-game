/**
 * FILE: Target.java
 * AUTHOR: SEDA GUNES
 * STUDENT ID: 2025719036
 * DATE: 19.10.2025
 * AI ASSISTANTS: OpenAI's ChatGPT (GPT-5) , Google Gemini , Claude
 * DESCRIPTION:
 * This class represents the rectangular target that the player aims to hit.
 * It extends the Shape base class, uses a distinct color (Orange),
 * and implements collision detection logic.
 */
public class Target extends Shape {
    /**
     * Constructor for the Target.
     * @param x The bottom-left corner's X-coordinate.
     * @param y The bottom-left corner's Y-coordinate.
     * @param w The total width of the target.
     * @param h The total height of the target.
     */
    public Target(double x, double y, double w, double h) {
        super(x, y, w, h);
    }

    @Override
    public void draw() {

        double xCenter = this.x + this.width / 2;
        double yCenter = this.y + this.height / 2;

        // Target color is set to Orange for visibility.
        StdDraw.setPenColor(StdDraw.ORANGE);
        // StdDraw.filledRectangle takes center x, center y, half-width, half-height
        StdDraw.filledRectangle(xCenter, yCenter, this.width / 2, this.height / 2);

    }
    /**
     * Checks for collision using the Circle vs. AABB (Axis-Aligned Bounding Box) algorithm.
     * @param bullet The circular projectile to check against.
     * @return True if the bullet collides with the target, false otherwise.
     */
    @Override
    public boolean isColliding(Bullet bullet) {
        // Get bullet properties
        double circleX = bullet.getxCur();
        double circleY = bullet.getyCur();
        double radius = bullet.getRadius();

        // 1. Find the closest point (closestX, closestY) on the rectangle's boundary to the bullet's center.
        // The rectangle spans from (this.x) to (this.x + this.width) in X.
        double closestX = Math.max(this.x, Math.min(circleX, this.x + this.width));
        // The rectangle spans from (this.y) to (this.y + this.height) in Y.
        double closestY = Math.max(this.y, Math.min(circleY, this.y + this.height));

        // 2. Calculate the distance vector components between the closest point and the bullet's center.
        double distanceX = circleX - closestX;
        double distanceY = circleY - closestY;

        // Collision occurs if the distance between the closest point and the center is less than the radius.
        return (distanceX * distanceX + distanceY * distanceY) < (radius * radius);
    }
}