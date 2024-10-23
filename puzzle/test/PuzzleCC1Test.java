package test;
import puzzle.*;


import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Unit test class for the Puzzle class.
 * Robust tests covering all cycle 1 methods.
 * 
 * @author Andersson David Sánchez Méndez
 * @author Cristian Santiago Pedraza Rodríguez
 * @version 2024
 */
public class PuzzleCC1Test {
    private Puzzle puzzle1; // Constructor Puzzle(int h, int w)
    private Puzzle puzzle2; // Constructor Puzzle(char[][] starting, char[][] ending)
    private Puzzle puzzle3; // Constructor Puzzle(char[][] ending)

    private char[][] starting;
    private char[][] ending;

    @BeforeEach
    public void setUp() {
        // Starting and ending 10x8 matrices
        starting = new char[][] {
            {'r', '*', 'b', '*', 'g', '*', 'y', '*'},
            {'*', 'y', '*', 'r', '*', 'g', 'r', 'b'},
            {'b', '*', 'g', '*', 'y', 'b', '*', 'y'},
            {'y', 'g', '*', 'r', '*', 'y', '*', 'r'},
            {'*', 'b', 'y', '*', 'r', '*', 'b', '*'},
            {'r', '*', '*', 'y', 'b', '*', 'y', 'g'},
            {'g', 'y', '*', '*', 'r', 'g', '*', 'b'},
            {'b', '*', 'g', '*', 'y', 'b', '*', 'r'},
            {'y', '*', 'b', 'r', '*', 'y', 'g', '*'},
            {'*', 'y', '*', 'g', 'b', '*', '*', 'g'}
        };

        ending = new char[][] {
            {'*', 'r', '*', 'b', 'y', '*', 'g', 'r'},
            {'y', '*', 'g', '*', 'b', 'y', '*', 'g'},
            {'*', 'b', 'r', '*', 'g', 'b', 'y', '*'},
            {'g', '*', 'y', 'r', '*', 'g', '*', 'b'},
            {'r', 'y', '*', 'g', '*', 'b', 'r', '*'},
            {'*', 'g', '*', 'y', 'b', '*', 'y', 'r'},
            {'b', '*', 'r', '*', 'y', 'g', '*', 'b'},
            {'*', 'y', 'b', 'r', '*', 'y', 'g', '*'},
            {'g', '*', 'r', '*', 'y', 'b', '*', 'y'},
            {'*', 'b', '*', 'g', 'r', '*', 'y', 'g'}
        };

        // Initialize puzzles with each constructor
        puzzle1 = new Puzzle(10, 8);
        puzzle2 = new Puzzle(starting, ending);
        puzzle3 = new Puzzle(ending);
    }

    /**
     * Robust test for the Puzzle(int h, int w) constructor.
     * This test covers all cycle 1 methods.
     */
    @Test
    public void testPuzzleConstructorWithDimensions() {
        Puzzle puzzle1 = new Puzzle(10,8);
        // Make puzzle visible
        puzzle1.makeVisible();
        assertTrue(puzzle1.ok(), "Puzzle should be visible.");

        // Add several tiles
        puzzle1.addTile(0, 0, 'r');
        assertTrue(puzzle1.ok(), "Should be able to add a red tile at (0,0).");

        puzzle1.addTile(1, 1, 'g');
        assertTrue(puzzle1.ok(), "Should be able to add a green tile at (1,1).");

        // Try to add a tile at an invalid position
        puzzle1.addTile(10, 0, 'b');
        assertFalse(puzzle1.ok(), "Should not be able to add a tile outside the board.");

        // Delete a tile
        puzzle1.deleteTile(0, 0);
        assertTrue(puzzle1.ok(), "Should be able to delete the tile at (0,0).");

        // Attempt to delete a non-existent tile
        puzzle1.deleteTile(0, 0);
        assertFalse(puzzle1.ok(), "Should not be able to delete a tile that does not exist.");

        // Relocate a tile
        puzzle1.addTile(2, 2, 'b');
        puzzle1.relocateTile(new int[]{2, 2}, new int[]{3, 3});
        assertTrue(puzzle1.ok(), "Should be able to relocate the tile from (2,2) to (3,3).");

        // Attempt to relocate a tile to an occupied position
        puzzle1.addTile(3, 3, 'y');
        puzzle1.relocateTile(new int[]{3, 3}, new int[]{1, 1});
        assertFalse(puzzle1.ok(), "Should not be able to relocate to an occupied position.");

        // Add and delete glue
        puzzle1.addGlue(3, 3);
        assertTrue(puzzle1.ok(), "Should be able to add glue at (3,3).");

        puzzle1.deleteGlue(3, 3);
        assertTrue(puzzle1.ok(), "Should be able to delete glue from (3,3).");

        // Attempt to add glue to an empty position
        puzzle1.addGlue(0, 0);
        assertFalse(puzzle1.ok(), "Should not be able to add glue to an empty position.");

        // Get the current arrangement
        char[][] arrangement = puzzle1.actualArrangement();
        assertNotNull(arrangement, "Current arrangement should not be null.");

        // Check if it is the goal state (it will not be)
        assertFalse(puzzle1.isGoal(), "Should not be in the goal state.");

        // Make puzzle invisible
        puzzle1.makeInvisible();
        assertTrue(puzzle1.ok(), "Puzzle should be invisible.");

        // Finish puzzle (commented to avoid program termination)
        puzzle1.finish();
        assertTrue(puzzle1.ok(), "Puzzle should finish correctly.");
    }

    /**
     * Robust test for the Puzzle(char[][] starting, char[][] ending) constructor.
     * This test covers all cycle 1 methods.
     */
    @Test
    public void testPuzzleConstructorWithStartingAndEndingMatrices() {
        // Make puzzle visible
        Puzzle puzzle2 = new Puzzle(starting,ending);
        puzzle2.makeVisible();
        assertTrue(puzzle1.ok(), "Puzzle should be visible.");

        // Add several tiles
        puzzle2.addTile(1, 0, 'r');
        assertTrue(puzzle1.ok(), "Should be able to add a red tile at (1,0).");

        puzzle2.addTile(9, 0, 'g');
        assertTrue(puzzle1.ok(), "Should be able to add a green tile at (0,9).");

        // Delete a tile
        puzzle2.deleteTile(0, 0);
        assertTrue(puzzle1.ok(), "Should be able to delete the tile at (0,0).");
        
        puzzle2.deleteTile(0,2);
        assertTrue(puzzle1.ok(), "Should be able to delete the tile at (0,2).");

        
        // Relocate a tile
        puzzle2.addTile(1, 2, 'b');
        puzzle2.relocateTile(new int[]{1, 2}, new int[]{0, 7});
        assertTrue(puzzle1.ok(), "Should be able to relocate the tile from (1,2) to (0,7).");


        // Add and delete glue
        puzzle2.addGlue(0, 7);
        assertTrue(puzzle1.ok(), "Should be able to add glue at (0,7).");
        
        // Perform a tilt
        puzzle2.tilt('d'); // Downwards
        assertTrue(puzzle1.ok(), "Should be able to perform a tilt downwards.");
        
        // Perform a tilt
        puzzle2.tilt('l'); // Downwards
        assertTrue(puzzle1.ok(), "Should be able to perform a tilt to the left.");
        
        puzzle2.deleteGlue(0, 7);
        assertFalse(puzzle1.ok(), "Should be able to delete glue from (0,7).");

        // Attempt to add glue to an empty position
        puzzle2.addGlue(2, 2);
        assertFalse(puzzle1.ok(), "Should be able to add glue at (2,2).");

        
        // Get the current arrangement
        char[][] arrangement = puzzle2.actualArrangement();
        assertNotNull(arrangement, "Current arrangement should not be null.");

        // Check if it is the goal state (it will not be)
        assertFalse(puzzle1.isGoal(), "Should not be in the goal state.");

        // Make puzzle invisible
        puzzle2.makeInvisible();
        assertTrue(puzzle1.ok(), "Puzzle should be invisible.");
        
        // Try to add a tile at an occupied position // Error Message doesnt' show in terminal cuz simulator is invisible
        puzzle2.addTile(9, 7, 'r');
        assertFalse(puzzle2.ok(), "Should not be able to add a tile at an occupied position.");

        // Finish puzzle (commented to avoid program termination)
        puzzle2.finish();
        assertTrue(puzzle1.ok(), "Puzzle should finish correctly.");
    }

    /**
     * Robust test for the Puzzle(char[][] ending) constructor.
     * This test covers all cycle 1 methods.
     */
    @Test
    public void testPuzzleConstructorWithEndingMatrix() {
        Puzzle puzzle3 = new Puzzle(ending);
        // Make puzzle visible
        puzzle3.makeVisible();
        assertTrue(puzzle3.ok(), "Puzzle should be visible.");

        // Add tiles to try to reach the goal state
        puzzle3.addTile(0, 1, 'r');
        puzzle3.addTile(1, 0, 'y');
        puzzle3.addTile(2, 1, 'b');
        assertTrue(puzzle3.ok(), "Should be able to add tiles at specified positions.");

        // Relocate tiles to approach the goal
        puzzle3.relocateTile(new int[]{0, 1}, new int[]{0, 0});
        puzzle3.relocateTile(new int[]{1, 0}, new int[]{0, 1});
        assertTrue(puzzle3.ok(), "Should be able to relocate tiles to approach the goal.");

        // Add and delete glue
        puzzle3.addGlue(0, 0);
        puzzle3.addGlue(0, 1);
        assertTrue(puzzle3.ok(), "Should be able to add glue at (0,0) and (0,1).");

        puzzle3.deleteGlue(0, 0);
        assertTrue(puzzle3.ok(), "Should be able to delete glue from (0,0).");

        // Perform tilt to move stuck tiles
        puzzle3.tilt('r'); // Rightwards
        assertTrue(puzzle3.ok(), "Should be able to perform a tilt to the right.");

        // Get the current arrangement
        char[][] arrangement = puzzle3.actualArrangement();
        assertNotNull(arrangement, "Current arrangement should not be null.");

        // Check if it is the goal state
        boolean goalReached = puzzle3.isGoal();
        assertFalse(goalReached, "Should not yet be in the goal state.");

        // Make puzzle invisible and then visible
        puzzle3.makeInvisible();
        assertTrue(puzzle3.ok(), "Puzzle should be invisible.");
        puzzle3.makeVisible();
        assertTrue(puzzle3.ok(), "Puzzle should be visible again.");

        // Delete a tile and check the state
        puzzle3.deleteTile(0, 1);
        assertTrue(puzzle3.ok(), "Should be able to delete the tile at (0,1).");

        // Check goal state again
        goalReached = puzzle3.isGoal();
        assertFalse(goalReached, "Should not yet be in the goal state.");

        // Finish puzzle (commented to avoid program termination)
        puzzle3.finish();
        assertTrue(puzzle3.ok(), "Puzzle should finish correctly.");
    }
}
