package com.netlight.app;

import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.UUID;
/**
 * Singleton class for handling market data subscriptions and orderbooks
 * @author bjorn
 *
 */
public class MarketDataSubscriptionModel implements Observer {
	private static MarketDataSubscriptionModel instance = null;
	
	BanzaiApplication app;
	TwoWayMap OrderBooks = new TwoWayMap();
	Map<String, OrderBook> keyToOrderbook = new HashMap<String, OrderBook>();
	
	public OrderBook getOrderbook(String symbol) {
		return keyToOrderbook.get(symbol);		
	}
	
	private MarketDataSubscriptionModel()
	{
		
	}
	public static MarketDataSubscriptionModel getInstance()
	{
		if (instance==null)
		{
			instance = new MarketDataSubscriptionModel();
		}
		return instance;
	}
	
	public void setApplication(BanzaiApplication app)
	{
		this.app = app;
		app.addLogonObserver(this);
	}
	public OrderBook getOrderBook(String mdReqID)
	{
		return (OrderBook) OrderBooks.getFirst(mdReqID);
	}
	
	@Override
	public void update(Observable arg0, Object arg) {
        LogonEvent logonEvent = (LogonEvent) arg;
        if(logonEvent.isLoggedOn()) {
        	doSubscribtionForStock("ERIC B BURG", logonEvent);
        	doSubscribtionForStock("ERIC B XSTO", logonEvent);
        }
	}
	
	private void doSubscribtionForStock(String key, LogonEvent logonEvent) {
		String reqID =  UUID.randomUUID().toString();
		OrderBook burgOrderbook = new OrderBook(reqID, key);
		OrderBooks.put(reqID, burgOrderbook);
		keyToOrderbook.put(key,burgOrderbook);
    	app.subscribe(key, logonEvent.getSessionID(), reqID);
	}
	

}

