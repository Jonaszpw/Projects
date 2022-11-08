package Server;

/**
 * Klasa odpowiedzialna za wypisywanie informacji dotyczących liczby podłączonych klientów
 * Używana tylko do testów
 */
public class ConnectedClientsStatus implements Runnable {

    // nigdzie nie używana ale
    // warto zostawić bo pomaga w szukaniu błędów

    private Server server;

    public ConnectedClientsStatus(Server server){
        this.server = server;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("------------------------");
            System.out.println("pool = " + server.pool);
            for (ClientHandler temp : server.clients)
                System.out.println("temp = " + temp);
        }
    }
}
