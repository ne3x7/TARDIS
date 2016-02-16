package com.ne3x7.tardisparallaxlwp;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.preference.Preference;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

public class SeekBarPreference extends Preference implements SeekBar.OnSeekBarChangeListener {
    private static final int maximum = 100;
    private static final int interval = 1;

    private TextView percentage;
    private float current = 45.0f;

    public SeekBarPreference(Context context) {
        super(context);
    }

    public SeekBarPreference(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public SeekBarPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * Dynamically creates preference layout. Some problems might occur with placement, can't be
     * seen until it at least starts, what it doesn't do. Maybe it's be easier to create a layout
     * using xml, but I don't know how to insert it here. findViewById() maybe?
     */
    @Override
    protected View onCreateView(ViewGroup parent) {
        super.onCreateView(parent);

        LinearLayout layout = new LinearLayout(getContext());

        LinearLayout.LayoutParams params_wrap = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params_wrap.gravity = Gravity.CENTER_HORIZONTAL;

        LinearLayout.LayoutParams params_match = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params_match.gravity = Gravity.CENTER_HORIZONTAL;

        layout.setPadding(10, 5, 10, 5);
        layout.setOrientation(LinearLayout.HORIZONTAL);

        TextView text = new TextView(getContext());
        text.setText(getTitle());
        text.setLayoutParams(params_wrap);

        SeekBar seek = new SeekBar(getContext());
        seek.setMax(maximum);
        seek.setProgress((int) current);
        seek.setLayoutParams(params_match);
        seek.setOnSeekBarChangeListener(this);

        percentage = new TextView(getContext());
        percentage.setText(seek.getProgress());

        layout.addView(text);
        layout.addView(seek);
        layout.addView(percentage);
        layout.setId(R.id.sbp);

        return layout;
    }

    /**
     * Changes the value of @current, which will be sent to TextView, and updates the preference
     */
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        progress = Math.round(((float)progress)/interval)*interval;

        if(!callChangeListener(progress)){
            seekBar.setProgress((int)current);
            return;
        }

        seekBar.setProgress(progress);
        current = progress;
        percentage.setText(progress);
        updatePreference(progress);

        notifyChanged();
    }

    /**
     * Puts the desired @val into some SharedPreference
     */
    private void updatePreference(int val) {
        // TODO Clean the mess with ints and floats all over the project
        SharedPreferences.Editor editor = getEditor();
        editor.putInt(getKey(), val);
        editor.commit();
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        // Do nothing
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        // Do nothing
    }

    /**
     * It was in the example
     */
    @Override
    protected void onSetInitialValue(boolean restorePersistedValue, Object defaultValue) {
        // TODO What is going on here and why?
        super.onSetInitialValue(restorePersistedValue, defaultValue);
        int temp = restorePersistedValue ? getPersistedInt(45) : (Integer)defaultValue;

        if(!restorePersistedValue)
            persistInt(temp);

        current = temp;
    }

    /**
     * It was in the example
     */
    @Override
    protected Object onGetDefaultValue(TypedArray a, int index) {
        // TODO What is going on here and why?
        super.onGetDefaultValue(a, index);
        int val = (int) a.getInt(index, 45);

        return validate(val);
    }

    /**
     * It was in the example and is used in onGetDefaultValue
     */
    private int validate(int value) {
        if(value > maximum)
            value = maximum;
        else if(value < 0)
            value = 0;
        else if(value % interval != 0)
            value = Math.round(((float)value)/interval)*interval;

        return value;
    }
}
