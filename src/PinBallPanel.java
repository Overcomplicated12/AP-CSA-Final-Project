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
        leftFlipper = new Flipper(30,150,Color.WHITE, false);
        rightFlipper = new Flipper(300,150,Color.WHITE, true);
        launch = new Launcher(600,400,20,100,Color.RED);

        // add Bumper declaration with for loop(?) later
        setFocusable(true);

        this.addKeyListener(new MyKeyHandler());

        timer = new Timer(33,this);
        timer.start();
    }

    public void actionPerformed(ActionEvent e)
    {
        updateGame();
        checkBumpCollisions();
        checkFlipCollisions();
        checkWallCollisions();
        repaint();
    }

    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        ball.draw(g);
        leftFlipper.draw(g);
        rightFlipper.draw(g);
        launch.draw(g);
    }


    private void updateGame()
    {
        
        if (spacePressed && ball.getXSpeed() == 0 && ball.getYSpeed() == 0)
        {
            launch.startCharging();
            launch.charge();
            launch.launch(ball);
        }
        else
        {
            launch.stopCharging();
            launch.resetCharge();
        }

        if (leftPressed)
        {
            leftFlipper.setFlipping(true);
        }
        else
        {
            leftFlipper.setFlipping(false);
        }

        if (rightPressed)
        {
            rightFlipper.setFlipping(true);
        }
        else
        {
            rightFlipper.setFlipping(false);
        }

        ball.move(600,400); 
    }

    private void checkBumpCollisions()
    {
            for (Bumper bumper : bumpers) {
                if (ball.getBounds().intersects(bumper.getBounds())) {
                    // Simple collision response: reverse the ball's Y direction
                    ball.reverseY();
                    updateScore(10); // Award points for hitting a bumper
                }
            }
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