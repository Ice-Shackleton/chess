package webSocketMessages;

public class SocketMessage {
    Class<?> type;
    String json;

    public SocketMessage(Class<?> type, String json){
        this.type = type;
        this.json = json;
    }

    public Class<?> getType() {
        return type;
    }

    public String getJson() {
        return json;
    }

    public void setType(Class<?> type) {
        this.type = type;
    }

    public void setJson(String json) {
        this.json = json;
    }
}