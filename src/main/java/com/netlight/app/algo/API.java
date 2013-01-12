package com.netlight.app.algo;

public class API {
	
	Double GetBestBid(String symbol)
	{
		return null;
	}
	Double GetBestAsk(String symbol)
	{
		return null;
	}
	
	Double GetBestAskVolume(String symbol)
	{
		return null;
	}
	Double GetBestBidVolume(String symbol)
	{
		return null;
	}
	
	public enum Side
	{
		BUY,
		SELL
	};

	
	public void SendOrder(String symbol, Double price, Double volume, Side side )
	{
		/**Does nothing if already has order for that symbol and side**/
	}
	public void SendMarketOrder(String symbol, Double volume, Side side )
	{
		
	}
	public void ModifyOrder(String symbol, Double price, Double volume, Side side)
	{
		
	}
}
