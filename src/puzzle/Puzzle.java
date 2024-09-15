import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

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
    private List<List<Tile>> tiles; // Lista de listas
    private List<List<Tile>> referingTiles; // Tiles de referencia

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
        endingBoard.moveHorizontal(rows * (tileSize + margin) + 350);
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
        this.referingTiles = new ArrayList<>();

        // Crear las piezas del puzzle inicial    
        for (int row = 0; row < starting.length; row++) {
            List<Tile> rowList = new ArrayList<>();
            for (int col = 0; col < starting[row].length; col++) {
                char label = starting[row][col];
                int xPosition = 105 + (col * (tileSize + margin));  // Ajustar la posición horizontal
                int yPosition = 55 + (row * (tileSize + margin));   // Ajustar la posición vertical

                // Crear la pieza y agregarla a la lista
                Tile tile = new Tile(tileSize, label, xPosition, yPosition, padding);
                //tiles.get(row).set(col, tile); // Agregar el tile a la sublista
                rowList.add(tile); // Inicializa la fila con null                
            }
            tiles.add(rowList);
        }

        // Crear las piezas del puzzle final
        for (int row = 0; row < ending.length; row++) {
            List<Tile> rowList = new ArrayList<>();
            for (int col = 0; col < ending[row].length; col++) {
                char label = ending[row][col];
                int xPosition = (rows * (tileSize + margin)) + 355 + (col * (tileSize + margin)); // Ajustar la posición horizontal
                int yPosition = 55 + (row * (tileSize + margin));   // Ajustar la posición vertical

                // Crear la pieza y agregarla a la lista
                Tile tile = new Tile(tileSize, label, xPosition, yPosition, padding);
                rowList.add(tile);
            }
            referingTiles.add(rowList);            
        }
    }
    
    public void addTile(int row, int column, char label){                
        if (row >= rows || column >= cols){
            JOptionPane.showMessageDialog(null,"You have exceeded the puzzle space.", "Error", JOptionPane.ERROR_MESSAGE); 
        } else {
            Tile previousTile = tiles.get(row).get(column);

            // Usa equals para comparar colores
            if(previousTile.getTileColor().equals(lightBrown)) {
                //previousTile.getLabel();
                previousTile.setTileColor(label);
            } else {
                JOptionPane.showMessageDialog(null, "There is a tile here now.", "Error", JOptionPane.ERROR_MESSAGE);
            }                                                    
        }
    }

    public void deleteTile(int row, int column){
        if (row >= rows || column >= cols){
            JOptionPane.showMessageDialog(null,"You have exceeded the puzzle space.", "Error", JOptionPane.ERROR_MESSAGE); 
        } else {
            Tile previousTile = tiles.get(row).get(column);
    
            // Usa equals para comparar colores
            if(!previousTile.getTileColor().equals(lightBrown)) {
                previousTile.setTileColor('*');        
            } else {
                JOptionPane.showMessageDialog(null, "There is no a tile here now.", "Error", JOptionPane.ERROR_MESSAGE);
            }        
        }
    }

    public void relocateTile(int[] from, int[] to) {
        // Validar las coordenadas de entrada
        if (!areValidCoordinates(from) || !areValidCoordinates(to)) {
            JOptionPane.showMessageDialog(null, "Invalid coordinates.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
    
        Tile fromTile = tiles.get(from[0]).get(from[1]);
        Tile toTile = tiles.get(to[0]).get(to[1]);
    
        // Validar existencia de la tile de origen y disponibilidad de la tile de destino
        if (isTileEmpty(fromTile)) {
            JOptionPane.showMessageDialog(null, "You cannot move a non-existent tile.", "Error", JOptionPane.ERROR_MESSAGE);
        } else if (!isTileEmpty(toTile)) {
            JOptionPane.showMessageDialog(null, "There is already a tile at the destination.", "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            // Mover la instancia de la tile visualmente
            fromTile.slowMoveHorizontal(to[1] * (tileSize + margin) - from[1] * (tileSize + margin));
            fromTile.slowMoveVertical(to[0] * (tileSize + margin) - from[0] * (tileSize + margin));            
            // Mover la instancia de la tile visualmente desde "to" a "from"
            toTile.moveHorizontal(from[1] * (tileSize + margin) - to[1] * (tileSize + margin));
            toTile.moveVertical(from[0] * (tileSize + margin) - to[0] * (tileSize + margin));
            // Actualizar la lista de tiles: intercambiar las posiciones de las fichas
            tiles.get(to[0]).set(to[1], fromTile);  // Mover la baldosa a la nueva posición
            tiles.get(from[0]).set(from[1], toTile);  // La ficha original ahora es la nueva ficha vacía
    
            // Cambiar el color de la ficha que ahora está vacía
            toTile.setTileColor('*');
        }
    }

    
    // Método para validar si las coordenadas son correctas
    private boolean areValidCoordinates(int[] coords) {
        return coords.length == 2 && coords[0] < rows && coords[1] < cols;
    }
    
    // Método para verificar si una Tile está vacía (es de color lightBrown)
    private boolean isTileEmpty(Tile tile) {
        return tile.getTileColor().equals(lightBrown);
    }    

    
    public void tilt(char direction) {
    switch (direction) {
        case 'd': // Tilt down
            for (int col = 0; col < cols; col++) {
                tiltDown(0, col);
            }
            break;
        case 'u': // Tilt up
            for (int col = 0; col < cols; col++) {
                tiltUp(rows - 1, col);
            }
            break;
        case 'r': // Tilt right
            for (int row = 0; row < rows; row++) {
                tiltRight(row, 0);
            }
            break;
        case 'l': // Tilt left
            for (int row = 0; row < rows; row++) {
                tiltLeft(row, cols - 1);
            }
            break;
        default:
            JOptionPane.showMessageDialog(null, "Invalid direction.", "Error", JOptionPane.ERROR_MESSAGE);
    }
}

    // Tilt down starting from the top row recursively
    private void tiltDown(int row, int col) {
        if (row >= rows - 1) return; // Caso base: hemos llegado al fondo
    
        int[] currentPos = {row, col};
        int[] nextPos = {row + 1, col};
    
        if (isTileEmpty(tiles.get(nextPos[0]).get(nextPos[1]))) {
            relocateTile(currentPos, nextPos); // Mueve la tile hacia abajo
            tiltDown(row + 1, col); // Llama recursivamente
        }
    }
    
    // Tilt up starting from the bottom row recursively
    private void tiltUp(int row, int col) {
        if (row <= 0) return; // Caso base: hemos llegado al tope
    
        int[] currentPos = {row, col};
        int[] nextPos = {row - 1, col};
    
        if (isTileEmpty(tiles.get(nextPos[0]).get(nextPos[1]))) {
            relocateTile(currentPos, nextPos); // Mueve la tile hacia arriba
            tiltUp(row - 1, col); // Llama recursivamente
        }
    }
    
    // Tilt right starting from the left column recursively
    private void tiltRight(int row, int col) {
        if (col >= cols - 1) return; // Caso base: hemos llegado al extremo derecho
    
        int[] currentPos = {row, col};
        int[] nextPos = {row, col + 1};
    
        if (isTileEmpty(tiles.get(nextPos[0]).get(nextPos[1]))) {
            relocateTile(currentPos, nextPos); // Mueve la tile hacia la derecha
            tiltRight(row, col + 1); // Llama recursivamente
        }
    }
    
    // Tilt left starting from the right column recursively
    private void tiltLeft(int row, int col) {
        if (col <= 0) return; // Caso base: hemos llegado al extremo izquierdo
    
        int[] currentPos = {row, col};
        int[] nextPos = {row, col - 1};
    
        if (isTileEmpty(tiles.get(nextPos[0]).get(nextPos[1]))) {
            relocateTile(currentPos, nextPos); // Mueve la tile hacia la izquierda
            tiltLeft(row, col - 1); // Llama recursivamente
        }
    }

    
    
    public static void main(String[] args) {
        // Crear matrices de caracteres de ejemplo con 8 filas y 4 columnas
        char[][] starting = {
                {'y', '*'},
                {'b', '*'}
            };

        char[][] ending = {
                {'y', '*'},
                {'*', 'b'}
            };                            
        // Instanciar los objetos de Puzzle
        Puzzle pz1 = new Puzzle(2, 2); // Tablero sin matrices
        Puzzle pz2 = new Puzzle(starting, ending); // Tablero con matrices

        // addTile manual tests
        //pz2.addTile(0,1,'r'); // should addTile - ok
        //pz2.addTile(10,10,Color.BLUE); // should not addTile - ok
        //pz2.addTile(0,0,Color.BLACK); // should not addTile (It exist a tile in this index) -ok

        // deleteTile manual tests
        //pz2.deleteTile(10,10); // should not delete (out of index) - ok
        //pz2.deleteTile(0,0); // should not delete (THere is not a tile in this index)) - ok
        //pz2.deleteTile(1,0); // should delete a tile - ok
        
        //pz2.addTile(0,0,'b');
        // relocateTile manual tests
        int[] from = {0,1};
        int[] to   = {1,1};        
        //pz2.relocateTile(from,to); // should pass     
        int[] from1 = {10,0};
        int[] to1   = {0,1};
        //pz2.relocateTile(from1,to1); // should not pass wrong from row
        int[] from2 = {0,10};
        int[] to2   = {0,1};
        //pz2.relocateTile(from2,to2); // should not pass wrong from column
        int[] from3 = {0,0};
        int[] to3   = {10,1};
        //pz2.relocateTile(from3,to3); // should not pass wrong to row
        int[] from4 = {0,0};
        int[] to4   = {0,10};
        //pz2.relocateTile(from2,to2); // should not pass wrong from column   
        // relocateTile manual tests
        int[] from5 = {1,1};
        int[] to5   = {0,1};        
        //pz2.relocateTile(from5,to5); // should not pass, there is a tile there
        //pz2.relocateTile(from5,to5); // shouldn't pass, relocate an origin tile with a new tile
        //pz2.relocateTile(to5,from5);
        
        //pz2.deleteTile(0,1);
        int[] from6 = {0,1};
        int[] to6   = {0,0};
        //pz2.relocateTile(from2,to2); // should not pass wrong from column   
        
        pz2.tilt('r');
        pz2.tilt('l');
    }
}
