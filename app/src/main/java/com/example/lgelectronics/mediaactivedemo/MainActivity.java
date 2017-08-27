package com.example.lgelectronics.mediaactivedemo;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
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
import com.google.android.gms.ads.MobileAds;

public class MainActivity extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback {
    public static String LOG_TAG = "MEDIA_ACTIVE_DEMO_LOG::";
    private String bn_ads = EnumBundle.AdmobKey.BANNER_ADS_KEY.getString(); //Banner Ads key
    //ListView Text
    private String[] music = {
            EnumBundle.Title.valueOf("頑張れ").name(),
            EnumBundle.Title.valueOf("毎度").name(),
            EnumBundle.Title.valueOf("その調子").name()
    };
    //Banner AdsView
    private AdView mAdView;
    private TelephonyManager telephony;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        int permissionCheck = ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_PHONE_STATE);

        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.READ_PHONE_STATE}, 1);
        } else {
            //TODO
        }


        //Detect Device ID
        telephony = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        Log.d(LOG_TAG, "getDeviceId : " + telephony.getDeviceId());

        //Setting ListView
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, music);
        ListView listView = (ListView) findViewById(R.id.listViewMain);
        listView.setAdapter(adapter);

        //ListView OnclickListener
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d(LOG_TAG, "Selected Position : " + position);
                showDialLog(position);
            }
        });

        //adMob Banner
        MobileAds.initialize(getApplicationContext(), bn_ads);
        mAdView = (AdView) findViewById(R.id.adView);

        AdRequest adRequest = new AdRequest.Builder().addTestDevice(telephony.getDeviceId()).build();
        mAdView.loadAd(adRequest);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1:
                if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    //TODO
                    Toast.makeText(getApplicationContext(), "READ_PHONE_STATE-PERMISSION_GRANTED", Toast.LENGTH_LONG).show();
                }
                break;

            default:
                break;
        }
    }

    //Setting Dialog
    private void showDialLog(final int _position) {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle(this.getResources().getString(R.string.main_dialog_title));
        alertDialogBuilder.setMessage(this.getResources().getString(R.string.main_dialog_message));
        //Positive Button
        alertDialogBuilder.setPositiveButton(this.getResources().getString(R.string.dialog_positive), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(MainActivity.this, ConnectScreenActivity.class);
                intent.putExtra("main", _position);
                startActivity(intent);
            }
        });
        //Negative Button
        alertDialogBuilder.setNegativeButton(this.getResources().getString(R.string.dialog_negative), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alertDialogBuilder.show();
    }
}
