package test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import domain.POOBvsZombies;

/**
 *
 * @author Andersson David Sánchez Mendez
 * @author Cristian Santiago Pedraza Rodriguez
 */
public class POOBvsZombiesTest {
    private POOBvsZombies game;

    @BeforeEach
    public void setUp() {
        // Configuramos el estado inicial para las pruebas
        ArrayList<String> plants = new ArrayList<>();
        plants.add("Sunflower");
        plants.add("Peashooter");

        ArrayList<String> zombies = new ArrayList<>();
        zombies.add("Basic");
        zombies.add("Conehead");
        // Constructor dos jugadores. 
        game = new POOBvsZombies(300, "PlayerOne", plants, 100, "PlayerTwo", 100, zombies);
    }

    @Test
    public void testAddEntity() {
        // Agregamos una planta en la posición (0, 0)
        game.addEntity(0, 0, "Peashooter");
        String entityName = game.getEntity(0, 0);
        assertEquals("Peashooter", entityName);
    }

    @Test
    public void testDeleteEntity() {
        // Agregamos y luego eliminamos una entidad
        game.addEntity(1, 1, "Sunflower");
        game.deleteEntity(1, 1);
        String entityName = game.getEntity(1, 1);
        assertNull(entityName);
    }

    @Test
    public void testGetFirstPlantInRow() {
        // Agregamos plantas en diferentes columnas
        game.addEntity(2, 2, "Peashooter");
        game.addEntity(2, 5, "Sunflower");
        int firstPlantCol = game.getFirstPlantInRow(2);
        assertEquals(5, firstPlantCol);
    }

    @Test
    public void testGetLawnmowerInRow() {
        // Verificamos si hay cortadora de césped en la fila 0
        boolean hasLawnmower = game.getLawnmowerInRow(0);
        assertFalse(hasLawnmower);
    }

    // @Test(expected = IndexOutOfBoundsException.class)
    // public void testAgregarEntidadPosicionInvalida() {
    //     // Intentamos agregar una entidad en una posición inválida
    //     game.addEntity(5, 0, "Peashooter");
    // }

    // ...existing code...
}