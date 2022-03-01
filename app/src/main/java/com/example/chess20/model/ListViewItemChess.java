package com.example.chess20.model;

import android.graphics.Canvas;

public class ListViewItemChess {


    private Canvas canvas;
    private String position;

    public ListViewItemChess(Canvas canvas, String position) {
        this.canvas = canvas;
        this.position = position;
    }

    public Canvas getCanvas() {
        return canvas;
    }

    public void setCanvas(Canvas canvas) {
        this.canvas = canvas;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }
}
