package javaIndicatorsTest.Trend;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;

import Tick.Tick;
import Tick.TickLogger;
import javaIndicators.Trend.Trend;
import javaIndicatorsTest.DataLoader;

public class testTrend {
	List<Tick> tickList;

	@Test
	public void testAccumulationDistributionLine() 
	{
		double[] resultsADL = {
				-3187045.45454551,-8215409.09090924,-10577250.2673798,-7617386.63101627,-5037393.52756817,
				-9715226.86090153,-4405126.86090153,-11621583.271158,-9344508.27115778,-14839679.0028651,
				-16261491.5028651,-17282830.6332997,-16982648.2803585,-12530948.2803584,-14230919.7089298,
				-22819540.5880506,-24571296.5202541,-24082092.8165505,-23152938.9703965,-22615696.1132537,
				-23166137.0968602,-27029362.6782555,-26868299.0418919,-27535516.6889507,-22137100.4098809,
				-19819109.50079,-17386409.50079,-14150459.5007901,-14879175.2902637,-13177095.2902636,
				-12169823.8616922,-14073407.6454761,-16703471.6454762,-13232943.0740477,-18087756.7104112,
				-18225999.5675541,-19748548.7200964,-21395715.3867631,-19533782.0534298,-18997459.4727847,
				-26642051.3095194,-23306790.3339095,-25296274.9492941,-25155908.2826274,-23545617.3735366,
				-21164179.8735367,-22758668.7624256,-22215806.69346,-25285015.9957856,-24612481.5130271,
				-23499592.6241383,-23170369.0947264,-25178143.2882748,-21972879.1857104,-29266526.5541315,
				-26184246.5541314,-23311330.4250992,-28374330.4250992,-25417259.5160084,-20480131.8237007,
				-22339610.0845702,-20510537.3572976,-26028670.6906311,-25925063.2832237,-28880428.5006149,
				-31127302.1848255,-33731613.5055802,-30737215.2004954,-33566186.6290668,-39949486.6290668,
				-38919871.4775517,-37544862.3866425,-34443662.3866425,-32267758.0388164,-31448571.8319199,
				-32398467.8319199,-33251867.8319198,-30312549.3134013,-33689120.7419728,-35577291.8530839,
				-36445484.2581472,-34930115.8370947,-35130903.3370948,-32642478.3370948,-31248209.5870948,
				-29609512.8129013,-29463759.6214118,-36117508.8017397,-36314897.6906286,-38978084.7874027,
				-39178646.6921646,-39512903.5549097,-34070086.6318329,-31432878.4685676,-30735096.6503858,
				-32292886.1240701,-30800944.7447598,-32183467.8216829,-31891192.8216828,-34829490.1189801,
				-37037262.11898,-37949804.2242433,-38634645.6035536,-40954596.9549051,-39062942.4094505,
				-36473762.4094504,-35078021.2329798,-32903291.0004217,-32546741.0004216,-30300116.0004216,
				-30108504.8893105,-29725568.5256741,-32321312.525674,-30731585.2529467,-28748251.9196133,
				-25025251.9196133,-26787231.9196132,-28978067.2137309,-28103854.1702526,-33030539.8845383,
				-33256617.1572656,-31409902.2318925,-30454015.1351182,-30091431.8017849,-29025180.077647
		};
		
		tickList = DataLoader.populateTestData();
		Trend trend = new Trend();
		Trend.AccumulationDistributionLine adl = trend.getAccumulationDistributionLine();
		
		TickLogger tl = TickLogger.getInstance();
		for(int i = 0; i < tickList.size(); i++)
		{
			tl.addTick(tickList.get(i));
			trend.addItem();
			assertEquals(resultsADL[i], adl.getValue(), 0.0000001);
			System.out.println(adl.getValue() + " ");
		}
	}

	@Test
	public void testAverageDirectionalIndex() 
	{
		double[] resultsADX = {
				56.64,56.64,56.25,55.86,55.57,55.65,54.39,52.52,50.53,48.64,
				47.18,44.44,41.66,39.89,38.94,38.05,38.07,37.48,37.68,37.55,
				36.96,36.06,35.49,34.97,34.71,33.92,31.52,29.29,27.50,26.09,
				24.56,24.16,24.15,23.09,21.71,20.79,20.60,20.64,21.20,20.74,
				20.32,18.99,17.75,17.46,17.20,17.70,18.53,19.00,18.77,18.55,
				17.51,17.10,16.73,15.86,14.93,14.22,13.56,13.14,13.56,13.95,
				14.61,15.42,15.62,15.47,14.95,14.76,14.59,14.54,13.93,13.47,
				13.05,12.21,11.69,12.80,13.73,16.32,19.36,21.94,24.48,25.71,
				27.12,28.74,29.37,30.47,32.78,34.93,35.73,36.48,36.85,37.36,
				38.36,39.30,39.76,40.37,39.93,39.79,39.83,39.38,37.72,35.93,
				34.26,33.06,31.27,29.39,27.65,26.04,25.10,24.57,23.86,23.36,
				22.90,21.85,21.26,21.04,20.85,20.36,21.41,23.23,25.08,25.99,
				26.15,26.95,27.69,29.03,28.90,29.09,29.22
		};
		
		tickList = DataLoader.populateTestData();
		Trend trend = new Trend();
		Trend.AverageDirectionalIndex adx = trend.getAverageDirectionalIndex();
		
		TickLogger tl = TickLogger.getInstance();
		for(int i = 0; i < tickList.size(); i++)
		{
			tl.addTick(tickList.get(i));
			trend.addItem();
			assertEquals(resultsADX[i], adx.getValue(), 0.0000001);
			System.out.println(adx.getValue() + " ");
		}
	}

}
