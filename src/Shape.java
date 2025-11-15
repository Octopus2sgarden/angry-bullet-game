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
     * Checks for collision between the current shape (Target or Obstacle) and the Bullet.
     * Collision occurs if the bullet's current position (xCur, yCur) is within the
     * rectangle defined by (x, y, width, height).
     */
    public boolean isColliding(Bullet bullet) {

        // Get the bullet's current coordinates
        double bulletX = bullet.getxCur();
        double bulletY = bullet.getyCur();

        // 1. Horizontal (X) Collision Check:
        // Is bulletX within the range [this.x, this.x + this.width]?
        boolean xCollision = (bulletX >= this.x) && (bulletX <= this.x + this.width);

        // 2. Vertical (Y) Collision Check:
        // Is bulletY within the range [this.y, this.y + this.height]?
        boolean yCollision = (bulletY >= this.y) && (bulletY <= this.y + this.height);

        // Collision requires both conditions to be true.
        return xCollision && yCollision;
    }

    /**
     * Draw method. This is intended to be overridden by subclasses (Target/Obstacle)
     * to provide specific drawing logic and color.
     */
    public void draw() {
        // Implementation is left empty here
    }
}