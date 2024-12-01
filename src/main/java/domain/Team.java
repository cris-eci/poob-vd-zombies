package domain;

import java.util.ArrayList;

/**
 * Clase abstracta que representa a un equipo en el juego.
 */
public abstract class Team {
    protected int resourceCounter;
    protected static final int RESOURCE_AMOUNT_INCREASE = 25;
    protected ArrayList<Entity> characters;

    public Team(int initialResources) {
        this.resourceCounter = initialResources;
        this.characters = new ArrayList<>();
    }

    public abstract void increaseResourceAmount();

    public int getResourceCounterAmount(){
        return resourceCounter;
    }

    public abstract void selectCharacters();

    public void setResourceCounter(int amount){
        this.resourceCounter = amount;
    }

    public ArrayList<Entity> getCharacters() {
        return characters;
    }

    public abstract String getName();

    /**
     * Método para agregar una entidad al equipo.
     * Puede ser una planta o un zombie.
     * 
     * @param character La entidad a agregar.
     */
    public void addCharacter(Entity character){
        characters.add(character);
    }
}
