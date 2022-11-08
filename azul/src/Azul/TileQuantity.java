package Azul;

/**
 * klasa do zwracania odpowiednich i jednolitych informacji o ilości danej płytki w danym miejscu
 */
public class TileQuantity {
    private Tile colour;
    private int quantity;

    public TileQuantity(Tile colour, int quantity) {
        this.colour = colour;
        this.quantity = quantity;
    }

    /**
     * quantity getter
     * @return int quantity
     */
    public int getQuantity() {
        return quantity;
    }

    public Tile getColour() {
        return colour;
    }

    @Override
    public String toString() {
        return quantity + colour.toString();
    }
}

