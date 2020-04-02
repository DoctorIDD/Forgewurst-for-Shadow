package net.wurstclient.forge.utils;

import net.minecraft.util.Timer;

public class TimerUtil extends Timer{
	public static Timer timer;
	public TimerUtil(float bm_25632) {
		super(bm_25632);
		timer=this;
	}
	public static Timer getTimer() {
		return timer;
	}
	private long lastMS = 0L;
	private long prevMS = 0L;
	
	public boolean isDelay(long delay) {
		if(System.currentTimeMillis() - lastMS >= delay) {
			return true;
		}
		return false;
	}
	
    public long getCurrentMS(){
		return System.nanoTime() / 1000000L;
	}
	
    public void setLastMS(long lastMS) {
		this.lastMS = lastMS;
	}
	
    public void setLastMS() {
		this.lastMS = System.currentTimeMillis();
	}
	
    public int convertToMS(int d) {
		return 1000 /d;
	}
	
    public boolean hasReached(float f){
		return (float) (getCurrentMS() - this.lastMS) >= f; 
	}

    public void reset(){
		this.lastMS = getCurrentMS();
	}
	
    public boolean delay(float milliSec){
		return (float)(getTime() - this.prevMS) >= milliSec;
	}
	
    public long getTime(){
		return System.nanoTime() / 1000000L;
	}
}
