package com.ne3x7.tardisparallaxlwp;

import android.app.WallpaperManager;
import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.nvanbenschoten.motion.ParallaxImageView;

import java.io.InputStream;

public class Main extends AppCompatActivity implements View.OnClickListener {

    private Button btn;
    private ParallaxImageView view;
    private InputStream is;
    private Bitmap img;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn = (Button) findViewById(R.id.btn);
        view = (ParallaxImageView) findViewById(R.id.test);

        is = getApplicationContext().getResources().openRawResource(R.raw.large);
        img = BitmapFactory.decodeStream(is);

        view.setImageBitmap(img);
        view.setParallaxIntensity(2.5f);

        btn.setOnClickListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();

        view.unregisterSensorManager();
    }

    @Override
    protected void onResume() {
        super.onResume();

        view.registerSensorManager();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn:
                Intent intent = new Intent();
                intent.setAction(WallpaperManager.ACTION_CHANGE_LIVE_WALLPAPER);
                intent.putExtra(WallpaperManager.EXTRA_LIVE_WALLPAPER_COMPONENT,
                        new ComponentName(this, WallPaperService.class));
                startActivity(intent);
                break;
            default:
                break;
        }
    }
}
