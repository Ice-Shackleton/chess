package passoffTests.serverTests;

import DataAccess.*;
import chess.ChessGame;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;


public class UnitTests {

    private UserDAO userDAO;
    private GameDAO gameDAO;
    private AuthDAO authDAO;


    @BeforeEach
    public void setup() throws dataAccess.DataAccessException {
        authDAO = new SQLAuthDAO();
        gameDAO = new SQLGameDAO();
        userDAO = new SQLUserDAO();
    }

    @AfterEach
    public void tearDown() {

    }

    @Test
    public void testClear(){

        //assertDoesNotThrow(() -> clearService. );
    }
}
