import java.awt.Color;

public class Tile extends Rectangle {
    private Color color;
    private char label;
    private int size;
    private int padding; // Padding interno
    Color lightBrown = new Color(207, 126, 60);
    private int xPosition;
    private int yPosition;
    
    private boolean hasGlue; // Si tiene pegamento
    private boolean isStuck; // Si está pegada a otras fichas

    public Tile(int size, char label, int xPosition, int yPosition, int padding) {
        this.size = size;
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        this.padding = padding; // Inicializa el padding
        this.label = label;
        int effectiveSize = size - 2 * padding; // Tamaño efectivo después de aplicar padding
        // Cambia el tamaño del rectángulo para reflejar el padding
        this.changeSize(size, size);            
        // Cambia el color y mueve el rectángulo visible
        this.setTileColor(this.label);
        this.moveHorizontal(xPosition);
        this.moveVertical(yPosition);
        this.hasGlue = false; // Inicialmente no tiene pegamento
        this.isStuck = false; // Inicialmente no está pegada a otras tiles
        if (!this.getTileColor().equals(lightBrown)){
            this.makeVisible();
        } else {
            this.makeInvisible();
        }
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
            case '*':
                color = lightBrown;
                break;                
            default:
                color = lightBrown;
                
        }        
        this.changeColor(color);
    }
    
    public Color getTileColor(){
        return color;
    }
    
    public char getLabel(){
        return label;
    }
    
    public int getXposition(){
        return xPosition;
    }
    
    public int getYposition(){
        return yPosition;
    }
    
        // Nuevos getters y setters para hasGlue e isStuck
    public boolean hasGlue() {
        return hasGlue;
    }

    public void setHasGlue(boolean hasGlue) {
        this.hasGlue = hasGlue;
    }

    public boolean isStuck() {
        return isStuck;
    }

    public void setStuck(boolean stuck) {
        this.isStuck = stuck;
    }
    
    
}
