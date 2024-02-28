package server;

import DataAccess.*;
import HandlerFiles.ClearHandler;
import HandlerFiles.LoginHandler;
import HandlerFiles.LogoutHandler;
import HandlerFiles.RegisterHandler;
import Services.ClearService;
import Services.LoginService;
import Services.LogoutService;
import Services.RegisterService;
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
        AuthDAO authDAO = new RAMAuthDAO();

        ClearService clearService = new ClearService(userDAO, gameDAO, authDAO);
        RegisterService registerService = new RegisterService(userDAO, authDAO);
        LoginService loginService = new LoginService(userDAO, authDAO);
        LogoutService logoutService = new LogoutService(authDAO);

        Spark.delete("/db", ((Request request, Response response) -> new ClearHandler(clearService).clearServer(request,response)));
        Spark.post("/user", ((Request request, Response response) -> new RegisterHandler(registerService).registerUser(request, response)));
        Spark.post("/session", ((Request request, Response response) -> new LoginHandler(loginService).loginService(request, response)));
        Spark.delete("/session",((Request request, Response response) -> new LogoutHandler(logoutService).logoutUser(request,response)));

        Spark.init();
        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
