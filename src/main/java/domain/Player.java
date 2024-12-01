package domain;

public abstract class Player {

    protected String name;
    protected int score;
    protected Team team;
    protected Resource resource;

    public Player(Team team,String name){
        this.team = team;
        this.name = name;

    }

    public String getName(){
        return name;
    }

    public int getScore(){
        return score;
    }


    public String getTeamName(){
        return "";
    }

    public abstract void setScore();

    public abstract void addCharacter();


}
