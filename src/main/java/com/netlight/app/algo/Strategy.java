package com.netlight.app.algo;

import com.netlight.app.algo.API.Side;

public class Strategy {
	
	void OnStrategyStart()
	{
		
	}
	
	void OnStrategyStop()
	{
		
	}

	void OnPriceUpdate(String symbol, Double price, Double volume, Side side)
	{
		/**
		 * if(symbol = hedge_leg)
		 * 	ModifyOrder(other_symbol, price, volume, side);
		 */
	}
	
	void OnTradeDone(String symbol, Double price, Double volume, Side side)
	{
		/**
		 * SendMarketOrder(other_symbol, volume, side);
		 */
	}
	
}
