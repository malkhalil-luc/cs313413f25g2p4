package com.ood.timerapplication;

import org.junit.Test;
import static org.junit.Assert.*;

public class StoppedStateTest {

    @Test
    public void testButtonPressTransitionsToIncrementingState() {
        TestTimeProvider timeProvider = new TestTimeProvider();
        StoppedState state = new StoppedState(timeProvider);

        TimerState newState = state.onButtonPress();

        assertTrue(newState instanceof IncrementingState);
        assertEquals(1, newState.getTime());
    }
}