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
     * Bu metot artık soyut değil, bu yüzden varsayılan bir uygulama gerektirir.
     * Alt sınıflar bunu ezecektir (override).
     */
    public boolean isColliding(Bullet bullet) {
        // Temel sınıfta çarpışma kontrolü mantıksızdır. Her zaman 'false' döndürülür.
        // Alt sınıfların (Obstacle, Target) bu metodu EZMESİ beklenir.
        return false;
    }

    /**
     * Draw method.
     * Alt sınıfların bu metodu ezmesi (override) beklenir.
     */
    public void draw() {
        // Temel sınıfta çizilecek özel bir şey yoktur.
    }
}