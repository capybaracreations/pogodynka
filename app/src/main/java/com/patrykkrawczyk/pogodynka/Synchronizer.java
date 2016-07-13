package com.patrykkrawczyk.pogodynka;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;

/**
 * Created by Patryk Krawczyk on 13.07.2016.
 */
public class Synchronizer implements Handler.Callback {

    private static final int DELAY = 60 * 1000; // 60s * 1000milis
    public static final int REFRESH_DATA = 20;

    private Handler mHandler;
    private boolean enabled;
    private OnSynchronizeRefresh refresher;

    private static Synchronizer ourInstance = new Synchronizer();

    public static Synchronizer getInstance() {
        return ourInstance;
    }

    private Synchronizer() {
        HandlerThread handlerThread = new HandlerThread("SynchronizeThread");
        handlerThread.start();
        mHandler = new Handler(handlerThread.getLooper(), this);
    }

    public void quit() {
        mHandler.getLooper().quit();
    }

    public void resume() {
        enabled = true;
        mHandler.sendEmptyMessage(REFRESH_DATA);
    }

    public void pause() {
        enabled = false;
        mHandler.removeMessages(REFRESH_DATA);
    }

    @Override
    public boolean handleMessage(Message message) {
        switch (message.what) {
            case REFRESH_DATA:
                refreshData();
                break;
        }
        return true;
    }

    private void refreshData() {
        if (refresher != null) refresher.OnRefresh();
        if (enabled) mHandler.sendEmptyMessageDelayed(REFRESH_DATA,DELAY);
    }

    public void setRefresher(OnSynchronizeRefresh refresher) {
        this.refresher = refresher;
    }
}
