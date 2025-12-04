package com.ood.timerapplication;

public class TestTimeProvider implements TimeProvider {
    private long currentTime;

    public void setTime(long time) {
        this.currentTime = time;
    }

    @Override
    public long getTime() {
        return currentTime;
    }
}