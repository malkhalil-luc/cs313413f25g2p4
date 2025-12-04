package edu.luc.etl.cs313.android.simplestopwatch.test.timer;

import edu.luc.etl.cs313.android.simplestopwatch.common.StopwatchModelListener;
import edu.luc.etl.cs313.android.simplestopwatch.model.clock.ClockModel;
import edu.luc.etl.cs313.android.simplestopwatch.model.clock.TickListener;
import edu.luc.etl.cs313.android.simplestopwatch.model.time.TimeModel;

class UnifiedMockDependency implements TimeModel, ClockModel, StopwatchModelListener {

    private int timeValue = -1, stateId = -1;
    private int runningTime = 0;
    private boolean started = false;
    private boolean beepPlayed = false;
    private boolean alarmSounding = false;

    public int getTime() {
        return timeValue;
    }

    public int getState() {
        return stateId;
    }

    public boolean isStarted() {
        return started;
    }

    public boolean isBeepPlayed() {
        return beepPlayed;
    }

    public boolean isAlarmSounding() {
        return alarmSounding;
    }

    public void resetBeep() {
        beepPlayed = false;
    }

    public void resetAlarm() {
        alarmSounding = false;
    }

    @Override
    public void onTimeUpdate(final int timeValue) {
        this.timeValue = timeValue;
    }

    @Override
    public void onStateUpdate(final int stateId) {
        this.stateId = stateId;
    }

    @Override
    public void setTickListener(TickListener listener) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void start() {
        started = true;
    }

    @Override
    public void stop() {
        started = false;
    }

    @Override
    public void resetRuntime() {
        runningTime = 0;
    }

    @Override
    public void incRuntime() {
        if (runningTime < 99) {
            runningTime++;
        }
    }

    public void decRuntime() {
        if (runningTime > 0) {
            runningTime--;
        }
    }

    @Override
    public int getRuntime() {
        return runningTime;
    }

    public void setRuntime(int time) {
        this.runningTime = time;
    }

    public void playBeep() {
        beepPlayed = true;
    }

    public void startAlarm() {
        alarmSounding = true;
    }

    public void stopAlarm() {
        alarmSounding = false;
    }

    @Override
    public void setLaptime() { // not used by the timer

    }

    @Override
    public int getLaptime() {
        return -1;
    }
}