package main;

import java.awt.Color;
import javax.swing.*;

public class GameplayGui {

    JFrame frame = new JFrame("UNIT SQUARE GAME");
    JPanel rightPanel = new JPanel();
    public Controller Controller;
    private JTextArea moveHistory;
    private int edge_square_amount;
    private int horizontal_line_length;
    private int vertical_line_length;
    private int panel_length;
    private int edge_square_amount_inc;
    private int edge_square_amount_mul2;
    private int edge_square_amount_mul4;
    private int heightOfHorizontalButtons = 10;

    public GameplayGui(Controller controller) {
        this.Controller = controller;
        this.edge_square_amount = Controller.edgeAmount;
        this.horizontal_line_length = edge_square_amount * (edge_square_amount + 1);
        this.vertical_line_length = edge_square_amount * (edge_square_amount + 1);
        this.panel_length = (int) Math.pow(edge_square_amount, 2);
        this.edge_square_amount_inc = edge_square_amount + 1;
        this.edge_square_amount_mul4 = edge_square_amount * 4;
        this.edge_square_amount_mul2 = edge_square_amount * 2;
    }

    public void run_gui() {
        frame.setSize(1100, 750);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);
        frame.setLocationRelativeTo(null);
        rightPanel.setBounds(700, 50, 380, 340);
        rightPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        rightPanel.setBackground(Color.WHITE);
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));

        JLabel label = new JLabel("Player RED's Turn, Your Score: 0");
        rightPanel.add(label);

        moveHistory = new JTextArea(15, 20);
        moveHistory.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(moveHistory);
        rightPanel.add(scrollPane);

        frame.add(rightPanel);

        for (int i = 0; i < horizontal_line_length ; i++) { // horizontal buttonlar
            JButton button = new JButton();
            int x = (i % edge_square_amount) * 70 + edge_square_amount_mul2;
            int y = (i / edge_square_amount) * 70 + edge_square_amount_mul2;
            int width = 70+edge_square_amount;
            int height = edge_square_amount_mul2;

            button.setBounds(x, y, width, height);

            Line line = new Line(x, y, width, height, i);
            Controller.addLine(line, "horizontal");
            button.addActionListener(e -> {
                Controller.afterClick(line);
            });
            line.setButton(button);
            frame.add(button);
        }

        for (int i = 0; i < panel_length; i++) { // panel
            int x = (i % edge_square_amount) * (70) + edge_square_amount_mul4;//edge_square_amount_mul2;
            int y = (i / edge_square_amount) * (70) + edge_square_amount_mul4;//edge_square_amount_mul2;
            int width = 70-edge_square_amount_mul2;
            int height = 70-edge_square_amount_mul2;

            JPanel panel = new JPanel();
            panel.setBackground(Color.WHITE);
            panel.setBounds(x, y, width, height);
            panel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            Controller.addPanel(panel);
            frame.add(panel);
        }

        for (int i = 0 ; i < vertical_line_length ; i++) { // vertical buttonlar
            JButton button = new JButton();
            int x = (i % edge_square_amount_inc) * 70 + edge_square_amount_mul2;
            int y = (i / edge_square_amount_inc) * 70 + edge_square_amount_mul2;
            int width = edge_square_amount_mul2;
            int height = 70+edge_square_amount;

            button.setBounds(x, y, width, height);

            Line line2 = new Line(x, y, width, height, i);
            Controller.addLine(line2, "vertical");
            button.addActionListener(e -> {
                Controller.afterClick(line2);
            });
            line2.setButton(button);
            frame.add(button);
        }

        Controller.createSquares();
        frame.setVisible(true);
    }

    public void setButton(Line line) {
        JButton button = line.getButton();
        if (button != null) {
            button.setOpaque(true);
            button.repaint();
        }
        frame.repaint();
    }

    public void setPanel(Square square) {
        JPanel panel = square.getPanel();
        if (panel != null) {
            panel.setBackground(square.color);
            panel.setOpaque(true);
            panel.repaint();
        }
        frame.repaint();
    }

    public void setLabel(String text) {
        JLabel label = (JLabel) rightPanel.getComponent(0);
        label.setText(text);
        label.repaint();
        frame.repaint();
    }

    public void addMoveToHistory(String moveLog) {
        if (moveHistory != null) {
            moveHistory.append(moveLog + "\n");
            moveHistory.setCaretPosition(moveHistory.getDocument().getLength());
        }
    }

    public void gameEndpopup(String text, String title) {
        JFrame popup = new JFrame(title);
        popup.setSize(300, 300);
        popup.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        popup.setLayout(null);
        JTextPane textPane = new JTextPane();
        textPane.setText(text);
        textPane.setBounds(10, 10, 280, 80);
        popup.add(textPane);
        popup.setVisible(true);
    }
}

