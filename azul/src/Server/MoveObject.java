package Server;

import Azul.Tile;

public class MoveObject {
    private int from;
    private int row;
    private Tile colour;

    public MoveObject(int from, int row, Tile colour){
        this.from = from;
        this. row = row;
        this.colour = colour;
    }

    public int getFrom() {
        return from;
    }

    public int getRow() {
        return row;
    }

    public Tile getColour() {
        return colour;
    }
}
