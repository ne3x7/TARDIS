package com.ne3x7.tardisparallaxlwp;

import android.content.Context;
import android.content.res.TypedArray;
import android.preference.Preference;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

public class SeekBarPreference extends Preference implements SeekBar.OnSeekBarChangeListener {
    private TextView tv;
    private int current;
    private int max;
    private final String TAG = "PERSONAL DEBUG DATA";
    public final int DEFAULT_VALUE = 25;

    /**
     * Constructor gets max & current values from xml, because why not
     */
    public SeekBarPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        max = attrs.getAttributeIntValue("http://schemas.android.com/apk/res-auto", "max", 100);
        current = attrs.getAttributeIntValue("http://schemas.android.com/apk/res-auto", "current",
                DEFAULT_VALUE);
    }

    @Override
    protected View onCreateView(ViewGroup parent) {
        super.onCreateView(parent);

        // That's how you avoid dynamic layout programming
        RelativeLayout layout = (RelativeLayout) LayoutInflater.from(getContext()).
                inflate(R.layout.seek_bar_preference_layout, null);

        // This may be hardcoded straight in xml
        TextView title = (TextView) layout.findViewById(R.id.title);
        title.setText(getTitle());

        SeekBar sb = (SeekBar) layout.findViewById(R.id.sb);
        sb.setMax(max);
        sb.setProgress(current);
        sb.setOnSeekBarChangeListener(this);

        tv = (TextView) layout.findViewById(R.id.value);
        tv.setText(String.format("%d", current) + "%");

        return layout;
    }

    /**
     * Sets the initial value of the seekbar progress.
     * Sets the progress to the saved value, or to the default, if there is no saved value.
     * @param restorePersistedValue boolean, true if there is a saved value, false otherwise.
     * @param defaultValue Integer, the default value from xml file pref.xml.
     */
    @Override
    protected void onSetInitialValue(boolean restorePersistedValue, Object defaultValue) {
        if(restorePersistedValue){
            // restores the seekbar value from its SharedPreferences.
            current = this.getPersistedInt(DEFAULT_VALUE);
        }else{
            current = (Integer) defaultValue;
            persistInt(current);
        }
    }

    /*
        Method that must be implemented in order to set current to defaultValue in onSetInitialValue.
        returns the default value from xml file or DEFAULT_VALUE, if the value is not defined in xml.
     */
    @Override
    protected Object onGetDefaultValue(TypedArray a, int index) {
        return a.getInteger(index, DEFAULT_VALUE);
    }

    /**
     * Now the value is not changing all the time, only on release. But it can be changed by
     * inserting some code into this method.
     */
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        tv.setText(String.format("%d", progress) + "%");
        tv.invalidate();
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        // Do nothing
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        current = seekBar.getProgress();
        update(current);
        notifyChanged();
    }

    private void update(int val) {
        // Saving value to this preference`s SharedPreferences
        persistInt(val);
        Log.d(TAG, "Stored: " + val);
    }
}
