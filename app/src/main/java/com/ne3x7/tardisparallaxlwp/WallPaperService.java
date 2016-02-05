package com.ne3x7.tardisparallaxlwp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.service.wallpaper.WallpaperService;
import android.view.SurfaceHolder;

import com.nvanbenschoten.motion.ParallaxImageView;

public class WallPaperService extends WallpaperService {
    @Override
    public Engine onCreateEngine() {
        return new WPEngine();
    }

    private class WPEngine extends Engine {

        private int width;
        private int height;
        private Paint paint = new Paint();
        private ParallaxImageView img;
        private boolean visible = false;
        public WPEngine() {
            img = new ParallaxImageView(getApplicationContext());
            img.setParallaxIntensity(2.5f);
            img.registerSensorManager();

            this.width = img.getWidth();
            this.height = img.getHeight();
        }

        @Override
        public void onSurfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            super.onSurfaceChanged(holder, format, width, height);

            //this.width = width;
            //this.height = height;

            Canvas c = holder.lockCanvas();
            img.layout(0, 0, this.width, this.height);
            img.setImageBitmap(BitmapFactory.decodeStream(getResources().
                    openRawResource(R.raw.large)));

            Bitmap b = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888); // магия
            Canvas canvas = new Canvas(b); // магия 2
            img.draw(canvas);
            c.drawBitmap(b, 0, 0, paint);
            holder.unlockCanvasAndPost(c);
        }

        @Override
        public void onSurfaceDestroyed(SurfaceHolder holder) {
            super.onSurfaceDestroyed(holder);
            // TODO
            img.unregisterSensorManager();
        }

        @Override
        public void onVisibilityChanged(boolean visible) {
            super.onVisibilityChanged(visible);
            // TODO
            if (visible) {
                this.visible = true;
                //img.registerSensorManager();
            } else {
                this.visible = false;
               // img.unregisterSensorManager();
            }
        }
    }
}
