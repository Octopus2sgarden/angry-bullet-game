

public class Target extends Shape {

    // Constructor calls the parent (Shape) class constructor.
    public Target(double x, double y, double width, double height) {
        super(x, y, width, height);
    }

    @Override
    public void draw() {
        // Targets are drawn in orange color[cite: 22].
        StdDraw.setPenColor(StdDraw.ORANGE);

        // StdDraw.filledRectangle requires the center coordinates (x + w/2) and
        // half the width/height (w/2) because (x,y) in Shape is the bottom-left corner[cite: 36].
        StdDraw.filledRectangle(
                this.x + this.width / 2.0,  // Center X coordinate
                this.y + this.height / 2.0, // Center Y coordinate
                this.width / 2.0,           // Half Width
                this.height / 2.0           // Half Height
        );
    }

    // The isColliding method is inherited directly from Shape.
}