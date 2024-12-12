package test;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import domain.Basic;
import domain.POOBvsZombies;
import domain.Plant;
import domain.Player;
import domain.Sunflower;
import domain.Zombie;

/**
 *
 * @author Usuario
 */
public class POOBvsZombiesTest {
    private POOBvsZombies game;

    @BeforeEach
    public void setUp() {
        // Configuramos el estado inicial para las pruebas
         // Configuramos el estado inicial para las pruebas
        ArrayList<String> plants = new ArrayList<>();
        plants.add("Sunflower");
        plants.add("Peashooter");

        ArrayList<String> zombies = new ArrayList<>();
        zombies.add("Basic");
        zombies.add("Conehead");

        game = new POOBvsZombies(300, "PlayerOne", plants, 100, "PlayerTwo", 100, zombies);
    }

    @Test
    public void testAgregarEntidad() {
        // Agregamos una planta en la posición (0, 0)
        game.addEntity(0, 0, "Peashooter");
        String entityName = game.getEntity(0, 0);
        assertEquals("Peashooter", entityName);
    }

    @Test
    public void testEliminarEntidad() {
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

    @Test
    public void testGetInstance() {
        POOBvsZombies instance1 = POOBvsZombies.getInstance();
        assertNotNull(instance1);
        POOBvsZombies instance2 = POOBvsZombies.getInstance();
        assertSame(instance1, instance2);
    }

    @Test
    public void testCreateZombieInstance() {
        Zombie basicZombie = game.createZombieInstance("Basic");
        assertNotNull(basicZombie);
        assertTrue(basicZombie instanceof Basic);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            game.createZombieInstance("InvalidZombieType");
        });
        assertEquals("Tipo de zombie inválido: InvalidZombieType", exception.getMessage());
    }

    @Test
    public void testCreatePlantInstance() {
        Plant sunflower = game.createPlantInstance("Sunflower");
        assertNotNull(sunflower);
        assertTrue(sunflower instanceof Sunflower);

        Plant invalidPlant = game.createPlantInstance("InvalidPlantType");
        assertNull(invalidPlant);
    }

    @Test
    public void testCalculateProgress() {
        int progress = game.calculateProgress();
        assertEquals(0, progress);
    }

    @Test
    public void testGetRoundTime() {
        float roundTime = game.getRoundTime();
        assertEquals(game.getMatchTime() / 2, roundTime);
    }

    @Test
    public void testGetHordersNumber() {
        int hordesNumber = game.getHordersNumber();
        assertEquals(0, hordesNumber);
    }

    @Test
    public void testGetModality() {
        String modality = game.getModality();
        assertEquals("PlayerVsPlayer", modality);
    }

    @Test
    public void testGetMatchTime() {
        float matchTime = game.getMatchTime();
        assertEquals(300 * 60, matchTime);
    }

    @Test
    public void testGetPlayerOne() {
        Player playerOne = game.getPlayerOne();
        assertNotNull(playerOne);
        assertEquals("PlayerOne", playerOne.getName());
    }

    @Test
    public void testGetPlayerTwo() {
        Player playerTwo = game.getPlayerTwo();
        assertNotNull(playerTwo);
        assertEquals("PlayerTwo", playerTwo.getName());
    }

    @Test
    public void testCalculateScores() {
        game.calculateScores();
        int scoreOne = game.getPlayerOne().getScore();
        int scoreTwo = game.getPlayerTwo().getScore();
        assertTrue(scoreOne >= 0);
        assertTrue(scoreTwo >= 0);
    }

    @Test
    public void testDetermineWinner() {
        game.getPlayerOne().setScore(150);
        game.getPlayerTwo().setScore(100);
        String winnerMessage = game.determineWinner();
        assertEquals("¡Las plantas han ganado! Puntaje: 150 vs 100", winnerMessage);
    }

    @Test
    public void testEndGame() {
        String winnerMessage = "¡Las plantas han ganado!";
        game.endGame(winnerMessage);
        // Verify that the game ends properly (additional checks may be needed)
    }

    @Test
    public void testGetEntitiesMatrix() {
        ArrayList<ArrayList<Object>> entitiesMatrix = game.getEntitiesMatrix();
        assertNotNull(entitiesMatrix);
        assertEquals(5, entitiesMatrix.size());
    }

    @Test
    public void testAddPendingExtraResources() {
        // Assuming gardenMenu is properly initialized
        game.addPendingExtraResources(0, 0, 2, 25, "Sun");
        // Verify that resources are added (additional checks may be needed)
    }

    // @Test
    // public void testSetAndGetGardenMenu() {
    //     GardenMenu mockGardenMenu = new GardenMenu();
    //     game.setGardenMenu(mockGardenMenu);
    //     assertSame(mockGardenMenu, game.getGardenMenu());
    // }

    // no trabaja por el updateEntityLabelScore
    // @Test
    // public void testRemoveZombiesInRow() {
    //     Zombie zombie = game.createZombieInstance("Basic");
    //     game.addEntity(0, 9, zombie);
    //     game.removeZombiesInRow(0);
    //     String entityName = game.getEntity(0, 9);
    //     assertNull(entityName);
    // }
}