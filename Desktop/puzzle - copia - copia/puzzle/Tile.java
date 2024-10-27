package puzzle;
import shapes.*;

import java.awt.Color;

/**
 * The Tile class represents a tile in a puzzle. It extends Rectangle to inherit graphical properties.
 * Each tile has a color, a label, coordinates for its position in the grid, and other attributes for managing its state. 
 *
 * @author: Andersson David Sánchez Méndez
 * @author: Cristian Santiago Pedraza Rodríguez
 * @version 2024
 */

public class Tile extends Rectangle{
    // Constants
    public static final int SIZE = 50;
    public static final int MARGIN = 10;
    public static final int PADDING = 10; // Intern Padding 
    public static Color FIXED_TILE_COLOR = new Color(139, 0, 0);

    // Attributes
    private Color color;
    private char label;    
    private int row; // Tile row
    private int column; // Tile Column
    private boolean hasGlue = false;
    private boolean isStuck = false;
    private boolean visited = false; // For tracking during tilt
    private Color originalColor;
    private int xPos;
    private int yPos;
    private boolean isHole = false;
    private boolean isFixed = true; // It is true because if after validate col and row it still is true, it's because it is really fixed. 
     
    /**
     * Constructor to create a new Tile object with specified label, position, row, and column.
     *
     * @param label The label character for the tile.
     * @param xPosition The x-coordinate of the tile.
     * @param yPosition The y-coordinate of the tile.
     * @param row The row index of the tile in the grid.
     * @param column The column index of the tile in the grid.
     */
    public Tile(char label, int xPosition, int yPosition,int row, int column) {
        this.label = label;
        this.row = row;
        this.column = column;
        xPos = xPosition;
        yPos = yPosition;                      
        this.changeSize(SIZE, SIZE);                
        this.setTileColor(label);
        this.moveHorizontal(xPosition);
        this.moveVertical(yPosition);
        this.makeVisible();
    }
    
     /**
     * Sets the color of the tile based on its label.
     *
     * @param label The label character to determine the tile color.
     */
    public void setTileColor(char label){
        Color lightBrown = Puzzle.lightBrown; 
        this.label = label;

        switch (label) {
            case 'r':
                color = Color.RED;
                break;
            case 'b':
                color = Color.BLUE;
                break;
            case 'y':
                color = Color.YELLOW;
                break;
            case 'g':
                color = Color.GREEN;
                break;
            case 'h':
                color = Color.WHITE;
                break;
            case 'n':
                color = lightBrown;
                break;
            default:
                color = lightBrown;
        }
        this.originalColor = color;
        this.changeColor(this.color);
    }
    
    /**
     * Gets the current color of the tile.
     *
     * @return The color of the tile.
     */
    public Color getTileColor(){
        return this.color;
    }
    
    /**
     * Sets the tile color to a specific value, in this case, for pale color when a tile hasGlue or isStuck.
     *
     * @param newColor The new paler color for the tile.
     */
    public void setTileColor(Color newColor) {
        this.color = newColor;
        this.changeColor(newColor);
    }

    // Methods to manage glue and stuck states

     /**
     * Checks if the tile has glue.
     *
     * @return True if the tile has glue, false otherwise.
     */    
    public boolean hasGlue() {
        return hasGlue;
    }

    /**
     * Sets whether the tile has glue or not.
     *
     * @param hasGlue True if the tile should have glue, false otherwise.
     */
    public void setHasGlue(boolean hasGlue) {
        this.hasGlue = hasGlue;
    }
    public boolean isStuck() {
        return isStuck;
    }

    /**
     * Sets whether the tile is stuck or not.
     *
     * @param isStuck True if the tile should be stuck, false otherwise.
     */
    public void setIsStuck(boolean isStuck) {
        this.isStuck = isStuck;
    }

    /**
     * Gets the original color of the tile.
     *
     * @return The original color of the tile.
     */
    public Color getOriginalColor() {
        return originalColor;
    }

    /**
     * Gets the row index of the tile.
     *
     * @return The row index of the tile.
     */
    public int getRow() {
        return row;
    }

    /**
     * Sets the row index of the tile.
     *
     * @param newRow The new row index for the tile.
     */
    public void setRow(int newRow) {
        this.row = newRow;
    }

    /**
     * Gets the column index of the tile.
     *
     * @return The column index of the tile.
     */
    public int getCol() {
        return column;
    }

    /**
     * Sets the column index of the tile.
     *
     * @param newCol The new column index for the tile.
     */
    public void setCol(int newCol) {
        this.column = newCol;
    }

    /**
     * Gets the label of the tile.
     *
     * @return The label character of the tile.
     */
    public char getLabel() {
        return label;
    }

    /**
     * Sets the label of the tile.
     *
     * @param newLabel The new label character for the tile.
     */
    public void setLabel(char newLabel) {
        this.label = newLabel;
    }

    /**
     * Checks if the tile has been visited.
     *
     * @return True if the tile has been visited, false otherwise.
     */
    public boolean isVisited() {
        return visited;
    }

    /**
     * Sets whether the tile has been visited or not.
     *
     * @param visited True if the tile has been visited, false otherwise.
     */
    public void setVisited(boolean visited) {
        this.visited = visited;
    }

    /**
     * Gets the x-coordinate position of the tile.
     *
     * @return The x-coordinate position.
     */
    public int getXPos() {
        return xPos;
    }

    /**
     * Sets the x-coordinate position of the tile.
     *
     * @param xPos The new x-coordinate position.
     */
    public void setXPos(int xPos) {
        this.xPos = xPos;
    }

    /**
     * Gets the y-coordinate position of the tile.
     *
     * @return The y-coordinate position.
     */
    public int getYPos() {
        return yPos;
    }

    /**
     * Sets the y-coordinate position of the tile.
     *
     * @param yPos The new y-coordinate position.
     */
    public void setYPos(int yPos) {
        this.yPos = yPos;
    }

    /**
     * Checks if the tile is a hole.
     *
     * @return True if the tile is a hole, false otherwise.
     */
    public boolean getIsHole() {
        return isHole;
    }

    /**
     * Sets whether the tile is a hole.
     *
     * @param isHole True if the tile is a hole, false otherwise.
     */
    public void setIsHole(boolean isHole) {
        this.isHole = isHole;
    }

    /**
     * Gets the size of the tile.
     *
     * @return The size of the tile.
     */
    public int getSize() {
        return SIZE;
    }

    /**
     * Makes the tile blink to indicate that it is a fixed tile.
     */
    public void blink() {
        Color originalColor = this.color;
        for (int i = 0; i < 4; i++) {
            changeColorMultipleTimes(Puzzle.lightBrown, 5);
            changeColorMultipleTimes(originalColor, 5);
        }
    }

    /**
     * Changes the tile color multiple times to create a blinking effect.
     *
     * @param color The color to change to.
     * @param times The number of times to change the color.
     */
    private void changeColorMultipleTimes(Color color, int times) {
        for (int i = 0; i < times; i++) {
            this.changeColor(color);
            this.changeColor(color);
        }
    }
    
    /**
     * Sets a tile as not fixed
     *     
     */    
    public void setIsNotFixed(){
        isFixed = false;
    }
    
    
    /**
     * Gets the fixed status of the tile.
     * 
     * @return true if the tile is fixed (cannot move), false otherwise.
     */
    public boolean getFixedStatus(){
        return isFixed;
    }
}
