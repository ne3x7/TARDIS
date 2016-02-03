package com.ne3x7.tardisparallaxlwp;

import android.service.wallpaper.WallpaperService;
import android.view.SurfaceHolder;

public class WPService extends WallpaperService{

    @Override
    public Engine onCreateEngine() {
        return new WPEngine();
    }

    private class WPEngine extends Engine {

        private int width;
        private int height;

        public WPEngine() {

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
        }

        @Override
        public void onVisibilityChanged(boolean visible) {
            super.onVisibilityChanged(visible);
        }
    }
}
