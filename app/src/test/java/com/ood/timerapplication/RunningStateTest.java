package com.ood.timerapplication;

import org.junit.Test;
import static org.junit.Assert.*;

public class RunningStateTest {

    @Test
    public void testTimeElapsedDecrementsTime() {
        RunningState state = new RunningState(5);

        TimerState newState = state.onSecondElapsed();

        assertTrue(newState instanceof RunningState);
        assertEquals(4, newState.getTime());
    }

    @Test
    public void testMultipleSecondsDecrement() {
        RunningState state = new RunningState(5);

        TimerState state2 = state.onSecondElapsed();  // 5 → 4
        TimerState state3 = state2.onSecondElapsed(); // 4 → 3
        TimerState state4 = state3.onSecondElapsed(); // 3 → 2

        assertTrue(state4 instanceof RunningState);
        assertEquals(2, state4.getTime());
    }

    @Test
    public void testTimeReachingZeroTransitionsToAlarm() {
        RunningState state = new RunningState(1);

        TimerState newState = state.onSecondElapsed();

        assertTrue(newState instanceof AlarmState);
        assertEquals(0, newState.getTime());
    }

    @Test
    public void testCompleteCountdownToAlarm() {
        RunningState state = new RunningState(3);

        TimerState state2 = state.onSecondElapsed();   // 3 → 2
        TimerState state3 = state2.onSecondElapsed();  // 2 → 1
        TimerState state4 = state3.onSecondElapsed();  // 1 → 0 (alarm)

        assertTrue(state2 instanceof RunningState);
        assertEquals(2, state2.getTime());

        assertTrue(state3 instanceof RunningState);
        assertEquals(1, state3.getTime());

        assertTrue(state4 instanceof AlarmState);
        assertEquals(0, state4.getTime());
    }

    @Test
    public void testButtonPressDuringRunningTransitionsToStopped() {
        TestTimeProvider timeProvider = new TestTimeProvider();
        RunningState state = new RunningState(5, timeProvider);

        TimerState newState = state.onButtonPress();

        assertTrue(newState instanceof StoppedState);
        assertEquals(0, newState.getTime());
    }
}