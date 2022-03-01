package com.example.chess20;

import android.graphics.*;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.Button;
import android.widget.ImageView;
import androidx.fragment.app.FragmentActivity;
import com.example.chess20.manager.chessFunctionality.ChessGameManager;
import com.example.chess20.manager.gameSaver.PositionFileManager;
import com.example.chess20.model.*;

import java.io.IOException;

/*
 * Main Activity class that loads {@link MainFragment}.
 */
public class BasicChessBoardActivity extends FragmentActivity {

    private ImageView imageView;
    private Button buttonTakeBackWhite;
    private Button buttonTakeBackBlack;

    private ChessGameManager chessGameManager;

    private double squareSize;
    private int boardHeight;

    // Wird auch durch Intent übergeben
    private int gameId;

    // TODO: Übergeben von Überactivity
    private boolean isNewGame = true;
    private String startPosition = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic_chess_board);
        // ChessGameManager
        try {
            // Init ChessGameManager with the directory path
            chessGameManager = new ChessGameManager(this.getDir("files", MODE_ENABLE_WRITE_AHEAD_LOGGING).getAbsolutePath()+"/");
            // If it's a new game -> create it and save the id
            if (isNewGame){
                // TODO:Edit position string
                gameId = chessGameManager.createNewGame(startPosition);
            }
            chessGameManager.readFromFile();    // For debugging only
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Init ImageView
        imageView = findViewById(R.id.imageView);
        // Set background
        imageView.setBackgroundColor(Color.BLACK);
        // Get the dimensions of the phone screen
        DisplayMetrics dm = getResources().getDisplayMetrics();
        // Get the squareSize by dividing the total width by eight
        squareSize = (float)dm.widthPixels/8;
        // Hardcoded (TODO: Maybe calculate it)
        boardHeight = 300;
        // Init the chess board and place the pieces. Get the Bitmap and Canvas for later use
        // TODO: Maybe replace Pair with Canvas only if possible
        BitmapCanvasPair bitmapCanvasPair = chessGameManager.initChessBoardAndPlacePieces(this.getResources(), squareSize, boardHeight, imageView, new Paint(Color.rgb(74, 71, 61)), new BitmapCanvasPair(), true); //TODO Setze backgroundcolor ein
        // Set touchListener on ImageView
        imageView.setOnTouchListener(chessGameManager.getTouchListener(this.getResources(), squareSize, boardHeight, imageView, bitmapCanvasPair, new Paint(Color.rgb(74, 71, 61))));
        // Init takeBack-Buttons
        buttonTakeBackWhite = findViewById(R.id.buttonTakeBackWhite);
        buttonTakeBackBlack = findViewById(R.id.buttonTakeBackBlack);
        // Set onClickListener on takeBack-Buttons
        buttonTakeBackWhite.setOnClickListener(chessGameManager.getOnClickListenerTakeBackWhite(this.getResources(), squareSize, boardHeight, imageView, bitmapCanvasPair, new Paint(Color.rgb(74, 71, 61)), buttonTakeBackBlack, buttonTakeBackWhite));
        buttonTakeBackBlack.setOnClickListener(chessGameManager.getOnClickListenerTakeBackBlack(this.getResources(), squareSize, boardHeight, imageView, bitmapCanvasPair, new Paint(Color.rgb(74, 71, 61)), buttonTakeBackBlack, buttonTakeBackWhite));
    }
}