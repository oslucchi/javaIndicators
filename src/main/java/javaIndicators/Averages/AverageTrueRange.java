package javaIndicators.Averages;

import java.util.List;

import Tick.Tick;

public class AverageTrueRange {
	double averageTrueRange;
	int periods;
	int currentPeriod;
	private List<Tick> tl = null;
	
	public AverageTrueRange(int periods, List<Tick> tl)
	{
		this.periods = periods; 
		this.tl = tl;
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
	
	public double getAverageTrueRangeNDays(int periods)
	{
		double averageTrueRange = 0;
		if (tl.size() < periods)
			periods = tl.size();
		
		for(int i = 0; i < periods; i++)
		{
//			averageTrueRange += tl.getClosureOfDay(TickLogger.CONTINUOUS, i).trueRange;
			averageTrueRange += tl.get(i).trueRange;
		}
		averageTrueRange /= periods;
		return averageTrueRange;
	}
	
	public double getPeriodAverageTrueRange()
	{
		double averageTrueRange = 0;
		
		for(int i = 0; i < (tl.size() < periods ? tl.size() : periods); i++)
		{
			averageTrueRange += tl.get(i).trueRange;
		}
		averageTrueRange /= (tl.size() < periods ? tl.size() : periods);
		return averageTrueRange;
	}

	public double getTrueRangeNDays(int periods)
	{
		double averageTrueRange = 0;

		if (tl.size() < periods)
			periods = tl.size();

		for(int i = 0; i < periods; i++)
		{
			averageTrueRange += tl.get(i).trueRange;
		}
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
								tl.get(periods).trueRange + 
								tick.trueRange) / periods;	
		}
	}
	
	public double getValue()
	{
		return averageTrueRange;
	}
}
