package test;

import puzzle.*;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * The test class PuzzleC1Test.
 *
 * @author Andersson David Sánchez Méndez
 * @author Cristian Santiago Pedraza Rodríguez
 * @version 2024
 */
public class PuzzleC1Test
{   
    // Elementary attributes to make tests
    private Puzzle puzzle;
    //private Puzzle puzzleI;
    //private Puzzle puzzleF;
    private char[][] starting;
    private char[][] ending;

    /**
     * Sets up the test fixture.
     *
     * Called before every test case method.
     */
    @BeforeEach
    public void setUp()
    {
        starting = new char[][]{
            {'r', 'g', 'b', 'y', '*', 'r'},
            {'g', 'b', 'y', '*', 'r', 'g'},
            {'b', 'y', '*', 'r', 'g', 'b'},
            {'y', '*', 'r', 'g', 'b', 'y'},
            {'*', 'r', 'g', 'b', 'y', '*'},
            {'r', 'g', 'b', 'y', '*', 'r'},
            {'g', 'b', 'y', '*', 'r', 'g'},
            {'b', 'y', '*', 'r', 'g', 'b'},
            {'y', '*', 'r', 'g', 'b', 'y'}
        };
        
        ending = new char[][]{
            {'r', 'g', 'b', 'y', '*', 'r'},
            {'g', 'b', 'y', '*', 'r', 'g'},
            {'b', 'y', '*', 'r', 'g', 'b'},
            {'y', '*', 'r', 'g', 'b', 'y'},
            {'*', 'r', 'g', 'b', 'y', '*'},
            {'r', 'g', 'b', 'y', '*', 'r'},
            {'g', 'b', 'y', '*', 'r', 'g'},
            {'b', 'y', '*', 'r', 'g', 'b'},
            {'y', '*', 'r', 'g', 'b', 'y'}
        };

        
        //puzzleI = new Puzzle(9,6);
        puzzle = new Puzzle(starting,ending);
        //puzzleF = new Puzzle(ending);
        puzzle.makeInvisible();
    }

    /**
     * Test
     *
     * We define different tests to every method in Cycle 1 about should and shouldn't do
     */
    
    //TESTS ABOUT CONSTRUCTORS
    /**
    @Test
    public void accordingPSShouldntCreateBoardsWithHWNegative(){
        puzzleI = new Puzzle(-7,0);
        assertFalse(puzzleI.ok());
        puzzleI = null;
    }
    
    @Test
    public void accordingPSShouldCreateBoardsWithHWPositive(){
        puzzleI = new Puzzle(4,2);
        assertTrue(puzzleI.ok());
        puzzleI = null;
    }
    **/
    
    @Test 
    public void accordingPSshouldCreateStartingEnding(){
        assertTrue(puzzle.ok());
    }
    
    /**
    @Test
    public void accordingPSShouldCreateEnding(){
        assertTrue(puzzleF.ok());
    }
    **/
    
    //addTile method
    
    @Test
    public void accordingPSshouldAddValidTile(){
        puzzle.addTile(0,4,'y');
        assertTrue(puzzle.ok(),"It should add a tile in the position because is empty and good label.");
        accordingPSshouldntAddInvalidTileExistent();
    }
    
    @Test
    private void accordingPSshouldntAddInvalidTileExistent(){
        puzzle.addTile(0,4,'y');
        assertFalse(puzzle.ok(),"There is already a tile here.");
    }
    
    
    @Test
    public void accordingPSshouldntAddInvalidTileLabel(){
        puzzle.addTile(4,0,'t');
        assertFalse(puzzle.ok(),"It shouldn't add a tile with an invalid label.");
    }
    
    @Test
    public void accordingPSshouldntAddInvalidTileOutOfRange(){
        puzzle.addTile(10,4,'y');
        assertFalse(puzzle.ok(),"It shouldn't add a tile because you have exceeded the puzzle space.");
    }
    
    @Test
    public void accordingPSshouldntAddInvalidTileNegativePositions(){
        puzzle.addTile(-3,3,'y');
        assertFalse(puzzle.ok(),"It shouldn't add a tile cuz you're searching for a non-existent tile with negative position.");
    }
    
    @Test
    public void accordingPSshouldntAddInvalidTileHole(){
        puzzle.makeHole(1,3);
        puzzle.addTile(1,3,'y');
        assertFalse(puzzle.ok(),"You cannot add a tile on a hole.");
    }
    
    //deleteTile method
    
    @Test
    public void accordingPSshouldDeleteValidTile(){
        puzzle.deleteTile(0,0);
        assertTrue(puzzle.ok(),"It should delete a tile in the position cuz the tile doesn't have glue or is stuck.");
    }
    
    @Test
    public void accordingPSshouldntDeleteInvalidTileInexistent(){
        puzzle.deleteTile(0,4);
        assertFalse(puzzle.ok(),"You're trying to delete a non-existent tile.");
    }
    
    
    @Test
    public void accordingPSshouldntDeleteInvalidTileOutOfRange(){
        puzzle.deleteTile(10,4);
        assertFalse(puzzle.ok(),"It shouldn't delete a tile because you have exceeded the puzzle space.");
    }
    
    @Test
    public void accordingPSshouldntDeleteInvalidTileNegativePositions(){
        puzzle.deleteTile(-3,3);
        assertFalse(puzzle.ok(),"It shouldn't delete a tile cuz you're searching for a non-existent tile with negative position.");
    }
    
    @Test
    public void accordingPSshouldntDeleteInvalidTileHole(){
        puzzle.makeHole(1,3);
        puzzle.deleteTile(1,3);
        assertFalse(puzzle.ok(),"You cannot delete a tile that is a hole.");
    }
    
    @Test
    public void accordingPSshouldntDeleteInvalidGlueStuck(){
        puzzle.addGlue(0,0);
        puzzle.deleteTile(0,0);
        puzzle.deleteTile(1,0);
        assertFalse(puzzle.ok(),"You cannot delete a tile that has glue or is stuck.");
    }
    
    
    //relocateTile method
    
    @Test
    public void accordingPSshouldRelocateTotileFromTile(){
        int [] fromTile = {0,0};
        int [] toTile = {7,2};
        puzzle.relocateTile(fromTile,toTile);
        assertTrue(puzzle.ok(),"You can relocate from tile to tile, because it fulfills all the conditions.");
    }
    
    @Test
    public void accordingPSshouldntRelocateTileInvalidCoords(){
        int [] fromTile = {-4,0};
        int [] toTile = {7,2};
        
        int [] fromTile1 = {4,0};
        int [] toTile1 = {7,-2};
        
        puzzle.relocateTile(fromTile,toTile);
        puzzle.relocateTile(fromTile1,toTile1);
        assertFalse(puzzle.ok(),"You cannot relocate a tile cuz there are invalid coordinates(negative or out of range).");
    }
    
    @Test
    public void accordingPSshouldntRelocateTileHole(){
        puzzle.makeHole(7,2);
        puzzle.makeHole(0,4);
        
        int [] fromTile = {0,0};
        int [] toTile = {7,2};
        
        int [] fromTile1 = {0,4};
        int [] toTile1 = {8,1};
        
        puzzle.relocateTile(fromTile,toTile);
        puzzle.relocateTile(fromTile1,toTile1);
        
        assertFalse(puzzle.ok(),"You cannot move a hole tile or you cannot relocate a tile to a position that has a hole.");
    }
    
    @Test
    public void accordingPSshouldntRelocateTileGlueStuck(){
        puzzle.addGlue(0,0);
        
        
        int [] fromTile = {0,0};
        int [] toTile = {0,4};
        
        int [] fromTile1 = {1,0};
        int [] toTile1 = {8,1};
        
        puzzle.relocateTile(fromTile,toTile);
        puzzle.relocateTile(fromTile1,toTile1);
        
        assertFalse(puzzle.ok(),"You cannot move a tile that has glue or is stuck.");
    }
    
    @Test
    public void accordingPSshouldntRelocateTileEmptyFull(){
        
        int [] fromTile = {0,0};
        int [] toTile = {0,5};
        
        int [] fromTile1 = {8,1};
        int [] toTile1 = {7,2};
        
        puzzle.relocateTile(fromTile,toTile);
        puzzle.relocateTile(fromTile1,toTile1);
        
        assertFalse(puzzle.ok(),"You cannot move a non-existent tile or there is already a tile in the destination position. ");
    }
    
    
    // addGlue method
    
    @Test
    public void accordingPSshouldAddGlue(){
        puzzle.addGlue(1,5);
        assertTrue(puzzle.ok(),"You can add glue because that tile fulfills with all the conditions.");
    }
    
    @Test
    public void accordingPSshouldntAddGlueInvalidCoords(){
        puzzle.addGlue(1,-5);
        assertFalse(puzzle.ok(),"You cannot add a glue cuz there are invalid coordinates(negative or out of range).");
    }
    
    @Test
    public void accordingPSshouldntAddGlueHole(){
        puzzle.makeHole(8,1);
        puzzle.addGlue(8,1);
        assertFalse(puzzle.ok(),"You cannot add glue on a hole tile.");
    }
    
    @Test
    public void accordingPSshouldntAddGlueEmpty(){
        puzzle.addGlue(8,1);
        assertFalse(puzzle.ok(),"You cannot apply glue to an empty tile.");
    }
    
    @Test
    public void accordingPSshouldntAddGlueExistent(){
        puzzle.addGlue(0,0);
        puzzle.addGlue(0,0);
        assertFalse(puzzle.ok(),"This tile already has glue applied.");
    }
    
    // deleteGlue method
    
    @Test
    public void accordingPSshouldDeleteGlue(){
        puzzle.addGlue(0,0);
        puzzle.deleteGlue(0,0);
        assertTrue(puzzle.ok(),"You can delete glue because that tile fulfills with all the conditions.");
    }
    
    @Test
    public void accordingPSshouldntDeleteGlueInvalidCoords(){
        puzzle.deleteGlue(1,-5);
        assertFalse(puzzle.ok(),"You cannot delete glue cuz there are invalid coordinates(negative or out of range).");
    }
    
    @Test
    public void accordingPSshouldntDeleteGlueHole(){
        puzzle.makeHole(8,1);
        puzzle.deleteGlue(8,1);
        assertFalse(puzzle.ok(),"You cannot delete glue on a hole tile.");
    }
    
    @Test
    public void accordingPSshouldntDeleteGlueEmpty(){
        puzzle.deleteGlue(8,1);
        assertFalse(puzzle.ok(),"You cannot delete glue to an empty tile.");
    }
    
    @Test
    public void accordingPSshouldntAddGlueInexistent(){
        puzzle.deleteGlue(0,0);
        assertFalse(puzzle.ok(),"There is no glue to remove on this tile.");
    }
    
    // tilt method
    
    @Test
    public void accordingPSshouldTilt(){
        puzzle.tilt('r');
        puzzle.tilt('u');
        puzzle.tilt('d');
        puzzle.tilt('l');
        assertTrue(puzzle.ok(),"You can tilt the actual board cuz it fulfills all the conditions.");
    }
    
    @Test
    public void accordingPSshouldntTiltLabel(){
        puzzle.tilt('s');
        assertFalse(puzzle.ok(),"You cannot tilt the actual board cuz invalid direction(inexistent).");
    }
    
    // isGoal method
    
    @Test
    public void accordingPSshouldIsGoalBoolean(){
        puzzle.isGoal();
        assertTrue(puzzle.ok(),"Returns true cuz starting = ending.");
    }
    
    @Test
    public void accordingPSshouldntIsGoalBoolean(){
        puzzle.addTile(0,4,'b');
        puzzle.isGoal();
        assertFalse(puzzle.ok(),"Returns false cuz starting != ending.");
    }
    
    // makeVisible method
    @Test
    public void accordingPSshouldMakeVisible(){
        puzzle.makeVisible();
        assertTrue(puzzle.ok(),"Simulator makes visible.");
    }
    
    
    // makeInvisible method
    @Test
    public void accordingPSshouldMakeInvisible(){
        puzzle.makeInvisible();
        assertTrue(puzzle.ok(),"Simulator makes invisible.");
    }
    
    
    // finish method
    @Test
    public void accordingPSshouldfinish(){
        puzzle.finish();
        assertTrue(puzzle.ok(),"Finishes without mistakes.");
    }
    
    // actualArrangement method
    @Test
    public void accordingPSshouldActualArrangement(){
        puzzle.actualArrangement();
        assertTrue(puzzle.ok(),"It printed the actual arrangement correctly.");
    }
    
    
    // ok method
    @Test
    public void accordingPSshouldOk(){
        puzzle.ok();
        assertTrue(puzzle.ok(),"Ok.");
    }
    
    
}
