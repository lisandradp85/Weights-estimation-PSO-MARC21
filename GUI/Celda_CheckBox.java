/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import java.awt.Component;
import javax.swing.DefaultCellEditor;
import javax.swing.JCheckBox;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

/**
 *
 * @author Elvis
 */
public class Celda_CheckBox extends DefaultCellEditor implements TableCellRenderer {
//
    private final JCheckBox component = new JCheckBox();
//    private boolean value = true; // valor de la celda

    /**
     * Constructor de clase
     */
    public Celda_CheckBox() {
        super(new JCheckBox());
    }

    /**
     * retorna componente
     * @return 
     */
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        component.setSelected((boolean) value);
        return component;
    }

}
