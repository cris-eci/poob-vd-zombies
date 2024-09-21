import java.awt.Color;

public class Tile extends Rectangle {
    private Color color;
    private char label;
    private int size;
    private int padding; // Padding interno
    private int row; // Fila de la baldosa
    private int col; // Columna de la baldosa
    private boolean glueApplied;   // Indica si el pegante está aplicado
    
    
    
    public Tile(int size, char label, int xPosition, int yPosition, int padding, int row, int col) {
        this.size = size;
        this.padding = padding; // Inicializa el padding
        this.label = label;
        this.row = row;
        this.col = col;
        this.glueApplied = false;  // Inicializa sin pegante
        int effectiveSize = size - 2 * padding; // Tamaño efectivo después de aplicar padding

        // Cambia el tamaño del rectángulo para reflejar el padding
        this.changeSize(size, size);
        
        // Cambia el color y mueve el rectángulo visible
        this.setTileColor(label);
        this.moveHorizontal(xPosition);
        this.moveVertical(yPosition);
        this.makeVisible();
    }
    
    public void setTileColor(char label){
        Color lightBrown = new Color(207, 126, 60);
        
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
            case 'n':
                color = lightBrown;
                break;                
            default:
                color = lightBrown;
                
        }
        
        this.changeColor(this.color);
    }
    
    public Color getTileColor(){
        return this.color;
    }
    
    // Métodos getter para fila y columna
    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public char getLabel() {
        return label;
    }
    
    
    // Método para aplicar pegante
    public void applyGlue() {
        if (!this.glueApplied) {
            this.glueApplied = true;
            this.changeColor(Color.GRAY);  // Cambia el color a gris para mostrar que tiene pegante
        } else {
            System.out.println("La baldosa ya tiene pegamento.");
        }
    }

    // Método para remover pegante, solo si está aplicado
    public void removeGlue() {
        if (this.glueApplied) {
            this.glueApplied = false;   // Quita el estado de pegante
            this.changeColor(this.color);  // Restaura el color original de la baldosa
        } else {
            System.out.println("No hay pegamento aplicado.");
        }
    }

    // Método para verificar si el pegante está aplicado
    public boolean isGlueApplied() {
        return glueApplied;
    }
}
