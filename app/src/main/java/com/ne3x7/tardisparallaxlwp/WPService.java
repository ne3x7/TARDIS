package com.ne3x7.tardisparallaxlwp;

import android.graphics.Color;
import android.service.wallpaper.WallpaperService;
import android.view.SurfaceHolder;

import com.nvanbenschoten.motion.ParallaxImageView;

public class WPService extends WallpaperService{

    @Override
    public Engine onCreateEngine() {
        return new WPEngine();
    }

    private class WPEngine extends Engine {

        private int width;
        private int height;
        private ParallaxImageView img;
        public WPEngine() {
            img = new ParallaxImageView(getApplicationContext());
            img.setBackgroundColor(Color.BLUE);
            img.setMinimumWidth(100);
            img.setMinimumHeight(100);
            img.registerSensorManager();
            width = img.getWidth();
            height = img.getHeight();
        }

        @Override
        public void onSurfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            super.onSurfaceChanged(holder, format, width, height);
            this.width = width;
            this.height = height;
        }

        @Override
        public void onSurfaceDestroyed(SurfaceHolder holder) {
            super.onSurfaceDestroyed(holder);
            img.unregisterSensorManager();
        }

        @Override
        public void onVisibilityChanged(boolean visible) {
            super.onVisibilityChanged(visible);
        }
    }
}
