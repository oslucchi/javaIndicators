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
    int shift;
    List<Tick> tl;
    
    public SmoothedMovingAverage(int periods, int shift, int frequency)
    {
    	        // initialize
        smmaResults = new double[periods];
        this.periods = periods;
        this.shift = shift;
        this.tl = TickLogger.getInstance().getTickList(frequency);
    }
    
    public void addItem()
    {        
        // roll through quotes
        double smma = 0.;

        // calculate SMMA
        if (period + 1 - shift > periods)
        {
            smma = ((prevValue * (periods - 1)) + tl.get(shift).close) / periods;
        }
        else if (period + 1 - shift == periods)
        {
            double sumClose = 0;
            for (int p = period + 1 - shift - periods; p < periods; p++)
            {
                sumClose += tl.get(p+shift).close;
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
