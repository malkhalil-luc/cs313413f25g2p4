package edu.luc.etl.cs313.android.simplestopwatch.android;

import android.app.Activity;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

import java.util.Locale;

import edu.luc.etl.cs313.android.simplestopwatch.R;
import edu.luc.etl.cs313.android.simplestopwatch.common.StopwatchModelListener;
import edu.luc.etl.cs313.android.simplestopwatch.model.timer.ConcreteTimerModelFacade;
import edu.luc.etl.cs313.android.simplestopwatch.model.timer.TimerModelFacade;

/**
 * Timer Activity - displays and controls the timer.
 */
public class TimerAdapter extends Activity implements StopwatchModelListener {

    private static String TAG = "timer-android-activity";

    private TimerModelFacade model;
    private ToneGenerator toneGenerator;
    private boolean alarmSounding = false;

    protected void setModel(final TimerModelFacade model) {
        this.model = model;
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);

        // Create the model
        this.setModel(new ConcreteTimerModelFacade());
        model.setModelListener(this);

        // Initialize tone generator for beeps
        toneGenerator = new ToneGenerator(AudioManager.STREAM_NOTIFICATION, 100);
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        model.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (toneGenerator != null) {
            toneGenerator.release();
        }
    }

    /**
     * Updates the time display (two-digit format 00-99)
     */
    @Override
    public void onTimeUpdate(final int time) {
        runOnUiThread(() -> {
            final TextView display = findViewById(R.id.timerDisplay);
            display.setText(String.format(Locale.getDefault(), "%02d", time));
        });
    }

    /**
     * Updates the state name display
     */
    @Override
    public void onStateUpdate(final int stateId) {
        runOnUiThread(() -> {
            final TextView stateName = findViewById(R.id.stateName);
            stateName.setText(getString(stateId));

            // Handle alarm state
            if (stateId == R.string.ALARM) {
                startAlarm();
            } else {
                stopAlarm();
            }
        });
    }

    /**
     * Play a single beep (when timer starts)
     */
    private void playBeep() {
        if (toneGenerator != null) {
            toneGenerator.startTone(ToneGenerator.TONE_PROP_BEEP, 200);
        }
    }

    /**
     * Start continuous alarm
     */
    private void startAlarm() {
        if (!alarmSounding && toneGenerator != null) {
            alarmSounding = true;
            // Play alarm tone continuously
            new Thread(() -> {
                while (alarmSounding) {
                    toneGenerator.startTone(ToneGenerator.TONE_CDMA_ALERT_CALL_GUARD, 500);
                    try {
                        Thread.sleep(600);
                    } catch (InterruptedException e) {
                        break;
                    }
                }
            }).start();
        }
    }

    /**
     * Stop alarm
     */
    private void stopAlarm() {
        alarmSounding = false;
    }

    // Button click handler
    public void onButton(final View view) {
        model.onStartStop();
    }
}