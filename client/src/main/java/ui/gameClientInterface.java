package ui;


import chess.ChessGame;
import com.google.gson.Gson;
import serverFacade.SocketFacade;
import webSocketMessages.SocketMessage;
import webSocketMessages.userCommands.JoinPlayerMessage;

public class gameClientInterface {

    private SocketFacade socket;
    private static final Gson gson = new Gson();

    /**
     * A gameClientInterface is a UI which the user interacts with in order to play a game of chess.
     * @param url the http address of the server.
     */
    public gameClientInterface(String url) {
        try {
            this.socket = new SocketFacade(url);
            JoinPlayerMessage thisMessage = new JoinPlayerMessage("authToken", 1, ChessGame.TeamColor.WHITE);
            String jsonStuff = new Gson().toJson(thisMessage);
            socket.send(jsonStuff);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /*public method

     */

}

//