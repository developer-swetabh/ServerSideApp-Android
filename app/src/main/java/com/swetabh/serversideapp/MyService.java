package com.swetabh.serversideapp;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Toast;

import java.util.Random;

public class MyService extends Service {

    // flag to check whether  the other application is still requesting for random number
    public static final int GET_RANDOM_NUMBER_FLAG = 0;

    private static final String TAG = MyService.class.getSimpleName();

    // min random number
    private final int MIN = 0;

    // max random number
    private final int MAX = 100;

    // variable for storing random number
    private int mRandomNumber;

    // variable to check whether random number generator is on or not
    private boolean mIsRandomGeneratorOn;

    private Messenger mRandomNumberMessenger = new Messenger(new RandomNumberHandler());

    public MyService() {
    }

    private int getRandomNumber() {
        return mRandomNumber;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mRandomNumberMessenger.getBinder();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mIsRandomGeneratorOn = true;
        new Thread(new Runnable() {
            @Override
            public void run() {
                startRandomNumberGenerator();
            }
        }).start();
        return START_STICKY;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        stopRandomNumberGenerator();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    private void startRandomNumberGenerator() {
        while (mIsRandomGeneratorOn) {
            try {
                Thread.sleep(1000);
                if (mIsRandomGeneratorOn) {
                    mRandomNumber = new Random().nextInt(MAX) + MIN;
                    Log.i(TAG, "Random Number: " + mRandomNumber);
                }
            } catch (InterruptedException e) {
                Log.i(TAG, "Thread Interrupted");
            }

        }
    }

    private void stopRandomNumberGenerator() {
        mIsRandomGeneratorOn = false;
        Toast.makeText(getApplicationContext(), "Service Stopped", Toast.LENGTH_SHORT).show();
    }

    /**
     * created a custom handler class for handling messages
     */
    private class RandomNumberHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case GET_RANDOM_NUMBER_FLAG:
                    Message msgSendRandomNumber = Message.obtain(null, GET_RANDOM_NUMBER_FLAG);
                    msgSendRandomNumber.arg1 = getRandomNumber();
                    try {
                        msg.replyTo.send(msgSendRandomNumber);
                    } catch (RemoteException e) {
                        Log.i(TAG, "" + e.getMessage());
                    }
            }
            super.handleMessage(msg);
        }
    }
}
