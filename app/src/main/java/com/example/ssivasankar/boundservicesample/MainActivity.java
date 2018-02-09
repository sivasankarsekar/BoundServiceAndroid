package com.example.ssivasankar.boundservicesample;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private MyBoundService myBoundService;
    private Button CallServiceMethod;
    private TextView txtUpdate;

    // Service connection call back method with status
    private final ServiceConnection mServiceConnection = new ServiceConnection() {

        // Called when bound service is connected to the activity
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            myBoundService = ((MyBoundService.LocalBinder) service).getBoundService();

        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }
    };


    private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getStringExtra("Update");
            System.out.println("action " + action+" "+intent.getStringExtra("Update"));
            txtUpdate.setText(action);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txtUpdate = findViewById(R.id.txtUpdate);
        CallServiceMethod = findViewById(R.id.CallServiceMethod);

        /*
           Call Bound Service
         */
        Intent gattServiceIntent = new Intent(this, MyBoundService.class);
        bindService(gattServiceIntent, mServiceConnection, BIND_AUTO_CREATE);


        /*
            Call Service methods from button click
          */
        CallServiceMethod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myBoundService.CallServiceMethods();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mGattUpdateReceiver);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(mServiceConnection);
    }

    /*
      Create intent filters for broad cost
      1.Add Unique Action string to find the Brod cost receiver
     */
    private static IntentFilter makeGattUpdateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("EXTRA_DATA");
        return intentFilter;
    }

}
