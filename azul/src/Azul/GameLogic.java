package Azul;

import Server.Session;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.PriorityQueue;

public class GameLogic {

    // TODO: przenieść bonusy i takie tam do nowej klasy
    private static final int[] floorScores = {0, -1, -2, -4, -6, -8, -11, -14};
    private boolean flag=true;
    public PriorityQueue<Player> players;
    /**
     * podsumowuje runde, push na mozaike,punkty i sprawdzenie czy to juz koniec gry
     * @param session
     */
    public void summarizeRound(Session session)
    {
        session.setTurnCounter(session.getTurnCounter()+1);
        if(session.intersection.isEmpty())
        {
            for(Player player : session.players)
            {
                player.getBoard().pushWallToMosaic();
                score(player);
                player.getBoard().endOfRound();
                if(player.getBoard().getOne()) {
                    session.setNextRoundFirst(player.getId()-1);
                    session.setTurnCounter(session.getNextRoundFirst());
                    player.getBoard().setOne(false);
                }
                if(player.getBoard().rowOnMosaicCompleted()&&flag)
                {
                    summarizeGame(session);
                    flag=false;
                }

            }
            if(flag==false)
                compareScores(session);
            session.intersection.createNewWorkshop();
        }
    }
    public void compareScores(Session session)
    {
        this.players= new PriorityQueue<>();
        for(Player player : session.players)
        {
            players.add(player);
        }
    }
    public boolean getFlag()
    {return flag;
    }
    /**
     * podsumowuje gre - bonusy punktow
     * @param session
     */
    private void summarizeGame(Session session)
    {
        for(Player player : session.players)
        {
            rowBonus(player);
            columnBonus(player);
            colorBonus(player);

        }
    }
    /**
     * zlicza punkty zdobyte w ostatniej turze i dodaje je do wyniku gracza
     *
     * @param player
     */
    private void score(Player player) {
        String[] pushed = player.getBoard().getPushedToMosaic();
        Tile[]  tempColours= player.getBoard().getTempColours();
        int score = 0;
        int x, y; // zmienne pomocnicze do oblicznenia poprawnego wyniku
        for (int i = 0; i < 5; i++) {
            if (!pushed[i].equals("")) {
                player.getBoard().getTempMosaic().setTile(Integer.parseInt(pushed[i].charAt(0) + ""), Integer.parseInt(pushed[i].charAt(2) + ""),tempColours[i]);
                x = placedInColumn(player, Integer.parseInt(pushed[i].charAt(0) + ""), Integer.parseInt(pushed[i].charAt(2) + ""));
                y = placedInRow(player, Integer.parseInt(pushed[i].charAt(0) + ""), Integer.parseInt(pushed[i].charAt(2) + ""));
                if (x == 1 || y == 1)
                    score += (x + y - 1);
                else
                    score += (x + y);
            }
        }
        score += placedOnFloor(player);
        player.setScore(score);
    }

    /**
     * zwraca punkty za plytki w kolumnie
     *
     * @param row
     * @param column
     * @return
     */
    private int placedInColumn(Player player, int row, int column) {
        int temp = row;
        boolean flag = true;
        int score = 0;
        while (row < 5 && row > -1) {
            if (player.getBoard().getTempMosaic().getTilePlaced(row, column) != null) {
                score++;
                if (flag){
                    row++;
                    if(row==5)
                    {   row = temp - 1;
                        flag = false;}
                }
                else
                    row--;
            } else if (flag) {
                row = temp - 1;
                flag = false;
            } else break;
        }
        return score;
    }

    /**
     * zwraca punkty za płytki w rzedzie
     *
     * @param player
     * @param row
     * @param column
     * @return
     */
    private int placedInRow(Player player, int row, int column) {
        int temp = column;
        boolean flag = true;
        int score = 0;
        for (; column < 5 && column > -1; ) {
            if (player.getBoard().getTempMosaic().getTilePlaced(row, column) != null) {
                score++;
                if (flag){
                    column++;
                if(column==5)
                {   column = temp - 1;
                    flag = false;}
                }
                else
                column--;
            } else if (flag) {
                column = temp - 1;
                flag = false;
            } else break;
        }
        return score;
    }

    /**
     * zwraca liczbe punktow kary za plytki na podlodze
     *
     * @param player
     * @return
     */
    private int placedOnFloor(Player player) {
        return floorScores[player.getBoard().getNumberOfTilesOnTheFloor()];
    }

    /**
     * bonus 7 pkt za kolumny na koniec gry
     */
    private void columnBonus(Player player) {
        boolean bonusFlag;
        for (int column = 0; column < 5; column++) {
            bonusFlag = true;
            for (int row = 0; row < 5; row++)
                if (player.getBoard().getMosaic().getTilePlaced(row, column) == null) {
                    bonusFlag = false;
                    break;
                }
            if(bonusFlag)
            player.setScore(7);
        }

    }

    /**
     * bonus 2 pkt za wiersze na koniec gry
     */
    private void rowBonus(Player player) {
        boolean bonusFlag;
        for (int row = 0; row < 5; row++) {
            bonusFlag = true;
            for (int column = 0; column < 5; column++)
                if (player.getBoard().getMosaic().getTilePlaced(row, column) == null) {
                    bonusFlag = false;
                    break;
                }
            if(bonusFlag)
            player.setScore(2);
        }
    }

    /**
     * bonus 10 pkt za skompletowane kolory na koniec gry
     */
    private void colorBonus(Player player) {
        boolean bonusFlags[] = new boolean[]{true, true, true, true, true};
        for (int row = 0; row < 5; row++) {
            for (int column = 0; column < 5; column++)
                if (player.getBoard().getMosaic().getTilePlaced(row, column) == null) {
                    bonusFlags[(column - row + 5) % 5] = false;
                }
        }
        for (boolean flag : bonusFlags)
            if (flag)
                player.setScore(10);
    }


}
