package HandlerFiles;

import DataAccess.IncorrectException;
import Services.LoginService;
import com.google.gson.Gson;
import model.Message;
import spark.Request;
import spark.Response;

public class LoginHandler {
    private final LoginService login;

    public LoginHandler(LoginService login) {
        this.login = login;
    }

    public Object loginService(Request r, Response q){
        LoginData stuff = new Gson().fromJson(r.body(), LoginData.class);
        String loginAttempt = null;
        try {
            loginAttempt = this.login.login(stuff.username(), stuff.password());
        } catch(dataAccess.DataAccessException user) {
            q.status(401);
            return new Gson().toJson(new Message("Error: username does not exist."));
        } catch(IncorrectException wrongPassword) {
            q.status(401);
            return new Gson().toJson(new Message("Error: unauthorized"));
        }
        q.status(200);
        return new Gson().toJson(new LoginMessage(stuff.username(), loginAttempt));
    }
}

record LoginData(String username, String password){}
record LoginMessage(String username, String authToken){}
