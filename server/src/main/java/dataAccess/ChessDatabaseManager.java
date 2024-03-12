package dataAccess;

public class ChessDatabaseManager extends DatabaseManager{

    public static void chessDatabase() throws dataAccess.DataAccessException {
        DatabaseManager.createDatabase();
    }

}
