package com.example.lgelectronics.mediaactivedemo;

/**
 * Created by LG Electronics on 2017/08/25.
 */

public class EnumBundle {

    public enum Title{頑張れ, 毎度, その調子}

    //Ads Key
    public enum AdmobKey{

        FULL_ADS_KEY("ca-app-pub-3940256099942544/1033173712"),
        BANNER_ADS_KEY("ca-app-pub-3940256099942544/6300978111");

        private String key;
        AdmobKey(String _key){
            key = _key;
        }
        public String getString(){
            return key;
        }
    }

    //Sound
    public enum ResSound{
        GANBARE(R.raw.ganbare1),
        MAIDO( R.raw.maidoarigatougozaimasu1),
        SONOTYOUSHI(R.raw.sonotyoushisonotyousi1);

        private int res_sound;
        ResSound(int _res){
            res_sound = _res;
        }
        public int getInt(){
            return res_sound;
        }
    }

    //Image
    public enum ResImage{

        GANBARE(R.drawable.android_1),
        MAIDO( R.drawable.android_2),
        SONOTYOUSHI(R.drawable.android_3);

        private int res_image;
        ResImage(int _res){
            res_image = _res;
        }
        public int getInt(){
            return res_image;
        }
    }
}
