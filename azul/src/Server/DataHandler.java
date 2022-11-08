package Server;

import Azul.Tile;
import Server.Validation;

public class DataHandler {

    /**
     * przyjmuje wiadomość w ustalonym przez nas protokole i zwraca obiekt klasy MoveObject z odpowiednimi parametrami (skąd, dokąd, jaki kolor)
     * @param message
     * @return MoveObject
     */
    public static MoveObject receiveMove(String message) {
        //"[RUCH] <skad(-1, 0, 1, 2.. 8)> <dokad (0, 1, 2, 3, 4)> <String("NAZWA ENUMA")>"
        String[] messageData = message.split(" ");
        int from = Integer.parseInt(messageData[1]);
        int row = Integer.parseInt(messageData[2]);
        Azul.Tile colour;
        switch (messageData[3]) {
            case "BLUE":
                colour = Tile.BLUE;
                break;
            case "YELLOW":
                colour = Tile.YELLOW;
                break;
            case "RED":
                colour = Tile.RED;
                break;
            case "BLACK":
                colour = Tile.BLACK;
                break;
            case "WHITE":
                colour = Tile.WHITE;
                break;
            default:
                System.err.println("DataHandler: Error, tile not recognized");
                colour = null;
        }

        return(new MoveObject(from, row, colour));
    }
}
