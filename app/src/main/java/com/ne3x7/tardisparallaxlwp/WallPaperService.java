package com.ne3x7.tardisparallaxlwp;

import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.service.wallpaper.WallpaperService;
import android.util.Log;
import android.view.SurfaceHolder;

import com.nvanbenschoten.motion.ParallaxImageView;

public class WallPaperService extends WallpaperService {
    @Override
    public Engine onCreateEngine() {
        return new WPEngine();
    }

    public class WPEngine extends Engine{

        private int width;
        private int height;
        private float intensity = 1.1f;
        private ParallaxImageView img;
        private final String TAG = "PERSONAL DEBUG DATA";
        private static final float min = 1.1f;
        private static final float max = 2.5f;
        private static final float delta = (max - min) / 100.0f;
        private boolean visible = false;
        // SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);

        // Instance of class Handler that the queue of processes, used to start the Runnable object
        // and to make it call itself indefinitely.
        private final Handler handler = new Handler();

        // A to-be-thread object, the only purpose - start a new thread that will redraw
        // the wallpaper all the time. Probably makes the program super inefficient.
        private final Runnable loadRunner = new Runnable() {
            @Override
            public void run() {
                final Thread draw_thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        refreshDraw();
                    }
                });
                draw_thread.setPriority(Thread.MAX_PRIORITY);
                draw_thread.start();
            }
        };

        /**
         * Constructor for Engine
         * Initializes the parallax image and its parameters,
         * starts the process of refreshing the image.
         **/
        public WPEngine() {
            Log.d(TAG, "Initializing Engine");

            img = new ParallaxImageView(getApplicationContext());
            img.setParallaxIntensity(intensity);
            img.setTiltSensitivity(1.25f);
            img.setScaledIntensities(true);
            img.registerSensorManager();
            img.setImageBitmap(BitmapFactory.decodeStream(getResources().
                    openRawResource(R.raw.large)));

            // registering a shared preference change listener.
            SharedPreferences preferences = PreferenceManager.
                    getDefaultSharedPreferences(getApplicationContext());
            preferences.registerOnSharedPreferenceChangeListener(prefListener);
            handler.post(loadRunner);
        }

        @Override
        public void onSurfaceCreated(SurfaceHolder surfaceHolder) {
            super.onSurfaceCreated(surfaceHolder);
            // Get screen params
            Canvas c = surfaceHolder.lockCanvas();
            if (c != null) {
                width = c.getWidth();
                height = c.getHeight();
            }

            // Apply screen params to parallax image view
            img.layout(0, 0, width, height);

            surfaceHolder.unlockCanvasAndPost(c);
        }

        /**
         * Draws the wallpaper image on the wallpaper canvas
         * No log here because it's an infinite loop
         */
        private void draw() {
            Canvas c = null;
            SurfaceHolder surfaceHolder = getSurfaceHolder();

            try {
                // Get canvas
                c = surfaceHolder.lockCanvas();

                // Draw the image on its canvas.
                img.draw(c);
            } catch (NullPointerException e) {
                Log.d(TAG, "Canvas or bitmap do not exist", e);
            }

            try {
                surfaceHolder.unlockCanvasAndPost(c);
            } catch (IllegalStateException e) {
                Log.d(TAG, "Surface destroyed, but thread is still running", e);
            }
        }

        private void refreshDraw() {
            draw();
            handler.removeCallbacks(loadRunner);
            if (visible)
                handler.post(loadRunner);
        }

        @Override
        public void onSurfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            Log.d(TAG, "SurfaceChanged");
            this.width = width;
            this.height = height;
            super.onSurfaceChanged(holder, format, this.width, this.height);
            draw();
            handler.removeCallbacks(loadRunner);
        }

        @Override
        public void onSurfaceDestroyed(SurfaceHolder holder) {
            Log.d(TAG, "SurfaceDestroyed");

            super.onSurfaceDestroyed(holder);
            handler.removeCallbacks(loadRunner);
            img.unregisterSensorManager();

            // unregistering a shared preference change listener.
            SharedPreferences preferences = PreferenceManager.
                    getDefaultSharedPreferences(getApplicationContext());
            preferences.unregisterOnSharedPreferenceChangeListener(prefListener);
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

        /**
         * This is a listener that gets called every time the application shared preferences
         * are changed. Right now it changes parallax intensity, later will extend functionality.
         */
        public SharedPreferences.OnSharedPreferenceChangeListener prefListener =
                new SharedPreferences.OnSharedPreferenceChangeListener() {
                    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
                                                          String key) {
                        Log.d(TAG, "Preference Changed");
                        if (SettingsActivity.SEEK_BAR_KEY.equals(key)) {
                            Log.d(TAG, "SharedPreferenceChanged: key = " + key +
                                    ", new value in percent = " +
                                    sharedPreferences.getInt(SettingsActivity.SEEK_BAR_KEY, 0));
                            intensity = 1.1f +
                                    (sharedPreferences.getInt(SettingsActivity.SEEK_BAR_KEY, 0)
                                    * delta);
                            img.setParallaxIntensity(intensity);
                        }
                    }
                };
    }
}
