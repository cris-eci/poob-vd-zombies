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

    // player vs player
    public POOBvsZombies(float matchTimeInSeconds, String namePlayerOne, ArrayList<String> plants, int sunAmount, String namePlayerTwo, int brainAmount, ArrayList<String> zombies) {
        this.players = new ArrayList<Player>();
        this.entities = new ArrayList<ArrayList<Entity>>();

        this.players.add(new ZombiesStrategic(namePlayerOne, sunAmount, plants));
        this.players.add(new PlantsStrategic(namePlayerTwo, brainAmount, zombies));

        this.modality = "PlayerVsPlayer";
        this.winner = "";

        //this.entities = new ArrayList<Entity>();
        this.matchTime = setMatchTime(matchTimeInSeconds);
        this.roundTime = this.matchTime / 2;
        setUpEntities();
    }

    // player vs machine
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

    }

    
    
    public POOBvsZombies(float matchTimeInSeconds, int hordersNumber, int suns, int brains) {
        this.players = new ArrayList<Player>();
        this.entities = new ArrayList<ArrayList<Entity>>();

        this.players.add(new PlantsIntelligent(suns));
        this.players.add(new ZombiesOriginal(hordersNumber, matchTimeInSeconds, MachinePlayer.ORIGINAL_ZOMBIES,brains));

        this.modality = "MachineVsMachine";
        this.winner = "";

        this.entities = new ArrayList<ArrayList<Entity>>();
        this.matchTime = setMatchTime(matchTimeInSeconds);
        this.roundTime = this.matchTime / 2;
        setUpEntities();
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
        for (int i = 0; i < 5; i++) {
            ArrayList<Entity> row = new ArrayList<Entity>();
            for (int j = 0; j < 10; j++) {                
                row.add(null); 
            }
            entities.add(row);
        }
    }

    // Método sobre cargado que me permite recibir un String del entity, convertirlo a un objeto y agregarlo a la matriz de entidades
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

    //Initial code for the method addEntity, it should be modified to fit the requirements of the project
    // método que me permite agregar un objeto entity a la matriz de entidades
    private void addEntity(int lane, int yPos, Entity entity) {
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

    // public Entity getEntity(int xPos, int yPos) {
    //     if (xPos >= 0 && xPos < entities.size() && yPos >= 0 && yPos < entities.get(xPos).size()) {
    //         return entities.get(xPos).get(yPos);
    //     } else {
    //         throw new IndexOutOfBoundsException("Invalid xPos or yPos");
    //     }
    // }

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
}
