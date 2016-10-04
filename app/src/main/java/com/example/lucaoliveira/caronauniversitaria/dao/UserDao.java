package com.example.lucaoliveira.caronauniversitaria.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.lucaoliveira.caronauniversitaria.database.Dao;
import com.example.lucaoliveira.caronauniversitaria.database.DatabaseConnection;
import com.example.lucaoliveira.caronauniversitaria.model.User;

/**
 * Created by lucas calegari a. de oliveira on 27/09/2016.
 */
public class UserDao extends DatabaseConnection implements Dao<User> {
    public static final String TAG = "UserDao";

    public UserDao(Context context) {
        super(context);
    }

    @Override
    public void insert(User user) {
        ContentValues cv = new ContentValues();

        cv.put("id", user.getId());
        cv.put("name", user.getName());
        cv.put("email", user.getEmail());
        cv.put("password", user.getPassword());
        cv.put("phoneNumber", user.getPhoneNumber());
        cv.put("university", user.getUniversity());
        cv.put("accesstype", user.getAccessType());
        cv.put("addressorigin", user.getAddressOrigin());
        cv.put("addressdestiny", user.getAddressDestiny());
        cv.put("studentsAllowed", user.getNumberOfStudentsAllowed());
        cv.put("studentRegister", user.getStudentRegister());
        cv.put("studentImage", user.getImage());
        cv.put("valueForRent", user.getValueForRent());

        try {
            getWritableDatabase().insertWithOnConflict("users", "users.id", cv, SQLiteDatabase.CONFLICT_IGNORE);
        } catch (SQLiteConstraintException e) {
            Log.d(TAG, "User already exists in this database : " + SQLiteDatabase.CONFLICT_FAIL);
        }
    }

    @Override
    public void updateEmail(User user) {
        ContentValues cv = new ContentValues();

        String sWhere = "id = ?";
        String[] parametros = {String.valueOf(user.getId())};

        cv.put("email", user.getEmail());

        getWritableDatabase().update("users", cv, sWhere, parametros);
    }

    @Override
    public void updatePassword(User user) {
        ContentValues cv = new ContentValues();

        String sWhere = "id = ?";
        String[] parametros = {String.valueOf(user.getId())};

        cv.put("password", user.getPassword());

        getWritableDatabase().update("users", cv, sWhere, parametros);
    }

    @Override
    public void updateRegister(User user) {
        ContentValues cv = new ContentValues();

        String sWhere = "id = ?";
        String[] parametros = {String.valueOf(user.getId())};

        cv.put("name", user.getName());
        cv.put("phoneNumber", user.getPhoneNumber());
        cv.put("university", user.getUniversity());
        cv.put("accesstype", user.getAccessType());
        cv.put("addressorigin", user.getAddressOrigin());
        cv.put("addressdestiny", user.getAddressDestiny());
        cv.put("studentsAllowed", user.getNumberOfStudentsAllowed());
        cv.put("studentRegister", user.getStudentRegister());

        getWritableDatabase().update("users", cv, sWhere, parametros);
    }

    @Override
    public User getUserByEmail(String email) {
        User user = null;

        String[] parametros = {email};

        Cursor cursor = null;

        try {
            StringBuilder sb = new StringBuilder();
            sb.append("select * from users where email = ?");

            cursor = getWritableDatabase().rawQuery(sb.toString(), parametros);
            Log.d(TAG, "Database Query To Select User by Email  " + sb.toString() + "parameters" + parametros);

            while (cursor.moveToNext()) {
                user = new User();
                user.setId(cursor.getLong(cursor.getColumnIndex("id")));
                user.setName(cursor.getString(cursor.getColumnIndex("name")));
                user.setEmail(cursor.getString(cursor.getColumnIndex("email")));
                user.setPassword(cursor.getString(cursor.getColumnIndex("password")));
                if (!cursor.isNull(cursor.getColumnIndex("phoneNumber"))) {
                    user.setPhoneNumber(cursor.getString(cursor.getColumnIndex("phoneNumber")));
                }
                user.setUniversity(cursor.getString(cursor.getColumnIndex("university")));
                user.setAccessType(cursor.getString(cursor.getColumnIndex("accesstype")));
                user.setAddressOrigin(cursor.getString(cursor.getColumnIndex("addressorigin")));
                user.setAddressDestiny(cursor.getString(cursor.getColumnIndex("addressdestiny")));
                user.setNumberOfStudentsAllowed(cursor.getInt(cursor.getColumnIndex("studentsAllowed")));
                user.setValueForRent(cursor.getInt(cursor.getColumnIndex("valuerent")));
                if (!cursor.isNull(cursor.getColumnIndex("studentRegister"))) {
                    user.setStudentRegister(cursor.getString(cursor.getColumnIndex("studentRegister")));
                }
                if (!cursor.isNull(cursor.getColumnIndex("studentImage"))) {
                    user.setImage(cursor.getString(cursor.getColumnIndex("studentImage")));
                }
            }

        } catch (Exception e) {
            Log.e("UserDao", "Error retrieving user for email {} " + email);
            Log.e("UserDao", "Error retrieving user " + e.getMessage());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return user;
    }
}
