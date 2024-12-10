
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

import domain.POOBvsZombies;
import domain.Peashooter;
import domain.Zombie;
import presentation.GardenMenu;

public class ProjectTileThreadManager {
    private POOBvsZombies game;
    private GardenMenu garden;
    private ZombieThreadManager zombieThreadManager;

    public ProjectTileThreadManager(POOBvsZombies game, GardenMenu garden, ZombieThreadManager zombieThreadManager) {
        this.game = game;
        this.garden = garden;
        this.zombieThreadManager = zombieThreadManager;
    }

    public void registerProjectTile(int row, int yPos, ProjectTile projectTile, int graphicXPosition) {
        // Iniciar el hilo que gestionará la lógica del disparo
        Thread t = new Thread(() -> projectTileLogic(row, yPos, projectTile, graphicXPosition));
        t.start();
    }

    private void projectTileLogic(int row, int yPos, ProjectTile projectTile, int graphicXPosition) {
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

            Thread targetZombieThread = (Thread) firstZombie.get(0);
            JLabel targetZombieLabel = (JLabel) firstZombie.get(1);
            Zombie targetZombie = (Zombie) firstZombie.get(2);

            // Mientras el zombie esté vivo, disparar
            while (targetZombie.health > 0) {
                // Crear un nuevo proyectil cada vez que vamos a disparar
                //JLabel projectTileLabel = createAndRegisterProjectTile(row, yPos);

                int originXpos = graphicXPosition;
                int targetXpos = targetZombieLabel.getX();

                // Mover el proyectil hacia el zombie
                //moveProjectTile(originXpos, targetXpos, projectTileLabel);
                moveProjectTile(originXpos, targetXpos);

                // Hacer daño al zombie
                targetZombie.takeDamage(Peashooter.DAMAGE);

                // Eliminar el proyectil tras el impacto
                // SwingUtilities.invokeLater(() -> {
                //     Container parent = projectTileLabel.getParent();
                //     if (parent != null) {
                //         parent.remove(projectTileLabel);
                //         parent.revalidate();
                //         parent.repaint();
                //     }
                // });

                // Verificar si el zombie murió
                if (targetZombie.health <= 0) {
                    // zombie muerto, salir del while interno para buscar otro zombie
                    // zombieThreadManager.deleteZombie(targetZombieThread, targetZombieLabel,
                    // targetZombie, row);
                    zombieThreadManager.terminateZombie(targetZombieThread);
                    break;
                }

                // Si sigue vivo, esperar 3 segundos y volver a disparar
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                }
            }
            // aquí se termina el while del zombie actual.
            // El loop externo while(true) volverá a chequear si hay otro zombie.
        }
    }

    // private JLabel createAndRegisterProjectTile(int row, int yPos) {
    //     // Cargar la imagen del pea
    //     ImageIcon icon = new ImageIcon("resources/images/pea.png");
    //     Image scaledImage = icon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
    //     ImageIcon scaledIcon = new ImageIcon(scaledImage);

    //     JLabel projectTileLabel = new JLabel(scaledIcon);

    //     // Calcular la posición inicial (X) del pea con respecto al row.
    //     // Aquí asumiendo que la peashooter está en (row, col)
    //     // Debes ajustar 'calculateOriginXPos' para que coincida con donde está la
    //     // Peashooter.
    //     int originXPos = calculateOriginXPos(row);
    //     projectTileLabel.setLocation(originXPos, yPos);
    //     projectTileLabel.setSize(30, 30); // tamaño del proyectil

    //     garden.getContentPane().add(projectTileLabel);
    //     garden.repaint();
    //     return projectTileLabel;
    // }

    // private int calculateOriginXPos(int row) {
    //     // Ajustar según tu diseño:
    //     int baseX = 120; // por ejemplo, la peashooter está en la columna 1 => x=120 px
    //     int rowSpacing = 100;
    //     // Si necesitas que sea fijo, simplifícalo:
    //     // return 120; // si todas las peashooter se ponen en columna 1
    //     return baseX;
    // }

    private void moveProjectTile(int originxPos, int targetXpos/*, JLabel projectTileLabel*/) {
        int delay = 50;
        final int step = 5;
        final int[] currentX = { originxPos };

        Timer timer = new Timer(delay, null);
        timer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (currentX[0] < targetXpos) {
                    currentX[0] += step;
                    //projectTileLabel.setLocation(currentX[0], projectTileLabel.getY());
                } else {
                    timer.stop();
                    // El proyectil llegó al zombie, no resetear aquí ya que se elimina tras el
                    // impacto
                }
            }
        });
        timer.start();
    }
}
