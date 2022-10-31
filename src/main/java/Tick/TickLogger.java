package Tick;

import java.util.ArrayList;
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
	
	private static List<List<Tick>> frequencies;
	private static TickLogger instance = null;
	
	private long previousTimestampOneSec = -1;
	private long previousTimestampFiveSec = -1;
	private long previousTimestampOneMin = -1;
	private long previousTimestampOneHour = -1;
	
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
		consolidatedOneSec.open = Double.MAX_VALUE;
		consolidatedOneSec.low = Double.MAX_VALUE;
		consolidatedOneSec.high = Double.MIN_VALUE;
		consolidatedOneSec.close = Double.MIN_VALUE;

		consolidatedFiveSec = new Tick();
		consolidatedFiveSec.open = Double.MAX_VALUE;
		consolidatedFiveSec.low = Double.MAX_VALUE;
		consolidatedFiveSec.high = Double.MIN_VALUE;
		consolidatedFiveSec.close = Double.MIN_VALUE;

		consolidatedOneMin = new Tick();
		consolidatedOneMin.open = Double.MAX_VALUE;
		consolidatedOneMin.low = Double.MAX_VALUE;
		consolidatedOneMin.high = Double.MIN_VALUE;
		consolidatedOneMin.close = Double.MIN_VALUE;

		consolidatedOneHour = new Tick();
		consolidatedOneHour.open = Double.MAX_VALUE;
		consolidatedOneHour.low = Double.MAX_VALUE;
		consolidatedOneHour.high = Double.MIN_VALUE;
		consolidatedOneHour.close = Double.MIN_VALUE;
	}
	
	private long consolidateOverPeriod(int period, int millisInPeriod, long previousTimeStamp,
									   Tick currentTick, Tick consolidated)
	{
		if (currentTick.timestamp.getTime() - previousTimeStamp > millisInPeriod)
		{
			previousTimeStamp = currentTick.timestamp.getTime() - currentTick.timestamp.getTime() % millisInPeriod;
			frequencies.get(period).add(0, consolidated);
			if (frequencies.get(period).size() > 1)
			{
				consolidated.trueRange = 0;
				if (consolidated.low - frequencies.get(period).get(1).close  > consolidated.trueRange)
				{
					consolidated.trueRange = frequencies.get(period).get(1).close - consolidated.low;
				}
				if (Math.abs(consolidated.high - frequencies.get(period).get(1).close) > consolidated.trueRange)
				{
					consolidated.trueRange = Math.abs(consolidated.high - frequencies.get(period).get(1).close);
				}
				if (consolidated.high - consolidated.low > consolidated.trueRange)
				{
					consolidated.trueRange = consolidated.high - consolidated.low;
				}
			}

			consolidated = new Tick();
			consolidated.open = Double.MAX_VALUE;
			consolidated.low = Double.MAX_VALUE;
			consolidated.high = Double.MIN_VALUE;
			consolidated.close = Double.MIN_VALUE;
		}
		else
		{
			if (consolidated.open > currentTick.open)
			{
				consolidated.open = currentTick.open;
			}
			if (consolidated.low > currentTick.low)
			{
				consolidated.low = currentTick.low;
			}
			if (consolidated.high < currentTick.high)
			{
				consolidated.high = currentTick.high;
			}
			if (consolidated.close < currentTick.close)
			{
				consolidated.close = currentTick.close;
			}
		}
		return previousTimeStamp;
	}

	public void addTick(Tick tick)
	{
		frequencies.get(CONTINUOUS).add(0, tick);
	
		if (previousTimestampOneSec == -1)
		{
			previousTimestampOneSec =
					previousTimestampFiveSec =
					previousTimestampOneMin = 
					previousTimestampOneHour = tick.timestamp.getTime() -  tick.timestamp.getTime() % 1000;
		}
		
		if (frequencies.get(CONTINUOUS).size() > 1)
		{
			tick.trueRange = 0;;
			if (tick.low - frequencies.get(CONTINUOUS).get(1).close  > tick.trueRange)
			{
				tick.trueRange = frequencies.get(CONTINUOUS).get(1).close - tick.low;
			}
			if (Math.abs(tick.high - frequencies.get(CONTINUOUS).get(1).close) > tick.trueRange)
			{
				tick.trueRange = Math.abs(tick.high - frequencies.get(CONTINUOUS).get(1).close);
			}
			if (tick.high - tick.low > tick.trueRange)
			{
				tick.trueRange = tick.high - tick.low;
			}
		}
		else
		{
			tick.trueRange = 0.;
		}
		
		previousTimestampOneSec = consolidateOverPeriod(ONE_SECOND, 1000, previousTimestampOneSec, 
														tick, consolidatedOneSec);
		
		previousTimestampFiveSec = consolidateOverPeriod(FIVE_SECONDS, 5000, previousTimestampFiveSec, 
														 tick, consolidatedFiveSec);

		previousTimestampOneMin = consolidateOverPeriod(ONE_MINUTE, 60000, previousTimestampOneMin, 
														tick, consolidatedOneMin);
		
		previousTimestampOneHour = consolidateOverPeriod(ONE_HOUR, 3600000, previousTimestampOneHour, 
														tick, consolidatedOneHour);

	}
	
	public int numberOfTicksRecorded()
	{
		return frequencies.get(CONTINUOUS).size();
	}
	
	public Tick getClosureOfDay(int frequency, int previousUnits)
	{
		if (frequencies.get(frequency).size() >= previousUnits)
			return frequencies.get(frequency).get(previousUnits);
		else
			return null;
	}
}
