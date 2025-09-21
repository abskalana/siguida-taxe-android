package com.gouandiaka.market;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.gouandiaka.market.entity.Entity;
import com.gouandiaka.market.entity.EntityResponse;
import com.gouandiaka.market.entity.Paiement;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class LocalDatabase extends SQLiteOpenHelper {

    private static final String DB_NAME = "db_bamako_express";


    private static final int DB_VERSION = 1;
    private static LocalDatabase instance;
    private final SQLiteDatabase db;

    public static LocalDatabase instance() {
        return instance;
    }

    public static void init(Context context) {
        if (instance == null) {
            instance = new LocalDatabase(context);
        }
    }

    private LocalDatabase(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        db = getWritableDatabase();
        create(db);
    }

    private void create(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS table_model (data TEXT, time INTEGER );");
        db.execSQL("CREATE TABLE IF NOT EXISTS table_remote_model (data TEXT, time INTEGER );");
        db.execSQL("CREATE TABLE IF NOT EXISTS table_paiement_model (data TEXT, time INTEGER );");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        create(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public void addEntity(Entity model) {
        db.delete("table_model", null, null);
        ContentValues values = new ContentValues();
        values.put("time", System.currentTimeMillis());
        values.put("data", new Gson().toJson(model));
        db.insertWithOnConflict("table_model", null, values, SQLiteDatabase.CONFLICT_REPLACE);
    }


    public synchronized void clearLocaleTraffic() {
        db.delete("table_model", null, null);
    }

    public String getModel() {
        Cursor cursor = db.query("table_model", null, null, null, null, null, null, null);
        List<Entity> response = new ArrayList<>();
        while (cursor.moveToNext()) {
            String data = cursor.getString(0);
            Entity model = new Gson().fromJson(data, Entity.class);
            response.add(model);
        }
        if (response.isEmpty()) return null;
        cursor.close();
        Gson gson = new Gson();
        return gson.toJson(response);
    }

    public EntityResponse getRemoteModel() {
        Cursor cursor = db.query("table_remote_model", null, null, null, null, null, null, null);
        List<Entity> response = new ArrayList<>();
        if (cursor.moveToNext()) {
            String data = cursor.getString(0);
            Type listType = new TypeToken<List<Entity>>() {
            }.getType();
            response = new Gson().fromJson(data, listType);
        }

        cursor.close();
        return new EntityResponse(response);
    }

    public void addRemoveEntity(List<Entity> model) {
        db.delete("table_remote_model", null, null);
        ContentValues values = new ContentValues();
        values.put("time", System.currentTimeMillis());
        values.put("data", new Gson().toJson(model));
        db.insertWithOnConflict("table_remote_model", null, values, SQLiteDatabase.CONFLICT_REPLACE);
    }

    public void savePaiement(Paiement paiement) {
        db.delete("table_paiement_model", null, null);
        ContentValues values = new ContentValues();
        values.put("time", System.currentTimeMillis());
        values.put("data", new Gson().toJson(paiement));
        db.insertWithOnConflict("table_paiement_model", null, values, SQLiteDatabase.CONFLICT_REPLACE);

    }

    public String getPaiement() {
        Cursor cursor = db.query("table_paiement_model", null, null, null, null, null, null, null);
        List<Paiement> response = new ArrayList<>();
        while (cursor.moveToNext()) {
            String data = cursor.getString(0);
            Paiement model = new Gson().fromJson(data, Paiement.class);
            response.add(model);
        }
        if(response.isEmpty()) return null;
        cursor.close();
        Gson gson = new Gson();
        return gson.toJson(response);
    }

    public void clearPaiement() {


    }
}
