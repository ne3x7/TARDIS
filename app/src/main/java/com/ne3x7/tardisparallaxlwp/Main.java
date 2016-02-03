package com.ne3x7.tardisparallaxlwp;

import android.app.WallpaperManager;
import android.content.ComponentName;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class Main extends AppCompatActivity implements AdapterView.OnItemClickListener {

    ListView preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        preferences = (ListView) findViewById(R.id.settings);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.items,
                android.R.layout.simple_list_item_1);

        preferences.setAdapter(adapter);

        preferences.setOnItemClickListener(this);
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (position) {
            case 0:
                Intent setWP = new Intent(WallpaperManager.ACTION_CHANGE_LIVE_WALLPAPER);
                setWP.putExtra(WallpaperManager.EXTRA_LIVE_WALLPAPER_COMPONENT, new ComponentName(this, WPService.class));
                startActivity(setWP);
                break;
        }
    }
}
