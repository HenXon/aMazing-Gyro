package com.example.henriktornqvist.amazinggyro;

import android.graphics.RectF;

import java.util.ArrayList;

/**
 * Created by henriktornqvist on 24/04/16.
 */
public class GameLevel {

    public ArrayList<RectF> walls = new ArrayList<RectF>();

    public GameLevel(int width, int height) {
        loadMap((float) width, (float) height);
    }

    private void loadMap(float width, float height) {
        walls.add(new RectF(0.1f * width, 0.1f * height, 0.2f * width, 0.2f * height));
    }

    public ArrayList<RectF> getWalls() {
        return walls;
    }
}
