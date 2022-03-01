package com.example.chess20.model;

import com.example.chess20.manager.gameSaver.PositionFileManager;

import java.util.ArrayList;

public class Game {

    private String position;
    private int id;
    private PositionFileManager.GameType gameType;
    private ArrayList<Move> moveList;
    private String startDate;
    private String lastPlayed;

    public Game(){}

    public Game(String position, int id, PositionFileManager.GameType gameType, ArrayList<Move> moveList, String startDate, String lastPlayed) {
        this.position = position;
        this.id = id;
        this.gameType = gameType;
        this.moveList = moveList;
        this.startDate = startDate;
        this.lastPlayed = lastPlayed;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public PositionFileManager.GameType getGameType() {
        return gameType;
    }

    public void setGameType(PositionFileManager.GameType gameType) {
        this.gameType = gameType;
    }

    public ArrayList<Move> getMoveList() {
        return moveList;
    }

    public void setMoveList(ArrayList<Move> moveList) {
        this.moveList = moveList;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getLastPlayed() {
        return lastPlayed;
    }

    public void setLastPlayed(String lastPlayed) {
        this.lastPlayed = lastPlayed;
    }
}
