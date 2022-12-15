package com.example.trademanager;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class Asset implements Serializable {

    int id;
    String name;
    int amount;
    double entryPrice;
    Date purchaseDate;
    double exitPrice;
    Date exitDate;

    public Asset(){
        this.name = "";
        this.amount = 0;
        this.entryPrice = 0.00;
        this.purchaseDate = null;
        exitPrice = 0.00;
        exitDate = null;
    }

}
