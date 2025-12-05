package edu.luc.etl.cs313.android.simplestopwatch.model.timer;

import edu.luc.etl.cs313.android.simplestopwatch.common.Startable;
import edu.luc.etl.cs313.android.simplestopwatch.common.StopwatchModelSource;
import edu.luc.etl.cs313.android.simplestopwatch.common.StopwatchUIListener;

/**
 * Timer model facade interface.
 */
public interface TimerModelFacade extends Startable, StopwatchUIListener, StopwatchModelSource { }