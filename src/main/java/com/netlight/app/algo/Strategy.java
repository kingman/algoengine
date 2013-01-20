package com.netlight.app.algo;

import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import com.netlight.app.BanzaiApplication;
import com.netlight.app.LogonEvent;
import com.netlight.app.algo.API.Side;

public class Strategy implements Observer{
	
	API api;
	
	Map<String, String> counterInstrument = new HashMap<String, String>();
	
	public Strategy(BanzaiApplication app) {
		counterInstrument.put("ERIC B XSTO", "ERIC B BURG");
		counterInstrument.put("ERIC B BURG", "ERIC B XSTO");
		this.api = new API(app);
		app.addLogonObserver(this);
	}
	
	void OnStrategyStart()
	{
		
	}
	
	void OnStrategyStop()
	{
		
	}

	public void OnPriceUpdate(String symbol, Double price, Double volume, Side side) {
		api.SendOrder(counterInstrument.get(symbol), getPrice(price, side), volume, (side == Side.BUY ? Side.SELL : Side.BUY));
	}
	
	private Double getPrice(Double current, Side side) {
		double delta = 0.45;
		delta *= (side == Side.BUY) ? 1.0 : -1.0;
		
		return current+delta;
	}
	
	public void OnTradeDone(String symbol, Double price, Double volume, Side side)
	{
		/**
		 * SendMarketOrder(other_symbol, volume, side);
		 */
	}
	
	@Override
	public void update(Observable o, Object arg) {
        LogonEvent logonEvent = (LogonEvent)arg;
        if(logonEvent.isLoggedOn())
            api.setSessionID(logonEvent.getSessionID());
        else
        	api.setSessionID(null);
	}
	
}
