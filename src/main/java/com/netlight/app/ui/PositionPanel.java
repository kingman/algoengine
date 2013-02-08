package com.netlight.app.ui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import com.netlight.app.PositionTableModel;

public class PositionPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1970471753752765793L;

	public PositionPanel(PositionTableModel positionTableModel)
	{
        setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx=0;
        constraints.gridy=0;
        constraints.gridy++;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.weightx = 1;
        constraints.weighty = 1;
        

        JTable table = new PositionTable(positionTableModel);
        add(new JScrollPane(table), constraints);
	}
}
