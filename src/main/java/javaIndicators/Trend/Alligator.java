package javaIndicators.Trend;

import java.util.List;

import Tick.Tick;
import Tick.TickLogger;
import javaIndicators.Averages.SmoothedMovingAverage;

public class Alligator {
	static public final int JAW = 0;	
	static public final int TEETH = 1;
	static public final int LIPS = 2;
	static public enum Indicator {
		CLOSE_POSITION,
		STILL,
		BUY,
		SELL,
		HOLD_POSITION
	};
	
//	private int periods;
//	private int period = 0;

	private Indicator syntetic = Indicator.STILL;
	
	SmoothedMovingAverage smma5 = null;
	SmoothedMovingAverage smma8 = null;
	SmoothedMovingAverage smma13 = null;
	
	List<Tick> tl = null;
	
	double[] smma = new double[3];
	
	public Alligator(int frequency)
	{
		smma5 = new SmoothedMovingAverage(5, 3, frequency);
		smma8 = new SmoothedMovingAverage(8, 5, frequency);
		smma13 = new SmoothedMovingAverage(13, 8, frequency);
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

		double openess = (smma[JAW] - smma[LIPS]) * 10 / tl.get(0).close;
		if (smma[JAW] == 0)
		{
			/*
			 * Alligator not formed yet. Hold on
			 */
			syntetic = Indicator.STILL;
		}
		else if (((smma[LIPS] > smma[TEETH]) && !(smma[JAW] < smma[TEETH])) || 
			((smma[JAW] > smma[TEETH]) && !(smma[LIPS] < smma[TEETH])))
		{
			if ((syntetic == Indicator.BUY) || (syntetic == Indicator.SELL)|| (syntetic == Indicator.HOLD_POSITION))
			{
				syntetic = Indicator.CLOSE_POSITION;
			}
			else
			{
				syntetic = Indicator.STILL;
			}
		}
		else if (Math.abs(openess) < .05)
		{
			if ((syntetic == Indicator.BUY) || (syntetic == Indicator.SELL) || (syntetic == Indicator.HOLD_POSITION))
			{
				syntetic = Indicator.CLOSE_POSITION;
			}
			else
			{
				syntetic = Indicator.STILL;
			}
		}
		else 
		{
			if (openess > 0)
			{
				if (syntetic == Indicator.STILL)
				{
					syntetic = Indicator.BUY;
				}
				else
				{
					syntetic = Indicator.HOLD_POSITION;
				}
			}
			else
			{
				if (syntetic == Indicator.STILL)
				{
					syntetic = Indicator.SELL;
				}
				else
				{
					syntetic = Indicator.HOLD_POSITION;
				}
			}
		}
		System.out.format("%6.4f - %s\n", openess, syntetic);
	}
	
	public double getValue(int which)
	{
		return smma[which];
	}
	
	public Indicator getSynteticValue()
	{
		return syntetic;
	}
}
