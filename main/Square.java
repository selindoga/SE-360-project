package main;

import java.awt.Color;
import javax.swing.*;

public class Square {

    public boolean is_active;
    public Color color;
    public Line line1;
    public Line line2;
    public Line line3;
    public Line line4;
    public JPanel panel;


    public Square(Line line1, Line line2, Line line3, Line line4, JPanel panel) {
        this.is_active = false;
        this.color = Color.BLUE;
        this.line1 = line1;
        this.line2 = line2;
        this.line3 = line3;
        this.line4 = line4;
        this.panel = panel;
    }

    public void activate(Color color) {
        if(this.is_active) {
            return;
        }
        this.is_active = true;
        this.color = color;
        if (panel != null) {
            panel.setBackground(color);
            panel.setOpaque(true);
            panel.repaint();
        }
    }


    public JPanel getPanel() {
        return this.panel;
    }

    public boolean isAllLinesAreActive() {
        return line1.isActive() && line2.isActive() && line3.isActive() && line4.isActive();
    }

}
