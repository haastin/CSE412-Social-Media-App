package com.cse412;
import java.sql.*;
import java.lang.System;

public class Database {

    private Connection conn;
    private String username = System.getenv("DB_USERNAME");
    private String password = System.getenv("DB_PASSWORD");
    private String url = System.getenv("DB_URL");

    
    public Database() throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        conn = DriverManager.getConnection(url, username, password);
    }

    /*browsing_homepage.sql */
    ResultSet getAllPidsOfUsersNotLoggedIn(int uid) throws SQLException{

        String query = "SELECT p.pid FROM Photos AS p, Albums AS a WHERE p.aid = a.aid AND a.uid != ?";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setInt(1, uid);
        ResultSet rs = stmt.executeQuery();
        return rs;
    }

    /*browsing_own_profile.sql */
    ResultSet getAllAlbumsOfLoggedInUser(int uid) throws SQLException{

        String query = "SELECT aid, albumName, dateCreated FROM Albums as a WHERE a.uid = ? GROUP BY a.uid";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setInt(1, uid);
        ResultSet rs = stmt.executeQuery();
        return rs;
    }

    ResultSet getAllPhotosInAnAlbum(int aid) throws SQLException{

        String query = "SELECT aid, albumName, dateCreated FROM Albums as a WHERE a.uid = ? GROUP BY a.uid";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setInt(1, aid);
        ResultSet rs = stmt.executeQuery();
        return rs;
    }

    ResultSet getAllPhotosOfLoggedInUser(int uid) throws SQLException{

        String query = "SELECT pid FROM Photos AS p, Albums AS a WHERE p.aid = a.aid AND a.uid = ?";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setInt(1, uid);
        ResultSet rs = stmt.executeQuery();
        return rs;

    }

    ResultSet getAllUserInfo(int uid) throws SQLException{

        String query = "SELECT * FROM Users as u WHERE u.uid = ?";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setInt(1, uid);
        ResultSet rs = stmt.executeQuery();
        return rs;
    }

    /*contribution_score.sql */
    ResultSet numPhotosEachUserHasUploaded() throws SQLException{

        String query = "SELECT COUNT(*), p.uid FROM Photos as p GROUP BY p.uid";
        PreparedStatement stmt = conn.prepareStatement(query);
        ResultSet rs = stmt.executeQuery();
        return rs;
    }

    ResultSet numCommentsEachUserHasUploaded() throws SQLException{

        String query = "SELECT COUNT(*), c.uid FROM Comments as c GROUP BY c.uid";
        PreparedStatement stmt = conn.prepareStatement(query);
        ResultSet rs = stmt.executeQuery();
        return rs;
    }

    ResultSet firstAndLastNameOfTopTenUsers(int[] uids) throws SQLException{

        String query = "SELECT u.firstName, u.lastName FROM Users AS u WHERE u.uid IN (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setInt(1, uids[0]);
        stmt.setInt(2, uids[1]);
        stmt.setInt(3, uids[2]);
        stmt.setInt(4, uids[3]);
        stmt.setInt(5, uids[4]);
        stmt.setInt(6, uids[5]);
        stmt.setInt(7, uids[6]);
        stmt.setInt(8, uids[7]);
        stmt.setInt(9, uids[8]);
        stmt.setInt(10, uids[9]);
        ResultSet rs = stmt.executeQuery();
        return rs;

    }

    /*creating_entries */
    


    




}
