package edu.luc.etl.cs313.android.simplestopwatch.model.timer.state;
import edu.luc.etl.cs313.android.simplestopwatch.R;

public class RunningState implements TimerState{

    private final TimerSMStateView sm;

    public RunningState( final TimerSMStateView sm) {
        this.sm = sm;
    }

    @Override
    public void updateView() {
        sm.updateUIRuntime();
    }
    @Override
    public int getId() {
        return R.string.RUNNING;
    }

    @Override
    public void onStartStop() {
        sm.actionStop();
        sm.actionReset();
        sm.toStoppedState();

    }

    @Override
    public void onLapReset() { // not needed

    }

    @Override
    public void onTick() {
        sm.actionDec();

        if (sm.getTime() <= 0) {
            sm.actionStop();
            sm.actionStartAlarm();
            sm.toAlarmingState();
        }

    }
}
