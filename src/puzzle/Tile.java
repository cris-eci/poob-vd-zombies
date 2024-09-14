import java.awt.Color;

public class Tile extends Rectangle {
    private Color color;
    private char label;
    private int size;
    private int padding; // Padding interno
    Color lightBrown = new Color(207, 126, 60);

    public Tile(int size, char label, int xPosition, int yPosition, int padding) {
        this.size = size;
        this.padding = padding; // Inicializa el padding
        this.label = label;
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
        return color;
    }
    
    public char getLabel(){
        return label;
    }
    
}
