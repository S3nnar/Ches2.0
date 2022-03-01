package com.example.chess20;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.example.chess20.manager.gameSaver.PositionFileManager;
import com.example.chess20.model.Game;
import com.example.chess20.view.ListAdapter;
import com.example.chess20.view.SavedGamesLoader;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button button = findViewById(R.id.buttonStartGame);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startGameActivity(view);
            }
        });
        try {
            initListView();
    } catch (IOException e) {
        e.printStackTrace();
    }
    }

    public void startGameActivity(View view) {
        Intent intent = new Intent(this, BasicChessBoardActivity.class);
        startActivity(intent);
    }

    /**
     * Initializes the listView with the saved games
     *
     */
    private void initListView() throws IOException {
        SavedGamesLoader savedGamesLoader = new SavedGamesLoader(PositionFileManager.GameType.CHESS_POSITION);
        ArrayList<Game> games = savedGamesLoader.getGames(this.getDir("files", MODE_ENABLE_WRITE_AHEAD_LOGGING).getAbsolutePath() + "/");

        ListView listView = findViewById(R.id.listView);

        ListAdapter listAdapter = new ListAdapter(this, games);
        listView.setAdapter(listAdapter);
    }
}