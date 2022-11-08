package Azul;

import java.util.ArrayList;
import java.util.List;

public class Box {
    //listy zawierające pogrupowane, konkretne elementy z pudełka:
    private List<Tile> blacks = new ArrayList<>();
    private List<Tile> whites = new ArrayList<>();
    private List<Tile> reds = new ArrayList<>();
    private List<Tile> yellows = new ArrayList<>();
    private List<Tile> blues = new ArrayList<>();

    public Box(){}

    /**
     * dodanie pojedynczej płytki do zawartości boxa
     */
    public void add(Tile colour) {
        switch (colour) {
            case BLACK:
                blacks.add(colour);
                break;
            case WHITE:
                whites.add(colour);
                break;
            case RED:
                reds.add(colour);
                break;
            case YELLOW:
                yellows.add(colour);
                break;
            case BLUE:
                blues.add(colour);
                break;
        }
    }

    /**
     * metoda zwraca 5 obiektów TileQuantity odpowiadających tym znajdującym się w boxie i czyści boxa
     */
    public TileQuantity[] get(){
        TileQuantity[] returnArray = {new TileQuantity(Tile.BLACK, blacks.size()), new TileQuantity(Tile.WHITE, whites.size()), new TileQuantity(Tile.RED, reds.size()), new TileQuantity(Tile.YELLOW, yellows.size()), new TileQuantity(Tile.BLUE, blues.size())};
        blacks.clear();
        whites.clear();
        reds.clear();
        yellows.clear();
        blues.clear();
        return returnArray;
    }
}
