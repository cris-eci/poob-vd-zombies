package domain;

public class PlantsIntelligent extends MachinePlayer {

    public PlantsIntelligent() {
        super("StPlant");
        Team plantsTeam = new Plants(100, MACHINE_PLANTS);
        this.team = plantsTeam;
    }

    @Override
    public void setScore() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setScore'");
    }
    
}