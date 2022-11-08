package GUI;

import javax.swing.*;
import java.awt.*;

/**
 * Wyświetlanie informacji o aktywnym graczu
 * ( czerwona lub zielona kropka przy przyciskach wyboru plasnzy)
 */
public class ActivePlayerStatus extends Container {

    JLabel[] green;
    JLabel[] red;
    int numberOfPlayers;

    /**
     * Konstruktor
     * @param numberOfPlayers - liczba graczy
     */
    public ActivePlayerStatus(int numberOfPlayers){
        setBounds(880,100,600,50);
        setLayout(null);

        this.numberOfPlayers = numberOfPlayers;
        green = new JLabel[numberOfPlayers];
        red = new JLabel[numberOfPlayers];

        for(int i=0;i<numberOfPlayers;i++){
            green[i] = new JLabel(new ImageIcon("src/img/greenStatus.png"));
            red[i] = new JLabel(new ImageIcon("src/img/redStatus.png"));
            green[i].setBounds(4+(i*163),4,14,14);
            red[i].setBounds(4+(i*163),4,14,14);
            green[i].setVisible(false);
            add(green[i]);
            add(red[i]);
        }
    }

    /**
     * Akutalizacja "kropki"
     * @param activePlayer - numer aktywnego gracza
     */
    public void showActivePlayerStatus(int activePlayer){
        for(int i=0; i<numberOfPlayers; i++){
            red[i].setVisible(true);
            green[i].setVisible(false);
        }
        red[activePlayer].setVisible(false);
        green[activePlayer].setVisible(true);
        repaint();
        revalidate();
        doLayout();
    }
}