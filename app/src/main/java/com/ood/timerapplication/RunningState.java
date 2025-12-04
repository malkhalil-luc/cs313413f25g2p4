package com.ood.timerapplication;

public class RunningState implements TimerState {

    private int time;
    private TimeProvider timeProvider;

    public RunningState(int time, TimeProvider timeProvider) {
        this.time = time;
        this.timeProvider = timeProvider;
    }

    public RunningState(int time) {
        this(time, null);
    }

    @Override
    public TimerState onButtonPress() {
        return new StoppedState(timeProvider);
    }

    @Override
    public TimerState onSecondElapsed() {
        int newTime = time - 1;

        if (newTime <= 0) {
            return new AlarmState(timeProvider);
        }

        return new RunningState(newTime, timeProvider);
    }

    @Override
    public int getTime() {
        return time;
    }
}