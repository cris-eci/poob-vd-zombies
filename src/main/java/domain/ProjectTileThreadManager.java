
package domain;

import java.awt.Container;
import java.awt.Image;
import java.awt.MediaTracker;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;

import presentation.GardenMenu;

public class ProjectTileThreadManager {
    private POOBvsZombies game;
    private GardenMenu garden;
    private ZombieThreadManager zombieThreadManager;
    private JLabel projectTileLabel;

    public ProjectTileThreadManager(POOBvsZombies game, GardenMenu garden, ZombieThreadManager zombieThreadManager) {
        this.game = game;
        this.garden = garden;
        this.zombieThreadManager = zombieThreadManager;
    }

    public void registerProjectTile(int row, int yPos, ProjectTile projectTile, int graphicXPosition, int graficYPosition) {
        // Iniciar el hilo que gestionará la lógica del disparo
        //this.projectTileLabel = createAndRegisterProjectTile(row, yPos);
        Thread t = new Thread(() -> projectTileLogic(row, yPos, projectTile, graphicXPosition, graficYPosition));
        t.start();
    }

    private void projectTileLogic(int row, int yPos, ProjectTile projectTile, int graphicXPosition, int graficYPosition) {
        while (true) {
            ArrayList<Object> firstZombie = zombieThreadManager.getFirstZombieInRow(row);

            if (firstZombie.isEmpty()) {
                // No hay zombies, esperar un segundo y volver a chequear
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                }
                continue;
            }

            // Si sigue vivo, esperar 3 segundos y volver a disparar
            if (game.getEntity(row, yPos) == null) {
                Thread.currentThread().interrupt();
                return;
            }
            Thread targetZombieThread = (Thread) firstZombie.get(0);
            JLabel targetZombieLabel = (JLabel) firstZombie.get(1);
            Zombie targetZombie = (Zombie) firstZombie.get(2);

            

            // Mientras el zombie esté vivo, disparar
            while (targetZombie.health > 0) {
                // Crear un nuevo proyectil cada vez que vamos a disparar
                JLabel projectTileLabel = this.projectTileLabel = createAndRegisterProjectTile(graphicXPosition,graficYPosition, targetZombieLabel);
                // JLabel projectTileLabel = createAndRegisterProjectTile(row, yPos);

                int originXpos = graphicXPosition;
                //int originYpos = graficYPosition;
                int targetXpos = targetZombieLabel.getX();

                // Mover el proyectil hacia el zombie
                // moveProjectTile(originXpos, targetXpos, projectTileLabel);
                moveProjectTile(originXpos + 95, targetXpos, projectTileLabel, targetZombie, targetZombieThread);

                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                }
            }

        }
    }

    private JLabel createAndRegisterProjectTile(int row, int yPos, JLabel zombieLabel) {
        ImageIcon icon = new ImageIcon("resources/images/pea.png");
        if (icon.getImageLoadStatus() != MediaTracker.COMPLETE) {
            System.err.println("Failed to load image: /resources/images/pea.png");
        }
        Image scaledImage = icon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
        ImageIcon scaledIcon = new ImageIcon(scaledImage);
    
        JLabel projectTileLabel = new JLabel(scaledIcon);
    
        //int originXPos = calculateOriginXPos(yPos);
        //projectTileLabel.setSize(30, 30);
         // Set the initial position of the projectile (Peashooter's position)
        projectTileLabel.setBounds(row+95, yPos+95, 30, 30);
    
        SwingUtilities.invokeLater(() -> {
            Container parent = zombieLabel.getParent();
            if (parent != null) {
                parent.setLayout(null); // Ensure absolute positioning
                //projectTileLabel.setBounds(row+600, yPos+95, 30, 30);
                parent.add(projectTileLabel);
                parent.setComponentZOrder(projectTileLabel, 0); // Bring to front if necessary
                parent.revalidate();
                parent.repaint();
            }
        });
        return projectTileLabel;
    }

    private void moveProjectTile(int originXpos, int targetXpos, JLabel projectTileLabel, Zombie targetZombie, Thread targetZombieThread) {
        int currentX = originXpos;
    
        while (currentX < targetXpos - 30) { // Subtract projectile width to stop before overlapping
            currentX += 5; // Move speed
            int finalX = currentX;
    
            SwingUtilities.invokeLater(() -> {
                projectTileLabel.setLocation(finalX, projectTileLabel.getY());
            });
    
            try {
                Thread.sleep(30); // Adjust for movement smoothness
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }   
        }
    
        // Upon impact
        targetZombie.takeDamage(Peashooter.DAMAGE);
    
        // Remove projectile from GUI
        SwingUtilities.invokeLater(() -> {
            Container parent = projectTileLabel.getParent();
            if (parent != null) {
                parent.remove(projectTileLabel);
                parent.revalidate();
                parent.repaint();
            }
        });
    
        // Check if zombie is dead
        if (targetZombie.getHealth() <= 0) {
            zombieThreadManager.terminateZombie(targetZombieThread);
        }
    }
}