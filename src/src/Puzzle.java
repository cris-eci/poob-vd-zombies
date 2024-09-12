import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public class Puzzle {
    private int tileSize;
    private int rows;
    private int cols;
    private int margin; // Margen entre cada baldosas
    private int padding; // Padding interno
    private Rectangle startingBoard;
    private Rectangle endingBoard;
    private Color color;
    private char[][] starting;
    private char[][] ending;
    private List<Tile> tiles;

    // Board color
    Color lightBrown = new Color(207, 126, 60);

    // Constructor para inicializar los tableros sin las matrices
    public Puzzle(int rows, int cols) {
        this.tileSize = 50;  // Tamaño de cada tile
        this.margin = 10;    // Margen entre tiles
        this.padding = 5;    // Padding interno
        this.rows = rows;
        this.cols = cols;
        this.color = lightBrown;

        startingBoard = new Rectangle();
        startingBoard.changeSize(rows * (tileSize + margin), cols * (tileSize + margin));
        startingBoard.changeColor(color);
        startingBoard.makeVisible();
        startingBoard.moveHorizontal(100);
        startingBoard.moveVertical(50);

        endingBoard = new Rectangle();
        endingBoard.changeSize(rows * (tileSize + margin), cols * (tileSize + margin));
        endingBoard.changeColor(color);
        endingBoard.makeVisible();
        endingBoard.moveHorizontal(rows * (tileSize + margin) + 150);
        endingBoard.moveVertical(50);
    }

    // Constructor para inicializar los tableros con matrices
    public Puzzle(char[][] starting, char[][] ending) {
        this.tileSize = 50;  // Tamaño de cada tile
        this.margin = 10;    // Margen entre tiles
        this.padding = 10;    // Padding interno
        this.rows = starting.length;
        this.cols = starting[0].length;
        this.starting = starting;
        this.ending = ending;
        this.tiles = new ArrayList<>();

        // Crear las piezas del puzzle inicial
        for (int row = 0; row < starting.length; row++) {
            for (int col = 0; col < starting[row].length; col++) {
                char label = starting[row][col];
                // xPosition + 5 to left maring 150 -->155
                int xPosition = 105 + (col * (tileSize + margin));  // Ajustar la posición horizontal
                // yPosition + 5 to upper maring 50 --> 55
                int yPosition = 55 + (row * (tileSize + margin));   // Ajustar la posición vertical

                // Crear la pieza y agregarla a la lista
                Tile tile = new Tile(tileSize, label, xPosition, yPosition, padding);
                tiles.add(tile);
            }
        }

        // Crear las piezas del puzzle final
        for (int row = 0; row < ending.length; row++) {
            for (int col = 0; col < ending[row].length; col++) {
                char label = ending[row][col];
                // xPosition + 5 to left maring 150 -->155
                int xPosition = (rows * (tileSize + margin)) + 155 + (col * (tileSize + margin)); // Ajustar la posición horizontal
                // yPosition + 5 to upper maring 50 --> 55
                int yPosition = 55 + (row * (tileSize + margin));   // Ajustar la posición vertical

                // Crear la pieza y agregarla a la lista
                Tile tile = new Tile(tileSize, label, xPosition, yPosition, padding);
                tiles.add(tile);
            }
        }
    }

    public static void main(String[] args) {
        // Crear matrices de caracteres de ejemplo con 8 filas y 4 columnas
        char[][] starting = {
            {'y', 'r'},
            {'b', 'b'}
        };

        char[][] ending = {
            {'r', 'r'},
            {'y', 'b'}
        };

        // Instanciar los objetos de Puzzle
        Puzzle pz1 = new Puzzle(2, 2); // Tablero sin matrices
        Puzzle pz2 = new Puzzle(starting, ending); // Tablero con matrices
    }
}
