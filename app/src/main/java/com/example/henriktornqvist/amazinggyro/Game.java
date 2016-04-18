package com.example.henriktornqvist.amazinggyro;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;

public class Game extends Activity implements SensorEventListener {
    /**
     * Called when the activity is first created.
     */
    CustomDrawableView mCustomDrawableView = null;
    ShapeDrawable mDrawable = new ShapeDrawable();
    private static float velX;
    private static float velY;

    private static float gravityFactor = 0.01f;
    private static float bounceFactor = 0.9f;
    private static float frictionFactor = 0.001f;

    private static int width;
    private static int height;

    public static float x;
    public static float y;

    private SensorManager sensorManager = null;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        // Get a reference to a SensorManager
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mCustomDrawableView = new CustomDrawableView(this);
        setContentView(mCustomDrawableView);
        // setContentView(R.layout.main);

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        width = size.x;
        height = size.y;

    }

    // This method will update the UI on new sensor events
    public void onSensorChanged(SensorEvent sensorEvent) {
        {
            if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                // the values you were calculating originally here were over 10000!

                velX = (velX - sensorEvent.values[0] * gravityFactor) * (1 - frictionFactor);
                velY = (velY + sensorEvent.values[1] * gravityFactor) * (1 - frictionFactor);

                x = x + velX;
                y = y + velY;

                checkBoundaries();

//                Log.d("Game", "x = " + Float.toString(sensorEvent.values[0]) + "\ny = " + Float.toString(sensorEvent.values[1]));

            }

            if (sensorEvent.sensor.getType() == Sensor.TYPE_ORIENTATION) {

            }
        }

    }

    private void checkBoundaries() {
        Log.d("Game", "x = " + x + "\nwidth = " + width);
        Log.d("Game", "y = " + y + "\nheight = " + height);
        if ((int) x > width - 50 || (int) x < 0) {
            velX = -velX * 1;
        }
        if ((int) y > height - 100 || (int) y < 0) {
            velY = -velY * 1;
        }
    }

    // I've chosen to not implement this method
    public void onAccuracyChanged(Sensor arg0, int arg1) {
        // TODO Auto-generated method stub

    }

    @Override
    protected void onResume() {
        super.onResume();
        // Register this class as a listener for the accelerometer sensor
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_GAME);
        // ...and the orientation sensor
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),
                SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    protected void onStop() {
        // Unregister the listener
        sensorManager.unregisterListener(this);
        super.onStop();
    }

    public class CustomDrawableView extends View {
        static final int width = 50;
        static final int height = 50;

        public CustomDrawableView(Context context) {
            super(context);

            mDrawable = new ShapeDrawable(new OvalShape());
            mDrawable.getPaint().setColor(0xff74AC23);
            mDrawable.setBounds((int) x, (int) y, (int) x + width, (int) y + height);
        }

        protected void onDraw(Canvas canvas) {
            RectF oval = new RectF(Game.x, Game.y, Game.x + width, Game.y
                    + height); // set bounds of rectangle
            Paint p = new Paint(); // set some paint options
            p.setColor(Color.BLUE);
            canvas.drawOval(oval, p);
//            Log.d("Game", "onDraw");
            invalidate();
        }
    }
}