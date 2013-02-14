package com.netlight.app;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Observable;
import java.util.Observer;

import javax.swing.table.AbstractTableModel;

public class PositionTableModel extends AbstractTableModel implements Observer {

	HashMap<String, Position> symbolToPosition;
	public HashMap<Integer, Position> rowToPosition;
	MarketDataSubscriptionModel marketDataSubscriptionModel;
	HashMap<String, OrderBook> symbolToOrderBook;
	Position totalPosition;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public PositionTableModel()
	{
		symbolToPosition = new HashMap<String, Position>();
		rowToPosition = new HashMap<Integer, Position>();
		symbolToOrderBook = new HashMap<String, OrderBook>();
		marketDataSubscriptionModel = MarketDataSubscriptionModel.getInstance();
		totalPosition = new Position("Total");
		rowToPosition.put(0, totalPosition);
	}
	
	public void addExecution(Execution e)
	{
		int mult = 1;
		Position p = symbolToPosition.get(e.getSymbol());
		if(p==null)
		{
			p = new Position(e.getSymbol());
			rowToPosition.put(rowToPosition.size(), p);
			symbolToPosition.put(e.getSymbol(), p);
		}
		if(e.getSide() == OrderSide.SELL)
			mult = -1;
		p.position += e.getQuantity() * mult;
		p.invested += (e.getQuantity() * e.getPrice()) * mult;
		
		OrderBook orderBook = symbolToOrderBook.get(e.getSymbol());
		
		if(orderBook == null)
		{
			orderBook = marketDataSubscriptionModel.getOrderbook(e.getSymbol());
			if(orderBook != null)
				orderBook.addObserver(this);
		}
		
		recalculatePosition(p);
		this.fireTableDataChanged();
	}
	@Override
	public int getRowCount() {
		// TODO Auto-generated method stub
		return rowToPosition.size();
	}

	@Override
	public int getColumnCount() {
		// TODO Auto-generated method stub
		return 5;
	}
	public String getColumnName(int column) {
		switch(column)
		{
		case 0:
			return "Symbol";
		case 1:
			return "Net #";
		case 2:
			return "Market value";
		case 3: 
			return "Invested";
		case 4:
			return "Result";
		}
		return null;
	}


	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		// TODO Auto-generated method stub
		Position p = rowToPosition.get(rowToPosition.size() -1 - rowIndex);
		switch(columnIndex)
		{
		case 0:
			return p.symbol;
		case 1:
			return p.position;
		case 2:
			return p.marketValue;
		case 3:
			return p.invested; 
		case 4:
			return p.result;
		}
		
		return null;
	}

	@Override
	public void update(Observable o, Object arg) {
		// Callback for market data update
		logHelper.logDebug("PositionTable: callback for market data update");
		Iterator<Entry<String, Position>> it = symbolToPosition.entrySet().iterator();
		while(it.hasNext())
		{
			Entry<String,Position> pairs = it.next();
			String symbol = pairs.getKey();
			Position position = pairs.getValue();
			OrderBook orderBook = symbolToOrderBook.get(symbol);
			if(orderBook == null)
			{
				orderBook = marketDataSubscriptionModel.getOrderbook(symbol);
				if(orderBook != null)
					orderBook.addObserver(this);
				else
					return;
			}
			Double bid = orderBook.getBestBid();
			Double ask = orderBook.getBestAsk();
			//position.price = (bid+ask)/2;
			
			if(bid == null)
				position.price = (ask!=null) ? ask : 0;
			else
				position.price = (ask!=null) ? (ask+bid)/2 : bid;
			recalculatePosition(position);
			
			
		}
		this.fireTableDataChanged();
	}
	
	private void recalculatePosition(Position position)
	{
		position.marketValue = (int) (position.position * position.price);
		position.result =  (int) (position.position * position.price) - position.invested;
		
		/**Update total position**/
		Iterator<Entry<Integer,Position>> it = rowToPosition.entrySet().iterator();
		totalPosition.clear();
		while(it.hasNext())
		{
			Entry<Integer,Position> pair = it.next();
			Position p = pair.getValue();
			totalPosition.result+=p.result;
			totalPosition.marketValue+=p.marketValue;
			totalPosition.invested+=p.invested;
		}
		
	}

}

class Position
{
	public String symbol;
	public int position;
	public int invested;
	public double price;
	public int result;
	public int marketValue;
	
	public Position(String symbol)
	{
		this.symbol = symbol;
		clear();
	}
	
	public void clear()
	{
		position = 0;
		invested = 0;
		price = 0;
		result = 0;
		marketValue=0;
	}
	
}
