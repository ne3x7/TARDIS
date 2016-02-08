package com.ne3x7.tardisparallaxlwp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Handler;
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

        // Instance of class Handler that the queue of processes, used to start the Runnable object
        // and to make it call itself indefinitely.
        private final Handler handler = new Handler();
        // A thread (or not thread?) object, the only purpose - start a new thread that will redraw
        // the wallpaper all the time. Probably makes the program super inefficient.
        private final Runnable loadRunner = new Runnable() {
            @Override
            public void run() {
                Thread d = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        refreshDraw();
                    }
                });
                d.start();
            }
        };
        public WPEngine() {
            // Initializing and setting up the parallax image.
            handler.post(loadRunner);
            img = new ParallaxImageView(getApplicationContext());
            img.setParallaxIntensity(2.5f);
            img.registerSensorManager();
        }

        /**
         * Draws the wallpaper image on the wallpaper canvas.
         */
        private void draw(){
            // Debugging comment
            System.out.println("Drawing image");
            // TODO Scale the image, so it has the dimensions of the device screen.
            SurfaceHolder surfaceHolder = getSurfaceHolder();
            Canvas c = surfaceHolder.lockCanvas();
            img.layout(0, 0, c.getHeight(), c.getWidth());
            img.setImageBitmap(BitmapFactory.decodeStream(getResources().
                    openRawResource(R.raw.large)));
            // Creating bitmap with specified width, height and configuration. The last argument
            // is configuration, it just specifies what method to use when creating Bitmap.
            Bitmap b = Bitmap.createBitmap(c.getHeight(), c.getWidth(), Bitmap.Config.ARGB_8888);

            // I don`t know these 2 lines do.
            Canvas canvas = new Canvas(b);
            img.draw(canvas);

            c.drawBitmap(b, 0, 0, paint);

            // TODO figure out why it throws exceptions.
            // For some reason, sometimes the program executes this line when canvas is already
            // released and some other exception may occur.
            try {
                surfaceHolder.unlockCanvasAndPost(c);
            }
            catch(Exception e){
                // Just do nothing. At least for now.
            }
        }

        private void refreshDraw(){
            draw();
            handler.removeCallbacks(loadRunner);
            if(visible)
                handler.post(loadRunner);
        }

        @Override
        public void onSurfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            this.width = width;
            this.height = height;
            super.onSurfaceChanged(holder, format, width, height);
            draw();
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
                img.registerSensorManager();
                handler.post(loadRunner);
            } else {
                this.visible = false;
                img.unregisterSensorManager();
            }
        }
    }
}
