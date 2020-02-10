package com.example.newsfeed.Fragments;


import android.os.Bundle;

import androidx.preference.PreferenceFragmentCompat;

import com.example.newsfeed.R;

public class SettingsFragment extends PreferenceFragmentCompat {
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preferences, rootKey);
    }
}
