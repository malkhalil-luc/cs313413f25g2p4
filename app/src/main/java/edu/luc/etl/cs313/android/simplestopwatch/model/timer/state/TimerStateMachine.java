package edu.luc.etl.cs313.android.simplestopwatch.model.timer.state;

import edu.luc.etl.cs313.android.simplestopwatch.common.StopwatchModelSource;
import edu.luc.etl.cs313.android.simplestopwatch.common.StopwatchUIListener;
import edu.luc.etl.cs313.android.simplestopwatch.model.clock.TickListener;

public interface TimerStateMachine extends StopwatchUIListener, TickListener, StopwatchModelSource, TimerSMStateView { }