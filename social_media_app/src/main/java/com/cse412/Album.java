package com.cse412;

public class Album {

    public int aid;
    public int uid;
    public String albumName;
    public String dateCreated;

    public Album(){}

    public Album(int aid, int uid, String albumName, String dateCreated){
        this.aid = aid;
        this.uid = uid;
        this.albumName = albumName;
        this.dateCreated = dateCreated;
    }
    
}
