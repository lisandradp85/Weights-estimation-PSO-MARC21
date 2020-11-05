/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.text.DecimalFormat;
import javax.swing.JComponent;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

/**
 *
 * @author Joker
 */
public class GProgressBar extends JComponent {

    private double value;
    private double minValue;
    private double maxValue;

    public GProgressBar(double minValue, double maxValue) {
        this();
        this.minValue = minValue;
        this.maxValue = maxValue;
    }

    public GProgressBar() {
        value = 0;
        minValue = 0;
        maxValue = 100;
        setMinimumSize(new Dimension(300, 50));
        setPreferredSize(getMinimumSize());
        setSize(getMinimumSize());
        setBorder(new TitledBorder(new LineBorder(Color.yellow), "Progreso"));
    }

    public double getValue() {
        return value;
    }

    public double getMinValue() {
        return minValue;
    }

    public double getMaxValue() {
        return maxValue;
    }

    public void setValue(double value) {
        if (value < minValue || value > maxValue) {
            return;
        }
        this.value = value;
        repaint();
    }

    public void setMinValue(double minValue) {
        if (minValue > maxValue || minValue > value) {
            return;
        }
        this.minValue = minValue;
        repaint();
    }

    public void setMaxValue(double maxValue) {
        if (maxValue < minValue || maxValue < value) {
            return;
        }
        this.maxValue = maxValue;
        repaint();

    }

    @Override
    public void paint(Graphics g) {
        //To change body of generated methods, choose Tools | Templates.
        int x, y;
        x = 5;
        y = 18;
        int width = this.getWidth() - 10;
        int height = this.getHeight() - 23;
        double por = ((value - minValue) / (maxValue - minValue)) * 100;

        int offsetX = x;
        int offsetY = y;
        g.setColor(Color.GRAY);

        g.fillRect(x, y, width, height);

        if (por < 20) {
            g.setColor(Color.red);
        } else if (por < 40) {
            g.setColor(Color.orange);
        } else if (por < 60) {
            g.setColor(Color.yellow);
        } else if (por < 80) {
            g.setColor(Color.decode("#FFFF99"));
        } else {
            g.setColor(Color.green);
        }
        String t=por+"";
      int p= t.lastIndexOf(".")+3;
     
        g.fillRect(offsetX, offsetY, (int) ((width) * (por / 100)), height);
        g.setColor(Color.black);
        g.setFont(new Font("lolo", Font.PLAIN, 18));
        g.drawString((t.substring(0,p==2?t.length():Math.min(p, t.length()))) + "%", (getWidth()) / 2, (getHeight() + y) / 2);
        super.paint(g);
    }

}
