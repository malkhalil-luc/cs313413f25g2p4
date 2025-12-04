package edu.luc.etl.cs313.android.simplestopwatch.model.state;

import edu.luc.etl.cs313.android.simplestopwatch.R;

class LapRunningState implements StopwatchState {

    public LapRunningState(final StopwatchSMStateView sm) {
        this.sm = sm;
    }

    private final StopwatchSMStateView sm;

    @Override
    public void onStartStop() {
        sm.actionStop();
        sm.toLapStoppedState();
    }

    @Override
    public void onLapReset() {
        sm.toRunningState();
        sm.actionUpdateView();
    }

    @Override
    public void onTick() {
        sm.actionInc();
        sm.toLapRunningState();
    }

    @Override
    public void updateView() {
        sm.updateUILaptime();
    }

    @Override
    public int getId() {
        return R.string.LAP_RUNNING;
    }
}
