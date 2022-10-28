package Tick;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TickLogger {
	static public final int CONTINUOUS = 0;
	static public final int ONE_SECOND = 1;
	static public final int FIVE_SECONDS = 2;
	static public final int ONE_MINUTE= 3;
	static public final int ONE_HOUR= 4;
	static public final int ONE_DAY = 5;
	
	private Tick consolidatedOneSec;
	private Tick consolidatedFiveSec;
	private Tick consolidatedOneMin;
	private Tick consolidatedOneHour;
	private Tick consolidatedOneDay;
	
	private static List<List<Tick>> frequencies;
	private static TickLogger instance = null;
	
	private long previousTimestampOneSec = -1;
	private long previousTimestampFiveSec = -1;
	private long previousTimestampOneMin = -1;
	private long previousTimestampOneHour = -1;
	private long previousTimestampOneDay = -1;
	
	public static TickLogger getInstance()
	{
		if (instance == null)
		{
			instance = new TickLogger();
		}
		return instance;
	}

	private TickLogger()
	{
		frequencies = new ArrayList<List<Tick>>();
		frequencies.add(CONTINUOUS, new ArrayList<Tick>());
		frequencies.add(ONE_SECOND, new ArrayList<Tick>());
		frequencies.add(FIVE_SECONDS, new ArrayList<Tick>());
		frequencies.add(ONE_MINUTE, new ArrayList<Tick>());
		frequencies.add(ONE_HOUR, new ArrayList<Tick>());
		frequencies.add(ONE_DAY, new ArrayList<Tick>());
		
		consolidatedOneSec = new Tick();
		consolidatedFiveSec = new Tick();
		consolidatedOneMin = new Tick();
		consolidatedOneHour = new Tick();
		consolidatedOneDay = new Tick();
	}
	
	public void addTick(Tick tick)
	{
		frequencies.get(CONTINUOUS).add(0, tick);
	
		if (previousTimestampOneSec == -1)
		{
			previousTimestampOneSec =
					previousTimestampFiveSec =
					previousTimestampOneMin = 
					previousTimestampOneHour = 
					previousTimestampOneDay = tick.timestamp.getTime() -  tick.timestamp.getTime() % 1000;
		}
		
		if (frequencies.get(CONTINUOUS).size() > 1)
		{
			tick.trueRange = tick.high - tick.low;
			if (Math.abs(tick.high - frequencies.get(CONTINUOUS).get(1).close) > tick.trueRange)
			{
				tick.trueRange = Math.abs(tick.high - frequencies.get(CONTINUOUS).get(1).close);
			}
			if (Math.abs(frequencies.get(CONTINUOUS).get(1).close - tick.low) > tick.trueRange)
			{
				tick.trueRange = Math.abs(frequencies.get(CONTINUOUS).get(1).close - tick.low) ;
			}
		}
		
		if (tick.timestamp.getTime() - previousTimestampOneSec > 1000)
		{
			previousTimestampOneSec = tick.timestamp.getTime() - tick.timestamp.getTime() % 1000;
			consolidatedOneSec = tick;
			frequencies.get(ONE_SECOND).add(0, consolidatedOneSec);
			if (frequencies.get(ONE_SECOND).size() > 1)
			{
				consolidatedOneSec.trueRange = consolidatedOneSec.high - consolidatedOneSec.low;
				if (Math.abs(consolidatedOneSec.high - frequencies.get(ONE_SECOND).get(1).close) > consolidatedOneSec.trueRange)
				{
					consolidatedOneSec.trueRange = Math.abs(consolidatedOneSec.high - frequencies.get(CONTINUOUS).get(1).close);
				}
				if (Math.abs(frequencies.get(CONTINUOUS).get(1).close - consolidatedOneSec.low) > consolidatedOneSec.trueRange)
				{
					consolidatedOneSec.trueRange = Math.abs(frequencies.get(CONTINUOUS).get(1).close - consolidatedOneSec.low) ;
				}
			}
			consolidatedOneSec = new Tick();
		}
		else
		{
			if (consolidatedOneSec.open == 0)
			{
				consolidatedOneSec.open = tick.open;
				consolidatedOneSec.high = tick.high;
				consolidatedOneSec.low = tick.low;
				consolidatedOneSec.close = 0;
			}
			if (consolidatedOneSec.high < tick.high)
			{
				consolidatedOneSec.high = tick.high;
			}
			if (consolidatedOneSec.low > tick.low)
			{
				consolidatedOneSec.low = tick.low;
			}
		}

		if (tick.timestamp.getTime() - previousTimestampFiveSec > 5000)
		{
			previousTimestampFiveSec = tick.timestamp.getTime() - tick.timestamp.getTime() % 5000;
			consolidatedFiveSec = tick;
			frequencies.get(FIVE_SECONDS).add(0, consolidatedFiveSec);
			if (frequencies.get(FIVE_SECONDS).size() > 1)
			{
				consolidatedFiveSec.trueRange = consolidatedFiveSec.high - consolidatedFiveSec.low;
				if (Math.abs(consolidatedFiveSec.high - frequencies.get(ONE_SECOND).get(1).close) > consolidatedFiveSec.trueRange)
				{
					consolidatedFiveSec.trueRange = Math.abs(consolidatedFiveSec.high - frequencies.get(CONTINUOUS).get(1).close);
				}
				if (Math.abs(frequencies.get(CONTINUOUS).get(1).close - consolidatedFiveSec.low) > consolidatedFiveSec.trueRange)
				{
					consolidatedFiveSec.trueRange = Math.abs(frequencies.get(CONTINUOUS).get(1).close - consolidatedFiveSec.low) ;
				}
			}

			consolidatedFiveSec = new Tick();
		}
		else
		{
			if (consolidatedFiveSec.open == 0)
			{
				consolidatedFiveSec.open = tick.open;
				consolidatedFiveSec.high = tick.high;
				consolidatedFiveSec.low = tick.low;
				consolidatedFiveSec.close = 0;
			}
			if (consolidatedFiveSec.high < tick.high)
			{
				consolidatedFiveSec.high = tick.high;
			}
			if (consolidatedFiveSec.low > tick.low)
			{
				consolidatedFiveSec.low = tick.low;
			}
		}
		
		if (tick.timestamp.getTime() - previousTimestampOneMin > 60000)
		{
			previousTimestampOneMin = tick.timestamp.getTime();
			consolidatedOneMin.close = tick.close;
			frequencies.get(ONE_MINUTE).add(0, consolidatedOneMin);
			consolidatedOneMin = new Tick();
		}
		else
		{
			if (consolidatedOneMin.open == 0)
			{
				consolidatedOneMin.open = tick.open;
				consolidatedOneMin.high = tick.high;
				consolidatedOneMin.low = tick.low;
				consolidatedOneMin.close = 0;
			}
			if (consolidatedOneMin.high < tick.high)
			{
				consolidatedOneMin.high = tick.high;
			}
			if (consolidatedOneMin.low > tick.low)
			{
				consolidatedOneMin.low = tick.low;
			}
		}

		if (tick.timestamp.getTime() - previousTimestampOneHour > 3600000)
		{
			previousTimestampOneHour = tick.timestamp.getTime();
			consolidatedOneHour.close = tick.close;
			frequencies.get(ONE_HOUR).add(0, consolidatedOneHour);
			consolidatedOneHour = new Tick();
		}
		else
		{
			if (consolidatedOneHour.open == 0)
			{
				consolidatedOneHour.open = tick.open;
				consolidatedOneHour.high = tick.high;
				consolidatedOneHour.low = tick.low;
				consolidatedOneHour.close = 0;
			}
			if (consolidatedOneHour.high < tick.high)
			{
				consolidatedOneHour.high = tick.high;
			}
			if (consolidatedOneHour.low > tick.low)
			{
				consolidatedOneHour.low = tick.low;
			}
		}
	}
	
	public Tick getClosureOfDay(int frequency, int previousUnits)
	{
		if (frequencies.get(frequency).size() >= previousUnits)
			return frequencies.get(frequency).get(previousUnits);
		else
			return null;
	}
}
