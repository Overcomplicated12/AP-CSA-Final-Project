import java.awt.Graphics;
import java.awt.Color;

public class Launcher extends GameObject
{
    private int charge;
    private int maxCharge;
    private boolean charging;

    public Launcher(int x, int y, int height, int width, Color color)
    {
        super(x,y,height,width,color);
    }

    public void startCharging()
    {
        charging = true;
    }

    public void charge()
    {

    }

    public void launch(Ball ball)
    {

    }

    @Override
    public void draw(Graphics g)
    {
        
    }

}
