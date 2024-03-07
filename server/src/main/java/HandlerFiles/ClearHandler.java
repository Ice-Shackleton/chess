package HandlerFiles;


import Services.ClearService;
import com.google.gson.Gson;
import model.Message;
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
        try {
            this.clean.clearAll();
        } catch (dataAccess.DataAccessException e){
            q.status(500);
            return new Gson().toJson(new Message("Error: You done @#$%%! up, A-aron.."));
        }
        q.status(200);
        return new Gson().toJson(new Message("{}"));
    }

}