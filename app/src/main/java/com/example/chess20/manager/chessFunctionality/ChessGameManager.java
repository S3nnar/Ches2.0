package com.example.chess20.manager.chessFunctionality;

import android.annotation.SuppressLint;
import android.content.res.Resources;
import android.graphics.*;
import android.graphics.drawable.Drawable;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import androidx.core.content.res.ResourcesCompat;
import com.example.chess20.R;
import com.example.chess20.manager.PieceCounter;
import com.example.chess20.manager.gameSaver.PositionFileManager;
import com.example.chess20.model.*;

import java.io.IOException;
import java.util.ArrayList;

public class ChessGameManager {

    private Square selectedSquare;
    private Board board;
    private PieceCounter pieceCounter;
    private boolean waitForPromotion;
    private boolean whiteOffersTakeBack;
    private boolean blackOffersTakeBack;
    private String position;
    private ChessGameHelper chessGameHelper;
    private ChessGamePieceDrawer chessGamePieceDrawer;
    private ChessGamePromotion chessGamePromotion;
    private PositionFileManager positionFileManager;

    public ChessGameManager(String dir) throws IOException {
        // Hardcoded position -> TODO: Replace with given position
        position = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR";
        // Init chess helper classes
        chessGameHelper = new ChessGameHelper();
        chessGamePieceDrawer = new ChessGamePieceDrawer();
        chessGamePromotion = new ChessGamePromotion();
        pieceCounter = new PieceCounter();
        // Init position FileManager
        positionFileManager = new PositionFileManager(dir, PositionFileManager.GameType.CHESS_POSITION);
        // Init the board with given position
        board = new Board(position);
        // Init selected square -> TODO: Make it possible to use a given square
        selectedSquare = chessGameHelper.initSelectedSquare();
        // Init booleans for promotion and takeBack
        waitForPromotion = false;
        whiteOffersTakeBack = false;
        blackOffersTakeBack = false;
    }

    /**
     * Initializes the canvas new (clears it) and places the pieces
     * @param resources
     * @param squareSize
     * @param boardHeight
     * @param imageView
     * @param backgroundColor
     * @param bitmapCanvasPair
     * @param firstCall
     * @return
     */
    public BitmapCanvasPair initChessBoardAndPlacePieces(Resources resources, double squareSize, int boardHeight, ImageView imageView, Paint backgroundColor, BitmapCanvasPair bitmapCanvasPair, boolean firstCall){
        if (firstCall){
            bitmapCanvasPair = initChessBoard(squareSize, boardHeight, backgroundColor, chessGameHelper.initBitmapAndCanvas(squareSize));
        } else {
            bitmapCanvasPair = initChessBoard(squareSize, boardHeight, backgroundColor, bitmapCanvasPair);
        }
        chessGamePieceDrawer.placePieces(bitmapCanvasPair.getCanvas(), resources, squareSize, boardHeight, position);
        imageView.setImageBitmap(bitmapCanvasPair.getBitmap());
        return bitmapCanvasPair;
    }

    /**
     * Initializes the colors of the chess board
     */
    private BitmapCanvasPair initChessBoard(double squareSize, int boardHeight, Paint backgroundColor, BitmapCanvasPair bitmapCanvasPair) {
        setBackgroundColor(backgroundColor, bitmapCanvasPair.getCanvas());
        // Init Paint dark squares
        Paint paintDark = new Paint();
        paintDark.setColor(Color.rgb(110, 82, 57));
        // Init Paint bright squares
        Paint paintBright = new Paint();
        paintBright.setColor(Color.rgb(227, 217, 209));
        // Init chess board
        // Init bright squares
        float startX = 0;
        float startY = boardHeight;
        for (int i = 0; i < 32; i++) {
            bitmapCanvasPair.getCanvas().drawRect(startX, startY + (int)squareSize, startX + (int)squareSize, startY + ((int)squareSize*2), paintBright);
            startX += ((int)squareSize*2);
            if (startX == (int)squareSize*8) {
                startX = (int)squareSize;
                startY += (int)squareSize;
            } else if (startX > (int)squareSize*8) {
                startX = 0;
                startY += (int)squareSize;
            }
        }
        // Init dark squares
        startX = (int)squareSize;
        startY = boardHeight;
        for (int i = 0; i < 32; i++) {
            bitmapCanvasPair.getCanvas().drawRect(startX, startY + (int)squareSize, startX + (int)squareSize, startY + ((int)squareSize*2), paintDark);
            startX += ((int)squareSize*2);
            if (startX == (int)squareSize*8) {
                startX = (int)squareSize;
                startY += (int)squareSize;
            } else if (startX > squareSize*8) {
                startX = 0;
                startY += squareSize;
            }
        }
        return new BitmapCanvasPair(bitmapCanvasPair.getBitmap(), bitmapCanvasPair.getCanvas());
    }

    /**
     * Paints the background in the given color
     * @param paint
     */
    private void setBackgroundColor(Paint paint, Canvas canvas) {
//        paint.setColor(Color.rgb(74, 71, 61));
        canvas.drawRect(0, 0, 0, 0, paint);
        canvas.drawRect(0, 0, 1440, 3000, paint);
    }

    public boolean imageViewOnTouchEvent(MotionEvent motionEvent, Resources resources, double squareSize, int boardHeight, ImageView imageView, BitmapCanvasPair bitmapCanvasPair, Paint backgroundColor) {
        if (waitForPromotion){
            promote(motionEvent.getX(), motionEvent.getY(), resources, squareSize, boardHeight, imageView,bitmapCanvasPair, backgroundColor);
        } else {
            markSquares(getSquarePositionFromCoordinates(motionEvent.getX(), motionEvent.getY()), imageView, resources, squareSize, boardHeight, backgroundColor, bitmapCanvasPair);
        }
        return false;
    }

    /**
     * Replaces the promoting pawn with the selected piece
     * @param x
     * @param y
     */
    private void promote(float x, float y, Resources resources, double squareSize, int boardHeight, ImageView imageView, BitmapCanvasPair bitmapCanvasPair, Paint backgroundColor) {
        Move lastMove = board.getMoveList().get(board.getMoveList().size()-1);
        SquarePosition squarePositionLastMove = lastMove.getToSquare().getsquarePosition();
        if (board.isWhitesMove()){
            Piece.Type pieceToPromote = checkWhitePromotionTarget(x, y);
            board.getBoard()[squarePositionLastMove.getColumnCount()][squarePositionLastMove.getRowCount()].setPiece(new Piece(pieceToPromote, Piece.Color.W));
        } else {
            Piece.Type pieceToPromote = checkBlackPromotionTarget(x, y);
            board.getBoard()[squarePositionLastMove.getColumnCount()][squarePositionLastMove.getRowCount()].setPiece(new Piece(pieceToPromote, Piece.Color.B));
        }
        waitForPromotion = false;
        position = getPositionFromBoard();
        if (board.isWhitesMove()) {
            board.setWhitesMove(false);
        } else {
            board.setWhitesMove(true);
        }
        initChessBoardAndPlacePieces(resources, squareSize, boardHeight, imageView, backgroundColor, bitmapCanvasPair, false);
        updateImageView(imageView, resources, squareSize, boardHeight, bitmapCanvasPair);
    }

    private Piece.Type checkWhitePromotionTarget(float x, float y) {
        if (x < 720 && y < 1030){
            return Piece.Type.B;
        } else if (x > 720 && y < 1030){
            return Piece.Type.N;
        } else if (x < 720 && y > 1030){
            return Piece.Type.R;
        } else {
            return Piece.Type.Q;
        }
    }

    private Piece.Type checkBlackPromotionTarget(float x, float y) {
        if (x < 720 && y < 1030){
            return Piece.Type.Q;
        } else if (x > 720 && y < 1030){
            return Piece.Type.R;
        } else if (x < 720 && y > 1030){
            return Piece.Type.N;
        } else {
            return Piece.Type.B;
        }
    }

    /**
     * Reads the position from the board
     *
     * @return
     */
    private String getPositionFromBoard() {
        String position = "";
        for (int i = 0; i < 8; i++) {
            int emptySquareCounter = 0;
            for (int i2 = 0; i2 < 8; i2++) {
                if (board.getBoard()[i2][i].getPiece().getType().equals(Piece.Type.NaN)) {
                    emptySquareCounter++;
                } else {
                    if (emptySquareCounter != 0) {
                        position += "" + emptySquareCounter;
                        emptySquareCounter = 0;
                    }
                    switch (board.getBoard()[i2][i].getPiece().getType()) {
                        case R:
                            if (board.getBoard()[i2][i].getPiece().getColor().equals(Piece.Color.W)) {
                                position += "R";
                            } else {
                                position += "r";
                            }
                            break;
                        case N:
                            if (board.getBoard()[i2][i].getPiece().getColor().equals(Piece.Color.W)) {
                                position += "N";
                            } else {
                                position += "n";
                            }
                            break;
                        case B:
                            if (board.getBoard()[i2][i].getPiece().getColor().equals(Piece.Color.W)) {
                                position += "B";
                            } else {
                                position += "b";
                            }
                            break;
                        case K:
                            if (board.getBoard()[i2][i].getPiece().getColor().equals(Piece.Color.W)) {
                                position += "K";
                            } else {
                                position += "k";
                            }
                            break;
                        case Q:
                            if (board.getBoard()[i2][i].getPiece().getColor().equals(Piece.Color.W)) {
                                position += "Q";
                            } else {
                                position += "q";
                            }
                            break;
                        case P:
                            if (board.getBoard()[i2][i].getPiece().getColor().equals(Piece.Color.W)) {
                                position += "P";
                            } else {
                                position += "p";
                            }
                            break;
                    }
                }
            }
            if (emptySquareCounter != 0) {
                position += "" + emptySquareCounter;
            }
            position += "/";
        }
        position += " ";
        if (board.isWhitesMove()){
            position += "w ";
        } else {
            position += "b ";
        }
        if (board.checkIfKingMoved(Piece.Color.W)){
            if (board.checkIfARockMoved(Piece.Color.W)){
                position += "K";
            }
            if (board.checkIfHRockMoved(Piece.Color.W)){
                position += "Q";
            }
        }
        if (board.checkIfKingMoved(Piece.Color.B)){
            if (board.checkIfARockMoved(Piece.Color.B)){
                position += "k";
            }
            if (board.checkIfHRockMoved(Piece.Color.B)){
                position += "q";
            }
        }
        //TODO: Enpassant -> Angabe des übersprungenen Feldes
        //TODO: Halbzüge -> Angabe der Anzahl von Zügen in welchen kein Bauer bewegt oder eine Figur geschlagen wurde
        //TODO: Zugnummer -> Nummer des Zuges (erster ist 1)
        return position;
    }

    /**
     * Updates the imageView and adds the piece difference
     */
    private void updateImageView(ImageView imageView, Resources resources, double squareSize, int boardHeight, BitmapCanvasPair bitmapCanvasPair) {
        highlightLastMove(squareSize, boardHeight, bitmapCanvasPair.getCanvas());
        addPieceDifference(board.getBoard(), resources, imageView, bitmapCanvasPair);
        imageView.setImageBitmap(bitmapCanvasPair.getBitmap());
    }

    /**
     * Highlights the last move
     */
    private void highlightLastMove(double squareSize, int boardHeight, Canvas canvas) {
        if (board.getMoveList().size() > 0) {
            Paint paint = new Paint();
            paint.setColor(Color.rgb(83, 139, 230));
            paint.setAlpha(100);
            paintSquare(board.getMoveList().get(board.getMoveList().size() - 1).getFromSquare().getsquarePosition(), paint, squareSize, boardHeight, canvas);
            paintSquare(board.getMoveList().get(board.getMoveList().size() - 1).getToSquare().getsquarePosition(), paint, squareSize, boardHeight, canvas);
        }
    }

    /**
     * Method to draw a square
     *
     * @param squarePosition
     * @param paint
     */
    private void paintSquare(SquarePosition squarePosition, Paint paint, double squareSize, int boardHeight, Canvas canvas) {
        Rect square = new Rect(squarePosition.getColumnCount() * (int)squareSize, squarePosition.getRowCount() * (int)squareSize + boardHeight+ (int)squareSize,
                squarePosition.getColumnCount() * (int)squareSize + (int)squareSize, squarePosition.getRowCount() * (int)squareSize  + boardHeight+ (int)squareSize*2);
        canvas.drawRect(square, paint);
    }

    /**
     * Prints the piece difference under the board
     *
     * @param board
     */
    @SuppressLint("SetTextI18n")
    private void addPieceDifference(Square[][] board, Resources resources, ImageView imageView, BitmapCanvasPair bitmapCanvasPair) {
        PieceDifference pieceDifference = pieceCounter.getAllPieces(board);
        int pieceTypesWhite = 0;
        int pieceTypesBlack = 0;
        int size = 150;
        int distanceOfSamePieces = 15;
        int spaceBetweenPieces = 100;
        Paint scorePaint = new Paint();
        scorePaint.setColor(Color.rgb(255, 255, 255));
        scorePaint.setTextSize(100);
        if (pieceDifference.getPawn() > 0) {
            for (int i = pieceDifference.getPawn() - 1; i >= 0; i--) {
                Drawable wp = ResourcesCompat.getDrawable(resources, R.drawable.wp, null);
                wp.setBounds((i * distanceOfSamePieces), 2000, (i * distanceOfSamePieces) + size, 2000 + size);
                wp.draw(bitmapCanvasPair.getCanvas());
            }
            pieceTypesWhite++;
        }
        if (pieceDifference.getPawn() < 0) {
            for (int i = (pieceDifference.getPawn() * -1) - 1; i >= 0; i--) {
                Drawable bp = ResourcesCompat.getDrawable(resources, R.drawable.bp_rotated, null);
                bp.setBounds((i * distanceOfSamePieces), 250,
                        (i * distanceOfSamePieces) + size, 250 + size);
                bp.draw(bitmapCanvasPair.getCanvas());
            }
            pieceTypesBlack++;
        }
        if (pieceDifference.getKnight() > 0) {
            for (int i = pieceDifference.getKnight() - 1; i >= 0; i--) {
                Drawable wn = ResourcesCompat.getDrawable(resources, R.drawable.wn, null);
                int additionalSpace = pieceCounter.getNumberOfPiecesBeforeKnight(pieceDifference, true) * distanceOfSamePieces;
                wn.setBounds((i * distanceOfSamePieces) + additionalSpace + pieceTypesWhite * spaceBetweenPieces, 2000,
                        (i * distanceOfSamePieces) + size + additionalSpace + pieceTypesWhite * spaceBetweenPieces, 2000 + size);
                wn.draw(bitmapCanvasPair.getCanvas());
            }
            pieceTypesWhite++;
        }
        if (pieceDifference.getKnight() < 0) {
            for (int i = (pieceDifference.getKnight() * -1) - 1; i >= 0; i--) {
                Drawable bn = ResourcesCompat.getDrawable(resources, R.drawable.bn_rotated, null);
                int additionalSpace = pieceCounter.getNumberOfPiecesBeforeKnight(pieceDifference, false) * -1 * distanceOfSamePieces;
                bn.setBounds((i * distanceOfSamePieces) + additionalSpace + pieceTypesBlack * spaceBetweenPieces, 250,
                        (i * distanceOfSamePieces) + size + additionalSpace + pieceTypesBlack * spaceBetweenPieces, 250 + size);
                bn.draw(bitmapCanvasPair.getCanvas());
            }
            pieceTypesBlack++;
        }
        if (pieceDifference.getBishop() > 0) {
            for (int i = pieceDifference.getBishop() - 1; i >= 0; i--) {
                Drawable wb = ResourcesCompat.getDrawable(resources, R.drawable.wb, null);
                int additionalSpace = pieceCounter.getNumberOfPiecesBeforeBishop(pieceDifference, true) * distanceOfSamePieces;
                wb.setBounds((i * distanceOfSamePieces) + additionalSpace + pieceTypesWhite * spaceBetweenPieces, 2000,
                        (i * distanceOfSamePieces) + size + additionalSpace + pieceTypesWhite * spaceBetweenPieces, 2000 + size);
                wb.draw(bitmapCanvasPair.getCanvas());
            }
            pieceTypesWhite++;
        }
        if (pieceDifference.getBishop() < 0) {
            for (int i = (pieceDifference.getBishop() * -1) - 1; i >= 0; i--) {
                Drawable bb = ResourcesCompat.getDrawable(resources, R.drawable.bb_rotated, null);
                int additionalSpace = pieceCounter.getNumberOfPiecesBeforeBishop(pieceDifference, false) * -1 * distanceOfSamePieces;
                bb.setBounds((i * distanceOfSamePieces) + additionalSpace + pieceTypesBlack * spaceBetweenPieces, 250,
                        (i * distanceOfSamePieces) + size + additionalSpace + pieceTypesBlack * spaceBetweenPieces, 250 + size);
                bb.draw(bitmapCanvasPair.getCanvas());
            }
            pieceTypesBlack++;
        }
        if (pieceDifference.getRock() > 0) {
            for (int i = pieceDifference.getRock() - 1; i >= 0; i--) {
                Drawable wr = ResourcesCompat.getDrawable(resources, R.drawable.wr, null);
                int additionalSpace = pieceCounter.getNumberOfPiecesBeforeRock(pieceDifference, true) * distanceOfSamePieces;
                wr.setBounds((i * distanceOfSamePieces) + additionalSpace + pieceTypesWhite * spaceBetweenPieces, 2000,
                        (i * distanceOfSamePieces) + size + additionalSpace + pieceTypesWhite * spaceBetweenPieces, 2000 + size);
                wr.draw(bitmapCanvasPair.getCanvas());
            }
            pieceTypesWhite++;
        }
        if (pieceDifference.getRock() < 0) {
            for (int i = (pieceDifference.getRock() * -1) - 1; i >= 0; i--) {
                Drawable br = ResourcesCompat.getDrawable(resources, R.drawable.br_rotated, null);
                int additionalSpace = pieceCounter.getNumberOfPiecesBeforeRock(pieceDifference, false) * -1 * distanceOfSamePieces;
                br.setBounds((i * distanceOfSamePieces) + additionalSpace + pieceTypesBlack * spaceBetweenPieces, 250,
                        (i * distanceOfSamePieces) + size + additionalSpace + pieceTypesBlack * spaceBetweenPieces, 250 + size);
                br.draw(bitmapCanvasPair.getCanvas());
            }
            pieceTypesBlack++;
        }
        if (pieceDifference.getQueen() > 0) {
            for (int i = pieceDifference.getQueen() - 1; i >= 0; i--) {
                Drawable wq = ResourcesCompat.getDrawable(resources, R.drawable.wq, null);
                int additionalSpace = pieceCounter.getNumberOfPiecesBeforeQueen(pieceDifference, true) * distanceOfSamePieces;
                wq.setBounds((i * distanceOfSamePieces) + additionalSpace + pieceTypesWhite * spaceBetweenPieces, 2000,
                        (i * distanceOfSamePieces) + size + additionalSpace + pieceTypesWhite * spaceBetweenPieces, 2000 + size);
                wq.draw(bitmapCanvasPair.getCanvas());
            }
            pieceTypesWhite++;
        }
        if (pieceDifference.getQueen() < 0) {
            for (int i = (pieceDifference.getQueen() * -1) - 1; i >= 0; i--) {
                Drawable bq = ResourcesCompat.getDrawable(resources, R.drawable.bq_rotated, null);
                int additionalSpace = pieceCounter.getNumberOfPiecesBeforeQueen(pieceDifference, false) * -1 * distanceOfSamePieces;
                bq.setBounds((i * distanceOfSamePieces) + additionalSpace + pieceTypesBlack * spaceBetweenPieces, 250,
                        (i * distanceOfSamePieces) + size + additionalSpace + pieceTypesBlack * spaceBetweenPieces, 250 + size);
                bq.draw(bitmapCanvasPair.getCanvas());
            }
            pieceTypesBlack++;
        }
        if (pieceDifference.getKing() > 0) {
            for (int i = pieceDifference.getKing() - 1; i >= 0; i--) {
                Drawable wk = ResourcesCompat.getDrawable(resources, R.drawable.wk, null);
                int additionalSpace = pieceCounter.getNumberOfPiecesBeforeKing(pieceDifference, true) * distanceOfSamePieces;
                wk.setBounds((i * distanceOfSamePieces) + additionalSpace + pieceTypesWhite * spaceBetweenPieces, 2000,
                        (i * distanceOfSamePieces) + size + additionalSpace + pieceTypesWhite * spaceBetweenPieces, 2000 + size);
                wk.draw(bitmapCanvasPair.getCanvas());
            }
            pieceTypesWhite++;
        }
        if (pieceDifference.getKing() < 0) {
            for (int i = (pieceDifference.getKing() * -1) - 1; i >= 0; i--) {
                Drawable bk = ResourcesCompat.getDrawable(resources, R.drawable.bk_rotated, null);
                int additionalSpace = pieceCounter.getNumberOfPiecesBeforeKing(pieceDifference, false) * -1 * distanceOfSamePieces;
                bk.setBounds((i * distanceOfSamePieces) + additionalSpace + pieceTypesBlack * spaceBetweenPieces, 250,
                        (i * distanceOfSamePieces) + size + additionalSpace + pieceTypesBlack * spaceBetweenPieces, 250 + size);
                bk.draw(bitmapCanvasPair.getCanvas());
            }
            pieceTypesBlack++;
        }
        if (pieceDifference.getScore() > 0) {
            String text = "+" + pieceDifference.getScore();
            int x = pieceCounter.getNumberOfPieces(pieceDifference, true) * distanceOfSamePieces + pieceTypesWhite * spaceBetweenPieces;
            bitmapCanvasPair.getCanvas().drawText(text, x + 20, 2130, scorePaint);
        }
        if (pieceDifference.getScore() < 0) {
            // Create new Canvas and rotate it
            Canvas reverseCanvas = new Canvas(bitmapCanvasPair.getBitmap());
            int x = pieceCounter.getNumberOfPieces(pieceDifference, false) * distanceOfSamePieces - pieceTypesBlack * spaceBetweenPieces;
            String text = "+" + (pieceDifference.getScore() * -1);
            reverseCanvas.rotate(180, 0, 0);
            reverseCanvas.drawText(text, x - 200, -290, scorePaint);
        }
        imageView.setImageBitmap(bitmapCanvasPair.getBitmap());
    }

    /**
     * Calls markSelectedSquare(squarePosition) and markSquaresPieceCanEnter(squarePosition)
     * Additional performs a move if the square position is that of a legal move
     *
     * @param squarePosition squarePosition... duh...
     */
    private void markSquares(SquarePosition squarePosition, ImageView imageView, Resources resources, double squareSize, int boardHeight, Paint backgroundColor, BitmapCanvasPair bitmapCanvasPair) {
        ArrayList<Square> squares = board.getSquaresPieceCanEnter(selectedSquare.getsquarePosition(), true);
        // If piece is of same color as player to move
        if (((board.isWhitesMove() && selectedSquare.getPiece().getColor().equals(Piece.Color.W)) ||
                (!board.isWhitesMove() && selectedSquare.getPiece().getColor().equals(Piece.Color.B))) &&
                squares.contains(board.getBoard()[squarePosition.getColumnCount()][squarePosition.getRowCount()])) {
            Move lastMove = new Move();
            lastMove.setPiece(getEmptyPiece());
            if (board.getMoveList().size() - 1 > 0){
                lastMove = board.getMoveList().get(board.getMoveList().size() - 1);
            }
            // checks if the last move was an en passant
            // if so the en passanted pawn will be removed
            if (board.checkForEnPassant(squarePosition, selectedSquare)) {
                board.getBoard()[lastMove.getToSquare().getsquarePosition().getColumnCount()][lastMove.getToSquare().getsquarePosition().getRowCount()].getPiece().setType(Piece.Type.NaN);
                board.getBoard()[lastMove.getToSquare().getsquarePosition().getColumnCount()][lastMove.getToSquare().getsquarePosition().getRowCount()].getPiece().setColor(Piece.Color.NaN);
            }
            // checks if the last move was a short castle
            // if so the rock will be placed at the correct position
            if (board.checkForShortCastle(squarePosition, selectedSquare)) {
                board.getBoard()[selectedSquare.getsquarePosition().getColumnCount() + 3][selectedSquare.getsquarePosition().getRowCount()].setPiece(getEmptyPiece());
                board.getBoard()[selectedSquare.getsquarePosition().getColumnCount() + 1][selectedSquare.getsquarePosition().getRowCount()].setPiece(new Piece(Piece.Type.R, board.getBoard()[selectedSquare.getsquarePosition().getColumnCount()][selectedSquare.getsquarePosition().getRowCount()].getPiece().getColor()));
            }
            // checks if the last move was a long castle
            // if so the rock will be placed at the correct position
            else if (board.checkForLongCastle(squarePosition, selectedSquare)) {
                board.getBoard()[selectedSquare.getsquarePosition().getColumnCount() - 4][selectedSquare.getsquarePosition().getRowCount()].setPiece(getEmptyPiece());
                board.getBoard()[selectedSquare.getsquarePosition().getColumnCount() - 1][selectedSquare.getsquarePosition().getRowCount()].setPiece(new Piece(Piece.Type.R, board.getBoard()[selectedSquare.getsquarePosition().getColumnCount()][selectedSquare.getsquarePosition().getRowCount()].getPiece().getColor()));
            }
            Piece.Color color = selectedSquare.getPiece().getColor();
            movePiece(selectedSquare, board.getBoard()[squarePosition.getColumnCount()][squarePosition.getRowCount()]);
            if (board.getMoveList().size() > 0){
                lastMove = board.getMoveList().get(board.getMoveList().size() - 1);
            }
            // If a pawn gets to the last square
            if (lastMove.getPiece().getType().equals(Piece.Type.P)
                    && ((lastMove.getPiece().getColor().equals(Piece.Color.W) && lastMove.getToSquare().getsquarePosition().getRowCount() == 0)
                    || (lastMove.getPiece().getColor().equals(Piece.Color.B) && lastMove.getToSquare().getsquarePosition().getRowCount() == 7))) {
                promotion(lastMove.getToSquare(), imageView, resources, squareSize, boardHeight, bitmapCanvasPair);
                waitForPromotion = true;
            }
            if (!waitForPromotion){
                position = getPositionFromBoard();
                board.addPositionToPositionList(position);
                if (color.equals(Piece.Color.W)) {
                    board.setWhitesMove(false);
                } else {
                    board.setWhitesMove(true);
                }
                initChessBoardAndPlacePieces(resources, squareSize, boardHeight, imageView, backgroundColor, bitmapCanvasPair, false);
                updateImageView(imageView, resources, squareSize, boardHeight, bitmapCanvasPair);
                checkForStalemateOrCheckmate();
                // TODO: Paste in here saving in file
            }
        } else {
            markSelectedSquare(squarePosition, imageView, resources, squareSize, boardHeight, backgroundColor, bitmapCanvasPair);
            markSquaresPieceCanEnter(squarePosition, imageView, resources, squareSize, boardHeight, bitmapCanvasPair);
        }
    }

    /**
     * Clears the selected square and places the piece on the target square
     *
     * @param selectedSquare
     * @param squareDestination
     */
    private void movePiece(Square selectedSquare, Square squareDestination) {
        Square[][] newBoard = board.getBoard();
        Piece movedPiece = board.getBoard()[selectedSquare.getsquarePosition().getColumnCount()][selectedSquare.getsquarePosition().getRowCount()].getPiece();
        board.addMove(new Move(selectedSquare, squareDestination, movedPiece));
        newBoard[selectedSquare.getsquarePosition().getColumnCount()][selectedSquare.getsquarePosition().getRowCount()].setPiece(getEmptyPiece());
        newBoard[squareDestination.getsquarePosition().getColumnCount()][squareDestination.getsquarePosition().getRowCount()].setPiece(movedPiece);
    }

    private void promotion(Square square, ImageView imageView, Resources resources, double squareSize, int boardHeight, BitmapCanvasPair bitmapCanvasPair) {
        chessGamePromotion.drawPromotionBackground(imageView, resources, squareSize, boardHeight, bitmapCanvasPair);
        updateImageView(imageView, resources, squareSize, boardHeight, bitmapCanvasPair);
        if (square.getPiece().getColor().equals(Piece.Color.W)) {
            chessGamePromotion.showPromoteWhite(resources, bitmapCanvasPair.getCanvas());
        } else {
            chessGamePromotion.showPromoteBlack(resources, bitmapCanvasPair.getCanvas());
        }
    }

    /**
     * Checks for stalemate or check mate
     */
    private void checkForStalemateOrCheckmate() {
        if (board.getSquaresWhichCanBeEnteredWithAnyPiece().size()==0){
            if (board.checkForCheck()){
                System.out.println("CHECKMATE");
            } else {
                System.out.println("STALEMATE");
            }
        }
    }

    /**
     * Marks the given square
     *
     * @param squarePosition Position of the square which should be marked
     */
    private void markSelectedSquare(SquarePosition squarePosition, ImageView imageView, Resources resources, double squareSize, int boardHeight, Paint backgroundColor, BitmapCanvasPair bitmapCanvasPair) {
        initChessBoardAndPlacePieces(resources, squareSize, boardHeight, imageView, backgroundColor, bitmapCanvasPair, false);
        if (board.isPieceOnSquare(squarePosition)) {
            Paint paint = new Paint();
            paint.setColor(Color.rgb(209, 90, 54));
            paint.setAlpha(125);
            paintSquare(squarePosition, paint, squareSize, boardHeight, bitmapCanvasPair.getCanvas());
            updateImageView(imageView, resources, squareSize, boardHeight, bitmapCanvasPair);
            selectedSquare = board.getBoard()[squarePosition.getColumnCount()][squarePosition.getRowCount()];
        }
    }

    /**
     * Marks the squares the piece on the square at the given position can enter
     *
     * @param squarePosition Position of the square
     */
    private void markSquaresPieceCanEnter(SquarePosition squarePosition, ImageView imageView, Resources resources, double squareSize, int boardHeight, BitmapCanvasPair bitmapCanvasPair) {
        ArrayList<Square> squares = board.getSquaresPieceCanEnter(squarePosition, true);
        if (squares.size() > 0) {
            Paint paint = new Paint();
            paint.setColor(Color.rgb(209, 90, 54));
            paint.setAlpha(200);
            for (Square f : squares) {
                paintSquare(f.getsquarePosition(), paint, squareSize, boardHeight, bitmapCanvasPair.getCanvas());
            }
        } else {
            selectedSquare = chessGameHelper.initSelectedSquare();
        }
        updateImageView(imageView, resources, squareSize, boardHeight, bitmapCanvasPair);
    }

    private void drawPromotionBackground(ImageView imageView, Resources resources, double squareSize, int boardHeight, BitmapCanvasPair bitmapCanvasPair){
        Paint paintBackground = new Paint();
        paintBackground.setColor(Color.rgb(255, 255, 255));
        paintBackground.setAlpha(200);
        Paint paintLine = new Paint();
        paintLine.setColor(Color.rgb(0, 0, 0));
        paintLine.setAlpha(255);
        paintLine.setStrokeWidth(10);
        Rect rect = new Rect(100, 700, 1500, 1700);
        bitmapCanvasPair.getCanvas().drawRect(rect, paintBackground);
        bitmapCanvasPair.getCanvas().drawLine(100, 1200, 1500, 1200, paintLine);
        bitmapCanvasPair.getCanvas().drawLine(800, 700, 800, 1700, paintLine);
        updateImageView(imageView, resources, squareSize, boardHeight, bitmapCanvasPair);
    }

    private void showPromoteWhite(Resources resources, Canvas canvas) {
        Drawable wb = ResourcesCompat.getDrawable(resources, R.drawable.wb, null);
        Drawable wq = ResourcesCompat.getDrawable(resources, R.drawable.wq, null);
        Drawable wr = ResourcesCompat.getDrawable(resources, R.drawable.wr, null);
        Drawable wn = ResourcesCompat.getDrawable(resources, R.drawable.wn, null);
        wb.setBounds(250, 700, 650, 1100);
        wb.draw(canvas);
        wn.setBounds(950, 700, 1350, 1100);
        wn.draw(canvas);
        wr.setBounds(250, 1200, 650, 1600);
        wr.draw(canvas);
        wq.setBounds(950, 1200, 1350, 1600);
        wq.draw(canvas);
    }

    private void showPromoteBlack(Resources resources, Canvas canvas) {
        Drawable bb = ResourcesCompat.getDrawable(resources, R.drawable.bb_rotated, null);
        Drawable bq = ResourcesCompat.getDrawable(resources, R.drawable.bq_rotated, null);
        Drawable br = ResourcesCompat.getDrawable(resources, R.drawable.br_rotated, null);
        Drawable bn = ResourcesCompat.getDrawable(resources, R.drawable.bn_rotated, null);
        bq.setBounds(250, 800, 650, 1200);
        bq.draw(canvas);
        br.setBounds(950, 800, 1350, 1200);
        br.draw(canvas);
        bn.setBounds(250, 1300, 650, 1700);
        bn.draw(canvas);
        bb.setBounds(950, 1300, 1350, 1700);
        bb.draw(canvas);
    }

    /**
     * Returns the position of the selected square
     *
     * @param x x-cord of the touch
     * @param y y-cord of the touch
     * @return square position
     */
    private SquarePosition getSquarePositionFromCoordinates(double x, double y) {
        SquarePosition squarePosition = new SquarePosition();
        if (x < 240) {
            squarePosition.setColumnCount(0);
        } else if (x < 410) {
            squarePosition.setColumnCount(1);
        } else if (x < 565) {
            squarePosition.setColumnCount(2);
        } else if (x < 725) {
            squarePosition.setColumnCount(3);
        } else if (x < 900) {
            squarePosition.setColumnCount(4);
        } else if (x < 1060) {
            squarePosition.setColumnCount(5);
        } else if (x < 1210) {
            squarePosition.setColumnCount(6);
        } else {
            squarePosition.setColumnCount(7);
        }
        if (y < 570) {
            squarePosition.setRowCount(0);
        } else if (y < 725) {
            squarePosition.setRowCount(1);
        } else if (y < 900) {
            squarePosition.setRowCount(2);
        } else if (y < 1050) {
            squarePosition.setRowCount(3);
        } else if (y < 1200) {
            squarePosition.setRowCount(4);
        } else if (y < 1370) {
            squarePosition.setRowCount(5);
        } else if (y < 1530) {
            squarePosition.setRowCount(6);
        } else {
            squarePosition.setRowCount(7);
        }
        return squarePosition;
    }

    public View.OnClickListener getOnClickListenerTakeBackWhite(Resources resources, double squareSize, int boardHeight, ImageView imageView, BitmapCanvasPair bitmapCanvasPair, Paint backgroundColor, Button buttonTakeBackBlack, Button buttonTakeBackWhite) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // If not the first move
                if (board.getMoveList().size()>0 && board.getPositionList().size()>0){
                    // If black has offered a take back
                    if (blackOffersTakeBack){
                        // Performs the take back
                        performTakeBack(resources, squareSize, boardHeight, imageView, bitmapCanvasPair, backgroundColor,buttonTakeBackBlack, buttonTakeBackWhite);
                        blackOffersTakeBack = false;
                        board.setWhitesMove(false);
                    } else {
                        if (!board.isWhitesMove() && board.getMoveList().size()>0){
                            // Offers a take back
                            whiteOffersTakeBack = true;
                            buttonTakeBackBlack.setBackgroundColor(Color.RED);
                        }
                    }
                }
            }
        };
    }

    public View.OnClickListener getOnClickListenerTakeBackBlack(Resources resources, double squareSize, int boardHeight, ImageView imageView, BitmapCanvasPair bitmapCanvasPair, Paint backgroundColor, Button buttonTakeBackBlack, Button buttonTakeBackWhite) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // If not the first move
                if (board.getMoveList().size()>0 && board.getPositionList().size()>0){
                    // If white has offered a take back
                    if (whiteOffersTakeBack){
                        // Performs the take back
                        performTakeBack(resources, squareSize, boardHeight, imageView, bitmapCanvasPair, backgroundColor,buttonTakeBackBlack, buttonTakeBackWhite);
                        whiteOffersTakeBack = false;
                        board.setWhitesMove(true);
                    } else {
                        if (board.isWhitesMove() && board.getMoveList().size()>0){
                            // Offers a take back
                            blackOffersTakeBack = true;
                            buttonTakeBackWhite.setBackgroundColor(Color.RED);
                        }
                    }
                }
            }
        };
    }

    /**
     * Returns onTouchListener
     *
     * @return
     */
    public View.OnTouchListener getTouchListener(Resources resources, double squareSize, int boardHeight, ImageView imageView, BitmapCanvasPair bitmapCanvasPair, Paint backgroundColor) {
        return new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return imageViewOnTouchEvent(motionEvent, resources, squareSize, boardHeight, imageView, bitmapCanvasPair, backgroundColor);
            }
        };
    }

    /**
     * Performs a take back
     */
    private void performTakeBack(Resources resources, double squareSize, int boardHeight, ImageView imageView, BitmapCanvasPair bitmapCanvasPair, Paint backgroundColor, Button buttonTakeBackBlack, Button buttonTakeBackWhite){
        // Get last move
        Move move = board.getMoveList().get(board.getMoveList().size()-1);
        // Deletes the last position from the list
        board.deleteLastPositionFromPositionList();
        // Sets the position from the move before as the actual move
        if (board.getPositionList().size()>0){
            position = board.getPositionList().get(board.getPositionList().size()-1);
        } else {
            position = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR";
        }
        // Deletes last move from the move List
        board.deleteLastMove();
        // Builds the position on the board
        board.buildPosition(position);
        // Removes the piece from its former square if there was no capture
        if (board.getBoard()[move.getToSquare().getsquarePosition().getColumnCount()][move.getToSquare().getsquarePosition().getRowCount()].getPiece().getColor().equals(
                move.getPiece().getColor()
        )){
            board.getBoard()[move.getToSquare().getsquarePosition().getColumnCount()][move.getToSquare().getsquarePosition().getRowCount()].setPiece(getEmptyPiece());
        }
        initChessBoardAndPlacePieces(resources, squareSize, boardHeight, imageView, backgroundColor, bitmapCanvasPair, false);
        updateImageView(imageView, resources, squareSize, boardHeight, bitmapCanvasPair);
        setTakeBackButtonsOnGrey(buttonTakeBackBlack, buttonTakeBackWhite);
        selectedSquare = chessGameHelper.initSelectedSquare();
    }

    /**
     * Sets the color of the takeBackButtons to grey
     * @param buttonTakeBackBlack
     * @param buttonTakeBackWhite
     */
    private void setTakeBackButtonsOnGrey(Button buttonTakeBackBlack, Button buttonTakeBackWhite){
        buttonTakeBackBlack.setBackgroundColor(Color.LTGRAY);
        buttonTakeBackWhite.setBackgroundColor(Color.LTGRAY);
    }

    public int createNewGame(String startPosition) throws IOException {
        return positionFileManager.createNewGame(PositionFileManager.GameType.CHESS_POSITION, startPosition);
    }

    public void readFromFile() throws IOException {
        System.out.println(positionFileManager.readFromFile(PositionFileManager.GameType.CHESS_POSITION));
    }









    /**
     * Returns an empty piece
     *
     * @return
     */
    private Piece getEmptyPiece() {
        return new Piece(Piece.Type.NaN, Piece.Color.NaN);
    }

    public Square getSelectedSquare() {
        return selectedSquare;
    }

    public void setSelectedSquare(Square selectedSquare) {
        this.selectedSquare = selectedSquare;
    }

    public Board getBoard() {
        return board;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public PieceCounter getPieceCounter() {
        return pieceCounter;
    }

    public void setPieceCounter(PieceCounter pieceCounter) {
        this.pieceCounter = pieceCounter;
    }

    public boolean isWaitForPromotion() {
        return waitForPromotion;
    }

    public void setWaitForPromotion(boolean waitForPromotion) {
        this.waitForPromotion = waitForPromotion;
    }

    public boolean isWhiteOffersTakeBack() {
        return whiteOffersTakeBack;
    }

    public void setWhiteOffersTakeBack(boolean whiteOffersTakeBack) {
        this.whiteOffersTakeBack = whiteOffersTakeBack;
    }

    public boolean isBlackOffersTakeBack() {
        return blackOffersTakeBack;
    }

    public void setBlackOffersTakeBack(boolean blackOffersTakeBack) {
        this.blackOffersTakeBack = blackOffersTakeBack;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }
}
