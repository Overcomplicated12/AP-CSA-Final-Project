import java.awt.*;
import java.awt.geom.*;

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
            restingAngle = 25;
            maxAngle = -30;
        }
        else
        {
            restingAngle = 155;
            maxAngle = 210;
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
                angle -= flipSpeed;
            else
                angle += returnSpeed;

            if (angle < maxAngle)
                angle = maxAngle;

            if (angle > restingAngle)
                angle = restingAngle;
        }
        else
        {
            if (flipping)
                angle += flipSpeed;
            else
                angle -= returnSpeed;

            if (angle > maxAngle)
                angle = maxAngle;

            if (angle < restingAngle)
                angle = restingAngle;
        }
    }

    public void draw(Graphics g)
    {
        Graphics2D g2 = (Graphics2D) g;

        g2.setColor(getColor());
        g2.fill(getShape());

        g2.setColor(Color.WHITE);
        g2.fillOval(getX() - 8, getY() - 8, 16, 16);
    }

    public Shape getShape()
    {
        RoundRectangle2D rect = new RoundRectangle2D.Double(
            0,
            -thickness / 2.0,
            length,
            thickness,
            thickness,
            thickness
        );

        AffineTransform transform = new AffineTransform();
        transform.translate(getX(), getY());
        transform.rotate(Math.toRadians(angle));

        return transform.createTransformedShape(rect);
    }

    @Override
    public Rectangle getBounds()
    {
        return getShape().getBounds();
    }

    public void drawBounds(Graphics g)
    {
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(Color.GREEN);
        g2.draw(getShape());
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