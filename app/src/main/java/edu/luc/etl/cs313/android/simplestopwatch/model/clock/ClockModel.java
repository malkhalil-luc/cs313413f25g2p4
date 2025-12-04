package edu.luc.etl.cs313.android.simplestopwatch.model.clock;

import edu.luc.etl.cs313.android.simplestopwatch.common.Startable;
import edu.luc.etl.cs313.android.simplestopwatch.common.Stoppable;

/**
 * The active model of the internal clock that periodically emits tick events.
 *
 * @author laufer
 */
public interface ClockModel extends Startable, Stoppable, TickSource { }
