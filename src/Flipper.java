import java.awt.*;
import java.awt.geom.AffineTransform;

public class Flipper {

    private int pivotX;
    private int pivotY;

    private int length;
    private int thickness;

    private double angle;

    private double restingAngle;
    private double maxAngle;

    private double flipSpeed;
    private double returnSpeed;

    private boolean flipping;

    private Color color;

    public Flipper(int x, int y) {

        pivotX = x;
        pivotY = y;

        length = 100;
        thickness = 18;

        restingAngle = 25;
        maxAngle = -45;

        angle = restingAngle;

        flipSpeed = 20;
        returnSpeed = 8;

        color = Color.RED;
    }

    public void update() {

        if (flipping) {

            angle -= flipSpeed;

            if (angle < maxAngle) {
                angle = maxAngle;
            }

        } else {

            angle += returnSpeed;

            if (angle > restingAngle) {
                angle = restingAngle;
            }
        }
    }

    public void draw(Graphics g) {

        Graphics2D g2 = (Graphics2D) g;

        AffineTransform old = g2.getTransform();

        g2.translate(pivotX, pivotY);

        g2.rotate(Math.toRadians(angle));

        g2.setColor(color);

        g2.fillRoundRect(
                0,
                -thickness / 2,
                length,
                thickness,
                thickness,
                thickness
        );

        g2.setColor(Color.WHITE);

        g2.fillOval(-8, -8, 16, 16);

        g2.setTransform(old);
    }

    public void setFlipping(boolean f) {
        flipping = f;
    }

    public boolean isFlipping() {
        return flipping;
    }

    public double getAngle() {
        return angle;
    }

    public void setFlipSpeed(double speed) {
        flipSpeed = speed;
    }

    public void setReturnSpeed(double speed) {
        returnSpeed = speed;
    }

    public void setColor(Color c) {
        color = c;
    }
}