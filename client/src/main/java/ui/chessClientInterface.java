package ui;

import chess.ChessBoard;
import exception.ResponseException;
import model.*;
import serverFacade.ServerMain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Scanner;
import static ui.EscapeSequences.*;

public class chessClientInterface {

    private final ServerMain serverFacade;
    private boolean loginStatus;
    private String authToken = "";
    private HashMap<Integer, GameData> currentGameList = null;

    public chessClientInterface(String url) {

        this.serverFacade = new ServerMain(url);
        this.loginStatus = false;
        Scanner scanner = new Scanner(System.in);
        var result = "";
        while (!result.equals("quit")) {
            printPrompt();
            String line = scanner.nextLine();
            try {
                result = this.eval(line.toLowerCase(), scanner);
                System.out.print(SET_TEXT_COLOR_LIGHT_GREY +  result);
            } catch (Exception e) {
                var msg = e.toString();
                System.out.print(msg);
            }
        }
        System.out.println();
    }

    /**
     * This method is used to abstract the functionabilty of the serverFacade when the client inputs commands.
     * For example, when the client tries to register a user, it should not know the http requirements to access
     * the database. This method will handle all such needs.
     * If the input is invalid, nothing will be returned.
     * @param input the user's command, taken from the terminal.
     * @return
     */
    private String eval(String input, Scanner lineReader) throws ResponseException {

        if(this.loginStatus) {
            switch (input) {
                case "logout" -> {
                    AuthStorage temp = this.serverFacade.logoutUser(this.authToken);
                    this.loginStatus = false;
                    return "logged out.";
                }
                case "help" -> {
                    System.out.println(SET_TEXT_COLOR_MAGENTA + "  logout" +
                            SET_TEXT_COLOR_LIGHT_GREY + " - to exit this session");
                    System.out.println(SET_TEXT_COLOR_MAGENTA + "  create game" +
                            SET_TEXT_COLOR_LIGHT_GREY + " - to start a new game of chess");
                    System.out.println(SET_TEXT_COLOR_MAGENTA + "  list games" +
                            SET_TEXT_COLOR_LIGHT_GREY + " - to see active games");
                    System.out.println(SET_TEXT_COLOR_MAGENTA + "  join game" +
                            SET_TEXT_COLOR_LIGHT_GREY + " - to join an active game");
                    System.out.println(SET_TEXT_COLOR_MAGENTA + "  join as observer" +
                            SET_TEXT_COLOR_LIGHT_GREY + " - to observe an active game");
                    System.out.println(SET_TEXT_COLOR_MAGENTA + "  display" +
                            SET_TEXT_COLOR_LIGHT_GREY + " - to visualize a specific game");
                    return (SET_TEXT_COLOR_MAGENTA + "  help" +
                            SET_TEXT_COLOR_LIGHT_GREY + " - with possible commands");
                }
                case "create game" -> {
                    System.out.print(SET_TEXT_COLOR_LIGHT_GREY + "\t[GAME_NAME] >>> ");
                    String gameName = lineReader.nextLine();
                    IdResponse temp = this.serverFacade.createGame(this.authToken, gameName);
                    GameRecord games = this.serverFacade.listGamesUser(this.authToken);
                    return "game created successfully.";
                }
                case "list games" -> {
                    GameRecord games = this.serverFacade.listGamesUser(this.authToken);
                    this.currentGameList = new HashMap<>();
                    int i=1;
                    for (GameData game: games.games()){
                        System.out.print(SET_TEXT_COLOR_LIGHT_GREY + i + ". ");
                        System.out.print(SET_TEXT_COLOR_MAGENTA + game.gameName());
                        if (game.blackUsername() == null && game.whiteUsername() == null){
                            System.out.print(SET_TEXT_COLOR_LIGHT_GREY + " currently has no players.\n");
                        } else {

                            if (game.whiteUsername() == null){
                                System.out.print(SET_TEXT_COLOR_LIGHT_GREY + ", WHITE is unoccupied, " );
                            } else {
                                System.out.print(SET_TEXT_COLOR_LIGHT_GREY + ", WHITE is occupied by "
                                        + game.whiteUsername() + ", ");
                            }

                            if (game.blackUsername() == null){
                                System.out.print(SET_TEXT_COLOR_LIGHT_GREY + "BLACK is unoccupied.\n");
                            } else {
                                System.out.print(SET_TEXT_COLOR_LIGHT_GREY + "BLACK is occupied by "
                                        + game.blackUsername() + ".\n");
                            }

                        }
                        this.currentGameList.put(i, game);
                        i++;
                    }
                    return "\nlist complete.";
                }
                case "join game" -> {
                    if(this.currentGameList == null){
                        return (SET_TEXT_COLOR_MAGENTA + "You need to list games first.");
                    }
                    System.out.print(SET_TEXT_COLOR_LIGHT_GREY + "\t[GAME_ID] >>> ");
                    String gameID = lineReader.nextLine();
                    System.out.print(SET_TEXT_COLOR_LIGHT_GREY + "\t[DESIRED_COLOR] >>> ");
                    String playerColor = lineReader.nextLine();
                    Message temp = this.serverFacade.joinGameUser(this.authToken,
                            playerColor.toUpperCase(), this.currentGameList.get(Integer.parseInt(gameID)).gameID());
                    if (this.currentGameList == null) {
                        return "game joined successfully.";
                    }
                    ChessBoard board = this.currentGameList.get(Integer.parseInt(gameID)).game().getBoard();
                    return board.toString() + "\n\n" + board.oppositePerspective();
                }
                case "join as observer" -> {
                    if(this.currentGameList == null){
                        return (SET_TEXT_COLOR_MAGENTA + "you need to list games first.");
                    }
                    System.out.print(SET_TEXT_COLOR_LIGHT_GREY + "\t[GAME_ID] >>> ");
                    String gameID = lineReader.nextLine();
                    Message temp = this.serverFacade.joinGameUser(this.authToken,
                            null, currentGameList.get(Integer.parseInt(gameID)).gameID());
                    if (this.currentGameList == null) {
                        return "game observed successfully.";
                    }
                    ChessBoard board = this.currentGameList.get(Integer.parseInt(gameID)).game().getBoard();
                    return board.toString() + "\n\n" + board.oppositePerspective();
                }
                default -> {
                    return "invalid command.";
                }
            }

        } else {

            switch (input) {
                case "register" -> {
                    System.out.print(SET_TEXT_COLOR_LIGHT_GREY + "\t[USERNAME] >>> ");
                    String username = lineReader.nextLine();
                    System.out.print(SET_TEXT_COLOR_LIGHT_GREY + "\t[PASSWORD] >>> ");
                    String password = lineReader.nextLine();
                    System.out.print(SET_TEXT_COLOR_LIGHT_GREY + "\t[EMAIL] >>> ");
                    String email = lineReader.nextLine();
                    RegisterMessage temp = this.serverFacade.registerUser(username, email, password);
                    this.loginStatus = true;
                    this.authToken = temp.authToken();
                    return "registered.";
                }
                case "login" -> {
                    System.out.print(SET_TEXT_COLOR_LIGHT_GREY + "\t[USERNAME] >>> ");
                    String username = lineReader.nextLine();
                    System.out.print(SET_TEXT_COLOR_LIGHT_GREY + "\t[PASSWORD] >>> ");
                    String password = lineReader.nextLine();
                    LoginMessage temp = this.serverFacade.loginUser(username, password);
                    this.loginStatus = true;
                    this.authToken = temp.authToken();
                    return "logged in successfully.";
                }
                case "help" -> {
                    System.out.println(SET_TEXT_COLOR_MAGENTA + "  register" +
                            SET_TEXT_COLOR_LIGHT_GREY + " - to create an account");
                    System.out.println(SET_TEXT_COLOR_MAGENTA + "  login" +
                            SET_TEXT_COLOR_LIGHT_GREY + " - to play chess");
                    System.out.println(SET_TEXT_COLOR_MAGENTA + "  quit" +
                            SET_TEXT_COLOR_LIGHT_GREY + " - playing chess");
                    return (SET_TEXT_COLOR_MAGENTA + "  help" +
                            SET_TEXT_COLOR_LIGHT_GREY + " - with possible commands");
                }
                default -> {
                    return "invalid command.";
                }
            }
        }
    }

    private void printPrompt() {
        if (this.loginStatus){
            System.out.print(SET_TEXT_COLOR_LIGHT_GREY + "\n[LOGGED_IN] >>> ");
        } else {
            System.out.print(SET_TEXT_COLOR_LIGHT_GREY + "\n[LOGGED_OUT] >>> ");
        }
    }






}
