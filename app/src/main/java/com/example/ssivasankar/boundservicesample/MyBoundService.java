package com.example.ssivasankar.boundservicesample;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.Toast;

/**
 * Created by ssivasankar on 09-02-2018.
 */

public class MyBoundService extends Service {
    private static int COUNT=0;
    IBinder iBinder = new LocalBinder();
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //Do something after 100ms
                COUNT=COUNT+1;
                System.out.println("mmmm COUNT="+COUNT);
                handler.postDelayed(this, 2000);
                UpdateActivity();
            }
        }, 2000);
        return iBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        // After using a given device, you should make sure that BluetoothGatt.close() is called
        // such that resources are cleaned up properly.  In this particular example, close() is
        // invoked when the UI is disconnected from the Service.
        return super.onUnbind(intent);
    }


    class LocalBinder extends Binder {

        MyBoundService getBoundService() {

            return MyBoundService.this;
        }

    }

    // Sample Service class Methods
    public int CallServiceMethods(){
        UpdateActivity();
        System.out.println("mmmm CallServiceMethods");
        return 1;
    }

    public void UpdateActivity(){
        // 1.Using Broadcast Receiver to update the UI
        final Intent intent = new Intent("EXTRA_DATA");
        intent.putExtra("Update",""+COUNT);
        sendBroadcast(intent);
        System.out.println("mmmm UpdateActivity");
    }

}
