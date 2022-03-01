package com.example.chess20.manager.chessFunctionality;

import com.example.chess20.model.Move;
import com.example.chess20.model.Piece;
import com.example.chess20.model.Square;
import com.example.chess20.model.SquarePosition;

import java.util.ArrayList;

public class MoveManager {


    public MoveManager() {
    }

    /**
     * Returns a list of all squares the piece on the given square can enter
     *
     * @param squarePosition   position of the square
     * @param firstMethodeCall parameter to change the purpose of the methode
     *                         in its second run it is used to check witch squares are protected/covered
     * @return
     */
    public ArrayList<Square> getSquaresPieceCanEnter(SquarePosition squarePosition, boolean firstMethodeCall, Square[][] board, ArrayList<Move> moveList) {
        ArrayList<Square> returnList = new ArrayList<>();
        switch (board[squarePosition.getColumnCount()][squarePosition.getRowCount()].getPiece().getType()) {
            case R:
                returnList.addAll(getSquaresToTheLeft(squarePosition, firstMethodeCall, board));
                returnList.addAll(getSquaresToTheRight(squarePosition, firstMethodeCall, board));
                returnList.addAll(getSquaresUp(squarePosition, firstMethodeCall, board));
                returnList.addAll(getSquaresDown(squarePosition, firstMethodeCall, board));
                break;
            case B:
                returnList.addAll(getSquaresLeftUp(squarePosition, firstMethodeCall, board));
                returnList.addAll(getSquaresLeftDown(squarePosition, firstMethodeCall, board));
                returnList.addAll(getSquaresRightUp(squarePosition, firstMethodeCall, board));
                returnList.addAll(getSquaresRightDown(squarePosition, firstMethodeCall, board));
                break;
            case Q:
                returnList.addAll(getSquaresToTheLeft(squarePosition, firstMethodeCall, board));
                returnList.addAll(getSquaresToTheRight(squarePosition, firstMethodeCall, board));
                returnList.addAll(getSquaresUp(squarePosition, firstMethodeCall, board));
                returnList.addAll(getSquaresDown(squarePosition, firstMethodeCall, board));
                returnList.addAll(getSquaresLeftUp(squarePosition, firstMethodeCall, board));
                returnList.addAll(getSquaresLeftDown(squarePosition, firstMethodeCall, board));
                returnList.addAll(getSquaresRightUp(squarePosition, firstMethodeCall, board));
                returnList.addAll(getSquaresRightDown(squarePosition, firstMethodeCall, board));
                break;
            case N:
                returnList.addAll(getSquaresOfKnight(squarePosition, firstMethodeCall, board));
                break;
            case K:
                returnList.addAll(getSquaresOfKing(squarePosition, firstMethodeCall, board, moveList));
                break;
            case P:
                returnList.addAll(getSquaresOfPawn(squarePosition, firstMethodeCall, board, moveList));
        }
        // Check if move is legal or results in a check
        if (firstMethodeCall) {
            ArrayList<Square> notPossibleBecauseOfCheck = new ArrayList<>();
            for (Square s : returnList) {
                Piece piece = board[squarePosition.getColumnCount()][squarePosition.getRowCount()].getPiece();
                Square[][] copyBoard = getCopyOfBoard(board);
                copyBoard[s.getsquarePosition().getColumnCount()][s.getsquarePosition().getRowCount()].setPiece(new Piece(piece.getType(), piece.getColor()));
                copyBoard[squarePosition.getColumnCount()][squarePosition.getRowCount()].setPiece(new Piece(Piece.Type.NaN, Piece.Color.NaN));
                if (getAllCoveredSquares(piece.getColor(), copyBoard, moveList).stream().anyMatch(sq -> sq.getPiece().getType().equals(Piece.Type.K) && sq.getPiece().getColor().equals(piece.getColor()))) {
                    notPossibleBecauseOfCheck.add(s);
                }
            }
            returnList.removeAll(notPossibleBecauseOfCheck);
        }
        return returnList;
    }

    /**
     * Returns squares to the left which the piece on the square position can enter
     *
     * @param squarePosition
     * @param firstMethodeCall
     * @return
     */
    private ArrayList<Square> getSquaresToTheLeft(SquarePosition squarePosition, boolean firstMethodeCall, Square[][] board) {
        ArrayList<Square> returnList = new ArrayList<>();
        boolean noOtherSquareInThisDirection = false;
        int deltaColumn = 0;
        // Loop for piece to the left
        while (!noOtherSquareInThisDirection) {
            deltaColumn--;
            // If square is still on the board
            if (squarePosition.getColumnCount() + deltaColumn != -1) {
                // If square is empty
                if (board[squarePosition.getColumnCount() + deltaColumn][squarePosition.getRowCount()].getPiece().getType().equals(Piece.Type.NaN)) {
                    returnList.add(board[squarePosition.getColumnCount() + deltaColumn][squarePosition.getRowCount()]);
                }
                // If pieces are of same color
                else if (board[squarePosition.getColumnCount() + deltaColumn][squarePosition.getRowCount()].getPiece().getColor().equals(
                        board[squarePosition.getColumnCount()][squarePosition.getRowCount()].getPiece().getColor()
                ) && firstMethodeCall) {
                    noOtherSquareInThisDirection = true;
                } else {
                    // On this square stands a piece of opposite color
                    returnList.add(board[squarePosition.getColumnCount() + deltaColumn][squarePosition.getRowCount()]);
                    noOtherSquareInThisDirection = true;
                }
            } else {
                noOtherSquareInThisDirection = true;
            }
        }
        return returnList;
    }

    /**
     * Returns squares to the right which the piece on the square position can enter
     *
     * @param squarePosition
     * @param firstMethodeCall
     * @return
     */
    private ArrayList<Square> getSquaresToTheRight(SquarePosition squarePosition, boolean firstMethodeCall, Square[][] board) {
        ArrayList<Square> returnList = new ArrayList<>();
        int deltaColumn = 0;
        boolean noOtherSquareInThisDirection = false;
        // Loop for piece to the right
        while (!noOtherSquareInThisDirection) {
            deltaColumn++;
            // If square is still on the board
            if (squarePosition.getColumnCount() + deltaColumn != 8) {
                // If square is empty
                if (board[squarePosition.getColumnCount() + deltaColumn][squarePosition.getRowCount()].getPiece().getType().equals(Piece.Type.NaN)) {
                    returnList.add(board[squarePosition.getColumnCount() + deltaColumn][squarePosition.getRowCount()]);
                }
                // If pieces are of same color
                else if (board[squarePosition.getColumnCount() + deltaColumn][squarePosition.getRowCount()].getPiece().getColor().equals(
                        board[squarePosition.getColumnCount()][squarePosition.getRowCount()].getPiece().getColor()
                ) && firstMethodeCall) {
                    noOtherSquareInThisDirection = true;
                } else {
                    // On this square stands a piece of opposite color
                    returnList.add(board[squarePosition.getColumnCount() + deltaColumn][squarePosition.getRowCount()]);
                    noOtherSquareInThisDirection = true;
                }
            } else {
                noOtherSquareInThisDirection = true;
            }
        }
        return returnList;
    }

    /**
     * Returns squares to the top which the piece on the square position can enter
     *
     * @param squarePosition
     * @param firstMethodeCall
     * @return
     */
    private ArrayList<Square> getSquaresUp(SquarePosition squarePosition, boolean firstMethodeCall, Square[][] board) {
        ArrayList<Square> returnList = new ArrayList<>();
        int deltaRow = 0;
        boolean noOtherSquareInThisDirection = false;
        // Loop for piece up
        while (!noOtherSquareInThisDirection) {
            deltaRow--;
            // If square is still on the board
            if (squarePosition.getRowCount() + deltaRow != -1) {
                // If square is empty
                if (board[squarePosition.getColumnCount()][squarePosition.getRowCount() + deltaRow].getPiece().getType().equals(Piece.Type.NaN)) {
                    returnList.add(board[squarePosition.getColumnCount()][squarePosition.getRowCount() + deltaRow]);
                }
                // If pieces are of same color
                else if (board[squarePosition.getColumnCount()][squarePosition.getRowCount() + deltaRow].getPiece().getColor().equals(
                        board[squarePosition.getColumnCount()][squarePosition.getRowCount()].getPiece().getColor()
                ) && firstMethodeCall) {
                    noOtherSquareInThisDirection = true;
                } else {
                    // On this square stands a piece of opposite color
                    returnList.add(board[squarePosition.getColumnCount()][squarePosition.getRowCount() + deltaRow]);
                    noOtherSquareInThisDirection = true;
                }
            } else {
                noOtherSquareInThisDirection = true;
            }
        }
        return returnList;
    }

    /**
     * Returns squares to the bottom which the piece on the square position can enter
     *
     * @param squarePosition
     * @param firstMethodeCall
     * @return
     */
    private ArrayList<Square> getSquaresDown(SquarePosition squarePosition, boolean firstMethodeCall, Square[][] board) {
        ArrayList<Square> returnList = new ArrayList<>();
        boolean noOtherSquareInThisDirection = false;
        int deltaRow = 0;
        // Loop for piece down
        while (!noOtherSquareInThisDirection) {
            deltaRow++;
            // If square is still on the board
            if (squarePosition.getRowCount() + deltaRow != 8) {
                // If square is empty
                if (board[squarePosition.getColumnCount()][squarePosition.getRowCount() + deltaRow].getPiece().getType().equals(Piece.Type.NaN)) {
                    returnList.add(board[squarePosition.getColumnCount()][squarePosition.getRowCount() + deltaRow]);
                }
                // If pieces are of same color
                else if (board[squarePosition.getColumnCount()][squarePosition.getRowCount() + deltaRow].getPiece().getColor().equals(
                        board[squarePosition.getColumnCount()][squarePosition.getRowCount()].getPiece().getColor()
                ) && firstMethodeCall) {
                    noOtherSquareInThisDirection = true;
                } else {
                    // On this square stands a piece of opposite color
                    returnList.add(board[squarePosition.getColumnCount()][squarePosition.getRowCount() + deltaRow]);
                    noOtherSquareInThisDirection = true;
                }
            } else {
                noOtherSquareInThisDirection = true;
            }
        }
        return returnList;
    }

    /**
     * Returns squares to the upper left which the piece on the square position can enter
     *
     * @param squarePosition
     * @param firstMethodeCall
     * @return
     */
    private ArrayList<Square> getSquaresLeftUp(SquarePosition squarePosition, boolean firstMethodeCall, Square[][] board) {
        ArrayList<Square> returnList = new ArrayList<>();
        boolean noOtherSquareInThisDirection = false;
        int deltaRow = 0;
        int deltaColumn = 0;
        // Loop for piece left up
        while (!noOtherSquareInThisDirection) {
            deltaColumn--;
            deltaRow--;
            // If square is still on the board
            if (squarePosition.getColumnCount() + deltaColumn != -1 && squarePosition.getRowCount() + deltaRow != -1) {
                // If square is empty
                if (board[squarePosition.getColumnCount() + deltaColumn][squarePosition.getRowCount() + deltaRow].getPiece().getType().equals(Piece.Type.NaN)) {
                    returnList.add(board[squarePosition.getColumnCount() + deltaColumn][squarePosition.getRowCount() + deltaRow]);
                }
                // If pieces are of same color
                else if (board[squarePosition.getColumnCount() + deltaColumn][squarePosition.getRowCount() + deltaRow].getPiece().getColor().equals(
                        board[squarePosition.getColumnCount()][squarePosition.getRowCount()].getPiece().getColor()
                ) && firstMethodeCall) {
                    noOtherSquareInThisDirection = true;
                } else {
                    // On this square stands a piece of opposite color
                    returnList.add(board[squarePosition.getColumnCount() + deltaColumn][squarePosition.getRowCount() + deltaRow]);
                    noOtherSquareInThisDirection = true;
                }
            } else {
                noOtherSquareInThisDirection = true;
            }
        }
        return returnList;
    }

    /**
     * Returns squares to the lower right which the piece on the square position can enter
     *
     * @param squarePosition
     * @param firstMethodeCall
     * @return
     */
    private ArrayList<Square> getSquaresRightDown(SquarePosition squarePosition, boolean firstMethodeCall, Square[][] board) {
        ArrayList<Square> returnList = new ArrayList<>();
        boolean noOtherSquareInThisDirection = false;
        int deltaRow = 0;
        int deltaColumn = 0;
        // Loop for bishop right down
        while (!noOtherSquareInThisDirection) {
            deltaColumn++;
            deltaRow++;
            // If square is still on the board
            if (squarePosition.getColumnCount() + deltaColumn != 8 && squarePosition.getRowCount() + deltaRow != 8) {
                // If square is empty
                if (board[squarePosition.getColumnCount() + deltaColumn][squarePosition.getRowCount() + deltaRow].getPiece().getType().equals(Piece.Type.NaN)) {
                    returnList.add(board[squarePosition.getColumnCount() + deltaColumn][squarePosition.getRowCount() + deltaRow]);
                }
                // If pieces are of same color
                else if (board[squarePosition.getColumnCount() + deltaColumn][squarePosition.getRowCount() + deltaRow].getPiece().getColor().equals(
                        board[squarePosition.getColumnCount()][squarePosition.getRowCount()].getPiece().getColor()
                ) && firstMethodeCall) {
                    noOtherSquareInThisDirection = true;
                } else {
                    // On this square stands a piece of opposite color
                    returnList.add(board[squarePosition.getColumnCount() + deltaColumn][squarePosition.getRowCount() + deltaRow]);
                    noOtherSquareInThisDirection = true;
                }
            } else {
                noOtherSquareInThisDirection = true;
            }
        }
        return returnList;
    }

    /**
     * Returns squares to the upper right which the piece on the square position can enter
     *
     * @param squarePosition
     * @param firstMethodeCall
     * @return
     */
    private ArrayList<Square> getSquaresRightUp(SquarePosition squarePosition, boolean firstMethodeCall, Square[][] board) {
        ArrayList<Square> returnList = new ArrayList<>();
        boolean noOtherSquareInThisDirection = false;
        int deltaRow = 0;
        int deltaColumn = 0;
        // Loop for bishop right up
        while (!noOtherSquareInThisDirection) {
            deltaColumn++;
            deltaRow--;
            // If square is still on the board
            if (squarePosition.getColumnCount() + deltaColumn != 8 && squarePosition.getRowCount() + deltaRow != -1) {
                // If square is empty
                if (board[squarePosition.getColumnCount() + deltaColumn][squarePosition.getRowCount() + deltaRow].getPiece().getType().equals(Piece.Type.NaN)) {
                    returnList.add(board[squarePosition.getColumnCount() + deltaColumn][squarePosition.getRowCount() + deltaRow]);
                }
                // If pieces are of same color
                else if (board[squarePosition.getColumnCount() + deltaColumn][squarePosition.getRowCount() + deltaRow].getPiece().getColor().equals(
                        board[squarePosition.getColumnCount()][squarePosition.getRowCount()].getPiece().getColor()
                ) && firstMethodeCall) {
                    noOtherSquareInThisDirection = true;
                } else {
                    // On this square stands a piece of opposite color
                    returnList.add(board[squarePosition.getColumnCount() + deltaColumn][squarePosition.getRowCount() + deltaRow]);
                    noOtherSquareInThisDirection = true;
                }
            } else {
                noOtherSquareInThisDirection = true;
            }
        }
        return returnList;
    }

    /**
     * Returns squares to the lower left which the piece on the square position can enter
     *
     * @param squarePosition
     * @param firstMethodeCall
     * @return
     */
    private ArrayList<Square> getSquaresLeftDown(SquarePosition squarePosition, boolean firstMethodeCall, Square[][] board) {
        ArrayList<Square> returnList = new ArrayList<>();
        boolean noOtherSquareInThisDirection = false;
        int deltaRow = 0;
        int deltaColumn = 0;
        // Loop for bishop left down
        while (!noOtherSquareInThisDirection) {
            deltaColumn--;
            deltaRow++;
            // If square is still on the board
            if (squarePosition.getColumnCount() + deltaColumn != -1 && squarePosition.getRowCount() + deltaRow != 8) {
                // If square is empty
                if (board[squarePosition.getColumnCount() + deltaColumn][squarePosition.getRowCount() + deltaRow].getPiece().getType().equals(Piece.Type.NaN)) {
                    returnList.add(board[squarePosition.getColumnCount() + deltaColumn][squarePosition.getRowCount() + deltaRow]);
                }
                // If pieces are of same color
                else if (board[squarePosition.getColumnCount() + deltaColumn][squarePosition.getRowCount() + deltaRow].getPiece().getColor().equals(
                        board[squarePosition.getColumnCount()][squarePosition.getRowCount()].getPiece().getColor()
                ) && firstMethodeCall) {
                    noOtherSquareInThisDirection = true;
                } else {
                    // On this square stands a piece of opposite color
                    returnList.add(board[squarePosition.getColumnCount() + deltaColumn][squarePosition.getRowCount() + deltaRow]);
                    noOtherSquareInThisDirection = true;
                }
            } else {
                noOtherSquareInThisDirection = true;
            }
        }
        return returnList;
    }

    /**
     * Returns squares the knight can enter
     *
     * @param squarePosition
     * @param firstMethodeCall
     * @return
     */
    private ArrayList<Square> getSquaresOfKnight(SquarePosition squarePosition, boolean firstMethodeCall, Square[][] board) {
        ArrayList<Square> returnList = new ArrayList<>();
        // top left
        // if square is still on the board
        if (squarePosition.getColumnCount() - 1 > -1 && squarePosition.getRowCount() - 2 > -1) {
            // if piece at target square has not the same color
            if (!board[squarePosition.getColumnCount() - 1][squarePosition.getRowCount() - 2].getPiece().getColor().equals(
                    board[squarePosition.getColumnCount()][squarePosition.getRowCount()].getPiece().getColor()
            ) || !firstMethodeCall) {
                returnList.add(board[squarePosition.getColumnCount() - 1][squarePosition.getRowCount() - 2]);
            }
        }
        // top right
        // if square is still on the board
        if (squarePosition.getColumnCount() + 1 < 8 && squarePosition.getRowCount() - 2 > -1) {
            // if piece at target square has not the same color
            if (!board[squarePosition.getColumnCount() + 1][squarePosition.getRowCount() - 2].getPiece().getColor().equals(
                    board[squarePosition.getColumnCount()][squarePosition.getRowCount()].getPiece().getColor()
            ) || !firstMethodeCall) {
                returnList.add(board[squarePosition.getColumnCount() + 1][squarePosition.getRowCount() - 2]);
            }
        }
        // top middle left
        // if square is still on the board
        if (squarePosition.getColumnCount() - 2 > -1 && squarePosition.getRowCount() - 1 > -1) {
            // if piece at target square has not the same color
            if (!board[squarePosition.getColumnCount() - 2][squarePosition.getRowCount() - 1].getPiece().getColor().equals(
                    board[squarePosition.getColumnCount()][squarePosition.getRowCount()].getPiece().getColor()
            ) || !firstMethodeCall) {
                returnList.add(board[squarePosition.getColumnCount() - 2][squarePosition.getRowCount() - 1]);
            }
        }
        // top middle right
        // if square is still on the board
        if (squarePosition.getColumnCount() + 2 < 8 && squarePosition.getRowCount() - 1 > -1) {
            // if piece at target square has not the same color
            if (!board[squarePosition.getColumnCount() + 2][squarePosition.getRowCount() - 1].getPiece().getColor().equals(
                    board[squarePosition.getColumnCount()][squarePosition.getRowCount()].getPiece().getColor()
            ) || !firstMethodeCall) {
                returnList.add(board[squarePosition.getColumnCount() + 2][squarePosition.getRowCount() - 1]);
            }
        }
        // bottom middle left
        // if square is still on the board
        if (squarePosition.getColumnCount() - 2 > -1 && squarePosition.getRowCount() + 1 < 8) {
            // if piece at target square has not the same color
            if (!board[squarePosition.getColumnCount() - 2][squarePosition.getRowCount() + 1].getPiece().getColor().equals(
                    board[squarePosition.getColumnCount()][squarePosition.getRowCount()].getPiece().getColor()
            ) || !firstMethodeCall) {
                returnList.add(board[squarePosition.getColumnCount() - 2][squarePosition.getRowCount() + 1]);
            }
        }
        // bottom middle right
        // if square is still on the board
        if (squarePosition.getColumnCount() + 2 < 8 && squarePosition.getRowCount() + 1 < 8) {
            // if piece at target square has not the same color
            if (!board[squarePosition.getColumnCount() + 2][squarePosition.getRowCount() + 1].getPiece().getColor().equals(
                    board[squarePosition.getColumnCount()][squarePosition.getRowCount()].getPiece().getColor()
            ) || !firstMethodeCall) {
                returnList.add(board[squarePosition.getColumnCount() + 2][squarePosition.getRowCount() + 1]);
            }
        }
        // bottom left
        // if square is still on the board
        if (squarePosition.getColumnCount() - 1 > -1 && squarePosition.getRowCount() + 2 < 8) {
            // if piece at target square has not the same color
            if (!board[squarePosition.getColumnCount() - 1][squarePosition.getRowCount() + 2].getPiece().getColor().equals(
                    board[squarePosition.getColumnCount()][squarePosition.getRowCount()].getPiece().getColor()
            ) || !firstMethodeCall) {
                returnList.add(board[squarePosition.getColumnCount() - 1][squarePosition.getRowCount() + 2]);
            }
        }
        // bottom right
        // if square is still on the board
        if (squarePosition.getColumnCount() + 1 < 8 && squarePosition.getRowCount() + 2 < 8) {
            // if piece at target square has not the same color
            if (!board[squarePosition.getColumnCount() + 1][squarePosition.getRowCount() + 2].getPiece().getColor().equals(
                    board[squarePosition.getColumnCount()][squarePosition.getRowCount()].getPiece().getColor()
            ) || !firstMethodeCall) {
                returnList.add(board[squarePosition.getColumnCount() + 1][squarePosition.getRowCount() + 2]);
            }
        }
        return returnList;
    }

    /**
     * Returns squares the king can enter
     *
     * @param squarePosition
     * @param firstMethodeCall
     * @return
     */
    private ArrayList<Square> getSquaresOfKing(SquarePosition squarePosition, boolean firstMethodeCall, Square[][] board, ArrayList<Move> moveList) {
        ArrayList<Square> returnList = new ArrayList<>();
        // check top left
        if (squarePosition.getColumnCount() - 1 > -1 && squarePosition.getRowCount() - 1 > -1) {
            if (!board[squarePosition.getColumnCount() - 1][squarePosition.getRowCount() - 1].getPiece().getColor().equals(
                    board[squarePosition.getColumnCount()][squarePosition.getRowCount()].getPiece().getColor()
            ) || !firstMethodeCall) {
                returnList.add(board[squarePosition.getColumnCount() - 1][squarePosition.getRowCount() - 1]);
            }
        }
        // check top middle
        if (squarePosition.getRowCount() - 1 > -1) {
            if (!board[squarePosition.getColumnCount()][squarePosition.getRowCount() - 1].getPiece().getColor().equals(
                    board[squarePosition.getColumnCount()][squarePosition.getRowCount()].getPiece().getColor()
            ) || !firstMethodeCall) {
                returnList.add(board[squarePosition.getColumnCount()][squarePosition.getRowCount() - 1]);
            }
        }
        // check top right
        if (squarePosition.getColumnCount() + 1 < 8 && squarePosition.getRowCount() - 1 > -1) {
            if (!board[squarePosition.getColumnCount() + 1][squarePosition.getRowCount() - 1].getPiece().getColor().equals(
                    board[squarePosition.getColumnCount()][squarePosition.getRowCount()].getPiece().getColor()
            ) || !firstMethodeCall) {
                returnList.add(board[squarePosition.getColumnCount() + 1][squarePosition.getRowCount() - 1]);
            }
        }
        // check middle left
        if (squarePosition.getColumnCount() - 1 > -1) {
            if (!board[squarePosition.getColumnCount() - 1][squarePosition.getRowCount()].getPiece().getColor().equals(
                    board[squarePosition.getColumnCount()][squarePosition.getRowCount()].getPiece().getColor()
            ) || !firstMethodeCall) {
                returnList.add(board[squarePosition.getColumnCount() - 1][squarePosition.getRowCount()]);
            }
        }
        // check middle right
        if (squarePosition.getColumnCount() + 1 < 8) {
            if (!board[squarePosition.getColumnCount() + 1][squarePosition.getRowCount()].getPiece().getColor().equals(
                    board[squarePosition.getColumnCount()][squarePosition.getRowCount()].getPiece().getColor()
            ) || !firstMethodeCall) {
                returnList.add(board[squarePosition.getColumnCount() + 1][squarePosition.getRowCount()]);
            }
        }
        // check bottom left
        if (squarePosition.getColumnCount() - 1 > -1 && squarePosition.getRowCount() + 1 < 8) {
            if (!board[squarePosition.getColumnCount() - 1][squarePosition.getRowCount() + 1].getPiece().getColor().equals(
                    board[squarePosition.getColumnCount()][squarePosition.getRowCount()].getPiece().getColor()
            ) || !firstMethodeCall) {
                returnList.add(board[squarePosition.getColumnCount() - 1][squarePosition.getRowCount() + 1]);
            }
        }
        // check bottom middle
        if (squarePosition.getRowCount() + 1 < 8) {
            if (!board[squarePosition.getColumnCount()][squarePosition.getRowCount() + 1].getPiece().getColor().equals(
                    board[squarePosition.getColumnCount()][squarePosition.getRowCount()].getPiece().getColor()
            ) || !firstMethodeCall) {
                returnList.add(board[squarePosition.getColumnCount()][squarePosition.getRowCount() + 1]);
            }
        }
        // check bottom right
        if (squarePosition.getColumnCount() + 1 < 8 && squarePosition.getRowCount() + 1 < 8) {
            if (!board[squarePosition.getColumnCount() + 1][squarePosition.getRowCount() + 1].getPiece().getColor().equals(
                    board[squarePosition.getColumnCount()][squarePosition.getRowCount()].getPiece().getColor()
            ) || !firstMethodeCall) {
                returnList.add(board[squarePosition.getColumnCount() + 1][squarePosition.getRowCount() + 1]);
            }
        }
        if (firstMethodeCall) {
            // check for short castle
            if (checkForShortCastle(squarePosition, board, moveList)) {
                returnList.add(board[squarePosition.getColumnCount() + 2][squarePosition.getRowCount()]);
            }
            // check for long castle
            if (checkForLongCastle(squarePosition, board, moveList)) {
                returnList.add(board[squarePosition.getColumnCount() - 2][squarePosition.getRowCount()]);
            }
        }
        // remove all covered squares
        if (firstMethodeCall) {
            returnList.removeAll(getAllCoveredSquares(board[squarePosition.getColumnCount()][squarePosition.getRowCount()].getPiece().getColor(), board, moveList));
        }
        return returnList;
    }

    /**
     * Returns squares the pawn can enter
     *
     * @param squarePosition
     * @param firstMethodeCall
     * @return
     */
    private ArrayList<Square> getSquaresOfPawn(SquarePosition squarePosition, boolean firstMethodeCall, Square[][] board, ArrayList<Move> moveList) {
        ArrayList<Square> returnList = new ArrayList<>();
        // White pawn
        if (board[squarePosition.getColumnCount()][squarePosition.getRowCount()].getPiece().getColor().equals(Piece.Color.W)) {
            // If not at the end of the board
            if (squarePosition.getRowCount() != 0) {
                // Check for en passant
                if (moveList.size() > 0) {
                    int rowDelta = moveList.get(moveList.size() - 1).getFromSquare().getsquarePosition().getRowCount() -
                            moveList.get(moveList.size() - 1).getToSquare().getsquarePosition().getRowCount();
                    if (moveList.get(moveList.size() - 1).getPiece().getType().equals(Piece.Type.P) &&
                            rowDelta == -2) {
                        if ((squarePosition.getColumnCount() == moveList.get(moveList.size() - 1).getToSquare().getsquarePosition().getColumnCount() + 1
                                || squarePosition.getColumnCount() == moveList.get(moveList.size() - 1).getToSquare().getsquarePosition().getColumnCount() - 1)
                                && squarePosition.getRowCount() == moveList.get(moveList.size() - 1).getToSquare().getsquarePosition().getRowCount()) {
                            returnList.add(board[moveList.get(moveList.size() - 1).getToSquare().getsquarePosition().getColumnCount()][moveList.get(moveList.size() - 1).getToSquare().getsquarePosition().getRowCount() - 1]);
                        }
                    }
                }
                // If square in front is empty
                if (board[squarePosition.getColumnCount()][squarePosition.getRowCount() - 1].getPiece().getType().equals(Piece.Type.NaN) && firstMethodeCall) {
                    returnList.add(board[squarePosition.getColumnCount()][squarePosition.getRowCount() - 1]);
                    // If on second rank
                    if (squarePosition.getRowCount() == 6) {
                        if (board[squarePosition.getColumnCount()][squarePosition.getRowCount() - 2].getPiece().getType().equals(Piece.Type.NaN)) {
                            returnList.add(board[squarePosition.getColumnCount()][squarePosition.getRowCount() - 2]);
                        }
                    }
                }
                // If enemy piece left of pawn
                if (squarePosition.getColumnCount() != 0) {
                    if (board[squarePosition.getColumnCount() - 1][squarePosition.getRowCount() - 1].getPiece().getColor().equals(Piece.Color.B)) {
                        returnList.add(board[squarePosition.getColumnCount() - 1][squarePosition.getRowCount() - 1]);
                    }
                }
                // If enemy piece right of pawn
                if (squarePosition.getColumnCount() != 7) {
                    if (board[squarePosition.getColumnCount() + 1][squarePosition.getRowCount() - 1].getPiece().getColor().equals(Piece.Color.B)) {
                        returnList.add(board[squarePosition.getColumnCount() + 1][squarePosition.getRowCount() - 1]);
                    }
                }
            }
        } else {
            // If not at the end of the board
            if (squarePosition.getRowCount() != 7) {
                // Check for en passant
                if (moveList.size() > 0) {
                    int rowDelta = moveList.get(moveList.size() - 1).getFromSquare().getsquarePosition().getRowCount() -
                            moveList.get(moveList.size() - 1).getToSquare().getsquarePosition().getRowCount();
                    if (moveList.get(moveList.size() - 1).getPiece().getType().equals(Piece.Type.P) &&
                            rowDelta == +2) {
                        if ((squarePosition.getColumnCount() == moveList.get(moveList.size() - 1).getToSquare().getsquarePosition().getColumnCount() + 1
                                || squarePosition.getColumnCount() == moveList.get(moveList.size() - 1).getToSquare().getsquarePosition().getColumnCount() - 1)
                                && squarePosition.getRowCount() == moveList.get(moveList.size() - 1).getToSquare().getsquarePosition().getRowCount()) {
                            returnList.add(board[moveList.get(moveList.size() - 1).getToSquare().getsquarePosition().getColumnCount()][moveList.get(moveList.size() - 1).getToSquare().getsquarePosition().getRowCount() + 1]);
                        }
                    }
                }
                // If square in front is empty
                if (board[squarePosition.getColumnCount()][squarePosition.getRowCount() + 1].getPiece().getType().equals(Piece.Type.NaN) && firstMethodeCall) {
                    returnList.add(board[squarePosition.getColumnCount()][squarePosition.getRowCount() + 1]);
                    // If on second rank
                    if (squarePosition.getRowCount() == 1) {
                        if (board[squarePosition.getColumnCount()][squarePosition.getRowCount() + 2].getPiece().getType().equals(Piece.Type.NaN)) {
                            returnList.add(board[squarePosition.getColumnCount()][squarePosition.getRowCount() + 2]);
                        }
                    }
                }
                // If enemy piece left of pawn
                if (squarePosition.getColumnCount() != 0) {
                    if (board[squarePosition.getColumnCount() - 1][squarePosition.getRowCount() + 1].getPiece().getColor().equals(Piece.Color.W)) {
                        returnList.add(board[squarePosition.getColumnCount() - 1][squarePosition.getRowCount() + 1]);
                    }
                }
                // If enemy piece right of pawn
                if (squarePosition.getColumnCount() != 7) {
                    if (board[squarePosition.getColumnCount() + 1][squarePosition.getRowCount() + 1].getPiece().getColor().equals(Piece.Color.W)) {
                        returnList.add(board[squarePosition.getColumnCount() + 1][squarePosition.getRowCount() + 1]);
                    }
                }
            }
        }
        return returnList;
    }

    /**
     * Returns a list of all squares covered by the other color
     *
     * @param color Color of the pieces
     * @return ArrayList of squares
     */
    private ArrayList<Square> getAllCoveredSquares(Piece.Color color, Square[][] board, ArrayList<Move> moveList) {
        ArrayList<Square> coveredSquares = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            for (int i2 = 0; i2 < 8; i2++) {
                SquarePosition squarePosition = new SquarePosition(i, i2);
                if (!board[squarePosition.getColumnCount()][squarePosition.getRowCount()].getPiece().getColor().equals(color))
                    coveredSquares.addAll(getSquaresPieceCanEnter(squarePosition, false, board, moveList));
            }
        }
        return coveredSquares;
    }

    /**
     * Checks if the king of the given color has already moved
     *
     * @param color
     * @param moveList
     * @return
     */
    public boolean checkIfKingMoved(Piece.Color color, ArrayList<Move> moveList) {
        for (Move m : moveList) {
            if (m.getPiece().getColor().equals(color) && m.getPiece().getType().equals(Piece.Type.K)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if the rock on the A file of the given color has already moved
     *
     * @param color
     * @param moveList
     * @return
     */
    public boolean checkIfARockMoved(Piece.Color color, ArrayList<Move> moveList) {
        for (Move m : moveList) {
            if (m.getPiece().getColor().equals(color) && m.getPiece().getType().equals(Piece.Type.R) && m.getFromSquare().getsquarePosition().getColumnCount() == 0) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if the rock on the H file of the given color has already moved
     *
     * @param color
     * @param moveList
     * @return
     */
    public boolean checkIfHRockMoved(Piece.Color color, ArrayList<Move> moveList) {
        for (Move m : moveList) {
            if (m.getPiece().getColor().equals(color) && m.getPiece().getType().equals(Piece.Type.R) && m.getFromSquare().getsquarePosition().getColumnCount() == 7) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if in this position short castle is a legal move
     *
     * @param squarePosition
     * @param board
     * @param moveList
     * @return
     */
    private boolean checkForShortCastle(SquarePosition squarePosition, Square[][] board, ArrayList<Move> moveList) {
        boolean spaceBetweenKAndRIsEmpty = false;
        boolean spaceBetweenKAndRIsUncovered = false;
        ArrayList<Square> squaresForCastle = new ArrayList<>();
        if (!checkIfKingMoved(board[squarePosition.getColumnCount()][squarePosition.getRowCount()].getPiece().getColor(), moveList)) {
            for (int i = 0; i < 3; i++) {
                squaresForCastle.add(board[squarePosition.getColumnCount() + i][squarePosition.getRowCount()]);
            }
            if (getAllCoveredSquares(board[squarePosition.getColumnCount()][squarePosition.getRowCount()].getPiece().getColor(), board, moveList).stream().noneMatch(squaresForCastle::contains)) {
                spaceBetweenKAndRIsUncovered = true;
            }
            if (board[squarePosition.getColumnCount() + 1][squarePosition.getRowCount()].getPiece().getType().equals(Piece.Type.NaN) &&
                    board[squarePosition.getColumnCount() + 2][squarePosition.getRowCount()].getPiece().getType().equals(Piece.Type.NaN)) {
                spaceBetweenKAndRIsEmpty = true;
            }
        }
        return !checkIfHRockMoved(board[squarePosition.getColumnCount()][squarePosition.getRowCount()].getPiece().getColor(), moveList) &&
                spaceBetweenKAndRIsEmpty &&
                spaceBetweenKAndRIsUncovered;
    }

    /**
     * Checks if in this position long castle is a legal move
     *
     * @param squarePosition
     * @param board
     * @param moveList
     * @return
     */
    private boolean checkForLongCastle(SquarePosition squarePosition, Square[][] board, ArrayList<Move> moveList) {
        boolean spaceBetweenKAndRIsEmpty = false;
        boolean spaceBetweenKAndRIsUncovered = false;
        ArrayList<Square> squaresForCastle = new ArrayList<>();
        if (!checkIfKingMoved(board[squarePosition.getColumnCount()][squarePosition.getRowCount()].getPiece().getColor(), moveList)) {
            for (int i = 0; i > -3; i--) {
                squaresForCastle.add(board[squarePosition.getColumnCount() + i][squarePosition.getRowCount()]);
            }
            if (getAllCoveredSquares(board[squarePosition.getColumnCount()][squarePosition.getRowCount()].getPiece().getColor(), board, moveList).stream().noneMatch(squaresForCastle::contains)) {
                spaceBetweenKAndRIsUncovered = true;
            }
            if (board[squarePosition.getColumnCount() - 1][squarePosition.getRowCount()].getPiece().getType().equals(Piece.Type.NaN) &&
                    board[squarePosition.getColumnCount() - 2][squarePosition.getRowCount()].getPiece().getType().equals(Piece.Type.NaN)) {
                spaceBetweenKAndRIsEmpty = true;
            }
        }
        return !checkIfARockMoved(board[squarePosition.getColumnCount()][squarePosition.getRowCount()].getPiece().getColor(), moveList) &&
                spaceBetweenKAndRIsEmpty &&
                spaceBetweenKAndRIsUncovered;
    }

    /**
     * Returns a clone of the real board
     *
     * @param board
     * @return
     */
    private Square[][] getCopyOfBoard(Square[][] board) {
        Square[][] copyBoard = new Square[8][8];
        for (int i = 0; i < 8; i++) {
            for (int i2 = 0; i2 < 8; i2++) {
                Piece piece = board[i2][i].getPiece();
                SquarePosition squarePosition = board[i2][i].getsquarePosition();
                copyBoard[i2][i] = new Square();
                copyBoard[i2][i].setPiece(new Piece(piece.getType(), piece.getColor()));
                copyBoard[i2][i].setsquarePosition(new SquarePosition(squarePosition.getColumnCount(), squarePosition.getRowCount()));
            }
        }
        return copyBoard;
    }

    /**
     * Checks if the performed move is an en passant
     *
     * @param squarePosition
     * @param moveList
     * @param selectedSquare
     * @param board
     * @return
     */
    public boolean checkForEnPassant(SquarePosition squarePosition, ArrayList<Move> moveList, Square selectedSquare, Square[][] board) {
        // If it's not the first move
        if (moveList.size() > 0) {
            // If moved piece is a king
            return board[selectedSquare.getsquarePosition().getColumnCount()][selectedSquare.getsquarePosition().getRowCount()].getPiece().getType().equals(Piece.Type.P)
                    // If en passanted piece is a pawn
                    && (moveList.get(moveList.size() - 1).getPiece().getType().equals(Piece.Type.P))
                    // If the square position is between the start and the destination from the last pawn move
                    && ((moveList.get(moveList.size() - 1).getToSquare().getsquarePosition().getRowCount() > squarePosition.getRowCount()
                    && moveList.get(moveList.size() - 1).getFromSquare().getsquarePosition().getRowCount() < squarePosition.getRowCount())
                    || (moveList.get(moveList.size() - 1).getToSquare().getsquarePosition().getRowCount() < squarePosition.getRowCount()
                    && moveList.get(moveList.size() - 1).getFromSquare().getsquarePosition().getRowCount() > squarePosition.getRowCount()))
                    // If the column of the pawns match
                    && (moveList.get(moveList.size() - 1).getToSquare().getsquarePosition().getColumnCount() == squarePosition.getColumnCount());
        }
        return false;
    }

    /**
     * Checks if the performed move is a short castle
     *
     * @param squarePosition
     * @param board
     * @param selectedSquare
     * @return
     */
    public boolean checkForShortCastle(SquarePosition squarePosition, Square[][] board, Square selectedSquare) {
        // If moved piece is a pawn
        return board[selectedSquare.getsquarePosition().getColumnCount()][selectedSquare.getsquarePosition().getRowCount()].getPiece().getType().equals(Piece.Type.K)
                // If king moved 2 to the right
                && (selectedSquare.getsquarePosition().getColumnCount() - squarePosition.getColumnCount() == -2);
    }

    /**
     * Checks if the performed move is a long castle
     *
     * @param squarePosition
     * @param board
     * @param selectedSquare
     * @return
     */
    public boolean checkForLongCastle(SquarePosition squarePosition, Square[][] board, Square selectedSquare) {
        // If moved piece is a pawn
        return board[selectedSquare.getsquarePosition().getColumnCount()][selectedSquare.getsquarePosition().getRowCount()].getPiece().getType().equals(Piece.Type.K)
                // If king moved 2 to the right
                && (selectedSquare.getsquarePosition().getColumnCount() - squarePosition.getColumnCount() == +2);
    }

    /**
     * Returns all squares which can be entered with any piece of the given color
     * @param board
     * @param isWhite
     * @param moveList
     * @return
     */
    public ArrayList<Square> getSquaresWhichCanBeEnteredWithAnyPiece(Square[][] board, boolean isWhite, ArrayList<Move> moveList){
        ArrayList<Square> squares = new ArrayList<>();
        ArrayList<SquarePosition> positions = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            for (int i2 = 0; i2 < 8; i2++) {
                Square square = board[i2][i];
                if (square.getPiece().getColor().equals(Piece.Color.W) && isWhite){
                    positions.add(square.getsquarePosition());
                }
                if (square.getPiece().getColor().equals(Piece.Color.B) && !isWhite){
                    positions.add(square.getsquarePosition());
                }
            }
        }
        for (SquarePosition p:positions){
            squares.addAll(getSquaresPieceCanEnter(p, true, board, moveList));
        }
        return squares;
    }

    public boolean checkForCheck(Piece.Color color, Square[][] board, ArrayList<Move> moveList){
        ArrayList<Square> squares;
        if (color.equals(Piece.Color.W)){
            squares = getSquaresWhichCanBeEnteredWithAnyPiece(board, false, moveList);
            for (Square s:squares){
                if (s.getPiece().getColor().equals(Piece.Color.W) && s.getPiece().getType().equals(Piece.Type.K)){
                    return true;
                }
            }
            return false;
        } else {
            squares = getSquaresWhichCanBeEnteredWithAnyPiece(board, true, moveList);
            for (Square s:squares){
                if (s.getPiece().getColor().equals(Piece.Color.B) && s.getPiece().getType().equals(Piece.Type.K)){
                    return true;
                }
            }
        }
        return false;
    }
}