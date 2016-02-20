package com.ne3x7.tardisparallaxlwp;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.util.Log;

public class SettingsActivity extends PreferenceActivity {

    private static final String TAG = "PERSONAL DEBUG DATA";

    /**
     * Connects a layout to the Activity and sets a listener
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            getFragmentManager().beginTransaction().replace(android.R.id.content, new MyPreferenceFragment()).commit();
        } catch (Exception e) {
            Log.d(TAG, "Exception occured in onCreate", e);
        }
    }

    public static class MyPreferenceFragment extends PreferenceFragment
    {
        @Override
        public void onCreate(final Bundle savedInstanceState)
        {
            super.onCreate(savedInstanceState);
            try {
                addPreferencesFromResource(R.xml.pref);
            } catch (Exception e) {
                Log.d(TAG, "Exception occured in fragment", e);
            }
        }
    }
}
