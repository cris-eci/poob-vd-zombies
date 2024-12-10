package domain;

import java.awt.Container;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

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

    public void registerProjectTile(int row, int yPos, ProjectTile projectTile) {
        createAndRegisterProjectTile(row, yPos);
        Thread t = new Thread(() -> projectTileLogic(row, yPos, projectTile));
        t.start();
    }

    // old
    // private void projectTileLogic(int row, int yPos, ProjectTile projectTile,
    // JLabel projectTileLabel) {
    // while (true) {
    // ArrayList<Object> getFirstZombieInRow =
    // zombieThreadManager.getFirstZombieInRow(row);
    // // obtenemos el hilo del zombie en la cabeza de la cola
    // Thread targetZombieThread = (Thread) getFirstZombieInRow.get(0);
    // // obtenemos el JLabel del zombie
    // JLabel targetZombieLabel = (JLabel) getFirstZombieInRow.get(1);
    // // obtenemos el zombie en la cabeza de la cola
    // Zombie targetZombie = (Zombie) getFirstZombieInRow.get(2);

    // while (targetZombie.health > 0) {

    // int targetXpos = targetZombieLabel.getX();
    // moveProjectTile(row, targetXpos, projectTileLabel);
    // targetZombie.takeDamage(Peashooter.DAMAGE);
    // // mover el proyectil hacia el zombie
    // // si el proyectil llega al zombie, restarle vida al zombie
    // // si el zombie muere, eliminar el proyectil
    // }
    // ;
    // }
    // }

    private void projectTileLogic(int row, int yPos, ProjectTile projectTile) {
        while (true) {
            ArrayList<Object> getFirstZombieInRow = zombieThreadManager.getFirstZombieInRow(row);

            // Verificar si hay zombis en la fila
            if (getFirstZombieInRow.isEmpty()) {
                // No hay zombis en la fila, esperar un tiempo antes de verificar nuevamente
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
                continue;
            }

            // Obtener los elementos del ArrayList<Object>
            Thread targetZombieThread = (Thread) getFirstZombieInRow.get(0);
            JLabel targetZombieLabel = (JLabel) getFirstZombieInRow.get(1);
            Zombie targetZombie = (Zombie) getFirstZombieInRow.get(2);

            while (targetZombie.health > 0) {
                int originXpos = projectTileLabel.getX();
                int targetXpos = targetZombieLabel.getX();

                // Mover el proyectil hacia el zombi
                moveProjectTile(originXpos, targetXpos, projectTileLabel);

                // Restar vida al zombi
                targetZombie.takeDamage(Peashooter.DAMAGE);

                // Esperar 3 segundos antes de disparar nuevamente
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }

                // Verificar si el zombi ha muerto
                if (targetZombie.health <= 0) {
                    // Eliminar el proyectil de la interfaz gráfica
                    SwingUtilities.invokeLater(() -> {
                        Container parent = projectTileLabel.getParent();
                        if (parent != null) {
                            parent.remove(projectTileLabel);
                            parent.revalidate();
                            parent.repaint();
                        }
                    });
                    break;
                }
            }
        }
    }

    // private void moveProjectTile(int originxPos, int targetXpos, JLabel projectTileLabel) {
    //     int delay = 50; // Intervalo de tiempo en milisegundos para cada paso
    //     final int step = 5; // Cantidad de píxeles que se moverá en cada paso
    //     int currentX = originxPos;

    //     Timer timer = new Timer(delay, null);
    //     timer.addActionListener(new ActionListener() {
    //         @Override
    //         public void actionPerformed(ActionEvent e) {
    //             if (currentX < targetXpos) {
    //                 currentX += step;
    //                 projectTileLabel.setLocation(currentX, projectTileLabel.getY());
    //             } else {
    //                 timer.stop();
    //                 // Reiniciar la posición del proyectil después de 3 segundos
    //                 Timer resetTimer = new Timer(3000, new ActionListener() {
    //                     @Override
    //                     public void actionPerformed(ActionEvent e) {
    //                         projectTileLabel.setLocation(originxPos, projectTileLabel.getY());
    //                         ((Timer) e.getSource()).stop();
    //                         // Reiniciar la animación si es necesario
    //                         moveProjectTile(originxPos, targetXpos, projectTileLabel);
    //                     }
    //                 });
    //                 resetTimer.setRepeats(false);
    //                 resetTimer.start();
    //             }
    //         }
    //     });
    //     timer.start();
    // }

    private void moveProjectTile(int originxPos, int targetXpos, JLabel projectTileLabel) {
        int delay = 50; // Intervalo de tiempo en milisegundos para cada paso
        final int step = 5; // Cantidad de píxeles que se moverá en cada paso
        final int[] currentX = {originxPos};
    
        Timer timer = new Timer(delay, null);
        timer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (currentX[0] < targetXpos) {
                    currentX[0] += step;
                    projectTileLabel.setLocation(currentX[0], projectTileLabel.getY());
                } else {
                    timer.stop();
                    // Reiniciar la posición del proyectil después de 3 segundos
                    Timer resetTimer = new Timer(3000, new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            projectTileLabel.setLocation(originxPos, projectTileLabel.getY());
                            ((Timer) e.getSource()).stop();
                            // Reiniciar la animación si es necesario
                            moveProjectTile(originxPos, targetXpos, projectTileLabel);
                        }
                    });
                    resetTimer.setRepeats(false);
                    resetTimer.start();
                }
            }
        });
        timer.start();
    }
    // public void createAndRegisterProjectTile(int row, int yPos, String projectTileImagePath) {
    //     ImageIcon icon = new ImageIcon(projectTileImagePath);
    //     JLabel projectTileLabel = new JLabel(
    //             new ImageIcon(icon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH)));
    //     projectTileLabel.setLocation(row, yPos); // Define originxPos según tu diseño
    //     projectTileLabel.setSize(30, 30); // Tamaño del proyectil
    //     garden.getContentPane().add(projectTileLabel);
    //     garden.repaint();

    //     ProjectTile projectTile = new ProjectTile(); // Instancia de tu clase ProjectTile
    //     registerProjectTile(row, yPos, projectTile, projectTileLabel);
    // }

    public void createAndRegisterProjectTile(int row, int yPos) {
        // Load the pea image from resources
        ImageIcon icon = new ImageIcon("resources/images/pea.png");
        Image scaledImage = icon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
        ImageIcon scaledIcon = new ImageIcon(scaledImage);

        JLabel projectTileLabel = new JLabel(scaledIcon);
        
        // Calculate origin X position based on row
        int originXPos = calculateOriginXPos(row); // Implement this method as needed
        projectTileLabel.setLocation(originXPos, yPos);
        projectTileLabel.setSize(30, 30); // Size of the projectile
    
        garden.getContentPane().add(projectTileLabel);
        garden.repaint();
    
        ProjectTile projectTile = new ProjectTile(); // Instance of your ProjectTile class
        this.projectTileLabel = projectTileLabel;
        //registerProjectTile(row, yPos, projectTile, projectTileLabel);
    }

    private int calculateOriginXPos(int row) {
        // Example implementation: adjust based on your layout
        int baseX = 50;
        int rowSpacing = 100;
        return baseX + (row * rowSpacing);
    }

}
