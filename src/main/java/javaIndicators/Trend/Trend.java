package javaIndicators.Trend;
import Tick.Tick;
import Tick.TickLogger;
import javaIndicators.Averages.AverageTrueRange;

public class Trend {
	private AccumulationDistributionLine adl;
	private AverageDirectionalIndex adx;
	private TickLogger tl = TickLogger.getInstance();
	
	public class AccumulationDistributionLine {
		double moneyFlowMultiplier;
		double moneyFlowVolume;
		double accumulationDistributionLine;

		// moneyFlowVolume = moneyFlowMultiplier * Period’s Volume 
		//		ADL = Previous ADL + Current Money Flow Volume
		public AccumulationDistributionLine()
		{
			moneyFlowVolume = 0;
		}		
		public void addItem()
		{
			Tick tick = tl.getClosureOfDay(TickLogger.CONTINUOUS, 0);
			moneyFlowMultiplier = ((tick.close - tick.low) - (tick.high - tick.close))/(tick.high - tick.low); 
			moneyFlowVolume = moneyFlowMultiplier * tick.tradedVolume; 
			accumulationDistributionLine += moneyFlowVolume;
		}		

		public double getValue()
		{
			return accumulationDistributionLine;
		}
	}
	
	public class AverageDirectionalIndex {
		public static final int CROSS_POSITIVE = 2;
		public static final int POSITIVE = 1;
		public static final int NEGATIVE = -1;
		public static final int CROSS_NEGATIVE = -2;
		public static final int STILL = 0;
		
		double previousNDM;
		double previousPDM;
		
		int trendIndicator;
				
		public AverageDirectionalIndex()
		{
		}
		
		/*
		 * Requires tick to be added to logger already
		 */
		public void addItem()
		{
			double atr = AverageTrueRange.getAverageTrueRangeNDays(14);
			double smoothedDirectionalMovement = 0;
			for(int i = 1; i < Math.min(14, tl.numberOfTicksRecorded()); i++)
			{
				double nDM = tl.getClosureOfDay(TickLogger.CONTINUOUS, i + 1).low - tl.getClosureOfDay(TickLogger.CONTINUOUS, i).low;
				smoothedDirectionalMovement = nDM - 1/Math.min(14, tl.numberOfTicksRecorded()) * nDM;
			}
			smoothedDirectionalMovement += tl.getClosureOfDay(TickLogger.CONTINUOUS, 1).low - tl.getClosureOfDay(TickLogger.CONTINUOUS, 0).low;
			double negativeDirectionalMovement = smoothedDirectionalMovement / atr;
			
			smoothedDirectionalMovement = 0;
			for(int i = 1; i < Math.min(14, tl.numberOfTicksRecorded()); i++)
			{
				double nDM = tl.getClosureOfDay(TickLogger.CONTINUOUS, i + 1).high - tl.getClosureOfDay(TickLogger.CONTINUOUS, i).high;
				smoothedDirectionalMovement = nDM - 1/Math.min(14, tl.numberOfTicksRecorded()) * nDM;
			}
			smoothedDirectionalMovement += tl.getClosureOfDay(TickLogger.CONTINUOUS, 1).high - tl.getClosureOfDay(TickLogger.CONTINUOUS, 0).high;
			double positiveDirectionalMovement = smoothedDirectionalMovement / atr;
			if (negativeDirectionalMovement > positiveDirectionalMovement)
			{
				trendIndicator = NEGATIVE;				
				if (previousNDM <= previousPDM)
				{
					trendIndicator = CROSS_NEGATIVE;
				}
			}	
			else if (positiveDirectionalMovement > negativeDirectionalMovement)
			{
				trendIndicator = POSITIVE;
				if (previousPDM <= previousNDM)
				{
					trendIndicator = CROSS_POSITIVE;
				}
			}
			else
			{
				trendIndicator = STILL;
			}
		}
		public int getValue()
		{
			return trendIndicator;
		}
	}
	
	public Trend()
	{
		adl = new AccumulationDistributionLine();
		adx = new AverageDirectionalIndex();
	}
	
	public void addItem()
	{
		adl.addItem();
		adx.addItem();
	}
	
	public AccumulationDistributionLine getAccumulationDistributionLine()
	{
		return adl;
	}

	public AverageDirectionalIndex getAverageDirectionalIndex()
	{
		return adx;
	}
}
