package misc;

import java.util.Date;

public class Timer {
	
	private static Date date = new Date ();
	private long begin;
	
	public Timer () {
		this.begin = date.getTime();
	}
	
	/**
	 * @return The time in milliseconds since this Timer object was created
	 */
	public long timeInSeconds () {
		return (this.date.getTime() - this.begin) / 1000;
	}
	
}
