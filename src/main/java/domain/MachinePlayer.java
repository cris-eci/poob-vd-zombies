package domain;

import domain.Strategy;

/**
 * Clase abstracta que representa a un jugador controlado por la máquina.
 * Implementa una estrategia para controlar su equipo.
 */
public abstract class MachinePlayer extends Player {
    protected Strategy strategy;

    public MachinePlayer(Team team, String name, Strategy strategy) {
        super(team, name);
        this.strategy = strategy;
    }

    @Override
    public void playTurn() {
        strategy.executeStrategy();
    }
}
