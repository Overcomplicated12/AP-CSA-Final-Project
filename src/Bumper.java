import java.awt.Graphics;
import java.awt.Color;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;

public class Bumper extends GameObject
{
    public Bumper(int x, int y, int width, int height, Color color)
    {
        super(x, y, width, height, color);
    }

    public void draw(Graphics g)
    {
        g.setColor(getColor());
        g.fillOval(getX(), getY(), getWidth(), getHeight());

        g.setColor(Color.WHITE);
        g.drawOval(getX(), getY(), getWidth(), getHeight());
    }

    public Shape getShape()
    {
        return new Ellipse2D.Double(
            getX(),
            getY(),
            getWidth(),
            getHeight()
        );
    }
}