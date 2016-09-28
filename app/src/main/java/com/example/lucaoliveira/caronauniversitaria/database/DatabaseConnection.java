package com.example.lucaoliveira.caronauniversitaria.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by lucas on 27/09/2016.
 */
public class DatabaseConnection extends SQLiteOpenHelper {
    public static final String DATABASE = "users.db3";
    public static final int VERSION = 1;

    public DatabaseConnection(Context context) {
        super(context, DatabaseConnection.DATABASE, null, DatabaseConnection.VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        StringBuilder sb = new StringBuilder();
        sb.append("CREATE TABLE IF NOT EXISTS [users] ([id] BIGINT NOT NULL, [name] NVARCHAR(200) NOT NULL, " +
                "[email] NVARCHAR(200) NOT NULL, [password] NVARCHAR(200) NOT NULL, [phoneNumber] NVARCHAR(50), " +
                "[university] NVARCHAR(200), [accesstype] NVARCHAR(20), [addressorigin] NVARCHAR(200), " +
                "[addressdestiny] NVARCHAR(200), [studentsAllowed] INT(11), " +
                "[image] BLOB, CONSTRAINT [] PRIMARY KEY ([id]));");

        db.execSQL(sb.toString().toLowerCase());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        StringBuilder sb = new StringBuilder();
        sb.append("DROP TABLE IF EXISTS users;");

        db.execSQL(sb.toString().toLowerCase());

        onCreate(db);
    }
}
