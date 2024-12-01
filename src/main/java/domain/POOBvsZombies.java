package domain;

import java.util.ArrayList;

import javax.swing.Timer;

public class POOBvsZombies {
    private String modality;
    private String winner;
    private float roundTime;
    private float matchTime;
    private ArrayList<Player> players;
    private ArrayList<Entity> entities;

    public POOBvsZombies(float matchTimeInSeconds, String namePlayerOne, ArrayList<String> plants, int sunAmount, String namePlayerTwo, int brainAmount, ArrayList<String> zombies) {
        this.players = new ArrayList<Player>();
        this.entities = new ArrayList<Entity>();

        this.players.add(new ZombiesStrategic(namePlayerOne, sunAmount, plants));
        this.players.add(new PlantsStrategic(namePlayerTwo, brainAmount, zombies));

        this.modality = "PlayerVsPlayer";
        this.winner = "";

        this.entities = new ArrayList<Entity>();
        this.matchTime = setMatchTime(matchTimeInSeconds);
        this.roundTime = this.matchTime / 2;
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

    /**
     * Converts seconds to minutes and sets the match time.
     *
     * @param seconds the number of seconds for the match time
     * @return the match time in minutes
     */
    private float setMatchTime(float seconds) {
        return seconds * 60;
    }

    public float getroundTime() {
        return roundTime;
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
