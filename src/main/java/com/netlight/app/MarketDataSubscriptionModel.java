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
        	OrderBooks.put(reqID, new OrderBook(reqID, "ERIC B BURG"));
        	app.subscribe("ERIC B BURG", logonEvent.getSessionID(), reqID);
        	
        	 reqID =  UUID.randomUUID().toString();
        	OrderBooks.put(reqID, new OrderBook(reqID, "ERIC B XSTO"));
        	app.subscribe("ERIC B XSTO", logonEvent.getSessionID(), reqID);

        }

	}
	

}

