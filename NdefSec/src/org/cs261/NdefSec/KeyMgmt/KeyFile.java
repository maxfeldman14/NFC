package org.cs261.NdefSec.KeyMgmt;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class KeyFile
{
    /* Provides a hashmap-like interface to Android sharedPreferences */
    // get the following with getPreferences(MODE_PRIVATE)
    private SharedPreferences storage;

    public KeyFile(SharedPreferences prefs)
    {
        storage = prefs;
    }

    public String get(String key)
    {
        return storage.getString(key, null);
    }

    public void put(String key, String value)
    {
        SharedPreferences.Editor editor = storage.edit(); 
        editor.putString(key, value); 
        editor.commit();
    }

    public boolean containsKey(String key)
    {
        /* Unknown behavior if key is not contained */
        return storage.contains(key);
    }

    public void remove(String key)
    {
        SharedPreferences.Editor editor = storage.edit(); 
        editor.remove(key);
        editor.commit();
    }

}


