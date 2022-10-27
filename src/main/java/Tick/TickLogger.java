package Tick;

import java.util.ArrayList;
import java.util.List;

public class TickLogger {
	static final int	YESTERDAY = 1;
	List<Tick> tickList;
	private static TickLogger instance = null;

	public static TickLogger getInstance()
	{
		if (instance == null)
		{
			instance = new TickLogger();
		}
		return(instance);
	}

	private TickLogger()
	{
		tickList = new ArrayList<Tick>();
	}
	
	public void addTick(Tick tick)
	{
		int lastTick = tickList.size();
		tickList.add(tick);
		
		if (tickList.size() > 1)
		{
			tick.trueRange = tick.high - tick.low;
			if (Math.abs(tick.high - tickList.get(lastTick - 1).close) > tick.trueRange)
			{
				tick.trueRange = Math.abs(tick.high - tickList.get(lastTick - 1).close);
			}
			if (Math.abs(tickList.get(lastTick - 1).close - tick.low) > tick.trueRange)
			{
				tick.trueRange = Math.abs(tickList.get(lastTick - 1).close - tick.low) ;
			}
		}
	}
	
	public Tick getClosureOfDay(int daysBeforeToday)
	{
		return new Tick();
	}
}
