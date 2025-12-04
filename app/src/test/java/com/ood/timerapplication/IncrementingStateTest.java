package com.ood.timerapplication;

import org.junit.Test;
import static org.junit.Assert.*;

public class IncrementingStateTest {

    @Test
    public void testThreeSecondTimeoutTransitionsToRunning() {

        TestTimeProvider timeProvider = new TestTimeProvider();
        timeProvider.setTime(0);

        IncrementingState state = new IncrementingState(5, timeProvider);

        timeProvider.setTime(3000);
        TimerState newState = state.checkTimeout();


        assertTrue(newState instanceof RunningState);
        assertEquals(5, newState.getTime());
    }

    @Test
    public void testNoTimeoutIfLessThan3Seconds() {

        TestTimeProvider timeProvider = new TestTimeProvider();
        timeProvider.setTime(0);

        IncrementingState state = new IncrementingState(5, timeProvider);


        timeProvider.setTime(2000);
        TimerState newState = state.checkTimeout();


        assertTrue(newState instanceof IncrementingState);
        assertEquals(5, newState.getTime());
    }
    @Test
    public void testIncrementingStopsAt99() {

        TestTimeProvider timeProvider = new TestTimeProvider();
        IncrementingState state = new IncrementingState(98, timeProvider);


        TimerState newState = state.onButtonPress();


        assertTrue(newState instanceof RunningState);
        assertEquals(99, newState.getTime());
    }

    @Test
    public void testMultipleButtonPressesIncrement() {

        TestTimeProvider timeProvider = new TestTimeProvider();
        IncrementingState state = new IncrementingState(1, timeProvider);


        TimerState state2 = state.onButtonPress();  // 1 → 2
        TimerState state3 = state2.onButtonPress(); // 2 → 3
        TimerState state4 = state3.onButtonPress(); // 3 → 4


        assertTrue(state4 instanceof IncrementingState);
        assertEquals(4, state4.getTime());
    }
}

