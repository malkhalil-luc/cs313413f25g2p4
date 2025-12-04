package com.ood.timerapplication;

public class AlarmState implements TimerState {

    private TimeProvider timeProvider;

    public AlarmState(TimeProvider timeProvider) {
        this.timeProvider = timeProvider;
    }


    public AlarmState() {
        this(null);
    }

    @Override
    public TimerState onButtonPress() {
        return new StoppedState(timeProvider);
    }

    @Override
    public TimerState onSecondElapsed() {

        return this;
    }

    @Override
    public int getTime() {
        return 0;
    }
}