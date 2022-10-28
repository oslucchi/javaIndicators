package Tick;
import java.util.Date;

public class Tick {
	public Date timestamp;
	public double open;
	public double close;
	public double high;
	public double low;
	public long tradedVolume;
	public double trueRange;
	
	public Tick() {
		open = 0.;
		close = 0.;
		high = 0.;
		low = 0.;
		trueRange = 0.;
		
		tradedVolume = (long) 0;
		timestamp = new Date();
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	public double getOpen() {
		return open;
	}

	public void setOpen(double open) {
		this.open = open;
	}

	public double getClose() {
		return close;
	}

	public void setClose(double close) {
		this.close = close;
	}

	public double getHigh() {
		return high;
	}

	public void setHigh(double high) {
		this.high = high;
	}

	public double getLow() {
		return low;
	}

	public void setLow(double low) {
		this.low = low;
	}

	public long getTradedVolume() {
		return tradedVolume;
	}

	public void setTradedVolume(long tradedVolume) {
		this.tradedVolume = tradedVolume;
	}

	public double getTrueRange() {
		return trueRange;
	}

	public void setTrueRange(double trueRange) {
		this.trueRange = trueRange;
	}
	
	
}
