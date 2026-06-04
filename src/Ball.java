import java.awt.Color;
import java.awt.Graphics;

public class Ball extends GameObject
{
    private double xSpeed = 10;
    private double ySpeed = 10;
    public Ball(int x, int y, int diameter, Color color)
    {
        super(x,y,diameter,diameter,color);
    }

    public void move(int screenwidth, int screenheight)
    {

    }

    public double getXSpeed()
    {
        return xSpeed;
    }
    
    public double getYSpeed()
    {
        return ySpeed;
    }

    public void reverseX()
    {
        xSpeed *= -1;
    }

    public void reverseY()
    {
        ySpeed *= -1;
    }

    public void setXSpeed(double speed)
    {
        xSpeed = speed;
    }

    public void setYSpeed(double speed)
    {
        ySpeed = speed;
    }

    public void multXSpeed(double sp)
    {
        xSpeed *= sp;
    }

    public void multYSpeed(double sp)
    {
        ySpeed *= sp;
    }

    public void incXSpeed(double speed)
    {
        xSpeed += speed;
    }

    public void incYSpeed(double speed)
    {
        ySpeed += speed;
    }







    @Override
    public void draw(Graphics g)
    {
        g.setColor(getColor());
        g.fillOval(getX(), getY(), getWidth(), getHeight());
    }



}