package net.wurstclient.forge.utils;

public class STimer {
	private long previousTime;
	private long prevMS;


    public STimer() {
        previousTime = -1L;
        prevMS=0;
    }

    public boolean check(float milliseconds) {
        return getTime() >= milliseconds;
    }

    public long getTime() {
        return getCurrentTime() - previousTime;
    }

    public void reset() {
        previousTime = getCurrentTime();
    }

    public long getCurrentTime() {
        return System.currentTimeMillis();
    }

    public boolean delay(float milliSec) {
        return (float) (getTime() - this.prevMS) >= milliSec;
    }
}
