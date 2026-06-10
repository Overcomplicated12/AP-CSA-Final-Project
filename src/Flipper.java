import java.awt.*;
import java.awt.geom.AffineTransform;

public class Flipper extends GameObject
{
    private int length;
    private int thickness;

    private double angle;
    private double restingAngle;
    private double maxAngle;

    private double flipSpeed;
    private double returnSpeed;

    private boolean flipping;
    private boolean leftSide;

    public Flipper(int x, int y, Color c, boolean left)
    {
        super(x, y, 100, 18, c);

        length = 100;
        thickness = 18;

        leftSide = left;

        flipSpeed = 12;
        returnSpeed = 8;

        if (leftSide)
        {
            restingAngle = -25;
            maxAngle = -70;
        }
        else
        {
            restingAngle = 205;
            maxAngle = 250;
        }

        angle = restingAngle;
        flipping = false;
    }

    public Flipper(int x, int y)
    {
        this(x, y, Color.RED, true);
    }

    public void update()
    {
        if (leftSide)
        {
            if (flipping)
            {
                angle -= flipSpeed;

                if (angle < maxAngle)
                {
                    angle = maxAngle;
                }
            }
            else
            {
                angle += returnSpeed;

                if (angle > restingAngle)
                {
                    angle = restingAngle;
                }
            }
        }
        else
        {
            if (flipping)
            {
                angle += flipSpeed;

                if (angle > maxAngle)
                {
                    angle = maxAngle;
                }
            }
            else
            {
                angle -= returnSpeed;

                if (angle < restingAngle)
                {
                    angle = restingAngle;
                }
            }
        }
    }

    public void draw(Graphics g)
    {
        Graphics2D g2 = (Graphics2D) g;

        AffineTransform old = g2.getTransform();

        g2.translate(getX(), getY());
        g2.rotate(Math.toRadians(angle));

        g2.setColor(getColor());
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

    public void setFlipping(boolean f)
    {
        flipping = f;
    }

    public boolean isFlipping()
    {
        return flipping;
    }

    public double getAngle()
    {
        return angle;
    }

    public boolean isLeftSide()
    {
        return leftSide;
    }

    public int getLength()
    {
        return length;
    }

    public int getThickness()
    {
        return thickness;
    }
}