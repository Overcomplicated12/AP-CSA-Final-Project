import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Area;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import javax.swing.*;

public class PinBallPanel extends JPanel implements ActionListener, KeyListener {
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

    private Line2D leftGuideWall;
    private Line2D rightGuideWall;

    private final int WIDTH = 500;
    private final int HEIGHT = 800;

    public PinBallPanel() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(Color.DARK_GRAY);

        launch = new Launcher(WIDTH - 70, HEIGHT - 230, 30, 140, Color.RED);

        ball = new Ball(
                launch.getX() + 5,
                launch.getY() - 20,
                20,
                Color.CYAN);

        ball.setXSpeed(0);
        ball.setYSpeed(0);

        leftFlipper = new Flipper(130, HEIGHT - 105, Color.WHITE, true);
        rightFlipper = new Flipper(WIDTH - 130, HEIGHT - 105, Color.WHITE, false);

        leftGuideWall = new Line2D.Double(
                0, HEIGHT - 166,
                130, HEIGHT - 105);

        rightGuideWall = new Line2D.Double(
                WIDTH, HEIGHT - 166,
                WIDTH - 130, HEIGHT - 105);

        bumpers = new ArrayList<Bumper>();
        createObjects();

        score = 0;

        setFocusable(true);
        addKeyListener(this);

        timer = new Timer(16, this);
        timer.start();
    }

    public void addNotify() {
        super.addNotify();
        requestFocusInWindow();
    }

    public void actionPerformed(ActionEvent e) {
        updateGame();

        if (!ballInLauncher) {
            checkBumpCollisions();
            checkGuideWallCollisions();
            checkFlipCollisions();
            checkWallCollisions();
        }

        repaint();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        drawBackground(g);

        for (Bumper bumper : bumpers) {
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

    private void drawBackground(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;

        g2.setColor(Color.GRAY);
        g2.drawRect(0, 0, WIDTH - 1, HEIGHT - 1);

        // launcher lane
        g2.drawLine(WIDTH - 95, 0, WIDTH - 95, HEIGHT);

        // angled guide walls near flippers
        g2.setColor(Color.WHITE);
        g2.setStroke(new BasicStroke(15));

        g2.draw(leftGuideWall);
        g2.draw(rightGuideWall);

        g2.setStroke(new BasicStroke(1));
    }

    private void updateGame() {
        leftFlipper.update();
        rightFlipper.update();

        if (ballInLauncher) {
            keepBallAtLauncherTop();

            if (spacePressed) {
                launch.startCharging();
                launch.charge();
            }

            if (wasSpacePressed && !spacePressed) {
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

    private void keepBallAtLauncherTop() {
        ball.setX(launch.getX() + 5);
        ball.setY(launch.getY() - ball.getHeight());
        ball.setXSpeed(0);
        ball.setYSpeed(0);
    }

    private boolean shapesIntersect(Shape s1, Shape s2) {
        Area area = new Area(s1);
        area.intersect(new Area(s2));
        return !area.isEmpty();
    }

    private void checkBumpCollisions() {
        for (Bumper bumper : bumpers) {
            if (ball.getBounds().intersects(bumper.getBounds())) {
                ball.reverseY();
                ball.multXSpeed(-1);
                updateScore(10);
            }
        }
    }

    private void checkGuideWallCollisions() {
        checkOneGuideWall(leftGuideWall, true);
        checkOneGuideWall(rightGuideWall, false);
    }

    private void checkOneGuideWall(Line2D wall, boolean isLeftWall) {
        Shape wallShape = new BasicStroke(15).createStrokedShape(wall);

        if (shapesIntersect(ball.getShape(), wallShape)) {
            pushBallOutOfWall(wallShape, isLeftWall);

            double wallBoost = 1;
            double wallFriction = 0.1;

            if (isLeftWall) {
                ball.setXSpeed(Math.abs(ball.getXSpeed()) + wallBoost);
            } else {
                ball.setXSpeed(-Math.abs(ball.getXSpeed()) - wallBoost);
            }

            ball.setYSpeed(ball.getYSpeed() * wallFriction);
        }
    }

    private void pushBallOutOfWall(Shape wallShape, boolean isLeftWall) {
        int count = 0;

        while (shapesIntersect(ball.getShape(), wallShape) && count < 50) {
            if (isLeftWall) {
                ball.setX(ball.getX() + 1);
            } else {
                ball.setX(ball.getX() - 1);
            }

            count++;
        }
    }

    private void checkFlipCollisions() {
        if (shapesIntersect(ball.getShape(), leftFlipper.getShape())) {
            pushBallOutOfFlipper(leftFlipper);

            if (leftFlipper.isMovingUp()) {
                ball.setYSpeed(-14);
                ball.setXSpeed(-6);
            } else {
                rollDownFlipper(leftFlipper);
            }
        }

        if (shapesIntersect(ball.getShape(), rightFlipper.getShape())) {
            pushBallOutOfFlipper(rightFlipper);

            if (rightFlipper.isMovingUp()) {
                ball.setYSpeed(-14);
                ball.setXSpeed(6);
            } else {
                rollDownFlipper(rightFlipper);
            }
        }
    }

    private void pushBallOutOfFlipper(Flipper flipper) {
        int count = 0;

        while (shapesIntersect(ball.getShape(), flipper.getShape()) && count < 50) {
            ball.setY(ball.getY() - 1);
            count++;
        }
    }

    private void rollDownFlipper(Flipper flipper) {
        double angle = Math.toRadians(flipper.getAngle());

        double rollX;

        if (flipper.isLeftSide()) {
            rollX = Math.cos(angle); // left rolls right
        } else {
            rollX = Math.cos(angle); // right angle already points left
        }

        double rollSpeed = 2.0;

        ball.setXSpeed(rollX * rollSpeed);

        // small bounce, but not enough to roll upward forever
        double bounce = -1;

        // always force the final roll direction downward
        ball.setYSpeed(Math.abs(Math.sin(angle) * rollSpeed) + bounce);

        if (ball.getYSpeed() < 0.3) {
            ball.setYSpeed(0.3);
        }
    }

    private void checkWallCollisions() {
        if (ball.getY() > HEIGHT) {
            resetBall();
            score = Math.max(0, score - 50);
        }
    }

    private void resetBall() {
        ballInLauncher = true;
        spacePressed = false;
        wasSpacePressed = false;

        keepBallAtLauncherTop();

        launch.stopCharging();
        launch.resetCharge();
    }

    private void updateScore(int points) {
        score += points;
    }

    private void createObjects() {
        bumpers.add(new Bumper(150, 160, 45, 45, Color.YELLOW));
        bumpers.add(new Bumper(260, 180, 45, 45, Color.GREEN));
        bumpers.add(new Bumper(210, 290, 45, 45, Color.ORANGE));
    }

    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();

        if (code == KeyEvent.VK_LEFT) {
            leftFlipper.setFlipping(true);
        } else if (code == KeyEvent.VK_RIGHT) {
            rightFlipper.setFlipping(true);
        } else if (code == KeyEvent.VK_SPACE) {
            spacePressed = true;
        }
    }

    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();

        if (code == KeyEvent.VK_LEFT) {
            leftFlipper.setFlipping(false);
        } else if (code == KeyEvent.VK_RIGHT) {
            rightFlipper.setFlipping(false);
        } else if (code == KeyEvent.VK_SPACE) {
            spacePressed = false;
        }
    }

    public void keyTyped(KeyEvent e) {
    }
}