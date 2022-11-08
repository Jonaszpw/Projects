package GUI;

import Azul.Tile;
import Client.Client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * Interfejs graficzny planszy gracza
 */
public class BoardGUI extends JLabel {

    final static int VERTICAL_TWEAK = 12;   // jak komponenty leżą w złym miejscu na planszy to tym można regulować wszystkie w pionie

    private Client client;

    private InputWallGUI inputWallGUI = new InputWallGUI(this);
    private DisplayWallGUI displayWallGUI = new DisplayWallGUI();
    private MosaicGUI mosaicGUI = new MosaicGUI();
    private DisplayFloorGUI displayFloorGUI = new DisplayFloorGUI();
    private RowButton inputFloorGUI = new RowButton(-1, this);
    private SendButton sendButton = new SendButton(this);
    private PointsIndicator pointsIndicator = new PointsIndicator();


    public final static int NONE_SELECTION = -999;
    private int selection = NONE_SELECTION;

    // ścieżki do odpowiednich grafik
    final static String RED = "src/img/TileRed.png";
    final static String BLUE = "src/img/TileBlue.png";
    final static String YELLOW = "src/img/TileYellow.png";
    final static String BLACK = "src/img/TileBlack.png";
    final static String WHITE = "src/img/TileWhite.png";
    final static String ONE = "src/img/TileOne.png";
    final static String PLACE_HOLDER = "src/img/tilePlaceholder.png";

    /**
     * Konstruktor bez przypisywania klienta
     */
    public BoardGUI(){
        super(new ImageIcon("src/img/boardBackgroundsmallGray2.png"));
        //setBounds(0,0,600,600);
        setLayout(null);
        add(displayWallGUI);
        add(mosaicGUI);
        add(displayFloorGUI);
        add(pointsIndicator);
    }

    /**
     * Konstruktor przypisujący klienta do planszy
     * @param client
     */
    public BoardGUI(Client client){
        super(new ImageIcon("src/img/boardBackgroundsmallGray2.png"));
        setLayout(null);
        add(inputWallGUI);
        add(displayWallGUI);
        add(mosaicGUI);
        add(inputFloorGUI);
        add(displayFloorGUI);
        add(sendButton);
        add(pointsIndicator);

        this.client = client;
    }

    /**
     * Ustawia mozaikę na planszy na podstawie danej macierzy boolean'ów (true - dodaj płytkę, false - usuń płytkę)
     * @param mosaicStatus - macierz 5x5 wartości boolowskich
     */
    public void setMosaic(boolean[][] mosaicStatus){
        mosaicGUI.setMosaic(mosaicStatus);
    }

    /**
     * Ustawia płytki na ściane na podstawie danej macierzy
     * @param wallStatus - macierz 5x5  (wartość Tile==null oznacza brak płytki)
     */
    public void setWall(Tile[][] wallStatus){
        displayWallGUI.setWall(wallStatus);
    }

    /**
     * Ustawia płytki na podłodze na podstwaie danej tablicy
     * @param floorStatus - tablica Tile[7]
     */
    public void setFloor(Tile[] floorStatus) {displayFloorGUI.setFloor(floorStatus);}

    public void setPoints(int points){
        pointsIndicator.updateIndicator(points);
    }

    void setSelection(int newSelection){
         selection = newSelection;
         for(int i=0; i<5; i++){
             inputWallGUI.getRowButtons()[i].displayAsSelected(selection == i);
         }
         inputFloorGUI.displayAsSelected(selection == -1);
         repaint();
         revalidate();
         doLayout();
    }

    public int getSelection(){
        return selection;
    }

    /**
     * Akcja związana z naciśnięciem przycisku "send" (komponent sendButton) na planszy
     */
    void sendButtonClicked(){
        client.sendAction();
    }

}

/**
 * Przycisk "send" na planszy gracza
 */
class SendButton extends JButton implements ActionListener{
    BoardGUI boardGUI;
    public SendButton(BoardGUI boardGUI){
        super("Send!");
        this.boardGUI = boardGUI;
        setBounds(432,504 + BoardGUI.VERTICAL_TWEAK,100,50);
        addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        boardGUI.sendButtonClicked();
    }
}

/**
 * Wyświetlanie płytek na podłodze
 */
class DisplayFloorGUI extends Container{

    private GridBagConstraints gbc = new GridBagConstraints();
    TileGUI[] tiles = new TileGUI[7];

    public DisplayFloorGUI(){
        setBounds(24,503 + BoardGUI.VERTICAL_TWEAK,400,50);
        setLayout(new GridBagLayout());

        gbc.anchor = GridBagConstraints.CENTER;
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.gridwidth = 1;

        for(int i=0; i<7; i++){
            tiles[i] = new TileGUI(true);
            gbc.gridx = i;
            add(tiles[i],gbc);
        }
    }

    /**
     * Aktualizuje wyświetlane płytki
     * @param floorStatus - tablica płytek do wyświetlenia
     */
    void setFloor(Tile[] floorStatus) {
        for(int i=0; i<7; i++){
            if(floorStatus[i]==null)
                tiles[i].displayThis(false);
            else {
                tiles[i].changeImage(floorStatus[i]);
                tiles[i].displayThis(true);
            }
        }
        repaint();
        revalidate();
        doLayout();
    }
}

/**
 * Element interfejsu odpowiedzialny za rozmieszczenie przycisków na rzędch ściany
 */
class InputWallGUI extends Container{

    private GridBagConstraints gbc = new GridBagConstraints();
    private RowButton[] rowButtons = new RowButton[5];

    public InputWallGUI(BoardGUI boardGUI){
        setBounds(28,202 + BoardGUI.VERTICAL_TWEAK,260,260);
        setLayout(new GridBagLayout());

        gbc.anchor = GridBagConstraints.LINE_END;
        gbc.weightx = 1;
        gbc.weighty = 1;

        for(int row=0; row<5; row++){
            rowButtons[row] = new RowButton(row,boardGUI);
            gbc.gridx = 4-row;
            gbc.gridy = 0+row;
            gbc.gridwidth = 1+row;
            add(rowButtons[row],gbc);
        }
    }

    public RowButton[] getRowButtons(){
        return rowButtons;
    }

}

/**
 * Element interfesju odpowiedzialny za wyświetlanie płytek na ścianie
 */
class DisplayWallGUI extends Container{

    private GridBagConstraints gbc = new GridBagConstraints();
    TileGUI[][] tiles = new TileGUI[5][5];

    public DisplayWallGUI(){
        setBounds(28,202 + BoardGUI.VERTICAL_TWEAK,260,260);
        setLayout(new GridBagLayout());

        gbc.anchor = GridBagConstraints.CENTER;
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.gridwidth = 1;

        for(int i=0; i<5; i++){
            gbc.gridy = i;
            for(int j=0; j<5; j++){
                tiles[i][j] = new TileGUI(j>=4-i);
                gbc.gridx = j;
                add(tiles[i][j], gbc);
            }
        }
    }

    /**
     * Ustawia płytki na ściane na podstawie danej macierzy
     * @param tileType - macierz 5x5  (wartość Tile==null oznacza brak płytki)
     */
    public void setWall(Tile[][] tileType){
        for(int i=0; i<5; i++){
            for (int j=4-i; j<5; j++){
                if(tileType[i][j]!=null){
                    tiles[i][j].changeImage(tileType[i][j]);
                    tiles[i][j].displayThis(true);
                }else tiles[i][j].displayThis(false);
            }
        }
        repaint();
        revalidate();
        doLayout();
    }
}

/**
 * Element interfejsu odpowiedzialny za wyświetlanie mozaiki na planszy
 */
class MosaicGUI extends Container{

    final static String[][] tileColor = {
            {BoardGUI.BLUE, BoardGUI.YELLOW, BoardGUI.RED, BoardGUI.BLACK, BoardGUI.WHITE},
            {BoardGUI.WHITE, BoardGUI.BLUE, BoardGUI.YELLOW, BoardGUI.RED, BoardGUI.BLACK},
            {BoardGUI.BLACK, BoardGUI.WHITE, BoardGUI.BLUE, BoardGUI.YELLOW, BoardGUI.RED},
            {BoardGUI.RED, BoardGUI.BLACK, BoardGUI.WHITE, BoardGUI.BLUE, BoardGUI.YELLOW},
            {BoardGUI.YELLOW, BoardGUI.RED, BoardGUI.BLACK, BoardGUI.WHITE, BoardGUI.BLUE}
    };

    private GridBagConstraints gbc = new GridBagConstraints();

    TileGUI[][] tiles = new TileGUI[5][5];

    public MosaicGUI(){
        setBounds(313,202 + BoardGUI.VERTICAL_TWEAK,260,260);
        setLayout(new GridBagLayout());

        gbc.anchor = GridBagConstraints.CENTER;
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.gridwidth = 1;


        for(int i=0; i<5; i++){
            gbc.gridy = i;
            for(int j=0; j<5; j++){
                tiles[i][j] = new TileGUI(i,j);
                gbc.gridx = j;
                add(tiles[i][j], gbc);
            }
        }
    }

    /**
     * Ustawia mozaikę na planszy na podstawie danej macierzy boolean'ów (true - dodaj płytkę, false - usuń płytkę)
     * @param status - macierz 5x5 wartości boolowskich
     */
    public void setMosaic(boolean[][] status){
        for(int i=0; i<5; i++){
            for (int j=0; j<5; j++)
                tiles[i][j].displayThis(status[i][j]);
        }
    }

}

/**
 * Przycisk pozwalający na "klikalność" rzędów na ścianie
 */
class RowButton extends JButton implements MouseListener {

    private BoardGUI boardGUI;
    private int rowNumber;  // 1 - 5

    /**
     * konstruktor
     * @param row - który rząd (0-4)
     */
    public RowButton(int row, BoardGUI boardGUI){
        ////// gdy robi za przycisk na podłodze ///////
        if(row == -1){
            rowNumber = -1;
            setBounds(24,501 + BoardGUI.VERTICAL_TWEAK,400,60);
        }

        else{
            rowNumber = row+1;
            setPreferredSize(new Dimension((row+1)*52, 52));
        }


        this.boardGUI = boardGUI;
        addMouseListener(this);
        setOpaque(false);
        setContentAreaFilled(false);
        setBorder(BorderFactory.createMatteBorder(1,1,1,3,Color.CYAN));
        if(rowNumber == -1) setBorder(BorderFactory.createMatteBorder(0,0,3,0,Color.CYAN));
        setBorderPainted(false);
    }

    public void displayAsSelected(boolean selected){
        setBorderPainted(selected);
    }

    @Override
    public void mouseClicked(MouseEvent mouseEvent) {
        if(rowNumber == -1) boardGUI.setSelection(rowNumber);
        else boardGUI.setSelection(rowNumber-1);
    }

    @Override
    public void mousePressed(MouseEvent mouseEvent) {}

    @Override
    public void mouseReleased(MouseEvent mouseEvent) {}

    @Override
    public void mouseEntered(MouseEvent mouseEvent) {
        setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    @Override
    public void mouseExited(MouseEvent mouseEvent) {
        setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
    }
}

/**
 * Element interfejsu odpowiedzialny za wyświetlanie pojedynczej płytki
 */
class TileGUI extends JPanel {

    // UWAGA: ta klasa dziedziczy po JPanel, a za wyświetlanie płytki odpowiedzialny jest JLabel tile dodany na ten panel
    // jest tak, ponieważ przy GridBagLayout, ustawienie płytki na niewidzialną powoduje "zawalenie się" całego layoutu
    // dlatego zmianę widoczności należy ustawiać tylko na JLabel tile

    public JLabel tile;
    private boolean containsTile;

    /**
     * konstruktor używany przy zapełnianiu ściany płytkami
     * @param containsTile - czy dane pole będzie zawierać płytkę (ściana jest kwadratem, a tylko miesjca na przekątnej i pod nią będą zawierać płytki)
     */
    public TileGUI(boolean containsTile){
        this.containsTile = containsTile;
        setPreferredSize(new Dimension(50,50));
        setLayout(new BorderLayout());
        setOpaque(false);
        if(containsTile){
            tile = new JLabel(new ImageIcon(BoardGUI.PLACE_HOLDER));
            tile.setVisible(false);
            add(tile);
        }
    }

    /**
     * kostruktor używany przy zapełnianiu mozaiki (płytka od razu ma odpowiedni kolor)
     * @param row - rząd moziaki
     * @param column - kolumna mozaiki
     */
    public TileGUI(int row, int column){
        containsTile = true;
        setPreferredSize(new Dimension(50,50));
        setLayout(new BorderLayout());
        tile = new JLabel(new ImageIcon(MosaicGUI.tileColor[row][column]));
        tile.setVisible(false);
        setOpaque(false);
        add(tile);
    }

    /**
     * zmiana koloru danej płytki
     * @param tileColor - nowy kolor
     */
    public void changeImage(Tile tileColor){
        remove(tile);
        if (tileColor==null) tile = new JLabel(new ImageIcon(BoardGUI.PLACE_HOLDER));
        else {
            switch (tileColor) {
                case BLUE:
                    tile = new JLabel(new ImageIcon(BoardGUI.BLUE));
                    break;
                case BLACK:
                    tile = new JLabel(new ImageIcon(BoardGUI.BLACK));
                    break;
                case RED:
                    tile = new JLabel(new ImageIcon(BoardGUI.RED));
                    break;
                case YELLOW:
                    tile = new JLabel(new ImageIcon(BoardGUI.YELLOW));
                    break;
                case WHITE:
                    tile = new JLabel(new ImageIcon(BoardGUI.WHITE));
                    break;
                case ONE:
                    tile = new JLabel(new ImageIcon(BoardGUI.ONE));
                    break;
                default:
                    tile = new JLabel(new ImageIcon(BoardGUI.PLACE_HOLDER));
                    break;
            }
        }
        add(tile);
    }

    /**
     * włączenie/wyłącznie wyświetlania płytki
     * @param b
     */
    public void displayThis(boolean b){
        if(b) tile.setVisible(true);
        else tile.setVisible(false);
    }
}

/**
 * Wyświetla liczbę punktów poprzez ustawienie czarnego klocka na odpowiednim miejscu planszy
 */
class PointsIndicator extends JLabel{
    public PointsIndicator() {
        super(new ImageIcon("src/img/pointsSquare.png"));
        setBounds(34,41,24,24);
        setOpaque(false);
    }

    /**
     * Aktualizuje wyświetlane punkty
     * @param points - liczba punktów
     */
    void updateIndicator(int points){
        int x, y;

        x = 33 + 27*((points-1)%20);
        if(points==0){
           x = 33;
           y=-2;
        }else if(points<21){
            y=29;
        }else if(points<41){
            y=61;
        }else if(points<61){
            y=93;
        }else if(points<81){
            y=126;
        }else{
            y=161;
        }
        y += BoardGUI.VERTICAL_TWEAK;
        setBounds(x,y,24,24);
    }
}
