package com.huntersdevs.www.gmessngr.app;

import android.content.Context;
import android.content.SharedPreferences;

public class PrefManager {

    private SharedPreferences pref;
    private static PrefManager instance  = null;
    private SharedPreferences.Editor editor;

    private static final String PREF_NAME = "GMessngrPref";

    private static final String KEY_IS_FIRST_TIME = "isFirstTime";
    private static final String KEY_IS_PROFILE_SETUPED = "isProfileSetuped";

    private static final String KEY_PHONE_NUMBER = "phoneNumber";
    private static final String KEY_PROFILE_NAME = "profileName";

    private PrefManager(Context context) {
        int PRIVATE_MODE = 0;
        pref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public static synchronized PrefManager getInstance(Context context){
        if (null == instance) instance = new PrefManager(context);
        return instance;
    }

    public void setIsFirstTime(boolean isFirstTime){
        editor.putBoolean(KEY_IS_FIRST_TIME, isFirstTime);
        editor.commit();
    }

    public boolean getIsFirstTime(){
        return pref.getBoolean(KEY_IS_FIRST_TIME, true);
    }

    public void setIsProfileSetuped(boolean isProfileSetuped){
        editor.putBoolean(KEY_IS_PROFILE_SETUPED, isProfileSetuped);
        editor.commit();
    }

    public boolean getIsProfileSetuped(){
        return pref.getBoolean(KEY_IS_PROFILE_SETUPED, false);
    }

    public void setPhoneNumber(String phoneNumber){
        editor.putString(KEY_PHONE_NUMBER, phoneNumber);
        editor.commit();
    }

    public String getPhoneNumber(){
        return pref.getString(KEY_PHONE_NUMBER, "");
    }

    public void setProfileName(String profileName){
        editor.putString(KEY_PROFILE_NAME, profileName);
        editor.commit();
    }

    public String getProfileName(){
        return pref.getString(KEY_PROFILE_NAME, "");
    }

}
