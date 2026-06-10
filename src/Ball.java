import java.awt.Color;
import java.awt.Graphics;

public class Ball extends GameObject
{
    private double xSpeed = 0;
    private double ySpeed = 0;

    public Ball(int x, int y, int diameter, Color color)
    {
        super(x, y, diameter, diameter, color);
    }

    public void move(int screenWidth, int screenHeight)
    {
        ySpeed += 0.25;

        setX((int)(getX() + xSpeed));
        setY((int)(getY() + ySpeed));

        if (getX() <= 0)
        {
            setX(0);
            reverseX();
        }
        else if (getX() + getWidth() >= screenWidth)
        {
            setX(screenWidth - getWidth());
            reverseX();
        }

        if (getY() <= 0)
        {
            setY(0);
            reverseY();
        }
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

    public void draw(Graphics g)
    {
        g.setColor(getColor());
        g.fillOval(getX(), getY(), getWidth(), getHeight());
    }
}
