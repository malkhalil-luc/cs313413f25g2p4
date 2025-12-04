package com.ood.timerapplication;

public class IncrementingState implements TimerState {

    private int time;
    private TimeProvider timeProvider;
    private long startTime;

    public IncrementingState(int time, TimeProvider timeProvider) {
        this.time = time;
        this.timeProvider = timeProvider;
        this.startTime = timeProvider.getTime();
    }

    @Override
    public TimerState onButtonPress() {
        int newTime = time + 1;

        if (newTime >= 99) {
            return new RunningState(99, timeProvider);
        }

        return new IncrementingState(newTime, timeProvider);
    }

    @Override
    public TimerState onSecondElapsed() {
        return checkTimeout();
    }

    @Override
    public int getTime() {
        return time;
    }

    public TimerState checkTimeout() {
        long elapsedTime = timeProvider.getTime() - startTime;

        if (elapsedTime >= 3000) {
            return new RunningState(time, timeProvider);
        }

        return this;
    }
}