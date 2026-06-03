import java.awt.Rectangle;
import java.awt.Graphics;
import java.awt.Color;

public class GameObject
{
    private int x,y,width,height;
    private Color color;
    public GameObject(int x, int y, int width, int height, Color color)
    {
        this.x=x;
        this.y=y;
        this.width=width;
        this.height=height;
        this.color=color;
    }

    public void draw(Graphics g)
    {

    }

    public Rectangle getBounds()
    {
        return new Rectangle(x,y,width,height);
    }

    public int getX()
    {
        return x;
    }

    public int getY()
    {
        return y;
    }

    public int getWidth()
    {
        return width;
    }

    public int getHeight()
    {
        return height;
    }

    public Color getColor()
    {
        return color;
    }

    public void setX(int x)
    {
        this.x=x;
    }

    public void setY(int y)
    {
        this.y=y;
    }
}
