package com.example.chess20.view;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.icu.text.Transliterator;
import android.widget.ListView;
import com.example.chess20.R;
import com.example.chess20.manager.gameSaver.PositionFileManager;
import com.example.chess20.model.Game;
import com.example.chess20.model.ListViewItemChess;

import java.io.IOException;
import java.util.ArrayList;

public class SavedGamesLoader {

    private PositionFileManager.GameType gameType;

    public SavedGamesLoader(PositionFileManager.GameType gameType){
        this.gameType = gameType;
    }

    public ArrayList<Game> getGames(String dir) throws IOException {
        PositionFileManager positionFileManager = new PositionFileManager(dir, gameType);
        return positionFileManager.getGamesOfFile(gameType);
    }

//    public void loadSavedGames(Context context, Resources resources, String dir, int resource) throws IOException {
        public void loadSavedGames(Context context, int listViewInt, String dir, ListView listView) throws IOException {
        PositionFileManager positionFileManager = new PositionFileManager(dir, gameType);
        ArrayList<Game> games = positionFileManager.getGamesOfFile(gameType);
    }

    public void addGamesToList(){

    }

    private ListViewItemChess getListViewChessModelFromPosition(String position, Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(Color.rgb(0, 0, 0));
        paint.setTextSize(100);
        canvas.drawText(position, 0, 0, paint);
        return new ListViewItemChess(canvas, "");
    }


}
