package javaIndicators.Trend;
import java.util.List;

import Tick.Tick;
import Tick.TickLogger;
import javaIndicators.Averages.AverageTrueRange;

public class Trend {
	private AccumulationDistributionLine adl;
	private AverageDirectionalIndex adx;
	private List<Tick> tl;
	private int periods;
	private int period = 0;
	
	
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
			Tick tick = tl.get(0);
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

		public static final int PLUS = 0;
		public static final int MINUS = 1;
		
		public static final int CROSS_POSITIVE = 2;
		public static final int POSITIVE = 1;
		public static final int NEGATIVE = -1;
		public static final int CROSS_NEGATIVE = -2;
		public static final int STILL = 0;

		private double[] smoothedDirectionalMovement = {0., 0.};
		
		private double averageDirectionalIndex = 0.;
		private double[] periodDirectionalIndex = new double[periods];
		
		private int trendIndicator;		
		private AverageTrueRange atr;

		public AverageDirectionalIndex()
		{
			atr = new AverageTrueRange(14, tl);
		}
		
		public void addItem()
		{
			double periodAtr = atr.getPeriodAverageTrueRange();
			double[] directionIndicator = {0., 0.};
			double[] directionalMovement = {0., 0.};
			double directionalIndex = 0;
			
			Tick current = tl.get(0);
			Tick previous = tl.get(1);
			if (current.high - previous.high > previous.low - current.low)
			{
				directionalMovement[PLUS] = Math.max(current.high - previous.high, 0.);
				directionalMovement[MINUS] = 0.;
			}
			else if (previous.low - current.low > current.high - previous.high)
			{
				directionalMovement[PLUS] = 0.;
				directionalMovement[MINUS] = Math.max(previous.low - current.low, 0.);
			}

			if (period < periods - 1)
			{
				smoothedDirectionalMovement[PLUS] += directionalMovement[PLUS];
				smoothedDirectionalMovement[MINUS] += directionalMovement[MINUS];
			}
			else
			{
				if (period == periods)
				{
					smoothedDirectionalMovement[PLUS] += directionalMovement[PLUS];
					smoothedDirectionalMovement[MINUS] += directionalMovement[MINUS];
				}
				else
				{
					smoothedDirectionalMovement[PLUS] = smoothedDirectionalMovement[PLUS] -
														(smoothedDirectionalMovement[PLUS] / periods) +
														directionalMovement[PLUS];
					
					smoothedDirectionalMovement[MINUS] = smoothedDirectionalMovement[MINUS] -
							  							 (smoothedDirectionalMovement[MINUS] / periods) +														  
							  							 directionalMovement[MINUS];
				}
				directionIndicator[PLUS] = smoothedDirectionalMovement[PLUS] * 100 / periodAtr;
				directionIndicator[MINUS] = smoothedDirectionalMovement[MINUS] * 100 / periodAtr;
				
				directionalIndex = 
						(Math.abs(directionIndicator[PLUS] - directionIndicator[MINUS]) /
						 Math.abs(directionIndicator[PLUS] + directionIndicator[MINUS])) * 100;
				
				averageDirectionalIndex = ((averageDirectionalIndex * periods -
										    periodDirectionalIndex[period % periods]) + 
										   directionalIndex) / 
										  periods;
				periodDirectionalIndex[period % periods] = directionalIndex;
				
				if (directionIndicator[PLUS] > directionIndicator[MINUS])
				{
					
					if (trendIndicator == NEGATIVE)
					{
						trendIndicator = CROSS_POSITIVE;
					}
					else
					{
						trendIndicator = POSITIVE;				
					}
				}	
				else if (directionIndicator[MINUS] > directionIndicator[PLUS])
				{
					
					if (trendIndicator == POSITIVE)
					{
						trendIndicator = CROSS_NEGATIVE;
					}
					else
					{
						trendIndicator = NEGATIVE;				
					}
				}	
				else
				{
					trendIndicator = STILL;
				}
			}
		
//			System.out.format("%03d;%7.4f;%7.4f;%7.4f;%7.4f;%9d;%6.4f;%6.4f;%6.4f;%6.4f;%6.4f;%6.4f;%6.4f;%6.4f;%6.4f;%6.4f;%6.4f;%6.4f;%d\n",
//								circularArrayIndex,
//								current.open,
//								current.high,
//								current.low,
//								current.close,
//								current.tradedVolume,
//								current.trueRange,
//								directionalMovement[PLUS],
//								directionalMovement[MINUS],
//								periodAtr,
//								smoothedDirectionalMovement[PLUS],
//								smoothedDirectionalMovement[MINUS],
//								directionIndicator[PLUS],
//								directionIndicator[MINUS],
//								Math.abs(directionIndicator[PLUS] + directionIndicator[MINUS]),
//								Math.abs(directionIndicator[PLUS] - directionIndicator[MINUS]),
//								directionalIndex,
//								averageDirectionalIndex,
//								trendIndicator 
//							);

//			System.out.print(", " + averageDirectionalIndex);
//			System.out.print(", " + trendIndicator);
		}

		
		/*
		 * Requires tick to be added to logger already
		 */
		public double getValue()
		{
			return averageDirectionalIndex;
		}
		
		public int getSytnteticIndicator()
		{
			return trendIndicator;
		}
	}
	
	public Trend(int periods, int useFrequency)
	{
		tl = TickLogger.getInstance().getTickList(useFrequency);
		this.periods = periods;
		adl = new AccumulationDistributionLine();
		adx = new AverageDirectionalIndex();
	}
	
	public void addItem()
	{
		adl.addItem();
		adx.addItem();
		period++;
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
