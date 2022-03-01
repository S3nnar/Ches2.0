package com.example.chess20.model;

public class SquarePosition {


    private int columnCount;
    private int rowCount;


    public SquarePosition() {
    }
    public SquarePosition(int columnCount, int rowCount) {
        this.rowCount = rowCount;
        this.columnCount = columnCount;
    }

    public int getColumnCount() {
        return columnCount;
    }

    public void setColumnCount(int columnCount) {
        this.columnCount = columnCount;
    }

    public int getRowCount() {
        return rowCount;
    }

    public void setRowCount(int rowCount) {
        this.rowCount = rowCount;
    }
}
