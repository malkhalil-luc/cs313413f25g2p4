package com.ood.timerapplication;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TimerViewModelTest {

    private TimerViewModel viewModel;
    private TestTimeProvider timeProvider;

    @Before
    public void setUp() {
        timeProvider = new TestTimeProvider();
        timeProvider.setTime(0);
        viewModel = new TimerViewModel(timeProvider);
    }

    @Test
    public void testTimerAutoStartsAfterThreeSeconds() {
        // Arrange - set time to 5
        viewModel.onButtonPressed();
        viewModel.onButtonPressed();
        viewModel.onButtonPressed();
        viewModel.onButtonPressed();
        viewModel.onButtonPressed();

        assertEquals(5, viewModel.getCurrentTime());
        assertFalse(viewModel.isRunning());

        // Act - advance time by 3 seconds
        timeProvider.setTime(3000);

        // Manually trigger timeout check (for testing)
        viewModel.triggerTimeoutCheck();

        // Assert
        assertTrue("Timer should be running after 3 seconds", viewModel.isRunning());
        assertEquals(5, viewModel.getCurrentTime());
    }


}