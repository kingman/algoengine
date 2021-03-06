package com.netlight.app.ui;

import java.util.Observable;
import java.util.Observer;

import javax.swing.table.AbstractTableModel;
import com.netlight.app.OrderBook;
import com.netlight.app.MarketDataSubscriptionModel;

public class MarketDataTableModel extends AbstractTableModel implements Observer {
	private MarketDataSubscriptionModel marketDataSubscriptionModel = null;
	private OrderBook orderBook = null;
	private String symbol = null;
	public MarketDataTableModel(String symbol)
	{
		this.symbol=symbol;
		this.marketDataSubscriptionModel = MarketDataSubscriptionModel.getInstance();
		this.orderBook = marketDataSubscriptionModel.getOrderbook(symbol);
	}
	
	public String getSymbol()
	{
		return symbol;
	}
	@Override
	public int getRowCount() {
		return 5;
	}

	@Override
	public int getColumnCount() {
		// TODO Auto-generated method stub
		return 4;
	}
	
	public String getColumnName(int column) {
		switch(column)
		{
		case 0:
			return "Bid #";
		case 1:
			return "Bid";
		case 2:
			return "Ask";
		case 3: 
			return "Ask #";
		}
		return null;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		if(orderBook == null)
		{
			/** Initialization hack **/
			orderBook = marketDataSubscriptionModel.getOrderbook(symbol);
			if(orderBook != null)
				orderBook.addObserver(this);
			else
				return null;
		}
		switch (columnIndex)
		{
		case 0:
			return orderBook.getBuyDepthVolume(rowIndex +1);
		case 1:
			return orderBook.getBuyDepthPrice(rowIndex +1);
		case 2:
			return orderBook.getSellDepthPrice(rowIndex +1);
		case 3:
			return orderBook.getSellDepthVolume(rowIndex +1);
			
		}
		
		//Should never get here
		return null;

		
	}

	@Override
	public void update(Observable o, Object arg) {
		this.fireTableDataChanged();
		
	}

		
}
