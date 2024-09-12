import java.awt.Color;

public class Tile extends Rectangle {
    private Color color;
    private char label;
    private int size;
    private int padding; // Padding interno

    public Tile(int size, char label, int xPosition, int yPosition, int padding) {
        this.size = size;
        this.padding = padding; // Inicializa el padding
        this.label = label;
        int effectiveSize = size - 2 * padding; // Tamaño efectivo después de aplicar padding

        // Cambia el tamaño del rectángulo para reflejar el padding
        this.changeSize(size, size);
        Color lightBrown = new Color(207, 126, 60);

        switch (label) {
            case 'r':
                this.color = Color.RED;
                break;
            case 'b':
                this.color = Color.BLUE;
                break;
            case 'y':
                this.color = Color.YELLOW;
                break;
            case 'g':
                this.color = Color.GREEN;
                break;
            default:
                this.color = lightBrown;
        }

        // Cambia el color y mueve el rectángulo visible
        this.changeColor(this.color);
        this.moveHorizontal(xPosition);
        this.moveVertical(yPosition);
        this.makeVisible();
    }
    
    public Color getColor(){
        return color;
    }
}
