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

        int permissionCheck = ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_PHONE_STATE);
        //Permission Check
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.READ_PHONE_STATE}, 1);
        } else {
            loadAd();
        }

    }//onCreate

    @Override//Permission Result
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1://Receive Permission Load Ads
                if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    loadAd();
                } else {
                    //Toast.makeText(getApplicationContext(), "PERMISSION_NOT_ALLOWED", Toast.LENGTH_LONG).show();
                    showKillAppDialLog();
                }
                break;//case 1
            default:
                break;
        }
    }

    //Setting Dialog Of Playing
    private void showDialLog(final int _position) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle(this.getResources().getString(R.string.main_dialog_title));
        alertDialogBuilder.setMessage(this.getResources().getString(R.string.main_dialog_message));
        //Positive Button Of Playing
        alertDialogBuilder.setPositiveButton(this.getResources().getString(R.string.dialog_positive), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(MainActivity.this, ConnectScreenActivity.class);
                intent.putExtra("main", _position);
                startActivity(intent);
            }
        });
        //Negative Button Of Playing
        alertDialogBuilder.setNegativeButton(this.getResources().getString(R.string.dialog_negative), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alertDialogBuilder.show();
    }
    //Load Ads
    private void loadAd() {
        //Detect Device ID
        telephony = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        Log.d(LOG_TAG, "getDeviceId : " + telephony.getDeviceId());

        AdRequest adRequest = new AdRequest.Builder().addTestDevice(telephony.getDeviceId()).build();
        mAdView.loadAd(adRequest);
    }

    //Setting Dialog Of Permission Diallog
    private void showKillAppDialLog() {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle(this.getResources().getString(R.string.permission_denied));
        alertDialogBuilder.setMessage(this.getResources().getString(R.string.permission_denied_message));
        //Positive Button
        alertDialogBuilder.setPositiveButton(this.getResources().getString(R.string.dialog_positive), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                killApp();
            }
        });
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.show();
    }

    private void killApp() {
        moveTaskToBack(true);
        finish();
        android.os.Process.killProcess(android.os.Process.myPid());
    }

}
