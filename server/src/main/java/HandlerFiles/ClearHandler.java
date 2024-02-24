package HandlerFiles;


import DataAccess.UserDAO;
import Services.ClearService;
import spark.Request;
import spark.Response;

public class ClearHandler {
    private final ClearService clean;

    public ClearHandler(ClearService clean) {
        this.clean = clean;
    }

    public Object clearServer(Request r, Response q){
        //clear the map that represents the game data.
        //Call ClearService.
        //return q.status(200) if works.

        this.clean.clearAll();
        q.status(200);
        return "{}";
    }

}
