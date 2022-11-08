package Server;

import Azul.Intersection;
import Azul.Player;
import Azul.TileQuantity;

/**
 * Klasa przetrzymująca aktualny stan rozrgrywki
 */
public class Session {
    int turnCounter;
    int numberOfPlayers;
    int nextRoundFirst;
   public Intersection intersection;
   public Player[] players;


    public Session(int numberOfPlayers) {

        this.intersection = new Intersection(numberOfPlayers);
        this.numberOfPlayers = numberOfPlayers;
        this.turnCounter = 0;
        this.nextRoundFirst=0;
        players = new Player[numberOfPlayers];

        for (int i = 0; i < numberOfPlayers; i++){
            players[i] = new Player(i);
            players[i].getBoard().setIntersection(intersection);        // TODO jak coś nie działa to szukać tutaj
        }


    }
    public void updateBoard(String message) {
        MoveObject moveObject = DataHandler.receiveMove(message);
        if(moveObject.getFrom()==-1){   // pobieranie z podłogi, sprawdzenie czy jest jedynka
            if(intersection.getFirstIndicator()) players[turnCounter%numberOfPlayers].getBoard().addFirstIndicatorToFloor();
        }
        TileQuantity tq= intersection.intersectionPick(moveObject.getFrom(),moveObject.getColour());
        if(moveObject.getRow() == -1) players[turnCounter%numberOfPlayers].getBoard().addTilesToFloor(tq);      // podłoga
        else players[turnCounter%numberOfPlayers].getBoard().addTilesToWall(moveObject.getRow(),tq);            // ściana
    }

    public Intersection getIntersection() {
        return intersection;
    }

    public int getNumberOfPlayers() {
        return numberOfPlayers;
    }

    public int getActivePlayer(){
        System.out.println("Liczba graczy: "+turnCounter+"%"+numberOfPlayers+" = " + turnCounter%numberOfPlayers);
        //if(turnCounter==0) return 0;
        return turnCounter%numberOfPlayers;
    }

    public Player getPlayer(int playerNumber){
        return players[playerNumber];
    }

    public Session setNextRoundFirst(int nextRoundFirst) {
        this.nextRoundFirst = nextRoundFirst;
        return this;
    }

    public Session setTurnCounter(int turnCounter) {
        this.turnCounter = turnCounter;
        return this;
    }

    public int getTurnCounter() {
        return turnCounter;
    }

    public int getNextRoundFirst() {
        return nextRoundFirst;
    }
}
