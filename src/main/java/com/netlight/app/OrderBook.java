package com.netlight.app;

import java.awt.List;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;

public class OrderBook extends Observable {

	String mdReqID;
	String symbol;
	Map<Integer, OrderDepthEntry> buySide;
	Map<Integer, OrderDepthEntry> sellSide;
	
	
	public OrderBook(String mdReqID, String symbol)
	{
		this.mdReqID = mdReqID;
		this.symbol = symbol;
		buySide = new HashMap<Integer, OrderDepthEntry>();
		sellSide = new HashMap<Integer, OrderDepthEntry>();
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
		return buySide.get(1) != null ? buySide.get(1).getPrice() : null;
	}

	public Double getBestAsk() {
		return sellSide.get(1) != null ? sellSide.get(1).getPrice() : null;
	}

	public Double getBestBidVolume() {
		return buySide.get(1) != null ? buySide.get(1).getVolume() : null;
	}
	public Double getBestAskVolume() {
		return sellSide.get(1) != null ? sellSide.get(1).getVolume() : null;
	}
	
	public void addBuyOrderDepth(Integer level, Double price, Double volume) {
		buySide.put(level, new OrderDepthEntry().setPrice(price).setVolume(volume));
	}

	public void addSellOrderDepth(Integer level, Double price, Double volume) {
		sellSide.put(level, new OrderDepthEntry().setPrice(price).setVolume(volume));
	}
	public Double getBuyDepthPrice(Integer level)
	{
		return buySide.get(level) != null ? buySide.get(level).getPrice() : null;
	}
	public Double getBuyDepthVolume(Integer level)
	{
		return buySide.get(level) != null ? buySide.get(level).getVolume() : null;
	}
	public Double getSellDepthPrice(Integer level)
	{
		return sellSide.get(level) != null ? sellSide.get(level).getPrice() : null;
	}
	public Double getSellDepthVolume(Integer level)
	{
		return sellSide.get(level) != null ? sellSide.get(level).getVolume() : null;
	}
	
	public void cleanMaxBuyLevel(int level)
	{
		for (int i = level+1; i < 6; i++)
			buySide.remove(i);
	}
	public void cleanMaxSellLevel(int level)
	{
		for (int i = level+1; i < 6; i++)
			sellSide.remove(i);
	}
	public void updateComplete()
	{
		setChanged();
		notifyObservers();
	}
	public String toString()
	{
		return "OrderBook for " + this.symbol + " updated. BBO:" + this.getBestBidVolume() + "@" + this.getBestBid() + " / " + this.getBestAskVolume() + " @" + this.getBestAsk();
	}
	
	
}
