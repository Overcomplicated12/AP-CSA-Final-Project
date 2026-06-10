import javax.swing.*;

public class Main 
{
    public static void main(String[] args) 
    {
        JFrame frame = new JFrame("Pin-Ball");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // TODO: Create the panel here
        PinBallPanel panel = new PinBallPanel();
        frame.add(panel);
        frame.setSize(600, 650);
        frame.setVisible(true);

        frame.requestFocusInWindow();
    }
}