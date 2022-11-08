package Client;

import Server.Protocol;

/**
 * Klasa odpowiedzialna za przetwarzanie wiadomości odebranych od serwera
 */
public class ClientSideMessageHandler {

    Client client;

    public ClientSideMessageHandler(Client client){
        this.client = client;
    }

    /**
     * Metoda podejmująca odpowiednie działania w zależności od odebranej wiadomości
     *
     * @param message - wiadomość odebrana od serwera
     */
    public void handleMessage(String message) {
        if (message.contains(Protocol.START_TURY)) {
            System.out.println("Twoja tura!");
        } else if (message.contains(Protocol.KONIEC_TURY)) {
            System.out.println("Koniec twojej tury");
        } else if (message.contains(Protocol.MAPA)) {
            System.out.println("Otrzymano mape...");
            client.updateSession(message);
            client.updateGUI();
        } else if (message.contains(Protocol.OK)) {
            System.out.println("Ruch zaakceptowany");
        } else if (message.contains(Protocol.WRONG)) {
            System.out.println("Ruch niedozwolony! Spróbuj jeszcze raz");
        } else if (message.contains(Protocol.PONG)) {
            System.out.println("pong");
        } else if (message.contains(Protocol.PLAYER_ID)){
            client.setPlayerNumberAndBoard(message);
        } else if (message.contains(Protocol.MESSAGE)) {
            client.displayTextMessage(message);
        } else System.out.println(message);
    }
}
