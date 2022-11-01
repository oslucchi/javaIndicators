package utils;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import Tick.Tick;

public class DataLoader {
	static List<Tick> tickList = new ArrayList<Tick>();
	
	public void readData() {
		BufferedReader reader;
		String[] split;
		Tick tick;
		try {
			Date start = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").parse("2022/10/01 09:00:03");
//			reader = new BufferedReader(new FileReader("docs/adxMarketData.csv"));
			reader = new BufferedReader(new FileReader("docs/adlMarketData.csv"));
			String line = reader.readLine();
			while (line != null) {
				split = line.split(";");
				tick = new Tick();
				try
				{
					tick.open = Double.parseDouble(split[0]);
					tick.high = Double.parseDouble(split[1]);
					tick.low = Double.parseDouble(split[2]);
					tick.close = Double.parseDouble(split[3]);
					tick.tradedVolume = Long.parseLong(split[4]);
					tick.timestamp = new Date(start.getTime() + 150);
					start = tick.timestamp;
					tickList.add(tick);
				}
				catch(Exception e)
				{
					System.out.println("Line '" + line + "' discarded'");
				}
				// read next line
				line = reader.readLine();
			}
			reader.close();
//            FileOutputStream fos = new FileOutputStream("docs/adxMarketData.ser");
            FileOutputStream fos = new FileOutputStream("docs/adlMarketData.ser");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(tickList);
            oos.close();
            fos.close();

		} catch (IOException | ParseException e) {
			e.printStackTrace();
		}
		System.exit(0);
	}

	@SuppressWarnings("unchecked")
	public static ArrayList<Tick> populateTestData(String filename) {
		ArrayList<Tick> tickList = new ArrayList<Tick>();
//        String filename = "docs/ADLlistData";
          
        try
        {   
            // Reading the object from a file
            FileInputStream file = new FileInputStream(filename);
            ObjectInputStream in = new ObjectInputStream(file);
              
            // Method for deserialization of object
            tickList = (ArrayList<Tick>)in.readObject();
              
            in.close();
            file.close();
//            SimpleDateFormat df = new SimpleDateFormat("yy/MM/dd HH:mm:ss:S");
//        	System.out.format("%20.20s;%5.5s;%5.5s;%5.5s;%5.5s;%9.9s\n",
//        					  "TIMESTAMP",
//        					  "OPEN",
//        					  "HIGH",
//        					  "LOW",
//        					  "CLOSE",
//        					  "VOLUME");
//            for(int i = 0; i < tickList.size(); i++)
//            {
//            	System.out.format("%20.20s;%5.2f;%5.2f;%5.2f;%5.2f;%9d\n",
//  					  df.format(tickList.get(i).timestamp),
//  					  tickList.get(i).open,
//					  tickList.get(i).high,
//					  tickList.get(i).low,
//					  tickList.get(i).close,
//					  tickList.get(i).tradedVolume);
//            }
        }
          
        catch(IOException | ClassNotFoundException ex)
        {
        	ex.printStackTrace();
        }    
        
        return tickList;
	}
}
