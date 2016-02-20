package com.ne3x7.tardisparallaxlwp;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.Preference;
import android.util.AttributeSet;
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

    public SeekBarPreference(Context context, AttributeSet attrs) {
        super(context, attrs);

        max = attrs.getAttributeIntValue("http://schemas.android.com/apk/res-auto", "max", 100);
        current = attrs.getAttributeIntValue("http://schemas.android.com/apk/res-auto", "current", 0);
    }

    @Override
    protected View onCreateView(ViewGroup parent) {
        super.onCreateView(parent);

        RelativeLayout layout = (RelativeLayout) LayoutInflater.from(getContext()).inflate(R.layout.seek_bar_preference_layout, null);

        TextView title = (TextView) layout.findViewById(R.id.title);
        title.setText(getTitle());

        SeekBar sb = (SeekBar) layout.findViewById(R.id.sb);
        sb.setMax(max);
        sb.setProgress(current);
        sb.setOnSeekBarChangeListener(this);

        tv = (TextView) layout.findViewById(R.id.value);
        tv.setText(current+""); // почему?

        return layout;
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        tv.setText(progress+"");
        tv.invalidate(); // что это?
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
        SharedPreferences.Editor ed = getEditor();
        ed.putInt(getKey(), val);
        ed.commit();
    }
}
