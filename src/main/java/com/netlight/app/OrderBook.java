package com.netlight.app;

public class OrderBook {

	String mdReqID;
	String symbol;
	Double bestBid;
	Double bestAsk;
	Double bestBidVolume;
	Double bestAskVolume;
	
	public OrderBook(String mdReqID, String symbol)
	{
		this.mdReqID = mdReqID;
		this.symbol = symbol;
	}
	public String getSymbol(){
		return symbol;
	}
	public String getMdReqID() {
		return mdReqID;
	}
	public void setMdReqID(String mdReqID) {
		this.mdReqID = mdReqID;
	}
	public Double getBestBid() {
		return bestBid;
	}
	public void setBestBid(Double bestBid) {
		this.bestBid = bestBid;
	}
	public Double getBestAsk() {
		return bestAsk;
	}
	public void setBestAsk(Double bestAsk) {
		this.bestAsk = bestAsk;
	}
	public Double getBestBidVolume() {
		return bestBidVolume;
	}
	public void setBestBidVolume(Double bestBidVolume) {
		this.bestBidVolume = bestBidVolume;
	}
	public Double getBestAskVolume() {
		return bestAskVolume;
	}
	public void setBestAskVolume(Double bestAskVolume) {
		this.bestAskVolume = bestAskVolume;
	}

	public void print()
	{
		System.out.println("OrderBook for " + this.symbol + " updated. BBO:" + this.getBestBidVolume() + "@" + this.getBestBid() + " / " + this.getBestAskVolume() + " @" + this.getBestAsk());
	}
	
	
}
