package com.ne3x7.tardisparallaxlwp;

import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;

public class SettingsActivity extends PreferenceActivity implements Preference.OnPreferenceChangeListener {
    public static final String SEEK_BAR_KEY = "seekBarKey";
    private Preference sbp;

    /**
     * Connects a layout to the Activity and sets a listener
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref);
        sbp = findPreference(SEEK_BAR_KEY);
        sbp.setOnPreferenceChangeListener(this);
    }

    /**
     * Listener
     */
    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        if (SettingsActivity.SEEK_BAR_KEY.equals(preference.getKey())) {
            int val = (Integer) newValue;
        }

        // TODO What does it do?

        return false;
    }
}
