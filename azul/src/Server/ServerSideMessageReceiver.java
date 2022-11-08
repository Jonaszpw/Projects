package Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 * Klasa odpowiedzialna za odbieranie wiadmości od klienta (na oddzielnym wątku)
 */
public class ServerSideMessageReceiver implements Runnable {

    protected BufferedReader input;
    private String message;
    ServerSideMessageHandler messageHandler;
    ClientHandler clientHandler;

    /**
     * Konstruktor
     *
     * @param socket
     * @param messageHandler
     * @param clientHandler
     */
    public ServerSideMessageReceiver(Socket socket, ServerSideMessageHandler messageHandler, ClientHandler clientHandler) throws IOException {
        input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.messageHandler = messageHandler;
        this.clientHandler = clientHandler;
    }

    @Override
    public void run() {
        try {
            while ((message = input.readLine()) != null) {
                //System.out.println(message);
                messageHandler.handleMessage(message);
            }
        } catch (Exception e) {
            //System.err.println("ServerSideMessageReceiver: " + e.getMessage());
        } finally {
            try {
                input.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            clientHandler.disconnectClient();
            System.err.println("MessageReceiver: Utracono połączenie z klientem!");
        }
    }
}
