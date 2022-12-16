package com.example.trademanager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class AddAssetActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    Spinner marketList, assetList;
    EditText amountET, entryET;
    Button saveBTN, cancelBTN;
    TextView errMsgTV;

    String market,asset;
    int amount;
    double entry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_asset);

        marketList = (Spinner)findViewById(R.id.marketSPIN);
        assetList = (Spinner)findViewById(R.id.assetSPIN);
        amountET = (EditText)findViewById(R.id.amountET);
        entryET = (EditText)findViewById(R.id.entryET);
        saveBTN = (Button)findViewById(R.id.saveBTN);
        cancelBTN = (Button)findViewById(R.id.cancelBTN);
        errMsgTV = (TextView) findViewById(R.id.errMsgTV);

        marketList.setOnItemSelectedListener(this);
        assetList.setOnItemSelectedListener(this);
        saveBTN.setOnClickListener(this);
        cancelBTN.setOnClickListener(this);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.marketList, android.R.layout.simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        marketList.setAdapter(adapter);
        marketList.setSelection(0);

        adapter = ArrayAdapter.createFromResource(this, R.array.defaultList, android.R.layout.simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        assetList.setAdapter(adapter);
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
    public void onClick(View view) {
        switch(view.getId())
        {
            case R.id.saveBTN:
                readData();
                String errMsg = validateData();
                if (errMsg.equals("")) {
                    DBHelper dbHelper = new DBHelper(this);
                    Asset newAsset = new Asset(asset, amount, entry);
                    dbHelper.addAsset(newAsset);
                    errMsg= dbHelper.showAsset();
                    errMsgTV.setTextColor(Color.BLUE);
                }
                else{
                    errMsgTV.setTextColor(getColor(com.google.android.material.R.color.design_default_color_error));
                }
                errMsgTV.setText(errMsg);
                errMsgTV.setVisibility(View.VISIBLE);
                break;
            case R.id.cancelBTN:
                finish();
                break;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        errMsgTV.setVisibility(View.GONE);
        switch (adapterView.getId())
        {
            case R.id.marketSPIN:
                switch(adapterView.getSelectedItemPosition())
                {
                    case 0:
                        ((TextView)adapterView.getChildAt(0)).setTextColor(Color.GRAY);
                        market = "";
                        assetList.setEnabled(false);
                        break;
                    case 1: // Crypto
                        market = "Crypto";
                        assetList.setEnabled(true);
                        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.cryptoToAdd, android.R.layout.simple_spinner_dropdown_item);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        assetList.setAdapter(adapter);
                        break;
                    case 2: // Stock
                        market = "Stock";
                        assetList.setEnabled(true);
                        adapter = ArrayAdapter.createFromResource(this, R.array.stockToAdd, android.R.layout.simple_spinner_dropdown_item);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        assetList.setAdapter(adapter);
                        break;
                }
                break;
            case R.id.assetSPIN:
                switch(adapterView.getSelectedItemPosition())
                {
                    case 0:
                        ((TextView)adapterView.getChildAt(0)).setTextColor(Color.GRAY);
                        asset="";
                        break;
                    case 1:
                    case 2:
                        asset=adapterView.getSelectedItem().toString();
                        break;
                }
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) { }

    private void readData()
    {
        // amount
        if (amountET.getText().toString().equals("")) amount=0;
        else amount = Integer.valueOf(amountET.getText().toString());

        // entry price
        if (entryET.getText().toString().equals("")) entry=0;
        else entry = Double.valueOf(entryET.getText().toString());
    }

    private String validateData()
    {
        String errMsg="";
        // market
        if(market == "") return "- Please select market.";

        // asset
        if(asset == "") return "- Please select asset.";

        // amount
        if (amount <= 0)
        {
            if (!errMsg.equals("")) { errMsg += "\n"; }
            errMsg += "- Amount should be greater than 0.";
        }

        // entry level
        if (entry <= 0)
        {
            if (!errMsg.equals("")) { errMsg += "\n"; }
            errMsg += "- Entry price should be greater than 0.";
        }
        return errMsg;
    }
}