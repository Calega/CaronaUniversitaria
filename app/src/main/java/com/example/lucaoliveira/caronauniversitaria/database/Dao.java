package com.example.lucaoliveira.caronauniversitaria.database;

/**
 * Created by lucas on 27/09/2016.
 */
public interface Dao<T> {

    public static final String DATABASE = "/sdcard/DBase/users.db3";
    public static final int VERSION = 1;

    public void insert(T object);

    public void update(T object);

//    public void delete(long id);

    public T getUserById(long id);

//    public ArrayList<T> getUserList();
}
