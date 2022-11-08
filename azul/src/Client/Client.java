package Client;

import GUI.*;
import Server.Protocol;
import Server.Session;
import com.google.gson.Gson;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;


/**
 * Klient - pojedynczy gracz
 */
public class Client implements Runnable {

    private String host;
    private int port;
    private BufferedReader input;
    private PrintWriter output;
    private Socket socket;
    private boolean kys = false;
    private Session session;
    private BoardGUI[] boardGUI;
    private WholeTableGUI wholeTableGUI;
    private ActivePlayerStatus activePlayerStatus;
    private int playerNumber = -1;
    boolean isSingleplayerGame;

    /**
     * Kontruktor
     * @param wholeTableGUI - obiekt WholeTableGUI
     * @param session - obiekt Session
     * @param activePlayerStatus - obiekt ActivePlayerStatus
     * @param singleplayer - true:rozgrywka singleplayer  false:multiplayer
     * @param port - numer portu do którego podłączy się klient
     * @param host - ip serwera do którego podłączy się kleint
     */
    public Client(WholeTableGUI wholeTableGUI, Session session, ActivePlayerStatus activePlayerStatus, boolean singleplayer, int port, String host){
        this.isSingleplayerGame = singleplayer;
        this.activePlayerStatus = activePlayerStatus;
        this.wholeTableGUI = wholeTableGUI;
        this.session = session;
        this.port = port;
        this.host = host;

        boardGUI = new BoardGUI[session.getNumberOfPlayers()];
        for(int i=0; i<boardGUI.length; i++) boardGUI[i] = new BoardGUI();
    }

    public BoardGUI[] getBoards(){
        return boardGUI;
    }
    public BoardGUI getClientsBoard() {return boardGUI[playerNumber];}
    public int getPlayerNumber(){
        return playerNumber;
    }

    /**
     * Aktualizuje cały interfejs użytkownika
     */
    void updateGUI(){
        for(int i=0; i<boardGUI.length; i++){
            boardGUI[i].setWall(session.getPlayer(i).getBoard().exportWall());
            boardGUI[i].setMosaic(session.getPlayer(i).getBoard().exportMosaic());
            boardGUI[i].setFloor(session.getPlayer(i).getBoard().exportFloor());
            boardGUI[i].setPoints(session.getPlayer(i).getScore());
            boardGUI[i].repaint();
            boardGUI[i].revalidate();
            boardGUI[i].doLayout();
        }

        if(isSingleplayerGame){
            if(playerNumber==0){
                activePlayerStatus.showActivePlayerStatus(session.getActivePlayer());
                wholeTableGUI.updateWholeTableGUI(session.getIntersection().workshop, session.getIntersection().getTableStatus(), session.getIntersection().getFirstIndicator());
                wholeTableGUI.repaint();
                wholeTableGUI.revalidate();
                wholeTableGUI.doLayout();
            }
        }
        else{
            activePlayerStatus.showActivePlayerStatus(session.getActivePlayer());
            wholeTableGUI.updateWholeTableGUI(session.getIntersection().workshop, session.getIntersection().getTableStatus(), session.getIntersection().getFirstIndicator());
            wholeTableGUI.repaint();
            wholeTableGUI.revalidate();
            wholeTableGUI.doLayout();
        }
    }

    /**
     * Ustawia numer gracza oraz przypisuje mu planszę
     * @param messageFromServer - String otrzymany od serwera
     */
    public void setPlayerNumberAndBoard(String messageFromServer) {
        String[] message = messageFromServer.split(" ");
        playerNumber = Integer.parseInt(message[1]);
        boardGUI[playerNumber] = new BoardGUI(this);
    }

    /**
     * Aktualizuje sesje na podstawie pliku json
     * @param json - String
     */
    void updateSession(String json){
        Gson gson = new Gson();
        session = gson.fromJson(json.substring(6), Session.class);
    }

    /**
     * Wysyła ruch do serwera
     */
    public void sendAction(){
        if(wholeTableGUI.getPick() == WholeTableGUI.NONE_SELECTION || boardGUI[playerNumber].getSelection() == BoardGUI.NONE_SELECTION)
            JOptionPane.showMessageDialog(boardGUI[0], "Należy wybrać warsztat oraz odpowiedneie pole na planszy!", "Azul", JOptionPane.WARNING_MESSAGE);
        else output.println(Protocol.RUCH + " " + wholeTableGUI.getPick().getQuantity() + " " + boardGUI[playerNumber].getSelection() + " " + wholeTableGUI.getPick().getColour().name());
    }

    /**
     * Wyświetla wiadomość w nowym oknie
     * @param message - wiadomość do wyświetlenia
     */
    public void displayTextMessage(String message) {
        // UWAGA znak nowej lini to %
        // komuniacja z serwerem poprzez PrintWriter tego wymagała

        String toDisplay = message.substring(5);
        StringBuilder fixed = new StringBuilder();
        for(int i=0;i<toDisplay.length(); i++){
            if(toDisplay.charAt(i)=='%') fixed.append("\n");
            else fixed.append(toDisplay.charAt(i));
        }

        JOptionPane.showMessageDialog(boardGUI[0], fixed.toString(), "Azul", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Wyłącza klienta
     */
    void killClient() {
        kys = true;
    }

    @Override
    public void run() {
        try {
            socket = new Socket(host, port);
            ClientSideMessageReceiver messageReceiver = new ClientSideMessageReceiver(socket, this);
            new Thread(messageReceiver).start();
            input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            output = new PrintWriter(socket.getOutputStream(), true);

            while (!kys) {Thread.sleep(1000);}
        } catch (Exception e) {
            System.err.println("Client exception: " + e);
        } finally {
            try {
                input.close();
                output.close();
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
