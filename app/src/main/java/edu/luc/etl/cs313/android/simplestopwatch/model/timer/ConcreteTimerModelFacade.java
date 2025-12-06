package edu.luc.etl.cs313.android.simplestopwatch.model.timer;

import edu.luc.etl.cs313.android.simplestopwatch.common.StopwatchModelListener;
import edu.luc.etl.cs313.android.simplestopwatch.model.clock.ClockModel;
import edu.luc.etl.cs313.android.simplestopwatch.model.clock.DefaultClockModel;
import edu.luc.etl.cs313.android.simplestopwatch.model.time.DefaultTimeModel;
import edu.luc.etl.cs313.android.simplestopwatch.model.time.TimeModel;
import edu.luc.etl.cs313.android.simplestopwatch.model.timer.state.DefaultTimerStateMachine;
import edu.luc.etl.cs313.android.simplestopwatch.model.timer.state.TimerStateMachine;

/**
 * Concrete implementation of TimerModelFacade.
 * Wires together all the timer components.
 */
public class ConcreteTimerModelFacade implements TimerModelFacade {

    private final TimerStateMachine stateMachine;
    private final ClockModel clockModel;
    private final TimeModel timeModel;

    public ConcreteTimerModelFacade() {
        timeModel = new DefaultTimeModel();
        clockModel = new DefaultClockModel();
        stateMachine = new DefaultTimerStateMachine(timeModel, clockModel);
        clockModel.setTickListener(stateMachine);
    }

    @Override
    public void start() {
        stateMachine.actionInit();
    }

    @Override
    public void setModelListener(final StopwatchModelListener listener) {
        stateMachine.setModelListener(listener);
    }

    @Override
    public void onStartStop() {
        stateMachine.onStartStop();
    }

    @Override
    public void onLapReset() {
        // Timer doesn't use lap/reset button
    }
}