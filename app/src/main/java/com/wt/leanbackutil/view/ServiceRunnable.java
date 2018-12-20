package com.wt.leanbackutil.view;

import android.app.Instrumentation;

public class ServiceRunnable implements Runnable {

    int mKeyCode;

    ServiceRunnable(int keyCode) {
        mKeyCode = keyCode;
    }

    @Override
    public void run() {
        try {
            new Instrumentation().sendKeyDownUpSync(mKeyCode);
            return;
        } catch (Exception localException) {
        }
    }

}