package Services;

import DataAccess.AuthDAO;
import DataAccess.GameDAO;
import DataAccess.UserDAO;

public class ClearService {

    private UserDAO userDAO;
    private GameDAO gameDAO;
    private AuthDAO authDAO;

    /**
     * Initializes the ClearService object and gives it access to the DAOs. Note that each DAO
     * will in effect be a pointer reference to the database rather than a unique object.
     * @param userInput
     * @param gameInput
     * @param authInput
     */
    public ClearService(UserDAO userInput, GameDAO gameInput, AuthDAO authInput) {
        this.userDAO = userInput;
        this.gameDAO = gameInput;
        this.authDAO = authInput;
    }

    /**
     * This method resets all DAOs to a blank state where no data is stored. The
     * objects still exist after.
     */
    public void clearAll() throws dataAccess.DataAccessException {
        try {
            this.authDAO.clearAccess();
            this.userDAO.clearAccess();
            this.gameDAO.clearAccess();
        } catch (dataAccess.DataAccessException e){
            throw new dataAccess.DataAccessException("You somehow failed the most basic task");
        }
    }
}
