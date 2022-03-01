package com.example.chess20.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.example.chess20.R;
import com.example.chess20.manager.chessFunctionality.ChessGamePieceDrawer;
import com.example.chess20.model.Game;

import java.util.ArrayList;
//https://github.com/foxandroid/Custom_Listview
public class ListAdapter extends ArrayAdapter<Game> {

    private Context context;

    public ListAdapter(Context context, ArrayList<Game> gameArrayList){
        super(context, R.layout.list_row, gameArrayList);
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        Game game = getItem(position);

        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_row_backup,parent,false);
        }

        ImageView imageView = convertView.findViewById(R.id.profile_pic);
        TextView userName = convertView.findViewById(R.id.personName);



//        // Init Bitmap with hardcoded width and height to match GS9-screen
        Bitmap bitmap = Bitmap.createBitmap(120, 120, Bitmap.Config.RGB_565);
//        // Init Canvas
        Canvas canvas = new Canvas(bitmap);
        ChessGamePieceDrawer chessGamePieceDrawer = new ChessGamePieceDrawer();
        canvas = chessGamePieceDrawer.initChessBoard(canvas, 15, -15);
        canvas = chessGamePieceDrawer.placePieces(canvas, context.getResources(), 15, -15, game.getPosition());

        imageView.setImageBitmap(bitmap);
//        imageView.setImageResource(R.drawable.bk);
        userName.setText(game.getPosition());


        return convertView;
    }
}
