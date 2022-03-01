package com.example.chess20.manager.gameSaver;

import com.example.chess20.model.*;

import java.io.*;
import java.util.ArrayList;

public class PositionFileManager {

    public enum GameType {CHESS_POSITION}

    private File positionFile;

    /**
     * Loads wished file or creates it if it doesn't exist yet.
     * @param dir
     * @param gameType
     * @throws IOException
     */
    public PositionFileManager(String dir, GameType gameType) throws IOException {
        switch (gameType){
            case CHESS_POSITION:
                String fileName = "positionFileChess";
                positionFile = new File(dir + fileName);
                boolean exists = positionFile.exists();
                if (!exists){
                    positionFile.createNewFile();
                }
                break;
        }
    }

    /**
     * Writes to the given file
     * @param text
     * @param gameType
     * @throws IOException
     */
    public void writeToFile(String text, int id, GameType gameType) throws IOException {
        switch (gameType){
            case CHESS_POSITION:
                FileWriter fw = new FileWriter(positionFile, true);
                fw.write(text + "#" + id + "\n");
                fw.close();
                break;
        }
    }

    /**
     * Reads the given file
     * @param gameType
     * @throws IOException
     */
    public String readFromFile(GameType gameType) throws IOException {
        switch (gameType){
            case CHESS_POSITION:
                int length = (int) positionFile.length();

                byte[] bytes = new byte[length];

                FileInputStream in = new FileInputStream(positionFile);
                try {
                    in.read(bytes);
                } finally {
                    in.close();
                }
                return new String(bytes);
        }
        return "";
    }

    // Format: position#id
    /**
     * Creates a new game in the given gameType and with the given position. Returns the id of the game
     * @param gameType
     * @param position
     * @return
     * @throws IOException
     */
    public int createNewGame(GameType gameType, String position) throws IOException {
        int counter = 0;
        ArrayList<Game> gameList = getGamesOfFile(gameType);
        boolean idFree = false;
        while (!idFree){
            int finalCounter = counter;
            if (gameList.stream().noneMatch(game -> game.getId() == finalCounter)){
                idFree = true;
            } else {
                counter++;
            }
        }
        writeToFile(position, counter, gameType);
        return counter;
    }

    /**
     * Replaces the existing position of the game with the given gameType and id with the given position
     * @param position
     * @param id
     * @param gameType
     * @throws IOException
     */
    public void editGameEntry(String position, int id, GameType gameType) throws IOException {
        ArrayList<Game> gameList = getGamesOfFile(gameType);
        for (Game game:gameList){
            if (game.getId() == id){
                game.setPosition(position);
            }
        }
        overwriteFile(getStringFromGameList(gameList), gameType);
    }

    /**
     * Returns a list of all games from the file of the given gameType
     * @param gameType
     * @return
     * @throws IOException
     */
    public ArrayList<Game> getGamesOfFile(GameType gameType) throws IOException {
        String[] games = readFromFile(gameType).split("\n");
        ArrayList<Game> gameList = new ArrayList<>();
        if (games.length > 0){
            for (String game: games){
                String[] components = game.split("#");
                if (components.length == 5){
                    gameList.add(new Game(components[0], Integer.parseInt(components[1]), gameType, getMoveListFromString(components[2]), components[3], components[4]));
                }
            }
        }
        return gameList;
    }

    /**
     * Takes a gameList and converts it to a string
     * @param gameList
     * @return
     */
    private String getStringFromGameList(ArrayList<Game> gameList){
        String returnString = "";
        for (Game game:gameList){
            returnString = returnString + game.getPosition() + "#" + game.getId()+"\n";
        }
        return returnString;
    }

    /**
     * Overwrites the file of the given gameType with the given text
     * @param text
     * @param gameType
     * @throws IOException
     */
    private void overwriteFile(String text, GameType gameType) throws IOException {
        switch (gameType){
            case CHESS_POSITION:
                FileWriter fw = new FileWriter(positionFile);
                fw.write(text);
                fw.close();
                break;
        }
    }

    /**
     * Returns the moveList
     * @param movesString
     * @return
     */
    private ArrayList<Move> getMoveListFromString(String movesString){
        // MoveList schema: FromSquare:ToSquare:Piece; ...
        // Init returnList
        ArrayList<Move> returnList = new ArrayList<>();
        // Get moves from string
        String[] moves = movesString.split(";");
        for (String move:moves){
            // Get components of move
            String[] components = move.split(":");
            // Get Squares and Piece
            Square fromSquare = new Square(getSquarePositionFromCords(components[0]), new Piece(Piece.Type.NaN, Piece.Color.NaN));
            Square toSquare = new Square(getSquarePositionFromCords(components[1]), getPieceFromString(components[2]));
            Piece piece = getPieceFromString(components[2]);
            // Add move to returnList
            returnList.add(new Move(fromSquare, toSquare, piece));
        }
        return returnList;
    }

    /**
     * Get a SquarePosition object from a String of cords
     * @param cords
     * @return
     */
    private SquarePosition getSquarePositionFromCords(String cords){
        // Get Array with cords
        char[] cordChars = cords.toLowerCase().toCharArray();
        // Init squarePosition
        SquarePosition squarePosition = new SquarePosition((int) cordChars[0], (int) cordChars[1]);
        return squarePosition;
    }

    /**
     * Get the piece to the according char
     * @param piece
     * @return
     */
    private Piece getPieceFromString(String piece){
        switch (piece){
            case "r":   return new Piece(Piece.Type.R, Piece.Color.B);
            case "n":   return new Piece(Piece.Type.N, Piece.Color.B);
            case "b":   return new Piece(Piece.Type.B, Piece.Color.B);
            case "q":   return new Piece(Piece.Type.Q, Piece.Color.B);
            case "k":   return new Piece(Piece.Type.K, Piece.Color.B);
            case "p":   return new Piece(Piece.Type.P, Piece.Color.B);
            case "R":   return new Piece(Piece.Type.R, Piece.Color.W);
            case "N":   return new Piece(Piece.Type.N, Piece.Color.W);
            case "B":   return new Piece(Piece.Type.B, Piece.Color.W);
            case "Q":   return new Piece(Piece.Type.Q, Piece.Color.W);
            case "K":   return new Piece(Piece.Type.K, Piece.Color.W);
            case "P":   return new Piece(Piece.Type.P, Piece.Color.W);
        }
        return new Piece(Piece.Type.NaN, Piece.Color.NaN);
    }

    /**
     * Transforms the moveList to a String
     * @param moveList
     * @return
     */
    private String getStringFromMoveList(ArrayList<Move> moveList){
        String returnString = "";
        for (Move move:moveList){
            String moveString = move.getFromSquare().getsquarePosition().getColumnCount() +
                    move.getFromSquare().getsquarePosition().getRowCount() + ":" +
                    move.getToSquare().getsquarePosition().getColumnCount() +
                    move.getToSquare().getsquarePosition().getRowCount() + ":" +
                    getPieceCharByPiece(move.getPiece()) + ";";
            returnString += moveString;
        }
        return returnString;
    }

    /**
     * Returns the piece according to the char
     * @param piece
     * @return
     */
    private String getPieceCharByPiece(Piece piece){
        switch (piece.getType()){
            case R: if (piece.getColor().equals(Piece.Color.W)){
                return "R";
            } else {
                return "r";
            }
            case N: if (piece.getColor().equals(Piece.Color.W)){
                return "N";
            } else {
                return "n";
            }
            case B: if (piece.getColor().equals(Piece.Color.W)){
                return "B";
            } else {
                return "b";
            }
            case Q: if (piece.getColor().equals(Piece.Color.W)){
                return "Q";
            } else {
                return "q";
            }
            case K: if (piece.getColor().equals(Piece.Color.W)){
                return "K";
            } else {
                return "k";
            }
            case P: if (piece.getColor().equals(Piece.Color.W)){
                return "P";
            } else {
                return "p";
            }
        }
        return "";
    }
}
