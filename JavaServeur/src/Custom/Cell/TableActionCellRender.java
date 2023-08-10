/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Custom.Cell;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author Salva
 */
public class TableActionCellRender extends DefaultTableCellRenderer{

    @Override
    public Component getTableCellRendererComponent(JTable jtable, Object o, boolean isSelected, boolean bln1, int row, int Column) {
        Component comp = super.getTableCellRendererComponent(jtable, o, isSelected, bln1, row, Column); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/OverriddenMethodBody
        
        PanelDelete action = new PanelDelete();
        
        if(isSelected == false && row%2 ==0)
        {
            action.setBackground(Color.WHITE);
        }
        else
        {
            action.setBackground(comp.getBackground());
        }
        
        return action;
    }
    
}
