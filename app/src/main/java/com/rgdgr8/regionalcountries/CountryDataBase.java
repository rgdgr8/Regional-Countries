package com.rgdgr8.regionalcountries;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Country.class}, version = 1)
public abstract class CountryDataBase extends RoomDatabase {
    public abstract CountryDao getDao();

    private static CountryDataBase instance;
    public static CountryDataBase getDataBase(Context context){
        if(instance==null){
            instance = Room.databaseBuilder(context.getApplicationContext(),CountryDataBase.class,"CountryDB")
                    .build();
        }
        return instance;
    }
}
