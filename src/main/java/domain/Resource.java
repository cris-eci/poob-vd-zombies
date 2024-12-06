package domain;

public class Resource extends Entity {
    public static final String SOL = "SOL";
    public static final String BRAIN = "BRAIN";
    public static final int SOL_VALUE = 25;
    public static final int BRAIN_VALUE = 50;

    private String type;

    public Resource(String type){
        this.type = type;
    }

    public String getType(){
        return type;
    }

    public int getValue(){
        if (SOL.equals(type)) {
            return SOL_VALUE;
        } else if (BRAIN.equals(type)) {
            return BRAIN_VALUE;
        }
        return 0;
    }

    public void collect(){
        // Implementar lógica de recolección si es necesario
    }

    public void spawn(){
        // Implementar lógica de aparición si es necesario
    }
}