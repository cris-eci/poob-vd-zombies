package domain;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter; 
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

import javax.swing.Timer;

import presentation.GardenMenu;
import presentation.POOBvsZombiesGUI;

public class POOBvsZombies {
    private String modality;
    private String winner;
    private float roundTime;
    private float matchTime;
    private ArrayList<Player> players;
    private ArrayList<ArrayList<Object>> entities;

    // Singleton instance
    private static POOBvsZombies instance;

    // Map to manage individual timers for each entity
    private Map<Entity, Timer> entityTimers = new HashMap<>();

    // Reference to GardenMenu
    private GardenMenu gardenMenu;

    // Campos para restaurar estado tras cargar partida
    private int restoredIndex=0;
    private int restoredRemaining=0;
    private boolean restoredPaused=false;

    public int getRestoredIndex(){return restoredIndex;}
    public int getRestoredRemaining(){return restoredRemaining;}
    public boolean getRestoredPaused(){return restoredPaused;}

    // Constructores para distintas modalidades
    public POOBvsZombies(float matchTimeInSeconds, String namePlayerOne, ArrayList<String> plants, int sunAmount, String namePlayerTwo, int brainAmount, ArrayList<String> zombies) throws POOBvsZombiesException {
        if(matchTimeInSeconds <= 0 || namePlayerOne == null || plants == null || sunAmount <= 0 || namePlayerTwo == null || zombies == null || brainAmount <= 0){
            throw new POOBvsZombiesException(POOBvsZombiesException.INVALID_INPUTS);
        }
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

    public POOBvsZombies(float matchTimeInSeconds,int hordersNumber, String namePlayerOne, ArrayList<String> plants)throws POOBvsZombiesException {
        if(matchTimeInSeconds <= 0 || hordersNumber <= 0 || namePlayerOne == null || plants == null){
            throw new POOBvsZombiesException(POOBvsZombiesException.INVALID_INPUTS);
        }
        this.players = new ArrayList<Player>();
        this.entities = new ArrayList<ArrayList<Object>>();

        this.players.add(new PlantsStrategic(namePlayerOne, 2615, plants));
        this.players.add(new ZombiesOriginal(hordersNumber, matchTimeInSeconds));

        this.modality = "PlayerVsMachine";
        this.winner = "";

        this.matchTime = setMatchTime(matchTimeInSeconds);
        this.roundTime = this.matchTime / 2;
        setUpEntities();

        ((ZombiesOriginal) players.get(1)).startAutomaticHordes(this);

        instance = this;
    }

    public POOBvsZombies(float matchTimeInSeconds, int hordersNumber, int suns, int brains) throws POOBvsZombiesException{

        if(matchTimeInSeconds <= 0 || hordersNumber <= 0 || suns <= 0 || brains <= 0){
            throw new POOBvsZombiesException(POOBvsZombiesException.INVALID_INPUTS);
        }

        this.players = new ArrayList<Player>();
        this.entities = new ArrayList<ArrayList<Object>>();

        this.players.add(new PlantsIntelligent(suns));
        this.players.add(new ZombiesOriginal(hordersNumber, matchTimeInSeconds, MachinePlayer.ORIGINAL_ZOMBIES, brains));

        this.modality = "MachineVsMachine";
        this.winner = "";

        this.matchTime = setMatchTime(matchTimeInSeconds);
        this.roundTime = this.matchTime / 2;
        setUpEntities();


        ((PlantsIntelligent) players.get(0)).startAutomaticPlanting(this);
        ((ZombiesOriginal) players.get(1)).startAutomaticHordes(this);

        instance = this;
    }

    public static POOBvsZombies getInstance() {
        return instance;
    }

    public Zombie createZombieInstance(String zombieType) {
        switch (zombieType) {
            case "Basic":
                return new Basic();
            case "Brainstein":
                return new Brainstein();
            case "BucketHead":
                return new Buckethead();
            case "Conehead":
                return new Conehead();
            case "ECIZombie":
                return new ECIZombie();
            default:
                throw new IllegalArgumentException("Tipo de zombie inválido: " + zombieType);
        }
    }

    public Plant createPlantInstance(String plantType) {
        switch (plantType) {
            case "Sunflower":
                return new Sunflower();
            case "Peashooter":
                return new Peashooter();
            case "WallNut":
                return new WallNut();
            case "PotatoMine":
                return new PotatoMine();
            case "ECIPlant":
                return new ECIPlant();
            default:
                return null;
        }
    }

    public void spawnZombieUI(int row, int col, Zombie zombie) {
        if (gardenMenu != null) {
            gardenMenu.spawnZombieAutomatically(row, col, zombie);
        }
    }

    public void spawnPlantUI(int row, int col, Plant plant) {
        if (gardenMenu != null) {
            gardenMenu.spawnPlantAutomatically(row, col, plant);
        }
    }

    public int calculateProgress(){
        return 0;
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

    public void calculateScores() {
        Player plantsPlayer = players.get(0);
        int plantsResources = plantsPlayer.getTeam().getResourceCounterAmount();
        int plantsValue = calculateEntitiesValue(entities, true);
        int plantsScore = (int) ((plantsResources + plantsValue) * 1.5);
        plantsPlayer.setScore(plantsScore);

        Player zombiesPlayer = players.get(1);
        int zombiesResources = zombiesPlayer.getTeam().getResourceCounterAmount();
        int zombiesValue = calculateEntitiesValue(entities, false);
        int zombiesScore = (zombiesResources + zombiesValue);
        zombiesPlayer.setScore(zombiesScore);
    }

    public String determineWinner() {
        Player plantsPlayer = players.get(0);
        Player zombiesPlayer = players.get(1);

        int plantsScore = plantsPlayer.getScore();
        int zombiesScore = zombiesPlayer.getScore();

        if (plantsScore > zombiesScore) {
            winner = plantsPlayer.getName();
            return "¡Las plantas han ganado! Puntaje: " + plantsScore + " vs " + zombiesScore;
        } else if (plantsScore < zombiesScore) {
            winner = zombiesPlayer.getName();
            return "¡Los zombies han ganado! Puntaje: " + zombiesScore + " vs " + plantsScore;
        } else {
            winner = "Empate";
            return "¡Es un empate! Ambos jugadores tienen puntaje: " + plantsScore;
        }
    }

    public void endGame(String winnerMessage) {
        calculateScores();
        if (gardenMenu != null) {
            gardenMenu.showWinnerMessage(winnerMessage);
        }
        System.exit(0);
    }

    private int calculateEntitiesValue(ArrayList<ArrayList<Object>> entities, boolean isPlant) {
        int totalValue = 0;
        for (ArrayList<Object> row : entities) {
            for (Object obj : row) {
                if (obj instanceof Plant && isPlant) {
                    totalValue += ((Plant) obj).getCost();
                } else if (obj instanceof Zombie && !isPlant) {
                    totalValue += ((Zombie) obj).getCost();
                }
            }
        }
        return totalValue;
    }

    public void setUpEntities() {
        for (int i = 0; i < 5; i++) {
            ArrayList<Object> row = new ArrayList<Object>();
            for (int j = 0; j < 10; j++) {
                if (j == 9) {
                    row.add(new LinkedList<Zombie>());
                } else {
                    row.add(null);
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

    public void addEntity(int lane, int yPos, Entity entity) {
        if (lane < 0 || lane >= 5 || yPos < 0 || yPos >= 10) {
            throw new IndexOutOfBoundsException("Invalid lane or yPos");
        }

        if (yPos < 9) {
            entities.get(lane).set(yPos, entity);
            if (entity instanceof Plant) {
                Player plantsPlayer = players.get(0);
                plantsPlayer.setScore(plantsPlayer.getScore() + ((Plant) entity).getCost());
            } else if (entity instanceof Zombie) {
                Player zombiesPlayer = players.get(1);
                zombiesPlayer.setScore(zombiesPlayer.getScore() + ((Zombie) entity).getCost());
            }
        } else {
            // Última columna: agregamos cola de zombies
            if (entity instanceof Zombie) {
                Queue<Zombie> queue = getZombieQueue(lane, 9);
                queue.offer((Zombie) entity);
                Player zombiesPlayer = players.get(1);
                zombiesPlayer.addToScore(((Zombie) entity).getCost());
                if (gardenMenu!=null) gardenMenu.updateScoreLabels();
            } else {
                throw new IllegalArgumentException("Only zombies can be added to the last column");
            }
        }
    }

    public void deleteEntity(int xPos, int yPos) {
        if (xPos<0||xPos>=5||yPos<0||yPos>=10) throw new IndexOutOfBoundsException("Invalid xPos or yPos");
        if (yPos<9) {
            entities.get(xPos).set(yPos, null);
        } else {
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
            Queue<Zombie> q = getZombieQueue(xPos,yPos);
            if (q.isEmpty()) return null;
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
        if (row<0||row>=5||col<0||col>=9) return null;
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

    public ArrayList<ArrayList<Object>> getEntitiesMatrix() {
        return entities;
    }

    public void addPendingExtraResources(int row, int col, int count, int value, String type) {
        for(int i =0; i < count; i++) {
            Resource resource = new Resource(type, value);
            spawnSpecificResource(row, col, resource);
        }
    }

    public void spawnSpecificResource(int row, int col, Resource resource) {
        if (gardenMenu != null) {
            gardenMenu.spawnSpecificResource(row, col, resource);
        }
    }

    public void setGardenMenu(GardenMenu gardenMenu) {
        this.gardenMenu = gardenMenu;
    }

    public GardenMenu getGardenMenu() {
        return gardenMenu;
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

    public void setWinner(String winner) {
        this.winner = winner;
    }

    public String getWinner() {
        return winner;
    }

    /**
     * Guarda el estado completo del juego, incluyendo modalidad, tiempos, jugadores, scores, recursos, índice del timer,
     * tiempo restante, estado de pausa, la matriz de entidades, y también las cartas seleccionadas por ambos jugadores.
     */
    public void saveGame(File file, int currentIndex, int currentRemaining, boolean isPaused) throws IOException {
        try (PrintWriter pw = new PrintWriter(new FileWriter(file))) {
            pw.println("modality=" + getModality());
            pw.println("matchTime=" + getMatchTime());
            pw.println("roundTime=" + getRoundTime());

            Player p1 = getPlayerOne();
            pw.println("playerOneName=" + p1.getName());
            pw.println("playerOneScore=" + p1.getScore());
            pw.println("playerOneSuns=" + p1.getTeam().getResourceCounterAmount());

            Player p2 = getPlayerTwo();
            pw.println("playerTwoName=" + p2.getName());
            pw.println("playerTwoScore=" + p2.getScore());
            pw.println("playerTwoBrains=" + p2.getTeam().getResourceCounterAmount());

            pw.println("currentTimerTaskIndex=" + currentIndex);
            pw.println("currentTimerTaskRemainingTime=" + currentRemaining);
            pw.println("isPaused=" + isPaused);

            // Guardar la matriz de entidades
            for (int i = 0; i < 5; i++) {
                StringBuilder sb = new StringBuilder();
                for (int j = 0; j < 10; j++) {
                    Object obj = entities.get(i).get(j);
                    if (j > 0) sb.append(" ");
                    if (j < 9) {
                        if (obj == null) sb.append("-");
                        else sb.append(entityName((Entity)obj));
                    } else {
                        @SuppressWarnings("unchecked")
                        Queue<Zombie> q = (Queue<Zombie>) obj;
                        if (q.isEmpty()) sb.append("-");
                        else {
                            Zombie z = q.peek();
                            sb.append(entityName(z));
                        }
                    }
                }
                pw.println(sb.toString());
            }

            // Guardar las cartas seleccionadas por ambos jugadores
            // Plantas del jugador 1
            for (String plant: getPlayerOne().getTeam().getCharacters()) {
                pw.println("selectedPlant=" + plant);
            }

            // Zombies del jugador 2
            for (String zombie: getPlayerTwo().getTeam().getCharacters()) {
                pw.println("selectedZombie=" + zombie);
            }
        }
    }

    /**
     * Carga el estado completo del juego desde el archivo especificado, restaurando modalidad, tiempos, jugadores, 
     * scores, recursos, índice del timer, tiempo restante, estado de pausa, la matriz de entidades y las cartas seleccionadas.
     */
    public static POOBvsZombies loadGame(File file) throws IOException, POOBvsZombiesException {
        BufferedReader br = new BufferedReader(new FileReader(file));

        String modality="";
        float matchTime=0f, roundTime=0f;
        String playerOneName="", playerTwoName="";
        int playerOneScore=0, playerOneSuns=0;
        int playerTwoScore=0, playerTwoBrains=0;
        int currentTaskIndex=0, currentTaskRemaining=0;
        boolean isPaused=false;

        String line;
        ArrayList<String> headerLines = new ArrayList<>();
        while((line=br.readLine())!=null) {
            if (line.startsWith("Basic")||line.startsWith("Conehead")||line.startsWith("BucketHead")||line.startsWith("Brainstein")||line.startsWith("ECIZombie")||
                line.startsWith("Sunflower")||line.startsWith("Peashooter")||line.startsWith("WallNut")||line.startsWith("PotatoMine")||line.startsWith("ECIPlant")||line.startsWith("LownMover")||
                line.startsWith("-")) {
                // Esto significa que ya comenzamos a leer la matriz
                // Añadimos el último que no es header a la lista también
                headerLines.add(line);
                break;
            }
            headerLines.add(line);
            if (line.startsWith("isPaused=")) break;
        }

        for (String l: headerLines) {
            if (l.startsWith("modality=")) modality = l.split("=")[1];
            else if (l.startsWith("matchTime=")) matchTime = Float.parseFloat(l.split("=")[1]);
            else if (l.startsWith("roundTime=")) roundTime = Float.parseFloat(l.split("=")[1]);
            else if (l.startsWith("playerOneName=")) playerOneName = l.split("=")[1];
            else if (l.startsWith("playerOneScore=")) playerOneScore = Integer.parseInt(l.split("=")[1]);
            else if (l.startsWith("playerOneSuns=")) playerOneSuns = Integer.parseInt(l.split("=")[1]);
            else if (l.startsWith("playerTwoName=")) playerTwoName = l.split("=")[1];
            else if (l.startsWith("playerTwoScore=")) playerTwoScore = Integer.parseInt(l.split("=")[1]);
            else if (l.startsWith("playerTwoBrains=")) playerTwoBrains = Integer.parseInt(l.split("=")[1]);
            else if (l.startsWith("currentTimerTaskIndex=")) currentTaskIndex = Integer.parseInt(l.split("=")[1]);
            else if (l.startsWith("currentTimerTaskRemainingTime=")) currentTaskRemaining = Integer.parseInt(l.split("=")[1]);
            else if (l.startsWith("isPaused=")) isPaused = Boolean.parseBoolean(l.split("=")[1]);
        }

        // Ahora leer la matriz
        ArrayList<String> matrixLines = new ArrayList<>();
        if (!headerLines.get(headerLines.size()-1).contains("isPaused=")) {
            // El ultimo leido no fue isPaused, sino que ya es parte de la matriz
            // La ultima linea leida fue la primera linea de la matriz
            matrixLines.add(headerLines.get(headerLines.size()-1));
        }

        // Leer las 4 lineas restantes de la matriz
        while(matrixLines.size()<5 && (line=br.readLine())!=null) {
            matrixLines.add(line);
        }

        ArrayList<ArrayList<Object>> newMatrix = new ArrayList<>();
        for (int i=0; i<5; i++) {
            String matLine = matrixLines.get(i);
            String[] parts = matLine.split(" ");
            if (parts.length<10) {
                br.close();
                throw new IOException("Línea de matriz inválida");
            }
            ArrayList<Object> row = new ArrayList<>();
            for (int j=0;j<10;j++) {
                String entName = parts[j];
                if (entName.equals("-")) {
                    if (j<9) row.add(null);
                    else {
                        row.add(new LinkedList<Zombie>());
                    }
                } else {
                    if (j<9) {
                        Entity e = createEntityStatic(entName);
                        row.add(e);
                    } else {
                        Queue<Zombie> q = new LinkedList<>();
                        Zombie z = (Zombie)createEntityStatic(entName);
                        q.offer(z);
                        row.add(q);
                    }
                }
            }
            newMatrix.add(row);
        }

        // Ahora leer las lineas restantes para las cartas seleccionadas
        ArrayList<String> selectedPlants = new ArrayList<>();
        ArrayList<String> selectedZombies = new ArrayList<>();
        while((line=br.readLine())!=null) {
            if (line.startsWith("selectedPlant=")) {
                selectedPlants.add(line.split("=")[1]);
            } else if (line.startsWith("selectedZombie=")) {
                selectedZombies.add(line.split("=")[1]);
            }
        }

        br.close();

        // Crear instancia POOBvsZombies según modalidad
        POOBvsZombies loadedGame;
        ArrayList<String> emptyList = new ArrayList<>();
        if (modality.equals("PlayerVsPlayer")) {
            loadedGame = new POOBvsZombies(1, "tempP1", emptyList, 50, "tempP2", 50, emptyList);
        } else if (modality.equals("PlayerVsMachine")) {
            loadedGame = new POOBvsZombies(1,1,"tempP1",emptyList);
        } else if (modality.equals("MachineVsMachine")) {
            loadedGame = new POOBvsZombies(1,1,50,50);
        } else {
            loadedGame = new POOBvsZombies(1,"tempP1",emptyList,50,"tempP2",50,emptyList);
        }

        loadedGame.modality = modality;
        loadedGame.matchTime = matchTime;
        loadedGame.roundTime = roundTime;

        loadedGame.getPlayerOne().setName(playerOneName);
        loadedGame.getPlayerOne().setScore(playerOneScore);
        loadedGame.getPlayerOne().getTeam().setResourceCounter(playerOneSuns);

        loadedGame.getPlayerTwo().setName(playerTwoName);
        loadedGame.getPlayerTwo().setScore(playerTwoScore);
        loadedGame.getPlayerTwo().getTeam().setResourceCounter(playerTwoBrains);

        loadedGame.entities = newMatrix;

        // Restaurar cartas seleccionadas
        loadedGame.getPlayerOne().getTeam().setCharacters(selectedPlants);
        loadedGame.getPlayerTwo().getTeam().setCharacters(selectedZombies);

        // Guardar info adicional para restaurar en GardenMenu
        loadedGame.restoredIndex = currentTaskIndex;
        loadedGame.restoredRemaining = currentTaskRemaining;
        loadedGame.restoredPaused = isPaused;

        return loadedGame;
    }

    private static Entity createEntityStatic(String name) {
        switch (name) {
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
            default: return null;
        }
    }

}
