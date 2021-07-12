package com.rgdgr8.regionalcountries;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "countries")
public class Country {
    @PrimaryKey(autoGenerate = true)
    private int id;

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    private String name;
    private String capital;
    private String flag_url;
    private String region;
    private String subRegion;
    private String population;
    private String borders;
    private String languages;

    public Country(String name, String capital, String flag_url, String region, String subRegion, String population, String borders, String languages) {
        this.name = name;
        this.capital = capital;
        this.flag_url = flag_url;
        this.region = region;
        this.subRegion = subRegion;
        this.population = population;
        this.borders = borders;
        this.languages = languages;
    }

    public String getName() {
        return name;
    }

    public String getCapital() {
        return capital;
    }

    public String getFlag_url() {
        return flag_url;
    }

    public String getRegion() {
        return region;
    }

    public String getSubRegion() {
        return subRegion;
    }

    public String getPopulation() {
        return population;
    }

    public String getBorders() {
        return borders;
    }

    public String getLanguages() {
        return languages;
    }
}
