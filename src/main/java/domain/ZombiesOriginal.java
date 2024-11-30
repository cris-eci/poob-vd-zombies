package domain;

public class ZombiesOriginal extends MachinePlayer implements Strategy{
    
    public static final int ORIGINAL_SPAWN_TIME = 0;
    private int hordersNumber;
    private int hordersTime;

    public ZombiesOriginal(Team team, String name) {
        super(team, name);
    }

    public void setOriginalStrategy(){

    }

    public void spawnZombie(Zombie zombie, int line, int xPos){

    }

    public void setHordersNumbers(int number){
        this.hordersNumber = number;
    }

    public void setHordersTime(int time){
        this.hordersTime = time;
    }

    @Override
    public void executeStrategicTurn(int state) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'executeStrategicTurn'");
    }

    @Override
    public int getStrategy(int currentGameState) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getStrategy'");
    }

    @Override
    public void setStrategy(int strategicGameState) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setStrategy'");
    }

}
