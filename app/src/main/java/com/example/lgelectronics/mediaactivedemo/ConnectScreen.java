package com.example.lgelectronics.mediaactivedemo;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.media.MediaPlayer;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

public class ConnectScreen extends AppCompatActivity {
    //Full ads
    private InterstitialAd mInterstitialAd;
    //Image　Source
    int[] res = {R.drawable.android_1, R.drawable.android_2, R.drawable.android_3};
    //Sound　Source
    int[] res_sound = {R.raw.ganbare1, R.raw.maidoarigatougozaimasu1, R.raw.sonotyoushisonotyousi1};

    Button connect;
    Button disconnect;
    ImageView iv_dial;
    MediaPlayer mediaPlayer;
    int mPosition;
    TelephonyManager telephony;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect_screen);
        telephony=(TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        //Admob Initial and Load
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");
        mInterstitialAd.loadAd(new AdRequest.Builder().addTestDevice(telephony.getDeviceId()).build());//load
        // button
        connect = (Button) findViewById(R.id.btn_connect);
        disconnect = (Button) findViewById(R.id.btn_disconnect);
        //imageview
        iv_dial = (ImageView) findViewById(R.id.iv_dialling);
        //get intent of Main
        Intent intent = getIntent();
        this.mPosition = intent.getExtras().getInt("main", 0);
        //mediaplayer
        mediaPlayer = MediaPlayer.create(getApplicationContext(), res_sound[mPosition]);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //setImage
        setImage(mPosition);
        //setMediaPlayer
        setMediaPlayer();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override//menu
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.actionbar_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override// menu listener
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.action_home:
                ConnectScreen.this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //Dialog
    public void showDialLog() {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("音楽の再生");
        alertDialogBuilder.setMessage("音楽を再生しますか？");
        alertDialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // mediaPlayer start and looping
                mediaPlayer.start();
                mediaPlayer.setLooping(true);
            }
        });
        // Cancel 버튼 이벤트
        alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alertDialogBuilder.show();
    }

    public void setMediaPlayer() {
        //音楽再生
        connect.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialLog();
            }
        });
        //Listviewに戻る
        disconnect.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.stop();
                    mInterstitialAd.show();

                    mInterstitialAd.setAdListener(new AdListener() {
                        @Override
                        public void onAdClosed() {
                            super.onAdClosed();
                            mInterstitialAd.loadAd(new AdRequest.Builder().build());

                            return;
                        }
                    });

                } else {
                    Toast.makeText(getApplicationContext(), "Please Press The Connect Button !", Toast.LENGTH_LONG).show();

                    return;
                }
                // ConnectScreen.this.finish();
            }//disconnect onclick end
        });

    }

    public void setImage(int _mPosition) {
        iv_dial.setImageResource(res[_mPosition]);
    }
}

