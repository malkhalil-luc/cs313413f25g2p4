package edu.luc.etl.cs313.android.simplestopwatch.model.timer.state;

import edu.luc.etl.cs313.android.simplestopwatch.common.StopwatchUIListener;
import edu.luc.etl.cs313.android.simplestopwatch.model.clock.TickListener;

interface TimerState extends StopwatchUIListener, TickListener {
    void updateView();
    int getId();
}