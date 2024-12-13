package domain;

import java.util.ArrayList;

public class Zombies extends Team {
    public static final String NAME = "Zombies";
    protected static final int BRAIN_AMOUNT_INCREASE = 50; // Adjusted resource increase for Zombies
    

    
    // public Zombies(int resourceCounter, ArrayList<String> characters){
    //     super(resourceCounter, characters);
    // }

    public Zombies(){
        super(MachinePlayer.ORIGINAL_ZOMBIES);
    }

    public Zombies(ArrayList<String> characters,int brains){
        super(brains, characters);
    }

    @Override
    public void increaseResourceAmount(){
        // Implement the logic to increase resource amount for Zombies
        resourceCounter += BRAIN_AMOUNT_INCREASE;
    }

    @Override
    public String getTeamName(){
        return NAME;
    }           
}
