package edu.luc.etl.cs313.android.simplestopwatch.test.timer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import edu.luc.etl.cs313.android.simplestopwatch.R;
import edu.luc.etl.cs313.android.simplestopwatch.model.state.StopwatchStateMachine;

/**
 * Abstract test for timer state machine.
 * Based on stopwatch test pattern but adapted for timer behavior.
 */
public abstract class AbstractTimerStateMachineTest {

    private StopwatchStateMachine model;
    private UnifiedMockDependency dependency;

    @Before
    public void setUp() throws Exception {
        dependency = new UnifiedMockDependency();
    }

    @After
    public void tearDown() {
        dependency = null;
    }

    protected void setModel(final StopwatchStateMachine model) {
        this.model = model;
        if (model == null)
            return;
        this.model.setModelListener(dependency);
        this.model.actionInit();
    }

    protected UnifiedMockDependency getDependency() {
        return dependency;
    }

    /**
     * Verifies initial state is STOPPED with time = 0
     */
    @Test
    public void testPreconditions() {
        assertEquals(R.string.STOPPED, dependency.getState());
        assertTimeEquals(0);
    }

    /**
     * Test: Button press in stopped state increments to 1
     */
    @Test
    public void testButtonPressTransitionsToIncrementingState() {
        assertTimeEquals(0);
        assertEquals(R.string.STOPPED, dependency.getState());

        model.onStartStop();

        assertEquals(R.string.INCREMENTING, dependency.getState());
        assertTimeEquals(1);
    }

    /**
     * Test: 3 ticks in incrementing state transitions to running
     */
    @Test
    public void testThreeSecondTimeoutTransitionsToRunning() {
        // Increment to 5
        model.onStartStop();
        model.onStartStop();
        model.onStartStop();
        model.onStartStop();
        model.onStartStop();

        assertTimeEquals(5);
        assertEquals(R.string.INCREMENTING, dependency.getState());
        assertFalse(dependency.isStarted());

        // Wait 3 ticks (3 seconds)
        onTickRepeat(3);

        assertEquals(R.string.RUNNING, dependency.getState());
        assertTrue(dependency.isStarted());
        assertTimeEquals(5);
    }

    /**
     * Test: Reaching 99 immediately starts timer
     */
    @Test
    public void testIncrementingStopsAt99() {
        // Increment to 98
        for (int i = 0; i < 98; i++) {
            model.onStartStop();
        }
        assertTimeEquals(98);
        assertEquals(R.string.INCREMENTING, dependency.getState());

        // Press one more time - should go to 99 and start
        model.onStartStop();

        assertEquals(R.string.RUNNING, dependency.getState());
        assertTimeEquals(99);
        assertTrue(dependency.isStarted());
    }

    /**
     * Test: Running state counts down each tick
     */
    @Test
    public void testTimeElapsedDecrementsTime() {
        // Set time to 5 and start
        for (int i = 0; i < 5; i++) {
            model.onStartStop();
        }
        onTickRepeat(3); // Auto-start after 3 seconds

        assertTimeEquals(5);
        assertEquals(R.string.RUNNING, dependency.getState());

        // One tick - should decrement to 4
        model.onTick();
        assertTimeEquals(4);

        // Another tick - should decrement to 3
        model.onTick();
        assertTimeEquals(3);
    }

    /**
     * Test: Time reaching zero transitions to alarm
     */
    @Test
    public void testTimeReachingZeroTransitionsToAlarm() {
        // Set time to 2 and start
        model.onStartStop();
        model.onStartStop();
        onTickRepeat(3); // Auto-start

        assertTimeEquals(2);
        assertEquals(R.string.RUNNING, dependency.getState());

        // Countdown: 2 → 1 → 0 → ALARM
        model.onTick(); // 2 → 1
        assertTimeEquals(1);

        model.onTick(); // 1 → 0, should go to ALARM
        assertEquals(R.string.ALARM, dependency.getState());
        assertTimeEquals(0);
        assertFalse(dependency.isStarted()); // Clock should stop
    }

    /**
     * Test: Button press during running cancels timer
     */
    @Test
    public void testButtonPressDuringRunningTransitionsToStopped() {
        // Set time to 5 and start
        for (int i = 0; i < 5; i++) {
            model.onStartStop();
        }
        onTickRepeat(3);

        assertEquals(R.string.RUNNING, dependency.getState());
        assertTimeEquals(5);

        // Press button - should cancel
        model.onStartStop();

        assertEquals(R.string.STOPPED, dependency.getState());
        assertTimeEquals(0);
        assertFalse(dependency.isStarted());
    }

    /**
     * Test: Button press in alarm stops alarm
     */
    @Test
    public void testButtonPressInAlarmTransitionsToStopped() {
        // Set time to 1, start, and let it reach alarm
        model.onStartStop();
        onTickRepeat(3); // Auto-start
        model.onTick(); // Countdown to 0, go to alarm

        assertEquals(R.string.ALARM, dependency.getState());
        assertTimeEquals(0);

        // Press button - should stop alarm
        model.onStartStop();

        assertEquals(R.string.STOPPED, dependency.getState());
        assertTimeEquals(0);
    }

    /**
     * Test: Complete timer flow from start to alarm
     */
    @Test
    public void testCompleteTimerFlowFromStartToAlarm() {
        // Start in stopped
        assertEquals(R.string.STOPPED, dependency.getState());
        assertTimeEquals(0);

        // Increment to 2
        model.onStartStop();
        model.onStartStop();
        assertEquals(R.string.INCREMENTING, dependency.getState());
        assertTimeEquals(2);

        // Wait 3 seconds - auto start
        onTickRepeat(3);
        assertEquals(R.string.RUNNING, dependency.getState());
        assertTrue(dependency.isStarted());

        // Countdown: 2 → 1 → 0
        model.onTick();
        assertTimeEquals(1);
        model.onTick();
        assertEquals(R.string.ALARM, dependency.getState());
        assertTimeEquals(0);

        // Stop alarm
        model.onStartStop();
        assertEquals(R.string.STOPPED, dependency.getState());
        assertTimeEquals(0);
    }

    /**
     * Helper: Simulate n ticks
     */
    protected void onTickRepeat(final int n) {
        for (int i = 0; i < n; i++) {
            model.onTick();
        }
    }

    /**
     * Helper: Assert time equals expected value
     */
    protected void assertTimeEquals(final int t) {
        assertEquals(t, dependency.getTime());
    }
}