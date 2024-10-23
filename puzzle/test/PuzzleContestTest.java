package test;
import puzzle.*;


import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * The test class PuzzleContestTest.
 *
 * @author Andersson David Sánchez Méndez
 * @author Cristian Santiago Pedraza Rodríguez
 * @version 2024
 */

public class PuzzleContestTest
{   
    
    private Puzzle puzzle;
    private char[][] starting;
    private char[][] ending;
    private PuzzleContest puzzleContest;

    /**
     * Sets up the test fixture.
     *
     * Called before every test case method.
     */
    @BeforeEach
    public void setUp()
    {
        starting = new char[][]{
            {'r', 'g', 'b', 'y', '*', 'r', 'g', 'b', 'y', 'r', 'g'},
            {'g', 'b', 'y', '*', 'r', 'g', 'b', 'y', '*', 'r', 'g'},
            {'b', 'y', '*', 'r', 'g', 'b', 'y', '*', 'r', 'g', 'b'},
            {'y', '*', 'r', 'g', 'b', 'y', '*', 'r', 'g', 'b', 'y'},
            {'*', 'r', 'g', 'b', 'y', '*', 'r', 'g', 'b', 'y', '*'},
            {'r', 'g', 'b', 'y', '*', 'r', 'g', 'b', 'y', '*', 'r'},
            {'g', 'b', 'y', '*', 'r', 'g', 'b', 'y', '*', 'r', 'g'},
            {'b', 'y', '*', 'r', 'g', 'b', 'y', '*', 'r', 'g', 'b'},
            {'y', '*', 'r', 'g', 'b', 'y', '*', 'r', 'g', 'b', 'y'},
            {'*', 'r', 'g', 'b', 'y', '*', 'r', 'g', 'b', 'y', '*'},
            {'r', 'g', 'b', 'y', '*', 'r', 'g', 'b', 'y', 'r', 'g'}
        };
        
        ending = new char[][]{
            {'b', 'y', 'r', 'g', 'b', 'y', 'r', 'g', '*', '*', '*'},
            {'y', 'r', 'g', 'b', 'y', 'r', 'g', 'g', '*', '*', '*'},
            {'g', 'y', 'r', 'g', 'b', 'y', 'r', 'g', 'b', '*', '*'},
            {'b', 'r', 'g', 'b', 'y', 'r', 'g', 'b', 'y', '*', '*'},
            {'b', 'g', 'b', 'y', 'r', 'g', 'b', 'y', 'r', '*', '*'},
            {'y', 'g', 'b', 'y', 'r', 'g', 'b', 'y', 'g', '*', '*'},
            {'r', 'b', 'y', 'r', 'g', 'b', 'y', 'r', 'b', '*', '*'},
            {'r', 'y', 'r', 'g', 'b', 'y', 'r', 'g', 'y', '*', '*'},
            {'g', 'r', 'g', 'b', 'y', 'r', 'g', 'b', 'r', '*', '*'},
            {'r', 'b', 'r', 'g', 'b', 'y', 'r', 'g', 'b', 'y', '*'},
            {'g', 'y', 'r', 'g', 'b', 'y', 'r', 'g', 'b', 'y', '*'}
        };
        
        puzzle = new Puzzle(starting,ending);
        puzzleContest = new PuzzleContest();
        puzzle.makeInvisible();
    }
    
    /**
     * Test
     *
     * We define different tests to every method in Cycle 3 about should and shouldn't do
     */
    
    // simulate method
    @Test
    public void accordingPSshouldSimulateStartingEnding(){
        puzzleContest.simulate(starting,ending);
    }
    
    // solve method
    @Test
    public void accordingPSshouldSolveStartingEnding(){
        puzzleContest.solve(starting,ending);
    }
}
