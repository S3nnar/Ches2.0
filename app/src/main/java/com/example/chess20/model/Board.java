package com.example.chess20.model;

import com.example.chess20.manager.chessFunctionality.MoveManager;

import java.util.ArrayList;

public class Board {

    private Square[][] board;

    private boolean whitesMove;

    private ArrayList<Move> moveList;

    private ArrayList<String> positionList;

    private MoveManager moveManager;

    public Board(String position) {
        board = new Square[8][8];
        buildBoard();
        buildPosition(position);
        whitesMove = true;
        moveList = new ArrayList<>();
        moveManager = new MoveManager();
        positionList = new ArrayList<>();
    }

    public Square[][] getBoard() {
        return board;
    }

    public boolean isWhitesMove() {
        return whitesMove;
    }

    public void setWhitesMove(boolean whitesMove) {
        this.whitesMove = whitesMove;
    }

    public void addMove(Move move) {
        moveList.add(move);
    }

    public void deleteLastMove() {
        moveList.remove(moveList.size() - 1);
    }

    /**
     * Initializes the board with the correct square positions and places NaN-pieces on all of them
     */
    private void buildBoard() {
        for (int i = 0; i < 8; i++) {
            for (int i2 = 0; i2 < 8; i2++) {
                Square f = new Square();
                f.setsquarePosition(new SquarePosition(i, i2));
                f.setPiece(new Piece(Piece.Type.NaN, Piece.Color.NaN));
                board[i][i2] = f;
            }
        }
    }

    /**
     * Places all pieces of the position on the board
     *
     * @param position Holds the position as a string
     */
    public void buildPosition(String position) {
        // splits string in separate rows
        String[] rows = position.split("/");
        int rowCount = 0;
        for (String s : rows) {
            int columnCount = 0;
            char[] chars = s.toCharArray();
            for (char c : chars) {
                if (Character.isDigit(c)) {
                    columnCount += Character.getNumericValue(c);
                }
                switch (c) {
                    case 'r':
                        board[columnCount][rowCount].setPiece(new Piece(Piece.Type.R, Piece.Color.B));
                        columnCount++;
                        break;
                    case 'n':
                        board[columnCount][rowCount].setPiece(new Piece(Piece.Type.N, Piece.Color.B));
                        columnCount++;
                        break;
                    case 'b':
                        board[columnCount][rowCount].setPiece(new Piece(Piece.Type.B, Piece.Color.B));
                        columnCount++;
                        break;
                    case 'k':
                        board[columnCount][rowCount].setPiece(new Piece(Piece.Type.K, Piece.Color.B));
                        columnCount++;
                        break;
                    case 'q':
                        board[columnCount][rowCount].setPiece(new Piece(Piece.Type.Q, Piece.Color.B));
                        columnCount++;
                        break;
                    case 'p':
                        board[columnCount][rowCount].setPiece(new Piece(Piece.Type.P, Piece.Color.B));
                        columnCount++;
                        break;
                    case 'R':
                        board[columnCount][rowCount].setPiece(new Piece(Piece.Type.R, Piece.Color.W));
                        columnCount++;
                        break;
                    case 'N':
                        board[columnCount][rowCount].setPiece(new Piece(Piece.Type.N, Piece.Color.W));
                        columnCount++;
                        break;
                    case 'B':
                        board[columnCount][rowCount].setPiece(new Piece(Piece.Type.B, Piece.Color.W));
                        columnCount++;
                        break;
                    case 'K':
                        board[columnCount][rowCount].setPiece(new Piece(Piece.Type.K, Piece.Color.W));
                        columnCount++;
                        break;
                    case 'Q':
                        board[columnCount][rowCount].setPiece(new Piece(Piece.Type.Q, Piece.Color.W));
                        columnCount++;
                        break;
                    case 'P':
                        board[columnCount][rowCount].setPiece(new Piece(Piece.Type.P, Piece.Color.W));
                        columnCount++;
                }
            }
            rowCount++;
        }
    }

    /**
     * Checks if any piece is on the given square
     *
     * @param squarePosition position of the square
     * @return returns true if a piece is placed on the square
     */
    public boolean isPieceOnSquare(SquarePosition squarePosition) {
        return board[squarePosition.getColumnCount()][squarePosition.getRowCount()].getPiece().getType() != Piece.Type.NaN;
    }

    public ArrayList<Square> getSquaresPieceCanEnter(SquarePosition squarePosition, boolean firstMethodeCall) {
        return moveManager.getSquaresPieceCanEnter(squarePosition, firstMethodeCall, board, moveList);
    }


    public ArrayList<Move> getMoveList() {
        return moveList;
    }

    public void setMoveList(ArrayList<Move> moveList) {
        this.moveList = moveList;
    }

    public boolean checkForEnPassant(SquarePosition squarePosition, Square selectedSquare) {
        return moveManager.checkForEnPassant(squarePosition, moveList, selectedSquare, board);
    }

    public boolean checkForShortCastle(SquarePosition squarePosition, Square selectedSquare){
        return moveManager.checkForShortCastle(squarePosition, board, selectedSquare);
    }

    public boolean checkForLongCastle(SquarePosition squarePosition, Square selectedSquare){
        return moveManager.checkForLongCastle(squarePosition, board, selectedSquare);
    }

    public ArrayList<String> getPositionList() {
        return positionList;
    }

    public void setPositionList(ArrayList<String> positionList) {
        this.positionList = positionList;
    }

    public void addPositionToPositionList(String position){
        positionList.add(position);
    }

    public void deleteLastPositionFromPositionList(){
        if (positionList.size()>0) {
            positionList.remove(positionList.size() - 1);
        }
    }

    public ArrayList<Square> getSquaresWhichCanBeEnteredWithAnyPiece(){
        return moveManager.getSquaresWhichCanBeEnteredWithAnyPiece(board, whitesMove, moveList);
    }

    public boolean checkForCheck(){
        if (whitesMove){
            return moveManager.checkForCheck(Piece.Color.W, board, moveList);
        } else {
            return moveManager.checkForCheck(Piece.Color.B, board, moveList);
        }
    }

    public boolean checkIfKingMoved(Piece.Color color){
        return moveManager.checkIfKingMoved(color, moveList);
    }

    public boolean checkIfARockMoved(Piece.Color color){
        return moveManager.checkIfARockMoved(color, moveList);
    }

    public boolean checkIfHRockMoved(Piece.Color color){
        return moveManager.checkIfHRockMoved(color, moveList);
    }
}
