package com.example.lucaoliveira.caronauniversitaria.database;

/**
 * Created by lucas on 27/09/2016.
 */
public interface Dao<T> {

    public void insert(T object);

    public void update(T object);

    public T getUserById(long id);

    public T getUserByEmail(String email);

}
