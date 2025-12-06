package edu.luc.etl.cs313.android.simplestopwatch.model.timer.state;
import edu.luc.etl.cs313.android.simplestopwatch.R;

public class StoppedState implements TimerState{

    private final TimerSMStateView sm;
    public StoppedState(final TimerSMStateView sm) { this.sm = sm; }

    @Override
    public void updateView() {
        sm.updateUIRuntime();
    }

    @Override
    public int getId() { return R.string.STOPPED;}

    @Override
    public void onStartStop() {
        sm.actionInc();
        //sm.actionStart();
        sm.toIncrementingState();

    }

    @Override
    public void onLapReset() { // not needed

    }

    @Override
    public void onTick() { // not needed

    }
}
