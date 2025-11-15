//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {

        StdDraw.setCanvasSize(1100, 530);
        // Optional: set coordinate scale (so you can draw easily)
        StdDraw.setXscale(0, 100);
        StdDraw.setYscale(0, 200);

        // Draw a rectangle in the center
        StdDraw.setPenColor(StdDraw.DARK_GRAY);
        StdDraw.filledRectangle(20, 25, 4, 25); // (x, y, halfWidth, halfHeight)

        double x = 10, y = 10;       // ball position
        double vx = 0, vy = 0;     // velocity
        double speed = 0.03;       // initial speed after push
        boolean moving = false;

        while (true) {
            StdDraw.clear();
            StdDraw.setPenColor(StdDraw.DARK_GRAY);
            StdDraw.filledRectangle(20, 25, 4, 25); // (x, y, halfWidth, halfHeight)

            // 1️⃣ Draw aiming line if ball is not moving
            if (!moving) {
                double mx = StdDraw.mouseX();
                double my = StdDraw.mouseY();
                StdDraw.setPenColor(StdDraw.GRAY);
                StdDraw.line(x, y, mx, my);
            }

            // 2️⃣ When mouse is pressed, set velocity
            if (StdDraw.isMousePressed() && !moving) {
                double mx = StdDraw.mouseX();
                double my = StdDraw.mouseY();

                double angle = Math.atan2(my - y, mx - x);
                vx = speed * Math.cos(angle);
                vy = speed * Math.sin(angle);
                moving = true;
            }

            // 3️⃣ Move ball if in motion
            if (moving) {
                x += vx;
                y += vy;

                // Optional: add friction (slow down)
                vx *= 0.99;
                vy *= 0.99;

                // Stop when slow
                if (Math.hypot(vx, vy) < 0.001)
                    moving = false;
            }

            // 4️⃣ Draw ball
            StdDraw.setPenColor(StdDraw.BLUE);
            StdDraw.filledCircle(x, y, 0.40);

            // 5️⃣ Refresh
            StdDraw.show();
            StdDraw.pause(20);
        }
    }





}
