package domain;
import java.util.ArrayList;


public abstract class MachinePlayer extends Player {
    public static final ArrayList<String> MACHINE_PLANTS = new ArrayList<>();
    public static final ArrayList<String> MACHINE_ZOMBIES = new ArrayList<>();
    public static final ArrayList<String> ORIGINAL_ZOMBIES = new ArrayList<>();

    static {
        ORIGINAL_ZOMBIES.add("Basic");
        ORIGINAL_ZOMBIES.add("Brainstein");
        ORIGINAL_ZOMBIES.add("BucketHead");
        ORIGINAL_ZOMBIES.add("Conehead");
        ORIGINAL_ZOMBIES.add("ECIZombie");
        
        MACHINE_PLANTS.add("Sunflower");
        MACHINE_PLANTS.add("Peashooter");
        MACHINE_PLANTS.add("Wallnut");
        
        MACHINE_ZOMBIES.add("Basic");
        MACHINE_ZOMBIES.add("Conehead");
        MACHINE_ZOMBIES.add("Buckhead");
    }
    
    public MachinePlayer(String name) {
        super(name);
    }


}
