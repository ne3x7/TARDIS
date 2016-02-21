package com.ne3x7.tardisparallaxlwp;

import android.app.Activity;
import android.os.Bundle;
import android.preference.PreferenceFragment;

public class SettingsActivity extends Activity {

    private static final String TAG = "PERSONAL DEBUG DATA";
    public static final String INTENSITY_KEY = "intensity", TILT_KEY = "tilt";
    /**
     * Calls the fragment - it's the only way to avoid deprecated Activity-method addPreferencesFromResource
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction().replace(android.R.id.content,
                new MyPreferenceFragment()).commit();
    }

    public static class MyPreferenceFragment extends PreferenceFragment
    {
        @Override
        public void onCreate(final Bundle savedInstanceState)
        {
            super.onCreate(savedInstanceState);

            // Load preferences from an xml resource
            addPreferencesFromResource(R.xml.pref);
        }
    }
}
