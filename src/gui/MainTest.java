package gui;

import gui.GridBagDemo;

import javax.swing.*;

/**
 * Created by OAIM on 2016/9/5.
 */
public class MainTest {

    public static void main(String[] args) {
       JFrame jf = new JFrame();
        jf.setVisible(true);
        jf.setSize(250, 220);
        jf.setVisible(true);
        jf.setLocation(400, 200);
jf.setContentPane(new GridBagDemo());
    }
}
