package puzzle;
import shapes.*;

import java.awt.Color;
import java.util.ArrayList;
import java.util.*;
import javax.swing.JOptionPane;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.HashSet;
import java.util.Set;


/**
 * This class represents a puzzle simulator with tiles, including initial and final boards,
 * movable tiles, holes, and glue. It allows operations such as adding, removing, moving tiles,
 * and applying or removing glue.
 *
 * @author Andersson David Sánchez Méndez
 * @author Cristian Santiago Pedraza Rodríguez

 * @version 2024
*/


public class Puzzle {   

    // Height and width of the board
    private int h;
    private int w;
    
    // Visual representations of the initial and final boards
    private Rectangle startingBoard;
    private Rectangle endingBoard;
    
    // Board color
    private Color color;
    
    // Character matrices representing the initial and target state of the board
    private char[][] starting;
    private char[][] ending;
    
    // Lists of lists of tiles representing the current board state and reference state
    private List<List<Tile>> tiles; // List of lists of tiles
    private List<List<Tile>> referingTiles; // List of lists of reference tiles
    
    // Determines if the simulator is visible
    private boolean visible = true;
    
    // Tracks if the last action was successful
    private boolean ok = true;
    
    // Auxiliary circle to represent holes
    private Circle circle;
    
    // Default board color
    public static Color lightBrown = new Color(207, 126, 60);
    
    // Matrix to track holes and list to store hole circles
    private boolean[][] holes;
    private List<Circle> holeCircles;
    
    
    /**
     * Constructor to initialize the boards without initial and final matrices.
     * 
     * @param h Height of the board.
     * @param w Width of the board.
     */
    public Puzzle(int h, int w) {
        this.h = h;
        this.w = w;
        this.color = lightBrown;
        this.tiles = new ArrayList<>();
        this.referingTiles = new ArrayList<>();
        
        // Initialize the hole matrix and the list of circles        
        holeCircles = new ArrayList<>();
        
        if (h > 0 && w > 0){
            holes = new boolean[h][w];
            
            // Create the initial and final boards
            startingBoard = new Rectangle(h,w,100,50,"starting");
            endingBoard = new Rectangle(h,w,350,50,"ending"); 
            
            // Create empty tiles on the initial board and reference tiles
            createEmptyTiles();
            createEmptyreferingTiles();
            this.ok = true;
            
        }
        else {
            showMessage("You cannot create the two boards with negative or zero h,w", "Error");
            this.ok = false; // Unsuccessful action
        }
        
    }

     /**
     * Constructor to initialize the boards with initial and final character matrices.
     * 
     * @param starting Initial state of the board.
     * @param ending Final state of the board.
     */
    public Puzzle(char[][] starting, char[][] ending) {
        this.h = starting.length;
        this.w = starting[0].length;
        this.starting = starting;
        this.ending = ending;
        this.tiles = new ArrayList<>();
        this.referingTiles = new ArrayList<>();
        this.color = lightBrown;
        
        // Initialize the hole matrix and the list of circles
        holes = new boolean[h][w];
        holeCircles = new ArrayList<>();
        
        // Create the boards
        startingBoard = new Rectangle(h, w, 100, 50, "starting");
        endingBoard = new Rectangle(h, w, 350, 50, "ending");
        
        // Create tiles based on the initial and final matrices
        createTiles(starting, tiles, 105, 55); // Initial position of the initial tiles
        createTiles(ending, referingTiles, w * (Tile.SIZE + Tile.MARGIN) + 355, 55); // Initial position of the reference board
        
        this.ok = true;
    }
    
    /**
     * Constructor to initialize an empty initial board and a final board with tiles.
     * 
     * @param ending Final state of the board.
     */
    public Puzzle(char[][] ending) {
        this.h = ending.length;
        this.w = ending[0].length;
        this.ending = ending;
        this.tiles = new ArrayList<>();
        this.referingTiles = new ArrayList<>();
        this.color = lightBrown;
       
        // Create the boards
        startingBoard = new Rectangle(h, w, 100, 50, "starting");
        endingBoard = new Rectangle(h, w, 350, 50, "ending");
        
        // Create empty tiles on the initial board and tiles on the final board
        createEmptyTiles();
        createTiles(ending, referingTiles, w * (Tile.SIZE + Tile.MARGIN) + 355, 55);
        
        this.ok = true;
    }
    
    
    /**
     * Method to create tiles in a list of lists, given the reference board.
     * 
     * @param board Character matrix representing the board.
     * @param tileList List of lists to store the created tiles.
     * @param xOffset X offset for positioning the tiles.
     * @param yOffset Y offset for positioning the tiles.
     */
    private void createTiles(char[][] board, List<List<Tile>> tileList, int xOffset, int yOffset) {
        for (int row = 0; row < board.length; row++) {
            List<Tile> rowList = new ArrayList<>();
            for (int col = 0; col < board[row].length; col++) {
                char label = board[row][col];
                int xPosition = xOffset + (col * (Tile.SIZE + Tile.MARGIN));
                int yPosition = yOffset + (row * (Tile.SIZE + Tile.MARGIN));
                Tile tile = new Tile(label, xPosition, yPosition, row, col);
                rowList.add(tile);
            }
            tileList.add(rowList);
        }
    }

    /**
     * Method to create empty tiles on the initial board.
     */
    private void createEmptyTiles() {
        for (int row = 0; row < h; row++) {
            List<Tile> rowList = new ArrayList<>();
            for (int col = 0; col < w; col++) {
                char label = '*';  // Empty tile
                int xPosition = 105 + (col * (Tile.SIZE + Tile.MARGIN));
                int yPosition = 55 + (row * (Tile.SIZE + Tile.MARGIN));
                Tile tile = new Tile(label, xPosition, yPosition, row, col);
                rowList.add(tile);
            }
            tiles.add(rowList);
        }
    }
    
    /**
     * Method to create empty reference tiles on the reference board.
     */
    private void createEmptyreferingTiles() {
        for (int row = 0; row < h; row++) {
            List<Tile> rowList = new ArrayList<>();
            for (int col = 0; col < w; col++) {
                char label = '*';  // Empty tile
                int xPosition = (w * (Tile.SIZE + Tile.MARGIN)) + 355 + (col * (Tile.SIZE + Tile.MARGIN));
                int yPosition = 55 + (row * (Tile.SIZE + Tile.MARGIN));
                Tile tile = new Tile(label, xPosition, yPosition, row, col);
                rowList.add(tile);
            }
            referingTiles.add(rowList);
        }
    }
    
    /**
     * Gets the height of the board.
     * 
     * @return Height of the board.
     */
    public int getHeight() {
        return this.h;
    }

    /**
     * Gets the width of the board.
     * 
     * @return Width of the board.
     */
    public int getWidth() {
        return this.w;
    }

    
    /**
     * Adds a tile to the board at the specified position.
     * 
     * @param row Row index of the tile.
     * @param column Column index of the tile.
     * @param label Label of the tile.
     */
    public void addTile(int row, int column, char label) {
        // List of valid labels for the tiles
        char[] validLabels = {'r', 'g', 'b', 'y'};

        // Validate if the label is valid
        boolean isValidLabel = false;
        for (char validLabel : validLabels) {
            if (label == validLabel) {
                isValidLabel = true;
                break;
            }
        }

        // If the label is invalid, show an error message
        if (!isValidLabel) {
            showMessage("Invalid label. Accepted labels are: r, g, b, y.", "Error");
            this.ok = false; // Error
            return;
        }

        // Other validations for the position
        if (row >= h || column >= w) {
            showMessage("You have exceeded the puzzle space.", "Error");
            this.ok = false; // Error
        } else if (row < 0 || column < 0) {
            showMessage("You're searching for a non-existent tile with negative position.", "Error");
            this.ok = false; // Error
        } else {
            Tile previousTile = tiles.get(row).get(column);

            // Check if the tile has a hole
            if (previousTile.getLabel() == 'h') {
                showMessage("You cannot add a tile on a hole.", "Error");
                this.ok = false; // Error
            }
            // Check if the tile is an empty cell
            else if (isTileEmpty(previousTile)) {
                previousTile.setTileColor(label); // Change the color of the tile
                previousTile.setLabel(label);
                previousTile.makeVisible();
                this.ok = true; // Successful action

            } else {
                showMessage("There is already a tile here.", "Error");
                this.ok = false; // Error
            }
        }
    }

    /**
     * Removes a tile from the board at the specified position.
     * 
     * @param row Row index of the tile.
     * @param column Column index of the tile.
     */
    public void deleteTile(int row, int column) {
        if (row >= h || column >= w) {
            showMessage("You have exceeded the puzzle space.", "Error");
            this.ok = false; // Error
        } else if (row < 0 || column < 0) {
            showMessage("You're searching for a non-existent tile with negative position.", "Error");
            this.ok = false; // Error
        } else {
            Tile previousTile = tiles.get(row).get(column);

            // Check if the tile has a hole
            if (previousTile.getLabel() == 'h') {
                showMessage("You cannot delete a tile that is a hole.", "Error");
                this.ok = false; // Error
            }else if (previousTile.hasGlue() || previousTile.isStuck()) {
                showMessage("You cannot delete a tile that has glue or is stuck.", "Error");
                this.ok = false;
            } else if (!isTileEmpty(previousTile)) {
                previousTile.setTileColor('n');
                previousTile.setLabel('*');
                previousTile.makeInvisible();
                this.ok = true; // Successful action
            } else {
                showMessage("You're trying to delete a non-existent tile.", "Error");
                this.ok = false; // Error
            }
        }
    }

    /**
     * Relocates a tile from the given source position to the destination position.
     * 
     * @param from the coordinates of the source position as an integer array [row, col].
     * @param to   the coordinates of the destination position as an integer array [row, col].
     */
    public void relocateTile(int[] from, int[] to) {
        // Validate input coordinates
        if (!areValidCoordinates(from) || !areValidCoordinates(to)) {
            showMessage("Invalid coordinates.", "Error");
            this.ok = false;
            return;
        }

        Tile fromTile = tiles.get(from[0]).get(from[1]);
        Tile toTile = tiles.get(to[0]).get(to[1]);

        // Validate existence of the source tile and availability of the destination tile
        if (fromTile.getLabel() == 'h') {
            showMessage("You cannot move a hole tile.", "Error");
            this.ok = false;

        } else if (toTile.getLabel() == 'h') {
            showMessage("You cannot relocate a tile to a position that has a hole.", "Error");
            this.ok = false;

        } else if (isTileEmpty(fromTile)) {
            showMessage("You cannot move a non-existent tile.", "Error");
            this.ok = false;

        } else if (!isTileEmpty(toTile)) {
            showMessage("There is already a tile in the destination position.", "Error");
            this.ok = false;

        } else if (fromTile.hasGlue() || fromTile.isStuck()) {
            showMessage("You cannot move a tile that has glue or is stuck.", "Error");
            this.ok = false;

        } else {
            // Perform the movement
            this.relocateTileMovement(fromTile, toTile, from, to);
            this.ok = true;
        }
    }

    /**
     * Performs the visual and logical movement of a tile from one position to another.
     * 
     * @param fromTile the tile to be moved from the source position.
     * @param toTile   the tile at the destination position (empty).
     * @param from     the source coordinates as an integer array [row, col].
     * @param to       the destination coordinates as an integer array [row, col].
     */
    private void relocateTileMovement(Tile fromTile, Tile toTile, int[] from, int[] to) {
        // Visually move the tile by updating its position
        fromTile.moveHorizontal((to[1] - from[1]) * (Tile.SIZE + Tile.MARGIN));
        fromTile.moveVertical((to[0] - from[0]) * (Tile.SIZE + Tile.MARGIN));

        // Update the tile list: move the tile to the new position
        tiles.get(to[0]).set(to[1], fromTile);

        // Update the tile's internal position
        fromTile.setRow(to[0]);
        fromTile.setCol(to[1]);

        // Create a new empty tile at the original position
        Tile emptyTile = createEmptyTile(from[0], from[1]);
        tiles.get(from[0]).set(from[1], emptyTile);
    }
    
    /**
     * Validates whether the given coordinates are within the bounds of the board.
     * 
     * @param coords the coordinates to validate as an integer array [row, col].
     * @return {@code true} if the coordinates are valid; {@code false} otherwise.
     */
    private boolean areValidCoordinates(int[] coords) {
        return coords.length == 2 && coords[0] >= 0 && coords[0] < h && coords[1] >= 0 && coords[1] < w;
    }  


    /**
     * Applies glue to a tile at the specified position.
     *
     * @param row the row of the tile.
     * @param column the column of the tile.
     */
    public void addGlue(int row, int column) {
        Tile tile = getTileAtPosition(row, column);
        if (tile == null) {
            showMessage("Invalid position.", "Error");
            this.ok = false;
        } else if (tile.getLabel() == 'h') {
            showMessage("You cannot add glue on a hole tile.", "Error");
            this.ok = false;

        } else if (isTileEmpty(tile)) {
            showMessage("Cannot apply glue to an empty tile.", "Error");
            this.ok = false;
        } else if (tile.hasGlue()) {
            showMessage("This tile already has glue applied.", "Error");
            this.ok = false;

        } else {
            tile.setHasGlue(true);

            // Change the tile color to a paler version
            Color evenPalerColor = getPaleColor(tile.getOriginalColor(), 150);
            tile.setTileColor(evenPalerColor);

            // Update adjacent tiles
            updateAdjacentTiles(tile);

            // Collect the group of stuck tiles
            List<Tile> group = new ArrayList<>();
            collectStuckGroup(tile, group);

            // Reset visited flags
            resetVisitedFlags();
            tile.setLabel('p');
            this.ok = true;
        }
    }

    /**
     * Removes glue from a tile at the specified position.
     * 
     * @param row the row of the tile.
     * @param column the column of the tile.
     */
    public void deleteGlue(int row, int column) {
        Tile tile = getTileAtPosition(row, column);
        if (tile == null) {
            showMessage("Invalid position.", "Error");
            this.ok = false;
        } else if (tile.getLabel() == 'h') {
            showMessage("You cannot delete glue on a hole tile.", "Error");
            this.ok = false;

        } else if (isTileEmpty(tile)) {
            showMessage("Cannot delete glue from an empty tile.", "Error");
            this.ok = false;

        } else if (!tile.hasGlue()) {
            showMessage("There is no glue to remove on this tile.", "Error");
            this.ok = false;

        } else {
            tile.setHasGlue(false);

            // If the tile is no longer stuck to any other, adjust its color
            if (!tile.isStuck()) {
                // Change the color to a slightly paler version
                Color slightlyPalerColor = getPaleColor(tile.getOriginalColor(), 50);
                tile.setTileColor(slightlyPalerColor);
            }

            // Update adjacent tiles
            updateAdjacentTilesAfterGlueRemoval(tile);

            // Collect the group of stuck tiles to update states
            List<Tile> group = new ArrayList<>();
            collectStuckGroup(tile, group);

            // Reset visited flags
            resetVisitedFlags();

            this.ok = true;
        }
    }

    /**
     * Updates the adjacent tiles after applying glue to a tile.
     * 
     * @param tile the tile to which glue has been applied.
     */
    private void updateAdjacentTiles(Tile tile) {
        int row = tile.getRow();
        int column = tile.getCol();
        int[][] directions = { { -1, 0 }, { 1, 0 }, { 0, -1 }, { 0, 1 } };
        for (int[] dir : directions) {
            int adjRow = row + dir[0];
            int adjCol = column + dir[1];
            Tile adjacentTile = getTileAtPosition(adjRow, adjCol);

            if (adjacentTile != null && !isTileEmpty(adjacentTile) && !adjacentTile.isStuck()
                    && !adjacentTile.getIsHole()) {
                adjacentTile.setIsStuck(true);
                // Change the color to a paler version
                Color paleColor = getPaleColor(adjacentTile.getOriginalColor(), 100);
                adjacentTile.setTileColor(paleColor);
            }
        }
    }


    /**
     * Updates the adjacent tiles after removing glue from a tile.
     * 
     * @param tile the tile from which glue has been removed.
     */
    private void updateAdjacentTilesAfterGlueRemoval(Tile tile) {
        int row = tile.getRow();
        int column = tile.getCol();
        int[][] directions = { { -1, 0 }, { 1, 0 }, { 0, -1 }, { 0, 1 } };
        for (int[] dir : directions) {
            int adjRow = row + dir[0];
            int adjCol = column + dir[1];
            Tile adjacentTile = getTileAtPosition(adjRow, adjCol);

            if (adjacentTile != null && !isTileEmpty(adjacentTile) && !adjacentTile.getIsHole()) {
                if (isAdjacentToGlue(adjacentTile)) {
                    // If still adjacent to another tile with glue, remain stuck
                    continue;
                } else {
                    adjacentTile.setIsStuck(false);
                    if (!adjacentTile.hasGlue()) {
                        // Change the color to a slightly paler version
                        Color slightlyPalerColor = getPaleColor(adjacentTile.getOriginalColor(), 50);
                        adjacentTile.setTileColor(slightlyPalerColor);
                    }
                }
            }
        }
    }

    /**
     * Checks if a tile is adjacent to any tile with glue applied.
     * 
     * @param tile the tile to check.
     * @return {@code true} if adjacent to a tile with glue; {@code false} otherwise.
     */
    private boolean isAdjacentToGlue(Tile tile) {
        int row = tile.getRow();
        int column = tile.getCol();
        int[][] directions = { { -1, 0 }, { 1, 0 }, { 0, -1 }, { 0, 1 } };

        for (int[] dir : directions) {
            int adjRow = row + dir[0];
            int adjCol = column + dir[1];
            Tile adjacentTile = getTileAtPosition(adjRow, adjCol);
            if (adjacentTile != null && adjacentTile.hasGlue()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Generates a paler version of the given color based on the paleness factor.
     * 
     * @param color the original color.
     * @param palenessFactor the amount to increase each RGB component.
     * @return a new {@code Color} object with adjusted RGB values.
     */
    private Color getPaleColor(Color color, int palenessFactor) {
        int r = Math.min(255, color.getRed() + palenessFactor);
        int g = Math.min(255, color.getGreen() + palenessFactor);
        int b = Math.min(255, color.getBlue() + palenessFactor);
        return new Color(r, g, b);
    }

    /**
     * Tilts the board in the specified direction.
     * 
     * @param direction the direction to tilt ('d' for down, 'u' for up, 'r' for right, 'l' for left).
     */
    public void tilt(char direction) {
        switch (direction) {
            case 'd':
                for (int col = 0; col < w; col++) {
                    tiltDownWithGlue(col);
                }
                break;
            case 'u':
                for (int col = 0; col < w; col++) {
                    tiltUpWithGlue(col);
                }
                break;
            case 'r':
                for (int row = 0; row < h; row++) {
                    tiltRightWithGlue(row);
                }
                break;
            case 'l':
                for (int row = 0; row < h; row++) {
                    tiltLeftWithGlue(row);
                }
                break;
            default:
                showMessage("Invalid direction.", "Error");
                this.ok = false;
        }
        resetVisitedFlags(); // Reset visited flags after tilting
    }

    /**
     * Performs a single tilt in the specified direction.
     * 
     * @param direction the direction to tilt ('d' for down, 'u' for up, 'r' for right, 'l' for left).
     * @return {@code true} if there were changes; {@code false} otherwise.
     */
    public boolean tiltOnce(char direction) {
        // Implement a version of tilt that returns true if there were changes
        // and false if there were no changes.
        // For simplicity, it currently always returns true.
        tilt(direction);
        return true; 
    }


    /**
     * Tilts the specified column upwards, handling glue and stuck tiles.
     * 
     * @param col the column to tilt upwards.
     */
    private void tiltUpWithGlue(int col) {
        List<List<Tile>> groups = new ArrayList<>();

        for (int row = 0; row < h; row++) {
            Tile tile = getTileAtPosition(row, col);
            if (!isTileEmpty(tile) && !tile.isVisited() && !tile.getIsHole()) {
                List<Tile> group = new ArrayList<>();
                boolean isGluedOrStuck = tile.isStuck() || tile.hasGlue();
                if (isGluedOrStuck) {
                    collectStuckGroup(tile, group);
                } else {
                    tile.setVisited(true);
                    group.add(tile);
                }
                groups.add(group);
            }
        }

        // Reset visited flags
        resetVisitedFlags();

        // Sort groups by the minimum row (upper tiles first)
        groups.sort(Comparator.comparingInt(g -> g.stream().mapToInt(Tile::getRow).min().orElse(h)));

        // Move the groups
        for (List<Tile> group : groups) {
            boolean isGluedOrStuck = group.get(0).isStuck() || group.get(0).hasGlue();
            int maxMove = calculateMaxMoveUpGroup(group, isGluedOrStuck);
            if (maxMove == -1) {
                if (!isGluedOrStuck) {
                    // Remove free tiles that fall into a hole
                    for (Tile tile : group) {
                        tiles.get(tile.getRow()).set(tile.getCol(), createEmptyTile(tile.getRow(), tile.getCol()));
                        tile.makeInvisible();
                        tile.setLabel('*');
                    }
                }
                // Glued or stuck tiles do not move
            } else {
                moveGroupUp(group, maxMove);
            }
        }
    }

    /**
     * Tilts the specified column downwards, handling glue and stuck tiles.
     * 
     * @param col the column to tilt downwards.
     */
    private void tiltDownWithGlue(int col) {
        List<List<Tile>> groups = new ArrayList<>();

        for (int row = h - 1; row >= 0; row--) {
            Tile tile = getTileAtPosition(row, col);
            if (!isTileEmpty(tile) && !tile.isVisited() && !tile.getIsHole()) {
                List<Tile> group = new ArrayList<>();
                boolean isGluedOrStuck = tile.isStuck() || tile.hasGlue();
                if (isGluedOrStuck) {
                    collectStuckGroup(tile, group);
                } else {
                    tile.setVisited(true);
                    group.add(tile);
                }
                groups.add(group);
            }
        }

        // Reset visited flags
        resetVisitedFlags();

        // Sort groups by the maximum row (lower tiles first)
        groups.sort((g1, g2) -> {
            int maxRow1 = g1.stream().mapToInt(Tile::getRow).max().orElse(-1);
            int maxRow2 = g2.stream().mapToInt(Tile::getRow).max().orElse(-1);
            return Integer.compare(maxRow2, maxRow1);
        });

        // Move the groups
        for (List<Tile> group : groups) {
            boolean isGluedOrStuck = group.get(0).isStuck() || group.get(0).hasGlue();
            int maxMove = calculateMaxMoveDownGroup(group, isGluedOrStuck);
            if (maxMove == -1) {
                if (!isGluedOrStuck) {
                    // Remove free tiles that fall into a hole
                    for (Tile tile : group) {
                        tiles.get(tile.getRow()).set(tile.getCol(), createEmptyTile(tile.getRow(), tile.getCol()));
                        tile.makeInvisible();
                        tile.setLabel('*');
                    }
                }
                // Glued or stuck tiles do not move
            } else {
                moveGroupDown(group, maxMove);
            }
        }
    }

    /**
     * Tilts the specified row to the left, handling glue and stuck tiles.
     * 
     * @param row the row to tilt to the left.
     */
    private void tiltLeftWithGlue(int row) {
        List<List<Tile>> groups = new ArrayList<>();

        for (int col = 0; col < w; col++) {
            Tile tile = getTileAtPosition(row, col);
            if (!isTileEmpty(tile) && !tile.isVisited() && !tile.getIsHole()) {
                List<Tile> group = new ArrayList<>();
                boolean isGluedOrStuck = tile.isStuck() || tile.hasGlue();
                if (isGluedOrStuck) {
                    collectStuckGroup(tile, group);
                } else {
                    tile.setVisited(true);
                    group.add(tile);
                }
                groups.add(group);
            }
        }

        // Reset visited flags
        resetVisitedFlags();

        // Sort groups by the minimum column (leftmost tiles first)
        groups.sort(Comparator.comparingInt(g -> g.stream().mapToInt(Tile::getCol).min().orElse(w)));

        // Move the groups
        for (List<Tile> group : groups) {
            boolean isGluedOrStuck = group.get(0).isStuck() || group.get(0).hasGlue();
            int maxMove = calculateMaxMoveLeftGroup(group, isGluedOrStuck);
            if (maxMove == -1) {
                if (!isGluedOrStuck) {
                    // Remove free tiles that fall into a hole
                    for (Tile tile : group) {
                        tiles.get(tile.getRow()).set(tile.getCol(), createEmptyTile(tile.getRow(), tile.getCol()));
                        tile.makeInvisible();
                        tile.setLabel('*');
                    }
                }
                // Glued or stuck tiles do not move
            } else {
                moveGroupLeft(group, maxMove);
            }
        }
    }


    /**
     * Tilts the specified row to the right, considering glue and stuck tiles.
     * 
     * @param row the row index to tilt to the right.
     */
    private void tiltRightWithGlue(int row) {
        List<List<Tile>> groups = new ArrayList<>();
    
        // Iterate from the rightmost column to the left
        for (int col = w - 1; col >= 0; col--) {
            Tile tile = getTileAtPosition(row, col);
            if (!isTileEmpty(tile) && !tile.isVisited() && !tile.getIsHole()) {
                List<Tile> group = new ArrayList<>();
                boolean isGluedOrStuck = tile.isStuck() || tile.hasGlue();
                if (isGluedOrStuck) {
                    // Collect all tiles that are glued or stuck together
                    collectStuckGroup(tile, group);
                } else {
                    // Mark the tile as visited and add it to the group
                    tile.setVisited(true);
                    group.add(tile);
                }
                groups.add(group);
            }
        }
    
        // Reset the visited flags for all tiles
        resetVisitedFlags();
    
        // Sort the groups based on the maximum column index (rightmost first)
        groups.sort((g1, g2) -> {
            int maxCol1 = g1.stream().mapToInt(Tile::getCol).max().orElse(-1);
            int maxCol2 = g2.stream().mapToInt(Tile::getCol).max().orElse(-1);
            return Integer.compare(maxCol2, maxCol1);
        });
    
        // Move each group to the right
        for (List<Tile> group : groups) {
            boolean isGluedOrStuck = group.get(0).isStuck() || group.get(0).hasGlue();
            int maxMove = calculateMaxMoveRightGroup(group, isGluedOrStuck);
            if (maxMove == -1) {
                if (!isGluedOrStuck) {
                    // Remove free tiles that fall into a hole
                    for (Tile tile : group) {
                        tiles.get(tile.getRow()).set(tile.getCol(), createEmptyTile(tile.getRow(), tile.getCol()));
                        tile.makeInvisible();
                        tile.setLabel('*');
                    }
                }
                // Glued or stuck tiles do not move
            } else {
                // Move the group to the right by the maximum possible steps
                moveGroupRight(group, maxMove);
            }
        }
    }


    // Adjusted maximum movement calculation methods to handle tiles with holes
    
    // Movement upwards
    /**
     * Calculates the maximum possible upward movement for a tile.
     *
     * @param row The current row of the tile.
     * @param column The current column of the tile.
     * @param group The group of tiles that are considered to be moving together.
     * @param isGluedOrStuck Indicates if the tile is glued or stuck.
     * @return The maximum number of steps the tile can move up, or -1 if it would fall into a hole.
     */
    private int calculateMaxMoveUp(int row,int column, List<Tile> group, boolean isGluedOrStuck) {
        int maxMove = 0;
        for (int i = row - 1; i >= 0; i--) {
            Tile nextTile = getTileAtPosition(i, column);
            if (nextTile.getIsHole()) {
                if (isGluedOrStuck) {
                    // Stuck tile: it cannot move to the hole, stop before
                    break;
                } else {
                    // Free tile: it would fall in the hole 
                    return -1; // Indicates that the free tile will fall in the hole 
                }
            } else if (isTileEmpty(nextTile) || (group != null && group.contains(nextTile))) {
                maxMove++;
            } else {
                break;
            }
        }
        return maxMove;
    }

    /**
     * Calculates the maximum possible upward movement for a group of tiles.
     *
     * @param group The group of tiles that are considered to be moving together.
     * @param isGluedOrStuck Indicates if the tiles are glued or stuck.
     * @return The maximum number of steps the group can move up, or -1 if any tile would fall into a hole.
     */
    private int calculateMaxMoveUpGroup(List<Tile> group, boolean isGluedOrStuck) {
        int maxMove = h;
        for (Tile tile : group) {
            int tileMaxMove = calculateMaxMoveUp(tile.getRow(), tile.getCol(), group, isGluedOrStuck);
            if (tileMaxMove == -1) {
                // One of the free tile of the group will fall in a hole
                return -1;
            }
            maxMove = Math.min(maxMove, tileMaxMove);
        }
        return maxMove;
    }


    // Movement downwards
    /**
     * Calculates the maximum possible downward movement for a tile.
     *
     * @param row The current row of the tile.
     * @param column The current column of the tile.
     * @param group The group of tiles that are considered to be moving together.
     * @param isGluedOrStuck Indicates if the tile is glued or stuck.
     * @return The maximum number of steps the tile can move down, or -1 if it would fall into a hole.
     */
    private int calculateMaxMoveDown(int row,int column, List<Tile> group, boolean isGluedOrStuck) {
        int maxMove = 0;
        for (int i = row + 1; i < h; i++) {
            Tile nextTile = getTileAtPosition(i, column);
            if (nextTile.getIsHole()) {
                if (isGluedOrStuck) {
                    break;
                } else {
                    return -1;
                }
            } else if (isTileEmpty(nextTile) || (group != null && group.contains(nextTile))) {
                maxMove++;
            } else {
                break;
            }
        }
        return maxMove;
    }

    /**
     * Calculates the maximum possible downward movement for a group of tiles.
     *
     * @param group The group of tiles that are considered to be moving together.
     * @param isGluedOrStuck Indicates if the tiles are glued or stuck.
     * @return The maximum number of steps the group can move down, or -1 if any tile would fall into a hole.
     */
    private int calculateMaxMoveDownGroup(List<Tile> group, boolean isGluedOrStuck) {
        int maxMove = h;
        for (Tile tile : group) {
            int tileMaxMove = calculateMaxMoveDown(tile.getRow(), tile.getCol(), group, isGluedOrStuck);
            if (tileMaxMove == -1) {
                return -1;
            }
            maxMove = Math.min(maxMove, tileMaxMove);
        }
        return maxMove;
    }

    // Movement leftwards
    /**
     * Calculates the maximum possible leftward movement for a tile.
     *
     * @param row The current row of the tile.
     * @param column The current column of the tile.
     * @param group The group of tiles that are considered to be moving together.
     * @param isGluedOrStuck Indicates if the tile is glued or stuck.
     * @return The maximum number of steps the tile can move left, or -1 if it would fall into a hole.
     */
    private int calculateMaxMoveLeft(int row,int column, List<Tile> group, boolean isGluedOrStuck) {
        int maxMove = 0;
        for (int i = column - 1; i >= 0; i--) {
            Tile nextTile = getTileAtPosition(row, i);
            if (nextTile.getIsHole()) {
                if (isGluedOrStuck) {
                    break;
                } else {
                    return -1;
                }
            } else if (isTileEmpty(nextTile) || (group != null && group.contains(nextTile))) {
                maxMove++;
            } else {
                break;
            }
        }
        return maxMove;
    }

    /**
     * Calculates the maximum possible leftward movement for a group of tiles.
     *
     * @param group The group of tiles that are considered to be moving together.
     * @param isGluedOrStuck Indicates if the tiles are glued or stuck.
     * @return The maximum number of steps the group can move left, or -1 if any tile would fall into a hole.
     */
    private int calculateMaxMoveLeftGroup(List<Tile> group, boolean isGluedOrStuck) {
        int maxMove = w;
        for (Tile tile : group) {
            int tileMaxMove = calculateMaxMoveLeft(tile.getRow(), tile.getCol(), group, isGluedOrStuck);
            if (tileMaxMove == -1) {
                return -1;
            }
            maxMove = Math.min(maxMove, tileMaxMove);
        }
        return maxMove;
    }

    // Movement rightwards
    /**
     * Calculates the maximum possible rightward movement for a tile.
     *
     * @param row The current row of the tile.
     * @param column The current column of the tile.
     * @param group The group of tiles that are considered to be moving together.
     * @param isGluedOrStuck Indicates if the tile is glued or stuck.
     * @return The maximum number of steps the tile can move right, or -1 if it would fall into a hole.
     */
    private int calculateMaxMoveRight(int row,int column, List<Tile> group, boolean isGluedOrStuck) {
        int maxMove = 0;
        for (int i = column + 1; i < w; i++) {
            Tile nextTile = getTileAtPosition(row, i);
            if (nextTile.getIsHole()) {
                if (isGluedOrStuck) {
                    break;
                } else {
                    return -1;
                }
            } else if (isTileEmpty(nextTile) || (group != null && group.contains(nextTile))) {
                maxMove++;
            } else {
                break;
            }
        }
        return maxMove;
    }
    
    /**
     * Calculates the maximum possible rightward movement for a group of tiles.
     *
     * @param group The group of tiles that are considered to be moving together.
     * @param isGluedOrStuck Indicates if the tiles are glued or stuck.
     * @return The maximum number of steps the group can move right, or -1 if any tile would fall into a hole.
     */
    private int calculateMaxMoveRightGroup(List<Tile> group, boolean isGluedOrStuck) {
        int maxMove = w;
        for (Tile tile : group) {
            int tileMaxMove = calculateMaxMoveRight(tile.getRow(), tile.getCol(), group, isGluedOrStuck);
            if (tileMaxMove == -1) {
                return -1;
            }
            maxMove = Math.min(maxMove, tileMaxMove);
        }
        return maxMove;
    }

// Methods for moving groups

    /**
     * Moves a group of tiles upward by a specified number of steps.
     * The tiles are sorted such that the uppermost tiles move first.
     *
     * @param group List of tiles to be moved
     * @param steps Number of steps to move upward
     */
    private void moveGroupUp(List<Tile> group, int steps) {
        if (steps == 0) return;
        // Order the group because the higher tiles move firstly
        group.sort(Comparator.comparingInt(Tile::getRow));
        // Delete tiles of last positions 
        for (Tile tile : group) {
            tiles.get(tile.getRow()).set(tile.getCol(), createEmptyTile(tile.getRow(), tile.getCol()));
        }
        // Move tiles to the new positions
        for (Tile tile : group) {
            int newRow = tile.getRow() - steps;
            tile.moveVertical(-steps * (Tile.SIZE + Tile.MARGIN));
            tiles.get(newRow).set(tile.getCol(), tile);
            tile.setRow(newRow);
        }
    }

     /**
     * Moves a group of tiles downward by a specified number of steps.
     * The tiles are sorted such that the bottommost tiles move first.
     *
     * @param group List of tiles to be moved
     * @param steps Number of steps to move downward
     */
    private void moveGroupDown(List<Tile> group, int steps) {
        if (steps == 0) return;
        // Order the group because the lower tiles move firstly
        group.sort((t1, t2) -> Integer.compare(t2.getRow(), t1.getRow()));
        // Delete tiles of last positions 
        for (Tile tile : group) {
            tiles.get(tile.getRow()).set(tile.getCol(), createEmptyTile(tile.getRow(), tile.getCol()));
        }
        // Move tiles to the new positions
        for (Tile tile : group) {
            int newRow = tile.getRow() + steps;
            tile.moveVertical(steps * (Tile.SIZE + Tile.MARGIN));
            tiles.get(newRow).set(tile.getCol(), tile);
            tile.setRow(newRow);
        }
    }

    /**
     * Moves a group of tiles to the left by a specified number of steps.
     * The tiles are sorted such that the leftmost tiles move first.
     *
     * @param group List of tiles to be moved
     * @param steps Number of steps to move to the left
     */
    private void moveGroupLeft(List<Tile> group, int steps) {
        if (steps == 0) return;
        // Order the group because the tiles with lower columnsa move firstly
        group.sort(Comparator.comparingInt(Tile::getCol));
        // Delete tiles of last positions
        for (Tile tile : group) {
            tiles.get(tile.getRow()).set(tile.getCol(), createEmptyTile(tile.getRow(), tile.getCol()));
        }
        // Move tiles to the new positions
        for (Tile tile : group) {
            int newCol = tile.getCol() - steps;
            tile.moveHorizontal(-steps * (Tile.SIZE + Tile.MARGIN));
            tiles.get(tile.getRow()).set(newCol, tile);
            tile.setCol(newCol);
        }
    }

    /**
     * Moves a group of tiles to the right by a specified number of steps.
     * The tiles are sorted such that the rightmost tiles move first.
     *
     * @param group List of tiles to be moved
     * @param steps Number of steps to move to the right
     */
    private void moveGroupRight(List<Tile> group, int steps) {
        if (steps == 0) return;
        // Order the group because the tiles with higher columns move firstly
        group.sort((t1, t2) -> Integer.compare(t2.getCol(), t1.getCol()));
        // Delete tiles of last positions
        for (Tile tile : group) {
            tiles.get(tile.getRow()).set(tile.getCol(), createEmptyTile(tile.getRow(), tile.getCol()));
        }
        // Move tiles to the new positions
        for (Tile tile : group) {
            int newCol = tile.getCol() + steps;
            tile.moveHorizontal(steps * (Tile.SIZE + Tile.MARGIN));
            tiles.get(tile.getRow()).set(newCol, tile);
            tile.setCol(newCol);
        }
    }

    /**
     * Collects all tiles that are stuck together as a group, starting from the given tile.
     * Uses depth-first search to recursively find connected tiles.
     *
     * @param tile The starting tile
     * @param group List to store the collected tiles
     */
    private void collectStuckGroup(Tile tile, List<Tile> group) {
        if (tile.isVisited()) return;
        tile.setVisited(true);
        group.add(tile);
        int row = tile.getRow();
       int column = tile.getCol();
        int[][] directions = { { -1, 0 }, { 1, 0 }, { 0, -1 }, { 0, 1 } };
        for (int[] dir : directions) {
            int adjRow = row + dir[0];
            int adjCol = column + dir[1];
            Tile adjacentTile = getTileAtPosition(adjRow, adjCol);
            if (adjacentTile != null && !adjacentTile.isVisited() && !isTileEmpty(adjacentTile) &&
                    (adjacentTile.isStuck() || adjacentTile.hasGlue()) && !adjacentTile.getIsHole()) {
                collectStuckGroup(adjacentTile, group);
            }
        }
    }

    /**
     * Retrieves the tile at a specific position.
     *
     * @param row Row index
     * @param column Column index
     * @return The tile at the specified position, or null if out of bounds
     */
    public Tile getTileAtPosition(int row,int column) {
        if (row >= 0 && row < h && column >= 0 && column < w) {
            return tiles.get(row).get(column);
        }
        return null;
    }

    /**
     * Checks if a given tile is empty.
     *
     * @param tile The tile to check
     * @return True if the tile is empty, false otherwise
     */
    private boolean isTileEmpty(Tile tile) {
        if (tile == null) return false;
        return !tile.getIsHole() && tile.getLabel() == '*';
    }

    /**
     * Resets the "visited" flags for all tiles after a tilt operation.
     */
    private void resetVisitedFlags() {
        for (List<Tile> rowList : tiles) {
            for (Tile tile : rowList) {
                tile.setVisited(false);
            }
        }
    }

    /**
     * Creates an empty tile at the given position.
     *
     * @param row Row index
     * @param column Column index
     * @return A new empty tile
     */
    private Tile createEmptyTile(int row,int column) {
        int xPosition = 105 + (column * (Tile.SIZE + Tile.MARGIN));
        int yPosition = 55 + (row * (Tile.SIZE + Tile.MARGIN));
        Tile emptyTile;
        if (holes[row][column]) {
            emptyTile = new Tile('h', xPosition, yPosition, row, column);
            emptyTile.setIsHole(true);
            createHoleCircle(emptyTile);
        } else {
            emptyTile = new Tile('*', xPosition, yPosition, row, column);
        }
        return emptyTile;
    }
    
    /**
     * Displays an error message if the simulator is visible.
     * @param message The message to display.
     * @param title The title of the message.
     */
    private void showMessage(String message, String title) {
        if (this.visible) {
            JOptionPane.showMessageDialog(null, message, title, JOptionPane.ERROR_MESSAGE);
            this.ok = false;
        }
    }
    
    /**
     * Checks if the puzzle has reached the goal state.
     * Compares the current board (tiles) with the reference board (ending).
     *
     * @return True if the puzzle is in the goal state, false otherwise
     */
    public boolean isGoal() {
        // Move in in the actual board(tiles) and compare it with the reference board(ending)
        for (int row = 0; row < h; row++) {
            for (int col = 0; col < w; col++) {
                Tile currentTile = tiles.get(row).get(col);
                char currentLabel = currentTile.getLabel();
                
                Tile targetTile = referingTiles.get(row).get(col);
                char targetLabel = targetTile.getLabel();
                
                if (currentLabel == 'h' || targetLabel == 'h'){
                    continue;
                }
                // Compare the actual tile with the tile in the objective state
                if (currentLabel != targetLabel) {
                    this.ok = false;
                    return false;  // If that's not true, the final state hasn't been reached 
                }
                
            }
        }
        
        // If all tiles asimilates with the reference tiles, so we reached the final state
        this.ok = true;
        return true;
    }


    /**
     * Makes the simulator visible.
     */
    
    public void makeVisible(){
        this.visible = true;
        
        // Verify if the boards have been initialized
        if (startingBoard != null) {
            startingBoard.makeVisible();  // Make visible the initial board
        }
        
        if (endingBoard != null) {
            endingBoard.makeVisible();    // Make visible the final board
        }
        
        
        for (List<Tile> row : tiles) {
            for (Tile tile : row) {
                tile.makeVisible();
            }
        }
        
        for (List<Tile> row : referingTiles) {
            for (Tile tile : row) {
                tile.makeVisible();
            }
        }
        
        this.ok = true;  // Successful action
        
    }
    
    /**
     * Makes the simulator invisible.
     */
    
    public void makeInvisible(){
        this.visible = false;
        
        // Hacer invisibles las baldosas
        for (List<Tile> row : tiles) {
            for (Tile tile : row) {
                tile.makeInvisible();
            }
        }
        
        for (List<Tile> row : referingTiles) {
            for (Tile tile : row) {
                tile.makeInvisible();
            }
        }
        
        // Make visible the boards (rectangles)
        if (startingBoard != null) {
            startingBoard.makeInvisible();  // Make invisible the initial board
        }
        
        if (endingBoard != null) {
            endingBoard.makeInvisible();    // Make invisible the final board
        }
        
        this.ok = true;  // Successful action
    }

    /**
     * Ends the simulator and exits the program.
     */
    public void finish() {
        System.out.println("The simulator has been finished.");
        System.exit(0);
        this.ok = true;
    }

    /**
     * Returns a copy of the current puzzle board (starting), representing the current state.
     *
     * @return A copy of the starting matrix
     */
    
    public char[][] actualArrangement() {
        // Create a copy of starting matrix
        char[][] currentArrangement = new char[h][w];
        
        for (int row = 0; row < h; row++) {
            for (int col = 0; col < w; col++) {
                Tile tile = getTileAtPosition(row, col);
                char label = tile.getLabel();
                System.out.print(label + " ");
            }
            System.out.println();
        }
        
        System.out.println();
        
        for (int row = 0; row < h; row++) {
            for (int col = 0; col < w; col++) {
                currentArrangement[row][col] = starting[row][col]; // Copia el valor actual
    
                // Simulation of painting or showing the tiles
                Tile tile = tiles.get(row).get(col);
                System.out.println("Baldosa en (" + row + ", " + col + "): " + tile.getLabel());
            }
        }
        
        this.ok = true;
        
        return currentArrangement; // Return the matrix copy
        
        
    }
    
    /**
     * Returns whether the last action was successful.
     *
     * @return True if the last action was successful, false otherwise
     */
    public boolean ok() {
        return this.ok;
    }

    /**
     * Exchanges the current puzzle board with the reference board.
     * Swaps the starting and ending matrices and updates the tiles visually.
     */
    
    public void exchange() {
        // Exchange character matrixes
        char[][] temp = starting;
        starting = ending;
        ending = temp;
    
        // Exchange tiles lists
        List<List<Tile>> tempTiles = tiles;
        tiles = referingTiles;
        referingTiles = tempTiles;
        
        // Exchange tiles positions visually 
        for (int row = 0; row < h; row++) {
            for (int col = 0; col < w; col++) {
                Tile startingTile = tiles.get(row).get(col);
                int xPositionStartingTile = startingTile.getXPos();
                int yPositionStartingTile = startingTile.getYPos();
    
                Tile endingTile = referingTiles.get(row).get(col);
                int xPositionEndingTile = endingTile.getXPos();
                int yPositionEndingTile = endingTile.getYPos();
    
                // Calculate the difference in positions
                int deltaXStarting = xPositionEndingTile - xPositionStartingTile;
                int deltaYStarting = yPositionEndingTile - yPositionStartingTile;
    
                int deltaXEnding = xPositionStartingTile - xPositionEndingTile;
                int deltaYEnding = yPositionStartingTile - yPositionEndingTile;
    
                // Move the tiles to the new positions
                startingTile.moveHorizontal(deltaXStarting);
                startingTile.moveVertical(deltaYStarting);
                startingTile.setXPos(xPositionEndingTile);
                startingTile.setYPos(yPositionEndingTile);
    
                endingTile.moveHorizontal(deltaXEnding);
                endingTile.moveVertical(deltaYEnding);
                endingTile.setXPos(xPositionStartingTile);
                endingTile.setYPos(yPositionStartingTile);
            }
        }
        
        this.ok = true;
        System.out.println("Boards have been exchanged. Now, you're editing the board that was the reference board before.");
    }
    
    /**
     * Creates a hole in a specified tile position.
     *
     * @param row Row index
     * @param column Column index
     */
    public void makeHole(int row,int column) {
        // Validate the coords
        if (row >= h || column >= w) {
            showMessage("You have exceeded the puzzle space.", "Error");
            this.ok = false; // Error Message

        } else if (row < 0 || column < 0) {
            showMessage("You cannot make a hole in a non-existent tile with negative position.", "Error");
            this.ok = false; // Error Message

        } else {

            Tile targetTile = tiles.get(row).get(column);

            if (targetTile.getLabel() == 'h') {
                showMessage("This tile already has a hole.", "Error");
                this.ok = false; // Error message
            } else if (isTileEmpty(targetTile) && !targetTile.getIsHole()) {

                // Mark the tile as hole
                targetTile.setLabel('h');
                targetTile.setIsHole(true);
                holes[row][column] = true;
                createHoleCircle(targetTile);
                this.ok = true; // Successful action

            } else {
                showMessage("You can only make a hole in an empty tile.", "Error");
                this.ok = false; // Error message
            }
        }
    }
    
    /**
     * Smart tilt method that performs an intelligent tilt to bring the puzzle closer to the solution.
     */
    public void tilt() {
        // Get the current board configuration
        char[][] currentArrangement = getCurrentArrangement();
    
        // Use BFS to find the sequence of moves from the current configuration to the solution
        List<Character> moves = bfsSolve(currentArrangement, ending);
    
        // If a sequence of moves is found, apply the first move
        if (moves != null && !moves.isEmpty()) {
            char firstMove = moves.get(0);
            tilt(firstMove);
            System.out.println("An intelligent tilt has been applied towards " + directionToString(firstMove));
            this.ok = true;
        } else {
            System.out.println("No possible moves to bring the puzzle closer to the solution.");
            this.ok = false;
        }
    }
    
    /**
     * Private method to get the current board configuration.
     */
    private char[][] getCurrentArrangement() {
        char[][] currentArrangement = new char[h][w];
        for (int row = 0; row < h; row++) {
            for (int col = 0; col < w; col++) {
                Tile tile = getTileAtPosition(row, col);
                currentArrangement[row][col] = tile.getLabel();
            }
        }
        return currentArrangement;
    }
    
    /**
     * BFS implementation to find the sequence of tilts from the current configuration to the solution.
     */
    private List<Character> bfsSolve(char[][] startConfig, char[][] goalConfig) {
        // Inner class to represent the state of the board
        class State {
            char[][] board;
            List<Character> moves;
    
            State(char[][] board, List<Character> moves) {
                this.board = board;
                this.moves = moves;
            }
    
            // Generate a unique key for the board state
            String getKey() {
                return Arrays.deepToString(board);
            }
        }
    
        // Possible tilt directions
        char[] directions = {'u', 'd', 'l', 'r'};
    
        // Initialize the BFS queue and the set of visited states
        Queue<State> queue = new LinkedList<>();
        Set<String> visited = new HashSet<>();
    
        // Add initial state to the queue
        State initialState = new State(copyBoard(startConfig), new ArrayList<>());
        queue.add(initialState);
        visited.add(initialState.getKey());
    
        while (!queue.isEmpty()) {
            State currentState = queue.poll();
    
            // Check if the current state matches the goal configuration
            if (boardsEqual(currentState.board, goalConfig)) {
                return currentState.moves;
            }
    
            // Generate next possible states
            for (char direction : directions) {
                char[][] newBoard = tiltBoard(currentState.board, direction);
                String key = Arrays.deepToString(newBoard);
    
                if (!visited.contains(key)) {
                    visited.add(key);
                    List<Character> newMoves = new ArrayList<>(currentState.moves);
                    newMoves.add(direction);
                    queue.add(new State(newBoard, newMoves));
                }
            }
        }
    
        // Return null if no solution is found
        return null;
    }
    
    // Helper methods for BFS and board manipulation
    
    /**
     * Copy a board.
     */
    private char[][] copyBoard(char[][] board) {
        return Arrays.stream(board).map(char[]::clone).toArray(char[][]::new);
    }
    
    /**
     * Check if two boards are equal.
     */
    private boolean boardsEqual(char[][] board1, char[][] board2) {
        return Arrays.deepEquals(board1, board2);
    }
    
    /**
     * Apply a tilt to a board and return the resulting new board.
     */
    private char[][] tiltBoard(char[][] board, char direction) {
        int h = board.length;
        int w = board[0].length;
        char[][] newBoard = copyBoard(board);
    
        switch (direction) {
            case 'u':
                for (int col = 0; col < w; col++) {
                    int insertPos = 0;
                    for (int row = 0; row < h; row++) {
                        if (newBoard[row][col] != '*') {
                            newBoard[insertPos++][col] = newBoard[row][col];
                            if (insertPos - 1 != row) {
                                newBoard[row][col] = '*';
                            }
                        }
                    }
                }
                break;
            case 'd':
                for (int col = 0; col < w; col++) {
                    int insertPos = h - 1;
                    for (int row = h - 1; row >= 0; row--) {
                        if (newBoard[row][col] != '*') {
                            newBoard[insertPos--][col] = newBoard[row][col];
                            if (insertPos + 1 != row) {
                                newBoard[row][col] = '*';
                            }
                        }
                    }
                }
                break;
            case 'l':
                for (int row = 0; row < h; row++) {
                    int insertPos = 0;
                    for (int col = 0; col < w; col++) {
                        if (newBoard[row][col] != '*') {
                            newBoard[row][insertPos++] = newBoard[row][col];
                            if (insertPos - 1 != col) {
                                newBoard[row][col] = '*';
                            }
                        }
                    }
                }
                break;
            case 'r':
                for (int row = 0; row < h; row++) {
                    int insertPos = w - 1;
                    for (int col = w - 1; col >= 0; col--) {
                        if (newBoard[row][col] != '*') {
                            newBoard[row][insertPos--] = newBoard[row][col];
                            if (insertPos + 1 != col) {
                                newBoard[row][col] = '*';
                            }
                        }
                    }
                }
                break;
        }
    
        return newBoard;
    }
    
    /**
     * Convert the direction to a readable string.
     */
    private String directionToString(char direction) {
        switch (direction) {
            case 'u':
                return "up";
            case 'd':
                return "down";
            case 'l':
                return "left";
            case 'r':
                return "right";
            default:
                return "";
        }
    }
    
    /**
     * Creates a visual representation of a hole at the specified tile position.
     *
     * @param tile The tile to create the hole in
     */
    private void createHoleCircle(Tile tile) {
        int xPos = tile.getXPos();
        int yPos = tile.getYPos();
        int diameter = Tile.SIZE;

        // Calcultate the center position of the circle
        int circleX = xPos;
        int circleY = yPos;

        // Create and make visible the circle (hole)
        Circle hole = new Circle(diameter, circleX, circleY, Color.WHITE);
        hole.makeVisible();
        holeCircles.add(hole);
    }

  // <----------------------------------- IMPLEMENTING FIXED_TILES METHOD ----------------------------------->
    /**
     * Identifies and returns a matrix indicating the fixed tiles that cannot move.
     * 
     * @return A matrix of fixed tiles, where 0 indicates a fixed tile and 1 indicates a movable tile.
     */
    public int[][] fixedTiles() {
        // Validates each row to check if there is an empty tile or a hole. 
        // If an empty tile or a hole is found, mark the entire row as not fixed.
        for (int i = 0; i < h; i++) {
            boolean flag = findEmptyTileOrHoleSegmentRow(i);
            if (flag) {
                for (int j = 0; j < w; j++) {
                    Tile tile = getTileAtPosition(i, j);
                    if (tile.getFixedStatus()) {
                        tile.setIsNotFixed();
                    }
                }
            }
        }
    
        // Validates each column to check if there is an empty tile or a hole. 
        // If an empty tile or a hole is found, mark the entire column as not fixed.
        for (int j = 0; j < w; j++) {
            boolean flag = findEmptyTileOrHoleSegmentColumn(j);
            if (flag) {
                for (int i = 0; i < h; i++) {
                    Tile tile = getTileAtPosition(i, j);
                    if (tile.getFixedStatus()) {
                        tile.setIsNotFixed();
                    }
                }
            }
        }
    
        // Create a matrix representing the fixed tiles, with 1's and 0's.
        // 0 represents a fixed tile, while 1 represents a movable tile.
        int[][] fixedTilesMatrix = new int[h][w];
        for (int i = 0; i < h; i++) {
            for (int j = 0; j < w; j++) {
                Tile targetTile = getTileAtPosition(i, j);
                if (targetTile.getFixedStatus()) {
                    fixedTilesMatrix[i][j] = 0;
                    if (visible) {                        
                            if (visible) {                        
                                targetTile.blink();
                            }
                        }
                } else {
                    fixedTilesMatrix[i][j] = 1;
                }
            }
        }
    
        // Print the fixed tiles matrix to the console
        System.out.println("Fixed Tiles Matrix:");
        for (int row = 0; row < h; row++) {
            for (int col = 0; col < w; col++) {
                System.out.print(fixedTilesMatrix[row][col] + " ");
            }
            System.out.println();
        }
        System.out.println();
    
        return fixedTilesMatrix;
    }
    
    /**
     * Finds an empty tile or a hole in the specified row.
     * 
     * @param row The row index to check.
     * @return true if an empty tile or a hole is found in the row, false otherwise.
     */
    private boolean findEmptyTileOrHoleSegmentRow(int row) {
        for (int col = 0; col < w; col++) {
            Tile currentTile = getTileAtPosition(row, col);
            if (currentTile.getLabel() == 'h' || currentTile.getLabel() == '*') {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Finds an empty tile or a hole in the specified column.
     * 
     * @param col The column index to check.
     * @return true if an empty tile or a hole is found in the column, false otherwise.
     */
    private boolean findEmptyTileOrHoleSegmentColumn(int col) {
        for (int row = 0; row < h; row++) {
            Tile currentTile = getTileAtPosition(row, col);
            if (currentTile.getLabel() == 'h' || currentTile.getLabel() == '*') {
                return true;
            }
        }
        return false;
    }
    
    
    // I used the same logic that method isGoal about comparing and to get the position on the tile with the label.
    /**
     * Counts the number of misplaced tiles compared to the reference board.
     *
     * @return The number of misplaced tiles
     */
    public int misplacedTiles(){
        
        int cont = 0;
        
        for (int row = 0; row < h;row++){
            for (int col = 0; col < w;col++){
                Tile currentTile = tiles.get(row).get(col);
                char currentLabel = currentTile.getLabel();
    
                Tile referingTile = referingTiles.get(row).get(col);
                char referenceLabel = referingTile.getLabel();
                
                if (currentLabel != referenceLabel && currentLabel != '*'){
                    cont++;
                }
            }
        }
        
        return cont;
    }

    
    public static void main(String[] args) {
        
        
         //SECOND TEST
        char[][] starting1 = {
        {'y', 'g', 'y', 'b', 'r', 'g', 'b', 'y', 'r', 'b'},
        {'b', 'r', 'g', 'b', 'y', 'r', 'g', 'b', 'y', 'g'},
        {'g', 'b', '*', 'y', 'b', 'g', 'r', 'y', 'b', 'r'},
        {'r', '*', 'g', 'b', 'r', '*', '*', 'b', 'r', 'g'},
        {'b', 'g', 'r', 'y', 'b', 'g', 'r', 'y', 'b', 'r'},
        {'y', '*', 'r', '*', 'y', 'b', 'r', 'g', 'y', 'b'},
        {'*', 'r', 'y', 'b', 'g', '*', '*', 'b', 'g', 'r'},
        {'*', 'g', 'b', 'y', 'r', 'g', 'b', 'y', 'r', 'b'},
        {'*', 'b', 'g', 'r', 'y', '*', 'g', 'r', 'y', 'g'},
        {'*', 'r', 'y', 'b', 'g', 'r', 'y', 'b', 'g', 'r'}
    };
        
        char[][] ending1 = {
        {'y', 'r', 'g', 'r', 'y', 'b', 'g', 'r', 'y', 'b'},
        {'g', 'b', 'g', 'b', 'r', 'g', 'b', 'y', 'r', 'g'},
        {'b', 'g', 'y', 'r', 'y', 'b', 'g', 'r', 'y', 'b'},
        {'r', 'g', 'b', 'y', 'r', 'g', 'b', 'y', 'r', 'g'},
        {'y', 'b', 'g', 'r', 'y', 'b', 'g', '*', 'y', 'b'},
        {'g', 'r', 'y', 'b', 'g', 'r', 'y', 'b', 'g', 'r'},
        {'r', 'g', 'b', 'y', 'r', 'g', 'b', 'y', 'r', 'b'},
        {'y', 'r', 'g', 'b', 'y', 'r', 'g', 'b', 'y', 'r'},
        {'g', 'b', 'y', 'r', 'g', 'b', 'y', '*', 'g', 'b'},
        {'r', 'g', 'b', 'y', 'r', 'g', 'b', 'y', 'r', 'g'}
    };
        
        Puzzle pz3 = new Puzzle(10, 10); // Tablero sin matrices
        Puzzle pz4 = new Puzzle(starting1, ending1); // Tablero con matrices
        
        pz4.addTile(9,0,'r');
        pz4.addGlue(9,1);
        pz4.tilt('u');
        pz4.tilt('r');
        
        //pz4.addTile(5,1,'b');
        //pz4.deleteTile(5,1);
        //pz4.deleteTile(9,8);
        
        int[] from1 = {9,9};
        int[] to1   = {3,1};
        //pz4.relocateTile(from1,to1);
        
        int[] from3 = {3,1};
        int[] to3   = {9,9};
        //pz4.relocateTile(from3,to3);
        
        int[] from2 = {1,9};
        int[] to2   = {3,2};
        //pz4.relocateTile(from2,to2);
    
        //pz4.tilt('l');
        //pz4.tilt('g');
        pz4.addTile(6,0,'r');
        
        int[] from4 = {6,0};
        int[] to4   = {3,1};
        pz4.relocateTile(from4,to4);
    }
}

