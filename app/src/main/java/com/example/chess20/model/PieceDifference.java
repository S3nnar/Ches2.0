package com.example.chess20.model;

public class PieceDifference {

    int pawn = 0;
    int knight = 0;
    int bishop = 0;
    int queen = 0;
    int rock = 0;
    int king = 0;

    public PieceDifference(){}

    public PieceDifference(int pawn, int knight, int bishop, int queen, int rock, int king) {
        this.pawn = pawn;
        this.knight = knight;
        this.bishop = bishop;
        this.queen = queen;
        this.rock = rock;
        this.king = king;
    }

    public int getPawn() {
        return pawn;
    }

    public void setPawn(int pawn) {
        this.pawn = pawn;
    }

    public int getKnight() {
        return knight;
    }

    public void setKnight(int knight) {
        this.knight = knight;
    }

    public int getBishop() {
        return bishop;
    }

    public void setBishop(int bishop) {
        this.bishop = bishop;
    }

    public int getQueen() {
        return queen;
    }

    public void setQueen(int queen) {
        this.queen = queen;
    }

    public int getRock() {
        return rock;
    }

    public void setRock(int rock) {
        this.rock = rock;
    }

    public int getKing() {
        return king;
    }

    public void setKing(int king) {
        this.king = king;
    }

    public int getScore() {
        return pawn+knight*3+bishop*3+rock*5+queen*9;
    }
}
