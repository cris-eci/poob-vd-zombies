package domain;

import java.util.ArrayList;

import javax.swing.Timer;

public abstract class Team {
    
    protected int resourceCounter;
    protected ArrayList<String> characters;
    public static final Timer RESOURCE_TIME_GENERATOR = new Timer(10000, null); // 10 seconds or 10000 milliseconds

    public Team(int resourceCounter, ArrayList<String> characters){
        this.resourceCounter = resourceCounter;
        this.characters = characters;
    }
    public abstract void increaseResourceAmount();

    public int getResourceCounterAmount(){
        return resourceCounter;
    }

    public ArrayList<String> getCharacters(){
        return characters;
    }

    public void setCharacters(ArrayList<String> characters){
        this.characters = characters;
    }

    public void setResourceCounter(int resourceCounter){
        this.resourceCounter = resourceCounter;
    }

    public abstract String getTeamName();
}
