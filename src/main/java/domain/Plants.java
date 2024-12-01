package domain;

import java.util.ArrayList;

public class Plants extends Team {
    protected static final int SUN_AMOUNT_INCREASE = 25;
    public static final String NAME = "Plants";
    public static final int PLANTING_TIME = 20;

    public Plants(int resourceCounter, ArrayList<String> characters){
        super(resourceCounter,characters);
    }
       
    @Override
    public void increaseResourceAmount(){
        resourceCounter += SUN_AMOUNT_INCREASE;
    }

    public void increaseResourceAmount(int amount) {
        resourceCounter += amount;
    }

    @Override
    public String getTeamName(){
        return NAME;
    }   
}
