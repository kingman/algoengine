package com.netlight.app;

public class logHelper {
	public static final boolean logEnabled = true;
	public static final boolean logDebug = true;
	public static void log(String msg)
	{
		if(logEnabled)
			System.out.println(msg);
	}
	public static void logDebug(String msg)
	{
		if(logEnabled && logDebug)
		{
			System.out.println(msg);
		}
	}
	
	
}
