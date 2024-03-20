package serverFacade;


import com.google.gson.Gson;
import exception.ResponseException;
import model.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;

public class ServerMain {

    private final String serverUrl;

    public ServerMain(String url) {
        serverUrl = url;
    }


    /**
     * THIS METHOD IS TO BE USED EXCLUSIVELY WITH TESTING.
     */
    public Message clearDB() throws ResponseException {
        var path = "/db";
        return this.makeRequest("DELETE", path, null, null, null);
    }

    public RegisterMessage registerUser(String username, String email, String password) throws ResponseException {
        var path = "/user";
        UserData user = new UserData(username, password, email);
        return this.makeRequest("POST", path, user, RegisterMessage.class, null);
    }

    public LoginMessage loginUser(String username, String password) throws ResponseException {
        var path = "/session";
        LoginData login = new LoginData(username, password);
        return this.makeRequest("POST", path, login, LoginMessage.class, null);
    }


    public AuthStorage logoutUser(String authToken) throws ResponseException {
        var path = "/session";
        return this.makeRequest("DELETE", path, null, AuthStorage.class, authToken);
    }


    private <T> T makeRequest(String method, String path, Object request, Class<T> responseClass,
                              String authToken) throws ResponseException {
        try {
            URL url = (new URI(serverUrl + path)).toURL();
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod(method);
            http.setDoOutput(true);

            if (authToken != null) {
                http.addRequestProperty("authorization", authToken);
            }
            writeBody(request, http);

            http.connect();
            throwIfNotSuccessful(http);
            return readBody(http, responseClass);
        } catch (Exception ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }

    private static void writeBody(Object request, HttpURLConnection http) throws IOException {
        if (request != null) {
            http.addRequestProperty("Content-Type", "application/json");

            String reqData = new Gson().toJson(request);
            try (OutputStream reqBody = http.getOutputStream()) {
                reqBody.write(reqData.getBytes());
            }
        }
    }

    private void throwIfNotSuccessful(HttpURLConnection http) throws IOException, ResponseException {
        var status = http.getResponseCode();
        if (!isSuccessful(status)) {
            throw new ResponseException(status, "failure: " + status);
        }
    }

    private static <T> T readBody(HttpURLConnection http, Class<T> responseClass) throws IOException {
        T response = null;
        var status = http.getResponseCode();
        if (http.getContentLength() < 0) {
            if(!isSuccessful(status)) {
                try (InputStream respBody = http.getErrorStream()) {
                    InputStreamReader reader = new InputStreamReader(respBody);
                    if (responseClass != null) {
                        response = new Gson().fromJson(reader, responseClass);
                    }

                }
            } else {
                try (InputStream respBody = http.getInputStream()) {
                    InputStreamReader reader = new InputStreamReader(respBody);
                    if (responseClass != null) {
                        response = new Gson().fromJson(reader, responseClass);
                    }
                }
            }
        }
        return response;
    }


    private static boolean isSuccessful(int status) {
        return status / 100 == 2;
    }


}
