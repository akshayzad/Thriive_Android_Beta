package com.thriive.app.utilities;

import java.util.Timer;
import java.util.TimerTask;

public abstract class PreciseCountdown extends Timer {
    private long totalTime, interval, delay;
    private TimerTask task;
    private long startTime = -1;
    private boolean restart = false, wasCancelled = false, wasStarted = false;

    public PreciseCountdown(long totalTime, long interval) {
        this(totalTime, interval, 0);
    }

    public PreciseCountdown(long totalTime, long interval, long delay) {
        super("PreciseCountdown", true);
        this.delay = delay;
        this.interval = interval;
        this.totalTime = totalTime;
        this.task = getTask(totalTime);
    }

    public void start() {
        wasStarted = true;
        this.scheduleAtFixedRate(task, delay, interval);
    }

    public void restart() {
        if(!wasStarted) {
            start();
        }
        else if(wasCancelled) {
            wasCancelled = false;
            this.task = getTask(totalTime);
            start();
        }
        else{
            this.restart = true;
        }
    }

    public void stop() {
        this.wasCancelled = true;
        this.task.cancel();
    }

    // Call this when there's no further use for this timer
    public void dispose(){
        cancel();
        purge();
    }

    private TimerTask getTask(final long totalTime) {
        return new TimerTask() {

            @Override
            public void run() {
                long timeLeft;
                if (startTime < 0 || restart) {
                    startTime = scheduledExecutionTime();
                    timeLeft = totalTime;
                    restart = false;
                } else {
                    timeLeft = totalTime - (scheduledExecutionTime() - startTime);

                    if (timeLeft <= 0) {
                        this.cancel();
                        startTime = -1;
                        onFinished();
                        return;
                    }
                }

                onTick(timeLeft);
            }
        };
    }

    public abstract void onTick(long timeLeft);
    public abstract void onFinished();
}
//    Usage example would be:
//
//        this.countDown = new PreciseCountdown(totalTime, interval, delay) {
//@Override
//public void onTick(long timeLeft) {
//        // update..
//        // note that this runs on a different thread, so to update any GUI components you need to use Activity.runOnUiThread()
//        }
//
//@Override
//public void onFinished() {
//        onTick(0); // when the timer finishes onTick isn't called
//        // count down is finished
//        }
//        };