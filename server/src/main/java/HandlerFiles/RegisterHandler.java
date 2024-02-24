package HandlerFiles;

import Services.RegisterService;
import spark.Request;
import spark.Response;

public class RegisterHandler {

    private final RegisterService registerService;

    public RegisterHandler(RegisterService register) {
        this.registerService = register;
    }

    public String registerUser(Request r, Response q){
        //String username = r.body()
        //return this.registerService.register(username, email, password);
        return null;
    }

}
