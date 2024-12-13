package domain;

public interface Strategy {

    public abstract void executeStrategicTurn(int state);

    public abstract int getStrategy(int currentGameState);

    public abstract void setStrategy(int strategicGameState);

} 