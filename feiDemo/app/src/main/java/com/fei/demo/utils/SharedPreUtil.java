package com.fei.demo.utils;

import android.content.Context;
import android.content.SharedPreferences;


public class SharedPreUtil {
    private static final String SP_NAME="V18S";
    public static final String IS_FIRST_START_APP="is_first_start_app";

    public static SharedPreferences getSharedPreferences(Context context){
        return context.getSharedPreferences(SP_NAME, 0);
    }

    public static void saveBoolean(Context context,String key, boolean value){
        SharedPreferences sharedPreferences = context.getSharedPreferences(SP_NAME, 0);
        sharedPreferences.edit().putBoolean(key, value).commit();
    }

    public static boolean getBoolean(Context context,String key){
        SharedPreferences sharedPreferences = context.getSharedPreferences(SP_NAME, 0);
        return sharedPreferences.getBoolean(key,false);
    }

    public static boolean getBoolean(Context context,String key,boolean defaultValue){
        SharedPreferences sharedPreferences = context.getSharedPreferences(SP_NAME, 0);
        return sharedPreferences.getBoolean(key,defaultValue);
    }

    public static void saveInt(Context context,String key, int value){
        SharedPreferences sharedPreferences = context.getSharedPreferences(SP_NAME, 0);
        sharedPreferences.edit().putInt(key, value).commit();
    }

    public static int getInt(Context context,String key){
        SharedPreferences sharedPreferences = context.getSharedPreferences(SP_NAME, 0);
        return sharedPreferences.getInt(key,0);
    }

    public static void saveString(Context context,String key, String value){
        SharedPreferences sharedPreferences = context.getSharedPreferences(SP_NAME, 0);
        sharedPreferences.edit().putString(key, value).commit();
    }

    public static String getString(Context context,String key){
        SharedPreferences sharedPreferences = context.getSharedPreferences(SP_NAME, 0);
        return sharedPreferences.getString(key,"");
    }

}
