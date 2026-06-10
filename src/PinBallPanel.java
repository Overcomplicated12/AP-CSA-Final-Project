import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.*;

public class PinBallPanel extends JPanel implements ActionListener, KeyListener
{
    private Ball ball;
    private int score;
    private Launcher launch;
    private Timer timer;

    private boolean ballInLauncher = true;
    private boolean spacePressed;
    private boolean wasSpacePressed;

    private ArrayList<Bumper> bumpers;
    private Flipper leftFlipper;
    private Flipper rightFlipper;

    private final int WIDTH = 300;
    private final int HEIGHT = 700;

    public PinBallPanel()
    {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(Color.DARK_GRAY);

        launch = new Launcher(WIDTH - 50, HEIGHT - 180, 25, 100, Color.RED);

        ball = new Ball(launch.getX() + 3,
                        launch.getY() - 20,
                        20,
                        Color.CYAN);

        ball.setXSpeed(0);
        ball.setYSpeed(0);

        leftFlipper = new Flipper(0, HEIGHT-50, Color.WHITE, true);
        rightFlipper = new Flipper(250, HEIGHT-50, Color.WHITE, false);

        bumpers = new ArrayList<Bumper>();
        createObjects();

        score = 0;

        setFocusable(true);
        addKeyListener(this);

        timer = new Timer(16, this);
        timer.start();
    }

    public void addNotify()
    {
        super.addNotify();
        requestFocusInWindow();
    }

    public void actionPerformed(ActionEvent e)
    {
        updateGame();

        if (!ballInLauncher)
        {
            checkBumpCollisions();
            checkFlipCollisions();
            checkWallCollisions();
        }

        repaint();
    }

    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);

        drawBackground(g);

        for (Bumper bumper : bumpers)
        {
            bumper.draw(g);
        }

        launch.draw(g);
        ball.draw(g);
        leftFlipper.draw(g);
        rightFlipper.draw(g);

        g.setColor(Color.WHITE);
        g.drawString("Score: " + score, 20, 20);
        g.drawString("LEFT / RIGHT = flippers", 20, 40);
        g.drawString("Hold SPACE, release to launch", 20, 60);
        g.drawString("Launcher charge: " + launch.getCharge(), 20, 80);
        leftFlipper.drawBounds(g);
rightFlipper.drawBounds(g);
    }

    private void drawBackground(Graphics g)
    {
        g.setColor(Color.GRAY);
        g.drawRect(0, 0, WIDTH - 1, HEIGHT - 1);

        g.drawLine(WIDTH - 75, 0, WIDTH - 75, HEIGHT);
    }

    private void updateGame()
    {
        leftFlipper.update();
        rightFlipper.update();

        if (ballInLauncher)
        {
            keepBallAtLauncherTop();

            if (spacePressed)
            {
                launch.startCharging();
                launch.charge();
            }

            if (wasSpacePressed && !spacePressed)
            {
                ballInLauncher = false;
                launch.launch(ball);
                launch.stopCharging();
                launch.resetCharge();
            }

            wasSpacePressed = spacePressed;
            return;
        }

        ball.move(WIDTH, HEIGHT);
    }

    private void keepBallAtLauncherTop()
    {
        ball.setX(launch.getX() + 3);
        ball.setY(launch.getY() - ball.getHeight());
        ball.setXSpeed(0);
        ball.setYSpeed(0);
    }

    private void checkBumpCollisions()
    {
        for (Bumper bumper : bumpers)
        {
            if (ball.getBounds().intersects(bumper.getBounds()))
            {
                ball.reverseY();
                ball.multXSpeed(-1);
                updateScore(10);
            }
        }
    }

    private void checkFlipCollisions()
    {
        if (ball.getBounds().intersects(leftFlipper.getBounds()))
        {
            ball.setYSpeed(-10);
            ball.setXSpeed(-4);

            if (leftFlipper.isFlipping())
            {
                ball.setYSpeed(-14);
                ball.setXSpeed(-6);
            }
        }

        if (ball.getBounds().intersects(rightFlipper.getBounds()))
        {
            ball.setYSpeed(-10);
            ball.setXSpeed(4);

            if (rightFlipper.isFlipping())
            {
                ball.setYSpeed(-14);
                ball.setXSpeed(6);
            }
        }
    }

    private void checkWallCollisions()
    {
        if (ball.getY() > HEIGHT)
        {
            resetBall();
            score = Math.max(0, score - 50);
        }
    }

    private void resetBall()
    {
        ballInLauncher = true;
        spacePressed = false;
        wasSpacePressed = false;

        keepBallAtLauncherTop();

        launch.stopCharging();
        launch.resetCharge();
    }

    private void updateScore(int points)
    {
        score += points;
    }

    private void createObjects()
    {
        bumpers.add(new Bumper(130, 140, 45, 45, Color.YELLOW));
        bumpers.add(new Bumper(240, 160, 45, 45, Color.GREEN));
        bumpers.add(new Bumper(185, 260, 45, 45, Color.ORANGE));
    }

    public void keyPressed(KeyEvent e)
    {
        int code = e.getKeyCode();

        if (code == KeyEvent.VK_LEFT)
        {
            leftFlipper.setFlipping(true);
        }
        else if (code == KeyEvent.VK_RIGHT)
        {
            rightFlipper.setFlipping(true);
        }
        else if (code == KeyEvent.VK_SPACE)
        {
            spacePressed = true;
        }
    }

    public void keyReleased(KeyEvent e)
    {
        int code = e.getKeyCode();

        if (code == KeyEvent.VK_LEFT)
        {
            leftFlipper.setFlipping(false);
        }
        else if (code == KeyEvent.VK_RIGHT)
        {
            rightFlipper.setFlipping(false);
        }
        else if (code == KeyEvent.VK_SPACE)
        {
            spacePressed = false;
        }
    }

    public void keyTyped(KeyEvent e)
    {
    }
}