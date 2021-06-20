package com.volvo.crm;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import com.volvo.crs.IMyAidlInterface;

public class MainActivity extends AppCompatActivity {

    private ServiceConnection serviceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {

            try {
                Log.d("AIDL return: ", "sum is " +
                        IMyAidlInterface.Stub.asInterface(service).add(1, 2));
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = new Intent();
        intent.setAction("com.volvo.crs.crservice");
        intent.setPackage("com.volvo.crs");
        bindService(intent, serviceConnection, BIND_AUTO_CREATE);

    }
}
