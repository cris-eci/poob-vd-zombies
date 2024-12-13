package domain;

import java.awt.Container;
import java.awt.Image;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import presentation.GardenMenu;

public class ZombieThreadManager {

    private POOBvsZombies game;
    private GardenMenu garden;

    // Mapas para administrar hilos y labels
    private Map<Integer, List<Thread>> zombieThreadsByRow = new HashMap<>();
    private Map<Thread, JLabel> threadToLabelMap = new HashMap<>();
    private Map<Thread, Zombie> threadToZombieMap = new HashMap<>();
    private Map<Thread, Thread> zombieToProjectileThreadMap = new HashMap<>();

    public ZombieThreadManager(POOBvsZombies game, GardenMenu garden) {
        this.game = game;
        this.garden = garden;
    }

    public void registerZombie(int row, Zombie zombie, JLabel zombieLabel) {
        Thread zombieThread = new Thread(() -> zombieLogic(row, zombie, zombieLabel));

        synchronized (zombieThreadsByRow) {
            zombieThreadsByRow.computeIfAbsent(row, k -> new ArrayList<>()).add(zombieThread);
        }
        synchronized (threadToLabelMap) {
            threadToLabelMap.put(zombieThread, zombieLabel);
        }
        synchronized (threadToZombieMap) {
            threadToZombieMap.put(zombieThread, zombie);
        }

        zombieThread.start();

        // Si es ECIZombie, iniciar hilo de proyectil
        if (zombie instanceof ECIZombie) {
            Thread projectileThread = new Thread(() -> attackPlantWithProjectile(row, (ECIZombie) zombie, zombieLabel));
            synchronized (zombieToProjectileThreadMap) {
                zombieToProjectileThreadMap.put(zombieThread, projectileThread);
            }
            projectileThread.start();
        }
    }

    private void zombieLogic(int row, Zombie zombie, JLabel zombieLabel) {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                if (!(zombie instanceof Brainstein)) {
                    // Para cualquier zombie (incluyendo ECIZombie) excepto Brainstein:
                    int plantCol = game.getFirstPlantInRow(row);
                    if (plantCol == -1) {
                        // No hay plantas
                        int currentCol = getCurrentColumn(zombieLabel.getX());
                        if (currentCol > 0) {
                            // Mover hasta la col 0
                            moveZombie(row, 0, zombieLabel);
                        }
                        if (game.getLawnmowerInRow(row)) {
                            game.removeZombiesInRow(row);
                            terminateZombiesInRow(row);
                            garden.deleteLawnmover(row);
                        }
                        break; // Sin plantas, ya en la col 0 o muerto
                    }

                    // Hay planta, moverse a plantCol+1
                    int targetCol = plantCol + 1;
                    moveZombie(row, targetCol, zombieLabel);

                    // Si NO es ECIZombie, ataca directamente
                    if (!(zombie instanceof ECIZombie)) {
                        attackPlant(row, plantCol, zombie, zombieLabel);
                    }
                    // Si es ECIZombie, no atacamos directamente, el proyectil se encarga.
                    // El ECIZombie seguirá disparando desde su hilo de proyectil.
                } else {
                    // Brainstein genera recursos, no se mueve ni ataca
                    ((Brainstein) zombie).generateResource(row);
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        return;
                    }
                }
            }
        } finally {
            cleanupZombieThread(row, zombieLabel);
        }
    }

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

    public void terminateZombiesInRow(int row) {
        List<Thread> threads;
        synchronized (zombieThreadsByRow) {
            threads = zombieThreadsByRow.remove(row);
        }
        if (threads != null) {
            for (Thread thread : threads) {
                thread.interrupt();
                JLabel zombieLabel = threadToLabelMap.remove(thread);
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

    private int getCurrentColumn(int xPosition) {
        int cellWidth = 80;
        int gridStartX = 40;
        return Math.max(0, (xPosition - gridStartX) / cellWidth);
    }

    private void moveZombie(int row, int targetCol, JLabel zombieLabel) {
        int cellWidth = 80;
        int startX = zombieLabel.getX();
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
                Thread.sleep(150);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
        }
    }

    private void attackPlant(int row, int plantCol, Zombie zombie, JLabel zombieLabel) {
        Plant plant = game.getPlantAt(row, plantCol);
        if (plant == null) {
            return;
        }

        while (!plant.isDead() && zombie.getHealth() > 0 && !Thread.currentThread().isInterrupted()) {
            plant.takeDamage(zombie.getDamage());
            if (plant.isDead()) {
                game.removeEntity(row, plantCol);
                SwingUtilities.invokeLater(() -> {
                    garden.removePlantAt(row, plantCol);
                });
                break;
            }
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
        }
    }
private void attackPlantWithProjectile(int row, ECIZombie zombie, JLabel zombieLabel) {
    try {
        JPanel mainPanel = garden.getMainPanel(); // Debes crear un getter en GardenMenu que retorne el panel principal
        while (!Thread.currentThread().isInterrupted() && zombie.getHealth() > 0) {
            int plantCol = game.getFirstPlantInRow(row);
            if (plantCol == -1) {
                Thread.sleep(1000);
                continue;
            }

            Plant targetPlant = game.getPlantAt(row, plantCol);
            if (targetPlant == null || targetPlant.isDead()) {
                Thread.sleep(1000);
                continue;
            }

            // Obtener la etiqueta de la planta
            JLabel plantLabel = garden.getPlantLabelAt(row, plantCol);
            if (plantLabel == null) {
                Thread.sleep(1000);
                continue;
            }

            // Convertir posiciones al mismo sistema de coordenadas (el del panel principal)
            // Posición absoluta del zombie en panel:
            java.awt.Point zombiePos = SwingUtilities.convertPoint(zombieLabel.getParent(), 
                                                                   zombieLabel.getLocation(), 
                                                                   mainPanel);

            // Posición absoluta de la planta en panel:
            java.awt.Point plantPos = SwingUtilities.convertPoint(plantLabel.getParent(), 
                                                                  plantLabel.getLocation(), 
                                                                  mainPanel);

            int zombieCenterX = zombiePos.x + zombieLabel.getWidth()/2;
            int zombieCenterY = zombiePos.y + zombieLabel.getHeight()/2;
            int plantCenterX = plantPos.x + plantLabel.getWidth()/2;
            int plantCenterY = plantPos.y + plantLabel.getHeight()/2;

            // Crear y mostrar el proyectil en el mismo panel que zombie y planta
            JLabel projectileLabel = createProjectileLabel(mainPanel, zombieCenterX - 15, zombieCenterY - 15);

            // Mover el proyectil hacia la planta
            boolean hit = moveProjectile(projectileLabel, zombieCenterX, plantCenterX, zombieCenterY, plantCenterY);

            // Eliminar el proyectil de la interfaz
            SwingUtilities.invokeLater(() -> {
                Container parent = projectileLabel.getParent();
                if (parent != null) {
                    parent.remove(projectileLabel);
                    parent.revalidate();
                    parent.repaint();
                }
            });

            if (hit) {
                targetPlant.takeDamage(ECIZombie.DAMAGE);
                if (targetPlant.isDead()) {
                    game.removeEntity(row, plantCol);
                    SwingUtilities.invokeLater(() -> {
                        garden.removePlantAt(row, plantCol);
                    });
                }
            }

            Thread.sleep(3000);
        }
    } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
    }
}

private JLabel createProjectileLabel(JPanel mainPanel, int startX, int startY) {
    ImageIcon icon = new ImageIcon("resources/images/blackPea.png");
    Image scaledImage = icon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
    ImageIcon scaledIcon = new ImageIcon(scaledImage);

    JLabel projectileLabel = new JLabel(scaledIcon);
    projectileLabel.setSize(30, 30);
    projectileLabel.setLocation(startX, startY);

    SwingUtilities.invokeLater(() -> {
        mainPanel.add(projectileLabel);
        mainPanel.setComponentZOrder(projectileLabel, 0);
        mainPanel.revalidate();
        mainPanel.repaint();
    });

    return projectileLabel;
}
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
        double steps = distance / 5;
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

            SwingUtilities.invokeLater(() -> {
                projectileLabel.setLocation(finalX, finalY);
            });

            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return false;
            }
        }

        // Impacto con la planta
        return true;
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

}
