package gui;

import javax.swing.*;

/**
 * Created by OAIM on 2016/9/4.
 */
public class Ba {
    private JButton button1;
    private JRadioButton radioButton1;
    private JPanel panel1;
    private JButton button2;

    private void createUIComponents() {
        // TODO: place custom component creation code here

    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Ba");
        frame.setContentPane(new Ba().panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
