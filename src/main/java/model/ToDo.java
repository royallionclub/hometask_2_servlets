package model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ToDo {
    @JsonProperty("id")
    private int id;
    @JsonProperty("text")
    private String txt;

    public ToDo() {

    }

    public ToDo(int id, String text) {
        this.id = id;
        this.txt = text;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTxt() {
        return txt;
    }

    public void setTxt(String txt) {
        this.txt = txt;
    }

    @Override
    public String toString() {
        return "ToDo{" +
                "id=" + id +
                ", text='" + txt + '\'' +
                '}';
    }
}

