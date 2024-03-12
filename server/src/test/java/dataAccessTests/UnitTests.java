package dataAccessTests;

import dataAccess.*;
import model.AuthData;
import org.junit.jupiter.api.*;

import java.sql.SQLException;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;


public class UnitTests {

    private static UserDAO userDAO;
    private static GameDAO gameDAO;
    private static AuthDAO authDAO;


    @BeforeAll
    public static void setup() throws DataAccessException {
        ChessDatabaseManager.chessDatabase();
        authDAO = new SQLAuthDAO();
        gameDAO = new SQLGameDAO();
        userDAO = new SQLUserDAO();
    }

    @BeforeEach
    public void initialize() throws DataAccessException {
        authDAO.clearAccess();
        userDAO.clearAccess();
        gameDAO.clearAccess();
    }

    @AfterEach
    public void tearDown() throws DataAccessException {
        authDAO.clearAccess();
        gameDAO.clearAccess();
        userDAO.clearAccess();
    }

    @Test
    public void testAuthDAOClear() {
        assertDoesNotThrow(()-> authDAO.clearAccess(), "clearing the auth table threw an error.");
    }

    @Test
    public void testGoodCreateAuth() {
        assertDoesNotThrow(()-> authDAO.createAuth("vaildUsername"),
                "threw an error despite entering a vaild username.");
    }

    @Test
    public void testBadCreateAuth() {
         assertThrows(DataAccessException.class, ()-> authDAO.createAuth(null),
                 "did not throw an error despite the username being null.");
    }

    @Test
    public void testGoodGetAuth() {
        String token;
        try {
            token = authDAO.createAuth("validUsername");
            assertEquals(AuthData.class, authDAO.getAuth(token).getClass());
            assertEquals(token, authDAO.getAuth(token).authToken());
        } catch (DataAccessException e) {
            System.out.println("You done foofed up, idiot.");
        }

    }

    @Test
    public void testBadGetAuth() {
        String token;
        try {
            token = "authDAO.createAuth(\"validUsername\")";
            assertNull(authDAO.getAuth(token));
        } catch (DataAccessException e) {
            System.out.println("You done foofed up, idiot.");
        }
    }

    @Test
    public void testGoodDeleteAuth() {
        try {
            String validToken = authDAO.createAuth("validUsername");
            authDAO.deleteAuth(validToken);
            assertNull(authDAO.getAuth(validToken));
        } catch (DataAccessException e){
            System.out.println("You done foofed up, idiot.");
        }
    }

    @Test
    public void testBadDeleteAuth() {
        try {
            String invalidToken = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa";
            authDAO.deleteAuth(invalidToken);
            assertNull(authDAO.getAuth(invalidToken));
        } catch (DataAccessException e){
            System.out.println("You done foofed up, idiot.");
        }
    }

    @Test
    public void testGameDAOClear() {
        assertDoesNotThrow(()-> gameDAO.clearAccess(), "clearing the game table threw an error.");
    }

    @Test
    public void testGoodGameList(){
        try {
            gameDAO.createGame("this game sucks");
            gameDAO.createGame("this game smacks");
            gameDAO.createGame("I LOVE YELLING");
            assertDoesNotThrow(() -> gameDAO.getGameList());
            assertEquals(3, gameDAO.getGameList().size());

        } catch (DataAccessException e) {
            System.out.println("You done foofed up, idiot.");
        }

    }

    @Test
    public void testGoodColorOccupation(){
        try {
            gameDAO.createGame("whiteUsernameTaken");
            gameDAO.joinGame("WHITE", 1, "validUsername");
            assertTrue(gameDAO.colorOccupied("WHITE", 1), "expected WHITE to be occupied but it was false.");
        } catch (DataAccessException | BadAccessException | SQLException e) {
            System.out.println("You done foofed up, idiot.");
        }
    }

    @Test
    public void testBadColorOccupation(){
        try {
            gameDAO.createGame("whiteUsernameTaken");
            gameDAO.joinGame("WHITE", 1, "validUsername");
            assertFalse(gameDAO.colorOccupied("BLACK", 1),
                    "Expected BLACK to be unoccupied but it returned true.");
        } catch (DataAccessException | BadAccessException | SQLException e) {
            System.out.println("You done foofed up, idiot.");
        }
    }

    @Test
    public void testGoodGameCreation() {
        assertDoesNotThrow(()-> gameDAO.createGame("valid game"));
    }

    @Test
    public void testGoodJoinGame() {
        try {
            gameDAO.createGame("valid game");
            assertTrue(gameDAO.joinGame(null, 1, "observer"));
            assertTrue(gameDAO.joinGame("WHITE", 1, "valid guy"));
            assertTrue(gameDAO.joinGame("BLACK", 1, "valid girl"));
            assertFalse(gameDAO.joinGame("WHITE", 1, "idiot"));
        } catch (DataAccessException e) {
            System.out.println("You done foofed up, idiot.");
        }
    }


    @Test
    public void testBadJoinGame(){
        try {
            gameDAO.createGame("valid game");
            assertThrows(DataAccessException.class, ()-> gameDAO.joinGame("white", 1, "a dummy"));
        } catch (DataAccessException e) {
            System.out.println("You done foofed up, idiot.");
        }
    }

    @Test
    public void testUserDAOClear() {
        assertDoesNotThrow(()-> userDAO.clearAccess(), "clearing the user table threw an error.");
    }

    @Test
    public void testGoodGetUser() {
        try {
            userDAO.createUser("validUser", "null", "null");
            assertDoesNotThrow(()-> userDAO.getUser("validUser"));
            assertNull(userDAO.getUser("invalidUser"));
        } catch (DataAccessException e) {
            System.out.println("You done foofed up, idiot.");
        }
    }

    @Test
    public void testBadGetUser() {
        try {
            assertNull(userDAO.getUser("nonExtantUser"));
        } catch (DataAccessException e) {
            System.out.println("You done foofed up, idiot.");
        }
    }

    @Test
    public void testGoodCreateUser() {
        assertDoesNotThrow(() -> userDAO.createUser("asdf", "asdf", "asdf"));
    }

    @Test
    public void testGoodPassword() {
        try {
            userDAO.createUser("validUser", "email@mail.com", "password");
            assertTrue(userDAO.checkPassword("validUser", "password"));
        } catch (DataAccessException e) {
            System.out.println("You done foofed up, idiot.");
        }
    }

    @Test
    public void testBadPassword() {
        try {
            userDAO.createUser("validUser", "email@mail.com", "password");
            assertFalse(userDAO.checkPassword("validUser", "notPassword"));
        } catch (DataAccessException e) {
            System.out.println("You done foofed up, idiot.");
        }
    }
}
