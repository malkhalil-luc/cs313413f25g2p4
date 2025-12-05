package edu.luc.etl.cs313.android.simplestopwatch.model.timer.state;

import edu.luc.etl.cs313.android.simplestopwatch.common.StopwatchModelListener;
import edu.luc.etl.cs313.android.simplestopwatch.model.clock.ClockModel;
import edu.luc.etl.cs313.android.simplestopwatch.model.time.TimeModel;

public class DefaultTimerStateMachine implements TimerStateMachine {

    public DefaultTimerStateMachine(final TimeModel timeModel, final ClockModel clockModel) {
        this.timeModel = timeModel;
        this.clockModel = clockModel;
    }

    private final TimeModel timeModel;
    private final ClockModel clockModel;
    private TimerState state;
    private StopwatchModelListener listener;

    protected void setState(final TimerState state) {
        this.state = state;
        listener.onStateUpdate(state.getId());
    }

    @Override
    public void setModelListener(final StopwatchModelListener listener) {
        this.listener = listener;
    }

    @Override public synchronized void onStartStop() { state.onStartStop(); }
    @Override public synchronized void onLapReset()  { state.onLapReset(); }
    @Override public synchronized void onTick()      { state.onTick(); }

    @Override public void updateUIRuntime() {
        listener.onTimeUpdate(timeModel.getRuntime());
    }

    @Override
    public void updateUILaptime() {

    }

    private final TimerState STOPPED = new StoppedState(this);
    private TimerState INCREMENTING = new IncrementingState(this);
    private final TimerState RUNNING = new RunningState(this);
    private final TimerState ALARMING = new AlarmingState(this);

    @Override public void toRunningState()      { setState(RUNNING); }
    @Override public void toStoppedState()      { setState(STOPPED); }

    @Override
    public void toLapRunningState() {

    }

    @Override
    public void toLapStoppedState() {

    }

    @Override public void toIncrementingState() {
        INCREMENTING = new IncrementingState(this);  // Create new to reset tick counter
        setState(INCREMENTING);
    }
    @Override public void toAlarmingState()     { setState(ALARMING); }

    @Override public void actionInit()       { toStoppedState(); actionReset(); }
    @Override public void actionReset()      { timeModel.resetRuntime(); actionUpdateView(); }
    @Override public void actionStart()      { clockModel.start(); }
    @Override public void actionStop()       { clockModel.stop(); }

    @Override
    public void actionLap() {

    }

    @Override public void actionInc()        { timeModel.incRuntime(); actionUpdateView(); }
    @Override public void actionDec()
    {
        int currentTime = timeModel.getRuntime();
        if (currentTime > 0) {
            timeModel.resetRuntime();
            for (int i = 0; i < currentTime - 1; i++) {
                timeModel.incRuntime();
            }
        }
        actionUpdateView();
    }
    @Override public void actionUpdateView() { state.updateView(); }

    @Override public void actionPlayBeep() {

    }

    @Override public void actionStartAlarm() {

    }

    @Override public void actionStopAlarm() {

    }

    @Override public int getTime() {
        return timeModel.getRuntime();
    }
}