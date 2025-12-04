package edu.luc.etl.cs313.android.simplestopwatch.model.timer.state;

interface TimerSMStateView {

    void toRunningState();
    void toStoppedState();
    void toIncrementingState();
    void toAlarmingState();

    void actionInit();
    void actionReset();
    void actionStart();
    void actionStop();
    void actionInc();
    void actionDec();
    void actionUpdateView();
    void actionPlayBeep();
    void actionStartAlarm();
    void actionStopAlarm();

    int getTime();

    void updateUIRuntime();
}