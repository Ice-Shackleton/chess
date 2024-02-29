package HandlerFiles;

import Services.ListGameService;
import com.google.gson.Gson;
import model.GameData;
import model.Message;
import spark.Request;
import spark.Response;

import java.util.ArrayList;
import java.util.List;

public class ListGamesHandler {
    private final ListGameService listGameService;

    public ListGamesHandler(ListGameService listGameService) {
        this.listGameService = listGameService;
    }

    public Object listService(Request r, Response q){
        String token = r.headers("authorization");
        ArrayList<GameData> gameStuff = null;
        try {
            gameStuff = this.listGameService.listGames(token);
        } catch (dataAccess.DataAccessException authorization){
            q.status(401);
            return new Gson().toJson(new Message("error: unauthorized"));
        }
        q.status(200);
        if (gameStuff == null){
            return new Gson().toJson("{}");
        }
        ArrayList<gameInfo> games = new ArrayList<>();
        for (int i=0; i < gameStuff.size(); i++){
            GameData single = (GameData) gameStuff.get(i);
            games.add(new gameInfo(single.gameID(), single.whiteUsername(), single.blackUsername(),
                    single.game().getGameName()));
        }
        return new Gson().toJson(games);
    }
}

record gameInfo(int gameID, String whiteUsername, String blackUsername, String gameName){}

