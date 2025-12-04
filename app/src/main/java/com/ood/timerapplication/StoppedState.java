package com.ood.timerapplication;

public class StoppedState implements TimerState {

    private TimeProvider timeProvider;

    public StoppedState(TimeProvider timeProvider) {
        this.timeProvider = timeProvider;
    }

    @Override
    public TimerState onButtonPress() {
        return new IncrementingState(1, timeProvider);
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