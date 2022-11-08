package GUI;

import Azul.Tile;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * Interfejs graficzny stołu (miejsca gdzie odkładane są płytki)
 */
public class TableGUI extends Container {

    WholeTableGUI wholeTableGUI;
    Tile[] tiles = new Tile[27];
    DisplayTableGUI displayTableGUI = new DisplayTableGUI();
    InputTableGUI inputTableGUI = new InputTableGUI(this);
    static final int TABLE = -1;

    public TableGUI(WholeTableGUI wholeTableGUI){
        setBounds(242,290,320,270);
        this.wholeTableGUI = wholeTableGUI;
        add(inputTableGUI);
        add(displayTableGUI);
    }

    public void updateTableGUI(Tile[] newTiles, boolean firstIndicator){
        for(int i=0; i<27; i++){
            tiles[i] = newTiles[i];
        }
        displayTableGUI.updateDisplayTableGUI(tiles, firstIndicator);
        repaint();
        revalidate();
        doLayout();
    }

    void buttonClicked(int numberOfButton){
        wholeTableGUI.setPick(tiles[numberOfButton], TABLE);
        System.out.println("wysłano setPick   stół(-1)  kolor:" + tiles[numberOfButton]);
    }

    public void displaySelection(Tile color) {
        for(int i=0; i<27; i++) inputTableGUI.tableButtons[i].displayAsSelected(color != null && tiles[i] == color);
    }
}

/**
 * Klasa odpowiedzialna za wyświetlanie stołu
 */
class DisplayTableGUI extends Container{
    GridBagConstraints gbc = new GridBagConstraints();
    TileGUI[] tiles = new TileGUI[28];

    public DisplayTableGUI() {
        setBounds(0,0,320,270);
        setLayout(new GridBagLayout());

        gbc.weightx = 1;
        gbc.weighty = 1;

        for (int i = 0; i < 28; i++) {
            tiles[i] = new TileGUI(true);
            tiles[i].setOpaque(false);
            gbc.gridx = i % 6;
            gbc.gridy = i / 6;
            add(tiles[i], gbc);
        }
    }

    public void updateDisplayTableGUI(Tile[] newTiles, boolean firstIndicator){
        for(int i=0; i<27; i++){
            if(newTiles[i]==null){
                tiles[i].displayThis(false);
            }else{
                tiles[i].changeImage(newTiles[i]);
                tiles[i].displayThis(true);
            }
        }
        tiles[27].changeImage(Tile.ONE);
        tiles[27].displayThis(firstIndicator);
        repaint();
        revalidate();
        doLayout();
    }
}

/**
 * Klasa odpowiedzialna za pobieraniu inputu od użytkownika
 */
class InputTableGUI extends Container{
    GridBagConstraints gbc = new GridBagConstraints();
    TableButton[] tableButtons = new TableButton[30];
    TableGUI tableGUI;

    public InputTableGUI(TableGUI tableGUI){
        setBounds(0,0,320,270);
        this.tableGUI = tableGUI;
        setLayout(new GridBagLayout());

        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(3,3,3,3);

        for (int i = 0; i < 27; i++) {
            tableButtons[i] = new TableButton(i);
            gbc.gridx = i % 6;
            gbc.gridy = i / 6;
            add(tableButtons[i], gbc);
        }
    }

    class TableButton extends JButton implements MouseListener {

        int numberOfButton;

        public TableButton(int number){
            this.numberOfButton = number;
            setOpaque(false);
            setContentAreaFilled(false);
            setBorder(BorderFactory.createMatteBorder(1,1,1,1,Color.CYAN));
            setBorderPainted(false);
            addMouseListener(this);
        }

        public void displayAsSelected(boolean selected){
            setBorderPainted(selected);
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            tableGUI.buttonClicked(numberOfButton);
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

