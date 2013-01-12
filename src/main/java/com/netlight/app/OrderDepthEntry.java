package com.netlight.app;

public class OrderDepthEntry {
	double price;
	double volume;
	
	public double getPrice() {
		return price;
	}
	public OrderDepthEntry setPrice(double price) {
		this.price = price;
		return this;
	}
	public double getVolume() {
		return volume;
	}
	public OrderDepthEntry setVolume(double volume) {
		this.volume = volume;
		return this;
	}
	
	

}
