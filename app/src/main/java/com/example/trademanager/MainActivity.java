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
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;

/*
    icons are downloaded from <a target="_blank" href="https://icons8.com/icon/NwAwrEVExFBt/stocks">Stocks</a> icon by <a target="_blank" href="https://icons8.com">Icons8</a>
    images are downloaded from https://www.pexels.com/search/stock%20market/
 */

public class MainActivity extends AppCompatActivity {
    BroadCast br = new BroadCast();
    NotificationManager nm;
    Button btnAddAsset;
    public static final String CHANNEL_ID = "myChannel";
    public static final int NOTIFICATION_ID = 100;

    TableLayout assetTable;
    TableLayout historyTable;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        assetTable = (TableLayout) findViewById(R.id.assetTable);
        historyTable = (TableLayout) findViewById(R.id.historyTable);
        btnAddAsset = (Button) findViewById(R.id.btnAddAsset);

        btnAddAsset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addAssetActivity = new Intent(getApplicationContext(), AddAssetActivity.class);
                startActivity(addAssetActivity);
            }
        });

        PopulateAssetTable();
        PopulateHistoryTable();
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

    private void PopulateAssetTable(){
        DBHelper dbHelper = new DBHelper(this);
        ArrayList<Asset> allAssets = dbHelper.getAllAssets();

        TableRow tbrowHeader = new TableRow(this);
        TableRow.LayoutParams tableRowParams = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT);
        tbrowHeader.setLayoutParams(tableRowParams);

        TextView tv0 = new TextView(this);
        tv0.setText("Asset ");
        tv0.setTextColor(Color.WHITE);
        tbrowHeader.addView(tv0);

        TextView tv1 = new TextView(this);
        tv1.setText(" Amount ");
        tv1.setTextColor(Color.WHITE);
        tbrowHeader.addView(tv1);

        TextView tv2 = new TextView(this);
        tv2.setText(" Entry ");
        tv2.setTextColor(Color.WHITE);
        tbrowHeader.addView(tv2);

        TextView tv3 = new TextView(this);
        tv3.setText(" Current Price ");
        tv3.setTextColor(Color.WHITE);
        tbrowHeader.addView(tv3);

        TextView tv4 = new TextView(this);
        tv4.setText(" ROI ");
        tv4.setTextColor(Color.WHITE);
        tbrowHeader.addView(tv4);

        TextView tv5 = new TextView(this);
        tv5.setText(" ");
        tv5.setTextColor(Color.WHITE);
        tbrowHeader.addView(tv5);

        assetTable.addView(tbrowHeader);

        for (int i = 0; i < allAssets.size(); i++) {
            TableRow tbrow = new TableRow(this);
            tbrow.setLayoutParams(tableRowParams);

            TextView tvAsset = new TextView(this);
            tvAsset.setText(String.valueOf(allAssets.get(i).name));
            tvAsset.setTextColor(Color.WHITE);
            tvAsset.setGravity(Gravity.CENTER);
            tbrow.addView(tvAsset);

            TextView tvAmount = new TextView(this);
            tvAmount.setText(String.valueOf(allAssets.get(i).amount));
            tvAmount.setTextColor(Color.WHITE);
            tvAmount.setGravity(Gravity.CENTER);
            tbrow.addView(tvAmount);

            TextView tvEntry = new TextView(this);
            tvEntry.setText(String.valueOf(allAssets.get(i).entryPrice));
            tvEntry.setTextColor(Color.WHITE);
            tvEntry.setGravity(Gravity.CENTER);
            tbrow.addView(tvEntry);

            TextView tvCurrent = new TextView(this);
            tvCurrent.setText("300");
            tvCurrent.setTextColor(Color.WHITE);
            tvCurrent.setGravity(Gravity.CENTER);
            tbrow.addView(tvCurrent);

            TextView tvROI = new TextView(this);
            tvROI.setText("+200");
            tvROI.setTextColor(Color.WHITE);
            tvROI.setGravity(Gravity.CENTER);
            tbrow.addView(tvROI);

            Button btnRemove = new Button(this);
            btnRemove.setText("Close");
            btnRemove.setTextColor(Color.RED);
            tbrow.addView(btnRemove);

            assetTable.addView(tbrow);
        }
    }

    private void PopulateHistoryTable(){
        /*DBHelper dbHelper = new DBHelper(this);
        ArrayList<Asset> allHistory = dbHelper.getAllHistory();

        TableRow tbrowHeader = new TableRow(this);
        TableRow.LayoutParams tableRowParams = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT);
        TableRow.LayoutParams textViewParam = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT,1.0f);
        tbrowHeader.setLayoutParams(tableRowParams);

        TextView tv0 = new TextView(this);
        tv0.setText("Asset ");
        tv0.setTextColor(Color.WHITE);
        tv0.setLayoutParams(textViewParam);
        tbrowHeader.addView(tv0);

        TextView tv1 = new TextView(this);
        tv1.setText("Amount ");
        tv1.setTextColor(Color.WHITE);
        tv1.setLayoutParams(textViewParam);
        tbrowHeader.addView(tv1);

        TextView tv2 = new TextView(this);
        tv2.setText("Entry ");
        tv2.setTextColor(Color.WHITE);
        tv2.setLayoutParams(textViewParam);
        tbrowHeader.addView(tv2);

        TextView tv3 = new TextView(this);
        tv3.setText("Exit ");
        tv3.setTextColor(Color.WHITE);
        tv3.setLayoutParams(textViewParam);
        tbrowHeader.addView(tv3);

        TextView tv4 = new TextView(this);
        tv4.setText("ROI ");
        tv4.setTextColor(Color.WHITE);
        tv4.setLayoutParams(textViewParam);
        tbrowHeader.addView(tv4);
        historyTable.addView(tbrowHeader);

        for (int i = 0; i < allHistory.size(); i++) {
            TableRow tbrow = new TableRow(this);
            tbrow.setLayoutParams(tableRowParams);

            TextView tvAsset = new TextView(this);
            tvAsset.setText(String.valueOf(allHistory.get(i).name));
            tvAsset.setTextColor(Color.WHITE);
            tvAsset.setLayoutParams(textViewParam);
            tbrow.addView(tvAsset);

            TextView tvAmount = new TextView(this);
            tvAmount.setText(String.valueOf(allHistory.get(i).amount));
            tvAmount.setTextColor(Color.WHITE);
            tvAmount.setLayoutParams(textViewParam);
            tbrow.addView(tvAmount);

            TextView tvEntry = new TextView(this);
            tvEntry.setText(String.valueOf(allHistory.get(i).entryPrice));
            tvEntry.setTextColor(Color.WHITE);
            tvEntry.setLayoutParams(textViewParam);
            tbrow.addView(tvEntry);

            TextView tvExit = new TextView(this);
            tvExit.setText(String.valueOf(allHistory.get(i).exitPrice));
            tvExit.setTextColor(Color.WHITE);
            tvExit.setLayoutParams(textViewParam);
            tbrow.addView(tvExit);

            TextView tvROI = new TextView(this);
            tvROI.setText("+200");
            tvROI.setTextColor(Color.WHITE);
            tvROI.setLayoutParams(textViewParam);
            tbrow.addView(tvROI);
            historyTable.addView(tbrow);
        }*/
    }
}