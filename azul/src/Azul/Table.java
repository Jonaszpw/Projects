package Azul;

import java.util.ArrayList;
import java.util.List;

public class Table {

    //listy zawierające pogrupowane, konkretne elementy ze stołu:
    private List<Tile> blacks = new ArrayList<>();
    private List<Tile> whites = new ArrayList<>();
    private List<Tile> reds = new ArrayList<>();
    private List<Tile> yellows = new ArrayList<>();
    private List<Tile> blues = new ArrayList<>();

    /**
     * metoda dodająca podaną płytkę do zawartości stołu
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
     * weź ze stołu wszystkie elementy danego koloru
     */
    public TileQuantity tablePick(Tile colour) {
        int quantity = 0;
        switch (colour) {
            case BLACK:
                quantity = blacks.size();
                blacks.clear();
                return new TileQuantity(colour, quantity);

            case WHITE:
                quantity = whites.size();
                whites.clear();
                return new TileQuantity(colour, quantity);

            case RED:
                quantity = reds.size();
                reds.clear();
                return new TileQuantity(colour, quantity);

            case YELLOW:
                quantity = yellows.size();
                yellows.clear();
                return new TileQuantity(colour, quantity);

            case BLUE:
                quantity = blues.size();
                blues.clear();
                return new TileQuantity(colour, quantity);

            default:
                return new TileQuantity(colour, 0);
        }
    }

    /**
     * podejrzyj na stole wszystkie elementy danego koloru (bez ich usuwania)
     */
    public TileQuantity tableGet(Tile colour) {
        switch (colour) {
            case BLACK:
                return new TileQuantity(colour, blacks.size());


            case WHITE:
                return new TileQuantity(colour, whites.size());


            case RED:
                return new TileQuantity(colour, reds.size());


            case YELLOW:
                return new TileQuantity(colour, yellows.size());


            case BLUE:
                return new TileQuantity(colour, blues.size());


            default:
                return new TileQuantity(colour, 0);
        }
    }

    /**
     * zwraca true jeśli element o danym kolorze znajduje się na stole
     */
    public boolean tableValidate(Tile colour) {
        switch (colour) {
            case BLACK:
                return !blacks.isEmpty();

            case WHITE:
                return !whites.isEmpty();

            case RED:
                return !reds.isEmpty();

            case YELLOW:
                return !yellows.isEmpty();

            case BLUE:
                return !blues.isEmpty();

            default:
                System.err.println("ERROR: tableValidate nie bangla :(");
                return false;
        }
    }

    /**
     * zwraca true jeśli stół jest pusty
     */
    public boolean isEmpty(){
        return blacks.isEmpty() & whites.isEmpty() & reds.isEmpty() & yellows.isEmpty() & blues.isEmpty();
    }

    /**
     * zwraca tablicę z zawartością stołu
     */
    public Tile[] getTableStatus(){
        Tile[] result = new Tile[27];
        int index = 0;

        for(int i=0; i<blacks.size(); i++)
            result[index++]=blacks.get(i);
        for(int i=0; i<whites.size(); i++)
            result[index++]=whites.get(i);
        for(int i=0; i<reds.size(); i++)
            result[index++]=reds.get(i);
        for(int i=0; i<yellows.size(); i++)
            result[index++]=yellows.get(i);
        for(int i=0; i<blues.size(); i++)
            result[index++]=blues.get(i);

        return result;
    }
}
