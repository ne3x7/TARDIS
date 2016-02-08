package com.ne3x7.tardisparallaxlwp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Handler;
import android.service.wallpaper.WallpaperService;
import android.util.Log;
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
        private final String TAG = "PERSONAL DEBUG DATA";
        private boolean visible = false;

        // Instance of class Handler that the queue of processes, used to start the Runnable object
        // and to make it call itself indefinitely.
        private final Handler handler = new Handler();

        // A to-be-thread object, the only purpose - start a new thread that will redraw
        // the wallpaper all the time. Probably makes the program super inefficient.
        private final Runnable loadRunner = new Runnable() {
            @Override
            public void run() {
                Thread draw_thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        refreshDraw();
                    }
                });
                draw_thread.start();
            }
        };

        /*
        Constructor for Engine
         */
        public WPEngine() {
            Log.d(TAG, "Initializing Engine");
            img = new ParallaxImageView(getApplicationContext());
            img.setParallaxIntensity(2.5f);
            img.registerSensorManager();
            handler.post(loadRunner);
        }

        /**
         * Draws the wallpaper image on the wallpaper canvas
         * No log here because it's an infinite loop
         */
        private void draw(){
            Canvas c = null;
            SurfaceHolder surfaceHolder = getSurfaceHolder();

            try {
                // Get canvas
                c = surfaceHolder.lockCanvas();

                // Get screen params
                // TODO It failed once, investigate why
                if (c != null) {
                    width = c.getWidth();
                    height = c.getHeight();
                }

                // Apply screen params to parallax image view
                img.layout(0, 0, width, height);
                img.setImageBitmap(BitmapFactory.decodeStream(getResources().
                        openRawResource(R.raw.large)));

                // Create bitmap with specified width, height and configuration. The last argument
                // specifies drawing method to use when creating Bitmap, ARGB_8888 uses color black
                // instead of transparent
                Bitmap b = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

                // Get the image canvas and draw it
                Canvas canvas = new Canvas(b);
                img.draw(canvas);

                // Draw Bitmap on canvas
                if (c != null) {
                    c.drawBitmap(b, 0, 0, paint);
                }
            } catch (NullPointerException e) {
                Log.d(TAG, "Canvas or bitmap do not exist", e);
            }

            try {
                surfaceHolder.unlockCanvasAndPost(c);
            }
            catch(IllegalStateException e){
                Log.d(TAG, "Surface destroyed, but thread is still running", e);
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
            Log.d(TAG, "SurfaceChanged");
            this.width = width;
            this.height = height;
            super.onSurfaceChanged(holder, format, this.width, this.height);
            draw();
            // TODO Why don't we removeCallbacks?
        }

        @Override
        public void onSurfaceDestroyed(SurfaceHolder holder) {
            Log.d(TAG, "SurfaceDestroyed");

            super.onSurfaceDestroyed(holder);

            handler.removeCallbacks(loadRunner);

            img.unregisterSensorManager();
        }

        @Override
        public void onVisibilityChanged(boolean visible) {
            Log.d(TAG, "VisibilityChanged from " + this.visible + " to " + visible);
            super.onVisibilityChanged(visible);
            if (visible) {
                this.visible = true;
                img.registerSensorManager();
                handler.post(loadRunner);
            } else {
                this.visible = false;
                handler.removeCallbacks(loadRunner);
                img.unregisterSensorManager();
            }
        }
    }
}
