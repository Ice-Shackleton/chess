package clientTests;

import exception.ResponseException;
import model.*;
import org.junit.jupiter.api.*;
import server.Server;
import serverFacade.ServerMain;

import static org.junit.jupiter.api.Assertions.*;


public class ServerFacadeTests {

    static ServerMain main;
    private static Server server;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        main = new ServerMain("http://localhost:" + port);
        System.out.println("Started test HTTP server on " + port);
    }


    @BeforeEach
    void testingClear(){
        try {
            main.clearDB();
        } catch (ResponseException ex) {
            System.out.println(ex.getMessage());
        }
    }

    @AfterAll
    static void stopServer() {
        try {
            main.clearDB();
        } catch (ResponseException ex) {
            System.out.println(ex.getMessage());
        }
        server.stop();

    }


    @Test
    public void sampleTest() {
        Assertions.assertTrue(true);
    }

    @Test
    public void registerPositive() throws ResponseException {
        RegisterMessage newMessage = main.registerUser("validUser", "validEmail", "validPass");
        assertEquals("validUser", newMessage.username());
        assertNotNull(newMessage.authToken());
        assertThrows(ResponseException.class, ()-> main.registerUser(
                "validUser", "validEmail", "validPass"));
    }

    @Test
    public void registerNegative() throws ResponseException {
        assertThrows(ResponseException.class, ()-> main.registerUser(
                "validUser", null, null));
        assertThrows(ResponseException.class, ()-> main.registerUser(
                null, null, null));
        assertThrows(ResponseException.class, ()-> main.registerUser(
                null, "password", "email"));
    }

    @Test
    public void loginPositive() throws ResponseException {
        RegisterMessage temp = main.registerUser("validUser", "validEmail", "validPass");
        LoginMessage newMessage = main.loginUser("validUser", "validPass");
        assertEquals(newMessage.username(), "validUser");
        assertNotNull(newMessage.authToken());
    }

    @Test
    public void loginNegative() {
        assertThrows(ResponseException.class, ()-> main.loginUser(null, "password"));
        assertThrows(ResponseException.class, ()-> main.loginUser("username", null));
        assertThrows(ResponseException.class, ()-> main.loginUser(null, null));
    }

    @Test
    public void logoutPositive() throws ResponseException {
        RegisterMessage temp = main.registerUser("validUser", "validEmail", "validPass");
        LoginMessage newMessage = main.loginUser("validUser", "validPass");
        assertDoesNotThrow(()-> main.logoutUser(temp.authToken()));
    }

    @Test
    public void logoutNegative() throws ResponseException {
        RegisterMessage temp = main.registerUser("validUser", "validEmail", "validPass");
        LoginMessage newMessage = main.loginUser("validUser", "validPass");
        assertThrows(ResponseException.class, ()-> main.logoutUser("aaaaaaaaaaaa"));
    }

    @Test
    public void createGamePositive() throws ResponseException {
        RegisterMessage temp = main.registerUser("validUser", "validEmail", "validPass");
        LoginMessage newMessage = main.loginUser("validUser", "validPass");
        assertDoesNotThrow(()-> main.createGame(newMessage.authToken(), "gameName1"));
    }

    @Test
    public void createGameNegative() throws ResponseException {
        RegisterMessage temp = main.registerUser("validUser", "validEmail", "validPass");
        LoginMessage newMessage = main.loginUser("validUser", "validPass");
        assertThrows(ResponseException.class, ()-> main.createGame("newMessage.authToken()", "gameName1"));
    }

    @Test
    public void listGamesPositive() throws ResponseException {
        RegisterMessage temp = main.registerUser("validUser", "validEmail", "validPass");
        LoginMessage newMessage = main.loginUser("validUser", "validPass");
        main.createGame(newMessage.authToken(), "gameName1");
        assertDoesNotThrow(()-> main.listGamesUser(newMessage.authToken()));
        GameRecord gameList = main.listGamesUser(newMessage.authToken());
        assertNotNull(gameList);
    }

    @Test
    public void listGamesNegative() throws ResponseException {
        RegisterMessage temp = main.registerUser("validUser", "validEmail", "validPass");
        LoginMessage newMessage = main.loginUser("validUser", "validPass");
        main.createGame(newMessage.authToken(), "gameName1");
        assertThrows(ResponseException.class, ()-> main.listGamesUser("newMessage.authToken()"));
    }

    @Test
    public void joinGamePositive() throws ResponseException {
        RegisterMessage temp = main.registerUser("validUser1", "validEmail", "validPass");
        LoginMessage newMessage = main.loginUser("validUser1", "validPass");
        RegisterMessage temp2 = main.registerUser("validUser2", "validEmail", "validPass");
        LoginMessage newMessage2 = main.loginUser("validUser2", "validPass");
        RegisterMessage temp3 = main.registerUser("validUser3", "validEmail", "validPass");
        LoginMessage newMessage3 = main.loginUser("validUser3", "validPass");
        main.createGame(newMessage.authToken(), "gameName1");
        //First, we join a game as an observer.
        assertDoesNotThrow(()-> main.joinGameUser(newMessage.authToken(), null, 1));
        //Then, we join as colors.
        assertDoesNotThrow(()-> main.joinGameUser(newMessage2.authToken(), "WHITE", 1));
        assertDoesNotThrow(()-> main.joinGameUser(newMessage3.authToken(), "BLACK", 1));
    }

    @Test
    public void joinGameNegative() throws ResponseException {
        RegisterMessage temp = main.registerUser("validUser1", "validEmail", "validPass");
        LoginMessage newMessage = main.loginUser("validUser1", "validPass");
        main.createGame(newMessage.authToken(), "gameName1");
        //Let's attempt to join with a bad Id.
        assertThrows(ResponseException.class, ()-> main.joinGameUser(newMessage.authToken(), null, 4));
        //Attempting to join with a bad authToken.
        assertThrows(ResponseException.class, ()-> main.joinGameUser("newMessage.authToken()", null, 1));

        //Attempting to join an occupied spot.
        RegisterMessage temp2 = main.registerUser("validUser2", "validEmail", "validPass");
        LoginMessage newMessage2 = main.loginUser("validUser2", "validPass");
        Message join = main.joinGameUser(newMessage2.authToken(), "WHITE", 1);
        assertThrows(ResponseException.class, ()-> main.joinGameUser(newMessage.authToken(), "WHITE", 1));
    }

}

