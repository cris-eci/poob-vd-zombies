package domain;

import java.util.ArrayList;

public class ZombiesOriginal extends HumanPlayer{
    public static final int ORIGINAL_SPAWN_TIME = 10;
    private int hordersNumber;
    private float horderTime;

    public ZombiesOriginal(int hordersNumber, float matchTime) {
        super("Original Zombies machine");
        Team zombiesHordersTeam = new Zombies();// El constructor de Zombies no recibe parámetros porque es zombie original
        this.team = zombiesHordersTeam;
        this.hordersNumber = hordersNumber;
        this.horderTime = matchTime / hordersNumber;
    }

    public ZombiesOriginal(int hordersNumber, float matchTime, ArrayList<String> zombiesMachine) {
        super("Smart Zombies machine");
        Team zombiesHordersTeam = new Zombies(zombiesMachine); // Assuming Zombies class has a constructor that accepts ArrayList<Zombie>
        this.team = zombiesHordersTeam;
        this.hordersNumber = hordersNumber;
        this.horderTime = matchTime / hordersNumber;
    }

    public void setOriginalStrategy() {
        // Implementation for setting the original strategy
    }

    public void spawnZombie(Zombie zombie, int line, int xPos) {
        // Implementation for spawning a zombie
    }

    public void setHordersNumbers(int number) {
        this.hordersNumber = number;
    }

    public void setHordersTime(float matchTime) {
        this.horderTime = matchTime / hordersNumber;
    }

    public int getHordersNumber() {
        return hordersNumber;
    }

    public float getHorderTime(float matchTime) {
        return horderTime;
    }

    public void playTurn() {
        // Implementation for playing a turn
    }
}
