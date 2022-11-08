package GUI;

import Azul.Tile;
import Azul.TileQuantity;
import Azul.Workshop;

import javax.swing.*;
import java.awt.*;

/**
 * Interfejs łączący stół i warsztaty
 */
public class WholeTableGUI extends Container {

    TableGUI tableGUI = new TableGUI(this);
    SingleWorkshopGUI[] workshops;
    TileQuantity pick = new TileQuantity(null,-99); // UWAGA! to nie jest normalne zastosowanie TileQuantity - argument quantity to wybrany workshop

    public static final TileQuantity NONE_SELECTION = new TileQuantity(null, -99);
    private int numberOfPlayers;

    public WholeTableGUI(int numberOfPlayers){
        this.numberOfPlayers = numberOfPlayers;
        setBounds(0,0,850,850);
        workshops = new SingleWorkshopGUI[1+2*numberOfPlayers];
        add(tableGUI);

        for(int i=0; i<workshops.length; i++)
            workshops[i] = new SingleWorkshopGUI(i,this);

        switch (numberOfPlayers){
            case 2:
                workshops[0].setBounds(308,28,186,186);
                workshops[1].setBounds(604,242,186,186);
                workshops[2].setBounds(491,587,186,186);
                workshops[3].setBounds(126,587,186,186);
                workshops[4].setBounds(13,242,186,186);
                break;
            case 3:
                workshops[0].setBounds(308,23,186,186);
                workshops[1].setBounds(541,136,186,186);
                workshops[2].setBounds(600,388,186,186);
                workshops[3].setBounds(438,592,186,186);
                workshops[4].setBounds(178,592,186,186);
                workshops[5].setBounds(16,388,186,186);
                workshops[6].setBounds(74,136,186,186);
                break;
            case 4:
                workshops[0].setBounds(308,12,186,186);
                workshops[1].setBounds(506,83,186,186);
                workshops[2].setBounds(609,264,186,186);
                workshops[3].setBounds(573,470,186,186);
                workshops[4].setBounds(415,606,186,186);
                workshops[5].setBounds(202,606,186,186);
                workshops[6].setBounds(44,470,186,186);
                workshops[7].setBounds(6,264,186,186);
                workshops[8].setBounds(112,83,186,186);
                break;
        }


        for(SingleWorkshopGUI temp:workshops)
            add(temp);
    }

    public void updateWholeTableGUI(Workshop workshop, Tile[] newTable, boolean firstIndicator){
        int numberOfWorkshops = 1 + numberOfPlayers*2;
        for(int i=0; i<numberOfWorkshops; i++){
            workshops[i].updateSingleWorkshop(workshop.workshopGet(i));
        }
        tableGUI.updateTableGUI(newTable, firstIndicator);
        setPick(null, -99);
        repaint();
        revalidate();
        doLayout();
    }

    void displaySelection(){
        for(int i=0; i<workshops.length; i++) workshops[i].displaySelection(null);
        tableGUI.displaySelection(null);
        if(pick != NONE_SELECTION){
            if(pick.getQuantity() == -1) // stol
                tableGUI.displaySelection(pick.getColour());
            else workshops[pick.getQuantity()].displaySelection(pick.getColour());
        }
        repaint();
        revalidate();
        doLayout();
    }


    // UWAGA - quantity to numer warsztatu
    public TileQuantity getPick(){
        return pick;
    }

    /**
     * Ustawienie wyboru gracza
     * @param color - kolor płytki
     * @param workshopNumber - numer warsztatu
     */
    public void setPick(Tile color, int workshopNumber){
        if(color == null)
            pick = NONE_SELECTION;
        else
            pick = new TileQuantity(color, workshopNumber);
        System.out.println("Ustawiono wybor! warsztat:" + pick.getQuantity() + "   kolor:"+pick.getColour());
        displaySelection();
    }
}
