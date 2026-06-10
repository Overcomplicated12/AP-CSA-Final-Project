import javax.swing.*;

public class Main
{
    public static void main(String[] args)
    {
        JFrame frame = new JFrame("Pin-Ball");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        PinBallPanel panel = new PinBallPanel();
        frame.add(panel);

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        panel.requestFocusInWindow();
    }
}
