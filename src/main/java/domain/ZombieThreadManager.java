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

public class ZombieThreadManager {

    private POOBvsZombies game;
    private GardenMenu garden;

    // Almacenamos los hilos según su fila
    private Map<Integer, List<Thread>> zombieThreadsByRow = new HashMap<>();
    // Almacenamos los JLabels de los zombies para poder moverlos o quitarlos
    private Map<Thread, JLabel> threadToLabelMap = new HashMap<>();
    // Almacenamos los zombies para poder acceder a ellos después y a sus atributos
    private Map<Thread, Zombie> threadToZombieMap = new HashMap<>();
    // Nueva estructura para rastrear los hilos de proyectiles por ECIZombie
    private Map<Thread, Thread> zombieToProjectileThreadMap = new HashMap<>();

    public ZombieThreadManager(POOBvsZombies game, GardenMenu garden) {
        this.game = game;
        this.garden = garden;
    }

    /**
     * Registra un zombie en la fila especificada.
     * Si el zombie es una instancia de ECIZombie, inicia un hilo de proyectil único.
     */
    public void registerZombie(int row, Zombie zombie, JLabel zombieLabel) {
        Thread zombieThread = new Thread(() -> zombieLogic(row, zombie, zombieLabel));
        
        // Almacenamos el hilo recién creado.
        synchronized (zombieThreadsByRow) {
            zombieThreadsByRow.computeIfAbsent(row, k -> new ArrayList<>()).add(zombieThread);
        }
        // Almacenamos el JLabel del zombie para poder moverlo o quitarlo
        synchronized (threadToLabelMap) {
            threadToLabelMap.put(zombieThread, zombieLabel);
        }
        // Almacenamos el zombie para poder acceder a sus atributos en su respectivo hilo
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
     * Lógica principal del zombie.
     * Solo maneja el movimiento y ataques directos para zombies que no son ECIZombie.
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
     * Termina todos los zombies y sus proyectiles en una fila específica.
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
     * Calcula la columna actual del zombie basándose en su posición X.
     */
    private int getCurrentColumn(int xPosition) {
        int cellWidth = 80;
        int gridStartX = 40;
        return Math.max(0, (xPosition - gridStartX) / cellWidth);
    }

    /**
     * Mueve el zombie hacia la columna objetivo.
     */
    private void moveZombie(int row, int targetCol, JLabel zombieLabel) {
        int cellWidth = 80;
        int startX = zombieLabel.getX(); // Posición actual X
        int startY = zombieLabel.getY();
        int endX = 40 + targetCol * cellWidth;

        int currentX = startX;
        int currentY = startY;

        while (currentX > endX && !Thread.currentThread().isInterrupted()) {
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
     * Ataca una planta específica hasta que muera o el zombie sea destruido.
     */
    private void attackPlant(int row, int plantCol, Zombie zombie, JLabel zombieLabel) {
        // Obtener la planta desde el dominio
        Plant plant = game.getPlantAt(row, plantCol);
        if (plant == null) {
            // Ya no hay planta (otro zombi la mató?), volver
            return;
        }

        // Mientras la planta y el zombie estén vivos, atacar
        while (!plant.isDead() && zombie.getHealth() > 0 && !Thread.currentThread().isInterrupted()) {
            plant.takeDamage(zombie.getDamage());
            if (plant.isDead()) {
                // Planta muerta, remover del dominio y de la interfaz
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

    /**
     * Método que maneja el ataque con proyectiles de un ECIZombie.
     * Este método corre en un hilo separado y asegura que solo haya un proyectil
     * activo por ECIZombie.
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

    /**
     * Obtiene la posición X gráfica de la planta.
     * Debes implementar este método para que retorne la posición correcta.
     */
    // private int getPlantGraphicXPosition(int row, int col) {
    //     JLabel plantLabel = garden.getPlantLabelAt(row, col);
    //     return plantLabel != null ? plantLabel.getX() : 0;
    // }

    // /**
    //  * Obtiene la posición Y gráfica de la planta.
    //  * Debes implementar este método para que retorne la posición correcta.
    //  */
    // private int getPlantGraphicYPosition(int row, int col) {
    //     JLabel plantLabel = garden.getPlantLabelAt(row, col);
    //     return plantLabel != null ? plantLabel.getY() : 0;
    // }

    /**
     * Obtiene el primer zombie en una fila específica.
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
     * Método para obtener la posición X de un zombie, dado su hilo.
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
     * Termina un zombie específico.
     */
    public void terminateZombie(Thread thread) {
        thread.interrupt();
    }
}
