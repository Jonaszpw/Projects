package Azul;


public class Workshop {

    Tile[] shops = new Tile[36]; // tablica zawierająca zawartość wszystkich warsztatów
    private int mark; //zmienna ułatwiająca poruszanie się po tablicach wewnatrz tej klasy

    /**
     * konstruktor warsztatów (w zależności od ilości graczy)
     */
    public Workshop(int numberOfPlayers, Bag bag) {
        mark = (4 * (numberOfPlayers * 2) + 4);
        newWorkshop(bag);
    }

    /**
     * metoda tworząca nowe warsztaty z zawartości worka
     */
    public void newWorkshop(Bag bag) {
            for (int i = 0; i < mark; i++)
                shops[i] = bag.pop();
    }

    /**
     * metoda zwracająca ile sztuk danej plytki znajduje sie w konkretnym warsztacie
     */
    public TileQuantity workshopTileQuantity(Tile colour, int whichShop) {
        int start = (whichShop * 4);
        int quantity = 0;

        for (int i = start; i < start + 4; i++) {
            if (shops[i] == colour) quantity++;
        }

        return new TileQuantity(colour, quantity);
    }

    /**
     * metoda sprawdzajaca czy dana plytka znajduje sie w danym warsztatcie, bez znaczenia w jakiej ilości
     */
    public boolean workshopValidate(Tile colour, int whichShop) {
        int start = (whichShop * 4);
        boolean validate = false;

        if (shops[start] == null) return validate;
        for (int i = start; i < start + 4; i++) {
            if (shops[i] == colour) validate = true;
        }

        return validate;
    }

    /**
     * zwraca 4-elementowa tablice zawierajaca zawartość podanego numeru warsztatu
     */
    public Tile[] workshopGet(int whichShop) {
        int start = (whichShop * 4);
        int end = start + 4;
        Tile[] returnArray = new Tile[4];
        while (start < end) {
            returnArray[start - (whichShop * 4)] = shops[start];
            start++;
        }
        return returnArray;
    }

    /**
     * zwraca true jeśli warsztaty są puste
     */
    public boolean isEmpty(){
        boolean returnValue = true;
        for (int i = 0; i < mark; i++)
            if (shops[i] != null) returnValue = false;

        return returnValue;
    }

    /**
     * zwraca ile płytek jest potrzebnych do utworzenia nowych warsztatów
     */
    public int getMark() {
        return mark;
    }
}
