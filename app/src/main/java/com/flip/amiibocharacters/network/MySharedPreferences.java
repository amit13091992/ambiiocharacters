package com.flip.amiibocharacters.network;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import com.flip.amiibocharacters.model.AmbiioCharacterModel;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Amit on 29-Mar-19.
 */
public class MySharedPreferences {

    private SharedPreferences preferences;

    public MySharedPreferences() {

    }

    public MySharedPreferences(Context appContext) {
        preferences = PreferenceManager.getDefaultSharedPreferences(appContext);
    }

    public void putListObject(String key, ArrayList<AmbiioCharacterModel> objArray) {
        checkForNullKey(key);
        Gson gson = new Gson();
        ArrayList<String> objStrings = new ArrayList<String>();
        for (AmbiioCharacterModel obj : objArray) {
            objStrings.add(gson.toJson(obj));
        }
        putListString(key, objStrings);
    }

    public ArrayList<AmbiioCharacterModel> getListObject(String key, Class<?> mClass) {
        Gson gson = new Gson();

        ArrayList<String> objStrings = getListString(key);
        ArrayList<AmbiioCharacterModel> objects = new ArrayList<AmbiioCharacterModel>();

        for (String jObjString : objStrings) {
            AmbiioCharacterModel value = (AmbiioCharacterModel) gson.fromJson(jObjString, mClass);
            objects.add(value);
        }
        return objects;
    }

    private void putListString(String key, ArrayList<String> stringList) {
        checkForNullKey(key);
        String[] myStringList = stringList.toArray(new String[stringList.size()]);
        preferences.edit().putString(key, TextUtils.join("‚‗‚", myStringList)).apply();
    }

    private ArrayList<String> getListString(String key) {
        return new ArrayList<String>(Arrays.asList(TextUtils.split(preferences.getString(key, ""), "‚‗‚")));
    }

    public void putBoolean(String key, boolean value) {
        checkForNullKey(key);
        preferences.edit().putBoolean(key, value).apply();
    }

    public boolean getBoolean(String key) {
        return preferences.getBoolean(key, false);
    }

    private void checkForNullKey(String key) {
        if (key == null) {
            throw new NullPointerException();
        }
    }

    public void checkForNullValue(String value) {
        if (value == null) {
            throw new NullPointerException();
        }
    }
}
