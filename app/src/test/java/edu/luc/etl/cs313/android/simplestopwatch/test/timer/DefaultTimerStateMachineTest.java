package edu.luc.etl.cs313.android.simplestopwatch.test.timer;

import org.junit.After;
import org.junit.Before;

import edu.luc.etl.cs313.android.simplestopwatch.model.timer.state.DefaultTimerStateMachine;

public class DefaultTimerStateMachineTest extends AbstractTimerStateMachineTest {

    @Before
    public void setUp() throws Exception {
        super.setUp();
        setModel(new DefaultTimerStateMachine(getDependency(), getDependency()));
    }

    @After
    public void tearDown() {
        setModel(null);
        super.tearDown();
    }
}
