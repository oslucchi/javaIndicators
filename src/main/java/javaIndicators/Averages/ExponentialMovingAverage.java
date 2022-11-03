package javaIndicators.Averages;

import java.lang.reflect.Field;
import java.util.List;

import Tick.Tick;
import Tick.TickLogger;

public class ExponentialMovingAverage {
	private int periods;
	private int period = 0;
	private List<Tick> tl = null;
	private Tick exponentialMovingAverages;
	private double k;

	public ExponentialMovingAverage(int periods, int frequency)
	{
		exponentialMovingAverages = new Tick();
		tl = TickLogger.getInstance().getTickList(frequency);
		this.periods = periods;
	}
	
	private void calculate(String fieldToCalculate) 
			  throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException
	{
		Field field = Tick.class.getDeclaredField(fieldToCalculate);

		if (period <= (periods - 1) / 2)
		{
			field.setDouble(exponentialMovingAverages, 
								(field.getDouble(exponentialMovingAverages) * period +
								 field.getDouble(tl.get(0))) / 
								 (period + 1));
		}
		else
		{
			k = 2.0 / (periods + 1);
			field.setDouble(exponentialMovingAverages, 
								(field.getDouble(exponentialMovingAverages) * (1 - k) +
								 field.getDouble(tl.get(0)) * k));
		}
	}
	
	public void addItem() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException
	{
		calculate("close");
		period++;

	}
	
	public Tick getAverage()
	{
		return exponentialMovingAverages;
	}
}
