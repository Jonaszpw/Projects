package Server;

import Azul.GameLogic;
import Azul.Player;
import Client.Client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * Serwer
 */
public class Server implements Runnable {

    private int numberOfPlayers;
    private Session session;
    private GameLogic gameLogic;
    private ServerSocket serverSocket;
    public ArrayList<ClientHandler> clients = new ArrayList<>();
    protected ExecutorService pool;  // liczba wątków, UWAGA! należy zarezerwować dodatkowy wątek przy włączonym wyświetlaniu statusu
    private boolean singleplayer;

    int port;

    public Server(int numberOfPlayers, int port, boolean singleplayer) {
        this.singleplayer = singleplayer;
        this.port = port;
        this.numberOfPlayers = numberOfPlayers;
        pool = Executors.newFixedThreadPool(numberOfPlayers);
        session = new Session(numberOfPlayers);
        gameLogic = new GameLogic();
    }

    /**
     * Usuwa dany obiekt clientHandler
     *
     * @param clientHandler
     */
    public void removeClientHandler(ClientHandler clientHandler) {
        clients.remove(clientHandler);
    }

    public Session getSession() {
        return session;
    }

    /**
     * Zwraca publiczny adres IP serwera
     */
    public static String getPublicIP() {
        String result;
        try {
            URL urlName = new URL("http://bot.whatismyipaddress.com");
            BufferedReader sc = new BufferedReader(new InputStreamReader(urlName.openStream()));
            result = sc.readLine().trim();
        } catch (Exception e) {
            result = "Cannot Execute Properly";
        }
        return result;
    }

    private void sendMapToAll() throws IOException {
        for (ClientHandler temp : clients)
            temp.sendMap();
    }

    public void killServer(){
        try{
            serverSocket.close();
        }catch (Exception e){}
    }

    @Override
    public void run() {
        InetAddress localHost = null;
        try {
            localHost = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        System.out.println("Local address = " + localHost.getHostAddress());

        try {
            serverSocket = new ServerSocket(port);
        } catch (Exception e) {
            System.out.println("Creating server socket: " + e.getMessage());
            return;
        }

        // oczekiwanie na graczy
        while (clients.size() < numberOfPlayers) {
            try {
                System.out.println("Oczekiwanie na " + (numberOfPlayers - clients.size()) + " graczy...");
                Socket socket = serverSocket.accept();
                ClientHandler clientHandler = new ClientHandler(socket, clients.size(), this);
                clients.add(clientHandler);
                pool.execute(clientHandler);
            } catch (Exception e) {
                System.out.println("Server: " + e.getMessage());
            }
        }

        System.out.println("Dobra są już wszyscy");

        // wysyłanie planszy
        try {
            sendMapToAll();
        } catch (IOException e) {
            e.printStackTrace();
        }

        while (gameLogic.getFlag()) {
            int activePlayer = session.turnCounter % numberOfPlayers;
            String lastActionLocal;

            // przkazanie ruchu
            clients.get(activePlayer).startTurn();

            // oczekiwanie na ruch
            while (true) {
                lastActionLocal = "";
                while (!clients.get(activePlayer).lastAction.contains("[RUCH]")) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                lastActionLocal = clients.get(activePlayer).lastAction;
                clients.get(activePlayer).lastAction = "";
                System.out.println("Otzymano ruch: " + lastActionLocal);

                if (Validation.validateAction(lastActionLocal, session, activePlayer,clients.get(activePlayer))) {
                    System.out.println("Ruch zaakceptowany");
                    clients.get(activePlayer).ok();
                    session.updateBoard(lastActionLocal);
                    gameLogic.summarizeRound(session);
                    break;
                } else {
                    clients.get(activePlayer).wrong();
                    System.out.println("Ruch odrzucony!");
                }
            }
            clients.get(activePlayer).endTurn();

            try {
                sendMapToAll();
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        ////// podsumowanie gry ///////
        int counter = 1;
      StringBuilder stringBuilder=new StringBuilder();
      stringBuilder.append("Koniec gry!%");
        while (!gameLogic.players.isEmpty()) {
            Player i = gameLogic.players.poll();
            stringBuilder.append(i + " miejsce " + counter+"%");
            counter++;
        }

        if(singleplayer) clients.get(0).sendMessage(stringBuilder.toString());
        else{
            for (ClientHandler clientHandler : clients) {
                clientHandler.sendMessage(stringBuilder.toString());
            }
        }
    }


    // zostawiam żeby można było na wirtualce hostowac czy cos
    public static void main(String[] args) {
        new Thread(new Server(2,6666,false)).start();
    }
}

