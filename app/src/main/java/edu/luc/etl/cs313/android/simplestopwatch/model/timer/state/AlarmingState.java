package edu.luc.etl.cs313.android.simplestopwatch.model.timer.state;

import edu.luc.etl.cs313.android.simplestopwatch.R;

public class AlarmingState implements TimerState {

    private final TimerSMStateView sm;

    public AlarmingState(TimerSMStateView sm) {
        this.sm = sm;
    }

    @Override
    public void updateView() {
        sm.updateUIRuntime();
    }

    @Override
    public int getId() {
        return R.string.ALARM;
    }

    @Override
    public void onTick() { } // not needed

    @Override
    public void onStartStop() {
        sm.actionStopAlarm();
        sm.actionReset();
        sm.toStoppedState();
    }

    @Override
    public void onLapReset() { } // not needed
}
