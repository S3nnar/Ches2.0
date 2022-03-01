package com.example.chess20.model;

import android.graphics.Bitmap;
import android.graphics.Canvas;

public class BitmapCanvasPair {

    private Bitmap bitmap;
    private Canvas canvas;

    public BitmapCanvasPair(){};

    public BitmapCanvasPair(Bitmap bitmap, Canvas canvas) {
        this.bitmap = bitmap;
        this.canvas = canvas;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public Canvas getCanvas() {
        return canvas;
    }

    public void setCanvas(Canvas canvas) {
        this.canvas = canvas;
    }
}
