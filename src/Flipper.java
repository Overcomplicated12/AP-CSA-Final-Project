import java.awt.Graphics;
import java.awt.Color;
import java.awt.geom.AffineTransform;

public class Flipper extends GameObject
{
    private int angle;
    private int angleChange;
    private double xPivot;
    private double yPivot;
    private boolean isLeft;

    public Flipper(int x, int y, int height, int width, Color color, boolean isLeft)
    {
        super(x,y,height,width,color);
        this.isLeft = isLeft;
        yPivot = y+height/2.0;
        if (isLeft)
        {
            xPivot = x;
        }
        else
        {
            xPivot = x+width;
        }
    }
    
    public void move(Graphics g, boolean up)
    {
        Graphics2D g2d = (Graphics2D) g;
        AffineTransform old = g2d.getTransform();
        double change = Math.toRadians(angleChange);
        if (isLeft&&up || !isLeft&&!up)
        {
            g2d.rotate(change,pivotX,pivotY);
        }
        else
        {
            double changeDif = change*-1;
            g2d.rotate(changeDif,pivotX,pivotY);
        }
        g2d.fillRect(getX(),getY(),getWidth(),getHeight());
        g2d.setTransform(old);
    }

    @Override
    public void draw(Graphics g)
    {
        g.setColor(getColor());
        g.fillRect(x,y,width,height);
    }

    public int getAngle()
    {
        return angle;
    }
}