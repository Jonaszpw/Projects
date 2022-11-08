package Server;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Klasa odpowiedzialna za obsługę pojedynczego klienta
 */
public class ClientHandler implements Runnable {

    public String lastAction = "";
    private int playerNumber;
    private Server server;
    private Socket client;
    private PrintWriter output;
    private ServerSideMessageReceiver messageReceiver;
    private ServerSideMessageHandler messageHandler = new ServerSideMessageHandler(this);
    private volatile boolean kys = false;       // wartość true spowoduje zakończenie obsługi klienta
    private boolean active = false;

    /**
     * Konstruktor klasy
     *
     * @param client - socket
     */
    public ClientHandler(Socket client, int playerNumber,Server server) throws IOException {
        this.client = client;
        this.playerNumber = playerNumber;
        this.server = server;
        output = new PrintWriter(client.getOutputStream(), true);
        messageReceiver = new ServerSideMessageReceiver(client, messageHandler, this);
        System.out.println("Połączono z klientem");
        for(int i=0; i<3; i++)
            output.println(Protocol.PLAYER_ID + " " + playerNumber);
    }

    @Override
    public void run() {
        new Thread(messageReceiver).start();
        while (!kys) {
        }
    }

    public boolean isActive() {
        return active;
    }

    /**
     * Wysyłanie stanu mapy do klienta
     */
    void sendMap() throws IOException {
        DataPacker dp = new DataPacker();
        output.println(Protocol.MAPA + dp.packData(server.getSession()));
    }

    /**
     * Wysłąnie wiadomości do klienta
     * @param message - wiadomość (nowa linia musi być oznaczona znakiem % zamiast \n)
     */
    void sendMessage(String message){
        output.println(Protocol.MESSAGE + message);
    }

    /**
     * Odłączenie klienta
     */
    void disconnectClient() {
        output.close();
        try {
            messageReceiver.input.close();
        } catch (Exception e) {
            System.err.println(e);
        }
        try {
            client.close();
        } catch (Exception e) {
            System.err.println(e);
        }
        kys = true;
        server.removeClientHandler(this);
    }

    /**
     * Wysłanie odpowiedzi pong
     */
    void pong() {
        output.println(Protocol.PONG);
    }

    /**
     * Wysłanie komunikatu o rozpoczęciu tury gracza
     */
    void startTurn() {
        active = true;
        output.println(Protocol.START_TURY);
    }

    /**
     * Wysłanie komunikaty o końcu tury gracza
     */
    void endTurn() {
        active = false;
        output.println(Protocol.KONIEC_TURY);
    }

    /**
     * Informuje klienta o zaakceptowaniu ruchu
     */
    void ok() {
        output.println(Protocol.OK);
    }

    /**
     * Informuje klienta o odrzuceniu ruchu
     */
    void wrong() {
        output.println(Protocol.WRONG);
    }

}
