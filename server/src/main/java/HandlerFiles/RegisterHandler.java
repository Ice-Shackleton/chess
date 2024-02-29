package HandlerFiles;

import DataAccess.BadAccessException;
import Services.RegisterService;
import com.google.gson.Gson;
import model.Message;
import model.UserData;
import spark.Request;
import spark.Response;

public class RegisterHandler {

    private final RegisterService registerService;

    public RegisterHandler(RegisterService register) {
        this.registerService = register;
    }

    public String registerUser(Request r, Response q) throws dataAccess.DataAccessException {
        UserData newRegister = new Gson().fromJson(r.body(), UserData.class);
        String temp = null;
        try {
            temp = this.registerService.register(newRegister.username(), newRegister.email(), newRegister.password());
        } catch (dataAccess.DataAccessException userExists) {
            q.status(403);
            return new Gson().toJson(new Message("Error: already taken"));
        } catch (BadAccessException noInfo){
            q.status(400);
            return new Gson().toJson(new Message("Error: bad request"));
        }
        q.status(200);
        return new Gson().toJson(new RegisterMessage(newRegister.username(), temp));
    }

}

record RegisterMessage(String username, String authToken){}
