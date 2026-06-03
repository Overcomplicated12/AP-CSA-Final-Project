import java.awt.*;
import java.awt.event.*;
import javax.swing.*;


public class PinBallPanel extends JPanel implements ActionListener
{
    private Ball ball;
    private int score;
    private Launcher launch;
    private Timer timer;
    private boolean leftPressed, rightPressed, spacePressed;
    private ArrayList<Bumper> bumpers;
    private Flipper leftFlipper, rightFlipper;
    
    public PinBallPanel()
    {
        setPreferredSize(new Dimension(600,400));
        setBackground(Color.DARK_GRAY);

        ball = new Ball(600,400,20,COLOR.CYAN);
        leftFlipper = new Flipper(30,150,15,80,Color.WHITE);
        rightFlipper = new Flipper(300,150,15,80,Color.WHITE);
        launch = new Launcher(600,400,20,100,Color.RED);

        // add Bumper declaration with for loop(?) later
        setFocusable(true)

        this.addKeyListener(new MyKeyHandler());

        timer = new Timer(33,this);
        timer.start();
    }

    public void actionPerformed(ActionEvent e)
    {

    }

    public void paintComponent(Graphics g)
    {

    }

    private void updateGame()
    {

    }

    private void checkBumpCollisions()
    {

    }

    private void checkFlipCollisions()
    {

    }

    private void checkWallCollisions()
    {

    }

    private void updateScore(int points)
    {
        score+=points;
    }

    private void createObjects()
    {
        
    }
}