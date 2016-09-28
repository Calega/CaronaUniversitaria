package com.example.lucaoliveira.caronauniversitaria.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.example.lucaoliveira.caronauniversitaria.database.Dao;
import com.example.lucaoliveira.caronauniversitaria.database.DatabaseConnection;
import com.example.lucaoliveira.caronauniversitaria.model.User;

/**
 * Created by lucas calegari a. de oliveira on 27/09/2016.
 */
public class UserDao extends DatabaseConnection implements Dao<User> {

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
//        cv.put("image", user.getThumbnail());

        getWritableDatabase().insert("users", null, cv);
    }

    @Override
    public void update(User user) {
        ContentValues cv = new ContentValues();

        String sWhere = "id = ?";
        String[] parametros = {String.valueOf(user.getId())};

        cv.put("email", user.getEmail());

        getWritableDatabase().update("users", cv, sWhere, parametros);
    }

    @Override
    public User getUserById(long id) {
        User user = null;

        String[] parametros = {String.valueOf(id)};

        Cursor cursor = null;

        try {
            StringBuilder sb = new StringBuilder();
            sb.append("select * from users where id = ?");

            cursor = getWritableDatabase().rawQuery(sb.toString(), parametros);

            while (cursor.moveToNext()) {
                user = new User();
                user.setId(cursor.getLong(cursor.getColumnIndex("id")));
                user.setName(cursor.getString(cursor.getColumnIndex("name")));
                user.setEmail(cursor.getString(cursor.getColumnIndex("email")));
                user.setPassword(cursor.getString(cursor.getColumnIndex("password")));
                user.setPhoneNumber(cursor.getString(cursor.getColumnIndex("phoneNumber")));
                user.setUniversity(cursor.getString(cursor.getColumnIndex("university")));
                user.setAccessType(cursor.getString(cursor.getColumnIndex("accesstype")));
                user.setUniversity(cursor.getString(cursor.getColumnIndex("university")));
                user.setAddressOrigin(cursor.getString(cursor.getColumnIndex("addressorigin")));
                user.setAddressDestiny(cursor.getString(cursor.getColumnIndex("addressdestiny")));
                user.setNumberOfStudentsAllowed(cursor.getInt(cursor.getColumnIndex("studentsAllowed")));
            }

        } catch (Exception e) {
            Log.e("UserDao", "Error retrieving user for id {} " + id);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return user;
    }

    @Override
    public User getUserByEmail(String email) {
        User user = null;

        String[] parametros = {email};

        Cursor cursor = null;

        try {
            StringBuilder sb = new StringBuilder();
            sb.append("select id, email from users where email = ?");

            cursor = getWritableDatabase().rawQuery(sb.toString(), parametros);

            while (cursor.moveToNext()) {
                user = new User();
                user.setId(cursor.getLong(cursor.getColumnIndex("id")));
                user.setEmail(cursor.getString(cursor.getColumnIndex("email")));
            }

        } catch (Exception e) {
            Log.e("UserDao", "Error retrieving user for email {} " + email);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return user;
    }
}
