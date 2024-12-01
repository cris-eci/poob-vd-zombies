package domain;

import java.util.ArrayList;

/**
 * The ZombiesStrategic class represents a human player with a strategic approach
 * in a game involving zombies. This class extends the HumanPlayer class and 
 * initializes the player's team as a Zombies team.
 * 
 * <p>With this constructor, we save time setting up the teams, as by default, 
 * a human zombie player has a zombie team.</p>
 * 
 * @param name the name of the player
 * @param brains the resources for the zombies in the zombies team
 * @param zombies the list of zombies in the team
 */
public class ZombiesStrategic extends HumanPlayer {
    

    public ZombiesStrategic(String name, int brains, ArrayList<String> zombies) {
        // brains are the resources for the zombies in zombies team
        super(name);
        Team zombiesTeam = new Zombies(brains, zombies);
        this.team = zombiesTeam;
    }


    @Override
    public void playTurn() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'playTurn'");
    }
    @Override
    public String getTeamName() {
        return team.getTeamName();
    }    
}
