package javaIndicatorsTest.Trend;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import Tick.Tick;
import Tick.TickLogger;
import utils.DataLoader;

public class Alligator {
	
	List<Tick> tickList = new ArrayList<Tick>();

	public Alligator()
	{
		tickList = DataLoader.populateTestData("docs/averageMarketData.ser");
	}

	@Test
	public void testAlligator()
	{
		TickLogger tl = TickLogger.getInstance();
		javaIndicators.Trend.Alligator all = new javaIndicators.Trend.Alligator(TickLogger.CONTINUOUS);
		for(int newItem = 0; newItem < tickList.size(); newItem++)
		{
			tl.addTick(tickList.get(newItem));
			try 
			{
				all.addItem();
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			System.out.format("%7.4f;%7.4f;%7.4f\n", all.getValue(javaIndicators.Trend.Alligator.JAW), 
														 all.getValue(javaIndicators.Trend.Alligator.TEETH),
														 all.getValue(javaIndicators.Trend.Alligator.LIPS));
//			assertEquals(resultsSMMA[newItem], smma.getValue(), 0.001);
		}
	}
}
