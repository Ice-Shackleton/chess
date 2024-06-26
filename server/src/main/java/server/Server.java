package server;

import dataAccess.*;
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

        UserDAO userDAO;
        GameDAO gameDAO;
        AuthDAO authDAO;
        try {
            ChessDatabaseManager.chessDatabase();
            gameDAO = SQLGameDAO.getInstance();
            authDAO = new SQLAuthDAO();
            userDAO = new SQLUserDAO();
        } catch (dataAccess.DataAccessException e) {
            System.out.println("database failed to start, you dummy.");
            gameDAO = new RAMGameDAO();
            authDAO = new RAMAuthDAO();
            userDAO = new RAMUserDAO();
        }

        WSServer socket = new WSServer(userDAO, gameDAO, authDAO);
        Spark.webSocket("/connect", socket);


        ClearService clearService = new ClearService(userDAO, gameDAO, authDAO);
        RegisterService registerService = new RegisterService(userDAO, authDAO);
        LoginService loginService = new LoginService(userDAO, authDAO);
        LogoutService logoutService = new LogoutService(authDAO);
        ListGameService listGameService = new ListGameService(authDAO, gameDAO);
        CreateGameService createGameService = new CreateGameService(authDAO, gameDAO);
        JoinGameService joinGameService = new JoinGameService(authDAO, gameDAO);

        Spark.delete("/db", ((Request request, Response response) ->
                new ClearHandler(clearService).clearServer(request,response, socket)));
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
