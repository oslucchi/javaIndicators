package javaIndicators.Averages;

import java.lang.reflect.Field;
import java.util.List;

import Tick.Tick;
import Tick.TickLogger;

public class WeightedMovingAverage {
	private int periods;
	private int period = 0;
	private List<Tick> tl = null;
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
								 field.getDouble(tl.get(0)) * weights[period]) /
								 field.getDouble(denominator));
			field.setDouble(numerator, 
								field.getDouble(numerator) + 
								field.getDouble(tl.get(0)) * weights[period]);
			field.setDouble(total, 
								field.getDouble(total) + 
								field.getDouble(tl.get(0))); 
		}
		else
		{
			field.setDouble(numerator,
								field.getDouble(numerator) + 
								(double) (weights[periods - 1] * field.getDouble(tl.get(0)) - 
								field.getDouble(total)));
			field.setDouble(total, 
								field.getDouble(total) + 
								field.getDouble(tl.get(0)) - 
								field.getDouble(tl.get(periods)));
			field.setDouble(denominator, (double)(weights[periods - 1] * weights[periods]) / 2);
			field.setDouble(weightedMovingAverage, 
								field.getDouble(numerator) / 
								field.getDouble(denominator));
		}
	}
	
	public WeightedMovingAverage(int periods, int frequency)
	{
		tl = TickLogger.getInstance().getTickList(frequency);
		total = new Tick();
		denominator = new Tick();
		numerator = new Tick();
		weightedMovingAverage = new Tick();
		this.weights = new int[periods + 1];
		this.periods = periods;
		for(int i = 1; i <= periods + 1; i++)
		{
			this.weights[i - 1] = i;
		}
	}
	
	public void setWeights(int[] weights)
	{
		this.weights = weights;
	}
	
	public void addItem() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException
	{
		Tick lastValueInPeriod = tl.get(period);
		calculate("close", lastValueInPeriod);
		calculate("open", lastValueInPeriod);
		calculate("high", lastValueInPeriod);
		calculate("low", lastValueInPeriod);
		period++;
	}
	
	public Tick getAverage()
	{
		return weightedMovingAverage;
	}		
}
