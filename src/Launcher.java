import java.awt.Color;
import java.awt.Graphics;

public class Launcher extends GameObject {
    private int charge;
    private int maxCharge;
    private boolean charging;

    public Launcher(int x, int y, int width, int height, Color color) {
        super(x, y, width, height, color);

        charge = 0;
        maxCharge = 100;
        charging = false;
    }

    public void startCharging() {
        charging = true;
    }

    public void charge() {
        if (charging && charge < maxCharge) {
            charge++;
        }
    }

    public void launch(Ball ball) {
        double launchPower = 8 + charge * 0.15;

        ball.setYSpeed(-launchPower);
        ball.setXSpeed(-launchPower * 0.1);

        stopCharging();
        resetCharge();
    }

    public void stopCharging() {
        charging = false;
    }

    public void resetCharge() {
        charge = 0;
    }

    public int getCharge() {
        return charge;
    }

    public boolean isCharging() {
        return charging;
    }

    public void draw(Graphics g) {
        g.setColor(getColor());

        int compressedAmount = charge / 3;

        g.fillRect(
            getX(),
            getY() + compressedAmount,
            getWidth(),
            getHeight() - compressedAmount
        );

        g.setColor(Color.GRAY);
        g.drawRect(getX(), getY(), getWidth(), getHeight());
    }
}
