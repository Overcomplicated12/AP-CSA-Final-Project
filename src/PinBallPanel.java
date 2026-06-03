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
    private ArrayList<Flipper> flippers;
    
    public PinBallPanel()
    {

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