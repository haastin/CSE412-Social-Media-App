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

    /* browsing_homepage.sql */
    ResultSet getAllPidsOfUsersNotLoggedIn(int uid) throws SQLException {

        String query = "SELECT p.pid FROM Photos AS p, Albums AS a WHERE p.aid = a.aid AND a.uid != ?";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setInt(1, uid);
        ResultSet rs = stmt.executeQuery();
        return rs;
    }

    /* browsing_own_profile.sql */
    ResultSet getAllAlbumsOfLoggedInUser(int uid) throws SQLException {

        String query = "SELECT aid, albumName, dateCreated FROM Albums as a WHERE a.uid = ? GROUP BY a.uid";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setInt(1, uid);
        ResultSet rs = stmt.executeQuery();
        return rs;
    }

    ResultSet getAllPhotosInAnAlbum(int aid) throws SQLException {

        String query = "SELECT aid, albumName, dateCreated FROM Albums as a WHERE a.uid = ? GROUP BY a.uid";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setInt(1, aid);
        ResultSet rs = stmt.executeQuery();
        return rs;
    }

    ResultSet getAllPhotosOfLoggedInUser(int uid) throws SQLException {

        String query = "SELECT pid FROM Photos AS p, Albums AS a WHERE p.aid = a.aid AND a.uid = ?";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setInt(1, uid);
        ResultSet rs = stmt.executeQuery();
        return rs;

    }

    ResultSet getAllUserInfo(int uid) throws SQLException {

        String query = "SELECT * FROM Users as u WHERE u.uid = ?";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setInt(1, uid);
        ResultSet rs = stmt.executeQuery();
        return rs;
    }

    /* contribution_score.sql */
    ResultSet numPhotosEachUserHasUploaded() throws SQLException {

        String query = "SELECT COUNT(*), p.uid FROM Photos as p GROUP BY p.uid";
        PreparedStatement stmt = conn.prepareStatement(query);
        ResultSet rs = stmt.executeQuery();
        return rs;
    }

    ResultSet numCommentsEachUserHasUploaded() throws SQLException {

        String query = "SELECT COUNT(*), c.uid FROM Comments as c GROUP BY c.uid";
        PreparedStatement stmt = conn.prepareStatement(query);
        ResultSet rs = stmt.executeQuery();
        return rs;
    }

    ResultSet firstAndLastNameOfTopTenUsers(int[] uids) throws SQLException {

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

    /* creating_entries */
    public void createPhoto(int aid, String caption, String url) throws SQLException {

        String query = "INSERT INTO Photos (aid, caption, url) VALUES (?, ?, ?)";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setInt(1, aid);
        stmt.setString(2, caption);
        stmt.setString(3, url);
        stmt.executeUpdate();
    }

    public void createAlbum(int uid, String albumName) throws SQLException {
        String query = "INSERT INTO Albums (uid, albumName) VALUES (?, ?)";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setInt(1, uid);
        stmt.setString(2, albumName);
        stmt.executeUpdate();
    }

    public void postComment(int pid, int uid, String text) throws SQLException {
        String query = "INSERT INTO Comments (pid, uid, text) VALUES (?, ?, ?)";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setInt(1, pid);
        stmt.setInt(2, uid);
        stmt.setString(3, text);
        stmt.executeUpdate();
    }

    public void recordLike(int pid, int uid) throws SQLException {
        String query = "INSERT INTO Likes (pid, uid) VALUES (?, ?)";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setInt(1, pid);
        stmt.setInt(2, uid);
        stmt.executeUpdate();
    }

    public void createTag(int pid, String word) throws SQLException {
        String query = "INSERT INTO Tags (pid, word) VALUES (?, ?)";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setInt(1, pid);
        stmt.setString(2, word);
        stmt.executeUpdate();
    }

    public void recordFriendship(int uid, int fid) throws SQLException {
        String query = "INSERT INTO Friends (uid, fid) VALUES (?, ?)";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setInt(1, uid);
        stmt.setInt(2, fid);
        stmt.executeUpdate();
    }

    /* deletion.sql */
    public void deleteUser(int uid) throws SQLException {
        String query = "DELETE FROM Users WHERE uid = ?";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setInt(1, uid);
        stmt.executeUpdate();
    }

    public void deletePhoto(int pid, int uid) throws SQLException {
        String query = "DELETE FROM Photos AS p WHERE p.pid = ? AND p.aid IN (SELECT a.aid FROM Albums AS a WHERE a.uid = ?)";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setInt(1, pid);
        stmt.setInt(2, uid);
        stmt.executeUpdate();
    }

    public void deleteAlbum(int aid, int uid) throws SQLException {
        String query = "DELETE FROM Albums WHERE aid = ? AND uid = ?";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setInt(1, aid);
        stmt.setInt(2, uid);
        stmt.executeUpdate();
    }

    public void deleteComment(int cid, int uid) throws SQLException {
        String query = "DELETE FROM Comments WHERE cid = ? AND uid = ?";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setInt(1, cid);
        stmt.setInt(2, uid);
        stmt.executeUpdate();
    }

    public void deleteLike(int uid, int pid) throws SQLException {
        String query = "DELETE FROM Likes WHERE uid = ? AND pid = ?";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setInt(1, uid);
        stmt.setInt(2, pid);
        stmt.executeUpdate();
    }

    public void unfollowUser(int uid, int fid) throws SQLException {
        String query = "DELETE FROM Friends WHERE uid = ? and fid = ?";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setInt(1, uid);
        stmt.setInt(2, fid);
        stmt.executeUpdate();
    }

    public void deleteTag(int pid, int uid, String word) throws SQLException {
        String query = "DELETE FROM Tags as t WHERE t.pid = ? AND t.word = ? AND t.pid IN (SELECT p.pid FROM Photos as p, Albums as a WHERE p.aid = a.aid AND a.uid = ?)";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setInt(1, pid);
        stmt.setString(2, word);
        stmt.setInt(3, uid);
        stmt.executeUpdate();
    }

    /* login:registration.sql */
    // Check if email exists in database and verify password
    // Return the logged in user’s UID
    public int loginUser(String email, String password) throws SQLException {
        String query = "SELECT u.uid FROM Users AS u WHERE u.email = ? AND u.password = ?";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setString(1, email);
        stmt.setString(2, password);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            return rs.getInt("uid");
        }
        return -1;
    }

    // Check if email exists in database
    // Return an empty relation if email doesn't exist
    public int checkEmailExists(String email) throws SQLException {
        String query = "SELECT u.email FROM Users AS u WHERE u.email = ?";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setString(1, email);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            return 0;
        }
        return 1;
    }

    // Create a new user with given user info
    public void createUser(String firstName, String lastName, String password, String gender, String hometown,
            String email, String dob) throws SQLException {
        String query = "INSERT INTO Users (firstName, lastName, password, gender, hometown, email, dob) VALUES (?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setString(1, firstName);
        stmt.setString(2, lastName);
        stmt.setString(3, password);
        stmt.setString(4, gender);
        stmt.setString(5, hometown);
        stmt.setString(6, email);
        stmt.setString(7, dob);
        stmt.executeUpdate();
    }

    /* regarding_friends.sql */
    // Find users that match first and last name when searching for friend (given
    // first and last name)
    public ResultSet findUsersByName(String firstName, String lastName) throws SQLException {
        String query = "SELECT u.uid FROM Users AS u WHERE u.firstName = ? AND u.lastName = ?";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setString(1, firstName);
        stmt.setString(2, lastName);
        ResultSet rs = stmt.executeQuery();
        return rs;
    }

    // Fetch all friends of a user (given user id)
    public ResultSet getAllFriends(int uid) throws SQLException {
        String query = "SELECT f.fid FROM Friends AS f WHERE f.uid = ?";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setInt(1, uid);
        ResultSet rs = stmt.executeQuery();
        return rs;
    }

    // Fetch all users that have this user ID as a friend
    public ResultSet getAllUsersWhoFriendedThisUser(int fid) throws SQLException {
        String query = "SELECT f.uid FROM Friends AS f WHERE f.fid = ?";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setInt(1, fid);
        ResultSet rs = stmt.executeQuery();
        return rs;
    }

    // Fetch all friends of friends of a user (given user id)
    public ResultSet getAllFriendsOfFriends(int uid) throws SQLException {
        String query = "SELECT f2.uid, COUNT(*) FROM Friends AS f2 WHERE f2.fid IN (SELECT f1.uid FROM Friends AS f1 WHERE f1.fid = ?) AND f2.uid NOT IN (SELECT f1.uid FROM Friends AS f1 WHERE f1.fid = ?) GROUP BY f2.uid";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setInt(1, uid);
        stmt.setInt(2, uid);
        ResultSet rs = stmt.executeQuery();
        return rs;
    }

    /* render_photo.sql */
    public ResultSet fetchPhotoInfo(int photoID) throws SQLException {
        String sql = "SELECT p.url, p.caption, p.pid FROM Photos AS p WHERE p.pid = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, photoID);
        ResultSet rs = stmt.executeQuery();
        return rs;
    }

public int fetchPhotoUser(int photoID) throws SQLException{
    String sql = "SELECT u.uid, u.firstName, u.lastName FROM Users AS u WHERE u.uid IN(SELECT a.uid FROM Albums as a WHERE a.pid = ?)";
    PreparedStatement stmt = conn.prepareStatement(sql);
    stmt.setInt(1, photoID);
    ResultSet rs = stmt.executeQuery();
    if(rs.next()){
        return rs.getInt("uid");
    }
    return -1;
}

    public ResultSet fetchPhotoLikes(int photoID) throws SQLException {
        String sql = "SELECT u.firstName, u.lastName, COUNT(*) FROM Users AS u WHERE u.uid IN(SELECT l.uid FROM Likes AS l WHERE l.pid = ?)";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, photoID);
        ResultSet rs = stmt.executeQuery();
        return rs;
    }

    public ResultSet fetchPhotoComments(int photoID) throws SQLException {
        String sql = "SELECT u.firstName, u.lastName, c.text, c.date FROM Users AS u, Comments AS c WHERE c.pid = ? AND u.uid = c.uid";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, photoID);
        ResultSet rs = stmt.executeQuery();
        return rs;
    }

    public ResultSet fetchPhotoTags(int photoID) throws SQLException {
        String sql = "SELECT t.word, t.pid FROM Tags as t WHERE t.pid = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, photoID);
        ResultSet rs = stmt.executeQuery();
        return rs;
    }

    /* searching.sql */
    public ResultSet searchCommentByText(String text) throws SQLException {
        String query = "SELECT c.text, u.firstName, u.lastName, COUNT(*) "
                + "FROM Comments AS c, Users AS u "
                + "WHERE c.text = ? AND c.uid = u.uid "
                + "GROUP BY c.uid "
                + "ORDER BY COUNT(*) DESC";
        PreparedStatement statement = conn.prepareStatement(query);
        statement.setString(1, text);
        return statement.executeQuery();
    }

    public ResultSet getPhotoIdsByTag(String tag) throws SQLException {
        String query = "SELECT pid "
                + "FROM Tags as t "
                + "WHERE t.word = ?";
        PreparedStatement statement = conn.prepareStatement(query);
        statement.setString(1, tag);
        return statement.executeQuery();
    }

    public ResultSet getOwnPhotoIdsByTag(int uid, String tag) throws SQLException {
        String query = "SELECT p.pid "
                + "FROM Photos AS p, Albums AS a "
                + "WHERE p.aid = a.aid AND a.uid = ? AND p.pid IN "
                + "(SELECT t.pid FROM Tags as t WHERE t.word = ?)";
        PreparedStatement statement = conn.prepareStatement(query);
        statement.setInt(1, uid);
        statement.setString(2, tag);
        return statement.executeQuery();
    }

    public ResultSet getPhotosByMultipleTags(String[] tags) throws SQLException {
        StringBuilder queryBuilder = new StringBuilder("SELECT t.pid FROM Tags as t WHERE ");
        for (int i = 0; i < tags.length; i++) {
            queryBuilder.append("t.word = ?");
            if (i < tags.length - 1) {
                queryBuilder.append(" AND ");
            }
        }
        String query = queryBuilder.toString();
        PreparedStatement statement = conn.prepareStatement(query);
        for (int i = 0; i < tags.length; i++) {
            statement.setString(i + 1, tags[i]);
        }
        return statement.executeQuery();
    }

    public ResultSet getPhotosByMultipleTagsAndUser(String[] tags, int uid) throws SQLException {
        StringBuilder queryBuilder = new StringBuilder("SELECT t.pid FROM Tags as t WHERE ");
        for (int i = 0; i < tags.length; i++) {
            queryBuilder.append("t.word = ?");
            if (i < tags.length - 1) {
                queryBuilder.append(" AND ");
            }
        }
        queryBuilder.append(" AND t.pid IN "
                + "(SELECT p.pid FROM Photos as p, Albums as a WHERE p.aid = a.aid AND a.uid = ?)");
        String query = queryBuilder.toString();
        PreparedStatement statement = conn.prepareStatement(query);
        for (int i = 0; i < tags.length; i++) {
            statement.setString(i + 1, tags[i]);
        }
        statement.setInt(tags.length + 1, uid);
        return statement.executeQuery();
    }

    public ResultSet getMostPopularTags() throws SQLException {
        String query = "SELECT t.word "
                + "FROM Tags as t "
                + "GROUP BY t.word "
                + "ORDER BY COUNT(*) DESC";
        PreparedStatement statement = conn.prepareStatement(query);
        return statement.executeQuery();
    }

    public ResultSet getPhotoIdsByTagWord(String tagWord) throws SQLException {
        String query = "SELECT t.pid "
                + "FROM Tags as t "
                + "WHERE t.word = ?";
        PreparedStatement statement = conn.prepareStatement(query);
        statement.setString(1, tagWord);
        return statement.executeQuery();
    }

    /* update.sql */

    // Update user info (given logged in user's uid what fields they want to change
    // (not all fields may be changed by a user, in which case the old values are
    // just passed in)
    public void updateUser(int uid, String firstName, String lastName, String gender, String hometown, String dob)
            throws SQLException {
        String query = "UPDATE Users AS u SET firstName = ?, lastName = ?, gender = ?, hometown = ?, dob = ? WHERE u.uid = ?";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setString(1, firstName);
        stmt.setString(2, lastName);
        stmt.setString(3, gender);
        stmt.setString(4, hometown);
        stmt.setString(5, dob);
        stmt.setInt(6, uid);
        stmt.executeUpdate();
    }

    // Update album (given user logged in's uid)
    public void updateAlbum(int uid, String albumName, int aid) throws SQLException {
        String query = "UPDATE Albums AS a SET albumName = ? WHERE a.uid = ? AND a.aid = ?";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setString(1, albumName);
        stmt.setInt(2, uid);
        stmt.setInt(3, aid);
        stmt.executeUpdate();
    }

    // Update a pic(given user logged in's uid); can update a pic's caption and tags
    // but not the pic itself
    public void updatePhoto(int uid, int pid, String caption) throws SQLException {
        String query = "UPDATE Photos AS p SET caption = ? WHERE p.pid = ? AND p.aid IN (SELECT a.aid FROM Albums AS a WHERE a.uid = ?)";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setString(1, caption);
        stmt.setInt(2, pid);
        stmt.setInt(3, uid);
        stmt.executeUpdate();
    }

    // Update a comment
    public void updateComment(int uid, String text, int pid) throws SQLException {
        String query = "UPDATE Comments AS c SET text = ? WHERE c.uid = ? AND c.pid = ?";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setString(1, text);
        stmt.setInt(2, uid);
        stmt.setInt(3, pid);
        stmt.executeUpdate();
    }

    // Update a tag if you want to change its word; otherwise delete it and/or
    // create a new one
    public void updateTag(int uid, int pid, String word) throws SQLException {
        String query = "UPDATE Tags AS t SET word = ? WHERE t.pid = ? AND t.pid IN (SELECT p.pid FROM Photos AS p, Albums AS a WHERE p.aid = a.aid AND a.uid = ?)";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setString(1, word);
        stmt.setInt(2, pid);
        stmt.setInt(3, uid);
        stmt.executeUpdate();
    }

    /* you_may_also_like.sql*/
    ResultSet getTopTagsForUser(int uid) throws SQLException {
        String query = "SELECT t1.pid, COUNT(*) " +
                       "FROM Tags as t1 " +
                       "WHERE t1.word IN " +
                           "(SELECT t2.word " +
                           "FROM Tags as t2 " +
                           "WHERE t2.pid IN " +
                               "(SELECT p.pid " +
                               "FROM Photos as p, Albums as a " +
                               "WHERE p.pid = a.pid AND a.uid = ?) " +
                           "GROUP BY t2.word " +
                           "ORDER BY COUNT(*) DESC " +
                           "LIMIT 5) " +
                       "GROUP BY t1.pid " +
                       "ORDER BY COUNT(*) DESC";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setInt(1, uid);
        ResultSet rs = stmt.executeQuery();
        return rs;
    }
}
