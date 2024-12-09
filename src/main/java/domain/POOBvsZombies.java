package domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

import javax.swing.Timer;

import presentation.GardenMenu;

public class POOBvsZombies {
    private String modality;
    private String winner; 
    private float roundTime;
    private float matchTime;
    private ArrayList<Player> players;
    private ArrayList<ArrayList<Object>> entities; 

    // Singleton instance
    private static POOBvsZombies instance;

    // Mapa para gestionar timers individuales por entidad
    private Map<Entity, Timer> entityTimers = new HashMap<>();

    // Constructors
    public POOBvsZombies(float matchTimeInSeconds, String namePlayerOne, ArrayList<String> plants, int sunAmount, String namePlayerTwo, int brainAmount, ArrayList<String> zombies) {
        this.players = new ArrayList<Player>();
        this.entities = new ArrayList<ArrayList<Object>>();

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
        this.entities = new ArrayList<ArrayList<Object>>();

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
        this.entities = new ArrayList<ArrayList<Object>>();

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
            ArrayList<Object> row = new ArrayList<Object>();
            for (int j = 0; j < 10; j++) {
                if (j == 9) {
                    row.add(new LinkedList<Zombie>()); // Cola de zombies
                } else {
                    row.add(null); // una sola entidad o null
                }
            }
            entities.add(row);
        }
    }

    public void addEntity(int lane, int yPos, String entityType) {
        Entity entity = createEntity(entityType);
        addEntity(lane, yPos, entity);
    }
    
    private Entity createEntity(String entityType) {
        switch (entityType) {
            case "Basic": return new Basic();
            case "Brainstein": return new Brainstein();
            case "BucketHead": return new Buckethead();
            case "Conehead": return new Conehead();
            case "ECIZombie": return new ECIZombie();
            case "Sunflower": return new Sunflower();
            case "Peashooter": return new Peashooter();
            case "WallNut": return new WallNut();
            case "PotatoMine": return new PotatoMine();
            case "ECIPlant": return new ECIPlant();
            case "LownMover": return new Lownmover();
            default:
                throw new IllegalArgumentException("Invalid entity type: " + entityType);
        }
    }

    private void addEntity(int lane, int yPos, Entity entity) {
        if (lane<0 || lane>=5 || yPos<0 || yPos>=10) throw new IndexOutOfBoundsException("Invalid lane or yPos");
        if (yPos<9) {
            entities.get(lane).set(yPos, entity);
        } else {
            // Ultima columna: agregamos cola de zombies
            if (entity instanceof Zombie) {
                Queue<Zombie> queue = getZombieQueue(lane, 9);
                queue.offer((Zombie)entity);
            } else {
                // si se agregara algo que no es zombi (ej: error)
                throw new IllegalArgumentException("Only zombies can be added to last column");
            }
        }
    }

    public void deleteEntity(int xPos, int yPos) {
        if (xPos<0||xPos>=5||yPos<0||yPos>=10) throw new IndexOutOfBoundsException("Invalid xPos or yPos");
        if (yPos<9) {
            entities.get(xPos).set(yPos, null);
        } else {
            // Si borramos aqui, significaría limpiar cola
            Queue<Zombie> q = getZombieQueue(xPos,9);
            q.clear();
        }
    }

    @SuppressWarnings("unchecked")
    private Queue<Zombie> getZombieQueue(int row, int col) {
        return (Queue<Zombie>) entities.get(row).get(col);
    }

    public String getEntity(int xPos, int yPos) {
        if (xPos<0||xPos>=5||yPos<0||yPos>=10) return null;
        if (yPos<9) {
            Object obj = entities.get(xPos).get(yPos);
            Entity entity = (Entity)obj;
            return entityName(entity);
        } else {
            // última columna
            Queue<Zombie> q = getZombieQueue(xPos,yPos);
            if (q.isEmpty()) return null;
            // Por si quisieramos el primer zombi del queue:
            Zombie z = q.peek();
            return entityName(z);
        }
    }

    private String entityName(Entity entity) {
        if (entity==null) return null;
        if (entity instanceof Basic) return "Basic";
        if (entity instanceof Brainstein) return "Brainstein";
        if (entity instanceof Buckethead) return "BucketHead";
        if (entity instanceof Conehead) return "Conehead";
        if (entity instanceof ECIZombie) return "ECIZombie";
        if (entity instanceof Sunflower) return "Sunflower";
        if (entity instanceof Peashooter) return "Peashooter";
        if (entity instanceof WallNut) return "WallNut";
        if (entity instanceof PotatoMine) return "PotatoMine";
        if (entity instanceof ECIPlant) return "ECIPlant";
        if (entity instanceof Lownmover) return "LownMover";
        return null;
    }

    /**
     * Encuentra la planta más cercana hacia la última columna en la fila dada.
     * Recorre de col=8 a col=0, si encuentra una planta retorna esa col.
     * Si no encuentra, retorna -1
     */
    public int getFirstPlantInRow(int row) {
        for (int col = 8; col >= 0; col--) {
            Object obj = entities.get(row).get(col);
            if (obj instanceof Plant) {
                return col;
            }
        }
        return -1;
    }

    public Plant getPlantAt(int row, int col) {
        if (row<0||row>=5||col<0||col>=9) return null; // En la ultima col no hay plantas
        Object obj = entities.get(row).get(col);
        if (obj instanceof Plant) {
            return (Plant)obj;
        }
        return null;
    }
    
    public void removeEntity(int row, int col) {
        if (row<0||row>=5||col<0||col>=10) return;
        entities.get(row).set(col,null);
    }

    // public void addEntity(int lane, int yPos, String entityType) {
    //     Entity entity = null;
    //     switch (entityType) {
    //         case "Basic":
    //             entity = new Basic();
    //             break;
    //         case "Brainstein":
    //             entity = new Brainstein();
    //             break;
    //         case "BucketHead":
    //             entity = new Buckethead();
    //             break;
    //         case "Conehead":
    //             entity = new Conehead();
    //             break;
    //         case "ECIZombie":
    //             entity = new ECIZombie();
    //             break;
    //         case "Sunflower":
    //             entity = new Sunflower();
    //             break;
    //         case "Peashooter":
    //             entity = new Peashooter();
    //             break;
    //         case "WallNut":
    //             entity = new WallNut();
    //             break;
    //         case "PotatoMine":
    //             entity = new PotatoMine();
    //             break;
    //         case "ECIPlant":
    //             entity = new ECIPlant();    
    //             break;
    //         case "LownMover":
    //             entity = new Lownmover();
    //             break;
    //         default:
    //             throw new IllegalArgumentException("Invalid entity type: " + entityType);
    //     }
    //     addEntity(lane, yPos, entity);
    // }

    // private void addEntity(int lane, int yPos, Entity entity) {
    //     if (lane >= 0 && lane < entities.size() && yPos >= 0 && yPos < entities.get(lane).size()) {
    //         entities.get(lane).set(yPos, entity);
    //         entity.setPosition(lane, yPos, entity.getName()); // Asignamos posición a la entidad
    //     } else {
    //         throw new IndexOutOfBoundsException("Invalid lane or yPos");
    //     }
    // }

    // // Método combinado para remover entidad y detener Timer
    // public void deleteEntity(int row, int col) {
    //     Entity entity = getEntitiesMatrix().get(row).get(col);
    //     if (entity != null) {
    //         // Detener y remover el Timer asociado
    //         Timer timer = entityTimers.get(entity);
    //         if (timer != null) {
    //             timer.stop();
    //             entityTimers.remove(entity);
    //             System.out.println("Timer detenido y removido para la entidad: " + entity.getName());
    //         }

    //         // Remover la entidad de la matriz
    //         deleteEntity(row, col);
    //         System.out.println("Entidad removida de la matriz en posición (" + row + ", " + col + ").");

    //         // Remover visualmente en GardenMenu
    //         spawnSpecificResource(row, col, null); // Ajusta este método para manejar null
    //     } else {
    //         System.out.println("No hay entidad en la posición (" + row + ", " + col + ") para remover.");
    //     }
    // }


    // public String getEntity(int xPos, int yPos) {
    //     try {
    //         Entity entity = entities.get(xPos).get(yPos);
    //         if (entity != null) {
    //             if (entity instanceof Basic) {
    //                 return "Basic";
    //             } else if (entity instanceof Brainstein) {
    //                 return "Brainstein";
    //             } else if (entity instanceof Buckethead) {
    //                 return "BucketHead";
    //             } else if (entity instanceof Conehead) {
    //                 return "Conehead";
    //             } else if (entity instanceof ECIZombie) {
    //                 return "ECIZombie";
    //             } else if (entity instanceof Sunflower) {
    //                 return "Sunflower";
    //             } else if (entity instanceof Peashooter) {
    //                 return "Peashooter";
    //             } else if (entity instanceof WallNut) {
    //                 return "WallNut";
    //             } else if (entity instanceof PotatoMine) {
    //                 return "PotatoMine";
    //             } else if (entity instanceof ECIPlant) {
    //                 return "ECIPlant";
    //             } else if (entity instanceof Lownmover) {
    //                 return "LownMover";
    //             }
    //         } 
    //     } catch (IndexOutOfBoundsException e) {
    //         // Position is out of bounds
    //     }
    //     return null;
    // }

    public ArrayList<ArrayList<Object>> getEntitiesMatrix() {
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

    public boolean getLawnmowerInRow(int row) {
        if (row < 0 || row >= entities.size()) {
            throw new IndexOutOfBoundsException("Invalid row index: " + row);
        }
        Object obj = entities.get(row).get(0);
        if (obj instanceof Lownmover) {
            entities.get(row).set(0, null);
            return true;
        }
        return false;
    }

    public void removeZombiesInRow(int row) {
        if (row < 0 || row >= 5) {
            throw new IndexOutOfBoundsException("Invalid row index: " + row);
        }
        Queue<Zombie> queue = getZombieQueue(row, 9);
        while (!queue.isEmpty()) {
            queue.poll();
        }
    }
}