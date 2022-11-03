package javaIndicators.Trend;

import java.util.List;

import Tick.Tick;
import Tick.TickLogger;
import javaIndicators.Averages.SmoothedMovingAverage;

public class Alligator {
	static public final int JAW = 0;	
	static public final int TEETH = 1;
	static public final int LIPS = 2;
	
//	private int periods;
//	private int period = 0;

	SmoothedMovingAverage smma5 = null;
	SmoothedMovingAverage smma8 = null;
	SmoothedMovingAverage smma13 = null;
	
	List<Tick> tl = null;
	
	double[] smma = new double[3];
	
	public Alligator(int frequency)
	{
		smma5 = new SmoothedMovingAverage(5, frequency);
		smma8 = new SmoothedMovingAverage(8, frequency);
		smma13 = new SmoothedMovingAverage(13, frequency);
		tl = TickLogger.getInstance().getTickList(frequency);
	}
	
	public void addItem() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException
	{
		smma5.addItem();
		smma8.addItem();
		smma13.addItem();
		smma[JAW] = smma13.getValue();
		smma[TEETH] = smma8.getValue();
		smma[LIPS] = smma5.getValue();
	}
	
	public double getValue(int which)
	{
		return smma[which];
	}
}
