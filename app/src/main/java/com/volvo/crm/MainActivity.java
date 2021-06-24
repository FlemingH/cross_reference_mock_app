package com.volvo.crm;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.volvo.crs.IMyAidlInterface;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private MockController mockTxtController = new MockController();
    private Gson gson = new Gson();

    private AlertDialog alertDialog;
    private String[] mockItems;
    private String curMockKey = "";

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


    // push按钮点击事件
    public void aidlPush() {

        if (!mBound) {
            attemptToBindService();
            return;
        }

        if (curMockKey.equals("")) {
            new Thread(){
                @Override
                public void run() {
                    Looper.prepare();
                    Toast.makeText(getApplicationContext(), "Please select a mock file", Toast.LENGTH_SHORT).show();
                    Looper.loop();
                }
            }.start();
            return;
        }

        String mockTxt = mockTxtController.getMockTxt(curMockKey);

        if (mockTxt.equals("")) {
            new Thread(){
                @Override
                public void run() {
                    Looper.prepare();
                    Toast.makeText(getApplicationContext(), "Mock file null", Toast.LENGTH_SHORT).show();
                    Looper.loop();
                }
            }.start();
            return;
        }

        try {
            iMyAidlInterface.push(mockTxt);
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

        Button getMockButton = findViewById(R.id.button2);
        Button pushButton = findViewById(R.id.button);

        getMockButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(runnable).start();
            }
        });

        pushButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(runnablePush).start();
            }
        });
    }

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler(){
        @Override
        public void handleMessage(@NotNull Message msg) {
            super.handleMessage(msg);
            Bundle data = msg.getData();
            String mockTxt = data.getString("value");

            ArrayList mockList = gson.fromJson(mockTxt, ArrayList.class);
            mockItems = (String[]) mockList.toArray(new String[0]);

            TextView mockJsonView = findViewById(R.id.mockJsonView);
            mockJsonView.setText(mockTxt);
        }
    };

    Runnable runnable = new Runnable(){
        @Override
        public void run() {
            Message msg = new Message();
            Bundle data = new Bundle();
            String mockTxt = mockTxtController.getMockList();
            data.putString("value", mockTxt);
            msg.setData(data);
            handler.sendMessage(msg);
        }
    };

    Runnable runnablePush = new Runnable(){
        @Override
        public void run() {
            aidlPush();
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    public void showSingleAlertDialog(View view) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
        alertBuilder.setTitle("Select mock file");
        alertBuilder.setSingleChoiceItems(mockItems, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                curMockKey = mockItems[i];
                Toast.makeText(MainActivity.this, mockItems[i], Toast.LENGTH_SHORT).show();
            }
        });

            alertBuilder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                TextView curJsonView = findViewById(R.id.curJsonView);
                curJsonView.setText(String.format("Selected: %s", curMockKey));

                alertDialog.dismiss();
            }
        });

        alertBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                alertDialog.dismiss();
            }
        });

        alertDialog = alertBuilder.create();
        alertDialog.show();
    }
}
