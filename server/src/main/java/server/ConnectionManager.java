package server;

import org.eclipse.jetty.websocket.api.Session;

import java.sql.Connection;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionManager {
    public final ConcurrentHashMap<String, Session> connections = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, Integer> openGames = new ConcurrentHashMap<>();


    public void add(String authToken, Integer gameID, Session session) {
        connections.put(authToken, session);
        openGames.put(authToken, gameID);
    }

    public Session getSession(String authToken) {
        return connections.get(authToken);
    }

    public Integer getGameID(String authToken) {
        return openGames.get(authToken);
    }

    public void remove(String authToken) {
        connections.remove(authToken);
        openGames.remove(authToken);
    }

    public HashSet<Session> getAllInGame(Integer gameID) {
        HashSet<Session> result = new HashSet<>();
        if (openGames.containsValue(gameID)) {
            for (String auth : openGames.keySet()) {
                if (Objects.equals(openGames.get(auth), gameID)) {
                    result.add(connections.get(auth));
                }
            }
            return result;
        }
        return null;
    }

}