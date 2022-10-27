package javaIndicators.Averages;

import Tick.Tick;
import Tick.TickLogger;

public class AverageTrueRange {
	double averageTrueRange;
	int periods;
	int currentPeriod;
	private static TickLogger tl = TickLogger.getInstance();
	
	public AverageTrueRange(int periods)
	{
		this.periods = periods; 
		currentPeriod = 0;
	}
	
	public double getAverageTrueRangeFromPeriod(Tick[] periodTicks, boolean storeData)
	{
		double averageTrueRange = 0;
		for(int i = 0; i < periodTicks.length; i++)
		{
			averageTrueRange += periodTicks[i].trueRange;
		}
		averageTrueRange /= periodTicks.length;
		if (storeData)
		{
			this.averageTrueRange = averageTrueRange;
			currentPeriod = periods - 1;
		}
		return averageTrueRange;
	}
	
	static public double getAverageTrueRangeNDays(int days)
	{
		double averageTrueRange = 0;
		for(int i = 0; i < days; i++)
		{
			averageTrueRange += tl.getClosureOfDay(i).trueRange;
		}
		averageTrueRange /= days;
		return averageTrueRange;
	}
	
	public void addTick(Tick tick)
	{
		if (currentPeriod < periods - 1)
		{
			averageTrueRange = (averageTrueRange * currentPeriod + tick.trueRange) / (currentPeriod + 1);
			currentPeriod++;
		}
		else
		{
			averageTrueRange = (averageTrueRange * currentPeriod - 
								tl.getClosureOfDay(periods).trueRange + 
								tick.trueRange) / periods;	
		}
	}
	
	public double getValue()
	{
		return averageTrueRange;
	}
}
