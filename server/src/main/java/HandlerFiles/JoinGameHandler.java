package HandlerFiles;

import DataAccess.BadAccessException;
import DataAccess.IncorrectException;
import Services.JoinGameService;
import com.google.gson.Gson;
import model.Message;
import spark.Request;
import spark.Response;

public class JoinGameHandler {

    private final JoinGameService joinGameService;

    public JoinGameHandler(JoinGameService joinGameService) {
        this.joinGameService = joinGameService;
    }

    public Object joinHandler(Request r, Response q){
        String authToken = r.headers("authorization");
        ColorStorage dataStuff = new Gson().fromJson(r.body(), ColorStorage.class);

        try {
            this.joinGameService.joinGame(authToken, dataStuff.playerColor(), dataStuff.gameID());
        } catch (dataAccess.DataAccessException badAuth){
            q.status(401);
            return new Gson().toJson(new Message("Error: unauthorized"));
        } catch (BadAccessException nonsense) {
            q.status(400);
            return new Gson().toJson(new Message("Error: bad request"));
        } catch (IncorrectException badJoin) {
            q.status(403);
            return new Gson().toJson(new Message("Error: already taken"));
        }
        q.status(200);
        return new Gson().toJson(new Message("{}"));
    }
}

record ColorStorage(String playerColor, int gameID){}
