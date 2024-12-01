package domain;

/**
 * Jugador humano que controla las plantas en el modo PlayerVsPlayer.
 */
public class PlantsStrategic extends HumanPlayer {

    public PlantsStrategic(Team team, String name) {
        super(team, name);
    }

    @Override
    public void playTurn() {
        // El jugador humano arrastra plantas en el jardín.
        // La lógica de interacción se maneja en la interfaz gráfica (GardenMenu).
    }
}
