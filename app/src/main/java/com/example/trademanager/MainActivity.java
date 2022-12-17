package com.example.trademanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

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
    private final Handler handler = new Handler();
    OkHttpClient client = new OkHttpClient();
    Request request = null;

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

        try {
            PopulateAssetTable();
        } catch (IOException e) {
            e.printStackTrace();
        }
        PopulateHistoryTable();
        doTheAutoRefresh();
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

    private void PopulateAssetTable() throws IOException {
        assetTable.removeAllViews();
        DBHelper dbHelper = new DBHelper(this);
        ArrayList<Asset> allCurrentAssets = dbHelper.getAllAssets();
        ArrayList<Asset> allAssets = new ArrayList<Asset>();
        if (allCurrentAssets.size() > 0) {
            for (int i = 0; i < allCurrentAssets.size(); i++) {
                if (allCurrentAssets.get(i).exitPrice == 0)
                    allAssets.add(allCurrentAssets.get(i));
            }
        }
        TableRow.LayoutParams tableRowParams = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT);

        if (allAssets.size() > 0){
            TableRow tbrowHeader = new TableRow(this);
            tbrowHeader.setLayoutParams(tableRowParams);

            TextView tv0 = new TextView(this);
            tv0.setText("Asset");
            applyDesign(tv0, true);
            tbrowHeader.addView(tv0, new TableRow.LayoutParams(0));

            TextView tv1 = new TextView(this);
            tv1.setText("Amount");
            applyDesign(tv1, true);
            tbrowHeader.addView(tv1, new TableRow.LayoutParams(1));

            TextView tv2 = new TextView(this);
            tv2.setText("Entry");
            applyDesign(tv2, true);
            tbrowHeader.addView(tv2, new TableRow.LayoutParams(2));

            TextView tv3 = new TextView(this);
            tv3.setText("Current");
            applyDesign(tv3, true);
            tbrowHeader.addView(tv3, new TableRow.LayoutParams(3));

            TextView tv4 = new TextView(this);
            tv4.setText("ROI");
            applyDesign(tv4, true);
            tbrowHeader.addView(tv4, new TableRow.LayoutParams(4));

            TextView tv5 = new TextView(this);
            tv5.setText(" ");
            TableRow.LayoutParams tbParms = new TableRow.LayoutParams(5);
            tbrowHeader.addView(tv5, tbParms);

            assetTable.addView(tbrowHeader);

            for (int i = 0; i < allAssets.size(); i++) {
                TableRow tbrow = new TableRow(this);
                tbrow.setLayoutParams(tableRowParams);

                TextView tvAsset = new TextView(this);
                tvAsset.setText(String.valueOf(allAssets.get(i).name));
                applyDesign(tvAsset,false);
                tbrow.addView(tvAsset);

                TextView tvAmount = new TextView(this);
                tvAmount.setText(String.valueOf(allAssets.get(i).amount));
                applyDesign(tvAmount,false);
                tbrow.addView(tvAmount);

                TextView tvEntry = new TextView(this);
                tvEntry.setText(String.valueOf(allAssets.get(i).entryPrice));
                applyDesign(tvEntry,false);
                tbrow.addView(tvEntry);

                TextView tvCurrent = new TextView(this);
                tvCurrent.setText("");
                applyDesign(tvCurrent,false);
                tbrow.addView(tvCurrent);

                TextView tvROI = new TextView(this);
                tvROI.setText("");
                applyDesign(tvROI,false);
                tbrow.addView(tvROI);

                request = null;
                String assetName = String.valueOf(allAssets.get(i).name);
                switch (assetName){
                    case "Bitcoin":
                        request = new Request.Builder()
                                .url("https://coinranking1.p.rapidapi.com/coin/Qwsogvtv82FCd?referenceCurrencyUuid=yhjMzLPhuIDl&timePeriod=24h")
                                .get()
                                .addHeader("X-RapidAPI-Key", "c0f7ac6ff7msh60beb7b9065b62fp1cebd7jsnf13d66f9c460")
                                .addHeader("X-RapidAPI-Host", "coinranking1.p.rapidapi.com")
                                .build();
                        break;
                    case "Ethereum":
                        request = new Request.Builder()
                                .url("https://coinranking1.p.rapidapi.com/coin/razxDUgYGNAdQ?referenceCurrencyUuid=yhjMzLPhuIDl&timePeriod=24h")
                                .get()
                                .addHeader("X-RapidAPI-Key", "c0f7ac6ff7msh60beb7b9065b62fp1cebd7jsnf13d66f9c460")
                                .addHeader("X-RapidAPI-Host", "coinranking1.p.rapidapi.com")
                                .build();
                        break;
                    case "Tesla":
                        request = new Request.Builder()
                                .url("https://alpha-vantage.p.rapidapi.com/query?function=GLOBAL_QUOTE&symbol=TSLA&datatype=json")
                                .get()
                                .addHeader("X-RapidAPI-Key", "c0f7ac6ff7msh60beb7b9065b62fp1cebd7jsnf13d66f9c460")
                                .addHeader("X-RapidAPI-Host", "alpha-vantage.p.rapidapi.com")
                                .build();
                        break;
                    case "Apple":
                        request = new Request.Builder()
                                .url("https://alpha-vantage.p.rapidapi.com/query?function=GLOBAL_QUOTE&symbol=AAPL&datatype=json")
                                .get()
                                .addHeader("X-RapidAPI-Key", "c0f7ac6ff7msh60beb7b9065b62fp1cebd7jsnf13d66f9c460")
                                .addHeader("X-RapidAPI-Host", "alpha-vantage.p.rapidapi.com")
                                .build();
                        break;
                }


                //Response response = client.newCall(request).execute();

                final String[] currentPrice = {""};
                final String[] roi = {""};
                Float entryPrice = Float.valueOf(String.valueOf(allAssets.get(i).entryPrice));

                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(@NonNull Call call, @NonNull IOException e) {
                        Log.e("Error", e.getMessage());
                        tvCurrent.setText("error");
                    }

                    @Override
                    public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                        if (response.isSuccessful()){
                            String myResponse = response.body().string();
                            JSONObject json = null;
                            try {
                                json = new JSONObject(myResponse);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            try {
                                if (assetName.equals("Bitcoin") || assetName.equals("Ethereum"))
                                    currentPrice[0] = json.getJSONObject("data").getJSONObject("coin").getString("price");
                                if (assetName.equals("Tesla") || assetName.equals("Apple"))
                                    currentPrice[0] = json.getJSONObject("Global Quote").getString("05. price");

                                currentPrice[0] = roundPrice(Float.valueOf(currentPrice[0])).toString();


                                roi[0] = roundPrice(Float.valueOf(currentPrice[0]) - entryPrice).toString();

                                if(roi[0].startsWith("-")){
                                    nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                                    Drawable dr = ResourcesCompat.getDrawable(getResources(), R.drawable.bitcoin, null);
                                    BitmapDrawable bmp = (BitmapDrawable) dr;
                                    Bitmap largeIcon = bmp.getBitmap();

                                    //style of themes
//                                    Notification.InboxStyle is = new Notification.InboxStyle()
//                                            .addLine("You are losing now")
//                                            .setBigContentTitle("Warning")
//                                            .setSummaryText("new Inbox msg");

                                    Notification notification;

                                    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                                        notification = new Notification.Builder(getApplicationContext())
                                                .setLargeIcon(largeIcon)
                                                .setSmallIcon(R.drawable.bitcoin)
                                                .setContentText("You are losing now")
                                                .setSubText("New Message of Trade Manager App")
//                                                .setStyle(is)
                                                .setChannelId(CHANNEL_ID)
                                                .build();
                                        nm.createNotificationChannel(new NotificationChannel(CHANNEL_ID,"My Channel", NotificationManager.IMPORTANCE_HIGH));
                                    }else{
                                        notification = new Notification.Builder(getApplicationContext())
                                                .setLargeIcon(largeIcon)
                                                .setSmallIcon(R.drawable.bitcoin)
                                                .setContentText("You are losing now")
//                                                .setStyle(is)
                                                .setSubText("New Message of Trade Manager App")
                                                .setChannelId(CHANNEL_ID)
                                                .build();
                                    }
                                    nm.notify(NOTIFICATION_ID, notification);
                                }
                                MainActivity.this.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        tvCurrent.setText(currentPrice[0]);
                                        tvROI.setText(roi[0]);
                                    }
                                });
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });

                Button btnRemove = new Button(this);
                btnRemove.setText("X");
                btnRemove.setTextColor(Color.RED);
                btnRemove.setId(allAssets.get(i).id);
                btnRemove.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(tvCurrent.getText() != "error"){
                            for (Asset ast:allAssets
                                 ) {
                                if (ast.id == btnRemove.getId()) {
                                    dbHelper.updateAsset(ast, Double.valueOf(tvCurrent.getText().toString()));
                                    startActivity(getIntent());
                                }
                            }
                        }else{
                            Toast.makeText(getApplicationContext(), "Cannot exit with the current price", Toast.LENGTH_LONG).show();
                        }
                    }
                });
                TableRow.LayoutParams btPrm = new TableRow.LayoutParams(5);
                btPrm.width=50;
                btPrm.setMargins(10, 2,2,2);
                tbrow.addView(btnRemove, btPrm);
                assetTable.addView(tbrow);
            }
        }
        else {
            assetTable.addView(getEmptyMessageRow());
        }
    }

    private void PopulateHistoryTable(){
        DBHelper dbHelper = new DBHelper(this);
        ArrayList<Asset> allHistory = dbHelper.getAllAssets();

        TableRow.LayoutParams tableRowParams = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT);
        TableRow.LayoutParams textViewParam = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT,1.0f);
        textViewParam.setMargins(0,20,0,0);

        ArrayList<Asset> historyList = new ArrayList<Asset>();
        for (int i = 0; i < allHistory.size(); i++){
            if (allHistory.get(i).exitPrice > 0)
                historyList.add(allHistory.get(i));
        }

        if (historyList.size() > 0){
            TableRow tbrowHeader = new TableRow(this);
            tbrowHeader.setLayoutParams(tableRowParams);

            TextView tv0 = new TextView(this);
            tv0.setText("Asset");
            applyDesign(tv0, true);
            tbrowHeader.addView(tv0, textViewParam);

            TextView tv1 = new TextView(this);
            tv1.setText("Amount");
            applyDesign(tv1, true);
            tbrowHeader.addView(tv1, textViewParam);

            TextView tv2 = new TextView(this);
            tv2.setText("Entry");
            applyDesign(tv2, true);
            tbrowHeader.addView(tv2, textViewParam);

            TextView tv3 = new TextView(this);
            tv3.setText("Exit");
            applyDesign(tv3, true);
            tbrowHeader.addView(tv3, textViewParam);

            TextView tv4 = new TextView(this);
            tv4.setText("ROI");
            applyDesign(tv4, true);
            tbrowHeader.addView(tv4, textViewParam);

            historyTable.addView(tbrowHeader);

            for (int i = 0; i < historyList.size(); i++) {
                TableRow tbrow = new TableRow(this);
                tbrow.setLayoutParams(tableRowParams);

                TextView tvAsset = new TextView(this);
                tvAsset.setText(String.valueOf(historyList.get(i).name));
                applyDesign(tvAsset,false);
                tbrow.addView(tvAsset, textViewParam);

                TextView tvAmount = new TextView(this);
                tvAmount.setText(String.valueOf(historyList.get(i).amount));
                applyDesign(tvAmount,false);
                tbrow.addView(tvAmount, textViewParam);

                TextView tvEntry = new TextView(this);
                tvEntry.setText(String.valueOf(historyList.get(i).entryPrice));
                applyDesign(tvEntry,false);
                tbrow.addView(tvEntry, textViewParam);

                TextView tvExit = new TextView(this);
                tvExit.setText(String.valueOf(historyList.get(i).exitPrice));
                applyDesign(tvExit,false);
                tbrow.addView(tvExit, textViewParam);

                double roiHist = Double.valueOf(historyList.get(i).exitPrice) - Double.valueOf(historyList.get(i).entryPrice);
                TextView tvROI = new TextView(this);
                tvROI.setText(String.valueOf(roundPrice((float)roiHist)));
                applyDesign(tvROI,false);
                tbrow.addView(tvROI, textViewParam);

                historyTable.addView(tbrow);
            }
        }
        else {
            historyTable.addView(getEmptyMessageRow());
        }
    }


    private void applyDesign(TextView tv, boolean isHeader) {
        tv.setTextColor(Color.WHITE);
        tv.setGravity(Gravity.CENTER);
        if (isHeader) tv.setTypeface(tv.getTypeface(), Typeface.BOLD);
    }

    private TableRow getEmptyMessageRow() {
        TableRow tbEmptyrow = new TableRow(this);
        TableRow.LayoutParams tableRowParams = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT);
        tbEmptyrow.setLayoutParams(tableRowParams);
        TextView tvEmptyAsset = new TextView(this);
        TableRow.LayoutParams emptyPrm = new TableRow.LayoutParams(0);
        emptyPrm.weight = 6;
        tvEmptyAsset.setText("No data was found.");
        tvEmptyAsset.setTextSize(15);
        tvEmptyAsset.setTextColor(Color.WHITE);
        tvEmptyAsset.setGravity(Gravity.CENTER);
        tbEmptyrow.addView(tvEmptyAsset, emptyPrm);
        return tbEmptyrow;
    }

    public static String GetPrice(){
        final String[] finalPrice = {"error"};

        return finalPrice[0];
    }

    public static BigDecimal roundPrice(float d) {
        BigDecimal bd = new BigDecimal(Float.toString(d));
        bd = bd.setScale(2, BigDecimal.ROUND_HALF_UP);
        return bd;
    }

    private void doTheAutoRefresh() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    PopulateAssetTable();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                doTheAutoRefresh();
            }
        }, 300000);
    }
}