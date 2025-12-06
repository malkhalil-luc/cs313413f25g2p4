package edu.luc.etl.cs313.android.simplestopwatch.model.timer.state;
import edu.luc.etl.cs313.android.simplestopwatch.R;

public class IncrementingState implements TimerState {
    private final TimerSMStateView sm;
    private int ticksSinceStart;
    private static final int TIMEOUT_TICKS = 3;
    private static final int MAX_TIME = 99;

    public IncrementingState(final TimerSMStateView sm) {
        this.sm = sm;
        this.ticksSinceStart = 0;
    }

    @Override
    public void updateView() {
        sm.updateUIRuntime();
    }

    @Override
    public int getId() {
        return R.string.INCREMENTING;
    }

    @Override
    public void onStartStop() {
        // Reset the timeout counter since user pressed button
        ticksSinceStart = 0;

        //sm.actionInc();

        int currentTime = sm.getTime();

        // prevent incrementing if already at 99
        if (currentTime < MAX_TIME) {
            sm.actionInc();
            currentTime = sm.getTime();
        }
        if (currentTime >= MAX_TIME) {
            // Reached max time, play beep and transition to running
            sm.actionStop();
            sm.actionPlayBeep();
            sm.actionStart();
            sm.toRunningState();
        } else {
            // Just update the view, don't recreate state
            sm.actionUpdateView();
        }
    }

    @Override
    public void onLapReset() {
        // not needed
    }

    @Override
    public void onTick() {
        ticksSinceStart++;

        if (ticksSinceStart >= TIMEOUT_TICKS) {
            // 3 seconds elapsed without button press, transition to running
            sm.actionPlayBeep();
            sm.actionStart();
            sm.toRunningState();
        }
    }
}