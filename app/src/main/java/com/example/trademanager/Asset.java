package com.example.trademanager;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;

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

    public Asset(String _name, int _amount, double _entry) {
        this.name = _name;
        this.amount = _amount;
        this.entryPrice = _entry;
        this.purchaseDate = java.sql.Date.valueOf(LocalDate.now().toString());
        exitPrice = 0.00;
        exitDate = null;
    }

}
