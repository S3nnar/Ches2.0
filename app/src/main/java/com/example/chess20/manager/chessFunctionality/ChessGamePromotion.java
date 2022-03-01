package com.example.chess20.manager.chessFunctionality;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;
import androidx.core.content.res.ResourcesCompat;
import com.example.chess20.R;
import com.example.chess20.model.BitmapCanvasPair;
import com.example.chess20.model.Square;
import com.example.chess20.model.SquarePosition;

import java.util.ArrayList;

public class ChessGamePromotion {

    public ChessGamePromotion(){}


    public void drawPromotionBackground(ImageView imageView, Resources resources, double squareSize, int boardHeight, BitmapCanvasPair bitmapCanvasPair){
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
    }

    public void showPromoteWhite(Resources resources, Canvas canvas) {
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

    public void showPromoteBlack(Resources resources, Canvas canvas) {
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
}
