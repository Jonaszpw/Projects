package Server;

import java.io.IOException;

/**
 * Klasa odpowiedzialna za przetwarzanie wiadomości odebranych od klienta
 */
public class ServerSideMessageHandler {

    ClientHandler clientHandler;

    /**
     * Konstruktor
     *
     * @param clientHandler
     */
    public ServerSideMessageHandler(ClientHandler clientHandler) {
        this.clientHandler = clientHandler;
    }

    /**
     * Metoda podejmująca odpowiednie działania w zależności od odebranej wiadomości
     *
     * @param message - wiadomość odebrana od klienta
     */
    public void handleMessage(String message) throws IOException {
        if (message.contains(Protocol.MAPA)) {
            clientHandler.sendMap();
        } else if (message.contains(Protocol.RUCH) && clientHandler.isActive()) {
            clientHandler.lastAction = message;
        } else if (message.contains(Protocol.PING)) {
            clientHandler.pong();
        } else if (message.contains(Protocol.ODLACZ)) {
            clientHandler.disconnectClient();
            System.out.println("Koniec połączenia");
        }
    }
}
