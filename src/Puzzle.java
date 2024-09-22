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
    
    
    // Método para imprimir el estado del tablero
    public void printBoardState() {
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                Tile tile = getTileAtPosition(row, col);
                char label = tile.getLabel();
                System.out.print(label + " ");
            }
            System.out.println();
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
    
    // Método para relocalizar una baldosa
    public void relocateTile(int[] from, int[] to) {
        // Validar las coordenadas de entrada
        if (!areValidCoordinates(from) || !areValidCoordinates(to)) {
            showMessage("Coordenadas inválidas.", "Error");
            this.ok = false;
            return;
        }

        Tile fromTile = tiles.get(from[0]).get(from[1]);
        Tile toTile = tiles.get(to[0]).get(to[1]);

        // Validar existencia de la baldosa de origen y disponibilidad de la baldosa de destino
        if (isTileEmpty(fromTile)) {
            showMessage("No puedes mover una baldosa inexistente.", "Error");
            this.ok = false;
        } else if (!isTileEmpty(toTile)) {
            showMessage("Ya hay una baldosa en la posición de destino.", "Error");
            this.ok = false;
        } else if (fromTile.hasGlue() || fromTile.isStuck()) {
            showMessage("No puedes mover una baldosa que tiene pegamento o está pegada.", "Error");
            this.ok = false;
        } else {
            // Realizar el movimiento
            this.relocateTileMovement(fromTile, toTile, from, to);
            this.ok = true;
        }
    }

    // Método auxiliar para realizar el movimiento visual y actualizar la lista de baldosas
    public void relocateTileMovement(Tile fromTile, Tile toTile, int[] from, int[] to) {
        // Mover la instancia de la baldosa visualmente
        fromTile.moveHorizontal((to[1] - from[1]) * (tileSize + margin));
        fromTile.moveVertical((to[0] - from[0]) * (tileSize + margin));
        // Actualizar la lista de baldosas: mover la baldosa a la nueva posición
        tiles.get(to[0]).set(to[1], fromTile);
        // Crear una nueva baldosa vacía en la posición original
        Tile emptyTile = createEmptyTile(from[0], from[1]);
        tiles.get(from[0]).set(from[1], emptyTile);
    }

    // Método para validar si las coordenadas son correctas
    private boolean areValidCoordinates(int[] coords) {
        return coords.length == 2 && coords[0] >= 0 && coords[0] < rows && coords[1] >= 0 && coords[1] < cols;
    }    

    // Método para aplicar pegamento a una baldosa
    public void addGlue(int row, int col) {
        Tile tile = getTileAtPosition(row, col);
        if (tile == null || isTileEmpty(tile)) {
            showMessage("No se puede aplicar pegamento a una baldosa vacía.", "Error");
            this.ok = false;
            return;
        }
        if (tile.hasGlue()) {
            showMessage("Esta baldosa ya tiene pegamento aplicado.", "Error");
            this.ok = false;
            return;
        }

        tile.setHasGlue(true);

        // Cambiar el color de la baldosa a una versión más pálida
        Color evenPalerColor = getPaleColor(tile.getOriginalColor(), 150);
        tile.setTileColor(evenPalerColor);

        // Actualizar las baldosas adyacentes
        updateAdjacentTiles(tile);

        // Recolectar el grupo de baldosas pegadas
        List<Tile> group = new ArrayList<>();
        collectStuckGroup(tile, group);

        // Resetear las banderas de visitado
        resetVisitedFlags();

        this.ok = true;
    }

    // Método para eliminar el pegamento de una baldosa
    public void deleteGlue(int row, int col) {
        Tile tile = getTileAtPosition(row, col);
        if (tile == null || !tile.hasGlue()) {
            showMessage("No hay pegamento para eliminar en esta baldosa.", "Error");
            this.ok = false;
            return;
        }

        tile.setHasGlue(false);

        // Si la baldosa ya no está pegada a ninguna otra, ajustar el color
        if (!tile.isStuck()) {
            // Cambiar el color a una versión ligeramente más clara
            Color slightlyPalerColor = getPaleColor(tile.getOriginalColor(), 50);
            tile.setTileColor(slightlyPalerColor);
        }

        // Actualizar las baldosas adyacentes
        updateAdjacentTilesAfterGlueRemoval(tile);

        // Recolectar el grupo de baldosas pegadas para actualizar estados
        List<Tile> group = new ArrayList<>();
        collectStuckGroup(tile, group);

        // Resetear las banderas de visitado
        resetVisitedFlags();

        this.ok = true;
    }

    // Método para actualizar las baldosas adyacentes después de aplicar pegamento
    private void updateAdjacentTiles(Tile tile) {
        int row = tile.getRow();
        int col = tile.getCol();
        int[][] directions = { { -1, 0 }, { 1, 0 }, { 0, -1 }, { 0, 1 } };
        for (int[] dir : directions) {
            int adjRow = row + dir[0];
            int adjCol = col + dir[1];
            Tile adjacentTile = getTileAtPosition(adjRow, adjCol);

            if (adjacentTile != null && !isTileEmpty(adjacentTile) && !adjacentTile.isStuck()) {
                adjacentTile.setIsStuck(true);
                // Cambiar el color a una versión pálida
                Color paleColor = getPaleColor(adjacentTile.getOriginalColor(), 100);
                adjacentTile.setTileColor(paleColor);
            }
        }
    }

    // Método para actualizar las baldosas adyacentes después de eliminar pegamento
    private void updateAdjacentTilesAfterGlueRemoval(Tile tile) {
        int row = tile.getRow();
        int col = tile.getCol();
        int[][] directions = { { -1, 0 }, { 1, 0 }, { 0, -1 }, { 0, 1 } };
        for (int[] dir : directions) {
            int adjRow = row + dir[0];
            int adjCol = col + dir[1];
            Tile adjacentTile = getTileAtPosition(adjRow, adjCol);

            if (adjacentTile != null && !isTileEmpty(adjacentTile)) {
                if (isAdjacentToGlue(adjacentTile)) {
                    // Si aún está adyacente a otra baldosa con pegamento, se mantiene pegada
                    continue;
                } else {
                    adjacentTile.setIsStuck(false);
                    if (!adjacentTile.hasGlue()) {
                        // Cambiar el color a una versión ligeramente más clara
                        Color slightlyPalerColor = getPaleColor(adjacentTile.getOriginalColor(), 50);
                        adjacentTile.setTileColor(slightlyPalerColor);
                    }
                }
            }
        }
    }

    // Método auxiliar para verificar si una baldosa está adyacente a alguna baldosa con pegamento
    private boolean isAdjacentToGlue(Tile tile) {
        int row = tile.getRow();
        int col = tile.getCol();
        int[][] directions = { { -1, 0 }, { 1, 0 }, { 0, -1 }, { 0, 1 } };

        for (int[] dir : directions) {
            int adjRow = row + dir[0];
            int adjCol = col + dir[1];
            Tile adjacentTile = getTileAtPosition(adjRow, adjCol);
            if (adjacentTile != null && adjacentTile.hasGlue()) {
                return true;
            }
        }
        return false;
    }

    // Método para obtener una versión más pálida de un color
    private Color getPaleColor(Color color, int palenessFactor) {
        int r = Math.min(255, color.getRed() + palenessFactor);
        int g = Math.min(255, color.getGreen() + palenessFactor);
        int b = Math.min(255, color.getBlue() + palenessFactor);
        return new Color(r, g, b);
    }

    // Método para inclinar el puzzle en una dirección
    public void tilt(char direction) {
        switch (direction) {
            case 'd':
                for (int col = 0; col < cols; col++) {
                    tiltDownWithGlue(col);
                }
                break;
            case 'u':
                for (int col = 0; col < cols; col++) {
                    tiltUpWithGlue(col);
                }
                break;
            case 'r':
                for (int row = 0; row < rows; row++) {
                    tiltRightWithGlue(row);
                }
                break;
            case 'l':
                for (int row = 0; row < rows; row++) {
                    tiltLeftWithGlue(row);
                }
                break;
            default:
                showMessage("Dirección inválida.", "Error");
                this.ok = false;
        }
        resetVisitedFlags(); // Resetear las banderas de visitado después de la inclinación
    }

    // Métodos de inclinación considerando pegamento y baldosas pegadas

    // Inclinación hacia arriba
    private void tiltUpWithGlue(int col) {
        for (int row = 0; row < rows; row++) {
            Tile tile = getTileAtPosition(row, col);
            if (!isTileEmpty(tile)) {
                if (!tile.isStuck() && !tile.hasGlue()) {
                    int maxMove = calculateMaxMoveUp(row, col, null);
                    moveTileUp(tile, maxMove);
                } else if (!tile.isVisited()) {
                    List<Tile> group = new ArrayList<>();
                    collectStuckGroup(tile, group);
                    int maxMove = calculateMaxMoveUpGroup(group);
                    moveGroupUp(group, maxMove);
                }
            }
        }
    }

    // Inclinación hacia abajo
    private void tiltDownWithGlue(int col) {
        for (int row = rows - 1; row >= 0; row--) {
            Tile tile = getTileAtPosition(row, col);
            if (!isTileEmpty(tile)) {
                if (!tile.isStuck() && !tile.hasGlue()) {
                    int maxMove = calculateMaxMoveDown(row, col, null);
                    moveTileDown(tile, maxMove);
                } else if (!tile.isVisited()) {
                    List<Tile> group = new ArrayList<>();
                    collectStuckGroup(tile, group);
                    int maxMove = calculateMaxMoveDownGroup(group);
                    moveGroupDown(group, maxMove);
                }
            }
        }
    }

    // Inclinación hacia la derecha
    private void tiltRightWithGlue(int row) {
        for (int col = cols - 1; col >= 0; col--) {
            Tile tile = getTileAtPosition(row, col);
            if (!isTileEmpty(tile)) {
                if (!tile.isStuck() && !tile.hasGlue()) {
                    int maxMove = calculateMaxMoveRight(row, col, null);
                    moveTileRight(tile, maxMove);
                } else if (!tile.isVisited()) {
                    List<Tile> group = new ArrayList<>();
                    collectStuckGroup(tile, group);
                    int maxMove = calculateMaxMoveRightGroup(group);
                    moveGroupRight(group, maxMove);
                }
            }
        }
    }

    // Inclinación hacia la izquierda
    private void tiltLeftWithGlue(int row) {
        for (int col = 0; col < cols; col++) {
            Tile tile = getTileAtPosition(row, col);
            if (!isTileEmpty(tile)) {
                if (!tile.isStuck() && !tile.hasGlue()) {
                    int maxMove = calculateMaxMoveLeft(row, col, null);
                    moveTileLeft(tile, maxMove);
                } else if (!tile.isVisited()) {
                    List<Tile> group = new ArrayList<>();
                    collectStuckGroup(tile, group);
                    int maxMove = calculateMaxMoveLeftGroup(group);
                    moveGroupLeft(group, maxMove);
                }
            }
        }
    }

    // Métodos auxiliares para calcular el movimiento máximo y mover baldosas/grupos

    // Movimiento hacia arriba
    private int calculateMaxMoveUp(int row, int col, List<Tile> group) {
        int maxMove = 0;
        for (int i = row - 1; i >= 0; i--) {
            Tile nextTile = getTileAtPosition(i, col);
            if (isTileEmpty(nextTile) || (group != null && group.contains(nextTile))) {
                maxMove++;
            } else {
                break;
            }
        }
        return maxMove;
    }

    private int calculateMaxMoveUpGroup(List<Tile> group) {
        int maxMove = rows;
        for (Tile tile : group) {
            int tileMaxMove = calculateMaxMoveUp(tile.getRow(), tile.getCol(), group);
            maxMove = Math.min(maxMove, tileMaxMove);
        }
        return maxMove;
    }

    private void moveTileUp(Tile tile, int steps) {
        if (steps == 0) return;
        int newRow = tile.getRow() - steps;
        tile.moveVertical(-steps * (tileSize + margin));
        tiles.get(tile.getRow()).set(tile.getCol(), createEmptyTile(tile.getRow(), tile.getCol()));
        tiles.get(newRow).set(tile.getCol(), tile);
        tile.setRow(newRow);
    }

    private void moveGroupUp(List<Tile> group, int steps) {
        if (steps == 0) return;
        // Ordenar el grupo para que las baldosas superiores se muevan primero
        group.sort((t1, t2) -> Integer.compare(t1.getRow(), t2.getRow()));
        // Eliminar baldosas de sus posiciones antiguas
        for (Tile tile : group) {
            tiles.get(tile.getRow()).set(tile.getCol(), createEmptyTile(tile.getRow(), tile.getCol()));
        }
        // Mover baldosas a sus nuevas posiciones
        for (Tile tile : group) {
            int newRow = tile.getRow() - steps;
            tile.moveVertical(-steps * (tileSize + margin));
            tiles.get(newRow).set(tile.getCol(), tile);
            tile.setRow(newRow);
        }
    }

    // Métodos similares para movimientos hacia abajo, izquierda y derecha...

    // Movimiento hacia abajo
    private int calculateMaxMoveDown(int row, int col, List<Tile> group) {
        int maxMove = 0;
        for (int i = row + 1; i < rows; i++) {
            Tile nextTile = getTileAtPosition(i, col);
            if (isTileEmpty(nextTile) || (group != null && group.contains(nextTile))) {
                maxMove++;
            } else {
                break;
            }
        }
        return maxMove;
    }

    private int calculateMaxMoveDownGroup(List<Tile> group) {
        int maxMove = rows;
        for (Tile tile : group) {
            int tileMaxMove = calculateMaxMoveDown(tile.getRow(), tile.getCol(), group);
            maxMove = Math.min(maxMove, tileMaxMove);
        }
        return maxMove;
    }

    private void moveTileDown(Tile tile, int steps) {
        if (steps == 0) return;
        int newRow = tile.getRow() + steps;
        tile.moveVertical(steps * (tileSize + margin));
        tiles.get(tile.getRow()).set(tile.getCol(), createEmptyTile(tile.getRow(), tile.getCol()));
        tiles.get(newRow).set(tile.getCol(), tile);
        tile.setRow(newRow);
    }

    private void moveGroupDown(List<Tile> group, int steps) {
        if (steps == 0) return;
        // Ordenar el grupo para que las baldosas inferiores se muevan primero
        group.sort((t1, t2) -> Integer.compare(t2.getRow(), t1.getRow()));
        // Eliminar baldosas de sus posiciones antiguas
        for (Tile tile : group) {
            tiles.get(tile.getRow()).set(tile.getCol(), createEmptyTile(tile.getRow(), tile.getCol()));
        }
        // Mover baldosas a sus nuevas posiciones
        for (Tile tile : group) {
            int newRow = tile.getRow() + steps;
            tile.moveVertical(steps * (tileSize + margin));
            tiles.get(newRow).set(tile.getCol(), tile);
            tile.setRow(newRow);
        }
    }

    // Movimiento hacia la derecha
    private int calculateMaxMoveRight(int row, int col, List<Tile> group) {
        int maxMove = 0;
        for (int i = col + 1; i < cols; i++) {
            Tile nextTile = getTileAtPosition(row, i);
            if (isTileEmpty(nextTile) || (group != null && group.contains(nextTile))) {
                maxMove++;
            } else {
                break;
            }
        }
        return maxMove;
    }

    private int calculateMaxMoveRightGroup(List<Tile> group) {
        int maxMove = cols;
        for (Tile tile : group) {
            int tileMaxMove = calculateMaxMoveRight(tile.getRow(), tile.getCol(), group);
            maxMove = Math.min(maxMove, tileMaxMove);
        }
        return maxMove;
    }

    private void moveTileRight(Tile tile, int steps) {
        if (steps == 0) return;
        int newCol = tile.getCol() + steps;
        tile.moveHorizontal(steps * (tileSize + margin));
        tiles.get(tile.getRow()).set(tile.getCol(), createEmptyTile(tile.getRow(), tile.getCol()));
        tiles.get(tile.getRow()).set(newCol, tile);
        tile.setCol(newCol);
    }

    private void moveGroupRight(List<Tile> group, int steps) {
        if (steps == 0) return;
        // Ordenar el grupo para que las baldosas con columnas más altas se muevan primero
        group.sort((t1, t2) -> Integer.compare(t2.getCol(), t1.getCol()));
        // Eliminar baldosas de sus posiciones antiguas
        for (Tile tile : group) {
            tiles.get(tile.getRow()).set(tile.getCol(), createEmptyTile(tile.getRow(), tile.getCol()));
        }
        // Mover baldosas a sus nuevas posiciones
        for (Tile tile : group) {
            int newCol = tile.getCol() + steps;
            tile.moveHorizontal(steps * (tileSize + margin));
            tiles.get(tile.getRow()).set(newCol, tile);
            tile.setCol(newCol);
        }
    }

    // Movimiento hacia la izquierda
    private int calculateMaxMoveLeft(int row, int col, List<Tile> group) {
        int maxMove = 0;
        for (int i = col - 1; i >= 0; i--) {
            Tile nextTile = getTileAtPosition(row, i);
            if (isTileEmpty(nextTile) || (group != null && group.contains(nextTile))) {
                maxMove++;
            } else {
                break;
            }
        }
        return maxMove;
    }

    private int calculateMaxMoveLeftGroup(List<Tile> group) {
        int maxMove = cols;
        for (Tile tile : group) {
            int tileMaxMove = calculateMaxMoveLeft(tile.getRow(), tile.getCol(), group);
            maxMove = Math.min(maxMove, tileMaxMove);
        }
        return maxMove;
    }

    private void moveTileLeft(Tile tile, int steps) {
        if (steps == 0) return;
        int newCol = tile.getCol() - steps;
        tile.moveHorizontal(-steps * (tileSize + margin));
        tiles.get(tile.getRow()).set(tile.getCol(), createEmptyTile(tile.getRow(), tile.getCol()));
        tiles.get(tile.getRow()).set(newCol, tile);
        tile.setCol(newCol);
    }

    private void moveGroupLeft(List<Tile> group, int steps) {
        if (steps == 0) return;
        // Ordenar el grupo para que las baldosas con columnas más bajas se muevan primero
        group.sort((t1, t2) -> Integer.compare(t1.getCol(), t2.getCol()));
        // Eliminar baldosas de sus posiciones antiguas
        for (Tile tile : group) {
            tiles.get(tile.getRow()).set(tile.getCol(), createEmptyTile(tile.getRow(), tile.getCol()));
        }
        // Mover baldosas a sus nuevas posiciones
        for (Tile tile : group) {
            int newCol = tile.getCol() - steps;
            tile.moveHorizontal(-steps * (tileSize + margin));
            tiles.get(tile.getRow()).set(newCol, tile);
            tile.setCol(newCol);
        }
    }

    // Método para recolectar todas las baldosas en un grupo pegado
    private void collectStuckGroup(Tile tile, List<Tile> group) {
        if (tile.isVisited()) return;
        tile.setVisited(true);
        group.add(tile);
        int row = tile.getRow();
        int col = tile.getCol();
        int[][] directions = { { -1, 0 }, { 1, 0 }, { 0, -1 }, { 0, 1 } };
        for (int[] dir : directions) {
            int adjRow = row + dir[0];
            int adjCol = col + dir[1];
            Tile adjacentTile = getTileAtPosition(adjRow, adjCol);
            if (adjacentTile != null && !adjacentTile.isVisited() && !isTileEmpty(adjacentTile) &&
                (adjacentTile.isStuck() || adjacentTile.hasGlue())) {
                collectStuckGroup(adjacentTile, group);
            }
        }
    }

    // Método para crear una baldosa vacía
    private Tile createEmptyTile(int row, int col) {
        int xPosition = 105 + (col * (tileSize + margin));
        int yPosition = 55 + (row * (tileSize + margin));
        Tile emptyTile = new Tile(tileSize, '*', xPosition, yPosition, padding, row, col);
        return emptyTile;
    }

    // Método para obtener una baldosa en una posición específica
    private Tile getTileAtPosition(int row, int col) {
        if (row >= 0 && row < rows && col >= 0 && col < cols) {
            return tiles.get(row).get(col);
        }
        return null;
    }

    // Método para verificar si una baldosa está vacía (basado en el color lightBrown)
    private boolean isTileEmpty(Tile tile) {
        return tile.getTileColor().equals(lightBrown);
    }

    // Resetear las banderas de visitado después de la inclinación
    private void resetVisitedFlags() {
        for (List<Tile> rowList : tiles) {
            for (Tile tile : rowList) {
                tile.setVisited(false);
            }
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
        
        for (List<Tile> row : referingTiles) {
            for (Tile tile : row) {
                tile.makeVisible();
            }
        }
        
        // Verificar si los tableros han sido inicializados
        if (startingBoard != null) {
            startingBoard.makeVisible();  // Hace visible el tablero inicial
        }
        
        if (endingBoard != null) {
            endingBoard.makeVisible();    // Hace visible el tablero final
        }
        
        this.ok = true;  // Indicar que la acción fue exitosa
            
    }

    // Hace invisible el simulador
    
    public void makeInvisible() {
        this.visible = false;
        
        for (List<Tile> row : tiles) {
            for (Tile tile : row) {
                tile.makeInvisible();
            }
        }
        
        for (List<Tile> row : referingTiles) {
            for (Tile tile : row) {
                tile.makeInvisible();
            }
        }
        
        // Verificar si los tableros han sido inicializados
        
        if (startingBoard != null) {
            startingBoard.makeInvisible();  // Hace invisible el tablero inicial
        }
        
        if (endingBoard != null) {
            endingBoard.makeInvisible();    // Hace invisible el tablero final
        }
        
        this.ok = true;  // Indicar que la acción fue exitosa
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
        
        pz4.addTile(9,0,'r');
        pz4.addGlue(9,1);
        pz4.tilt('u');
        pz4.tilt('r');
        
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
        pz4.addTile(6,0,'r');
        
        int[] from4 = {6,0};
        int[] to4   = {3,1};
        pz4.relocateTile(from4,to4);
    }
}
