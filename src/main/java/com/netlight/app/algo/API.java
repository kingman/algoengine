package com.netlight.app.algo;

import java.text.MessageFormat;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import quickfix.SessionID;

import com.netlight.app.BanzaiApplication;
import com.netlight.app.Order;
import com.netlight.app.OrderBook;
import com.netlight.app.OrderSide;
import com.netlight.app.OrderTIF;
import com.netlight.app.OrderType;
import com.netlight.app.logHelper;

public class API{
	
	BanzaiApplication app;
	private SessionID sessionID;
	private Map<Side,Map<String, String>> orderKeeper = new HashMap<Side, Map<String, String>>();
	
	private boolean orderAdded = false;
	
	public API(BanzaiApplication app) {
		this.app = app;
		orderKeeper.put(Side.BUY, new HashMap<String, String>());
		orderKeeper.put(Side.SELL, new HashMap<String, String>());
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
		String orderId = orderKeeper.get(side).get(symbol);
		Order oldOrder = app.getOrder(orderId);
		if(oldOrder == null)
		{
			orderAdded=false;
			logHelper.logDebug("Marked order for " + symbol + "/" + side + " as inactive");
		}
		else
		{
			if(oldOrder.getOpen() == 0 || oldOrder.getCanceled() == true)
			{
				orderKeeper.get(side).remove(symbol);
				orderAdded =  false;
				logHelper.logDebug("Marked order for " + symbol + "/" + side + " as inactive");
			}
		}
		
		if(!orderAdded) {
			Order order = getOrder(symbol, volume, side);
	        order.setType(OrderType.LIMIT);
	        order.setLimit(price);
	        order.setSessionID(sessionID);
	        orderKeeper.get(side).put(symbol, order.getID());
			app.send(order);
			orderAdded = true;
			logHelper.log("Sending new order:" + MessageFormat.format("{0} {1} {2} for {3}", side, volume, symbol, price));
		}
		else {
			ModifyOrder(symbol, price, volume, side);
		}
	}
	
	public void SendMarketOrder(String symbol, Double volume, Side side ) {
		Order order = getOrder(symbol, volume, side);
		order.setType(OrderType.MARKET);
		order.setSessionID(sessionID);
		order.setTIF(OrderTIF.IOC);
		orderKeeper.get(side).put(symbol, order.getID());
		logHelper.logDebug("Sending market order: " + side + " " + volume + "@" + symbol);
		app.send(order);		
	}
	
	private Order getOrder(String symbol, Double volume, Side side) {
		Order order = new Order();
        order.setSide(OrderSide.parse(side.toString()));
        order.setTIF(OrderTIF.DAY);
        order.setSymbol(symbol);
        order.setQuantity(volume.intValue()+order.getExecuted());
        order.setOpen(volume.intValue()+order.getExecuted());
        return order;
	}
	public void ModifyOrder(String symbol, Double price, Double volume, Side side) {
		String orderId = orderKeeper.get(side).get(symbol);
		Order oldOrder = app.getOrder(orderId);
		Order newOrder = (Order) oldOrder.clone(); //getOrder(symbol, volume, side);
		newOrder.setLimit(price);
		newOrder.setQuantity(volume.intValue() + oldOrder.getExecuted());
        newOrder.setRejected(false);
        newOrder.setCanceled(false);
        newOrder.setOpen(volume.intValue());
        newOrder.setExecuted(oldOrder.getExecuted());
		newOrder.setSessionID(sessionID);
		newOrder.setType(OrderType.LIMIT);
		logHelper.logDebug("Modifying order:" + MessageFormat.format("{0} {1} {2} for {3}", side, volume, symbol, price));
		app.replace(oldOrder, newOrder);
	}
	
	public void setSessionID(SessionID sessionID) {
		this.sessionID = sessionID;
	}
	
	public OrderBook getOrderbook(String symbol) {
		return app.getOrderbook(symbol);
	}
	
	public void cancelOrder(String symbol, Side side) {
		String orderId = orderKeeper.get(side).remove(symbol);
		Order order = app.getOrder(orderId);
		if(order != null) {
			app.cancel(order);
		}
		logHelper.logDebug("Sent order cancel request for " + symbol + " " + side);
	}
	
	public void cancelAllOrder(Collection<String> symbols) {
		logHelper.logDebug("Canceling all orders");
		for(String symbol : symbols) {
			cancelOrder(symbol, Side.BUY);
			cancelOrder(symbol, Side.SELL);
			
		}
		orderAdded = false;
	}
}
