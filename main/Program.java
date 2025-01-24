package main;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.FlowLayout;

public class Program {
    public static void main(String[] args) {
        
        JFrame setupFrame = new JFrame("Select your square amount for the game");
        
        setupFrame.setSize(400, 200);
        setupFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));

        
        setupFrame.add(panel);

        setupFrame.setLayout(new FlowLayout());
        setupFrame.setLocationRelativeTo(null);

        JLabel label = new JLabel("Select Square Amount:");
        panel.add(label);
        panel.add(Box.createRigidArea(new Dimension(10,0)));

        JComboBox<Integer> dropdown = new JComboBox<>();
        for (int i = 2; i <= 11; i++) {
            dropdown.addItem(i);
        }
        panel.add(dropdown);
        panel.add(Box.createRigidArea(new Dimension(20,0)));

        JButton startButton = new JButton("Start Game");
        panel.add(startButton);
        setupFrame.setVisible(true);

        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int squareAmount = (int) dropdown.getSelectedItem();
                setupFrame.dispose();

                Player p1 = new Player(Color.RED, "Player RED");
                Player p2 = new Player(Color.CYAN, "Player BLUE");
                Controller controller = new Controller(p1, p2,squareAmount);
                controller.run();
            }
        });

    }
}
