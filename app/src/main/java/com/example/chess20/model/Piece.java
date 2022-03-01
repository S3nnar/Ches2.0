package com.example.chess20.model;

public class Piece {

    public enum Type {P, R, N, B, Q, K, NaN}
    public enum Color {B, W, NaN}

    private Type type;
    private Color color;

    public Piece() {
    }

    public Piece(Type type, Color color) {
        this.type = type;
        this.color = color;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }


}
