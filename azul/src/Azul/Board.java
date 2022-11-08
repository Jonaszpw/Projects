package Azul;

import java.util.Arrays;

/**
 * Plansza gracza
 */
public class Board {

    private final Tile[] floor = new Tile[7];       // podłoga
    private final Tile[][] wall = new Tile[5][];    // ściana
    private final Mosaic mosaic = new Mosaic();     // mozaika
    private Mosaic tempMosaic = new Mosaic();
    private Tile[] tempColours=  new Tile[5];
    private String[] pushedToMosaic;
    private Intersection intersection;
    private boolean one=false;

    /**
     * konstruktor
     */
    public Board() {
        initializeWall();
    }

    public String[] getPushedToMosaic() {
        return pushedToMosaic;
    }
    public Mosaic getTempMosaic() {
        return tempMosaic;
    }
    public Mosaic getMosaic() {
        return mosaic;
    }
    public boolean getOne() {return one;}
    public Tile[] getTempColours() {
        return tempColours;
    }
    public void setOne(boolean one) {
        this.one = one;
    }
    public void setIntersection(Intersection intersection){
        this.intersection = intersection;
    }

    /**
     * Zwraca kolor płytek ułożonych w danym rzędzie na ścianie
     * @param row - rząd (0-4)
     * @return - Tile
     */
    public Tile getColorOfTheRow(int row){
        return wall[row][row];
    }

    /**
     * Zwraca liczbę płytek umieszoczonych na podłodze
     * @return liczba płytek na podłodze
     */
    public int getNumberOfTilesOnTheFloor() {
        int result = 0;
        while (floor[result] != null)
            result++;
        return result;
    }

    /**
     * Konwertuje stan mozaiki na macierz 5x5 wartości boolowskich
     *
     * @return boolean[5][5]
     */
    public boolean[][] exportMosaic() {
        return mosaic.exportMosaic();
    }

    /**
     * Zwraca stan podłgi w formie tablicy płytek
     *
     * @return Tile[7]
     */
    public Tile[] exportFloor() {
        return floor;
    }

    /**
     * Generowanie macierzy 5x5 zawierajacej stan sciany (brak plytki = null)
     *
     * @return macierz 5x5
     */
    public Tile[][] exportWall() {
        Tile[][] result = new Tile[5][5];
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                if (j < 4 - i) result[i][j] = null;
                else result[i][j] = wall[i][j - 4 + i];
            }
        }
        return result;
    }

    /**
     * Dodaje płytki do rzędu na ścianie
     *
     * @param row   - numer rzędu
     * @param tiles - obiekt klasy TileQuantity, zawiera typ oraz liczbę płytek
     */
    public void addTilesToWall(int row, TileQuantity tiles) {
        int freeInRow = freeInRow(row);
        if (tiles.getQuantity() >= freeInRow) {     // zapełnienie wiersza, dodatnie reszty na podłogę
            for (int i = 0; i < wall[row].length; i++)
                wall[row][i] = tiles.getColour();
            addTilesToFloor(new TileQuantity(tiles.getColour(), tiles.getQuantity() - freeInRow));
        } else {                                              // jeśli wiersz nie zostanie zapełniony
            int firstFreeSpot = freeInRow(row) - 1;        // indeks pierwszego pustego miejsca w wierszu
            for (int i = 0; i < tiles.getQuantity(); i++)              // dodanie płytak do wiersza
                wall[row][firstFreeSpot - i] = tiles.getColour();
        }
    }

    /**
     * Dodaje płytki na podłogę
     *
     * @param tiles - obiekt klasy TileQuantity, zawiera typ oraz liczbę płytek
     */
    public void addTilesToFloor(TileQuantity tiles) {
        // TODO: nowa metoda może nie działać - sprawdzić

        int count = tiles.getQuantity();
        for(int i=0; i<7 && count>0; i++){
            if(floor[i] == null){
                floor[i] = tiles.getColour();
                count--;
            }
        }
        if(count>0) intersection.addToBox(new TileQuantity(tiles.getColour(), count));
    }

    /**
     * Dodaje "jedynkę" (płytkę gracza rozpoczynającego) na podłoge
     */
    public void addFirstIndicatorToFloor(){
        if(freeOnFloor()>0) {     // cos tam jeszcze miejsca zostało
            floor[7-freeOnFloor()] = Tile.ONE;
        }
        else{       // nie ma miejsca, trzeba wrzucić na początek a płytkę z tego miejsca do boxa
            intersection.addToBox(new TileQuantity(floor[0],1));
            floor[0] = Tile.ONE;
        }
    }

    /**
     * Usuwa wszystkie płytki z danego rzędu ściany
     *
     * @param row - numer rzędu
     */
    private void clearWallRow(int row) {
        Arrays.fill(wall[row], null);
    }

    /**
     * Przenoszenie płytek ze śniany na mozaikę (koniec tury)
     */
    void pushWallToMosaic() {
        for(int i=0;i<5;i++)
            for(int j=0;j<5;j++)
                this.tempMosaic.setTile(i,j,mosaic.getTilePlaced(i,j));
        this.tempColours= new Tile[]{null, null, null, null, null};
        this.pushedToMosaic = new String[]{"", "", "", "", ""};
        for (int i = 0; i < 5; i++) {
            if (freeInRow(i) == 0) {   // zapełniony wiersz na ścianie
                for (int j = 0; j < 5; j++) {
                    if (mosaic.getTileAllowed(i, j) == wall[i][0]) {
                        mosaic.setTile(i, j, wall[i][0]);
                        pushedToMosaic[i] = i + " " + j;
                        tempColours[i]= wall[i][0];
                        break;
                    }
                }
                intersection.addToBox(new TileQuantity(wall[i][0],i));      // TODO upewnić się czy działa dodawanie do worka
                clearWallRow(i);
            }
        }
    }

    /**
     * Inicjalizuje ścianę
     */
    private void initializeWall() {
        wall[0] = new Tile[1];
        wall[1] = new Tile[2];
        wall[2] = new Tile[3];
        wall[3] = new Tile[4];
        wall[4] = new Tile[5];
    }

    /**
     * Usunięcie płytek z podłogi
     */
    public void endOfRound(){
        // zawrtość podłogi leci do pudełka
        for(int i=0; i<7; i++){
            if(floor[i]!=null){
                if(floor[i] != Tile.ONE) intersection.addToBox(new TileQuantity(floor[i],1));
                else one = true;
                floor[i] = null;
            }
        }
    }

    /**
     * Zwraca liczbę wolnych miejsc na podłodze
     * @return - liczba wolnych miejsc
     */
    private int freeOnFloor(){
        int result = 0;
        for(int i=0; i<7; i++){
            if(floor[i] == null) result++;
        }
        return result;
    }

    /**
     * Zwraca liczbę wolnych miejsc w danym rzędzie na ścianie
     *
     * @param rowNumber - numer rzędu
     * @return - liczba wolnych miejsc
     */
    public int freeInRow(int rowNumber) {
        int occupied = 0;
        for (Tile i : wall[rowNumber])
            if (i != null) ++occupied;
        return rowNumber - occupied + 1;
    }

    /**
     * Zwraca informację czy na mozaice istnieje wypełniony cały rząd
     * @return - true:istnieje  false:nie istnieje
     */
    public boolean rowOnMosaicCompleted(){
        for(int i=0; i<5; i++){
            int counter = 0;
            for(int j=0; j<5; j++){
                if(mosaic.getTilePlaced(i,j)!=null) counter++;
            }
            if(counter==5) return true;
        }
        return false;
    }
}

