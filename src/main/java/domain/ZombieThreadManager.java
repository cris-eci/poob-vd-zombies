package domain;

import javax.swing.JLabel;
import javax.swing.SwingUtilities;

public class ZombieThreadManager {

    private POOBvsZombies game;

    public ZombieThreadManager(POOBvsZombies game) {
        this.game = game;
    }

    public void registerZombie(int row, Zombie zombie, JLabel zombieLabel) {
        // Encontrar primera planta en la fila
        int plantCol = game.getFirstPlantInRow(row);
        int targetCol = (plantCol == -1) ? 0 : plantCol+1; // si no hay planta, col=0
        
        // Iniciar hilo para mover el zombi
        Thread t = new Thread(()-> moveZombie(row, targetCol, zombieLabel));
        t.start();
    }

    /**
     * Mueve el zombie desde col=9 hasta col=targetCol en pixeles
     * Suponemos que cada celda: ancho=80, alto=100 (por el gridPanel)
     * gridPanel en (40,80)
     */
    private void moveZombie(int row, int targetCol, JLabel zombieLabel) {
        int cellWidth = 80;
        int cellHeight = 100;
        int startX = 40 + 9*cellWidth;
        int startY = 80 + row*cellHeight;
        int endX = 40 + targetCol*cellWidth;
        int currentX = startX;
        int currentY = startY;

        // Mover el zombi hacia la izquierda hasta endX
        while (currentX > endX) {
            currentX -= 5;
            int finalX = currentX;
            int finalY = currentY;
            SwingUtilities.invokeLater(()->{
                zombieLabel.setLocation(finalX, finalY);
            });
            try {
                // Wait 100ms - 0.1s or zombie movement speed
                Thread.sleep(150);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }
}
