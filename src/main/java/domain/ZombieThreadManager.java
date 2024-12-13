package domain;

import java.awt.Container;
import java.awt.Image;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;

import presentation.GardenMenu;

/**
 * Manages zombie threads and their interactions within the game.
 * <p>
 * This class handles the creation, management, and termination of zombie threads,
 * as well as their interactions with plants and other game elements. It also manages
 * the graphical representation of zombies and their movements within the user interface.
 * </p>
  * @author: Andersson David Sánchez Méndez
 * @author: Cristian Santiago Pedraza Rodríguez
 * @version 2024
 */
public class ZombieThreadManager {

    private POOBvsZombies game;
    private GardenMenu garden;

    // Almacenamos los hilos segun su columna
    private Map<Integer, List<Thread>> zombieThreadsByRow = new HashMap<>();

    // Almacenamos los JLabels de los zombies para poder moverlos o quitarlos
    private Map<Thread, JLabel> threadToLabelMap = new HashMap<>();

    // Almacenamos los zombies para poder acceder a ellos despues y a sus atributos
    private Map<Thread, Zombie> threadToZombieMap = new HashMap<>();

    // Nueva estructura para rastrear los hilos de proyectiles por ECIZombie
    private Map<Thread, Thread> zombieToProjectileThreadMap = new HashMap<>();

    /**
     * Terminates all zombie threads in the specified row, stops their movement,
     * removes their graphical representation from the user interface, and interrupts
     * any associated projectile threads if the zombies are instances of {@code ECIZombie}.
     *
     * @param row the row number whose zombie threads are to be terminated
     */
    public void terminateZombiesInRow(int row) {
        List<Thread> threads;
        synchronized (zombieThreadsByRow) {
            threads = zombieThreadsByRow.remove(row);
        }
        if (threads != null) {
            for (Thread thread : threads) {
                
                thread.interrupt();
                JLabel zombieLabel = threadToLabelMap.remove(thread);
                // Eliminar el zombieLabel de la interfaz gráfica
                if (zombieLabel != null) {
                    SwingUtilities.invokeLater(() -> {
                        Container parent = zombieLabel.getParent();
                        if (parent != null) {
                            parent.remove(zombieLabel);
                            parent.revalidate();
                            parent.repaint();
                        }
                    });
                }
                // Si el zombie es un ECIZombie, también terminamos su hilo de proyectil
                Zombie zombie = threadToZombieMap.get(thread);
                if (zombie instanceof ECIZombie) {
                    Thread projectileThread = zombieToProjectileThreadMap.remove(thread);
                    if (projectileThread != null) {
                        projectileThread.interrupt();
                    }
                }                
            }
        }
    }

    /**
     * Constructs a new ZombieThreadManager associated with the given game and garden menu.
     *
     * @param game   the POOBvsZombies game instance
     * @param garden the GardenMenu associated with the game
     */
    public ZombieThreadManager(POOBvsZombies game, GardenMenu garden) {
        this.game = game;
        this.garden = garden;
    }

    /**
     * Registers a zombie to be managed by the thread manager.
     * <p>
     * This method creates a new thread to handle the zombie's movement and logic.
     * It stores the thread, zombie instance, and associated JLabel for future reference.
     * If the zombie is an instance of {@code ECIZombie}, it also creates and starts a separate
     * thread to manage its projectile attacks.
     *
     * @param row         the row where the zombie is located
     * @param zombie      the zombie instance to register
     * @param zombieLabel the JLabel representing the zombie in the GUI
     */
    public void registerZombie(int row, Zombie zombie, JLabel zombieLabel) {
        Thread zombieThread = new Thread(() -> zombieLogic(row, zombie, zombieLabel));

        // almacenamos el hilo recien creado.
        synchronized (zombieThreadsByRow) {
        zombieThreadsByRow.computeIfAbsent(row, k -> new ArrayList<>()).add(zombieThread);
        }

        // almacenamos el JLabel del zombie para poder moverlo o quitarlo
        synchronized (threadToLabelMap) {
            threadToLabelMap.put(zombieThread, zombieLabel);
        }

        // almacenamos el zombie para poder acceder a sus atributos en su respectivo
        // hilo
        synchronized (threadToZombieMap) {
            threadToZombieMap.put(zombieThread, zombie);
        }
        zombieThread.start();

        // Si el zombie es un ECIZombie, iniciamos su hilo de proyectil único
        if (zombie instanceof ECIZombie) {
            Thread projectileThread = new Thread(() -> attackPlantWithProjectile(row, (ECIZombie) zombie, zombieLabel));
            synchronized (zombieToProjectileThreadMap) {
                zombieToProjectileThreadMap.put(zombieThread, projectileThread);
            }
            projectileThread.start();
        }
    }

    /**
     * Retrieves the first zombie in the specified row.
     *
     * This method returns an {@code ArrayList<Object>} containing the following elements:
     * <ul>
     *   <li>The {@code Thread} associated with the first zombie.</li>
     *   <li>The {@code JLabel} representing the zombie's visual component.</li>
     *   <li>The {@code Zombie} object representing the zombie's data.</li>
     * </ul>
     *
     * @param row the row number to search for the first zombie.
     * @return an {@code ArrayList<Object>} containing the first zombie's thread, label, and object;
     *         or an empty list if there are no zombies in the specified row.
     */
    public ArrayList<Object> getFirstZombieInRow(int row) {
        ArrayList<Object> zombieTarget = new ArrayList<>();
        List<Thread> threadsInRow;
        synchronized (zombieThreadsByRow) {
            threadsInRow = zombieThreadsByRow.get(row);
        }
        if (threadsInRow != null && !threadsInRow.isEmpty()) {
            Thread firstThread = threadsInRow.get(0);
            JLabel zombieLabel;
            synchronized (threadToLabelMap) {
                zombieLabel = threadToLabelMap.get(firstThread);
            }
            Zombie zombie;
            synchronized (threadToZombieMap) {
                zombie = threadToZombieMap.get(firstThread);
            }
            zombieTarget.add(firstThread);
            zombieTarget.add(zombieLabel);
            zombieTarget.add(zombie);

         
        }
        return zombieTarget;
    }

    /**
     * Lógica principal del zombie.
     * Solo maneja el movimiento y ataques directos para zombies que no son ECIZombie.
     */
    /**
     * Executes the main logic for a zombie's behavior within the game.
     * <p>
     * This method handles the movement and actions of a zombie based on its type.
     * Regular zombies move towards the nearest plant in their row, attack it when adjacent,
     * and handle interactions with lawnmowers. Special zombies like {@code Brainstein} and
     * {@code ECIZombie} have unique behaviors: {@code Brainstein} generates resources,
     * while {@code ECIZombie} attacks using projectiles managed in separate threads.
     * </p>
     *
     * @param row         the row number where the zombie is located
     * @param zombie      the zombie instance performing actions
     * @param zombieLabel the JLabel representing the zombie in the user interface
     */
    private void zombieLogic(int row, Zombie zombie, JLabel zombieLabel) {
        try {
            while (!Thread.currentThread().isInterrupted()) {

                if (!(zombie instanceof Brainstein)) {

                    // Solo los zombies que no son ECIZombie realizarán ataques directos aquí
                    if (!(zombie instanceof ECIZombie)) {
                        // Buscar la planta más cercana
                        int plantCol = game.getFirstPlantInRow(row);
                        if (plantCol == -1) {
                            // No hay más plantas, mover hasta el final (col=0) si no está ya allí
                            int currentCol = getCurrentColumn(zombieLabel.getX());
                            if (currentCol > 0) {
                                moveZombie(row, 0, zombieLabel);
                            }
                            // Si hay una cortadora de césped en la fila, eliminar zombies y cortadora
                            if (game.getLawnmowerInRow(row)) {
                                game.removeZombiesInRow(row);
                                terminateZombiesInRow(row);
                                garden.deleteLawnmover(row);
                            }
                            // Ya está al final, detener el hilo
                            break;
                        }

                        int targetCol = plantCol + 1;
                        moveZombie(row, targetCol, zombieLabel);
                        // Ahora el zombi está adyacente a la planta. Atacar.
                        attackPlant(row, plantCol, zombie, zombieLabel);
                        // Si la planta murió, se remueve y el loop continúa para buscar la siguiente.
                    }
                    // Los ECIZombies manejan sus ataques con proyectiles en hilos separados
                } else {
                    // Brainstein no ataca, solo genera recursos
                    ((Brainstein) zombie).generateResource(row);
                    // Esperar 2 segundos antes de generar otro recurso
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        return;
                    }
                }
            }
        } finally {
            // Limpieza al terminar el hilo del zombie
            cleanupZombieThread(row, zombieLabel);
        }
    }

    /**
     * Limpia los datos y hilos asociados a un zombie al terminar su ejecución.
     */
    /**
     * Cleans up resources associated with a zombie thread after it has finished its execution.
     * This includes removing the thread from tracking collections, interrupting any associated
     * projectile threads (for ECIZombie types), and removing the zombie's label from the GUI.
     *
     * @param row         the row index where the zombie was located
     * @param zombieLabel the JLabel representing the zombie in the GUI
     */
    private void cleanupZombieThread(int row, JLabel zombieLabel) {
        synchronized (zombieThreadsByRow) {
            List<Thread> threads = zombieThreadsByRow.get(row);
            if (threads != null) {
                threads.remove(Thread.currentThread());
                if (threads.isEmpty()) {
                    zombieThreadsByRow.remove(row);
                }
            }
        }
        synchronized (threadToLabelMap) {
            threadToLabelMap.remove(Thread.currentThread());
        }
        synchronized (threadToZombieMap) {
            threadToZombieMap.remove(Thread.currentThread());
        }
        // Si es un ECIZombie, también removemos su hilo de proyectil
        synchronized (zombieToProjectileThreadMap) {
            Thread projectileThread = zombieToProjectileThreadMap.remove(Thread.currentThread());
            if (projectileThread != null) {
                projectileThread.interrupt();
            }
        }
        SwingUtilities.invokeLater(() -> {
            Container parent = zombieLabel.getParent();
            if (parent != null) {
                parent.remove(zombieLabel);
                parent.revalidate();
                parent.repaint();
            }
        });
    }

    /**
     * Calcula la columna actual del zombie basándose en su posición X.
     */
    /**
     * Calculates the current column index based on the given x-coordinate position.
     *
     * @param xPosition the x-coordinate position
     * @return the current column index corresponding to the xPosition
     */
    private int getCurrentColumn(int xPosition) {
        int cellWidth = 80;
        int gridStartX = 40;
        return Math.max(0, (xPosition - gridStartX) / cellWidth);
    }

    /**
     * Moves a zombie JLabel from its current position to a target column.
     *
     * @param row The row in which the zombie is located.
     * @param targetCol The target column to which the zombie should move.
     * @param zombieLabel The JLabel representing the zombie to be moved.
     */
    private void moveZombie(int row, int targetCol, JLabel zombieLabel) {
        int cellWidth = 80;
        int startX = zombieLabel.getX(); // Posición actual X
        int startY = zombieLabel.getY();
        int endX = 40 + targetCol * cellWidth;

        int currentX = startX;
        int currentY = startY;

        while (currentX > endX) {
            currentX -= 5;
            int finalX = currentX;
            int finalY = currentY;
            SwingUtilities.invokeLater(() -> {
                zombieLabel.setLocation(finalX, finalY);
            });
            try {
                // Espera de 150ms para controlar la velocidad de movimiento
                Thread.sleep(150);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
        }
    }

    /**
     * Attacks a plant at the specified position with the given zombie.
     * 
     * @param row The row position of the plant.
     * @param plantCol The column position of the plant.
     * @param zombie The zombie that will attack the plant.
     * @param zombieLabel The label representing the zombie in the UI.
     */
    private void attackPlant(int row, int plantCol, Zombie zombie, JLabel zombieLabel) {
        // Obtener la planta desde el dominio
        Plant plant = game.getPlantAt(row, plantCol);
        if (plant == null) {
            // Ya no hay planta (otro zombi la mató?), volver
            return;
        }

        // Mientras la planta esté viva, atacarla cada 0.5s
        while (!plant.isDead()) {
            plant.takeDamage(zombie.getDamage());
            if (plant.isDead()) {
                // Planta muerta, remover del dominio y de la interfaz
                Player plantsPlayer = game.getPlayerOne();
                plantsPlayer.setScore(plantsPlayer.getScore() - plant.getCost());
                game.removeEntity(row, plantCol);
                SwingUtilities.invokeLater(() -> {
                    garden.removePlantAt(row, plantCol);
                });
                break;
            }

            // Esperar 0.5 segundos antes del próximo ataque
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
        }
    }

    // método para obtener la posición X de un zombie, dado su hilo.
    /**
     * Retrieves the X position of the zombie associated with the given thread.
     *
     * @param zombieThread the thread associated with the zombie whose X position is to be retrieved
     * @return the X position of the zombie
     * @throws IllegalArgumentException if the zombie thread is not found or has no associated JLabel
     */
    public int getZombieXPosition(Thread zombieThread) {
        JLabel zombieLabel;
        synchronized (threadToLabelMap) {
            zombieLabel = threadToLabelMap.get(zombieThread);
        }
        if (zombieLabel != null) {
            return zombieLabel.getX();
        } else {
            throw new IllegalArgumentException("Zombie thread not found or has no associated JLabel.");
        }
    }

    /**
     * Terminates the specified zombie thread by interrupting it.
     *
     * @param thread the thread representing the zombie to be terminated
     */
    public void terminateZombie(Thread thread) {
        thread.interrupt();
    }


    /**
     * Notifies the game that a zombie has reached the house in the specified row.
     * If there is a lawnmower in the row, it removes all zombies in that row and deletes the lawnmower.
     * Otherwise, it calculates the final scores and ends the game with a message indicating that the zombies have won.
     *
     * @param row the row in which a zombie has reached the house
     */
    private void notifyZombieReachedHouse(int row) {
        if (game.getLawnmowerInRow(row)) {
            game.removeZombiesInRow(row);
            terminateZombiesInRow(row);
            garden.deleteLawnmover(row);
        } else {
            game.calculateScores(); // Calcula puntajes finales
            String winnerMessage = "¡Los zombies han ganado! Llegaron a la casa.";
            game.endGame(winnerMessage); // Finaliza el juego
        }
    }
    
     /**
     * Método que maneja el ataque con proyectiles de un ECIZombie.
     * Este método corre en un hilo separado y asegura que solo haya un proyectil
     * activo por ECIZombie.
     */
    /**
     * Attacks the first plant in the specified row with a projectile from the given zombie.
     * The method continuously checks for the presence of plants in the row and fires projectiles
     * at them until the zombie is interrupted or its health drops to zero.
     *
     * @param row The row in which to attack the plant.
     * @param zombie The zombie that is attacking the plant.
     * @param zombieLabel The JLabel representing the zombie in the graphical interface.
     */
    private void attackPlantWithProjectile(int row, ECIZombie zombie, JLabel zombieLabel) {
        try {
            while (!Thread.currentThread().isInterrupted() && zombie.getHealth() > 0) {
                int plantCol = game.getFirstPlantInRow(row);
                int cellWidth = 80;
                int targetCol = plantCol + 1;
                int endX = 40 + targetCol * cellWidth;
                if (plantCol == -1) {
                    // No hay plantas en la fila, esperar antes de intentar de nuevo
                    Thread.sleep(1000);
                    continue;
                }

                Plant targetPlant = game.getPlantAt(row, plantCol);
                if (targetPlant == null || targetPlant.isDead()) {
                    // La planta ya no existe, esperar antes de intentar de nuevo
                    Thread.sleep(1000);
                    continue;
                }

                // Obtener la posición actual del ECIZombie
                int startX = zombieLabel.getX();
                int startY = zombieLabel.getY();

                // Obtener la posición de la planta
                // int targetX = getPlantGraphicXPosition(row, plantCol);
                // int targetY = getPlantGraphicYPosition(row, plantCol);

                int targetX = endX;
                int targetY = plantCol * cellWidth;

                // Crear y mostrar el proyectil
                JLabel projectileLabel = createProjectileLabel(startX, startY);

                // Mover el proyectil hacia la planta
                boolean hit = moveProjectile(projectileLabel, startX, targetX, startY, targetY);

                // Eliminar el proyectil de la interfaz gráfica
                SwingUtilities.invokeLater(() -> {
                    Container parent = projectileLabel.getParent();
                    if (parent != null) {
                        parent.remove(projectileLabel);
                        parent.revalidate();
                        parent.repaint();
                    }
                });

                if (hit) {
                    // Aplicar daño al impactar
                    targetPlant.takeDamage(zombie.getDamage());

                    if (targetPlant.isDead()) {
                        game.removeEntity(row, plantCol);
                        SwingUtilities.invokeLater(() -> {
                            garden.removePlantAt(row, plantCol);
                        });
                    }
                }

                // Esperar antes de disparar el siguiente proyectil
                Thread.sleep(1000); // Ajusta este valor según la velocidad de disparo deseada
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Crea un JLabel para el proyectil y lo agrega a la interfaz gráfica.
     */
    /**
     * Creates a JLabel representing a projectile with a specified starting position.
     * The projectile is represented by an image icon which is scaled to a specific size.
     * The JLabel is then added to the garden's content pane and its Z-order is set.
     *
     * @param startX the starting X coordinate of the projectile
     * @param startY the starting Y coordinate of the projectile
     * @return the JLabel representing the projectile
     */
    private JLabel createProjectileLabel(int startX, int startY) {
        ImageIcon icon = new ImageIcon("resources/images/blackPea.png");
        Image scaledImage = icon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
        ImageIcon scaledIcon = new ImageIcon(scaledImage);

        JLabel projectileLabel = new JLabel(scaledIcon);
        projectileLabel.setSize(30, 30);
        projectileLabel.setLocation(startX, startY);

        SwingUtilities.invokeLater(() -> {
            garden.getContentPane().add(projectileLabel);
            garden.getContentPane().setComponentZOrder(projectileLabel, 0);
            garden.getContentPane().revalidate();
            garden.getContentPane().repaint();
        });

        return projectileLabel;
    }

    /**
     * Mueve el proyectil desde la posición inicial hasta la posición objetivo.
     * Retorna true si el proyectil alcanzó la planta, false si fue interrumpido.
     */
    /**
     * Moves a projectile from a starting position to a target position.
     *
     * @param projectileLabel the JLabel representing the projectile
     * @param startX the starting X coordinate of the projectile
     * @param targetX the target X coordinate of the projectile
     * @param startY the starting Y coordinate of the projectile
     * @param targetY the target Y coordinate of the projectile
     * @return true if the projectile reached the target position, false if the thread was interrupted
     */
    private boolean moveProjectile(
        JLabel projectileLabel,
        int startX,
        int targetX,
        int startY,
        int targetY
    ) {
        int currentX = startX;
        int currentY = startY;

        int deltaX = targetX - startX;
        int deltaY = targetY - startY;
        double distance = Math.sqrt(deltaX * deltaX + deltaY * deltaY);
        double steps = distance / 5; // Ajusta la velocidad del proyectil aquí
        double stepX = deltaX / steps;
        double stepY = deltaY / steps;

        for (int i = 0; i < steps; i++) {
            if (Thread.currentThread().isInterrupted()) {
                return false;
            }

            currentX += stepX;
            currentY += stepY;
            int finalX = (int) currentX;
            int finalY = (int) currentY;
            // Puedes eliminar o comentar el siguiente System.err si no lo necesitas
            // System.err.println("Proyectil: " + finalX + " " + finalY);  

            SwingUtilities.invokeLater(() -> {
                projectileLabel.setLocation(finalX, finalY);
            });

            try {
                Thread.sleep(10); // Ajusta la suavidad del movimiento aquí
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return false;
            }
        }

        // El proyectil alcanzó la planta
        return true;
    }
    


}