package com.netlight.app.algo;

import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import com.netlight.app.BanzaiApplication;
import com.netlight.app.LogonEvent;
import com.netlight.app.algo.API.Side;

public class Strategy implements Observer{
	
	/**
	 * Bid price - what you are offering to buy for
	 * Ask price - what is your offered sell price
	 */
	
	
	API api;
	
	Map<String, String> counterInstrument = new HashMap<String, String>();
	
	Side baiteSide = API.Side.SELL;
	String baiteMarket = "BURG";
	private boolean strategyStarted = false;
	
	public Strategy(BanzaiApplication app) {
		counterInstrument.put("ERIC B XSTO", "ERIC B BURG");
		counterInstrument.put("ERIC B BURG", "ERIC B XSTO");
		this.api = new API(app);
		app.addLogonObserver(this);
	}
	
	/**
	 * Activated when user clicks Start button in GUI
	 */
	public void OnStrategyStart()
	{
		strategyStarted = true;
		System.out.println("Strategy Started");
	}
	/**
	 * Activated when user clicks Stop button in GUI
	 */
	public void OnStrategyStop()
	{
		api.cancelAllOrder(counterInstrument.keySet());
		strategyStarted = false;
		System.out.println("Strategy Stopped");
	}

	/**
	 * Invoked when a message from Stock Exchange is received with new prices
	 */
	public void OnPriceUpdate(String symbol, Double price, Double volume, Side side) {
		if(strategyStarted && symbol.contains(baiteMarket) && side == baiteSide) {
			api.SendOrder(counterInstrument.get(symbol), getPrice(price, side), volume, side);
		}
	}
	
	private Double getPrice(Double current, Side side) {
		double delta = 0.45;
		delta *= (side == Side.BUY) ? -1.0 : 1.0;
		
		return current+delta;
	}
	
	/** Actions to perform when requested trade operation is concluded on
	 * Stock exchange
	 * @param symbol
	 * @param price
	 * @param volume
	 * @param side
	 */
	public void OnTradeDone(String symbol, Double price, Double volume, Side side)
	{
		if(!symbol.contains(baiteMarket)) {
			api.SendMarketOrder(counterInstrument.get(symbol), volume, side == Side.BUY ? Side.SELL : Side.BUY);
			api.modifyOrderVolum(symbol, side);
		}
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
