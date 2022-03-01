package com.example.chess20.manager.chessFunctionality;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import com.example.chess20.model.BitmapCanvasPair;
import com.example.chess20.model.Piece;
import com.example.chess20.model.Square;
import com.example.chess20.model.SquarePosition;

public class ChessGameHelper {


    public ChessGameHelper(){}

    /**
     * Returns an empty Square
     * @return
     */
    public Square initSelectedSquare() {
        Square selectedSquare = new Square();
        Piece piece = new Piece();
        piece.setColor(Piece.Color.NaN);
        piece.setType(Piece.Type.NaN);
        selectedSquare.setPiece(piece);
        SquarePosition squarePosition = new SquarePosition(0, 0);
        selectedSquare.setsquarePosition(squarePosition);
        return selectedSquare;
    }

    /**
     * Initializes the BitmapCanvasPair
     * @param squareSize
     * @return
     */
    public BitmapCanvasPair initBitmapAndCanvas(double squareSize) {
        // Init Bitmap with hardcoded width and height to match GS9-screen
        Bitmap bitmap = Bitmap.createBitmap((int)squareSize*8, 3000, Bitmap.Config.RGB_565);
        // Init Canvas
        Canvas canvas = new Canvas(bitmap);
        return new BitmapCanvasPair(bitmap, canvas);
    }
}
