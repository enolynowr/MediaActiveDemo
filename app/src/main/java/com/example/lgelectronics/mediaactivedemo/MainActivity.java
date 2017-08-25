package com.example.lgelectronics.mediaactivedemo;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;

import static android.R.attr.tag;

public class MainActivity extends AppCompatActivity {
    //Banner Ads
    private AdView mAdView;
    private static String bn_ads = "ca-app-pub-4180035077241491/4498564479"; //Banner Ads End
    //ListView Text
    static final String[] music = {"fun", "enjoy", "exiting"};
    static TelephonyManager telephony;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        telephony=(TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);

        Log.d("이거다", "getDeviceId 이거다 : "+telephony.getDeviceId());




        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, music);

        ListView listView = (ListView) findViewById(R.id.listViewMain);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, ConnectScreen.class);
                intent.putExtra("main", position);

                startActivity(intent);
            }
        });

        //adMob Banner
        MobileAds.initialize(getApplicationContext(), bn_ads);
        mAdView = (AdView) findViewById(R.id.adView);

        AdRequest adRequest = new AdRequest.Builder().addTestDevice(telephony.getDeviceId()).build();
        mAdView.loadAd(adRequest);

    }



}
