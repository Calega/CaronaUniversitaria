package com.example.lucaoliveira.caronauniversitaria.dao;

import android.content.ContentValues;
import android.content.Context;

import com.example.lucaoliveira.caronauniversitaria.data.User;
import com.example.lucaoliveira.caronauniversitaria.database.Dao;
import com.example.lucaoliveira.caronauniversitaria.database.DatabaseConnection;

import java.util.ArrayList;

/**
 * Created by lucas on 27/09/2016.
 */
public class UserDao extends DatabaseConnection implements Dao<User> {

    public UserDao(Context context) {
        super(context);
    }

    @Override
    public void insert(User user) {
        ContentValues cv = new ContentValues();
        //
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

        //cv.put("idcontato", object.getIdcontato());
//        cv.put("nome", user.getNome());
//        cv.put("telefone", user.getTelefone());
//        cv.put("idade", user.getIdade());

        getWritableDatabase().update("users", cv, sWhere, parametros);
    }

    @Override
    public void delete(long id) {

    }

    @Override
    public User getUserById(long id) {
        return null;
    }

    @Override
    public ArrayList<User> getUserList() {
        return null;
    }
}
