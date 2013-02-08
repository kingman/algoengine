package com.netlight.app.ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;

import com.netlight.app.PositionTableModel;


public class PositionTable extends JTable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private PositionTableModel positionTableModel;
	public PositionTable(PositionTableModel positionTableModel) {
		super(positionTableModel);
		this.positionTableModel = positionTableModel;
		
		setRowSelectionAllowed(false);
		setColumnSelectionAllowed(false);
		setCellSelectionEnabled(false); 
    }

    public Component prepareRenderer(TableCellRenderer renderer,
                                     int row, int column) {
//        Execution execution = (Execution)((ExecutionTableModel)dataModel)
//                              .getExecution(row);

        DefaultTableCellRenderer r = (DefaultTableCellRenderer)renderer;
        r.setForeground(Color.black);

	        if((row%2) == 0)
	            r.setBackground(Color.white);
	        else
	            r.setBackground(Color.lightGray);
	        if(row == (positionTableModel.getRowCount() -1))
	        {
	        	r.setBackground(Color.orange);
	        	r.setFont(new Font("Arial", Font.BOLD,20));
	        	
	        }
        return super.prepareRenderer(renderer, row, column);
	
    }
}
