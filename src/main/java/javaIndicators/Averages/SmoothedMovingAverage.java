package javaIndicators.Averages;

import java.util.List;

import Tick.Tick;
import Tick.TickLogger;

public class SmoothedMovingAverage {
	// SMOOTHED MOVING AVERAGE (SERIES)
    // calculate series
    double[] smmaResults;
    double prevValue = 0.;
    int period = 0;
    int periods;
    List<Tick> tl;
    
    public SmoothedMovingAverage(int periods, int frequency)
    {
    	        // initialize
        smmaResults = new double[periods];
        this.periods = periods;
        this.tl = TickLogger.getInstance().getTickList(frequency);
    }
    
    public void addItem()
    {        
        // roll through quotes
        double smma = 0.;
        Tick tick = tl.get(0);

        // calculate SMMA
        if (period + 1 > periods)
        {
            smma = ((prevValue * (periods - 1)) + tick.close) / periods;
        }

        // first SMMA calculated as simple SMA
        else if (period + 1 == periods)
        {
            double sumClose = 0;
            for (int p = period + 1 - periods; p <= period; p++)
            {
                sumClose += tl.get(p).close;
            }

            smma = sumClose / periods;
        }
        smmaResults[period % periods] = smma;
        prevValue = smma;
        period++;
    }
    
    public double getValue()
    {
    	return prevValue;
    }
}
