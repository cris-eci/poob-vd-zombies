package domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

import presentation.GardenMenu;

public class ZombiesOriginal extends MachinePlayer{
    public static final int ORIGINAL_SPAWN_TIME = 10;
    private int hordersNumber;
    private float horderTime;
    private GardenMenu gardenMenu;
    private ArrayList<String> zombieTypes;  // Tipos de zombies posibles
    private POOBvsZombies game; // Referencia al juego para acceder a métodos necesarios

        
    private JLabel zombieLabel;
        
    
        public ZombiesOriginal(int hordersNumber, float matchTime) {
            super("OZombies");
            Team zombiesHordersTeam = new Zombies();// El constructor de Zombies no recibe parámetros porque es zombie original
            this.team = zombiesHordersTeam;
            this.hordersNumber = hordersNumber;
            this.horderTime = matchTime / hordersNumber;
            this.zombieTypes = new ArrayList<>(Arrays.asList("Basic", "BucketHead", "Conehead", "ECIZombie"));  // ejemplo de tipos de zombies
        }
    
        public ZombiesOriginal(int hordersNumber, float matchTime, ArrayList<String> zombiesMachine, int brains) {
            super("OZombies");
            Team zombiesHordersTeam = new Zombies(zombiesMachine, brains); // Assuming Zombies class has a constructor that accepts ArrayList<Zombie>
            this.team = zombiesHordersTeam;
            this.hordersNumber = hordersNumber;
            this.horderTime = matchTime / hordersNumber;
            this.zombieTypes = new ArrayList<>(Arrays.asList("Basic", "BucketHead", "Conehead"));  // ejemplo de tipos de zombies
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
    
        public void startAutomaticHordes(POOBvsZombies game) {
            this.game = game; // Asignar la referencia al juego
            new Thread(() -> {
                    try {
                        // Esperar 20 segundos antes de iniciar la primera horda
                        Thread.sleep(20000);
                        for (int i = 0; i < hordersNumber; i++) {
                            // Determinar la modalidad para usar el método adecuado
                            if ("PlayerVsMachine".equals(game.getModality())) {
                                spawnHordePlayerVsMachine(game);
                            } else if ("MachineVsMachine".equals(game.getModality())) {
                                spawnHordeMachineVsMachine(game);
                            }
                            // Esperar el tiempo asignado por horda antes de la siguiente
                            Thread.sleep((long) (horderTime * 1000));
                        }
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
            }).start();
        }
    
        // Método para PlayerVsMachine (ya implementado previamente)
        private void spawnHordePlayerVsMachine(POOBvsZombies game) {
            spawnHorde(game);
        }
    
        private void spawnHorde(POOBvsZombies game) {

            // Supongamos que cada horda tiene 10 zombies
            // Cada zombie aparecerá de forma aleatoria en una de las 5 filas (0 a 4)
            // y será de un tipo aleatorio de la lista zombieTypes.
            // Además, cada zombie aparece cada 3 segundos.
            int zombiesInHorde = 10;  // Cada horda tendrá 10 zombies (ejemplo dado)
            for (int i = 0; i < zombiesInHorde; i++) {
                try {
                    int row = (int) (Math.random() * 5); 
                    String zombieType = zombieTypes.get((int) (Math.random() * zombieTypes.size()));
                    Zombie zombie = game.createZombieInstance(zombieType);
                    
                    // Añadir el zombie al tablero en la última columna (col = 9)
                    // Esto ya incrementa el score en addEntity si así está configurado.
                    // Pero debemos sumar el costo del zombie en cerebros a OZombies
                    game.addEntity(row, 9, zombie);
        
                    // Sumar el costo del zombie en cerebros al jugador OZombies
                    Player zombiesPlayer = game.getPlayerTwo();
                    zombiesPlayer.getTeam().addResource(new Resource(Resource.BRAIN, zombie.getCost()));
                    
                    // Ahora invocar método para que se refleje en la interfaz gráfica
                    // Este método lo crearemos en POOBvsZombies
                    game.spawnZombieUI(row, 9, zombie);
        
                    Thread.sleep(3000);  // Esperar 3 segundos antes del siguiente zombie
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                }
            }
        }
        
    
        // Nuevo Método para MachineVsMachine
    private void spawnHordeMachineVsMachine(POOBvsZombies game) {
        // Continuar generando zombies hasta que se agoten las hordas
        for (int i = 0; i < hordersNumber; i++) {
            // Cada horda tendrá varios zombies según los cerebros disponibles
            // En este ejemplo, cada horda tendrá 10 zombies si los cerebros lo permiten
            int zombiesInHorde = 10;
            for (int j = 0; j < zombiesInHorde; j++) {
                try {
                    // Verificar si hay suficientes cerebros para el siguiente zombie
                    int row = (int) (Math.random() * 5); 
                    String zombieType = zombieTypes.get((int) (Math.random() * zombieTypes.size()));
                    Zombie zombie = game.createZombieInstance(zombieType);

                    int zombieCost = zombie.getCost();

                    Player zombiesPlayer = game.getPlayerTwo(); // OZombies es el segundo jugador
                    synchronized (zombiesPlayer.getTeam()) { // Sincronizar para evitar condiciones de carrera
                        if (zombiesPlayer.getTeam().getResourceCounterAmount() >= zombieCost) {
                            // Deduct the cost
                            zombiesPlayer.getTeam().deductResource(zombieCost);

                            // Añadir zombie en el dominio
                            game.addEntity(row, 9, zombie);

                            // Crear la interfaz gráfica del zombie
                            game.spawnZombieUI(row, 9, zombie);

                            // Actualizar las etiquetas de recursos
                            if (game.getGardenMenu() != null) {
                                game.getGardenMenu().updateScoreLabels();
                            }

                            // Esperar 3 segundos antes de generar el siguiente zombie
                            Thread.sleep(3000);
                        } else {
                            // No hay suficientes cerebros, esperar hasta que se acumulen más recursos
                            // Aquí, podríamos implementar una espera activa o esperar a un evento
                            // Para simplificar, haremos una espera de 1 segundo y reintentar
                            Thread.sleep(1000);
                            j--; // Reintentar el mismo zombie
                        }
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                }
            }
        }
    }

    @Override
    public void setScore() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setScore'");
    }

    
}
