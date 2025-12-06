package edu.luc.etl.cs313.android.simplestopwatch.test.android;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import edu.luc.etl.cs313.android.simplestopwatch.android.TimerAdapter;
import org.junit.Test;

import android.widget.Button;
import android.widget.TextView;
import edu.luc.etl.cs313.android.simplestopwatch.R;

import static edu.luc.etl.cs313.android.simplestopwatch.common.Constants.SEC_PER_MIN;

/**
 * Abstract GUI-level test superclass for essential timer scenarios.
 * Tests the timer's incrementing, running, and alarming behavior.
 *
 * @author [Your name]
 */
public abstract class AbstractTimerActivityTest {

    /**
     * Verifies that the activity under test can be launched.
     */
    @Test
    public void testActivityCheckTestCaseSetUpProperly() {
        assertNotNull("activity should be launched successfully", getActivity());
    }

    /**
     * Verifies the following scenario: time is 0, state is STOPPED.
     *
     * @throws Throwable
     */
    @Test
    public void testActivityScenarioInit() throws Throwable {
        getActivity().runOnUiThread(() -> {
            assertEquals(0, getDisplayedValue());
            assertEquals("Stopped", getStateName().getText().toString());
        });
    }

    /**
     * Verifies the following scenario:
     * time is 0, press start 3 times, expect time 3 and state INCREMENTING.
     *
     * @throws Throwable
     */
    @Test
    public void testActivityScenarioIncrementing() throws Throwable {
        getActivity().runOnUiThread(() -> {
            assertEquals(0, getDisplayedValue());

            // Press start/stop button 3 times
            for (int i = 0; i < 3; i++) {
                assertTrue(getStartStopButton().performClick());
            }

            // Should be in incrementing state with time = 3
            assertEquals(3, getDisplayedValue());
            // Note: Exact state name depends on strings.xml
            String stateName = getStateName().getText().toString();
            assertTrue("Should be in incrementing state",
                    stateName.contains("Setting") || stateName.contains("INCREMENTING"));
        });
    }

    /**
     * Verifies the following scenario:
     * Set time to 3, wait 3+ seconds for timeout, expect transition to RUNNING.
     *
     * @throws Throwable
     */
    @Test
    public void testActivityScenarioIncrementingTimeout() throws Throwable {
        getActivity().runOnUiThread(() -> {
            assertEquals(0, getDisplayedValue());

            // Set time to 3 seconds
            for (int i = 0; i < 3; i++) {
                assertTrue(getStartStopButton().performClick());
            }

            assertEquals(3, getDisplayedValue());
        });

        // Wait for timeout (3 seconds) + buffer
        Thread.sleep(3500);
        runUiThreadTasks();

        getActivity().runOnUiThread(() -> {
            // Should now be in RUNNING state
            String stateName = getStateName().getText().toString();
            assertTrue("Should be in running state",
                    stateName.contains("Running") || stateName.contains("RUNNING"));

            // Time should still be 3 (hasn't counted down yet)
            assertEquals(3, getDisplayedValue());
        });
    }

    /**
     * Verifies the following scenario:
     * Set time to 2, let it transition to running, wait for countdown to 0,
     * expect ALARM state.
     *
     * @throws Throwable
     */
    @Test
    public void testActivityScenarioCountdownToAlarm() throws Throwable {
        getActivity().runOnUiThread(() -> {
            // Set time to 2 seconds
            for (int i = 0; i < 2; i++) {
                assertTrue(getStartStopButton().performClick());
            }
            assertEquals(2, getDisplayedValue());
        });

        // Wait for timeout to transition to RUNNING
        Thread.sleep(3500);
        runUiThreadTasks();

        getActivity().runOnUiThread(() -> {
            assertEquals(2, getDisplayedValue());
        });

        // Wait for countdown (2 seconds) + buffer
        Thread.sleep(2500);
        runUiThreadTasks();

        getActivity().runOnUiThread(() -> {
            // Should now be in ALARM state
            String stateName = getStateName().getText().toString();
            assertTrue("Should be in alarm state",
                    stateName.contains("ALARM") || stateName.contains("Alarm"));

            // Time should be 0
            assertEquals(0, getDisplayedValue());
        });
    }

    /**
     * Verifies the following scenario:
     * Set time, let countdown complete, stop alarm by pressing button.
     *
     * @throws Throwable
     */
    @Test
    public void testActivityScenarioStopAlarm() throws Throwable {getActivity().runOnUiThread(() -> {
        // Set time to 1 second for quick test
        assertTrue(getStartStopButton().performClick());
        assertEquals(1, getDisplayedValue());
    });

        // Wait for timeout (3s) + countdown (1s) + buffer
        Thread.sleep(3500);
        runUiThreadTasks();

        // Poll until timer reaches 0 (max 5 seconds)
        long startTime = System.currentTimeMillis();
        while (System.currentTimeMillis() - startTime < 5000) {
            final int[] currentValue = {-1};
            getActivity().runOnUiThread(() -> {
                currentValue[0] = getDisplayedValue();
            });
            Thread.sleep(100);  // Small delay to let UI thread run

            if (currentValue[0] == 0) {
                break;  // Timer reached 0!
            }
        }

        runUiThreadTasks();

        getActivity().runOnUiThread(() -> {
            // Should be in ALARM state
            assertEquals(0, getDisplayedValue());

            // Press button to stop alarm
            assertTrue(getStartStopButton().performClick());
        });

        runUiThreadTasks();

        getActivity().runOnUiThread(() -> {
            // Should be back in STOPPED state
            String stateName = getStateName().getText().toString();
            assertTrue("Should be in stopped state",
                    stateName.contains("Stopped") || stateName.contains("STOPPED"));
            assertEquals(0, getDisplayedValue());
        });
    }

    /**
     * Verifies the following scenario:
     * Set time, let it start running, then manually stop before alarm.
     *
     * @throws Throwable
     */
    @Test
    public void testActivityScenarioManualStop() throws Throwable {
        getActivity().runOnUiThread(() -> {
            // Set time to 5 seconds
            for (int i = 0; i < 5; i++) {
                assertTrue(getStartStopButton().performClick());
            }
            assertEquals(5, getDisplayedValue());
        });

        // Wait for timeout to start running
        Thread.sleep(3500);
        runUiThreadTasks();

        getActivity().runOnUiThread(() -> {
            // Should be running
            assertEquals(5, getDisplayedValue());
        });

        // Wait a bit for some countdown
        Thread.sleep(2000);
        runUiThreadTasks();

        getActivity().runOnUiThread(() -> {
            // Time should have decreased
            int currentTime = getDisplayedValue();
            assertTrue("Time should be counting down", currentTime < 5 && currentTime > 0);

            // Press button to stop
            assertTrue(getStartStopButton().performClick());
        });

        runUiThreadTasks();

        getActivity().runOnUiThread(() -> {
            // Should be back in STOPPED state with time reset to 0
            String stateName = getStateName().getText().toString();
            assertTrue("Should be in stopped state",
                    stateName.contains("Stopped") || stateName.contains("STOPPED"));
            assertEquals(0, getDisplayedValue());
        });
    }

    // Auxiliary methods for easy access to UI widgets

    protected abstract TimerAdapter getActivity();

    protected int tvToInt(final TextView t) {
        return Integer.parseInt(t.getText().toString().trim());
    }

    protected int getDisplayedValue() {
        final TextView display = getActivity().findViewById(R.id.timerDisplay);
        return tvToInt(display);
    }

    protected Button getStartStopButton() {
        return getActivity().findViewById(R.id.timerButton);
    }


    protected TextView getStateName() {
        return getActivity().findViewById(R.id.stateName);
    }

    /**
     * Explicitly runs tasks scheduled to run on the UI thread in case this is required
     * by the testing framework, e.g., Robolectric.
     */
    protected void runUiThreadTasks() { }
}