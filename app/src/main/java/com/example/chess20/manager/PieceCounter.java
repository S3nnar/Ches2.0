package com.example.chess20.manager;

import com.example.chess20.model.Piece;
import com.example.chess20.model.PieceDifference;
import com.example.chess20.model.Square;

import java.util.ArrayList;

public class PieceCounter {

    public PieceCounter(){}

    /**
     * Returns the piece difference
     * @param board
     * @return
     */
    public PieceDifference getAllPieces(Square[][] board){
        ArrayList<Piece> whitePieces = new ArrayList<>();
        ArrayList<Piece> blackPieces = new ArrayList<>();
        PieceDifference pieceDifference = new PieceDifference();
        for (Square[] s:board){
            for (Square sq:s){
                switch (sq.getPiece().getColor()){
                    case B:
                        blackPieces.add(sq.getPiece());
                        break;
                    case W:
                        whitePieces.add(sq.getPiece());
                        break;
                }
            }
        }
        for (Piece p:whitePieces){
            switch (p.getType()){
                case P:
                    pieceDifference.setPawn(pieceDifference.getPawn()+1);
                    break;
                case N:
                    pieceDifference.setKnight(pieceDifference.getKnight()+1);
                    break;
                case B:
                    pieceDifference.setBishop(pieceDifference.getBishop()+1);
                    break;
                case Q:
                    pieceDifference.setQueen(pieceDifference.getQueen()+1);
                    break;
                case R:
                    pieceDifference.setRock(pieceDifference.getRock()+1);
                    break;
                case K:
                    pieceDifference.setKing(pieceDifference.getKing()+1);
                    break;
            }
        }
        for (Piece p:blackPieces){
            switch (p.getType()){
                case P:
                    pieceDifference.setPawn(pieceDifference.getPawn()-1);
                    break;
                case N:
                    pieceDifference.setKnight(pieceDifference.getKnight()-1);
                    break;
                case B:
                    pieceDifference.setBishop(pieceDifference.getBishop()-1);
                    break;
                case Q:
                    pieceDifference.setQueen(pieceDifference.getQueen()-1);
                    break;
                case R:
                    pieceDifference.setRock(pieceDifference.getRock()-1);
                    break;
                case K:
                    pieceDifference.setKing(pieceDifference.getKing()-1);
                    break;
            }
        }
        return pieceDifference;
        }

    public int getNumberOfPiecesBeforeKnight(PieceDifference pieceDifference, boolean forWhite) {
        int numberOfPieces = 0;
        if ((forWhite && pieceDifference.getPawn() > 0)
                || (!forWhite && pieceDifference.getPawn() < 0)) {
            numberOfPieces = pieceDifference.getPawn();
        }
        return numberOfPieces;
    }

    public int getNumberOfPiecesBeforeBishop(PieceDifference pieceDifference, boolean forWhite) {
        int numberOfPieces = getNumberOfPiecesBeforeKnight(pieceDifference, forWhite);
        if ((forWhite && pieceDifference.getKnight() > 0)
                || (!forWhite && pieceDifference.getKnight() < 0)) {
            numberOfPieces += pieceDifference.getKnight();
        }
        return numberOfPieces;
    }

    public int getNumberOfPiecesBeforeRock(PieceDifference pieceDifference, boolean forWhite) {
        int numberOfPieces = getNumberOfPiecesBeforeBishop(pieceDifference, forWhite);
        if ((forWhite && pieceDifference.getBishop() > 0)
                || (!forWhite && pieceDifference.getBishop() < 0)) {
            numberOfPieces += pieceDifference.getBishop();
        }
        return numberOfPieces;
    }

    public int getNumberOfPiecesBeforeQueen(PieceDifference pieceDifference, boolean forWhite) {
        int numberOfPieces = getNumberOfPiecesBeforeRock(pieceDifference, forWhite);
        if ((forWhite && pieceDifference.getRock() > 0)
                || (!forWhite && pieceDifference.getRock() < 0)) {
            numberOfPieces += pieceDifference.getRock();
        }
        return numberOfPieces;
    }

    public int getNumberOfPiecesBeforeKing(PieceDifference pieceDifference, boolean forWhite) {
        int numberOfPieces = getNumberOfPiecesBeforeQueen(pieceDifference, forWhite);
        if ((forWhite && pieceDifference.getQueen() > 0)
                || (!forWhite && pieceDifference.getQueen() < 0)) {
            numberOfPieces += pieceDifference.getQueen();
        }
        return numberOfPieces;
    }

    public int getNumberOfPieces(PieceDifference pieceDifference, boolean forWhite) {
        int numberOfPieces = getNumberOfPiecesBeforeKing(pieceDifference, forWhite);
        if ((forWhite && pieceDifference.getKing() > 0)
                || (!forWhite && pieceDifference.getKing() < 0)) {
            numberOfPieces += pieceDifference.getKing();
        }
        return numberOfPieces;
    }
    }
