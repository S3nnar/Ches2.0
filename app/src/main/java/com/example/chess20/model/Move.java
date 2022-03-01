package com.example.chess20.model;

public class Move {

    private Square fromSquare;
    private Square toSquare;
    private Piece piece;

    public Move(){

    }

    public Move(Square fromSquare, Square toSquare, Piece piece) {
        this.fromSquare = fromSquare;
        this.toSquare = toSquare;
        this.piece = piece;
        System.out.println(getPrintedMove(fromSquare, toSquare, piece));
    }

    public Piece getPiece() {
        return piece;
    }

    public void setPiece(Piece piece) {
        this.piece = piece;
    }

    public Square getFromSquare() {
        return fromSquare;
    }

    public void setFromSquare(Square fromSquare) {
        this.fromSquare = fromSquare;
    }

    public Square getToSquare() {
        return toSquare;
    }

    public void setToSquare(Square toSquare) {
        this.toSquare = toSquare;
    }

    // TODO: Remove fromSquare
    private String getPrintedMove(Square fromSquare, Square toSquare, Piece piece){
        return getColumnCharacterFromNumber(toSquare.getsquarePosition().getColumnCount()) +
                getRowCount(toSquare.getsquarePosition().getRowCount()) +
                piece.getType();
    }

    private int getRowCount(int rowCount) {
        switch (rowCount){
            case 0:
                return 8;
            case 1:
                return 7;
            case 2:
                return 6;
            case 3:
                return 5;
            case 4:
                return 4;
            case 5:
                return 3;
            case 6:
                return 2;
            case 7:
                return 1;
        }
        return 0;
    }

    private String getColumnCharacterFromNumber(int number){
        switch (number){
            case 0:
                return "a";
            case 1:
                return "b";
            case 2:
                return "c";
            case 3:
                return "d";
            case 4:
                return "e";
            case 5:
                return "f";
            case 6:
                return "g";
            case 7:
                return "h";
        }
        return "error";
    }
}
