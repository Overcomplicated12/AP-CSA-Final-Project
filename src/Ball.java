import java.awt.Color;
import java.awt.Graphics;

public class Ball extends GameObject
{
    private int xSpeed = 10;
    private int ySpeed = 10;
    public Ball(int x, int y, int diameter, Color color)
    {
        super(x,y,diameter,diameter,color);
    }

    public void move(int screenwidth, int screenheight)
    {

    }

    public double getXSpeed()
    {
        return xspeed;
    }
    
    public double getYSpeed()
    {
        return yspeed;
    }

    public void reverseX()
    {
        xspeed*=-1;
    }

    public void reverseY()
    {
        yspeed*=-1;
    }

    public void setXSpeed(double speed)
    {
        xspeed = speed;
    }

    public void setYSpeed(double speed)
    {
        yspeed = speed;
    }

    public void multXSpeed(double sp)
    {
        xspeed*=sp;
    }

    public void multYSpeed(double sp)
    {
        yspeed*=sp;
    }

    public void incXSpeed(double speed)
    {
        xspeed+=speed;
    }

    public void incYSpeed(double speed)
    {
        yspeed+=speed;
    }







    @Override
    public void draw(Graphics g)
    {
        g.setColor(getColor())
    }



}