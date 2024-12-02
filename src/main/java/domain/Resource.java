package domain;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Clase que representa un recurso (sol o cerebro).
 */
public class Resource extends Entity {
    private String type; // "sun" o "brain"
    private int value;
    private boolean collected = false;

    public Resource(int line, int xPos, String type) {
        super(line, xPos);
        this.type = type;
        this.value = determineValue(type);
        scheduleCollection();
    }

    private int determineValue(String type) {
        switch (type) {
            case "sun":
                return 25;
            case "brain":
                return 50;
            default:
                return 0;
        }
    }

    private void scheduleCollection() {
        // Programar la recolección del recurso después de 2 segundos
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                collected = true;
            }
        }, 2000);
    }

    public boolean isCollected() {
        return collected;
    }

    public String getType() {
        return type;
    }

    public int getValue() {
        return value;
    }
}
