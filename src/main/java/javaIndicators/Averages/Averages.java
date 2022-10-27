package javaIndicators.Averages;

public class Averages {
	private int periods;
	private double[] values;
	private int period = 0;
	private int current = 0;

	public class WeightedMovingAverage {
		private double weightedMovingAverage;
		private double total;
		private double denominator;
		private double numerator;
		int[] weights;

		public WeightedMovingAverage()
		{
			total = 0.;
			denominator = 0.;
			numerator = 0.;
			weightedMovingAverage = 0.;
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
		
		private void addItem(double value)
		{
			if (period < periods)
			{
				weightedMovingAverage = weightedMovingAverage * denominator;
				denominator = (double)(weights[period] * weights[period + 1]) / 2;
				weightedMovingAverage += value * weights[period];
				weightedMovingAverage /= denominator;
				numerator += value * weights[period];
				total += value;
			}
			else
			{
				numerator += (double) weights[periods - 1] * value - total;
				total += value - values[current % periods];
				denominator = (double)(weights[periods - 1] * weights[periods]) / 2;
				weightedMovingAverage = numerator / denominator;
			}		
		}
		public double getAverage()
		{
			return weightedMovingAverage;
		}		
	}
	
	public class SimpleMovingAverage {
		private double simpleMovingAverage;

		public SimpleMovingAverage()
		{
		}
		
		private void addItem(double value)
		{
			if (period < periods)
			{
				simpleMovingAverage = simpleMovingAverage * period + value;
				simpleMovingAverage /= (period + 1);
			}
			else
			{
				simpleMovingAverage = simpleMovingAverage * periods - values[current % periods] + value;
				simpleMovingAverage /= periods;
			}		
		}
		public double getAverage()
		{
			return simpleMovingAverage;
		}
	}
	
	public class ExponentialMovingAverage {
		private double exponentialMovingAverages;
		private double k;

		public ExponentialMovingAverage()
		{
		}
		
		private void addItem(double value)
		{
			if (period <= (periods - 1) / 2)
			{
				exponentialMovingAverages = exponentialMovingAverages * period + value;
				exponentialMovingAverages /= (period + 1);
			}
			else
			{
				k = 2.0 / (periods + 1);
				exponentialMovingAverages = value * k + exponentialMovingAverages * (1 - k);
			}
		}
		
		public double getAverage()
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
		this.values = new double[periods];
		
		wma = new WeightedMovingAverage();
		sma = new SimpleMovingAverage();
		ema = new ExponentialMovingAverage();
	}
	
	public void addItem(double value)
	{
		wma.addItem(value);
		sma.addItem(value);
		ema.addItem(value);
		
		if (period < periods)
		{
			values[period++] = value;
		}
		else
		{
			values[current++ % periods] = value;
		}
	}
	
	public double[] getValues()
	{
		return values;
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
