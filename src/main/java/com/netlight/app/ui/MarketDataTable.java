package com.netlight.app.ui;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;

import com.netlight.app.ExecutionTableModel;

public class MarketDataTable extends JTable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public MarketDataTable(MarketDataTableModel marketDataTableModel) {
        super(marketDataTableModel);
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

        return super.prepareRenderer(renderer, row, column);
	
    }
}
