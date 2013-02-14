package com.netlight.app.algo;

import java.util.Observable;
import java.util.Observer;

import com.netlight.app.BanzaiApplication;
import com.netlight.app.LogonEvent;
import com.netlight.app.logHelper;
import com.netlight.app.algo.API.Side;

public class Strategy implements Observer{
	
	/**
	 * Bid price - what you are offering to buy for
	 * Ask price - what is your offered sell price
	 */
	
	
	API api;
	private boolean strategyStarted = false;
	
	/**
	 * Strategy initialization
	 * Subscribe to the logon event from the market
	 * Add more initialization action here if needed
	 * @param app
	 */
	public Strategy(BanzaiApplication app) {
		this.api = new API(app);
		app.addLogonObserver(this);
	}
	
	/**
	 * Activated when user clicks Start button in GUI
	 */
	public void OnStrategyStart()
	{
		System.out.println("Strategy Started");
	}
	/**
	 * Activated when user clicks Stop button in GUI
	 */
	public void OnStrategyStop()
	{
		System.out.println("Strategy Stopped");
	}

	/**
	 * Invoked when a message from Stock Exchange is received with new prices
	 * Note that price can be null, indicating no price available
	 */
	public void OnPriceUpdate(String symbol, Double price, Double volume, Side side) {
		logHelper.logDebug("OnPriceUpdate called()");		
		
		if(strategyStarted) {

		}
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

	}
	
	/**
	 * Logon event handling, DO NO TOUCH!!!
	 */
	@Override
	public void update(Observable o, Object arg) {
        LogonEvent logonEvent = (LogonEvent)arg;
        if(logonEvent.isLoggedOn())
            api.setSessionID(logonEvent.getSessionID());
        else
        	api.setSessionID(null);
	}
	
}
