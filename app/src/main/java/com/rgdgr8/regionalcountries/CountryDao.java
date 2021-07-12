package com.rgdgr8.regionalcountries;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface CountryDao {
    @Insert
    void insertAll(List<Country> countries);

    @Query("SELECT * FROM countries")
    List<Country> getAll();

    @Query("DELETE FROM countries")
    void deleteAll();
}
