package javaIndicatorsTest;

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

import org.junit.Ignore;
import org.junit.Test;

import Tick.Tick;

public class DataLoader {
	
	@Ignore
	@Test
	public void readData() {
		BufferedReader reader;
		List<Tick> tickList = new ArrayList<Tick>();
		String[] split;
		Tick tick;
		try {
			Date start = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").parse("2022/10/01 09:00:03");
			reader = new BufferedReader(new FileReader("/tmp/HistorycalData.csv"));
			String line = reader.readLine();
			while (line != null) {
				split = line.split(";");
				tick = new Tick();
				tick.open = Double.parseDouble(split[1]);
				tick.high = Double.parseDouble(split[2]);
				tick.low = Double.parseDouble(split[3]);
				tick.close = Double.parseDouble(split[4]);
				tick.tradedVolume = Long.parseLong(split[5]);
				tick.timestamp = new Date(start.getTime() + 150);
				start = tick.timestamp;
				tickList.add(tick);
				// read next line
				line = reader.readLine();
			}
			reader.close();
            FileOutputStream fos = new FileOutputStream("docs/listData");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(tickList);
            oos.close();
            fos.close();

		} catch (IOException | ParseException e) {
			e.printStackTrace();
		}
	}

	@Ignore
	@SuppressWarnings("unchecked")
	@Test
	public void useSavedData() {
        List<Tick> tickList = new ArrayList<Tick>();
        String filename = "docs/listData";
          
        try
        {   
            // Reading the object from a file
            FileInputStream file = new FileInputStream(filename);
            ObjectInputStream in = new ObjectInputStream(file);
              
            // Method for deserialization of object
            tickList = (ArrayList<Tick>)in.readObject();
              
            in.close();
            file.close();
            SimpleDateFormat df = new SimpleDateFormat("yy/MM/dd HH:mm:ss:S");
            for(int i = 0; i < tickList.size(); i++)
            {
            	System.out.format("%s - %5.2f %5.2f %5.2f %5.2f %09d\n",
  					  df.format(tickList.get(i).timestamp),
  					  tickList.get(i).open,
					  tickList.get(i).high,
					  tickList.get(i).low,
					  tickList.get(i).close,
					  tickList.get(i).tradedVolume);
            }
        }
          
        catch(IOException ex)
        {
            System.out.println("IOException is caught");
        }
          
        catch(ClassNotFoundException ex)
        {
            System.out.println("ClassNotFoundException is caught");
        }         
	}
}
