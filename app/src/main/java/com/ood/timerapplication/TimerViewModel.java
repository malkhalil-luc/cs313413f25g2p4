package com.ood.timerapplication;

import java.util.Timer;
import java.util.TimerTask;

public class TimerViewModel {

    private TimeProvider timeProvider;
    private TimerState currentState;
    private Timer timer;

    public TimerViewModel(TimeProvider timeProvider) {
        this.timeProvider = timeProvider;
        this.currentState = new StoppedState(timeProvider);
    }

    public void onButtonPressed() {
        currentState = currentState.onButtonPress();

        if (currentState instanceof IncrementingState) {
            startTimeoutChecker();
        }

        if (currentState instanceof StoppedState && timer != null) {
            stopTimer();
        }
    }

    public int getCurrentTime() {
        return currentState.getTime();
    }

    public boolean isRunning() {
        return currentState instanceof RunningState;
    }

    // ← ADD THIS METHOD FOR TESTING
    public void triggerTimeoutCheck() {
        checkTimeout();
    }

    private void startTimeoutChecker() {
        if (timer != null) {
            timer.cancel();
        }

        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                checkTimeout();
            }
        }, 100, 100);
    }

    private void checkTimeout() {
        if (currentState instanceof IncrementingState) {
            currentState = currentState.onSecondElapsed();

            if (currentState instanceof RunningState) {
                startCountdown();
            }
        }
    }

    private void startCountdown() {
        if (timer != null) {
            timer.cancel();
        }

        timer = new Timer();
        scheduleNextTick();
    }

    private void scheduleNextTick() {
        if (timer != null) {
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    currentState = currentState.onSecondElapsed();

                    if (currentState instanceof RunningState) {
                        scheduleNextTick();
                    } else {
                        stopTimer();
                    }
                }
            }, 1000);
        }
    }

    private void stopTimer() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    public void cleanup() {
        stopTimer();
    }
}