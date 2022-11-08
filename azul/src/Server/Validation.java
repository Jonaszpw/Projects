package Server;

import Azul.Tile;

import java.util.Random;

/**
 * Klasa stosowana do walidacji ruchu otrzymanego od klienta
 */
public class Validation {

    public static boolean validateAction(String message, Session session, int playerNumber, ClientHandler clientHandler) {

        //rozbiera wiadomosc na czynniki pierwsze i przekazuje odpowiednie dane do metod prywatnych validateAction{From/Row}
        MoveObject moveObject = DataHandler.receiveMove(message);
        return validateActionFrom(moveObject.getFrom(), moveObject.getColour(), session) & validateActionRow(moveObject.getRow(), moveObject.getColour(), session, playerNumber, clientHandler);

    }

    /**
     * zwraca true jesli dany element mozna pobrac z danego miejsca
     */
    private static boolean validateActionFrom(int from, Tile colour, Session session){
        //from=-1 oznacza element na stole; from={0,1,2,...,8} oznacza element z danego warsztatu

        if (from == -1) return session.getIntersection().tableValidate(colour);


        //sprawdzenie czy element znajduje się w danym warsztacie
        int numberOfShops = (session.numberOfPlayers*2) + 1;
        if (from >= 0 && from < numberOfShops) return session.getIntersection().workshopTileQuantity(colour, from).getQuantity() > 0;


        System.err.println("Validation ERROR: nie ma takiego numeru warsztatu/stołu");
        return false;

    }

    /**
     * zwraca true jeśli podany element można wstawić w dane miejsce
     */
    private static boolean validateActionRow(int row, Tile colour, Session session, int playerNumber, ClientHandler clientHandler){

        if(colour==null){
            System.err.println("validateActionRow error: płytka nie może być null");
            return false;
        }

        if(row<-1 || row >4) {  // poza przedziałem
            System.err.println("validateActionRow error: nie ma takiego wiersza");
            return false;
        }

        ////////// wybrana podłoga ///////////////
        if(row == -1) return true;     // na podłogę zawsze można

        ////////// pole mozaiki zajete ///////////
        for(int i=0; i<5; i++){
            if(colour == session.players[playerNumber].getBoard().getMosaic().getTilePlaced(row,i)){
                System.err.println("validateActionRow error: pole takiego koloru jest już zajęte na mozaice");
                clientHandler.sendMessage("Pole takiego koloru jest już zajęte na mozaice!");
                return false;
            }
        }

        ///////// wybrany pusty rząd /////////////
        if(session.players[playerNumber].getBoard().freeInRow(row) == row+1) return true;

        ///////// wybrany pełny rząd /////////////
        if(session.players[playerNumber].getBoard().freeInRow(row) == 0) {
            System.err.println("validateActionRow error: wybrany rząd jest już pełny");
            clientHandler.sendMessage("Wybrany rząd jest pełny!");
            return false;
        }

        ///////// wybrany niepusty i niepełny rząd //////////
        if(session.players[playerNumber].getBoard().getColorOfTheRow(row) == colour) return true;
        else {
            System.err.println("validateActionRow error: w tym rzędzie znajduje się już płytka innego koloru");
            clientHandler.sendMessage("W tym rzędzie znajduje się już płytka innego koloru!");
            return false;
        }
    }
}
