package edu.luc.etl.cs313.android.simplestopwatch.android;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;
import android.widget.Button;
import android.media.MediaPlayer;
import java.util.Locale;
import edu.luc.etl.cs313.android.simplestopwatch.R;
import edu.luc.etl.cs313.android.simplestopwatch.common.StopwatchModelListener;
import edu.luc.etl.cs313.android.simplestopwatch.model.timer.ConcreteTimerModelFacade;
import edu.luc.etl.cs313.android.simplestopwatch.model.timer.TimerModelFacade;

/**
 * Timer Adapter manages the timer UI and integrates with the timer state machine.
 */
public class TimerAdapter extends Activity implements StopwatchModelListener {

    private static String TAG = "timer-android-activity";

    private TimerModelFacade model;

    private MediaPlayer beepPlayer;
    private MediaPlayer alarmPlayer;

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

        try {
            beepPlayer = MediaPlayer.create(getApplicationContext(),R.raw.beep ); //beepUri);

            alarmPlayer = MediaPlayer.create(getApplicationContext(), R.raw.digital_alarm);
            alarmPlayer.setLooping(true);

            if (alarmPlayer != null) {
                alarmPlayer.setLooping(true);
            }
        } catch (Exception e) {
            android.util.Log.e(TAG, "Failed to initialize MediaPlayer", e);
        }
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

        if (beepPlayer != null) {
            beepPlayer.release();
            beepPlayer = null;
        }
        if (alarmPlayer != null) {
            if (alarmPlayer.isPlaying()) {
                alarmPlayer.stop();
            }
            alarmPlayer.release();
            alarmPlayer = null;
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

            final Button button = findViewById(R.id.timerButton);
            if (stateId == R.string.STOPPED || stateId == R.string.INCREMENTING) {
                button.setText("Increment");
            } else if (stateId == R.string.RUNNING ) {
                button.setText("Stop");
            } else if ( stateId == R.string.ALARM){
                button.setText("Reset");
            }


            if (stateId == R.string.RUNNING) {
                playBeep();
            } else if (stateId == R.string.ALARM) {
                startAlarm();
            } else if (stateId == R.string.STOPPED) {
                stopAlarm();
            }
        });
    }

    /**
     * Play a single beep (when timer starts)
     */
    private void playBeep() {

        if (beepPlayer != null) {
            try {
                if (beepPlayer.isPlaying()) {
                    beepPlayer.stop();
                    beepPlayer.prepare();
                }
                beepPlayer.start();
            } catch (Exception e) {
                android.util.Log.e(TAG, "Error playing beep", e);
            }
        }

    }

    /**
     * Start continuous alarm
     */
    private void startAlarm() {

        if (alarmPlayer != null && !alarmPlayer.isPlaying()) {
            try {
                alarmPlayer.start();
            } catch (Exception e) {
                android.util.Log.e(TAG, "Error starting alarm", e);
            }
        }

    }

    /**
     * Stop alarm
     */
    private void stopAlarm() {

        //alarmSounding = false; rem
        if (alarmPlayer != null && alarmPlayer.isPlaying()) {
            try {
                alarmPlayer.pause();
                alarmPlayer.seekTo(0);
            } catch (Exception e) {
                android.util.Log.e(TAG, "Error stopping alarm", e);
            }
        }


    }

    // Button click handler
    public void onButton(final View view) {

        model.onStartStop();
    }
}