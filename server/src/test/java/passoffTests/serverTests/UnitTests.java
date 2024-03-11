package passoffTests.serverTests;

import DataAccess.*;
import chess.ChessGame;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;


public class UnitTests {

    private static UserDAO userDAO;
    private static GameDAO gameDAO;
    private static AuthDAO authDAO;


    @BeforeAll
    public static void setup() throws dataAccess.DataAccessException {
        ChessDatabaseManager.chessDatabase();
        authDAO = new SQLAuthDAO();
        gameDAO = new SQLGameDAO();
        userDAO = new SQLUserDAO();
    }

    @BeforeEach
    public void initialize() throws dataAccess.DataAccessException {
        authDAO.clearAccess();
        userDAO.clearAccess();
        gameDAO.clearAccess();
    }

    @AfterEach
    public void tearDown() throws dataAccess.DataAccessException {
        authDAO.clearAccess();
        gameDAO.clearAccess();
        userDAO.clearAccess();
    }

    @Test
    public void testAuthDAOClear() {
        assertDoesNotThrow(()-> authDAO.clearAccess(), "clearing the auth table threw an error.");
    }
}
