package domain;

public class Resource extends Entity {
    public static final String SOL = "SOL";
    public static final String BIG_SOL = "BIG_SOL"; // Nuevo tipo para Big Suns
    public static final String BRAIN = "BRAIN";
    public static final String THREE_BIG_SOL = "THREE_BIG_SOL"; // Nuevo tipo para 3 Big Suns
    public static final int SOL_VALUE = 25;
    public static final int BRAIN_VALUE = 50;
    public static final int BIG_SOL_VALUE = 50; // Valor de cada Big Sun
    public static final int THREE_BIG_VALUE = 150; // Valor de 3 Big Suns
    

    private String type;
    private int value;

    public Resource(String type){
        this.type = type;
        if (SOL.equals(type)) {
            this.value = SOL_VALUE;
        } else if (BRAIN.equals(type)) {
            this.value = BRAIN_VALUE;
        } else if (BIG_SOL.equals(type)) {
            this.value = BIG_SOL_VALUE;
        }
        else if(THREE_BIG_SOL.equals(type)){
            this.value = THREE_BIG_VALUE;
        }
    }

    // Constructor con valor personalizado
    public Resource(String type, int customValue){
        this.type = type;
        this.value = customValue;
    }

    public String getType(){
        return type;
    }

    public int getValue(){
        return value;
    }

    public void collect(){
        // Implementar lógica de recolección si es necesario
    }

    public void spawn(){
        // Implementar lógica de aparición si es necesario
    }
}
