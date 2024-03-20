package clientTests;

import exception.ResponseException;
import model.AuthStorage;
import model.LoginMessage;
import model.RegisterMessage;
import model.UserData;
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

}
