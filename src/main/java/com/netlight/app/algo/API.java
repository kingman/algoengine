package com.netlight.app.algo;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

import quickfix.SessionID;

import com.netlight.app.BanzaiApplication;
import com.netlight.app.Order;
import com.netlight.app.OrderSide;
import com.netlight.app.OrderTIF;
import com.netlight.app.OrderType;

public class API{
	
	BanzaiApplication app;
	private SessionID sessionID;
	private Map<Side,Map<String, Order>> orderKeeper = new HashMap<Side, Map<String, Order>>();
	
	public API(BanzaiApplication app) {
		this.app = app;
		orderKeeper.put(Side.BUY, new HashMap<String, Order>());
		orderKeeper.put(Side.SELL, new HashMap<String, Order>());
	}
	
	Double GetBestBid(String symbol) {
		return app.getOrderbook(symbol).getBestBid();
	}
	
	Double GetBestAsk(String symbol) {
		return app.getOrderbook(symbol).getBestAsk();
	}
	
	Double GetBestAskVolume(String symbol) {
		return app.getOrderbook(symbol).getBestAskVolume();
	}
	Double GetBestBidVolume(String symbol) {
		return app.getOrderbook(symbol).getBestBidVolume();
	}
	
	public enum Side
	{
		BUY("Buy"),
		SELL("Sell");
		
		private final String str;
		
		Side(String str) {
			this.str = str;
		}
		
		public String toString() {
			return str;
		}
	};

	
	public void SendOrder(String symbol, Double price, Double volume, Side side ) {
		Order order = getOrder(symbol, volume, side);
        order.setType(OrderType.LIMIT);
        order.setLimit(price);
        order.setSessionID(sessionID);
        orderKeeper.get(side).put(symbol, order);
		app.send(order);
		System.out.println(MessageFormat.format("{0} {1} {2} for {3}", side, volume, symbol, price));
	}
	public void SendMarketOrder(String symbol, Double volume, Side side ) {
		Order order = getOrder(symbol, volume, side);
		order.setType(OrderType.MARKET);
		order.setSessionID(sessionID);
		orderKeeper.get(side).put(symbol, order);
		app.send(order);		
	}
	
	private Order getOrder(String symbol, Double volume, Side side) {
		Order order = new Order();
        order.setSide(OrderSide.parse(side.toString()));
        order.setTIF(OrderTIF.DAY);
        order.setSymbol(symbol);
        order.setQuantity(volume.intValue());
        order.setOpen(order.getQuantity());
        return order;
	}
	public void ModifyOrder(String symbol, Double price, Double volume, Side side) {
		Order newOrder = getOrder(symbol, volume, side);
		newOrder.setLimit(price);
		newOrder.setSessionID(sessionID);
		Order oldOrder = orderKeeper.get(side).get(symbol);
		orderKeeper.get(side).put(symbol, newOrder);
		app.replace(oldOrder, newOrder);
	}
	
	public void setSessionID(SessionID sessionID) {
		this.sessionID = sessionID;
	}
}