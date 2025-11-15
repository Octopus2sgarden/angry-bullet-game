// Assuming StdDraw is available and imported
// import edu.princeton.cs.algs4.StdDraw;

public class Obstacle extends Shape {

    public Obstacle(double x, double y, double width, double height) {
        super(x, y, width, height);
    }

    @Override
    public void draw() {
        // Obstacles are drawn in dark grey color[cite: 22].
        StdDraw.setPenColor(StdDraw.DARK_GRAY);

        // Draw the filled rectangle using center coordinates and half dimensions.
        StdDraw.filledRectangle(
                this.x + this.width / 2.0,
                this.y + this.height / 2.0,
                this.width / 2.0,
                this.height / 2.0
        );
    }

    // The isColliding method is inherited directly from Shape.
}