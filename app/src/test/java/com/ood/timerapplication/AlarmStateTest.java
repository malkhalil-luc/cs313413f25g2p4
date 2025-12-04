package com.ood.timerapplication;

import org.junit.Test;
import static org.junit.Assert.*;

public class AlarmStateTest {

    @Test
    public void testButtonPressInAlarmTransitionsToStopped() {

        TestTimeProvider timeProvider = new TestTimeProvider();
        AlarmState state = new AlarmState(timeProvider);


        TimerState newState = state.onButtonPress();


        assertTrue(newState instanceof StoppedState);
        assertEquals(0, newState.getTime());
    }

    @Test
    public void testAlarmStateHasZeroTime() {

        TestTimeProvider timeProvider = new TestTimeProvider();
        AlarmState state = new AlarmState(timeProvider);


        assertEquals(0, state.getTime());
    }
}