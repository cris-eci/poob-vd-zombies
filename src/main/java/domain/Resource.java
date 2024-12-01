package domain;

/**
 * Clase que representa un recurso (sol o cerebro).
 */
public class Resource extends Entity {
    private String type; // "sun", "bigSun", "brain"
    private int value;

    public Resource(int line, int xPos, String type) {
        super(line, xPos);
        this.type = type;
        this.value = determineValue(type);
    }

    private int determineValue(String type) {
        switch (type) {
            case "sun":
                return 25;
            case "bigSun":
                return 50;
            case "brain":
                return 25;
            default:
                return 0;
        }
    }

    public void collect() {
        // Lógica para recolectar el recurso
    }

    public String getType() {
        return type;
    }

    public int getValue() {
        return value;
    }
}
