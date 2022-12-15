package com.example.trademanager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;

/*
    icons are downloaded from <a target="_blank" href="https://icons8.com/icon/NwAwrEVExFBt/stocks">Stocks</a> icon by <a target="_blank" href="https://icons8.com">Icons8</a>
    images are downloaded from https://www.pexels.com/search/stock%20market/
 */

public class MainActivity extends AppCompatActivity {
    BroadCast br = new BroadCast();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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