package domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.swing.Timer;

import presentation.GardenMenu;

public class POOBvsZombies {
    private String modality;
    private String winner; 
    private float roundTime;
    private float matchTime;
    private ArrayList<Player> players;
    private ArrayList<ArrayList<Entity>> entities;

    // Singleton instance
    private static POOBvsZombies instance;

    // Mapa para gestionar timers individuales por entidad
    private Map<Entity, Timer> entityTimers = new HashMap<>();

    // Constructors
    public POOBvsZombies(float matchTimeInSeconds, String namePlayerOne, ArrayList<String> plants, int sunAmount, String namePlayerTwo, int brainAmount, ArrayList<String> zombies) {
        this.players = new ArrayList<Player>();
        this.entities = new ArrayList<ArrayList<Entity>>();

        this.players.add(new ZombiesStrategic(namePlayerOne, sunAmount, plants));
        this.players.add(new PlantsStrategic(namePlayerTwo, brainAmount, zombies));

        this.modality = "PlayerVsPlayer";
        this.winner = "";

        this.matchTime = setMatchTime(matchTimeInSeconds);
        this.roundTime = this.matchTime / 2;
        setUpEntities();

        instance = this;
    }

    public POOBvsZombies(float matchTimeInSeconds,int hordersNumber, String namePlayerOne, ArrayList<String> plants) {
        this.players = new ArrayList<Player>();
        this.entities = new ArrayList<ArrayList<Entity>>();

        this.players.add(new PlantsStrategic(namePlayerOne, 50, plants));
        this.players.add(new ZombiesOriginal(hordersNumber, matchTimeInSeconds));

        this.modality = "PlayerVsMachine";
        this.winner = "";

        this.matchTime = setMatchTime(matchTimeInSeconds);
        this.roundTime = this.matchTime / 2;
        setUpEntities();

        instance = this;
    }
    
    public POOBvsZombies(float matchTimeInSeconds, int hordersNumber, int suns, int brains) {
        this.players = new ArrayList<Player>();
        this.entities = new ArrayList<ArrayList<Entity>>();

        this.players.add(new PlantsIntelligent(suns));
        this.players.add(new ZombiesOriginal(hordersNumber, matchTimeInSeconds, MachinePlayer.ORIGINAL_ZOMBIES, brains));

        this.modality = "MachineVsMachine";
        this.winner = "";

        this.matchTime = setMatchTime(matchTimeInSeconds);
        this.roundTime = this.matchTime / 2;
        setUpEntities();

        instance = this;
    }

    // Método para obtener la instancia Singleton
    public static POOBvsZombies getInstance() {
        return instance;
    }
    
    public int calculateProgress(){
        return 0;
    }

    private Timer setRoundTime(int minutes) {
        int milliseconds = minutes * 60 * 1000;
        return new Timer(milliseconds, null);
    }

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
        return 0; 
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

    public void setUpEntities() {        
        for (int i = 0; i < 5; i++) {
            ArrayList<Entity> row = new ArrayList<Entity>();
            for (int j = 0; j < 10; j++) {
                row.add(null); 
            }
            entities.add(row);
        }
    }

    public void addEntity(int lane, int yPos, String entityType) {
        Entity entity = null;
        switch (entityType) {
            case "Basic":
                entity = new Basic();
                break;
            case "Brainstein":
                entity = new Brainstein();
                break;
            case "BucketHead":
                entity = new Buckethead();
                break;
            case "Conehead":
                entity = new Conehead();
                break;
            case "ECIZombie":
                entity = new ECIZombie();
                break;
            case "Sunflower":
                entity = new Sunflower();
                break;
            case "Peashooter":
                entity = new Peashooter();
                break;
            case "WallNut":
                entity = new WallNut();
                break;
            case "PotatoMine":
                entity = new PotatoMine();
                break;
            case "ECIPlant":
                entity = new ECIPlant();    
                break;
            case "LownMover":
                entity = new Lownmover();
                break;
            default:
                throw new IllegalArgumentException("Invalid entity type: " + entityType);
        }
        addEntity(lane, yPos, entity);
    }

    private void addEntity(int lane, int yPos, Entity entity) {
        if (lane >= 0 && lane < entities.size() && yPos >= 0 && yPos < entities.get(lane).size()) {
            entities.get(lane).set(yPos, entity);
            entity.setPosition(lane, yPos, entity.getName()); // Asignamos posición a la entidad
        } else {
            throw new IndexOutOfBoundsException("Invalid lane or yPos");
        }
    }

    // Método combinado para remover entidad y detener Timer
    public void deleteEntity(int row, int col) {
        Entity entity = getEntitiesMatrix().get(row).get(col);
        if (entity != null) {
            // Detener y remover el Timer asociado
            Timer timer = entityTimers.get(entity);
            if (timer != null) {
                timer.stop();
                entityTimers.remove(entity);
                System.out.println("Timer detenido y removido para la entidad: " + entity.getName());
            }

            // Remover la entidad de la matriz
            deleteEntity(row, col);
            System.out.println("Entidad removida de la matriz en posición (" + row + ", " + col + ").");

            // Remover visualmente en GardenMenu
            spawnSpecificResource(row, col, null); // Ajusta este método para manejar null
        } else {
            System.out.println("No hay entidad en la posición (" + row + ", " + col + ") para remover.");
        }
    }


    public String getEntity(int xPos, int yPos) {
        try {
            Entity entity = entities.get(xPos).get(yPos);
            if (entity != null) {
                if (entity instanceof Basic) {
                    return "Basic";
                } else if (entity instanceof Brainstein) {
                    return "Brainstein";
                } else if (entity instanceof Buckethead) {
                    return "BucketHead";
                } else if (entity instanceof Conehead) {
                    return "Conehead";
                } else if (entity instanceof ECIZombie) {
                    return "ECIZombie";
                } else if (entity instanceof Sunflower) {
                    return "Sunflower";
                } else if (entity instanceof Peashooter) {
                    return "Peashooter";
                } else if (entity instanceof WallNut) {
                    return "WallNut";
                } else if (entity instanceof PotatoMine) {
                    return "PotatoMine";
                } else if (entity instanceof ECIPlant) {
                    return "ECIPlant";
                } else if (entity instanceof Lownmover) {
                    return "LownMover";
                }
            } 
        } catch (IndexOutOfBoundsException e) {
            // Position is out of bounds
        }
        return null;
    }

    public ArrayList<ArrayList<Entity>> getEntitiesMatrix() {
        return entities;
    }

    // Método para añadir recursos extra para la ECIPlant cuando el jugador se queda sin soles
    public void addPendingExtraResources(int row, int col, int count, int value, String type) {
        for(int i =0; i < count; i++) {
            Resource resource = new Resource(type, value);
            spawnSpecificResource(row, col, resource);
        }
    }

    // Método para colocar un recurso específico en la posición (row, col)
    public void spawnSpecificResource(int row, int col, Resource resource) {
        if (gardenMenu != null) {
            gardenMenu.spawnSpecificResource(row, col, resource);
        }
    }

    // Referencia a GardenMenu
    private GardenMenu gardenMenu;

    public void setGardenMenu(GardenMenu gardenMenu) {
        this.gardenMenu = gardenMenu;
    }
}
