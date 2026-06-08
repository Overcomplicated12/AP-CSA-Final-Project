import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
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

        ball = new Ball(600,400,20,Color.CYAN);
        leftFlipper = new Flipper(30,150,15,80,Color.WHITE, true);
        rightFlipper = new Flipper(300,150,15,80,Color.WHITE, false);
        launch = new Launcher(600,400,20,100,Color.RED);

        // add Bumper declaration with for loop(?) later
        setFocusable(true);

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
        if (ball.getX() <= 0 || ball.getX() + ball.getWidth() >= getWidth()) {
            ball.reverseX();
        }

        if (ball.getY() <= 0) {
            ball.reverseY();
        }

        if (ball.getY() > getHeight()) {
            ball.setX(535);
            ball.setY(120);
            ball.setXSpeed(0);
            ball.setYSpeed(0);

            score = Math.max(0, score - 50);
        }
    }

    private void updateScore(int points)
    {
        score+=points;
    }

    private void createObjects()
    {
        
    }

    private class MyKeyHandler implements KeyListener
    {
        @Override
        public void keyTyped(KeyEvent e) {}

        @Override
        public void keyPressed(KeyEvent e)
        {
            int code = e.getKeyCode();
            if (code == KeyEvent.VK_LEFT)
            {
                leftPressed = true;
            }
            else if (code == KeyEvent.VK_RIGHT)
            {
                rightPressed = true;
            }
            else if (code == KeyEvent.VK_SPACE)
            {
                spacePressed = true;
            }
        }

        @Override
        public void keyReleased(KeyEvent e)
        {
            int code = e.getKeyCode();
            if (code == KeyEvent.VK_LEFT)
            {
                leftPressed = false;
            }
            else if (code == KeyEvent.VK_RIGHT)
            {
                rightPressed = false;
            }
            else if (code == KeyEvent.VK_SPACE)
            {
                spacePressed = false;
            }
        }
    }}