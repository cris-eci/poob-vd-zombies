package domain;

/**
 * Jugador humano que controla los zombies en el modo PlayerVsPlayer.
 */
public class ZombiesStrategic extends HumanPlayer {

    public ZombiesStrategic(Team team, String name) {
        super(team, name);
    }

    @Override
    public void playTurn() {
        // El jugador humano arrastra zombies en el jardín.
        // La lógica de interacción se maneja en la interfaz gráfica (GardenMenu).
    }
}
