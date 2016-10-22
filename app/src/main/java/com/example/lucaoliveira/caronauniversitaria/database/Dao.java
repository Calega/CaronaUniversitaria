package com.example.lucaoliveira.caronauniversitaria.database;

/**
 * Created by Lucas Calegari Alves de Oliveira on 27/09/2016.
 */
public interface Dao<T> {

    public void insert(T object);

    public void updateEmail(T object);

    public void updatePassword(T object);

    public void updateRegister(T object);

    public T getUserByEmail(String email);

}
