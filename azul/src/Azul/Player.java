package Azul;

public class Player implements Comparable<Player>{
    private int score;
    private Board board = new Board();
    private int id;

    public Player(int i) {
        this.id=i+1;
        this.score = 0;
    }

    public int getScore() {
        return score;
    }

    public Player setScore(int add) {
        this.score += add;
        if (score < 0)
            this.score = 0;
        return this;
    }

    public Board getBoard() {
        return board;
    }

    @Override
    public int compareTo(Player o) {
        if(this.getScore()==o.getScore())
            return 0;
        if(this.getScore()>o.getScore())
            return -1;
        else
            return 1;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return "Wynik:"+ score+" graczNumer"+id+" ";
    }
}
