package domain;

import java.util.ArrayList;

import javax.swing.Timer;

public class POOBvsZombies {
    private String modality;
    private String winner;
    private Timer roundTimer;
    private ArrayList<Player> players;
    private ArrayList<Entity> entities;

    public POOBvsZombies(int matchTimer, String namePlayerOne, ArrayList<String> plants, int sunAmount, String namePlayerTwo, int brainAmount, ArrayList<String> zombies) {
        this.players = new ArrayList<Player>();
        this.entities = new ArrayList<Entity>();

        this.players.add(new ZombiesStrategic(namePlayerOne, sunAmount, plants));
        this.players.add(new PlantsStrategic(namePlayerTwo, brainAmount, zombies));

        this.modality = "PlayerVsPlayer";
        this.winner = "";

        this.entities = new ArrayList<Entity>();
        this.roundTimer = setRoundTime(matchTimer);
    }

    public int calculateProgress(){
        return 0;
    }

    /**
     * Sets a timer for the specified number of minutes.
     *
     * @param minutes the number of minutes for the timer
     * @return a Timer object set to the specified duration in milliseconds
     */
    private Timer setRoundTime(int minutes) {
        int milliseconds = minutes * 60 * 1000;
        return new Timer(milliseconds, null);
    }
    
    public String getModality() {
        return modality;
    }

    public Player getPlayerOne(){
        return players.get(0);
    }

    public Player getPlayerTwo(){
        return players.get(1);
    }
}
