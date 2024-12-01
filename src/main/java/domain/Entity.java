package domain;

/**
 * Clase abstracta que representa cualquier entidad en el jardín.
 * Contiene atributos de posición.
 */
public abstract class Entity {
    protected int line; // Fila en el jardín (0-4)
    protected int xPos; // Columna en el jardín (0-9)

    public Entity(int line, int xPos) {
        this.line = line;
        this.xPos = xPos;
    }

    public int getLine() {
        return line;
    }

    public int getXPos() {
        return xPos;
    }

    public void setLine(int line) {
        this.line = line;
    }

    public void setXPos(int xPos) {
        this.xPos = xPos;
    }
}
