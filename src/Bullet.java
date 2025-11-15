public class Bullet {
    private double x0;
    private double y0;
    private double xCur;
    private double yCur;
    private double v0;
    private double vX;
    private double vY0;
    private double time;
    private double theta;
    public static final double g = 9.81; //gravity constant

    public Bullet(double x0, double y0, double v0, double theta) {
        this.x0 = x0;
        this.y0 = y0;
        this.xCur = x0;
        this.yCur = y0;
        this.v0 = v0;
        this.vX = v0*Math.cos(Math.toRadians(getTheta()));
        this.vY0 = v0*Math.sin(Math.toRadians(getTheta()));
        this.time = 0.0;
        this.theta = theta;//angle
    }

    public double getX0() {
        return x0;
    }

    public double getY0() {
        return y0;
    }

    public double getxCur() {
        return xCur;
    }

    public double getyCur() {
        return yCur;
    }

    public double getV0() {
        return v0;
    }

    public double getTheta() {
        return theta;
    }
    public void updatePosition(double deltaTime){
        this.time += deltaTime;
        xCur = x0 + vX*time;
        yCur = y0 + vY0*time - 0.5*g*time*time;
    }
}