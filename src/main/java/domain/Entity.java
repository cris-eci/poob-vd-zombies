package domain;

public abstract class Entity {
    private int row;
    private int col;
    private String name;

    public void setPosition(int row, int col, String name) {
        this.row = row;
        this.col = col;
        this.name = name;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public String getName() {
        return name;
    }

    // Resto de la clase...
}

