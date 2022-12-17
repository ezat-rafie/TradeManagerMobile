package com.example.trademanager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;

public class OverallPerformanceActivity extends AppCompatActivity {

    DBHelper dbhelper = new DBHelper(this);
    TableLayout allocationTable;
    TextView txtTotalProfit, txtTotalLoss, txtRevenue, txtAnalysis;
    Button btcBtn, ethBtn, teslaBtn, appleBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_overall_performance);
        allocationTable = (TableLayout) findViewById(R.id.allocationTable);
        txtTotalProfit = (TextView) findViewById(R.id.txtTotalProfit);
        txtTotalLoss = (TextView) findViewById(R.id.txtTotalLoss);
        txtRevenue = (TextView) findViewById(R.id.txtRevenue);
        txtAnalysis = (TextView) findViewById(R.id.txtAnalysis);
        btcBtn = (Button) findViewById(R.id.btcBtn);
        ethBtn = (Button) findViewById(R.id.ethBtn);
        teslaBtn = (Button) findViewById(R.id.teslaBtn);
        appleBtn = (Button) findViewById(R.id.appleBtn);

        PopulateAssetAllocation();
        PopulateOverallPanel();
    }

    private void PopulateOverallPanel(){
        ArrayList<Asset> assets = dbhelper.getAllAssets();
        if (assets.size() > 0) {
            double totalProfit = 0;
            double totalLoss = 0;
            double totalRevenue = 0;
            for (Asset ast: assets ) {
                if (ast.exitPrice > 0) {
                    if ((ast.exitPrice - ast.entryPrice) < 0)
                        totalLoss += ast.entryPrice - ast.exitPrice;
                    if ((ast.exitPrice - ast.entryPrice) > 0)
                        totalProfit += ast.exitPrice - ast.entryPrice;
                }
            }
            totalRevenue = totalProfit - totalLoss;
            txtTotalProfit.setText("Total Profit: " + String.valueOf(RoundPrices(totalProfit)) + "$");
            txtTotalLoss.setText("Total Loss: -" + String.valueOf(RoundPrices(totalLoss)) + "$");
            txtRevenue.setText("Total Revenue: " + String.valueOf(RoundPrices(totalRevenue)) + "$");
            if (totalRevenue > 0){
                txtAnalysis.setText("Congratulations, you're making great progress. " +
                        "Feel free to go to our help page for trading tips.");
            }
            else {
                txtAnalysis.setText("Unfortunately, you are losing money in your trades. " +
                        "Feel free to go to our help page for trading tips.");
            }
        }
    }

    private void PopulateAssetAllocation(){
        allocationTable.removeAllViews();
        TableRow.LayoutParams tableRowParams = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT);
        ArrayList<Asset> assets = dbhelper.getAllAssets();
        if (assets.size() > 0){
            double totalAssets = 0;
            double totalTesla = 0;
            double totalApple = 0;
            double totalBtc = 0;
            double totalEth = 0;
            double applePercent = 0;
            double teslaPercent = 0;
            double btcPercent = 0;
            double ethPercent = 0;
            for (Asset ast: assets ) {
                if (ast.exitPrice < 1) {
                    switch (ast.name) {
                        case "Tesla":
                            totalTesla += ast.amount * ast.entryPrice;
                            break;
                        case "Apple":
                            totalApple += ast.amount * ast.entryPrice;
                            break;
                        case "Bitcoin":
                            totalBtc += ast.amount * ast.entryPrice;
                            break;
                        case "Ethereum":
                            totalEth += ast.amount * ast.entryPrice;
                            break;
                    }
                    totalAssets += ast.amount * ast.entryPrice;
                }
            }
            if (totalAssets > 0){
                if (totalApple > 0) {
                    applePercent = (totalApple * 100) / totalAssets;
                    TableRow appleRow = new TableRow(this);
                    appleRow.setLayoutParams(tableRowParams);
                    TextView tvApple = new TextView(this);
                    tvApple.setText("Apple: " + String.valueOf(RoundPrices(applePercent)) + "%");
                    tvApple.setTextColor(Color.WHITE);
                    appleRow.addView(tvApple);
                    allocationTable.addView(appleRow);
                    LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                            0,
                            TableRow.LayoutParams.WRAP_CONTENT,
                            (float) (applePercent / 100)
                    );
                    appleBtn.setLayoutParams(param);
                }
                if (totalTesla > 0) {
                    teslaPercent = (totalTesla * 100) / totalAssets;
                    TableRow teslaRow = new TableRow(this);
                    teslaRow.setLayoutParams(tableRowParams);
                    TextView tvTesla = new TextView(this);
                    tvTesla.setText("Tesla: " + String.valueOf(RoundPrices(teslaPercent)) + "%");
                    tvTesla.setTextColor(Color.WHITE);
                    teslaRow.addView(tvTesla);
                    allocationTable.addView(teslaRow);
                    LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                            0,
                            TableRow.LayoutParams.WRAP_CONTENT,
                            (float) (teslaPercent / 100)
                    );
                    teslaBtn.setLayoutParams(param);
                }
                if (totalBtc > 0){
                    btcPercent = (totalBtc * 100) / totalAssets;
                    TableRow btcRow = new TableRow(this);
                    btcRow.setLayoutParams(tableRowParams);
                    TextView tvBtc = new TextView(this);
                    tvBtc.setText("Bitcoin: " + String.valueOf(RoundPrices(btcPercent)) + "%");
                    tvBtc.setTextColor(Color.WHITE);
                    btcRow.addView(tvBtc);
                    allocationTable.addView(btcRow);
                    LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                            0,
                            TableRow.LayoutParams.WRAP_CONTENT,
                            (float) (btcPercent / 100)
                    );
                    btcBtn.setLayoutParams(param);
                }
                if (totalEth > 0) {
                    ethPercent = (totalEth * 100) / totalAssets;
                    TableRow ethRow = new TableRow(this);
                    ethRow.setLayoutParams(tableRowParams);
                    TextView tvEth = new TextView(this);
                    tvEth.setText("Ethereum: " + String.valueOf(RoundPrices(ethPercent)) + "%");
                    tvEth.setTextColor(Color.WHITE);
                    ethRow.addView(tvEth);
                    allocationTable.addView(ethRow);
                    LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                            0,
                            TableRow.LayoutParams.WRAP_CONTENT,
                            (float) (ethPercent / 100)
                    );
                    ethBtn.setLayoutParams(param);
                }
            }
        }
        else {
            TableRow tbEmptyrow = new TableRow(this);
            tbEmptyrow.setLayoutParams(tableRowParams);
            TextView tvEmptyAsset = new TextView(this);
            tvEmptyAsset.setText("No data was found.");
            tvEmptyAsset.setTextColor(Color.WHITE);
            tbEmptyrow.addView(tvEmptyAsset);
            allocationTable.addView(tbEmptyrow);
        }
    }

    public static BigDecimal RoundPrices(double d) {
        BigDecimal bd = new BigDecimal(Double.toString(d));
        bd = bd.setScale(2, BigDecimal.ROUND_HALF_UP);
        return bd;
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