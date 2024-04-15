package ui;

import chess.*;
import com.google.gson.Gson;
import exception.ResponseException;
import model.*;
import serverFacade.ServerMain;
import serverFacade.SocketFacade;
import webSocketMessages.userCommands.*;

import java.util.HashMap;
import java.util.Scanner;
import static ui.EscapeSequences.*;

public class ChessClientInterface {

    private final ServerMain serverFacade;
    private final SocketFacade socketFacade;
    private boolean loginStatus;
    private boolean gameStatus;
    //this next variables will help keep track of a user's current game.
    private Integer currentGameLoaded;
    private String authToken = "";
    private HashMap<Integer, GameData> currentGameList = null;
    private Gson gson = new Gson();

    public ChessClientInterface(String url) throws Exception {
            this.serverFacade = new ServerMain(url);
            this.socketFacade  = new SocketFacade(url);
            this.loginStatus = false;
            this.gameStatus = false;
            this.currentGameLoaded = -1;
            Scanner scanner = new Scanner(System.in);
            var result = "";
            while (!result.equals("quit")) {
                printPrompt();
                String line = scanner.nextLine();
                try {
                    result = this.eval(line.toLowerCase(), scanner);
                    System.out.print(SET_TEXT_COLOR_LIGHT_GREY + result);
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
    private String eval(String input, Scanner lineReader) throws Exception {
        //this line is used to check if the user has their websocket open.
        if (!this.gameStatus) {
            //phase 5 stuff.
            if (this.loginStatus) {
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
                                SET_TEXT_COLOR_LIGHT_GREY + " - to rejoin a specific game");
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
                        int i = 1;
                        for (GameData game : games.games()) {
                            System.out.print(SET_TEXT_COLOR_LIGHT_GREY + i + ". ");
                            System.out.print(SET_TEXT_COLOR_MAGENTA + game.gameName());
                            if (game.blackUsername() == null && game.whiteUsername() == null) {
                                System.out.print(SET_TEXT_COLOR_LIGHT_GREY + " currently has no players.\n");
                            } else {

                                if (game.whiteUsername() == null) {
                                    System.out.print(SET_TEXT_COLOR_LIGHT_GREY + ", WHITE is unoccupied, ");
                                } else {
                                    System.out.print(SET_TEXT_COLOR_LIGHT_GREY + ", WHITE is occupied by "
                                            + game.whiteUsername() + ", ");
                                }

                                if (game.blackUsername() == null) {
                                    System.out.print(SET_TEXT_COLOR_LIGHT_GREY + "BLACK is unoccupied.\n");
                                } else {
                                    System.out.print(SET_TEXT_COLOR_LIGHT_GREY + "BLACK is occupied by "
                                            + game.blackUsername() + ".\n");
                                }

                            }
                            this.currentGameList.put(i, game);
                            i++;
                        }
                        return "list complete.";
                    }
                    /**
                     * This method is smarter than it looks. In essence, when a user opens a client to join a game, it
                     * will handle the logic as follows:
                     * 1. Is the color slot of the game open? If yes, then perform the http request to add user to database.
                     * If no, then http request is not sent.
                     * 2. Is the user already in the appropriate color slot? If yes, then launch gameUI with a join message.
                     */
                    case "join game" -> {
                        if (this.currentGameList == null) {
                            return (SET_TEXT_COLOR_MAGENTA + "You need to list games first.");
                        }
                        System.out.print(SET_TEXT_COLOR_LIGHT_GREY + "\t[GAME_ID] >>> ");
                        String gameID = lineReader.nextLine();
                        System.out.print(SET_TEXT_COLOR_LIGHT_GREY + "\t[DESIRED_COLOR] >>> ");
                        String playerColor = lineReader.nextLine();

                        boolean badEntry = true;
                        ChessGame.TeamColor color = ChessGame.TeamColor.WHITE;

                        if(playerColor.equalsIgnoreCase("white")) {
                            badEntry = false;
                            color = ChessGame.TeamColor.WHITE;
                        }
                        if (playerColor.equalsIgnoreCase("black")) {
                            badEntry = false;
                            color = ChessGame.TeamColor.BLACK;
                        }

                        while (badEntry) {
                            System.out.println(SET_TEXT_COLOR_MAGENTA + "invalid color; enter 'white' or 'black' only.");
                            System.out.print(SET_TEXT_COLOR_LIGHT_GREY + "\t[DESIRED_COLOR] >>> ");
                            playerColor = lineReader.nextLine();
                            if(playerColor.equalsIgnoreCase("white")) {
                                badEntry = false;
                                color = ChessGame.TeamColor.WHITE;
                            }
                            if (playerColor.equalsIgnoreCase("black")) {
                                badEntry = false;
                                color = ChessGame.TeamColor.BLACK;
                            }
                        }

                        Message temp = this.serverFacade.joinGameUser(this.authToken,
                                playerColor.toUpperCase(), this.currentGameList.get(Integer.parseInt(gameID)).gameID());
                        if (this.currentGameList == null) {
                            return "game joined successfully.";
                        }

                        JoinPlayerMessage request = new JoinPlayerMessage(this.authToken,
                                this.currentGameList.get(Integer.parseInt(gameID)).gameID(), color);
                        this.socketFacade.send(gson.toJson(request));
                        this.gameStatus = true;
                        this.currentGameLoaded = this.currentGameList.get(Integer.parseInt(gameID)).gameID();

                        //ChessBoard board = this.currentGameList.get(Integer.parseInt(gameID)).game().getBoard();
                        //return board.toString() + "\n\n" + board.oppositePerspective();
                        return "";
                    }
                    case "join as observer" -> {
                        if (this.currentGameList == null) {
                            return (SET_TEXT_COLOR_MAGENTA + "you need to list games first.");
                        }
                        System.out.print(SET_TEXT_COLOR_LIGHT_GREY + "\t[GAME_ID] >>> ");
                        String gameID = lineReader.nextLine();
                        Message temp = this.serverFacade.joinGameUser(this.authToken,
                                null, currentGameList.get(Integer.parseInt(gameID)).gameID());
                        if (this.currentGameList == null) {
                            return "you need to list games to observe them.";
                        }

                        JoinObserverMessage request = new JoinObserverMessage(this.authToken,
                                this.currentGameList.get(Integer.parseInt(gameID)).gameID());
                        this.socketFacade.send(gson.toJson(request));
                        this.gameStatus = true;
                        this.currentGameLoaded = this.currentGameList.get(Integer.parseInt(gameID)).gameID();

                        //ChessBoard board = this.currentGameList.get(Integer.parseInt(gameID)).game().getBoard();
                        //return board.toString() + "\n\n" + board.oppositePerspective();
                        return "";
                    }
                    case "display" -> {
                        System.out.print(SET_TEXT_COLOR_LIGHT_GREY + "\t[GAME_ID] >>> ");
                        String gameID = lineReader.nextLine();
                        JoinObserverMessage request = new JoinObserverMessage(this.authToken,
                                this.currentGameList.get(Integer.parseInt(gameID)).gameID());
                        this.socketFacade.send(gson.toJson(request));
                        this.gameStatus = true;
                        this.currentGameLoaded = this.currentGameList.get(Integer.parseInt(gameID)).gameID();
                        return "";
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

            
        } else {
            switch (input) {
                case "help" -> {
                    System.out.println(SET_TEXT_COLOR_MAGENTA + "  redraw" +
                            SET_TEXT_COLOR_LIGHT_GREY + " - to update your visual of the current game");
                    System.out.println(SET_TEXT_COLOR_MAGENTA + "  leave" +
                            SET_TEXT_COLOR_LIGHT_GREY + " - to exit the game UI");
                    System.out.println(SET_TEXT_COLOR_MAGENTA + "  make move" +
                            SET_TEXT_COLOR_LIGHT_GREY + " - to make a legal move with a piece");
                    System.out.println(SET_TEXT_COLOR_MAGENTA + "  highlight" +
                            SET_TEXT_COLOR_LIGHT_GREY + " - to all a piece's legal moves");
                    System.out.println(SET_TEXT_COLOR_MAGENTA + "  resign" +
                            SET_TEXT_COLOR_LIGHT_GREY + " - to forfeit your active game and quit to the login UI");
                    return (SET_TEXT_COLOR_MAGENTA + "  help" +
                            SET_TEXT_COLOR_LIGHT_GREY + " - with possible commands");
                }
                case "redraw" -> {
                    JoinObserverMessage request = new JoinObserverMessage(this.authToken, this.currentGameLoaded);
                    this.socketFacade.send(gson.toJson(request));
                }
                case "leave" -> {
                    this.gameStatus = false;
                    LeaveMessage request = new LeaveMessage(this.authToken, this.currentGameLoaded);
                    socketFacade.send(gson.toJson(request));
                    return "";
                }
                case "make move" -> {
                    GameData game = getExactGame(this.currentGameLoaded);
                    System.out.print(SET_TEXT_COLOR_LIGHT_GREY + "\t[START_POSITION] >>> ");
                    String start = lineReader.nextLine();
                    while (start.length() != 2) {
                        System.out.print(SET_TEXT_COLOR_MAGENTA + "invalid entry. remember to enter your start in chess notation," +
                                "as in 'e4' or 'a7'. The letter represents the row, and the number the column."
                                + SET_TEXT_COLOR_LIGHT_GREY + "\n\t[START_POSITION] >>> ");
                        start = lineReader.nextLine();
                    }
                    System.out.print(SET_TEXT_COLOR_LIGHT_GREY + "\t[END_POSITION] >>> ");
                    String end = lineReader.nextLine();
                    while (end.length() != 2) {
                        System.out.print(SET_TEXT_COLOR_MAGENTA + "invalid entry. remember to enter your start in chess notation," +
                                "as in 'e4' or 'a7'. The letter represents the row, and the number the column."
                                + SET_TEXT_COLOR_LIGHT_GREY + "\n\t[END_POSITION] >>> ");
                        end = lineReader.nextLine();
                    }

                    ChessPosition startPos = convertNotation(start);
                    ChessPosition endPos = convertNotation(end);
                    ChessPiece.PieceType type = null;
                    if (endPos.getRow() == 1 || endPos.getRow() == 8) {
                        System.out.print(SET_TEXT_COLOR_MAGENTA +
                                "please enter what piece type this move should be promoted to.\n");
                        System.out.print(SET_TEXT_COLOR_LIGHT_GREY + "\t[PIECE_TYPE] >>> ");
                        String promotion = lineReader.nextLine();
                        type = promotionTranslation(promotion);
                        while (type == null) {
                            System.out.print(SET_TEXT_COLOR_MAGENTA + "invalid entry. remember, valid promotion pieces are as follows:" +
                                    " 'knight', 'bishop', 'rook', and 'queen'.\n" +
                                    "please enter what piece type this move should be promoted to.\n");
                            System.out.print(SET_TEXT_COLOR_LIGHT_GREY + "\t[PIECE_TYPE] >>> ");
                            promotion = lineReader.nextLine();
                            type = promotionTranslation(promotion);
                        }
                    }

                    if (startPos == null) {
                        System.out.print(SET_TEXT_COLOR_MAGENTA + "invalid start position. returning to game UI.");
                        break;
                    } else if (endPos == null) {
                        System.out.print(SET_TEXT_COLOR_MAGENTA + "invalid end position. returning to game UI.");
                        break;
                    }

                    ChessMove move = new ChessMove(startPos, endPos, type);
                    MakeMoveMessage moveMessage = new MakeMoveMessage(this.authToken, this.currentGameLoaded, move);
                    this.socketFacade.send(gson.toJson(moveMessage));
                    return "";
                }
                case "resign" -> {
                    this.gameStatus = false;
                    ResignMessage request = new ResignMessage(this.authToken, this.currentGameLoaded);
                    this.socketFacade.send(gson.toJson(request));
                    return "";
                }
                case "highlight" -> {
                    return "no, screw you.";
                }
                default -> {
                    return "invalid command.";
                }
            }
        }
        return "";
    }

    private void printPrompt() {
        if (!this.gameStatus) {
            if (this.loginStatus) {
                System.out.print(SET_TEXT_COLOR_LIGHT_GREY + "\n[LOGGED_IN] >>> ");
            } else {
                System.out.print(SET_TEXT_COLOR_LIGHT_GREY + "\n[LOGGED_OUT] >>> ");
            }
        } else {
            System.out.print(SET_TEXT_COLOR_LIGHT_GREY + "\n[GAME] >>> ");
        }
    }

    private GameData getExactGame(Integer gameID) throws ResponseException {
        GameRecord games = this.serverFacade.listGamesUser(this.authToken);
        GameData theThing = null;
        for (GameData game : games.games()) {
            if (this.currentGameLoaded.equals(game.gameID())) {
                theThing = game;
                break;
            }
        }
        return theThing;
    }

    private ChessPosition convertNotation(String postion) {
        try {
            if (postion.length() != 2) {
                return null;
            }
            char row = postion.charAt(0);
            Integer col = Integer.parseInt(String.valueOf(postion.charAt(1)));
            int realRow = 9 - (Character.getNumericValue(row) - 9);
            return new ChessPosition(col, realRow);
        } catch (Exception e) {
            return null;
        }
    }

    private ChessPiece.PieceType promotionTranslation(String input) {
        if (input.equalsIgnoreCase("bishop")) {
            return ChessPiece.PieceType.BISHOP;
        } else if (input.equalsIgnoreCase("rook")) {
            return ChessPiece.PieceType.ROOK;
        } else if (input.equalsIgnoreCase("knight")) {
            return ChessPiece.PieceType.KNIGHT;
        } else if (input.equalsIgnoreCase("queen")){
            return ChessPiece.PieceType.QUEEN;
        }
        return null;
    }

}
