package javaIndicators.Averages;

import java.lang.reflect.Field;
import java.util.List;

import Tick.Tick;
import Tick.TickLogger;

public class SimpleMovingAverage {
	private int periods;
	private int period = 0;
	private List<Tick> tl = null;
	private Tick simpleMovingAverage;

	public SimpleMovingAverage(int periods, int frequency)
	{
		simpleMovingAverage = new Tick();
		tl = TickLogger.getInstance().getTickList(frequency);
		this.periods = periods;
	}
	
	private void calculate(String fieldToCalculate) 
			  throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException
	{
		Field field = Tick.class.getDeclaredField(fieldToCalculate);
		if (period < periods)
		{
			field.setDouble(simpleMovingAverage, (field.getDouble(simpleMovingAverage) * period +
												  field.getDouble(tl.get(0))) /
												  (period + 1));
		}
		else
		{
			field.setDouble(simpleMovingAverage,
								(field.getDouble(simpleMovingAverage) * periods - 
								 field.getDouble(tl.get(periods)) + 
								 field.getDouble(tl.get(0))) /
								periods);
		}
	}
	
	public void addItem() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException
	{
		calculate("close");
		calculate("open");
		calculate("high");
		calculate("low");
		period++;
	}
	
	public Tick getAverage()
	{
		return simpleMovingAverage;
	}
}	
