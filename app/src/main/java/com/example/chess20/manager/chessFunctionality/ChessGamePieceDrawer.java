package com.example.chess20.manager.chessFunctionality;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import androidx.core.content.res.ResourcesCompat;
import com.example.chess20.R;
import com.example.chess20.model.BitmapCanvasPair;
import com.example.chess20.model.Piece;

public class ChessGamePieceDrawer {



    public ChessGamePieceDrawer(){

    }

    /**
     * Places the pieces on the chess board
     *
     */
    public Canvas placePieces(Canvas canvas, Resources resources, double squareSize, int boardHeight, String position) {
        Drawable bp = ResourcesCompat.getDrawable(resources, R.drawable.bp_rotated, null);
        Drawable wp = ResourcesCompat.getDrawable(resources, R.drawable.wp, null);
        Drawable bb = ResourcesCompat.getDrawable(resources, R.drawable.bb_rotated, null);
        Drawable wb = ResourcesCompat.getDrawable(resources, R.drawable.wb, null);
        Drawable bk = ResourcesCompat.getDrawable(resources, R.drawable.bk_rotated, null);
        Drawable wk = ResourcesCompat.getDrawable(resources, R.drawable.wk, null);
        Drawable bq = ResourcesCompat.getDrawable(resources, R.drawable.bq_rotated, null);
        Drawable wq = ResourcesCompat.getDrawable(resources, R.drawable.wq, null);
        Drawable br = ResourcesCompat.getDrawable(resources, R.drawable.br_rotated, null);
        Drawable wr = ResourcesCompat.getDrawable(resources, R.drawable.wr, null);
        Drawable bn = ResourcesCompat.getDrawable(resources, R.drawable.bn_rotated, null);
        Drawable wn = ResourcesCompat.getDrawable(resources, R.drawable.wn, null);
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
                        drawRook(Piece.Color.B, columnCount, rowCount, canvas, resources, squareSize, boardHeight);
                        columnCount++;
                        break;
                    case 'n':
                        drawKnight(Piece.Color.B, columnCount, rowCount, canvas, resources, squareSize, boardHeight);
                        columnCount++;
                        break;
                    case 'b':
                        drawBishop(Piece.Color.B, columnCount, rowCount, canvas, resources, squareSize, boardHeight);
                        columnCount++;
                        break;
                    case 'k':
                        drawKing(Piece.Color.B, columnCount, rowCount, canvas, resources, squareSize, boardHeight);
                        columnCount++;
                        break;
                    case 'q':
                        drawQueen(Piece.Color.B, columnCount, rowCount, canvas, resources, squareSize, boardHeight);
                        columnCount++;
                        break;
                    case 'p':
                        drawPawn(Piece.Color.B, columnCount, rowCount, canvas, resources, squareSize, boardHeight);
                        columnCount++;
                        break;
                    case 'R':
                        drawRook(Piece.Color.W, columnCount, rowCount, canvas, resources, squareSize, boardHeight);
                        columnCount++;
                        break;
                    case 'N':
                        drawKnight(Piece.Color.W, columnCount, rowCount, canvas, resources, squareSize, boardHeight);
                        columnCount++;
                        break;
                    case 'B':
                        drawBishop(Piece.Color.W, columnCount, rowCount, canvas, resources, squareSize, boardHeight);
                        columnCount++;
                        break;
                    case 'K':
                        drawKing(Piece.Color.W, columnCount, rowCount, canvas, resources, squareSize, boardHeight);
                        columnCount++;
                        break;
                    case 'Q':
                        drawQueen(Piece.Color.W, columnCount, rowCount, canvas, resources, squareSize, boardHeight);
                        columnCount++;
                        break;
                    case 'P':
                        drawPawn(Piece.Color.W, columnCount, rowCount, canvas, resources, squareSize, boardHeight);
                        columnCount++;
                }
            }
            rowCount++;
        }
        return canvas;
    }

    private void drawRook(Piece.Color c, int columnCount, int rowCount, Canvas canvas, Resources resources, double squareSize, int boardHeight) {
        int left = columnCount * (int)(squareSize);
        int top = boardHeight + (int)squareSize + rowCount * (int)(squareSize);
        int right = columnCount * (int)(squareSize) + (int)(squareSize);
        int bottom = (int)(squareSize)*2+boardHeight + rowCount * (int)(squareSize);
        Drawable rock;
        if (c.equals(Piece.Color.W)){
            rock = ResourcesCompat.getDrawable(resources, R.drawable.wr, null);
        } else {
            rock = ResourcesCompat.getDrawable(resources, R.drawable.br_rotated, null);
        }
        rock.setBounds(left, top, right, bottom);
        rock.draw(canvas);
    }

    private void drawKnight(Piece.Color c, int columnCount, int rowCount, Canvas canvas, Resources resources, double squareSize, int boardHeight) {
        int left = columnCount * (int)(squareSize);
        int top = boardHeight + (int)squareSize + rowCount * (int)(squareSize);
        int right = columnCount * (int)(squareSize) + (int)(squareSize);
        int bottom = (int)(squareSize)*2+boardHeight + rowCount * (int)(squareSize);
        Drawable knight;
        if (c.equals(Piece.Color.W)){
            knight = ResourcesCompat.getDrawable(resources, R.drawable.wn, null);
        } else {
            knight = ResourcesCompat.getDrawable(resources, R.drawable.bn_rotated, null);
        }
        knight.setBounds(left, top, right, bottom);
        knight.draw(canvas);
    }

    private void drawBishop(Piece.Color c, int columnCount, int rowCount, Canvas canvas, Resources resources, double squareSize, int boardHeight) {
        int left = columnCount * (int)(squareSize);
        int top = boardHeight + (int)squareSize + rowCount * (int)(squareSize);
        int right = columnCount * (int)(squareSize) + (int)(squareSize);
        int bottom = (int)(squareSize)*2+boardHeight + rowCount * (int)(squareSize);
        Drawable bishop;
        if (c.equals(Piece.Color.W)){
            bishop = ResourcesCompat.getDrawable(resources, R.drawable.wb, null);
        } else {
            bishop = ResourcesCompat.getDrawable(resources, R.drawable.bb_rotated, null);
        }
        bishop.setBounds(left, top, right, bottom);
        bishop.draw(canvas);
    }

    private void drawQueen(Piece.Color c, int columnCount, int rowCount, Canvas canvas, Resources resources, double squareSize, int boardHeight) {
        int left = columnCount * (int)(squareSize);
        int top = boardHeight + (int)squareSize + rowCount * (int)(squareSize);
        int right = columnCount * (int)(squareSize) + (int)(squareSize);
        int bottom = (int)(squareSize)*2+boardHeight + rowCount * (int)(squareSize);
        Drawable queen;
        if (c.equals(Piece.Color.W)){
            queen = ResourcesCompat.getDrawable(resources, R.drawable.wq, null);
        } else {
            queen = ResourcesCompat.getDrawable(resources, R.drawable.bq_rotated, null);
        }
        queen.setBounds(left, top, right, bottom);
        queen.draw(canvas);
    }

    private void drawPawn(Piece.Color c, int columnCount, int rowCount, Canvas canvas, Resources resources, double squareSize, int boardHeight) {
        int left = columnCount * (int)(squareSize);
        int top = boardHeight + (int)squareSize + rowCount * (int)(squareSize);
        int right = columnCount * (int)(squareSize) + (int)(squareSize);
        int bottom = (int)(squareSize)*2+boardHeight + rowCount * (int)(squareSize);
        Drawable pawn;
        if (c.equals(Piece.Color.W)){
            pawn = ResourcesCompat.getDrawable(resources, R.drawable.wp, null);
        } else {
            pawn = ResourcesCompat.getDrawable(resources, R.drawable.bp_rotated, null);
        }
        pawn.setBounds(left, top, right, bottom);
        pawn.draw(canvas);
    }

    private void drawKing(Piece.Color c, int columnCount, int rowCount, Canvas canvas, Resources resources, double squareSize, int boardHeight) {
        int left = columnCount * (int)(squareSize);
        int top = boardHeight + (int)squareSize + rowCount * (int)(squareSize);
        int right = columnCount * (int)(squareSize) + (int)(squareSize);
        int bottom = (int)(squareSize)*2+boardHeight + rowCount * (int)(squareSize);
        Drawable king;
        if (c.equals(Piece.Color.W)){
            king = ResourcesCompat.getDrawable(resources, R.drawable.wk, null);
        } else {
            king = ResourcesCompat.getDrawable(resources, R.drawable.bk_rotated, null);
        }
        king.setBounds(left, top, right, bottom);
        king.draw(canvas);
    }

    //TODO: Refactoren und aus ChessGameManager rausziehen

    public Canvas initChessBoard(Canvas canvas, double squareSize, int boardHeight) {
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
            canvas.drawRect(startX, startY + (int)squareSize, startX + (int)squareSize, startY + ((int)squareSize*2), paintBright);
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
            canvas.drawRect(startX, startY + (int)squareSize, startX + (int)squareSize, startY + ((int)squareSize*2), paintDark);
            startX += ((int)squareSize*2);
            if (startX == (int)squareSize*8) {
                startX = (int)squareSize;
                startY += (int)squareSize;
            } else if (startX > squareSize*8) {
                startX = 0;
                startY += squareSize;
            }
        }
        return canvas;
    }
}
