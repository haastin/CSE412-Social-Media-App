package com.cse412;

public class Comment {

    public int pid;
    public int uid;
    public String text;
    public String date;
    public int cid;

    public Comment() {
    }

    public Comment(int pid, int uid, String text, String date, int cid) {
        this.pid = pid;
        this.uid = uid;
        this.text = text;
        this.date = date;
        this.cid = cid;
    }

}
