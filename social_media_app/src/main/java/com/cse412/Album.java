package com.cse412;

public class Album {

    public int uid;
    public String albumName;
    public String dateCreated;

    public Album(){}

    public Album(int uid, String albumName, String dateCreated){
        this.uid = uid;
        this.albumName = albumName;
        this.dateCreated = dateCreated;
    }
    
}
