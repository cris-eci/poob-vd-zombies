package domain;

public abstract class Player {

    protected String name;
    protected int score;
    protected Team team;

    public Player(String name){
        this.name = name;
    }

    public String getName(){
        return name;
    }

    public int getScore(){
        return score;
    }

    public abstract void setScore();

    public Team getTeam(){
        return team;
    }



}
