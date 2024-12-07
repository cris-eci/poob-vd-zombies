package domain;

import java.util.ArrayList;

import javax.swing.Timer;

public abstract class Team {
    
    protected int resourceCounter;
    protected ArrayList<String> characters;
    public static final Timer RESOURCE_TIME_GENERATOR = new Timer(10000, null); // 10 segundos

    // public static final String SOL = "SOL";
    // public static final String BRAIN = "BRAIN";
    public Team(ArrayList<String> characters){
        this.characters = characters;
    }
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

    // Método para agregar recursos
    public void addResource(Resource resource){
        this.resourceCounter += resource.getValue();
    }

    public void deductResource(int amount) {
        if (amount > resourceCounter) {
            throw new IllegalArgumentException("No hay suficientes recursos.");
        }
        resourceCounter -= amount;
    }
}