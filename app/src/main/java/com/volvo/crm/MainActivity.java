package com.volvo.crm;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;

import com.volvo.crs.IMyAidlInterface;

public class MainActivity extends AppCompatActivity {

    private String TAG = "client";

    //由AIDL文件生成的Java类
    private IMyAidlInterface iMyAidlInterface = null;

    //标志当前与服务端连接状况的布尔值，false为未连接，true为连接中
    private boolean mBound = false;

    private ServiceConnection serviceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {

            Log.e(TAG, "service connected");
            iMyAidlInterface = IMyAidlInterface.Stub.asInterface(service); // init manager
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.e(TAG, "service disconnected");
            mBound = false;
        }
    };

    private void attemptToBindService() {
        Intent intent = new Intent();
        intent.setAction("com.volvo.crs.crservice");
        intent.setPackage("com.volvo.crs");
        bindService(intent, serviceConnection, BIND_AUTO_CREATE);
    }


    // 按钮点击事件
    public void aidlPush(View view) {

        if (!mBound) {
            attemptToBindService();
            return;
        }

        try {
            iMyAidlInterface.push("testJson");
        } catch (RemoteException e) {
            Log.e(TAG, "aidlPush RemoteException");
            e.printStackTrace();
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        if (!mBound) {
            attemptToBindService();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mBound) {
            unbindService(serviceConnection);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
