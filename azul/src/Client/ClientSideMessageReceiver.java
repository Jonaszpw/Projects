package Client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 * Klasa odpowiedzialna za odbieranie wiadmości od serwera (na oddzielnym wątku)
 */
public class ClientSideMessageReceiver implements Runnable {

    private ClientSideMessageHandler messageHandler;
    private BufferedReader input;
    private Client client;

    /**
     * Konstruktor klasy
     *
     * @param socket - socket z którego czytane będą wiadmości
     */
    public ClientSideMessageReceiver(Socket socket, Client client) throws IOException {
        input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.client = client;
        messageHandler = new ClientSideMessageHandler(client);
    }

    @Override
    public void run() {
        try {
            String message;
            while ((message = input.readLine()) != null) {
                messageHandler.handleMessage(message);
            }
        } catch (Exception e) {
            System.err.println("ClientSideMessageReceiver: " + e.getMessage());
        } finally {
            try {
                input.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            client.killClient();
        }
    }
}
