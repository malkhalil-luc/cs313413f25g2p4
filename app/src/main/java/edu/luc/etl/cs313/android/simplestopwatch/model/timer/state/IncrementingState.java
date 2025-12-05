package edu.luc.etl.cs313.android.simplestopwatch.model.timer.state;

public class IncrementingState  implements TimerState{
    private TimerSMStateView sm;

    public IncrementingState(TimerSMStateView sm) {
        this.sm = sm;
    }
    @Override
    public void updateView() { }
    @Override
    public int getId() { return 2; }


}
