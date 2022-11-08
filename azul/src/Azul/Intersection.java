package Azul;

public class Intersection {

    public Workshop workshop;
    private Bag bag;
    private Table table;
    private Box box;
    private boolean firstIndicator;
    private int players;

    /**
     * konstruktor części wspólej dla wszystkich graczy (stół, warsztaty, worek)
     */
    public Intersection(int numberOfPlayers) {
        this.bag = new Bag(20, 20, 20, 20, 20);
        this.table = new Table();
        this.workshop = new Workshop(numberOfPlayers, bag);
        this.box = new Box();
        this.firstIndicator = true;
        players = numberOfPlayers;
    }

    /**
     * zwraca true jeśli jedynka znajduje się na stole
     */
    public boolean getFirstIndicator(){
        return this.firstIndicator;
    }

    /**
     * sprawdza czy stół i warsztaty są już puste i można zacząć nową turę (zwraca true jesli puste)
     */
    public boolean isEmpty(){
        return workshop.isEmpty() & table.isEmpty();
    }


    /**
     * metoda służąca do pobrania elementów ze stołu (form=-1) lub z warsztatów (from>=0)
     * zwraca obiekt TileQUantity odpowiadający pobranym elementom
     */
    public TileQuantity intersectionPick(int from, Tile colour){
        if (from == -1) {
            firstIndicator = false;
            return tablePick(colour);
        }
        if (from>=0 && from < (players*2) + 1) return workshopPick(colour, from);

        System.err.println("ERROR (intersectionPick): niepoprawny numer warsztatu/stołu");
        return new TileQuantity(colour, 0);
    }

    //METODY OBSŁUGUJĄCE WORKSHOP:

    /**
     * wpisujemy z ktorego konkretnie warsztatu chcemy pobrac jaki kolor i zwracamy odpowiednia liste elementow a reszta trafia na stol (ktory tez jest lista)
     * zwraca obiekt TileQUantity odpowiadający pobranym elementom
     */
    private TileQuantity workshopPick(Tile colour, int fromWhichShop) {
        int quantity = 0;
        if (workshop.workshopValidate(colour, fromWhichShop)) {

            int start = (fromWhichShop * 4);

            for (int i = start; i < start + 4; i++) {
                if (workshop.shops[i] == colour) {
                    quantity++;
                    workshop.shops[i] = null;
                } else {
                    table.add(workshop.shops[i]);
                    workshop.shops[i] = null;
                }
            }

        }
        return new TileQuantity(colour, quantity);
    }

    /**
     * zwraca obiekt TileQuantity wskazujący ile elementów danego koloru znajduje się w danym warsztacie
     */
    public TileQuantity workshopTileQuantity(Tile colour, int whichShop) {
        return workshop.workshopTileQuantity(colour, whichShop);
    }


    /**
     * wywołać tą metodę kiedy chce się utworzyć nowy workshop (pobierając elementy z baga), w przypadku brakujących elementów w bagu, bag jest uzupełniany z boxa
     */
    public void createNewWorkshop(){

        firstIndicator = true;

        if (bag.size() >= workshop.getMark()){
            workshop.newWorkshop(bag);
        }
        else {
            bag.bagSupply(box);
            workshop.newWorkshop(bag);
        }
    }

    //METODY OBSŁUGUJĄCE TABLE:

    /**
     * weź ze stołu wszystkie elementy danego koloru
     * @return TileQuantity
     */
    private TileQuantity tablePick(Tile colour) {
        return table.tablePick(colour);
    }

    /**
     * podejrzyj na stole wszystkie elementy danego koloru (bez ich usuwania ze stołu)
     * @return TileQuantity
     */
    public TileQuantity tableGet(Tile colour) {
        return table.tableGet(colour);
    }

    /**
     * zwraca true jeśli element o danym kolorze znajduje się na stole
     */
    public boolean tableValidate(Tile colour) {
        return table.tableValidate(colour);
    }

    public Tile[] getTableStatus() {
        return table.getTableStatus();
    }

    //METODY OBSŁUGUJĄCE BOX:

    /**
     * metoda przyjmuje obiekt TileQuantitty i w odpowiedni sposób umiesza płytki w boxie
     */
    public void addToBox(TileQuantity tilesToAdd){

        for(int j = 0; j < tilesToAdd.getQuantity(); j++)
            box.add(tilesToAdd.getColour());
    }

}
