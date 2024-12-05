package domain;

import java.util.ArrayList;

import javax.swing.Timer;

public class POOBvsZombies {
    private String modality;
    private String winner;
    private float roundTime;
    private float matchTime;
    private ArrayList<Player> players;
    private ArrayList<ArrayList<Entity>> entities;

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

    public POOBvsZombies(float matchTimeInSeconds,int hordersNumber, String namePlayerOne, ArrayList<String> plants) {
        this.players = new ArrayList<Player>();
        this.entities = new ArrayList<Entity>();

        this.players.add(new PlantsStrategic(namePlayerOne, 50, plants));
        this.players.add(new ZombiesOriginal(hordersNumber, matchTimeInSeconds));

        this.modality = "PlayerVsMachine";
        this.winner = "";

        this.entities = new ArrayList<Entity>();
        this.matchTime = setMatchTime(matchTimeInSeconds);
        this.roundTime = this.matchTime / 2;
    }
    
    public POOBvsZombies(float matchTimeInSeconds, int hordersNumber) {
        this.players = new ArrayList<Player>();
        this.entities = new ArrayList<Entity>();

        this.players.add(new PlantsIntelligent());
        this.players.add(new ZombiesOriginal(hordersNumber, matchTimeInSeconds, MachinePlayer.ORIGINAL_ZOMBIES));

        this.modality = "MachineVsMachine";
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

    public float getRoundTime() {
        return roundTime;
    }
    
    public int getHordersNumber() {
        if (players.size() > 1 && players.get(1) instanceof ZombiesOriginal) {
            ZombiesOriginal zombiesOriginal = (ZombiesOriginal) players.get(1);
            return zombiesOriginal.getHordersNumber();
        }
        return 0; // Return 0 if not applicable
    }

    public String getModality() {
        return modality;
    }

    public float getMatchTime() {
        return matchTime;
    }

    public Player getPlayerOne(){
        return players.get(0);
    }

    public Player getPlayerTwo(){
        return players.get(1);
    }

    
    /**
     * Sets up the entities by initializing a 10x5 matrix filled with null values.
     * This matrix will be used later for storing entities.
     */
    public void setUpEntities() {        
        for (int i = 0; i < 10; i++) {
            ArrayList<Entity> row = new ArrayList<Entity>();
            for (int j = 0; j < 5; j++) {                
                row.add(null); // 
            }
            entities.add(row);
        }
    }

    //Initial code for the method addEntity, it should be modified to fit the requirements of the project
    public void addEntity(int lane, int yPos, Entity entity) {
        if (lane >= 0 && lane < entities.size() && yPos >= 0 && yPos < entities.get(lane).size()) {
            entities.get(lane).set(yPos, entity);
        } else {
            throw new IndexOutOfBoundsException("Invalid lane or yPos");
        }
    }

    //Initial code for the method addEntity, it should be modified to fit the requirements of the projec
    public void deleteEntity(int xPos, int yPos) {
        if (xPos >= 0 && xPos < entities.size() && yPos >= 0 && yPos < entities.get(xPos).size()) {
            entities.get(xPos).set(yPos, null);
        } else {
            throw new IndexOutOfBoundsException("Invalid xPos or yPos");
        }
    }
}
