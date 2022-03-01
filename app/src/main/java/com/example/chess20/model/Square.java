package com.example.chess20.model;

public class Square {

    private SquarePosition squarePosition;
    private Piece piece;

    public Square() {
    }

    public Square(SquarePosition squarePosition, Piece piece) {
        this.squarePosition = squarePosition;
        this.piece = piece;
    }

    public SquarePosition getsquarePosition() {
        return squarePosition;
    }

    public void setsquarePosition(SquarePosition squarePosition) {
        this.squarePosition = squarePosition;
    }

    public Piece getPiece() {
        return piece;
    }

    public void setPiece(Piece piece) {
        this.piece = piece;
    }
}
