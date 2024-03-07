package server;

import DataAccess.*;
import HandlerFiles.*;
import Services.*;
import spark.*;

public class Server {

    /**
     * I'm gonna be real honest, I have no idea how all of this works. Pray to the Machine Gods
     * and hope it doesn't break.
     * @param desiredPort
     * @return
     */
    public int run(int desiredPort) {
        Spark.port(desiredPort);
        //Database temp = new Database();
        Spark.staticFiles.location("web");
        // Register your endpoints and handle exceptions here.



        UserDAO userDAO = new RAMUserDAO();
        GameDAO gameDAO = new RAMGameDAO();
        AuthDAO authDAO;
        try {
            ChessDatabaseManager.chessDatabase();
            authDAO = new SQLAuthDAO();
            // userDAO
        } catch (dataAccess.DataAccessException e) {
            System.out.println("database failed to start, you dummy.");
            authDAO = new RAMAuthDAO();
        }

        ClearService clearService = new ClearService(userDAO, gameDAO, authDAO);
        RegisterService registerService = new RegisterService(userDAO, authDAO);
        LoginService loginService = new LoginService(userDAO, authDAO);
        LogoutService logoutService = new LogoutService(authDAO);
        ListGameService listGameService = new ListGameService(authDAO, gameDAO);
        CreateGameService createGameService = new CreateGameService(authDAO, gameDAO);
        JoinGameService joinGameService = new JoinGameService(authDAO, gameDAO);

        Spark.delete("/db", ((Request request, Response response) ->
                new ClearHandler(clearService).clearServer(request,response)));
        Spark.post("/user", ((Request request, Response response) ->
                new RegisterHandler(registerService).registerUser(request, response)));
        Spark.post("/session", ((Request request, Response response) ->
                new LoginHandler(loginService).loginService(request, response)));
        Spark.delete("/session",((Request request, Response response) ->
                new LogoutHandler(logoutService).logoutUser(request,response)));
        Spark.get("/game", ((Request request, Response response) ->
                new ListGamesHandler(listGameService).listHandler(request,response)));
        Spark.post("/game", ((Request request, Response response) ->
                new CreateGameHandler(createGameService).createHandler(request,response)));
        Spark.put("/game", ((Request request, Response response) ->
                new JoinGameHandler(joinGameService).joinHandler(request,response)));

        Spark.init();
        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
