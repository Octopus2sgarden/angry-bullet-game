public class Shape {
    private int height;
    private  int width;
    private  double x; // x coordinate
    private double y;  // y coordinate
    private String color;

    public Shape(int height, int wıdth, double x, double y) {
        this.height = height;
        this.width = wıdth;
        this.x = x;
        this.y = y;
        this.color = color;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
