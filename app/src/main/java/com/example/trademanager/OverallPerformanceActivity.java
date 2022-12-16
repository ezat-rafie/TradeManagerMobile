package com.example.trademanager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;

public class OverallPerformanceActivity extends AppCompatActivity {

    DBHelper dbhelper = new DBHelper(this);
    ListView assetList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_overall_performance);
        assetList = (ListView) findViewById(R.id.assetList);
        double totalPurchase = 0;

        ArrayList<Asset> assets = dbhelper.getAllAssets();
        for (Asset ast: assets ) {
            totalPurchase += ast.amount * ast.entryPrice;
        }

        ArrayList<String> assetDisplay = setAssetAllocation(assets, totalPurchase);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter(this, R.layout.activity_listview, R.id.textViewAllocation, assetDisplay);
        assetList.setAdapter(arrayAdapter);
    }

    ArrayList<String> setAssetAllocation (ArrayList<Asset> assets, double totalPurchase){
        ArrayList<String> assetDisplay = new ArrayList<>();

        for (Asset ast: assets ) {

            int portion = (int) ((ast.amount * ast.entryPrice) / totalPurchase *100 );
            assetDisplay.add(ast.name + " at " + ast.purchaseDate + ":    " + portion + "% ");
        }
        return assetDisplay;
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
}