package HandlerFiles;

import Services.RegisterService;
import com.google.gson.Gson;
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
        return new Gson().toJson(this.registerService.register(newRegister.username(), newRegister.email(), newRegister.password()));
    }

}
