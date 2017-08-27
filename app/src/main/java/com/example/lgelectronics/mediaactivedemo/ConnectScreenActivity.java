package com.example.lgelectronics.mediaactivedemo;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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

public class ConnectScreenActivity extends AppCompatActivity {
    //Full ads
    private InterstitialAd mInterstitialAd;
    private Button connect;
    private Button disconnect;
    private ImageView iv_dial;
    private MediaPlayer mediaPlayer;
    private TelephonyManager telephony;

    private int mPosition;
    private boolean mediaPauseFlag = false;
    private int mediaPauseLength = 0;

    //Image　Source
    private int[] res = {
            EnumBundle.ResImage.GANBARE.getInt(),
            EnumBundle.ResImage.MAIDO.getInt(),
            EnumBundle.ResImage.SONOTYOUSHI.getInt()
    };
    //Sound　Source
    private int[] res_sound = {
            EnumBundle.ResSound.GANBARE.getInt(),
            EnumBundle.ResSound.MAIDO.getInt(),
            EnumBundle.ResSound.SONOTYOUSHI.getInt()
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect_screen);
        telephony=(TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        //Admob Initial and Load
        mInterstitialAd = new InterstitialAd(this);

        mInterstitialAd.setAdUnitId(EnumBundle.AdmobKey.FULL_ADS_KEY.getString());
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

        //setImage
        setImage(mPosition);
        //setMediaPlayer
        setMediaPlayer();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(mediaPauseFlag){
            mediaPauseFlag = false;
            mediaPlayer.seekTo(mediaPauseLength);
            mediaPlayer.start();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(mediaPlayer.isPlaying()){
            mediaPauseFlag = true;
            mediaPlayer.pause();
            mediaPauseLength = mediaPlayer.getCurrentPosition();
        }
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
                ConnectScreenActivity.this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //Dialog
    private void showDialLog() {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle(this.getResources().getString(R.string.contents_dialog_title));
        alertDialogBuilder.setMessage(this.getResources().getString(R.string.contents_dialog_message));
        alertDialogBuilder.setPositiveButton(this.getResources().getString(R.string.dialog_positive), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // mediaPlayer start and looping
                mediaPlayer.start();
                mediaPlayer.setLooping(true);
            }
        });
        // Cancel 버튼 이벤트
        alertDialogBuilder.setNegativeButton(this.getResources().getString(R.string.dialog_negative), new DialogInterface.OnClickListener() {
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
                    Log.d(MainActivity.LOG_TAG, "Show Full Ads.");
                    mediaPlayer.stop();
                    mInterstitialAd.show();

                    mInterstitialAd.setAdListener(new AdListener() {
                        @Override
                        public void onAdClosed() {
                            super.onAdClosed();
                            Log.d(MainActivity.LOG_TAG, "Close Full Ads.");
                            mInterstitialAd.loadAd(new AdRequest.Builder().build());

                            finish();
                            return;
                        }
                    });

                } else {
                    Toast.makeText(getApplicationContext(), ConnectScreenActivity.this.getResources().getString(R.string.toast_alert),
                            Toast.LENGTH_LONG).show();
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

