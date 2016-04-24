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
import android.view.Display;
import android.view.View;

import java.util.ArrayList;

public class Game extends Activity implements SensorEventListener {
    /**
     * Called when the activity is first created.
     */
    CustomDrawableView mCustomDrawableView = null;
    ShapeDrawable mDrawable = new ShapeDrawable();
    private static float velX;
    private static float velY;

    private static float accX;
    private static float accY;

    private static float gravityFactor = 0.1f;
    private static float bounceFactor = 0.9f;
    private static float frictionFactor = 0.001f;

    private static int width;
    private static int height;

    public static float x;
    public static float y;

    public static float xPrevious = 0f;
    public static float yPrevious = 0f;

    public static int epsilonPix = 1;

    public static GameLevel gameLevel;
    public static ArrayList<RectF> gameLevelWalls;

    private SensorManager sensorManager = null;

    private static float ballSizeFactor = 0.05f;

    private int ballDiameter;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        width = size.x;
        height = size.y;

        // Get a reference to a SensorManager
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mCustomDrawableView = new CustomDrawableView(this);
        setContentView(mCustomDrawableView);
        // setContentView(R.layout.main);


        gameLevel = new GameLevel(width, height);
        gameLevelWalls = gameLevel.getWalls();

        ballDiameter = (int) (width * ballSizeFactor);

    }

    // This method will update the UI on new sensor events
    public void onSensorChanged(SensorEvent sensorEvent) {
        {
            if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                // the values you were calculating originally here were over 10000!

                accX = -sensorEvent.values[0] * gravityFactor;
                accY = sensorEvent.values[1] * gravityFactor;

                moveBall();

//                Log.d("Game", "x = " + Float.toString(sensorEvent.values[0]) + "\ny = " + Float.toString(sensorEvent.values[1]));

            }

            if (sensorEvent.sensor.getType() == Sensor.TYPE_ORIENTATION) {

            }
        }

    }

    private void moveBall() {
//        Log.d("Game", "x = " + x + "\nballWidth = " + ballWidth);
//        Log.d("Game", "y = " + y + "\nballHeight = " + ballHeight);

        velX = (velX + accX) * (1 - frictionFactor);
        velY = (velY + accY) * (1 - frictionFactor);

        x = x + velX;
        y = y + velY;

        if ((int) x > width - ballDiameter) {
            velX = -1f * Math.abs(velX) * bounceFactor;
            x = xPrevious;
        }
        if ((int) x < 0) {
            velX = Math.abs(velX) * bounceFactor;
            x = xPrevious;
        }
        if ((int) y > height - ballDiameter - 50) {
            velY = -1f * Math.abs(velY) * bounceFactor;
            y = yPrevious;
        }
        if ((int) y < 0) {
            velY = Math.abs(velY) * bounceFactor;
            y = yPrevious;
        }

        for (int index = 0; index < gameLevelWalls.size(); index++) {

            FIIIIIIIIXAAAA
            if ((int) x > width - ballDiameter) {
                velX = -1f * Math.abs(velX) * bounceFactor;
                x = xPrevious;
            }
            if ((int) x < 0) {
                velX = Math.abs(velX) * bounceFactor;
                x = xPrevious;
            }
            if ((int) y > height - ballDiameter - 50) {
                velY = -1f * Math.abs(velY) * bounceFactor;
                y = yPrevious;
            }
            if ((int) y < 0) {
                velY = Math.abs(velY) * bounceFactor;
                y = yPrevious;
            }
        }

        xPrevious = x;
        yPrevious = y;


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

        public CustomDrawableView(Context context) {
            super(context);

            mDrawable = new ShapeDrawable(new OvalShape());
            mDrawable.getPaint().setColor(0xff74AC23);
            mDrawable.setBounds((int) x, (int) y, (int) x + ballDiameter, (int) y + ballDiameter);
        }

        protected void onDraw(Canvas canvas) {
            RectF oval = new RectF(Game.x, Game.y, Game.x + ballDiameter, Game.y
                    + ballDiameter); // set bounds of rectangle
            Paint p = new Paint(); // set some paint options
            p.setColor(Color.BLUE);
            canvas.drawOval(oval, p);
//            Log.d("Game", "onDraw");
            for (int index = 0; index < gameLevelWalls.size(); index++) {
                canvas.drawRect(gameLevelWalls.get(index), p);
            }
            invalidate();
        }
    }
}