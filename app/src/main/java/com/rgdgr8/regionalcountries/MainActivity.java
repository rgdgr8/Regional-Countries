package com.rgdgr8.regionalcountries;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentManager fm = getSupportFragmentManager();
        Mainfragment fragment = (Mainfragment) fm.findFragmentById(R.id.frame);
        if (fragment==null){
            fragment = new Mainfragment();
            fm.beginTransaction().add(R.id.frame,fragment).commit();
        }
    }
}