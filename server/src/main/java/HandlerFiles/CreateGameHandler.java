package HandlerFiles;

import DataAccess.BadAccessException;
import Services.CreateGameService;
import com.google.gson.Gson;
import model.Message;
import model.UserData;
import spark.Request;
import spark.Response;

public class CreateGameHandler {

    private CreateGameService createGameService;

    public CreateGameHandler(CreateGameService createGameService) {
        this.createGameService = createGameService;
    }

    public Object createHandler(Request r, Response q){
        String authToken = r.headers("authorization");
        String gameName = new Gson().fromJson(r.body(), String.class);
        Integer gameID = -1;
        try{
            this.createGameService.CreateGame(authToken, gameName);
        } catch (dataAccess.DataAccessException wrongToken){
            q.status(401);
            return new Gson().toJson(new Message("Error: unauthorized"));
        } catch (BadAccessException noName) {
            q.status(400);
            return new Gson().toJson(new Message("Error: bad request"));
        }
        return new Gson().toJson(new Message(gameID.toString()));
    }
}
