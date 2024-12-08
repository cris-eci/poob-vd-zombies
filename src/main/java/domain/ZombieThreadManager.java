package domain;

import javax.swing.JLabel;
import javax.swing.SwingUtilities;

import presentation.GardenMenu;

public class ZombieThreadManager {

    private POOBvsZombies game;
    private GardenMenu garden;

    public ZombieThreadManager(POOBvsZombies game, GardenMenu garden) {
        this.game = game;
        this.garden = garden;
    }

    public void registerZombie(int row, Zombie zombie, JLabel zombieLabel) {
        Thread t = new Thread(()-> zombieLogic(row, zombie, zombieLabel));
        t.start();
    }

    private void zombieLogic(int row, Zombie zombie, JLabel zombieLabel) {
        while (true) {
            // Buscar la planta más cercana
            int plantCol = game.getFirstPlantInRow(row);
            if (plantCol == -1) {
                // No hay más plantas, el zombi se queda quieto.
                break;
            }

            int targetCol = plantCol + 1; 
            moveZombie(row, targetCol, zombieLabel);

            // Ahora el zombi está adyacente a la planta. Atacar.
            attackPlant(row, plantCol, zombie, zombieLabel);
            // Si la planta murió, se remueve y el loop continua para buscar la siguiente.
        }
    }

    private void moveZombie(int row, int targetCol, JLabel zombieLabel) {
        int cellWidth = 80;
        int cellHeight = 100;
        int startX = zombieLabel.getX(); // Tomamos la posición actual
        int startY = zombieLabel.getY();
        int endX = 40 + targetCol*cellWidth;

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

        // Mientras la planta esté viva, atacarla cada 0.5s
        while (!plant.isDead()) {
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
}
