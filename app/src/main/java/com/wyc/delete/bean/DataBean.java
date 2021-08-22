package com.wyc.delete.bean;

public class DataBean {
    private int id;
    private String flag;
    private String title;
    private String state;
    private int selectedPos;


    public DataBean() {
    }


    public DataBean(int id, String flag, String title, String state) {
        this.id = id;
        this.flag = flag;
        this.title = title;
        this.state = state;
    }

    public int getSelectedPos() {
        return selectedPos;
    }

    public void setSelectedPos(int selectedPos) {
        this.selectedPos = selectedPos;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "DataBean{" +
                "id=" + id +
                ", flag='" + flag + '\'' +
                ", title='" + title + '\'' +
                ", state='" + state + '\'' +
                ", selectedPos=" + selectedPos +
                '}';
    }
}
