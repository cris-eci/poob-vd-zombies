package domain;

public class ZombiesOriginal extends MachinePlayer implements Strategy{
    
    public static final int ORIGINAL_SPAWN_TIME = 0;
    private int hordersNumber;
    private int hordersTime;
    private int currentStrategy;

    public ZombiesOriginal(Team team, String name) {
        super(team, name);
    }

    public void setOriginalStrategy(){
        this.currentStrategy = ORIGINAL_SPAWN_TIME;
    }

    public void spawnZombie(Zombie zombie, int line, int xPos){
        // Implementation for spawning a zombie
    }

    public void setHordersNumbers(int number){
        this.hordersNumber = number;
    }

    public void setHordersTime(int time){
        this.hordersTime = time;
    }

    @Override
    public void executeStrategicTurn(int state) {
        // Implementation for executing a strategic turn
        if (state == currentStrategy) {
            // Execute specific strategy
        }
    }

    @Override
    public int getStrategy(int currentGameState) {
        // Return the current strategy based on the game state
        return this.currentStrategy;
    }

    @Override
    public void setStrategy(int strategicGameState) {
        // Set the strategy based on the strategic game state
        this.currentStrategy = strategicGameState;
    }
    

}
