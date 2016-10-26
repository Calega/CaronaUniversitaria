package com.lucaoliveira.unicaroneiro.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Lucas Calegari A. de Oliveira on 27/09/2016.
 */
public class DatabaseConnection extends SQLiteOpenHelper {
    public static final String TAG = "DatabaseConnection";
    public static final String DATABASE = "users.db3";
    public static final int VERSION = 1;

    public DatabaseConnection(Context context) {
        super(context, DatabaseConnection.DATABASE, null, DatabaseConnection.VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        StringBuilder sb = new StringBuilder();
        sb.append("CREATE TABLE IF NOT EXISTS [users] ([id] BIGINT NOT NULL, [name] NVARCHAR(200) NOT NULL, " +
                "[email] NVARCHAR(200) NOT NULL, [password] NVARCHAR(200) NOT NULL, [phoneNumber] NVARCHAR(200), " +
                "[university] NVARCHAR(200) NOT NULL, [accesstype] NVARCHAR(20) NOT NULL, [addressorigin] NVARCHAR(200) NOT NULL, " +
                "[addressdestiny] NVARCHAR(200) NOT NULL, [studentsAllowed] INT(11), [studentRegister] NVARCHAR(200), [studentImage] BLOB, " +
                "[valueForRent] NVARCHAR(200)," +
                "CONSTRAINT [] PRIMARY KEY ([id]));");

        Log.d(TAG, "Database Query To Create " + sb.toString().toLowerCase());

        db.execSQL(sb.toString().toLowerCase());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        StringBuilder sb = new StringBuilder();
        sb.append("DROP TABLE IF EXISTS users;");

        Log.d(TAG, "Database Query To Drop " + sb.toString().toLowerCase());
        db.execSQL(sb.toString().toLowerCase());

        onCreate(db);
    }
}
