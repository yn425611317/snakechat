package websocket;

import java.io.Serializable;

public class ChannelEntity implements Serializable {
    private int id;
    private String name;
    private int num;
    private boolean status;

    public ChannelEntity() {
    }

    public ChannelEntity(int id, String name, int num, boolean status) {
        this.id = id;
        this.name = name;
        this.num = num;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
