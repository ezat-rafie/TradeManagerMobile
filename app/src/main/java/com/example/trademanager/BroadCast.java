package com.example.trademanager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

public class BroadCast extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().intern() == Intent.ACTION_AIRPLANE_MODE_CHANGED)
        {
            if(Settings.System.getInt(context.getContentResolver(), Settings.Global.AIRPLANE_MODE_ON, 0) == 0){
                Toast.makeText(context, "AirPlane Mode Off", Toast.LENGTH_LONG).show();
                Log.e("Airplane mode ", "OFF");
            }else{
                Toast.makeText(context, "AirPlane Mode On", Toast.LENGTH_LONG ).show();
                Log.e("Airplane Mode ", "ON");
            }
        }
    }
}
