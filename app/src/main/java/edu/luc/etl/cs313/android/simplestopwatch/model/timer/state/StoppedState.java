package edu.luc.etl.cs313.android.simplestopwatch.model.timer.state;

public class StoppedState implements TimerState{

    private TimerSMStateView sm;
    public StoppedState(TimerSMStateView sm) { this.sm = sm; }

    @Override
    public void updateView() {
        sm.actionUpdateView();
    }

    @Override
    public int getId() { return 0;}

    @Override
    public void onStartStop() {

    }

    @Override
    public void onLapReset() {

    }

    @Override
    public void onTick() { // not needed

    }
}
