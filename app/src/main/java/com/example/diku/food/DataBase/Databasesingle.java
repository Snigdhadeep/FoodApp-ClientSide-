package com.example.diku.food.DataBase;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.util.Log;

import com.example.diku.food.Module.Order;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Diku on 06-06-2018.
 */

public class Databasesingle extends SQLiteAssetHelper {
    private static final String DB_NAME="single_eatDB.db";
    private static final int DB_VER=1;
    public Databasesingle(Context context) {
        super(context,DB_NAME,null,DB_VER);
    }

    public List<Order> getCarts(){

        SQLiteDatabase db=getReadableDatabase();
        SQLiteQueryBuilder qb=new SQLiteQueryBuilder();

        String[] sqlSelect={"productid","productname","quantity","price","discount"};
        String sqlTable="eat";

        qb.setTables(sqlTable);
        Cursor c=qb.query(db,sqlSelect,null,null,null,null,null);

        final List<Order> result=new ArrayList<>();

        if (c.moveToFirst()){

            do {
                result.add(new Order(c.getString(c.getColumnIndex("productid")),
                        c.getString(c.getColumnIndex("productname")),
                        c.getString(c.getColumnIndex("quantity")),
                        c.getString(c.getColumnIndex("price")),
                        c.getString(c.getColumnIndex("discount"))

                ));
                Log.i("productid",c.getString(c.getColumnIndex("productid")));
                Log.i("productname",c.getString(c.getColumnIndex("productname")));
                Log.i("quantity",c.getString(c.getColumnIndex("quantity")));
                Log.i("price",c.getString(c.getColumnIndex("price")));
                Log.i("discount",c.getString(c.getColumnIndex("discount")));

            }
            while (c.moveToNext());

        }

        return result;




    }


    public List<Order> getSingleCart(){

        SQLiteDatabase db=getReadableDatabase();
        SQLiteQueryBuilder qb=new SQLiteQueryBuilder();

        String[] sqlSelect={"productid","productname","quantity","price","discount"};
        String sqlTable="eat";

        qb.setTables(sqlTable);
        Cursor c=qb.query(db,sqlSelect,null,null,null,null,null);

        final List<Order> result=new ArrayList<>();

        if (c.moveToFirst()){

            do {
                result.add(new Order(c.getString(c.getColumnIndex("productid")),
                        c.getString(c.getColumnIndex("productname")),
                        c.getString(c.getColumnIndex("quantity")),
                        c.getString(c.getColumnIndex("price")),
                        c.getString(c.getColumnIndex("discount"))

                ));
                Log.i("productid",c.getString(c.getColumnIndex("productid")));
                Log.i("productname",c.getString(c.getColumnIndex("productname")));
                Log.i("quantity",c.getString(c.getColumnIndex("quantity")));
                Log.i("price",c.getString(c.getColumnIndex("price")));
                Log.i("discount",c.getString(c.getColumnIndex("discount")));

            }
            while (c.moveToNext());

        }

        return result;




    }


    public void addToCart(Order order){

        SQLiteDatabase db=getReadableDatabase();
        String query= String.format("INSERT INTO eat(productid,productname,quantity,price,discount) VALUES('%s','%s','%s','%s','%s');",

                order.getProductId(),
                order.getProductName(),
                order.getQuantity(),
                order.getPrice(),
                order.getDiscount()


        );


        db.execSQL(query);
    }

    public void cleanCart(){

        SQLiteDatabase db=getReadableDatabase();
        String query= String.format("DELETE FROM eat");
        db.execSQL(query);
    }
}


