package com.rgdgr8.regionalcountries;

import android.net.Uri;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class DataFetcher {
    private static final String TAG = "DataFetcher";
    private static List<Country> countries = new ArrayList<>();
    private static final String REGION_ENDPOINT = "https://restcountries.eu/rest/v2/region/";
    private static final String BORDERS = "borders";
    private static final String LANGUAGES = "languages";

    public static List<Country> getCountries() {
        return countries;
    }

    public static void getData(String region) throws IOException, JSONException {
        String urlString = REGION_ENDPOINT + region;
        Log.i(TAG, "url: " + urlString);

        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        InputStream input = connection.getInputStream();
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        byte[] buffer = new byte[2048];
        int bytesRead = 0;
        while ((bytesRead = input.read(buffer)) > 0) {
            output.write(buffer, 0, bytesRead);
        }
        output.close();
        connection.disconnect();

        String jsonData = new String(output.toByteArray());
        Log.i(TAG, "getData: " + jsonData);

        JSONArray objectArray = new JSONArray(jsonData);
        for (int i = 0; i < objectArray.length(); i++) {
            JSONObject countryObject = objectArray.getJSONObject(i);
            String name = countryObject.getString("name");
            String capital = countryObject.getString("capital");
            String flag_url = countryObject.getString("flag");
            region = countryObject.getString("region");
            String subRegion = countryObject.getString("subregion");
            String population = countryObject.getString("population");
            String borders = getStringFromJSONArray(BORDERS, countryObject);
            String languages = getStringFromJSONArray(LANGUAGES, countryObject);

            Country country = new Country(name,capital,flag_url,region,subRegion,population,borders,languages);
            countries.add(country);
        }
    }

    private static String getStringFromJSONArray(String query, JSONObject countryObject) throws JSONException {
        JSONArray jsonArray = countryObject.getJSONArray(query);
        StringBuilder builder = new StringBuilder("");
        for (int j = 0; j < jsonArray.length(); j++) {
            if (query.equals(BORDERS))
                builder.append(jsonArray.getString(j));
            else {
                JSONObject languageObject = jsonArray.getJSONObject(j);
                builder.append(languageObject.getString("name"));
            }

            if(j==(jsonArray.length()-1)) break;
            builder.append(", ");
        }

        return builder.toString();
    }
}