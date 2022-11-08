package GUI;

import Client.Client;
import Server.Session;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;


/**
 * Wyświetla plansze graczy, stół oraz warsztaty
 */
public class PlayerView extends Container {
    // można grać bezpośrednio z tej klasy
    // została stworzona aby można ją było dodać na dowolny interfejs

    WholeTableGUI wholeTableGUI;
    Client[] clients;
    BoardGUI[] boardGUI;
    ActivePlayerStatus activePlayerStatus;
    String host;
    int port;

    BoardSelectionButton[] chooseBoard;

    /**
     * Konstruktor
     * @param numberOfPlayers - liczba graczy
     * @param singleplayer - true:singleplayer false:multiplayer
     * @param host - ip serwera
     * @param port - port na serwerze
     */
    public PlayerView(int numberOfPlayers, boolean singleplayer, String host, int port) {
        setBounds(0, 0, 1518, 820);
        setLayout(null);

        this.port = port;
        this.host = host;

        activePlayerStatus = new ActivePlayerStatus(numberOfPlayers);

        chooseBoard = new BoardSelectionButton[numberOfPlayers];
        for (int i = 0; i < numberOfPlayers; i++) {
            chooseBoard[i] = new BoardSelectionButton(i, this);
            chooseBoard[i].setBounds(880 + (i * 163), 120, 110, 40);
            add(chooseBoard[i]);
        }

        wholeTableGUI = new WholeTableGUI(numberOfPlayers);
        wholeTableGUI.setBounds(10, 10, 850, 850);

        if(singleplayer) clients=new Client[numberOfPlayers];
        else clients=new Client[1];

        for(int i=0; i<clients.length; i++){
            clients[i] = new Client(wholeTableGUI, new Session(numberOfPlayers), activePlayerStatus,singleplayer,port,host);
            new Thread(clients[i]).start();
        }

        try {
            Thread.sleep(1000); // TODO powinien miec wiecej czasu
        } catch (Exception e) {
        }

        boardGUI = new BoardGUI[numberOfPlayers];

        if(singleplayer){
            for(int i=0; i<boardGUI.length; i++){
                boardGUI[i] = clients[i].getClientsBoard();
            }
        }else boardGUI = clients[0].getBoards();


        for (int i = 0; i < numberOfPlayers; i++) {
            boardGUI[i].setBounds(880, 180, 600, 600);
            boardGUI[i].setVisible(false);
            add(boardGUI[i]);
        }

        if(!singleplayer){
            chooseBoard[clients[0].getPlayerNumber()].setText("Gracz " + (clients[0].getPlayerNumber() + 1) + " (Ty)");
            showBoard(clients[0].getPlayerNumber());
        }else showBoard(0);
        repaint();
        revalidate();
        doLayout();

        add(wholeTableGUI);
        add(activePlayerStatus);
    }

    /**
     * Wyświetla odpowiednią planszę
     * @param numberOfBoard - numer planszy do wyświetlenia
     */
    void showBoard(int numberOfBoard) {
        for (BoardGUI temp : boardGUI) {
            temp.setVisible(false);
        }
        boardGUI[numberOfBoard].setVisible(true);
        for (int i = 0; i < chooseBoard.length; i++)
            chooseBoard[i].displayAsSelected(numberOfBoard == chooseBoard[i].numberOfBoard);
        repaint();
        revalidate();
        doLayout();
    }

    /**
     * Przycisk wyboru wyświetlanej planszy
     */
    class BoardSelectionButton extends JButton implements MouseListener {
        int numberOfBoard;
        PlayerView playerView;

        /**
         * Konstruktor
         * @param numberOfBoard - numer przypisanej plasnzy do przycisku
         * @param playerView - obiekt PlayerView
         */
        public BoardSelectionButton(int numberOfBoard, PlayerView playerView) {
            super("Gracz " + (numberOfBoard + 1));
            this.playerView = playerView;
            this.numberOfBoard = numberOfBoard;
            setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, Color.CYAN));
            setBorderPainted(false);
            addMouseListener(this);
        }

        public void displayAsSelected(boolean selected) {
            setBorderPainted(selected);
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            playerView.showBoard(numberOfBoard);
        }

        @Override
        public void mousePressed(MouseEvent e) {
        }

        @Override
        public void mouseReleased(MouseEvent e) {
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            setCursor(new Cursor(Cursor.HAND_CURSOR));
        }

        @Override
        public void mouseExited(MouseEvent e) {
            setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        }
    }
}

