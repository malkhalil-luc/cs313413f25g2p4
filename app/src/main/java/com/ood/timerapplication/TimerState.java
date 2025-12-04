package com.ood.timerapplication;

public interface TimerState {
    TimerState onButtonPress();
    TimerState onSecondElapsed();
    int getTime();
}