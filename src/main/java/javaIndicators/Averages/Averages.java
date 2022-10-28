package javaIndicators.Averages;

import java.lang.reflect.Field;

import Tick.Tick;
import Tick.TickLogger;


public class Averages {
	private int periods;
	private int period = 0;
	private TickLogger tl = TickLogger.getInstance();

	public class WeightedMovingAverage {
		private Tick weightedMovingAverage = null;
		private Tick total = null;
		private Tick denominator = null;
		private Tick numerator;
		int[] weights;

		private void calculate(String fieldToCalculate, Tick lastInPeriod) 
							  throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException
		{
			Field field = Tick.class.getDeclaredField(fieldToCalculate);
			
			if (period < periods)
			{
				field.setDouble(weightedMovingAverage, field.getDouble(weightedMovingAverage) * field.getDouble(denominator));
				field.setDouble(denominator, (double)(weights[period] * weights[period + 1]) / 2);
				field.setDouble(weightedMovingAverage, 
									(field.getDouble(weightedMovingAverage) +
									 field.getDouble(tl.getClosureOfDay(TickLogger.CONTINUOUS, 0)) * weights[period]) /
									 field.getDouble(denominator));
				field.setDouble(numerator, 
									field.getDouble(numerator) + 
									field.getDouble(tl.getClosureOfDay(TickLogger.CONTINUOUS, 0)) * weights[period]);
				field.setDouble(total, 
									field.getDouble(total) + 
									field.getDouble(tl.getClosureOfDay(TickLogger.CONTINUOUS, 0))); 
			}
			else
			{
				field.setDouble(numerator,
									field.getDouble(numerator) + 
									(double) (weights[periods - 1] * field.getDouble(tl.getClosureOfDay(TickLogger.CONTINUOUS, 0)) - 
									field.getDouble(total)));
				field.setDouble(total, 
									field.getDouble(total) + 
									field.getDouble(tl.getClosureOfDay(TickLogger.CONTINUOUS, 0)) - 
									field.getDouble(tl.getClosureOfDay(TickLogger.CONTINUOUS, periods)));
				field.setDouble(denominator, (double)(weights[periods - 1] * weights[period]) / 2);
				field.setDouble(weightedMovingAverage, 
									field.getDouble(numerator) / 
									field.getDouble(denominator));
			}
		}
		
		public WeightedMovingAverage()
		{
			total = new Tick();
			denominator = new Tick();
			numerator = new Tick();
			weightedMovingAverage = new Tick();
			this.weights = new int[periods + 1];
			for(int i = 1; i <= periods + 1; i++)
			{
				this.weights[i - 1] = i;
			}
		}
		
		public void setWeights(int[] weights)
		{
			this.weights = weights;
		}
		
		private void addItem() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException
		{
			Tick lastValueInPeriod = tl.getClosureOfDay(TickLogger.CONTINUOUS, period);
			calculate("close", lastValueInPeriod);
			calculate("open", lastValueInPeriod);
			calculate("high", lastValueInPeriod);
			calculate("low", lastValueInPeriod);
		}
		
		public Tick getAverage()
		{
			return weightedMovingAverage;
		}		
	}
	
	public class SimpleMovingAverage {
		private Tick simpleMovingAverage;

		public SimpleMovingAverage()
		{
			simpleMovingAverage = new Tick();
		}
		
		private void calculate(String fieldToCalculate) 
				  throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException
		{
			Field field = Tick.class.getDeclaredField(fieldToCalculate);
			if (period < periods)
			{
				field.setDouble(simpleMovingAverage, (field.getDouble(simpleMovingAverage) * period +
													  field.getDouble(tl.getClosureOfDay(TickLogger.CONTINUOUS, 0))) /
													  (period + 1));
			}
			else
			{
				field.setDouble(simpleMovingAverage,
									(field.getDouble(simpleMovingAverage) * periods - 
									 field.getDouble(tl.getClosureOfDay(TickLogger.CONTINUOUS, periods)) + 
									 field.getDouble(tl.getClosureOfDay(TickLogger.CONTINUOUS, 0))) /
									periods);
			}
		}
		
		private void addItem() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException
		{
			calculate("close");
			calculate("open");
			calculate("high");
			calculate("low");
		}
		
		public Tick getAverage()
		{
			return simpleMovingAverage;
		}
	}	
	
	public class ExponentialMovingAverage {
		private Tick exponentialMovingAverages;
		private double k;

		public ExponentialMovingAverage()
		{
			exponentialMovingAverages = new Tick();
		}
		
		private void calculate(String fieldToCalculate) 
				  throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException
		{
			Field field = Tick.class.getDeclaredField(fieldToCalculate);

			if (period <= (periods - 1) / 2)
			{
				field.setDouble(exponentialMovingAverages, 
									(field.getDouble(exponentialMovingAverages) * period +
									 field.getDouble(tl.getClosureOfDay(TickLogger.CONTINUOUS, 0))) / 
									 (period + 1));
			}
			else
			{
				k = 2.0 / (periods + 1);
				field.setDouble(exponentialMovingAverages, 
									(field.getDouble(exponentialMovingAverages) * (1 - k) +
									 field.getDouble(tl.getClosureOfDay(TickLogger.CONTINUOUS, 0)) * k));
			}
		}
		
		private void addItem() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException
		{
			calculate("close");
		}
		
		public Tick getAverage()
		{
			return exponentialMovingAverages;
		}
	}

	private WeightedMovingAverage wma;
	private SimpleMovingAverage sma;
	private ExponentialMovingAverage ema;
	
	public Averages(int periods)
	{
		this.periods = periods;
		
		wma = new WeightedMovingAverage();
		sma = new SimpleMovingAverage();
		ema = new ExponentialMovingAverage();
	}
	
	public void addItem() 
					throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException
	{
		wma.addItem();
		sma.addItem();
		ema.addItem();
		
		if (period < periods)
		{
			period++;
		}
	}
	
	public WeightedMovingAverage getWeightedMovingAverage()
	{
		return wma;
	}

	public SimpleMovingAverage getSimpleMovingAverage() {
		return sma;
	}
	
	public ExponentialMovingAverage getExponentialMovingAverages() {
		return ema;
	}
	
}
