package domain;

import java.util.ArrayList;

public class Zombies extends Team {
    public static final String NAME = "Zombies";
    protected static final int BRAIN_AMOUNT_INCREASE = 50; // Adjusted resource increase for Zombies
    private static final ArrayList<String> ORIGINAL_ZOMBIES = new ArrayList<>();

    static {
        ORIGINAL_ZOMBIES.add("Basic");
        ORIGINAL_ZOMBIES.add("Brainstein");
        ORIGINAL_ZOMBIES.add("BucketHead");
        ORIGINAL_ZOMBIES.add("Conehead");
        ORIGINAL_ZOMBIES.add("ECIZombie");
    }

    public Zombies(int resourceCounter, ArrayList<String> characters){
        super(resourceCounter, characters);
    }

    public Zombies(){
        super(0, ORIGINAL_ZOMBIES);
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
