package com.team11.hackernews.settings;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import com.team11.hackernews.R;

public class SettingsFragment extends PreferenceFragment {

    public static SettingsFragment newInstance() {
        SettingsFragment fragment = new SettingsFragment();
        return fragment;
    }

    public SettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.preferences);
    }

}
