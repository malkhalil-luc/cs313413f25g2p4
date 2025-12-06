package edu.luc.etl.cs313.android.simplestopwatch.test.timer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import edu.luc.etl.cs313.android.simplestopwatch.R;
import edu.luc.etl.cs313.android.simplestopwatch.model.timer.state.TimerStateMachine;

public abstract class AbstractTimerStateMachineTest {


    private TimerStateMachine model;
    private UnifiedMockDependency dependency;

    @Before
    public void setUp() throws Exception {
        dependency = new UnifiedMockDependency();
    }

    @After
    public void tearDown() {
        dependency = null;
    }

    protected void setModel(final TimerStateMachine model) {
        this.model = model;
        if (model == null)
            return;
        this.model.setModelListener(dependency);
        this.model.actionInit();
    }

    protected UnifiedMockDependency getDependency() {
        return dependency;
    }


    @Test
    public void testPreconditions() {
        assertEquals(R.string.STOPPED, dependency.getState());
        assertTimeEquals(0);
    }


    @Test
    public void testButtonPressTransitionsToIncrementingState() {
        assertTimeEquals(0);
        assertEquals(R.string.STOPPED, dependency.getState());

        model.onStartStop();

        assertEquals(R.string.INCREMENTING, dependency.getState());
        assertTimeEquals(1);
    }


    @Test
    public void testThreeSecondTimeoutTransitionsToRunning() {
        model.onStartStop();
        model.onStartStop();
        model.onStartStop();
        model.onStartStop();
        model.onStartStop();

        assertTimeEquals(5);
        assertEquals(R.string.INCREMENTING, dependency.getState());
        //assertFalse(dependency.isStarted());
        assertTrue(dependency.isStarted());
        onTickRepeat(3);

        assertEquals(R.string.RUNNING, dependency.getState());
        assertTrue(dependency.isStarted());
        assertTimeEquals(5);
    }


    @Test
    public void testIncrementingStopsAt99() {
        for (int i = 0; i < 98; i++) {
            model.onStartStop();
        }
        assertTimeEquals(98);
        assertEquals(R.string.INCREMENTING, dependency.getState());

        model.onStartStop();

        assertEquals(R.string.RUNNING, dependency.getState());
        assertTimeEquals(99);
        assertTrue(dependency.isStarted());
    }


    @Test
    public void testTimeElapsedDecrementsTime() {
        for (int i = 0; i < 5; i++) {
            model.onStartStop();
        }
        onTickRepeat(3);

        assertTimeEquals(5);
        assertEquals(R.string.RUNNING, dependency.getState());

        model.onTick();
        assertTimeEquals(4);

        model.onTick();
        assertTimeEquals(3);
    }


    @Test
    public void testTimeReachingZeroTransitionsToAlarm() {
        model.onStartStop();
        model.onStartStop();
        onTickRepeat(3);

        assertTimeEquals(2);
        assertEquals(R.string.RUNNING, dependency.getState());

        model.onTick();
        assertTimeEquals(1);

        model.onTick();
        assertEquals(R.string.ALARM, dependency.getState());
        assertTimeEquals(0);
        assertFalse(dependency.isStarted());
    }


    @Test
    public void testButtonPressDuringRunningTransitionsToStopped() {

        for (int i = 0; i < 5; i++) {
            model.onStartStop();
        }
        onTickRepeat(3);

        assertEquals(R.string.RUNNING, dependency.getState());
        assertTimeEquals(5);


        model.onStartStop();

        assertEquals(R.string.STOPPED, dependency.getState());
        assertTimeEquals(0);
        assertFalse(dependency.isStarted());
    }


    @Test
    public void testButtonPressInAlarmTransitionsToStopped() {

        model.onStartStop();
        onTickRepeat(3);
        model.onTick();

        assertEquals(R.string.ALARM, dependency.getState());
        assertTimeEquals(0);


        model.onStartStop();

        assertEquals(R.string.STOPPED, dependency.getState());
        assertTimeEquals(0);
    }


    @Test
    public void testCompleteTimerFlowFromStartToAlarm() {
        assertEquals(R.string.STOPPED, dependency.getState());
        assertTimeEquals(0);

        model.onStartStop();
        model.onStartStop();
        assertEquals(R.string.INCREMENTING, dependency.getState());
        assertTimeEquals(2);

        onTickRepeat(3);
        assertEquals(R.string.RUNNING, dependency.getState());
        assertTrue(dependency.isStarted());

        model.onTick();
        assertTimeEquals(1);
        model.onTick();
        assertEquals(R.string.ALARM, dependency.getState());
        assertTimeEquals(0);

        model.onStartStop();
        assertEquals(R.string.STOPPED, dependency.getState());
        assertTimeEquals(0);
    }

    /// /////////////
    protected void onTickRepeat(final int n) {
        for (int i = 0; i < n; i++) {
            model.onTick();
        }
    }
    protected void assertTimeEquals(final int t) {
        assertEquals(t, dependency.getTime());
    }
}