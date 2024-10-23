package test;
import puzzle.Puzzle;


import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Unit test class for the Puzzle class.
 * Robust tests covering all cycle 2 methods.
 * 
 * @version
 */
public class PuzzleCC2Test {
    private Puzzle puzzle1; // Constructor Puzzle(int h, int w)
    private Puzzle puzzle2; // Constructor Puzzle(char[][] starting, char[][] ending)
    private Puzzle puzzle3; // Constructor Puzzle(char[][] ending)

    private char[][] starting;
    private char[][] ending;

    @BeforeEach
    public void setUp() {
        // Starting and ending 5x7 matrices
        starting = new char[][] {
            {'r', '*', 'b', '*', 'g', '*', 'y'},
            {'*', 'y', '*', 'r', '*', 'g', 'r'},
            {'b', '*', 'g', '*', 'y', 'b', '*'},
            {'y', 'g', '*', 'r', '*', 'y', '*'},
            {'*', 'b', 'y', '*', 'r', '*', 'b'}
        };

        ending = new char[][] {
            {'*', 'r', '*', 'b', 'y', '*', 'g'},
            {'y', '*', 'g', '*', 'b', 'y', '*'},
            {'*', 'b', 'r', '*', 'g', 'b', 'y'},
            {'g', '*', 'y', 'r', '*', 'g', '*'},
            {'r', 'y', '*', 'g', '*', 'b', 'r'}
        };

        // Initialize puzzles with each constructor
        puzzle1 = new Puzzle(5, 7);
        puzzle2 = new Puzzle(starting, ending);
        puzzle3 = new Puzzle(ending);
    }

    /**
     * Robust test for the Puzzle(int h, int w) constructor.
     * This test covers all cycle 2 methods.
     */
    @Test
    public void testPuzzleConstructorWithDimensions() {
        // Use cycle 1 methods to set up the board
        puzzle1.addTile(1, 1, 'r');
        puzzle1.addTile(2, 2, 'b');
        assertTrue(puzzle1.ok(), "Should add tiles correctly.");

        // Test makeHole method
        puzzle1.makeHole(1, 1);
        assertTrue(puzzle1.ok(), "Should be able to make a hole at (1,1).");
        puzzle1.addTile(1, 1, 'y'); // Try adding a tile where there’s a hole
        assertFalse(puzzle1.ok(), "Should not add a tile to a hole position.");

        // Test exchange method
        puzzle1.exchange();
        assertTrue(puzzle1.ok(), "Should be able to exchange the boards.");

        // Test fixedTiles method
        int[][] fixedTiles = puzzle1.fixedTiles();
        assertNotNull(fixedTiles, "Fixed tiles matrix should not be null.");
        
        // Test misplacedTiles method
        int misplacedCount = puzzle1.misplacedTiles();
        assertTrue(misplacedCount >= 0, "Misplaced tiles count should be non-negative.");

        // Test tilt method
        puzzle1.tilt();
        assertTrue(puzzle1.ok(), "Should be able to tilt smart.");

        // Verify final arrangements
        char[][] arrangement = puzzle1.actualArrangement();
        assertNotNull(arrangement, "Arrangement should not be null.");
    }

    /**
     * Robust test for the Puzzle(char[][] starting, char[][] ending) constructor.
     * This test covers all cycle 2 methods.
     */
    @Test
    public void testPuzzleConstructorWithStartingAndEndingMatrices() {
        // Use cycle 1 methods to set up the board
        puzzle2.addTile(0, 1, 'g');
        puzzle2.deleteTile(0, 1);
        assertTrue(puzzle2.ok(), "Should delete tiles correctly.");

        // Test makeHole method
        puzzle2.makeHole(3, 4);
        assertTrue(puzzle2.ok(), "Should be able to make a hole at (3,4).");

        // Test exchange method
        puzzle2.exchange();
        assertTrue(puzzle2.ok(), "Should be able to exchange the boards.");

        // Test fixedTiles method
        int[][] fixedTiles = puzzle2.fixedTiles();
        assertNotNull(fixedTiles, "Fixed tiles matrix should not be null.");

        // Test misplacedTiles method
        int misplacedCount = puzzle2.misplacedTiles();
        assertTrue(misplacedCount >= 0, "Misplaced tiles count should be non-negative.");

        // Test tilt method
        puzzle2.tilt();
        assertTrue(puzzle2.ok(), "Should be able to tilt smart.");

        // Verify final arrangements
        char[][] arrangement = puzzle2.actualArrangement();
        assertNotNull(arrangement, "Arrangement should not be null.");
    }

    /**
     * Robust test for the Puzzle(char[][] ending) constructor.
     * This test covers all cycle 2 methods.
     */
    @Test
    public void testPuzzleConstructorWithEndingMatrix() {
        // Use cycle 1 methods to set up the board
        puzzle3.addTile(2, 0, 'y');
        puzzle3.addTile(4, 6, 'b');
        assertTrue(puzzle3.ok(), "Should add tiles correctly.");

        // Test makeHole method
        puzzle3.makeHole(2, 0);
        assertTrue(puzzle3.ok(), "Should be able to make a hole at (2,0).");
        puzzle3.addTile(2, 0, 'g'); // Attempt to add to a hole
        assertFalse(puzzle3.ok(), "Should not add tile to a hole position.");

        // Test exchange method
        puzzle3.exchange();
        assertTrue(puzzle3.ok(), "Should be able to exchange the boards.");

        // Test fixedTiles method
        int[][] fixedTiles = puzzle3.fixedTiles();
        assertNotNull(fixedTiles, "Fixed tiles matrix should not be null.");

        // Test misplacedTiles method
        int misplacedCount = puzzle3.misplacedTiles();
        assertTrue(misplacedCount >= 0, "Misplaced tiles count should be non-negative.");

        // Test tilt method
        puzzle3.tilt();
        assertTrue(puzzle3.ok(), "Should be able to tilt smart.");

        // Verify final arrangements
        char[][] arrangement = puzzle3.actualArrangement();
        assertNotNull(arrangement, "Arrangement should not be null.");
    }
}
