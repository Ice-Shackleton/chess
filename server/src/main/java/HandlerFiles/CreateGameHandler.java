package HandlerFiles;

import dataAccess.BadAccessException;
import Services.CreateGameService;
import com.google.gson.Gson;
import model.*;
import spark.Request;
import spark.Response;

public class CreateGameHandler {

    private CreateGameService createGameService;

    public CreateGameHandler(CreateGameService createGameService) {
        this.createGameService = createGameService;
    }

    public Object createHandler(Request r, Response q){
        String authToken = r.headers("authorization");
        GameName gameName = new Gson().fromJson(r.body(), GameName.class);
        int gameID = -1;
        try{
            gameID = this.createGameService.createGame(authToken, gameName.gameName());
        } catch (dataAccess.DataAccessException wrongToken){
            q.status(401);
            return new Gson().toJson(new Message("Error: unauthorized"));
        } catch (BadAccessException noName) {
            q.status(400);
            return new Gson().toJson(new Message("Error: bad request"));
        }
        return new Gson().toJson(new IdResponse(Integer.toString(gameID)));
    }
}