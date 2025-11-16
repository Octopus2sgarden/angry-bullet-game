import java.awt.Color;

/**
 * Obstacle class represents a static rectangular barrier. It extends the base Shape class
 * and implements collision logic (Circle vs. AABB).
 */
public class Obstacle extends Shape { // Artık 'implements Shape' yerine 'extends Shape' kullanılıyor.

    private Color color; // Engelin rengini saklamak için

    // Yapıcı metot, Shape'in yapıcı metodunu çağırır ve rengi alır.
    public Obstacle(double x, double y, double w, double h, Color color) {
        super(x, y, w, h); // Temel sınıfın yapıcı metodunu çağırır
        this.color = color;
    }

    @Override
    public void draw() {
        // Çizim için merkezi koordinatları hesapla
        double xCenter = this.x + this.width / 2;
        double yCenter = this.y + this.height / 2;

        StdDraw.setPenColor(this.color);
        // StdDraw.filledRectangle takes center x, center y, half-width, half-height
        StdDraw.filledRectangle(xCenter, yCenter, this.width / 2, this.height / 2);
    }

    @Override
    public boolean isColliding(Bullet bullet) {
        // Gelişmiş AABB vs Circle çarpışma kontrolü (önceki versiyonunuzdan korunmuştur)
        double circleX = bullet.getxCur();
        double circleY = bullet.getyCur();
        double radius = bullet.getRadius();

        // Dikdörtgene en yakın noktayı bul
        double closestX = Math.max(this.x, Math.min(circleX, this.x + this.width));
        double closestY = Math.max(this.y, Math.min(circleY, this.y + this.height));

        // En yakın nokta ile mermi merkezi arasındaki mesafeyi hesapla
        double distanceX = circleX - closestX;
        double distanceY = circleY - closestY;

        // Mesafe, yarıçaptan küçükse çarpışma var demektir
        return (distanceX * distanceX + distanceY * distanceY) < (radius * radius);
    }
}