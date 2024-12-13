package domain;

import java.util.ArrayList;
import java.util.Arrays;
import domain.POOBvsZombies;

public class PlantsIntelligent extends MachinePlayer {

    private ArrayList<String> plantingOrder; // Orden de tipos de plantas a colocar
    private int currentPlantIndex; // Índice actual en el orden de plantación

    public PlantsIntelligent(int suns) {
        super("StPlant");
        Team plantsTeam = new Plants(suns, MACHINE_PLANTS);
        this.team = plantsTeam;
        this.plantingOrder = new ArrayList<>(Arrays.asList("Sunflower", "Peashooter", "WallNut"));
        this.currentPlantIndex = 0;
    }

    /**
     * Inicia la plantación automática siguiendo la estrategia definida.
     * Este método se ejecuta en un hilo separado para no bloquear el hilo principal del juego.
     */
    public void startAutomaticPlanting(POOBvsZombies game) {
        new Thread(() -> {
            while (true) { // Intentar plantar continuamente
                // Obtener el tipo de planta actual en la estrategia
                String plantType = plantingOrder.get(currentPlantIndex);
                Plant plant = game.createPlantInstance(plantType);
                if (plant == null) {
                    System.err.println("Tipo de planta inválido: " + plantType);
                    continue;
                }

                // Verificar si hay suficientes recursos para plantar
                synchronized (team) { // Sincronizar acceso a los recursos
                    if (team.getResourceCounterAmount() >= plant.getCost()) {
                        // Determinar la columna objetivo basada en el tipo de planta
                        int targetCol = getTargetColumn(plantType);
                        if (targetCol == -1) {
                            System.err.println("No se ha definido una columna objetivo para: " + plantType);
                            continue;
                        }

                        // Encontrar la primera fila vacía en la columna objetivo
                        int targetRow = findEmptyRowInColumn(targetCol,game);
                        if (targetRow != -1) {
                            // Colocar la planta en el dominio (lógica del juego)
                            game.addEntity(targetRow, targetCol, plant);
                            // Visualizar la planta en la interfaz gráfica
                            game.spawnPlantUI(targetRow, targetCol, plant);
                            // Deducir el costo de la planta
                            team.deductResource(plant.getCost());
                            // Actualizar las etiquetas de puntaje en la interfaz
                            if (game.getGardenMenu() != null) {
                                game.getGardenMenu().updateScoreLabels();
                            }
                            System.out.println("Máquina colocó un " + plantType + " en (" + targetRow + ", " + targetCol + ")");
                        } else {
                            System.out.println("No hay celdas vacías en la columna " + targetCol + " para " + plantType);
                        }
                    } else {
                        // No hay suficientes recursos, esperar hasta que se acumulen más
                        System.out.println("No hay suficientes recursos para plantar " + plantType + ". Esperando...");
                    }
                }

                // Pasar a la siguiente planta en la estrategia
                currentPlantIndex = (currentPlantIndex + 1) % plantingOrder.size();

                // Esperar antes de intentar plantar la siguiente planta
                try {
                    Thread.sleep(2000); // Esperar 3 segundos
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }).start();
    }

    /**
     * Determina la columna objetivo basada en el tipo de planta.
     *
     * @param plantType Tipo de planta.
     * @return Número de columna objetivo o -1 si no está definido.
     */
    private int getTargetColumn(String plantType) {
        switch (plantType) {
            case "Sunflower":
                return 1; // Primera columna después de la cortadora de césped
            case "Peashooter":
                return 2; // Segunda columna
            case "WallNut":
                return 3; // Tercera columna
            default:
                return -1;
        }
    }

    /**
     * Encuentra la primera fila vacía en una columna específica.
     *
     * @param col Número de columna.
     * @return Número de fila vacía o -1 si no hay celdas disponibles.
     */
    private int findEmptyRowInColumn(int col,POOBvsZombies game) {
        for (int row = 0; row < 5; row++) {
            String entity = game.getEntity(row, col);
            if (entity == null) {
                return row;
            }
        }
        return -1; // No hay celdas vacías
    }

    @Override
    public void setScore() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setScore'");
    }
    
}
