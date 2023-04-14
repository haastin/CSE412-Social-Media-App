package com.cse412;

public class Photo {
    
    public String url;
    public String caption;
    public int aid;
    int pid;

    public Photo(){}

    public Photo(String url, String caption, int aid, int pid){
        this.url = url;
        this.caption = caption;
        this.aid = aid;
        this.pid = pid;
    }
}
