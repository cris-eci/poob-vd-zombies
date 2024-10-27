package test;
import puzzle.*;


import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Unit test class for the PuzzleContest class.
 * This class tests the `simulate` and `solve` methods with a 12x12 board configuration.
 */
public class PuzzleContestCTest {
    private PuzzleContest contest;
    private char[][] starting;
    private char[][] ending;

    @BeforeEach
    public void setUp() {
        contest = new PuzzleContest();

        // 12x12 starting and ending matrices
        starting = new char[][] {
            { '*', '*', 'r', 'g', '*', '*', 'b', 'y', '*', '*', 'g', 'b' },
            { 'r', 'b', '*', '*', 'y', 'r', '*', '*', 'y', 'g', '*', '*' },
            { '*', '*', 'g', 'b', '*', '*', 'r', '*', '*', 'r', 'b', 'g' },
            { 'g', 'y', '*', 'r', '*', 'y', 'b', '*', '*', 'g', '*', '*' },
            { '*', '*', 'y', 'b', 'g', '*', '*', 'r', 'y', '*', 'r', '*' },
            { 'b', '*', '*', 'g', '*', 'y', 'r', '*', '*', 'b', 'g', '*' },
            { '*', 'y', 'r', '*', 'b', '*', '*', 'g', '*', 'y', 'r', 'b' },
            { 'r', '*', '*', 'y', 'g', '*', '*', 'b', '*', '*', 'g', 'y' },
            { '*', '*', 'b', '*', '*', 'r', 'y', 'g', '*', 'b', '*', '*' },
            { 'y', 'g', '*', '*', 'r', '*', '*', 'b', 'y', '*', 'g', '*' },
            { '*', 'r', '*', 'b', '*', '*', 'y', 'r', '*', '*', 'b', 'g' },
            { 'g', '*', '*', 'y', 'r', '*', 'b', 'g', '*', '*', 'y', '*' }
        };

        ending = new char[][] {
            { 'y', '*', 'r', 'g', 'b', '*', '*', 'y', 'g', 'b', 'r', '*' },
            { '*', 'g', 'y', '*', '*', 'r', 'b', '*', '*', 'y', 'g', '*' },
            { 'r', '*', '*', 'y', 'g', 'b', '*', '*', 'r', '*', 'b', '*' },
            { '*', 'y', '*', '*', 'r', '*', 'b', 'g', 'y', 'r', '*', '*' },
            { 'g', 'r', '*', '*', 'b', 'y', '*', '*', 'g', '*', 'y', 'b' },
            { '*', 'b', 'y', '*', '*', 'g', 'r', '*', '*', 'b', 'y', '*' },
            { 'y', '*', 'g', 'b', '*', '*', 'r', '*', 'y', 'g', '*', '*' },
            { '*', '*', 'r', 'b', 'y', '*', 'g', 'y', '*', '*', 'b', '*' },
            { 'g', '*', '*', 'y', 'r', '*', '*', 'b', '*', 'g', '*', 'y' },
            { 'r', 'y', '*', 'g', '*', '*', 'b', '*', 'r', '*', 'y', '*' },
            { '*', '*', 'b', '*', 'y', 'g', '*', '*', 'r', 'b', '*', '*' },
            { '*', 'g', '*', '*', 'b', 'y', 'r', 'g', '*', '*', 'r', 'y' }
        };
    }

    /**
     * Tests the solve method to determine if there is a solution from the starting to the ending configuration.
     */
    @Test
    public void testSolveMethod() {
        boolean result = contest.solve(starting, ending);
        
        assertFalse(result, "The puzzle shouldn't be solvable from the starting configuration to the ending configuration.");
    }

    /**
     * Tests the simulate method to confirm it correctly displays the steps to solve the puzzle if a solution exists.
     */
    @Test
    public void testSimulateMethod() {
        // This test will visually confirm that each step moves towards the solution.
        contest.simulate(starting, ending);
        
        // No assert statement is required here as we are simulating the puzzle and visually inspecting each step.
        // Ensure the visual output matches the expected steps towards the solution.
    }
}
