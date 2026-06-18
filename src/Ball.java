import java.awt.*;
import java.awt.geom.Ellipse2D;

public class Ball extends GameObject
{
    private double xSpeed;
    private double ySpeed;

    public Ball(int x, int y, int diameter, Color c)
    {
        super(x, y, diameter, diameter, c);
        xSpeed = 0;
        ySpeed = 0;
    }

    public void move(int screenWidth, int screenHeight)
    {
        setX((int)(getX() + xSpeed));
        setY((int)(getY() + ySpeed));

        ySpeed += 0.25; // gravity

        if (getX() <= 0 || getX() + getWidth() >= screenWidth)
        {
            reverseX();
        }

        if (getY() <= 0)
        {
            reverseY();
        }
    }

    public void draw(Graphics g)
    {
        g.setColor(getColor());
        g.fillOval(getX(), getY(), getWidth(), getHeight());
    }

    public Ellipse2D getShape()
    {
        return new Ellipse2D.Double(
            getX(),
            getY(),
            getWidth(),
            getHeight()
        );
    }

    @Override
    public Rectangle getBounds()
    {
        return getShape().getBounds();
    }
    // for debugging purposes, draw the hitbox of the ball
    public void drawHitbox(Graphics g)
    {
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(Color.GREEN);
        g2.draw(getShape());
    }

    public double getXSpeed()
    {
        return xSpeed;
    }

    public double getYSpeed()
    {
        return ySpeed;
    }

    public void setXSpeed(double speed)
    {
        xSpeed = speed;
    }

    public void setYSpeed(double speed)
    {
        ySpeed = speed;
    }

    public void reverseX()
    {
        xSpeed *= -1;
    }

    public void reverseY()
    {
        ySpeed *= -1;
    }
}