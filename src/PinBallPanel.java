import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Area;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import javax.swing.*;

public class PinBallPanel extends JPanel implements ActionListener, KeyListener {
    private Ball ball;
    private int score;
    private int lives = 3;

    private Launcher launch;
    private Timer timer;
    // Game state variables
    private boolean ballInLauncher = true;
    private boolean spacePressed;
    private boolean wasSpacePressed;
    private boolean ballHasExitedLauncherLane = false;
    private boolean gameOver = false;

    private ArrayList<Bumper> bumpers;
    private Flipper leftFlipper;
    private Flipper rightFlipper;

    private Line2D leftGuideWall;
    private Line2D rightGuideWall;

    private enum Difficulty {
        EASY, NORMAL, HARD
    }

    private Difficulty difficulty = null;
    private double bumperBounceSpeed = 7.0;

    private final BasicStroke guideWallStroke = new BasicStroke(
            15,
            BasicStroke.CAP_BUTT,
            BasicStroke.JOIN_MITER);

    private final int WIDTH = 500;
    private final int HEIGHT = 800;

    // Constructor
    public PinBallPanel() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(Color.DARK_GRAY);

        launch = new Launcher(WIDTH - 30, HEIGHT - 140, 30, 140, Color.RED);

        ball = new Ball(
                launch.getX() + 5,
                launch.getY() - 20,
                20,
                Color.CYAN);

        ball.setXSpeed(0);
        ball.setYSpeed(0);

        leftFlipper = new Flipper(120, HEIGHT - 105, Color.WHITE, true);
        rightFlipper = new Flipper(360, HEIGHT - 105, Color.WHITE, false);

        createGuideWalls();

        bumpers = new ArrayList<Bumper>();
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

    // Main game loop
    public void actionPerformed(ActionEvent e) {
        if (difficulty == null || gameOver) {
            repaint();
            return;
        }

        updateGame();

        if (!ballInLauncher) {
            checkBumpCollisions();
            checkGuideWallCollisions();
            checkFlipCollisions();
            checkWallCollisions();
        }

        repaint();
    }

    // Render current screen
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (difficulty == null) {
            drawStartScreen(g);
            return;
        }

        if (gameOver) {
            drawEndScreen(g);
            return;
        }

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
        g.drawString("Lives: " + lives, 20, 40);
        g.drawString("Difficulty: " + difficulty, 20, 60);
        g.drawString("LEFT / RIGHT = flippers", 20, 80);
        g.drawString("Hold SPACE, release to launch", 20, 100);
        g.drawString("Launcher charge: " + launch.getCharge(), 20, 120);

    }

    // Difficulty selection screen
    private void drawStartScreen(Graphics g) {
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 42));
        g.drawString("PINBALL", 155, 230);

        g.setFont(new Font("Arial", Font.PLAIN, 24));
        g.drawString("Press 1 - Easy", 170, 320);
        g.drawString("Press 2 - Normal", 170, 365);
        g.drawString("Press 3 - Hard", 170, 410);

        g.setFont(new Font("Arial", Font.PLAIN, 14));
        g.drawString("Harder modes have bigger flipper gaps and stronger bumper bounces.", 30, 470);
    }

    // Game over screen
    private void drawEndScreen(Graphics g) {
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 42));
        g.drawString("GAME OVER", 125, 300);

        g.setFont(new Font("Arial", Font.PLAIN, 28));
        g.drawString("Final Score: " + score, 150, 360);

        g.setFont(new Font("Arial", Font.PLAIN, 18));
        g.drawString("Press 1 (easy), 2 (normal), or 3 (hard) to play again", 50, 430);
    }

    // Position guide walls from flipper pivots
    private void createGuideWalls() {
        double leftAngle = Math.toRadians(leftFlipper.getRestingAngle());
        double rightAngle = Math.toRadians(rightFlipper.getRestingAngle());

        double leftPivotX = leftFlipper.getX();
        double leftPivotY = leftFlipper.getY();

        double rightPivotX = rightFlipper.getX();
        double rightPivotY = rightFlipper.getY();

        double leftStartX = 0;
        double rightStartX = WIDTH - 35;

        double leftRun = leftPivotX - leftStartX;
        double leftRise = Math.tan(leftAngle) * leftRun;

        leftGuideWall = new Line2D.Double(
                leftStartX,
                leftPivotY - leftRise,
                leftPivotX,
                leftPivotY);

        double rightRun = rightStartX - rightPivotX;
        double rightRise = Math.tan(Math.PI - rightAngle) * rightRun;

        rightGuideWall = new Line2D.Double(
                rightStartX,
                rightPivotY - rightRise,
                rightPivotX,
                rightPivotY);
    }

    // Draw playfield walls
    private void drawBackground(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;

        g2.setColor(Color.GRAY);
        g2.drawRect(0, 0, WIDTH - 1, HEIGHT - 1);

        g2.drawLine(WIDTH - 30, 0, WIDTH - 30, HEIGHT);

        g2.setColor(Color.WHITE);
        g2.setStroke(guideWallStroke);

        g2.draw(leftGuideWall);
        g2.draw(rightGuideWall);

        g2.setStroke(new BasicStroke(1));
    }

    // Update movement and launcher state
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
                ballHasExitedLauncherLane = false;

                launch.launch(ball);

                launch.stopCharging();
                launch.resetCharge();
            }

            wasSpacePressed = spacePressed;
            return;
        }

        ball.move(WIDTH, HEIGHT);
    }

    // Hold ball at launcher
    private void keepBallAtLauncherTop() {
        ball.setX(launch.getX() + 5);
        ball.setY(launch.getY() - ball.getHeight());
        ball.setXSpeed(0);
        ball.setYSpeed(0);
    }

    // Shape collision helper
    private boolean shapesIntersect(Shape s1, Shape s2) {
        Area area = new Area(s1);
        area.intersect(new Area(s2));
        return !area.isEmpty();
    }

    // Bumper collisions
    private void checkBumpCollisions() {
        for (Bumper bumper : bumpers) {
            if (shapesIntersect(ball.getShape(), bumper.getShape())) {
                bounceOffCircularBumper(bumper);
                updateScore(10);
            }
        }
    }

    // Circular bumper bounce using normal vector from collision point, with a small adjustment to prevent vertical bouncing on top of bumpers
    private void bounceOffCircularBumper(Bumper bumper) {
        double ballCenterX = ball.getX() + ball.getWidth() / 2.0;
        double ballCenterY = ball.getY() + ball.getHeight() / 2.0;

        double bumperCenterX = bumper.getX() + bumper.getWidth() / 2.0;
        double bumperCenterY = bumper.getY() + bumper.getHeight() / 2.0;

        double dx = ballCenterX - bumperCenterX;
        double dy = ballCenterY - bumperCenterY;

        double distance = Math.sqrt(dx * dx + dy * dy);

        if (distance == 0) {
            distance = 1;
        }

        double normalX = dx / distance;
        double normalY = dy / distance;

        // Prevent repeated vertical bounces on top of bumpers
        if (Math.abs(normalX) < 0.15 && normalY < 0) {
            normalX = -0.2; // slight push to the left

            double length = Math.sqrt(normalX * normalX + normalY * normalY);
            normalX /= length;
            normalY /= length;
        }

        ball.setXSpeed(normalX * bumperBounceSpeed);
        ball.setYSpeed(normalY * bumperBounceSpeed);

        double ballRadius = ball.getWidth() / 2.0;
        double bumperRadius = bumper.getWidth() / 2.0;

        double targetDistance = ballRadius + bumperRadius + 2;

        ball.setX((int) (bumperCenterX + normalX * targetDistance - ballRadius));
        ball.setY((int) (bumperCenterY + normalY * targetDistance - ballRadius));
    }

    // Guide wall collisions
    private void checkGuideWallCollisions() {
        Shape leftWallShape = guideWallStroke.createStrokedShape(leftGuideWall);
        Shape rightWallShape = guideWallStroke.createStrokedShape(rightGuideWall);

        checkOneGuideWall(leftWallShape, true);
        checkOneGuideWall(rightWallShape, false);
    }

    // Single guide wall response
    private void checkOneGuideWall(Shape wallShape, boolean isLeftWall) {
        if (shapesIntersect(ball.getShape(), wallShape)) {
            pushBallOutOfGuideWall(wallShape, isLeftWall);

            double bounceStrength = 0.75;
            double minimumBounce = 1.0;
            double wallFriction = 0.65;
            double upwardBounce = 4.0;

            if (isLeftWall) {
                ball.setXSpeed(Math.abs(ball.getXSpeed()) * bounceStrength + minimumBounce);
            } else {
                ball.setXSpeed(-Math.abs(ball.getXSpeed()) * bounceStrength - minimumBounce);
            }

            ball.setYSpeed(ball.getYSpeed() * wallFriction - upwardBounce);
        }
    }

    // Prevent wall clipping
    private void pushBallOutOfGuideWall(Shape wallShape, boolean isLeftWall) {
        int count = 0;

        while (shapesIntersect(ball.getShape(), wallShape) && count < 50) {
            if (isLeftWall) {
                ball.setX(ball.getX() + 1);
                ball.setY(ball.getY() - 1);
            } else {
                ball.setX(ball.getX() - 1);
                ball.setY(ball.getY() - 1);
            }

            count++;
        }
    }

    // Flipper collisions
    private void checkFlipCollisions() {
        if (shapesIntersect(ball.getShape(), leftFlipper.getShape())) {
            pushBallOutOfFlipper(leftFlipper);

            if (leftFlipper.isMovingUp()) {
                bounceOffMovingFlipper(leftFlipper);
            } else {
                rollDownFlipper(leftFlipper);
            }
        }

        if (shapesIntersect(ball.getShape(), rightFlipper.getShape())) {
            pushBallOutOfFlipper(rightFlipper);

            if (rightFlipper.isMovingUp()) {
                bounceOffMovingFlipper(rightFlipper);
            } else {
                rollDownFlipper(rightFlipper);
            }
        }
    }

    // Moving flipper bounce
    private void bounceOffMovingFlipper(Flipper flipper) {
        double ballCenterX = ball.getX() + ball.getWidth() / 2.0;
        double ballCenterY = ball.getY() + ball.getHeight() / 2.0;

        double pivotX = flipper.getX();
        double pivotY = flipper.getY();

        double angle = Math.toRadians(flipper.getAngle());

        double tangentX = Math.cos(angle);
        double tangentY = Math.sin(angle);

        double toBallX = ballCenterX - pivotX;
        double toBallY = ballCenterY - pivotY;

        double distanceAlongFlipper = toBallX * tangentX + toBallY * tangentY;

        double hitRatio = distanceAlongFlipper / flipper.getLength();

        if (hitRatio < 0) {
            hitRatio = 0;
        }

        if (hitRatio > 1) {
            hitRatio = 1;
        }

        double minPower = 7.0;
        double maxPower = 17.0;

        double power = minPower + (maxPower - minPower) * hitRatio;

        double directionX;

        if (flipper.isLeftSide()) {
            directionX = 1;
        } else {
            directionX = -1;
        }

        ball.setXSpeed(directionX * power * 0.45);
        ball.setYSpeed(-power);
    }

    // Prevent flipper clipping
    private void pushBallOutOfFlipper(Flipper flipper) {
        double angle = Math.toRadians(flipper.getAngle());

        double normalX = -Math.sin(angle);
        double normalY = Math.cos(angle);

        if (normalY > 0) {
            normalX = -normalX;
            normalY = -normalY;
        }

        int count = 0;

        while (shapesIntersect(ball.getShape(), flipper.getShape()) && count < 50) {
            ball.setX(ball.getX() + (int) Math.round(normalX));
            ball.setY(ball.getY() + (int) Math.round(normalY));
            count++;
        }
    }

    // Resting flipper roll, small bounce based on angle
    private void rollDownFlipper(Flipper flipper) {
        double angle = Math.toRadians(flipper.getAngle());

        double rollSpeed = 2.0;
        double bounceStrength = 1.5;

        double rollX;
        double rollY = Math.abs(Math.sin(angle));

        boolean fullyExtended = Math.abs(flipper.getAngle() - flipper.getMaxAngle()) < 2.0;

        if (flipper.isLeftSide()) {
            if (fullyExtended) {
                rollX = -Math.abs(Math.cos(angle));
            } else {
                rollX = Math.abs(Math.cos(angle));
            }
        } else {
            if (fullyExtended) {
                rollX = Math.abs(Math.cos(angle));
            } else {
                rollX = -Math.abs(Math.cos(angle));
            }
        }

        ball.setXSpeed(rollX * rollSpeed);

        ball.setYSpeed(rollY * rollSpeed - bounceStrength);

        if (ball.getYSpeed() < -1.0) {
            ball.setYSpeed(-1.0);
        }
    }

    // Outer wall and drain collisions
    private void checkWallCollisions() {
        double wallRestitution = 0.6;

        int wallX = WIDTH - 30;

        if (!ballHasExitedLauncherLane &&
                ball.getX() + ball.getWidth() < wallX) {
            ballHasExitedLauncherLane = true;
        }

        if (ball.getY() < 0) {
            ball.setY(0);
            ball.setYSpeed(Math.abs(ball.getYSpeed()) * wallRestitution);
        }

        if (ball.getX() < 0) {
            ball.setX(0);
            ball.setXSpeed(Math.abs(ball.getXSpeed()) * wallRestitution);
        }

        if (ballHasExitedLauncherLane &&
                ball.getX() + ball.getWidth() > wallX) {
            ball.setX(wallX - ball.getWidth());
            ball.setXSpeed(-Math.abs(ball.getXSpeed()) * wallRestitution);
        }

        if (ball.getY() > HEIGHT) {
            lives--;

            if (lives <= 0) {
                gameOver = true;
                ball.setXSpeed(0);
                ball.setYSpeed(0);
            } else {
                resetBall();
            }
        }
    }

    // Reset ball after drain
    private void resetBall() {
        ballInLauncher = true;
        ballHasExitedLauncherLane = false;
        spacePressed = false;
        wasSpacePressed = false;

        keepBallAtLauncherTop();

        launch.stopCharging();
        launch.resetCharge();
    }

    private void updateScore(int points) {
        score += points;
    }

    // Apply selected difficulty
    private void applyDifficulty(Difficulty selectedDifficulty) {
        difficulty = selectedDifficulty;
        lives = 3;
        gameOver = false;
        score = 0;

        if (difficulty == Difficulty.EASY) {
            bumperBounceSpeed = 6.0;

            leftFlipper = new Flipper(130, HEIGHT - 105, Color.WHITE, true);
            rightFlipper = new Flipper(350, HEIGHT - 105, Color.WHITE, false);
        } else if (difficulty == Difficulty.NORMAL) {
            bumperBounceSpeed = 7.5;

            leftFlipper = new Flipper(120, HEIGHT - 105, Color.WHITE, true);
            rightFlipper = new Flipper(360, HEIGHT - 105, Color.WHITE, false);
        } else if (difficulty == Difficulty.HARD) {
            bumperBounceSpeed = 9.5;

            leftFlipper = new Flipper(105, HEIGHT - 105, Color.WHITE, true);
            rightFlipper = new Flipper(375, HEIGHT - 105, Color.WHITE, false);
        }

        createGuideWalls();
        createObjects();
        resetBall();
    }

    // Create bumpers
    private void createObjects() {
        bumpers.clear();

        if (difficulty == Difficulty.EASY) {
            bumperBounceSpeed = 6.0;

            bumpers.add(new Bumper(130, 150, 50, 50, Color.YELLOW));
            bumpers.add(new Bumper(270, 150, 50, 50, Color.GREEN));
            bumpers.add(new Bumper(200, 250, 50, 50, Color.ORANGE));
            bumpers.add(new Bumper(130, 350, 45, 45, Color.PINK));
            bumpers.add(new Bumper(270, 350, 45, 45, Color.MAGENTA));
        } else if (difficulty == Difficulty.NORMAL) {
            bumperBounceSpeed = 7.5;

            bumpers.add(new Bumper(150, 160, 45, 45, Color.YELLOW));
            bumpers.add(new Bumper(250, 160, 45, 45, Color.GREEN));
            bumpers.add(new Bumper(200, 260, 45, 45, Color.ORANGE));
            bumpers.add(new Bumper(200, 360, 45, 45, Color.PINK));
        } else if (difficulty == Difficulty.HARD) {
            bumperBounceSpeed = 9.5;

            bumpers.add(new Bumper(140, 180, 40, 40, Color.YELLOW));
            bumpers.add(new Bumper(260, 180, 40, 40, Color.GREEN));
            bumpers.add(new Bumper(200, 320, 40, 40, Color.ORANGE));
        }
    }

    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();

        if (difficulty == null || gameOver) {
            if (code == KeyEvent.VK_1) {
                applyDifficulty(Difficulty.EASY);
            } else if (code == KeyEvent.VK_2) {
                applyDifficulty(Difficulty.NORMAL);
            } else if (code == KeyEvent.VK_3) {
                applyDifficulty(Difficulty.HARD);
            }

            return;
        }

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

        if (difficulty == null || gameOver) {
            return;
        }

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