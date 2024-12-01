package domain;

import java.util.ArrayList;

public class PlantsStrategic extends HumanPlayer {

    public PlantsStrategic(String name, int sunlight, ArrayList<String> plants) {
        // sunlight are the resources for the plants in plants team
        super(name);
        Team plantsTeam = new Plants(sunlight, plants);
        this.team = plantsTeam;
    }

    @Override
    public void playTurn() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'playTurn'");
    }

    @Override
    public String getTeamName() {
        return team.getTeamName();
    }
}
