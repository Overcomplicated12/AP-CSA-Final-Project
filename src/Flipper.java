import java.awt.Graphics;
import java.awt.Color;

public class Flipper extends GameObject
{
    private int angle;
    private int angleChange;
    private int xPivot;
    private int yPivot;

    public Flipper(int x, int y, int height, int width, Color color)
    {
        super(x,y,height,width,color);
    }

    public void moveUp(boolean isLeft)
    {

    }

    @Override
    public void draw(Graphics g)
    {

    }

    public int getAngle()
    {
        return angle;
    }

    public void moveDown(boolean isLeft)
    {
        
    }
}