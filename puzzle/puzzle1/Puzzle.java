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
    private boolean visible = true; //Determina si el simulador está visible
    private boolean ok = true; //Rastrea si la última acción fue exitosa
    
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
        
        /**
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
        **/
        
        startingBoard = new Rectangle(rows * (tileSize + margin), cols * (tileSize + margin), color,100,50);
        endingBoard = new Rectangle(rows * (tileSize + margin), cols * (tileSize + margin),color, rows * (tileSize + margin) + 350, 50);
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
                Tile tile = new Tile(tileSize, label, xPosition, yPosition, padding,row, col);
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
                Tile tile = new Tile(tileSize, label, xPosition, yPosition, padding, row, col);
                rowList.add(tile);
            }
            referingTiles.add(rowList);            
        }
    }
    
   
    public void addTile(int row, int column, char label){                
        if (row >= rows || column >= cols){
            showMessage("You have exceeded the puzzle space.", "Error"); 
            this.ok = false; //Error message
        }
        else{
            Tile previousTile = tiles.get(row).get(column);

            // Usa equals para comparar colores
            if(previousTile.getTileColor().equals(lightBrown)) {
                //previousTile.getLabel();
                previousTile.setTileColor(label);
                //System.out.println("Color cambiado a: " + previousTile.getTileColor());
                //System.out.println("neo");
                this.ok = true; //Acciòn exitosa
            } else {
                showMessage("There is a tile here now.", "Error");
                this.ok = false; //Error message
            }                                               
        }
             
    }

    public void deleteTile(int row, int column){
        if (row >= rows || column >= cols){
            showMessage("You have exceeded the puzzle space.", "Error");
            this.ok = false; //Error message
        }
        else{
            Tile previousTile = tiles.get(row).get(column);

            // Usa equals para comparar colores
            if(!previousTile.getTileColor().equals(lightBrown)) {
                previousTile.setTileColor('n');  
                this.ok = true; //Acciòn exitosa
            } else {
                showMessage("There is a tile here now.", "Error");
                this.ok = false; //Error message
            }
        }
        
    }
    
    public void relocateTileMovement(Tile fromTile, Tile toTile, int[] from, int[] to){
            // Mover la instancia de la tile visualmente
            fromTile.moveHorizontal(to[1] * (tileSize + margin) - from[1] * (tileSize + margin));
            fromTile.moveVertical(to[0] * (tileSize + margin) - from[0] * (tileSize + margin));            
            // Mover la instancia de la tile visualmente desde "to" a "from"
            toTile.moveHorizontal(from[1] * (tileSize + margin) - to[1] * (tileSize + margin));
            toTile.moveVertical(from[0] * (tileSize + margin) - to[0] * (tileSize + margin));
            // Actualizar la lista de tiles: intercambiar las posiciones de las fichas
            tiles.get(to[0]).set(to[1], fromTile);  // Mover la baldosa a la nueva posición
            tiles.get(from[0]).set(from[1], toTile);  // La ficha original ahora es la nueva ficha vacía    
            // Cambiar el color de la ficha que ahora está vacía
            toTile.setTileColor('*'); 
    
    }
    public void relocateTile(int[] from, int[] to) {
        // Validar las coordenadas de entrada
        if (!areValidCoordinates(from) || !areValidCoordinates(to)) {
            showMessage("Invalid coordinates.", "Error");
            this.ok = false; //Error message
            return;
        }
    
        Tile fromTile = tiles.get(from[0]).get(from[1]);
        Tile toTile = tiles.get(to[0]).get(to[1]);        
        // Validar existencia de la tile de origen y disponibilidad de la tile de destino
        if (isTileEmpty(fromTile)) {
            showMessage("You cannot move a non-existent tile.", "Error");
            this.ok = false; //Error message
        } else if (!isTileEmpty(toTile)) {
            showMessage("There is already a tile at the destination.", "Error");
            this.ok = false; //Error message
        } else {
            /**
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
            **/
            this.relocateTileMovement(fromTile, toTile, from, to);
            this.ok = true; //Acciòn exitosa
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
            showMessage("Invalid direction.", "Error");
            this.ok = false; //Error message
    }
}
    //YA NO SE HACE DE FORMA RECURRENTE
    /**
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
    **/
        
    //AHORA DE FORMA ITERATIVA
    
    // Tilt down starting from the top row recursively
    private void tiltDown(int row, int col) {
        for (int i = rows - 2; i >= 0; i--) { // Comienza desde la penúltima fila hacia arriba
            for (int j = i; j < rows - 1; j++) { // Intenta mover la ficha lo más abajo posible
                int[] currentPos = {j, col};
                int[] nextPos = {j + 1, col};
    
                Tile fromTile = tiles.get(currentPos[0]).get(currentPos[1]);
                Tile toTile = tiles.get(nextPos[0]).get(nextPos[1]);
    
                if (isTileEmpty(toTile) && !isTileEmpty(fromTile)) {
                    relocateTileMovement(fromTile, toTile, currentPos, nextPos); // Mueve la tile hacia abajo
                    this.ok = true; //Accion exitosa
                }
            }
        }
    }
    
    // Tilt up starting from the bottom row recursively
    private void tiltUp(int row, int col) {
        for (int i = 1; i < rows; i++) { // Comienza desde la segunda fila hacia abajo
            for (int j = i; j > 0; j--) { // Intenta mover la ficha lo más arriba posible
                int[] currentPos = {j, col};
                int[] nextPos = {j - 1, col};
    
                Tile fromTile = tiles.get(currentPos[0]).get(currentPos[1]);
                Tile toTile = tiles.get(nextPos[0]).get(nextPos[1]);
    
                if (isTileEmpty(toTile) && !isTileEmpty(fromTile)) {
                    relocateTileMovement(fromTile, toTile, currentPos, nextPos); // Mueve la tile hacia arriba
                    this.ok = true; //Accion exitosa
                }
            }
        }
    }
    
    // Tilt right starting from the left column recursively
    private void tiltRight(int row, int col) {
        for (int i = cols - 2; i >= 0; i--) { // Comienza desde la penúltima columna hacia la izquierda
            for (int j = i; j < cols - 1; j++) { // Intenta mover la ficha lo más a la derecha posible
                int[] currentPos = {row, j};
                int[] nextPos = {row, j + 1};
    
                Tile fromTile = tiles.get(currentPos[0]).get(currentPos[1]);
                Tile toTile = tiles.get(nextPos[0]).get(nextPos[1]);
    
                if (isTileEmpty(toTile) && !isTileEmpty(fromTile)) {
                    relocateTileMovement(fromTile, toTile, currentPos, nextPos); // Mueve la tile hacia la derecha
                    this.ok = true; //Accion exitosa
                }
            }
        }
    }
    
    // Tilt left starting from the right column recursively
    private void tiltLeft(int row, int col) {
        for (int i = 1; i < cols; i++) { // Comienza desde la segunda columna hacia la derecha
            for (int j = i; j > 0; j--) { // Intenta mover la ficha lo más a la izquierda posible
                int[] currentPos = {row, j};
                int[] nextPos = {row, j - 1};
    
                Tile fromTile = tiles.get(currentPos[0]).get(currentPos[1]);
                Tile toTile = tiles.get(nextPos[0]).get(nextPos[1]);
    
                if (isTileEmpty(toTile) && !isTileEmpty(fromTile)) {
                    relocateTileMovement(fromTile, toTile, currentPos, nextPos); // Mueve la tile hacia la izquierda
                    this.ok = true; //Accion exitosa
                }
            }
        }
    }
    
        // Método para obtener una baldosa en una posición específica
    private Tile getTileAtPosition(int row, int col) {
        if (row >= 0 && row < rows && col >= 0 && col < cols) {
            return tiles.get(row).get(col);  // Accede directamente a la baldosa
        }
        return null;
    }

    
        // Método para adicionar pegante a una baldosa y baldosas adyacentes
    public void addGlue(int row, int col) {
        if (isInBounds(row, col)) {
            applyGlue(row, col);
            this.ok = true; //Accion exitosa
        } else {
            showMessage("La baldosa en esa posición no existe.", "Error");
            this.ok = false; //Error message
        }
    
        if (isInBounds(row + 1, col)) applyGlue(row + 1, col);
        if (isInBounds(row - 1, col)) applyGlue(row - 1, col);
        if (isInBounds(row, col + 1)) applyGlue(row, col + 1);
        if (isInBounds(row, col - 1)) applyGlue(row, col - 1);
    }
    
    // Método para eliminar pegante de una baldosa
    public void removeGlue(int row, int col) {
        if (isInBounds(row, col)) {
            clearGlue(row, col);
            this.ok = true; //Accion exitosa
        } else {
            showMessage("La baldosa en esa posición no existe.", "Error");
            this.ok = false; //Error message
        }
    
        if (isInBounds(row + 1, col)) clearGlue(row + 1, col);
        if (isInBounds(row - 1, col)) clearGlue(row - 1, col);
        if (isInBounds(row, col + 1)) clearGlue(row, col + 1);
        if (isInBounds(row, col - 1)) clearGlue(row, col - 1);
    }
    
    // Método para verificar si una posición está dentro del tablero
    private boolean isInBounds(int row, int col) {
        return row >= 0 && row < rows && col >= 0 && col < cols;
    }
    
    // Método auxiliar para aplicar pegante
    private void applyGlue(int row, int col) {
        Tile previousTile = tiles.get(row).get(col);
        if (previousTile != null && !previousTile.isGlueApplied()) {
            previousTile.applyGlue();
            this.ok = true; //Accion exitosa
        } else {
            showMessage("La baldosa ya tiene pegamento.", "Error");
            this.ok = false; //Error message
        }
    }
    
    // Método auxiliar para remover pegante
    private void clearGlue(int row, int col) {
        Tile previousTile = tiles.get(row).get(col);
        if (previousTile != null && previousTile.isGlueApplied()) {
            previousTile.removeGlue();
            this.ok = true; //Accion exitosa
        } else {
            showMessage("No hay pegamento para eliminar.", "Error");
            this.ok = false; //Error message
        }
    }
    
    // Muestra un mensaje de error si el simulador es visible y cambia el estado de ok a false
    public void showMessage(String message, String title){
        if(this.visible){
            JOptionPane.showMessageDialog(null,message,title,JOptionPane.ERROR_MESSAGE);
            this.ok = false;
        }
    }
    
    // Método para verificar si el puzzle alcanzó el estado final
    public boolean isGoal() {
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                if (starting[row][col] != ending[row][col]) {
                    this.ok = false;
                    return false;
                }
            }
        }
        this.ok = true;
        return true;
    }
    
    // Hace visible el simulador
    public void makeVisible() {
        this.visible = true;
        for (List<Tile> row : tiles) {
            for (Tile tile : row) {
                tile.makeVisible();
            }
        }
    }

    // Hace invisible el simulador
    public void makeInvisible() {
        this.visible = false;
        for (List<Tile> row : tiles) {
            for (Tile tile : row) {
                tile.makeInvisible();
            }
        }
    }
    
    // Termina el simulador
    public void finish() {
        System.out.println("El simulador ha finalizado.");
        System.exit(0);
    }

    /**
     * Devuelve una copia de la matriz actual de edición (starting),
     * representando el estado actual del puzzle y pinta las baldosas.
     * @return Una copia de la matriz starting.
     */
    
    public char[][] actualArrangement() {
        // Crear una copia de la matriz starting
        char[][] currentArrangement = new char[rows][cols];
    
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                currentArrangement[row][col] = starting[row][col]; // Copia el valor actual
    
                // Simulación de pintar o mostrar la baldosa
                Tile tile = tiles.get(row).get(col);
                System.out.println("Baldosa en (" + row + ", " + col + "): " + tile.getLabel());
            }
        }
    
        return currentArrangement; // Retornar la copia de la matriz
    }


    // Retorna si la última acción fue exitosa
    public boolean ok() {
        return this.ok;
    }
    
    
    public static void main(String[] args) {
        
        /**
         * FIRST TEST
         */
        
        /**
        // Crear matrices de caracteres de ejemplo con 8 filas y 4 columnas
        char[][] starting = {
            {'b', 'b','y','b'},
            {'y', '*','r','y'},
            {'g','*','g','b'} 
        };

        char[][] ending = {
            {'y', '*','*','r'},
            {'*', 'b','g','b'},
            {'*','g','y','*'}
        };

        // Instanciar los objetos de Puzzle
        Puzzle pz1 = new Puzzle(3, 4); // Tablero sin matrices
        Puzzle pz2 = new Puzzle(starting, ending); // Tablero con matrices
        
        //pz2.addTile(77,81,'b');
        //pz2.addTile(0,1,'r');
        //pz2.addTile(2,3,'b');
        //pz2.addTile(1,1,'g');
        
        //pz2.addTile(0,0,Color.BLACK);
        
        //pz2.addGlue(0,0);
        //pz2.addGlue(1,2);
        
        //pz2.removeGlue(1,2);
        
        //pz2.deleteTile(8,0);
        //pz2.deleteTile(1,1);
        
        int[] from = {0,1};
        int[] to   = {1,2};  
        //pz2.relocateTile(from, to);
        
        int[] from1 = {2,3};
        int[] to1   = {0,1};  
        //pz2.relocateTile(from1, to1);
        
        int[] from2 = {4124,414};
        int[] to2   = {0,131};  
        //pz2.relocateTile(from2, to2);
        
        int[] from3 = {2,0};
        int[] to3   = {0,2};  
        //pz2.relocateTile(from3, to3);
        
        //pz2.tilt('r');
        //pz2.tilt('l');
        //pz2.tilt('u');
        //pz2.tilt('d');
        
        **/
        
        
        
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
        
    }
}
