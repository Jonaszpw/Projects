package Azul;

import java.util.Collections;
import java.util.Stack;

public class Bag {

    private Stack bag = new Stack(); //reprezentacja worka ze wszystkimi elementami

    /**
     * konstruktor worka - stowrz worek ze wszystkimi elementami
     */
    public Bag(int blacks, int whites, int reds, int yellows, int blues) {
        for (int i = 0; i < 20; i++) {
            if (blacks > 0) {
                bag.push(Tile.BLACK);
                blacks--;
            }
            if (whites > 0) {
                bag.push(Tile.WHITE);
                whites--;
            }
            if (reds > 0) {
                bag.push(Tile.RED);
                reds--;
            }
            if (yellows > 0) {
                bag.push(Tile.YELLOW);
                yellows--;
            }
            if (blues > 0) {
                bag.push(Tile.BLUE);
                blues--;
            }
        }
        Collections.shuffle(bag);

    }

    /**
     * uzupełnij worek zawartością pudełka
     */
    public void bagSupply(Box box){
        //sprawdzenie zawartosci boxa
        TileQuantity[] boxContets = box.get();
        int blacks = 0, whites = 0, reds = 0, yellows = 0, blues = 0;
        for (int i = 0; i < boxContets.length; i++){
            if (boxContets[i].getColour() == Tile.BLACK) blacks += boxContets[i].getQuantity();
            else if (boxContets[i].getColour() == Tile.WHITE) whites += boxContets[i].getQuantity();
            else if (boxContets[i].getColour() == Tile.YELLOW) yellows += boxContets[i].getQuantity();
            else if (boxContets[i].getColour() == Tile.RED) reds += boxContets[i].getQuantity();
            else if (boxContets[i].getColour() == Tile.BLUE) blues += boxContets[i].getQuantity();
        }

        //uzupelnij worek na podstawie tego co bylo w boxie
        for (int i = 0; i < 20; i++) {
            if (blacks > 0) {
                bag.push(Tile.BLACK);
                blacks--;
            }
            if (whites > 0) {
                bag.push(Tile.WHITE);
                whites--;
            }
            if (reds > 0) {
                bag.push(Tile.RED);
                reds--;
            }
            if (yellows > 0) {
                bag.push(Tile.YELLOW);
                yellows--;
            }
            if (blues > 0) {
                bag.push(Tile.BLUE);
                blues--;
            }
        }
        Collections.shuffle(bag);
    }

    /**
     * metoda odpowiadająca wyciągnięciu elementu z worka (usuwa element z gory i go zwraca)
     */
    public Tile pop() {
        return (Tile) bag.pop();
    }

    public int size(){
        return bag.size();
    }

    public boolean isEmpty(){
        return bag.isEmpty();
    }

}
