package GUI;

import Azul.Tile;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * Interfejs graficzny pojedynczego warsztatu
 */
public class SingleWorkshopGUI extends JLabel {

    Tile[] tiles = new Tile[4];
    private DisplayWorkshopGUI displayWorkshopGUI = new DisplayWorkshopGUI(this);
    private InputWorkshopGUI inputWorkshopGUI = new InputWorkshopGUI(this);
    private WholeTableGUI wholeTableGUI;
    int workshopNumber;

    /**
     * Konstruktor
     * @param workshopNumber - numer warsztatu
     * @param wholeTableGUI - obiek WholeTableGUI
     */
    public SingleWorkshopGUI(int workshopNumber, WholeTableGUI wholeTableGUI) {
        super(new ImageIcon("src/img/workshop.png"));
        setLayout(null);
        this.workshopNumber = workshopNumber;
        this.wholeTableGUI = wholeTableGUI;

        displayWorkshopGUI.setBounds(33, 33, 120, 120);
        inputWorkshopGUI.setBounds(33, 33, 120, 120);

        add(inputWorkshopGUI);
        add(displayWorkshopGUI);

    }

    /**
     * Akcja związana z naciśnięciem przycisku z numerem buttonNumber
     * @param buttonNumber - numer przycisku
     */
    void buttonPressed(int buttonNumber){
         wholeTableGUI.setPick(tiles[buttonNumber], workshopNumber);
    }

    /**
     * Aktualizuje interfejs
     * @param newTiles - stan warsztatu podany jako Tile[4]
     */
    public void updateSingleWorkshop(Tile[] newTiles){
        System.out.print("Workshop nr " + workshopNumber + " dostaje:");
        for(int i=0; i<4; i++){
            System.out.print(newTiles[i] + " ");
            tiles[i] = newTiles[i];
        }
        System.out.println();
        displayWorkshopGUI.updateDisplayWorkshop();
    }

    Tile[] getTiles(){
        return tiles;
    }

    /**
     * Podświetla płytki danego koloru
     * @param color - kolor płytek do podświetlenia
     */
    public void displaySelection(Tile color) {
        for(int i=0; i<4; i++) inputWorkshopGUI.buttons[i].displayAsSelected(color != null && tiles[i] == color);
    }
}

/**
 * Odpowiada za część graficzną pojedynczego warsztatu
 */
class DisplayWorkshopGUI extends Container {
    SingleWorkshopGUI singleWorkshopGUI;
    GridBagConstraints gbc = new GridBagConstraints();
    TileGUI[] tiles = new TileGUI[4];

    public DisplayWorkshopGUI(SingleWorkshopGUI singleWorkshopGUI) {
        setLayout(new GridBagLayout());
        this.singleWorkshopGUI = singleWorkshopGUI;

        gbc.weightx = 1;
        gbc.weighty = 1;

        for (int i = 0; i < 4; i++) {
            tiles[i] = new TileGUI(true);
        }

        gbc.gridx = 0;
        gbc.gridy = 0;
        add(tiles[0], gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        add(tiles[1], gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        add(tiles[2], gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        add(tiles[3], gbc);

    }

    void updateDisplayWorkshop(){
        Tile[] tempTiles = singleWorkshopGUI.getTiles();
        for(int i=0; i<4; i++){
            if(tempTiles[i]==null) tiles[i].displayThis(false);
            else{
                tiles[i].changeImage(tempTiles[i]);
                tiles[i].displayThis(true);
            }
        }
    }
}

/**
 * Odpowiada za input od użytkownika
 */
class InputWorkshopGUI extends Container {
    GridBagConstraints gbc = new GridBagConstraints();
    WorkshopButton[] buttons = new WorkshopButton[4];
    SingleWorkshopGUI singleWorkshopGUI;

    public InputWorkshopGUI(SingleWorkshopGUI singleWorkshopGUI) {
        this.singleWorkshopGUI = singleWorkshopGUI;
        setLayout(new GridBagLayout());

        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.insets = new Insets(6,6,6,6);
        gbc.fill = GridBagConstraints.BOTH;

        for (int i = 0; i < 4; i++) {
            buttons[i] = new WorkshopButton(i,this);
        }

        gbc.gridx = 0;
        gbc.gridy = 0;
        add(buttons[0], gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        add(buttons[1], gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        add(buttons[2], gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        add(buttons[3], gbc);
    }

    void buttonPressed(int buttonNumber){
        singleWorkshopGUI.buttonPressed(buttonNumber);
    }

    /**
     * Przycisk odpowiadający za jedno miejsce na warsztacie
     */
    class WorkshopButton extends JButton implements MouseListener {

        InputWorkshopGUI inputWorkshopGUI;
        int numberOfButton;

        public WorkshopButton(int numberOfButton, InputWorkshopGUI inputWorkshopGUI) {
            this.inputWorkshopGUI = inputWorkshopGUI;
            this.numberOfButton = numberOfButton;
            addMouseListener(this);
            setOpaque(false);
            setContentAreaFilled(false);
            setBorder(BorderFactory.createMatteBorder(1,1,1,1,Color.CYAN));
            setBorderPainted(false);
        }

        public void displayAsSelected(boolean selected){
           setBorderPainted(selected);
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            inputWorkshopGUI.buttonPressed(numberOfButton);
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
