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
                previousTile.setTileColor('n');        
            } else {
                JOptionPane.showMessageDialog(null, "There is no a tile here now.", "Error", JOptionPane.ERROR_MESSAGE);
            }        
        }
    }

    public void relocateTile(int[] from, int[] to){                        
        if (from.length != 2){
            JOptionPane.showMessageDialog(null, "You just should give row and column for from", "Error", JOptionPane.ERROR_MESSAGE);
        } else if (to.length != 2){
            JOptionPane.showMessageDialog(null, "You just should give row and column for to", "Error", JOptionPane.ERROR_MESSAGE);
        }         
        
        int fromRow = from[0];   
        int fromCol = from[1];
        
        if (fromRow >= rows || fromCol >= cols){
            JOptionPane.showMessageDialog(null,"Wrong row position, you have exceeded the puzzle space in from [].", "Error", JOptionPane.ERROR_MESSAGE);
        }
                
        int toRow = to[0];   
        int toCol = to[1];
        
        if (toRow >= rows || toCol >= cols){
            JOptionPane.showMessageDialog(null,"Wrong row position, you have exceeded the puzzle space in to [].", "Error", JOptionPane.ERROR_MESSAGE);
        }
        
        Tile fromTile = tiles.get(fromRow).get(fromCol);
        Tile toTile = tiles.get(toRow).get(toCol);
        
        if(fromTile.getTileColor().equals(lightBrown)){
            JOptionPane.showMessageDialog(null,"you cannot move a non-existent tile", "Error", JOptionPane.ERROR_MESSAGE);
        } else if(!toTile.getTileColor().equals(lightBrown)){
            JOptionPane.showMessageDialog(null,"There is a tile here now.", "Error", JOptionPane.ERROR_MESSAGE);
        } else{
            char fromLabel = fromTile.getLabel();
            char toLabel = toTile.getLabel();            
            fromTile.setTileColor(fromLabel);
            toTile.setTileColor(toLabel);
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
        pz2.addTile(0,1,'r'); // should addTile - ok
        //pz2.addTile(10,10,Color.BLUE); // should not addTile - ok
        //pz2.addTile(0,0,Color.BLACK); // should not addTile (It exist a tile in this index) -ok

        // deleteTile manual tests
        pz2.deleteTile(10,10); // should not delete (out of index) - ok
        //pz2.deleteTile(1,1); // should not delete (THere is not a tile in this index)) - ok
        //pz2.deleteTile(1,0); // should delete a tile - ok
        
        // relocateTile manual tests
        int[] from = {0,0};
        int[] to   = {0,1};        
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
        int[] from5 = {0,0};
        int[] to5   = {1,0};        
        //pz2.relocateTile(from5,to5); // should not pass, there is a tile there
        pz2.relocateTile(from,to); // shouldn't pass, relocate an origin tile with a new tile
        
    }
}
