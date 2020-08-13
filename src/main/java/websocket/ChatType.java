package websocket;

public enum ChatType {

    SINGLE("1"),
    GROUP("2");
    private String type;

    ChatType(String s) {
        this.type = s;
    }
    public String getType(){
        return type;
    }
}
