package com.ood.timerapplication;

import org.junit.Test;
import static org.junit.Assert.*;

public class TimerIntegrationTest {

    @Test
    public void testCompleteTimerFlowFromStartToAlarm() {
        TestTimeProvider timeProvider = new TestTimeProvider();
        timeProvider.setTime(0);

        TimerState state = new StoppedState(timeProvider);
        assertEquals(0, state.getTime());
        assertTrue(state instanceof StoppedState);


        // Step 1: Press button → Incrementing (time = 1)
        state = state.onButtonPress();
        assertTrue("Should be in Incrementing state", state instanceof IncrementingState);
        assertEquals("Time should be 1", 1, state.getTime());

        // Step 2: Press button again → Incrementing (time = 2)
        state = state.onButtonPress();
        assertTrue("Should still be in Incrementing state", state instanceof IncrementingState);
        assertEquals("Time should be 2", 2, state.getTime());

        // Step 3: Wait 3 seconds → Running (time = 2)
        timeProvider.setTime(3000);
        state = state.onSecondElapsed();
        assertTrue("Should transition to Running after 3 seconds", state instanceof RunningState);
        assertEquals("Time should still be 2", 2, state.getTime());

        // Step 4: One second passes → Running (time = 1)
        state = state.onSecondElapsed();
        assertTrue("Should stay in Running", state instanceof RunningState);
        assertEquals("Time should be 1", 1, state.getTime());

        // Step 5: Another second passes → Alarm (time = 0)
        state = state.onSecondElapsed();
        assertTrue("Should transition to Alarm when time reaches 0", state instanceof AlarmState);
        assertEquals("Time should be 0", 0, state.getTime());

        // Step 6: Press button → Stopped (time = 0)
        state = state.onButtonPress();
        assertTrue("Should return to Stopped", state instanceof StoppedState);
        assertEquals("Time should be 0", 0, state.getTime());
    }
    @Test
    public void testCancelTimerDuringCountdown() {
        TestTimeProvider timeProvider = new TestTimeProvider();
        timeProvider.setTime(0);

        TimerState state = new StoppedState(timeProvider);

        state = state.onButtonPress(); // 1
        state = state.onButtonPress(); // 2
        state = state.onButtonPress(); // 3
        state = state.onButtonPress(); // 4
        state = state.onButtonPress(); // 5
        assertEquals(5, state.getTime());

        timeProvider.setTime(3000);
        state = state.onSecondElapsed();
        assertTrue(state instanceof RunningState);
        assertEquals(5, state.getTime());

        state = state.onSecondElapsed(); // 4
        state = state.onSecondElapsed(); // 3
        assertEquals(3, state.getTime());

        state = state.onButtonPress();

        assertTrue(state instanceof StoppedState);
        assertEquals(0, state.getTime());
    }

    @Test
    public void testReaching99ImmediatelyStartsTimer() {
        TestTimeProvider timeProvider = new TestTimeProvider();
        timeProvider.setTime(0);

        TimerState state = new StoppedState(timeProvider);

        for (int i = 0; i < 98; i++) {
            state = state.onButtonPress();
        }
        assertEquals(98, state.getTime());
        assertTrue(state instanceof IncrementingState);

        state = state.onButtonPress();

        assertTrue("Should transition to Running at 99", state instanceof RunningState);
        assertEquals(99, state.getTime());
    }
}