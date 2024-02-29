package HandlerFiles;

import Services.LogoutService;
import com.google.gson.Gson;
import model.Message;
import spark.Request;
import spark.Response;

import java.io.Reader;

public class LogoutHandler {

    private final LogoutService logoutService;

    public LogoutHandler(LogoutService logoutService) {
        this.logoutService = logoutService;
    }

    public Object logoutUser(Request r, Response q){
        String token = r.headers("authorization");
        try {
            this.logoutService.logout(token);
        } catch (dataAccess.DataAccessException noAuthToken){
            q.status(401);
            return new Gson().toJson(new Message("Error: unauthorized"));
        }
        q.status(200);
        return new Gson().toJson(new Message("{}"));
    }
}

record AuthStorage(String authorization){}

