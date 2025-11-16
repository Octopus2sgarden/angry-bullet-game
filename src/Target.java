/**
 * Target class represents a static rectangular target. It extends the base Shape class.
 */
public class Target extends Shape { // Artık 'implements Shape' yerine 'extends Shape' kullanılıyor.

    // Yapıcı metot, Shape'in yapıcı metodunu çağırır.
    public Target(double x, double y, double w, double h) {
        super(x, y, w, h); // Temel sınıfın yapıcı metodunu çağırır
    }

    @Override
    public void draw() {
        // Çizim için merkezi koordinatları hesapla
        double xCenter = this.x + this.width / 2;
        double yCenter = this.y + this.height / 2;

        StdDraw.setPenColor(StdDraw.ORANGE);
        // StdDraw.filledRectangle takes center x, center y, half-width, half-height
        StdDraw.filledRectangle(xCenter, yCenter, this.width / 2, this.height / 2);

    }

    @Override
    public boolean isColliding(Bullet bullet) {
        // Gelişmiş AABB vs Circle çarpışma kontrolü
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