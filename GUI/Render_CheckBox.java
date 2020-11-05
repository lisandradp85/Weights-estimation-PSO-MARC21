/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;



/**
 *
 * @author Elvis
 */
public class Render_CheckBox extends JCheckBox implements TableCellRenderer {

    /** Constructor de clase */
    public Render_CheckBox() {
        setOpaque(true);
    }

    @Override
  public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
      this.setSelected((Boolean) value);
      return this;
  }

}

