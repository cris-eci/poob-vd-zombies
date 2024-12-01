package domain;

import java.util.ArrayList;

public abstract class Team {
    
    protected int resourceCounter;
    protected static final int RESOURCE_AMOUNT_INCREASE = 0;
    protected ArrayList<Entity> characters;

    public abstract void increaseResourceAmount();

    public int getResourceCounterAmount(){
        return 0;
    }

    public abstract void selectCharacters();

    public void setResourceCounter(int amount){
    
    }


}
