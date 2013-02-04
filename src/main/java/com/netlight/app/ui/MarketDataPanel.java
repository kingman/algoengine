package com.netlight.app.ui;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
public class MarketDataPanel extends JPanel
{
	public MarketDataPanel(MarketDataTableModel marketDataTableModel)
	{
        setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx=0;
        constraints.gridy=0;
        JLabel label = new JLabel(marketDataTableModel.getSymbol());
        add(label,constraints);
        constraints.gridy++;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.weightx = 1;
        constraints.weighty = 1;
        

        JTable table = new MarketDataTable(marketDataTableModel);
        add(new JScrollPane(table), constraints);
	}
	

}