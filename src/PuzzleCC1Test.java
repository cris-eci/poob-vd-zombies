import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PuzzleCC1Test {

    private Puzzle puzzle;
    private Puzzle puzzleBoard;
    private char[][] starting;
    private char[][] ending;

    @BeforeEach
    public void setUp() {
        // Configurar un estado inicial del puzzle con matrices de ejemplo
        starting = new char[][]{
            {'*', 'b', 'g', 'y'},
            {'b', 'y', 'r', 'g'},
            {'g', 'r', '*', 'y'}
        };

        ending = new char[][]{
            {'r', 'b', 'g', 'y'},
            {'b', 'y', 'r', 'g'},
            {'g', 'r', 'b', 'y'}
        };
        
        puzzleBoard = new Puzzle(3, 4); // Tablero sin matrices
        puzzle = new Puzzle(starting, ending);  //Tablero para llenarlo de baldosas
    }

    // Prueba para verificar si el puzzle alcanza el estado final
    @Test
    public void testIsGoalTrue() {
        assertTrue(puzzle.isGoal(), "El puzzle debería estar en el estado final.");
    }

    @Test
    public void testIsGoalFalse() {
        starting[0][0] = 'x'; // Cambia algo en el estado inicial
        puzzle = new Puzzle(starting, ending);
        assertFalse(puzzle.isGoal(), "El puzzle no debería estar en el estado final.");
    }

    // Prueba para verificar la visibilidad
    @Test
    public void testMakeVisible() {
        puzzle.makeInvisible(); // Primero lo hace invisible
        puzzle.makeVisible(); // Luego lo hace visible
        assertTrue(puzzle.ok(), "El simulador debería ser visible.");
    }

    @Test
    public void testMakeInvisible() {
        puzzle.makeInvisible();
        // Verifica que después de hacer invisible, no afecta `ok()`
        assertTrue(puzzle.ok(), "El simulador debería estar en estado válido aunque no sea visible.");
    }

    // Prueba para agregar baldosas
    @Test
    public void testAddTileValidPosition() {
        puzzle.addTile(2, 3, 'r');
        assertTrue(puzzle.ok(), "Debería poder agregar una baldosa en una posición válida.");
    }

    @Test
    public void testAddTileInvalidPosition() {
        puzzle.addTile(4, 4, 'r'); // Posición inválida (fuera de los límites)
        assertFalse(puzzle.ok(), "No debería poder agregar una baldosa fuera de los límites.");
    }

    // Prueba para eliminar baldosas
    @Test
    public void testDeleteTileValidPosition() {
        puzzle.addTile(1, 1, 'r');
        puzzle.deleteTile(1, 1);
        assertTrue(puzzle.ok(), "Debería poder eliminar una baldosa en una posición válida.");
    }

    @Test
    public void testDeleteTileInvalidPosition() {
        puzzle.deleteTile(4, 4); // Posición inválida (fuera de los límites)
        assertFalse(puzzle.ok(), "No debería poder eliminar una baldosa fuera de los límites.");
    }

    // Prueba para consultar el estado actual del puzzle
    
    @Test
    public void testActualArrangement() {
        char[][] currentState = puzzle.actualArrangement();
        assertArrayEquals(starting, currentState, "El estado actual del puzzle debería coincidir con el inicial.");
    }
    
    // Prueba para el método tilt (desplazamiento)
    @Test
    public void testTiltValidDirection() {
        puzzle.tilt('d'); // Tilt hacia abajo
        assertTrue(puzzle.ok(), "El tilt hacia abajo debería ser exitoso.");
    }

    @Test
    public void testTiltInvalidDirection() {
        puzzle.tilt('x'); // Dirección no válida
        assertFalse(puzzle.ok(), "El tilt con una dirección inválida debería fallar.");
    }

    // Prueba para verificar si se actualiza `ok` cuando se muestra un mensaje
    @Test
    public void testShowMessageUpdatesOk() {
        puzzle.addTile(4, 4, 'r'); // Acción inválida debería mostrar un mensaje
        assertFalse(puzzle.ok(), "Después de mostrar un mensaje de error, `ok` debería ser falso.");
    }
    
    // Prueba para mover una baldosa a una posición válida
    
    @Test
    public void testRelocateValidMovement() {
        // Estado inicial: mover la baldosa (0, 0) a la posición vacía (2, 2)
        int[] from = {0, 0};
        int[] to = {2, 2};
        puzzle.addTile(0, 0, 'r'); // Agregar baldosa en la posición inicial
        puzzle.deleteTile(2, 2);   // Vaciar la posición final

        puzzle.relocateTile(from, to); // Relocalizar baldosa

        assertTrue(puzzle.ok(), "La baldosa debería moverse correctamente.");
        assertEquals('r', puzzle.actualArrangement()[2][2], "La baldosa debería estar en la nueva posición.");
        assertEquals('*', puzzle.actualArrangement()[0][0], "La posición anterior debería estar vacía.");
    }

    // Prueba para mover una baldosa a una posición inválida (fuera de los límites)
    @Test
    public void testRelocateInvalidPositionOutOfBounds() {
        int[] from = {0, 0};
        int[] to = {4, 4}; // Posición fuera de los límites

        puzzle.addTile(0, 0, 'r'); // Agregar baldosa en la posición inicial

        puzzle.relocateTile(from, to);

        assertFalse(puzzle.ok(), "El movimiento debería fallar al intentar mover la baldosa fuera de los límites.");
        assertEquals('r', puzzle.actualArrangement()[0][0], "La baldosa debería seguir en su posición original.");
    }

    // Prueba para mover una baldosa a una posición que ya está ocupada
    @Test
    public void testRelocateOccupiedPosition() {
        int[] from = {0, 0};
        int[] to = {1, 1}; // Posición ya ocupada

        puzzle.addTile(0, 0, 'r'); // Agregar baldosa en la posición inicial
        puzzle.addTile(1, 1, 'b'); // Agregar baldosa en la posición de destino

        puzzle.relocateTile(from, to);

        assertFalse(puzzle.ok(), "El movimiento debería fallar al intentar mover la baldosa a una posición ocupada.");
        assertEquals('r', puzzle.actualArrangement()[0][0], "La baldosa debería seguir en su posición original.");
        assertEquals('b', puzzle.actualArrangement()[1][1], "La baldosa en la posición de destino no debería cambiar.");
    }

    // Prueba para mover una baldosa desde una posición vacía (inválido)
    @Test
    public void testRelocateFromEmptyPosition() {
        int[] from = {2, 2}; // Posición vacía
        int[] to = {1, 1};

        puzzle.deleteTile(2, 2); // Asegurarse de que la posición de origen está vacía
        puzzle.addTile(1, 1, 'b'); // Agregar baldosa en la posición de destino

        puzzle.relocateTile(from, to);

        assertTrue(puzzle.ok(), "El movimiento debería fallar al intentar mover una baldosa desde una posición vacía.");
        assertEquals('b', puzzle.actualArrangement()[1][1], "La baldosa en la posición de destino no debería cambiar.");
        assertEquals('*', puzzle.actualArrangement()[2][2], "La posición de origen debería seguir vacía.");
    }

    // Prueba para mover una baldosa de una posición válida a otra posición válida
    @Test
    public void testRelocateBetweenValidPositions() {
        int[] from = {0, 1};
        int[] to = {2, 1};

        puzzle.addTile(0, 1, 'b'); // Agregar baldosa en la posición inicial
        puzzle.deleteTile(2, 1);   // Vaciar la posición final

        puzzle.relocateTile(from, to); // Relocalizar baldosa

        assertTrue(puzzle.ok(), "La baldosa debería moverse correctamente entre posiciones válidas.");
        assertEquals('b', puzzle.actualArrangement()[2][1], "La baldosa debería estar en la nueva posición.");
        assertEquals('*', puzzle.actualArrangement()[0][1], "La posición anterior debería estar vacía.");
    }
    
    // Prueba para terminar el simulador
    @Test
    public void testFinish() {
        assertDoesNotThrow(() -> puzzle.finish(), "El método finish debería terminar el simulador sin lanzar excepciones.");
    }
    
    @Test
    public void testActualArrangementReturnsCopy() {
        char[][] currentArrangement = puzzle.actualArrangement();
    
        // Asegurarse de que el contenido de la matriz sea igual
        assertArrayEquals(starting, currentArrangement, "La matriz devuelta debería tener los mismos valores que la original.");
    
        // Modificar la copia y verificar que la original no cambie
        currentArrangement[0][0] = 'x'; // Modificamos la copia
        assertNotEquals(currentArrangement[0][0], puzzle.actualArrangement()[0][0], "La matriz original no debería cambiar cuando se modifica la copia.");
    }

}
