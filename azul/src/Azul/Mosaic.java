package Azul;

/**
 * Mozaika
 */
public class Mosaic {
    private final MosaicSquare[][] mosaic = makeMosaic();

    /**
     * Konwertuje mozaikę na macierz 5x5 booleanów gdzie true:płytka ułożona  false:brak płytki
     * @return boolean[5][5]
     */
    public boolean[][] exportMosaic(){
        boolean[][] result = new boolean[5][5];
        for(int i=0; i<5; i++){
            for(int j=0; j<5; j++){
                result[i][j] = mosaic[i][j].isPlaced();
            }
        }
        return result;
    }

    /**
     * Inicjalizuje kratki mozaiki dozwolonymi polami
     */
    private MosaicSquare[][] makeMosaic() {
        MosaicSquare[][] tempMosaic = new MosaicSquare[5][5];
        tempMosaic[0][0] = new MosaicSquare(Tile.BLUE);
        tempMosaic[1][1] = new MosaicSquare(Tile.BLUE);
        tempMosaic[2][2] = new MosaicSquare(Tile.BLUE);
        tempMosaic[3][3] = new MosaicSquare(Tile.BLUE);
        tempMosaic[4][4] = new MosaicSquare(Tile.BLUE);

        tempMosaic[0][1] = new MosaicSquare(Tile.YELLOW);
        tempMosaic[1][2] = new MosaicSquare(Tile.YELLOW);
        tempMosaic[2][3] = new MosaicSquare(Tile.YELLOW);
        tempMosaic[3][4] = new MosaicSquare(Tile.YELLOW);
        tempMosaic[4][0] = new MosaicSquare(Tile.YELLOW);

        tempMosaic[0][2] = new MosaicSquare(Tile.RED);
        tempMosaic[1][3] = new MosaicSquare(Tile.RED);
        tempMosaic[2][4] = new MosaicSquare(Tile.RED);
        tempMosaic[3][0] = new MosaicSquare(Tile.RED);
        tempMosaic[4][1] = new MosaicSquare(Tile.RED);

        tempMosaic[0][3] = new MosaicSquare(Tile.BLACK);
        tempMosaic[1][4] = new MosaicSquare(Tile.BLACK);
        tempMosaic[2][0] = new MosaicSquare(Tile.BLACK);
        tempMosaic[3][1] = new MosaicSquare(Tile.BLACK);
        tempMosaic[4][2] = new MosaicSquare(Tile.BLACK);

        tempMosaic[0][4] = new MosaicSquare(Tile.WHITE);
        tempMosaic[1][0] = new MosaicSquare(Tile.WHITE);
        tempMosaic[2][1] = new MosaicSquare(Tile.WHITE);
        tempMosaic[3][2] = new MosaicSquare(Tile.WHITE);
        tempMosaic[4][3] = new MosaicSquare(Tile.WHITE);

        return tempMosaic;
    }

    /**
     * Zwraca ustawioną płytkę na danym polu mozaiki
     *
     * @param row    - wiersz
     * @param column - kolumna
     * @return - płytka
     */
    public Tile getTilePlaced(int row, int column) {
        return mosaic[row][column].getPlaced();
    }

    /**
     * Zwraca dozwoloną płytkę na danym polu mozaiki
     *
     * @param row    - wiersz
     * @param column - kolumna
     * @return - płytka
     */
    public Tile getTileAllowed(int row, int column) {
        return mosaic[row][column].getAllowed();
    }

    /**
     * Ustawia płytkę na danym polu mozaiki
     *
     * @param row    - wiersz
     * @param column - kolumna
     * @param tile   - płytka
     */
    public void setTile(int row, int column, Tile tile) {
        mosaic[row][column].setPlaced(tile);
    }
}

/**
 * Klasa pomocnicza - odpowiada za pojedyncze pole mozaiki
 * Przechowuje ułożoną płytkę oraz dozwoloną płytkę
 */
class MosaicSquare {
    private Tile placed;
    private final Tile allowed;

    MosaicSquare(Tile allowed) {
        this.allowed = allowed;
        this.placed = null;
    }

    boolean isPlaced(){
        return placed!=null;
    }

    void setPlaced(Tile placed) {
        this.placed = placed;
    }

    Tile getAllowed() {
        return allowed;
    }

    Tile getPlaced() {
        return placed;
    }
}
