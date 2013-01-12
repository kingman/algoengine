package com.netlight.app;

import java.util.Observable;
import java.util.Observer;
import java.util.UUID;

public class MarketDataSubscriptionModel implements Observer {

	BanzaiApplication app;
	TwoWayMap OrderBooks = new TwoWayMap();
	
	public MarketDataSubscriptionModel()
	{
		
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
		// TODO Auto-generated method stub
        LogonEvent logonEvent = (LogonEvent) arg;
        if(logonEvent.isLoggedOn()) {
        	String reqID =  UUID.randomUUID().toString();
        	OrderBooks.put(reqID, new OrderBook(reqID, "A"));
        	app.subscribe("A", logonEvent.getSessionID(), reqID);
        	//String reqID =  UUID.randomUUID().toString();
        	//OrderBooks.put(reqID, new OrderBook(reqID));
        	//app.subscribe("B", logonEvent.getSessionID(), reqID);
        }

	}
	

}

