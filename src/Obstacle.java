/**
 * FILE: Obstacle.java
 * AUTHOR: SEDA GUNES
 * STUDENT ID: 2025719036
 * DATE: 19.10.2025
 * AI ASSISTANTS: OpenAI's ChatGPT (GPT-5) , Google Gemini , Claude
 * DESCRIPTION:
 * This class represents a rectangular obstacle that the bullet must avoid.
 * It extends the Shape base class and includes logic for checking collision
 * between the circular Bullet and the rectangular obstacle.
 */

import java.awt.Color;

/**
 * Obstacle class represents a static rectangular barrier. It extends the base Shape class
 * and implements collision logic (Circle vs. AABB).
 */
public class Obstacle extends Shape {

    private Color color;

    // x, y: center of the rectangle's base; w, h: half-width, half-height.
    public Obstacle(double x, double y, double w, double h, Color color) {
        super(x, y, w, h); // calls parent
        this.color = color;
    }

    @Override
    public void draw() {
        // Calculate the center coordinates for StdDraw.filledRectangle.
        // x is bottom-left X, so center X is x + width/2.
        double xCenter = this.x + this.width / 2;
        // y is bottom-left Y, so center Y is y + height/2.
        double yCenter = this.y + this.height / 2;

        StdDraw.setPenColor(this.color);
        // StdDraw.filledRectangle takes (center x, center y, half-width, half-height)
        StdDraw.filledRectangle(xCenter, yCenter, this.width / 2, this.height / 2);
    }

    @Override
    public boolean isColliding(Bullet bullet) {
        // Get bullet properties
        double circleX = bullet.getxCur();
        double circleY = bullet.getyCur();
        double radius = bullet.getRadius();

        // 1. Find the closest point (closestX, closestY) on the rectangle to the bullet's center.
        // The rectangle spans from (this.x) to (this.x + this.width) in X.
        double closestX = Math.max(this.x, Math.min(circleX, this.x + this.width));
        // The rectangle spans from (this.y) to (this.y + this.height) in Y.
        double closestY = Math.max(this.y, Math.min(circleY, this.y + this.height));

        // 2. Calculate the distance vector components between the closest point and the bullet's center
        double distanceX = circleX - closestX;
        double distanceY = circleY - closestY;

        // 3. Check if the squared distance is less than the squared radius.
        return (distanceX * distanceX + distanceY * distanceY) < (radius * radius);
    }
}