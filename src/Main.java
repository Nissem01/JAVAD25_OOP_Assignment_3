import javax.swing.*;
import java.awt.*;

public class Main {

    public static void main(String[] args) {
        JFrame frame = new JFrame("Fifteen Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(new FifteenGame(4, 400, 0));
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}


