package com.example.trademanager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "trade_manager_db";

    private static final int DB_VERSION = 1;

    private static final String TB_NAME = "asset";

    private static final String ID_COL = "id";

    private static final String ASSET_NAME_COL = "name";

    private static final String ASSET_AMOUNT_COL = "amount";

    private static final String ASSET_ENTRY_PRICE_COL = "entry_price";

    private static final String ASSET_PURCHASE_DATE = "purchase_date";

    private static final String ASSET_EXIT_PRICE = "exit_price";

    private static final String ASSET_EXIT_DATE = "exit_date";

    public DBHelper(Context context) {super(context, DB_NAME, null, DB_VERSION);}

    @Override
    public void onCreate(SQLiteDatabase db) {
        //create a table
        //create table tablename(colname datatype primary key AUTOINCREMENT, colname datatype.....)
        String query="create table "+TB_NAME+" ("+ID_COL+" INTEGER PRIMARY KEY AUTOINCREMENT, " +
                ASSET_NAME_COL+" TEXT, " +
                ASSET_AMOUNT_COL+" INTEGER, " +
                ASSET_ENTRY_PRICE_COL+" DOUBLE, " +
                ASSET_PURCHASE_DATE+" DATE, " +
                ASSET_EXIT_PRICE+" DOUBLE, " +
                ASSET_EXIT_DATE+" DATE);";
        //execute Query
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        //drop table
        String query="drop table "+TB_NAME;
        db.execSQL(query);
        //recreate it
        onCreate(db);
    }

    public void addAsset(Asset asset)
    {
        SQLiteDatabase db=this.getWritableDatabase();

        ContentValues values=new ContentValues();
        values.put(ASSET_NAME_COL,asset.name);
        values.put(ASSET_AMOUNT_COL,asset.amount);
        values.put(ASSET_ENTRY_PRICE_COL,asset.entryPrice);
        values.put(ASSET_PURCHASE_DATE, String.valueOf(asset.purchaseDate));

        db.insert(TB_NAME,null,values);

        db.close();
    }

    public String showAsset()
    {
        SQLiteDatabase db=this.getWritableDatabase();
        String data ="";
        Cursor cr = db.rawQuery("SELECT * FROM " + TB_NAME, null);
        cr.moveToFirst();
        if (cr != null && cr.getCount() > 0) {
            do {
                for (int i = 0; i < cr.getColumnCount(); i++) {
                    data = data + " / " + cr.getString(i);
                }
                data = data + "\n";
            } while(cr.moveToNext());
        } else {
            data = "no data found";
        }
        db.close();
        return data;
    }

    public ArrayList<Asset> getAllAssets(){
        SQLiteDatabase db = this.getReadableDatabase();
        String data ="";
        Cursor cr = db.rawQuery("SELECT * FROM " + TB_NAME, null);
        ArrayList<Asset> list = new ArrayList<>();

        cr.moveToFirst();
        if (cr != null && cr.getCount() > 0) {
            do {
                try{
                    Asset ast = new Asset();
                    ast.id = cr.getInt(cr.getColumnIndexOrThrow(ID_COL));
                    ast.name = cr.getString(cr.getColumnIndexOrThrow(ASSET_NAME_COL));
                    ast.amount = cr.getInt(cr.getColumnIndexOrThrow(ASSET_AMOUNT_COL));
                    ast.entryPrice = cr.getDouble(cr.getColumnIndexOrThrow(ASSET_ENTRY_PRICE_COL));
                    ast.purchaseDate = Date.valueOf( cr.getString(cr.getColumnIndexOrThrow(ASSET_PURCHASE_DATE)));
                    ast.exitPrice = cr.getDouble(cr.getColumnIndexOrThrow(ASSET_EXIT_PRICE));
                    String exitDate = cr.getString(cr.getColumnIndexOrThrow(ASSET_EXIT_DATE));
                    if(exitDate != null){
                        ast.exitDate = Date.valueOf(exitDate);
                    }

                    list.add(ast);
                }
                catch (Exception e){
                    Log.e("Date parse error", e.getMessage());
                }
            } while(cr.moveToNext());
        } else {

        }
        db.close();
        return list;
    }
}
