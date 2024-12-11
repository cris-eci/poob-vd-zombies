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

    // Almacenamos los hilos segun su columna
    private Map<Integer, List<Thread>> zombieThreadsByRow = new HashMap<>();
    // Almacenamos los JLabels de los zombies para poder moverlos o quitarlos
    private Map<Thread, JLabel> threadToLabelMap = new HashMap<>();
    // Almacenamos los zombies para poder acceder a ellos despues y a sus atributos
    private Map<Thread, Zombie> threadToZombieMap = new HashMap<>();

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
            }
        }
    }

    public ZombieThreadManager(POOBvsZombies game, GardenMenu garden) {
        this.game = game;
        this.garden = garden;
    }

    public void registerZombie(int row, Zombie zombie, JLabel zombieLabel) {
        Thread t = new Thread(() -> zombieLogic(row, zombie, zombieLabel));
        // almacenamos el hilo recien creado.
        synchronized (zombieThreadsByRow) {
            zombieThreadsByRow.computeIfAbsent(row, k -> new ArrayList<>()).add(t);
        }
        // almacenamos el JLabel del zombie para poder moverlo o quitarlo
        synchronized (threadToLabelMap) {
            threadToLabelMap.put(t, zombieLabel);
        }
        // almacenamos el zombie para poder acceder a sus atributos en su respectivo
        // hilo
        synchronized (threadToZombieMap) {
            threadToZombieMap.put(t, zombie);
        }
        t.start();
    }

    private void zombieLogic(int row, Zombie zombie, JLabel zombieLabel) {
        try {
            while (!Thread.currentThread().isInterrupted()) {


                if (!(zombie instanceof Brainstein)) {
                    
                    // if (zombie instanceof ECIZombie) {
                    //     // Start attacking immediately
                    //     attackPlantWithProjectile(row,(ECIZombie) zombie, zombieLabel);
                    //}

                    // Buscar la planta más cercana
                    int plantCol = game.getFirstPlantInRow(row);
                    if (plantCol == -1) {
                        // No hay más plantas, mover hasta el final (col=0) si no está ya allí
                        int currentCol = getCurrentColumn(zombieLabel.getX());
                        if (currentCol > 0) {
                            moveZombie(row, 0, zombieLabel);
                        }
                        // Si es el primer zombi de la fila, activar la cortadora de césped y hacer que
                        // se elimine la podadora.But it's me that I get to die with me. What you do?
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
                    // If the zombie is supposed to move, ensure it's alive
                    if (zombie instanceof ECIZombie) {
                        attackPlantWithProjectile(row,(ECIZombie) zombie, zombieLabel);
                    } else {
                        // Ahora el zombi está adyacente a la planta. Atacar.
                        attackPlant(row, plantCol, zombie, zombieLabel);
                    }
                    // Si la planta murió, se remueve y el loop continúa para buscar la siguiente.
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
            // Cleanup for this zombie
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
            SwingUtilities.invokeLater(() -> {
                Container parent = zombieLabel.getParent();
                if (parent != null) {
                    parent.remove(zombieLabel);
                    parent.revalidate();
                    parent.repaint();
                }
            });
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
                //System.err.println( "Zombie: " + finalX + " " + finalY);
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

    private void attackPlant(int row, int plantCol, Zombie zombie, JLabel zombieLabel) {
        // Obtener la planta desde el dominio
        Plant plant = game.getPlantAt(row, plantCol);
        if (plant == null) {
            // Ya no hay planta (otro zombi la mató?), volver
            return;
        }

        // While both the plant and zombie are alive, attack
        // Mientras la planta esté viva, atacarla cada 0.5s
        while (!plant.isDead() && zombie.getHealth() > 0) {
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

    // método para obtener la posición X de un zombie, dado su hilo.
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

    public void terminateZombie(Thread thread) {
        thread.interrupt();
    }

    // private void attackPlantWithProjectile(int row, int plantCol, ECIZombie zombie, JLabel zombieLabel) {
    //     Plant targetPlant = game.getPlantAt(row, plantCol);
    //     if (targetPlant == null) {
    //         return;
    //     }

    //     // Create a thread for the projectile attack
    //     Thread projectileThread = new Thread(() -> {
    //         try {
    //             while (!Thread.currentThread().isInterrupted() && zombie.getHealth() > 0 && !targetPlant.isDead()) {
    //                 // Create the projectile
    //                 JLabel projectileLabel = createProjectileLabel(zombieLabel.getX(), zombieLabel.getY());

    //                 // Move the projectile towards the plant
    //                 moveProjectile(projectileLabel, zombieLabel.getX(),/*  targetPlant.getGraphicXPosition()*/10);
                    
    //                 // Apply damage upon impact
    //                 targetPlant.takeDamage(zombie.getDamage());

    //                 // Remove projectile from GUI
    //                 SwingUtilities.invokeLater(() -> {
    //                     Container parent = projectileLabel.getParent();
    //                     if (parent != null) {
    //                         parent.remove(projectileLabel);
    //                         parent.revalidate();
    //                         parent.repaint();
    //                     }
    //                 });

    //                 if (targetPlant.isDead()) {
    //                     game.removeEntity(row, plantCol);
    //                     SwingUtilities.invokeLater(() -> {
    //                         garden.removePlantAt(row, plantCol);
    //                     });
    //                 }

    //                 // Wait before next attack
    //                 Thread.sleep(2000);
    //             }
    //         } catch (InterruptedException e) {
    //             Thread.currentThread().interrupt();
    //         }
    //     });

    //     projectileThread.start();
    // }

    private void attackPlantWithProjectile(int row, ECIZombie zombie, JLabel zombieLabel) {
        Thread projectileThread = new Thread(() -> {
            try {
                while (!Thread.currentThread().isInterrupted() && zombie.getHealth() > 0) {
                    int plantCol = game.getFirstPlantInRow(row);
                    if (plantCol == -1) {
                        // No plants in the row
                        Thread.sleep(1000);
                        continue;
                    }
    
                    Plant targetPlant = game.getPlantAt(row, plantCol);
                    if (targetPlant == null || targetPlant.isDead()) {
                        // Plant is dead or doesn't exist
                        Thread.sleep(1000);
                        continue;
                    }
    
                    // Get current positions
                    int startX = zombieLabel.getX();
                    int startY = zombieLabel.getY();
                    //int targetX = getPlantGraphicXPosition(row, plantCol);
                    //int targetY = getPlantGraphicYPosition(row, plantCol);

                    int targetX = 10;
    
                    int targetY = 10;
                    // Create and display the projectile
                    JLabel projectileLabel = createProjectileLabel(startX, startY);
    
                    // Move the projectile towards the plant
                    boolean hit = moveProjectile(projectileLabel, startX, targetX, startY, targetY);
    
                    // Remove projectile from GUI
                    SwingUtilities.invokeLater(() -> {
                        Container parent = projectileLabel.getParent();
                        if (parent != null) {
                            parent.remove(projectileLabel);
                            parent.revalidate();
                            parent.repaint();
                        }
                    });
    
                    if (hit) {
                        // Apply damage upon impact
                        targetPlant.takeDamage(zombie.getDamage());
    
                        if (targetPlant.isDead()) {
                            game.removeEntity(row, plantCol);
                            SwingUtilities.invokeLater(() -> {
                                garden.removePlantAt(row, plantCol);
                            });
                        }
                    }
    
                    // Wait before shooting again
                    Thread.sleep(2000); // Adjust delay as needed
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
            // Start the projectile thread
    projectileThread.start();
    }

    private JLabel createProjectileLabel(int startX, int startY) {
        ImageIcon icon = new ImageIcon("resources/images/pea.png");
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

    // private void moveProjectile(JLabel projectileLabel, int startX, int targetX) {
    //     int currentX = startX;

    //     while (currentX > targetX) {
    //         currentX -= 5; // Adjust speed as needed
    //         int finalX = currentX;
    //         SwingUtilities.invokeLater(() -> {
    //             projectileLabel.setLocation(finalX, projectileLabel.getY());
    //         });

    //         try {
    //             Thread.sleep(30); // Adjust for smoothness
    //         } catch (InterruptedException e) {
    //             Thread.currentThread().interrupt();
    //             return;
    //         }
    //     }
    // }
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
    double steps = distance / 5; // Adjust speed as needed
    double stepX = deltaX / steps;
    double stepY = deltaY / steps;

    for (int i = 0; i < steps; i++) {
        currentX += stepX;
        currentY += stepY;
        int finalX = (int) currentX;
        int finalY = (int) currentY;
        System.err.println("Proyectil: " + finalX + " " + finalY);  

        SwingUtilities.invokeLater(() -> {
            projectileLabel.setLocation(finalX, finalY);
        });

        try {
            Thread.sleep(30); // Adjust for smoothness
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return false;
        }
    }

    // Projectile reached the target
    return true;
}

    // public int getPlantGraphicXPosition(int row, int col) {
    //     // Implement this to return the X position of the plant's JLabel
    //     // This might involve accessing the plant's JLabel in the garden
    //     JLabel plantLabel = garden.getPlantLabelAt(row, col);
    //     return plantLabel != null ? plantLabel.getX() : 0;
    // }

}
