import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.geom.AffineTransform;

public class Flipper extends GameObject
{
    private int angle;
    private int angleChange;
    private double xPivot;
    private double yPivot;
    private boolean isLeft;

    public Flipper(int x, int y, int width, int height, Color color, boolean isLeft)
    {
        super(x,y,width,height,color);
        this.isLeft = isLeft;
        yPivot = y + height / 2.0;
        if (isLeft)
        {
            xPivot = x;
        }
        else
        {
            xPivot = x + width;
        }
    }
    
    public void move(Graphics g, boolean up)
    {
        Graphics2D g2d = (Graphics2D) g;
        AffineTransform old = g2d.getTransform();
        double change = Math.toRadians(angleChange);
        if ((isLeft && up) || (!isLeft && !up))
        {
            g2d.rotate(change, xPivot, yPivot);
        }
        else
        {
            double changeDif = -change;
            g2d.rotate(changeDif, xPivot, yPivot);
        }
        g2d.fillRect(getX(), getY(), getWidth(), getHeight());
        g2d.setTransform(old);
    }

    @Override
    public void draw(Graphics g)
    {
        g.setColor(getColor());
        g.fillRect(getX(), getY(), getWidth(), getHeight());
    }

    public int getAngle()
    {
        return angle;
    }
}