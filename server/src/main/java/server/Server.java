package server;

import DataAccess.*;
import HandlerFiles.ClearHandler;
import Services.ClearService;
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

        Spark.delete("/db", ((Request request, Response response) -> new ClearHandler(clearService).clearServer(request,response)));


        Spark.init();
        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
