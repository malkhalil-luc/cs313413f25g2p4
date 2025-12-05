package edu.luc.etl.cs313.android.simplestopwatch.android;

import androidx.test.filters.SmallTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Rule;
import org.junit.runner.RunWith;

import edu.luc.etl.cs313.android.simplestopwatch.test.android.AbstractTimerActivityTest;
/**
 * Concrete Android instrumentation test subclass for TimerAdapter.
 * Has to inherit from framework class and uses delegation to concrete
 * subclass of abstract test superclass.
 *
 * IMPORTANT: project must export JUnit 4 to make it available on the device.
 *
 * @author [Your name]
 * @see "https://developer.android.com/training/testing/ui-testing/"
 */
@RunWith(AndroidJUnit4.class)
@SmallTest
public class TimerActivityTest extends AbstractTimerActivityTest {

    @Rule
    public final ActivityTestRule<TimerAdapter> activityRule =
            new ActivityTestRule<>(TimerAdapter.class);

    @Override
    protected TimerAdapter getActivity() {
        return activityRule.getActivity();
    }
}