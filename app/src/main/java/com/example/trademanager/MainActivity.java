package com.example.trademanager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;

/*
    icons are downloaded from <a target="_blank" href="https://icons8.com/icon/NwAwrEVExFBt/stocks">Stocks</a> icon by <a target="_blank" href="https://icons8.com">Icons8</a>
    images are downloaded from https://www.pexels.com/search/stock%20market/
 */

public class MainActivity extends AppCompatActivity {
    BroadCast br = new BroadCast();
    NotificationManager nm;

    public static final String CHANNEL_ID = "myChannel";
    public static final int NOTIFICATION_ID = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Drawable dr = ResourcesCompat.getDrawable(getResources(), R.drawable.bitcoin, null);
        BitmapDrawable bmp = (BitmapDrawable) dr;
        Bitmap largeIcon = bmp.getBitmap();

        //style of themes
        Notification.BigPictureStyle bps = new Notification.BigPictureStyle()
                .bigPicture(largeIcon)
                .setBigContentTitle("MSG")
                .setSummaryText("mgs mgs mgs");

        Notification.InboxStyle is = new Notification.InboxStyle()
                .addLine("You are losing now")
                .setBigContentTitle("Warning")
                .setSummaryText("new Inbox msg");

        Notification notification;

        // get all assets
        DBHelper dh = new DBHelper(this);
        ArrayList<Asset> assets = new ArrayList<>();
        assets = dh.getAllAssets();
        if( assets.isEmpty() == false){
            //TODO
            // get current price from API

            boolean isItLosing = false;
            for(int i = 0; i < assets.size(); i++){
                // TODO
                // compare it and if a current price is lower than entry price
                isItLosing = true;
            }

            // TODO
            // show the notification
            if(isItLosing == true){
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                    notification = new Notification.Builder(this)
                            .setLargeIcon(largeIcon)
                            .setSmallIcon(R.drawable.bitcoin)
                            .setContentText("new Message")
                            .setSubText("New Message of Trade Manager App")
                            .setStyle(is)
                            .setChannelId(CHANNEL_ID)
                            .build();
                    nm.createNotificationChannel(new NotificationChannel(CHANNEL_ID,"My Channel", NotificationManager.IMPORTANCE_HIGH));
                }else{
                    notification = new Notification.Builder(this)
                            .setLargeIcon(largeIcon)
                            .setSmallIcon(R.drawable.bitcoin)
                            .setContentText("new Message")
                            .setStyle(is)
                            .setSubText("New Message of Trade Manager App")
                            .setChannelId(CHANNEL_ID)
                            .build();
                }
                nm.notify(NOTIFICATION_ID, notification);
            }
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.trademanager_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.menuHome:
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent i = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(i);
                    }
                }, 300);
                break;
            case R.id.menuAdd:
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent i = new Intent(getApplicationContext(), AddAssetActivity.class);
                        startActivity(i);
                    }
                }, 300);
                break;
            case R.id.menuPerformance:
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent i = new Intent(getApplicationContext(), OverallPerformanceActivity.class);
                        startActivity(i);
                    }
                }, 300);
                break;
            case R.id.menuAbout:
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent i = new Intent(getApplicationContext(), AboutActivity.class);
                        startActivity(i);
                    }
                }, 300);
                break;
            case R.id.menuHelp:
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent i = new Intent(getApplicationContext(), HelpActivity.class);
                        startActivity(i);
                    }
                }, 300);
                break;
        }
        return false;
    }

    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter filter = new IntentFilter(Intent.ACTION_AIRPLANE_MODE_CHANGED);
        registerReceiver(br, filter);
    }

    @Override
    protected void onStop() {
        unregisterReceiver(br);
        super.onStop();
    }
}