package com.cse412;

import java.sql.*;
import java.lang.System;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

import javafx.util.Pair;

import java.util.HashMap;

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
    List<Integer> getAllPidsOfUsersNotLoggedIn(int uid) throws SQLException {

        List<Integer> pids = new ArrayList<Integer>();
        String query = "SELECT p.pid FROM Photos AS p, Albums AS a WHERE p.aid = a.aid AND a.uid != ?";
        try (
                PreparedStatement stmt = conn.prepareStatement(query);) {

            stmt.setInt(1, uid);

            try (
                    ResultSet rs = stmt.executeQuery();) {

                while (rs.next()) {
                    pids.add(rs.getInt("pid"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return pids;
    }

    /* browsing_a_profile.sql */
    List<Album> getAllAlbumsOfLoggedInUser(int uid) throws SQLException {

        String query = "SELECT aid, albumName, dateCreated FROM Albums as a WHERE a.uid = ?";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setInt(1, uid);
        ResultSet rs = stmt.executeQuery();
        List<Album> albums = new ArrayList<>();
        while (rs.next()) {
            Album album = new Album();
            album.aid = rs.getInt("aid");
            album.albumName = rs.getString("albumName");
            album.dateCreated = rs.getString("dateCreated");
            albums.add(album);
        }
        return albums;
    }

    List<Integer> getAllPhotosInAnAlbum(int aid) throws SQLException {

        String query = "SELECT p.pid FROM Photos as p, Albums as a WHERE a.aid = p.aid AND p.aid = ?";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setInt(1, aid);
        ResultSet rs = stmt.executeQuery();
        List<Integer> pics = new ArrayList<>();
        while (rs.next()) {
            pics.add(rs.getInt("p.pid"));
        }
        return pics;
    }

    List<Integer> getAllPhotosOfLoggedInUser(int uid) throws SQLException {

        String query = "SELECT pid FROM Photos AS p, Albums AS a WHERE p.aid = a.aid AND a.uid = ?";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setInt(1, uid);
        ResultSet rs = stmt.executeQuery();
        List<Integer> pics = new ArrayList<>();
        while (rs.next()) {
            pics.add(rs.getInt("pid"));
        }
        return pics;

    }

    User getAllUserInfo(int uid) throws SQLException {

        String query = "SELECT * FROM Users as u WHERE u.uid = ?";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setInt(1, uid);
        ResultSet rs = stmt.executeQuery();
        User user = new User();
        while (rs.next()) {
            user.firstName = rs.getString("firstName");
            user.lastName = rs.getString("lastName");
            user.dob = rs.getString("dob");
            user.email = rs.getString("email");
            user.gender = rs.getString("gender");
            user.hashPass = rs.getString("password");
            user.hometown = rs.getString("hometown");
            user.uid = uid;

        }
        return user;
    }

    List<Friend> getAllUsersFriend(int uid) throws SQLException {
        String query = "SELECT fid, uid, whenFriends FROM Friends WHERE uid = ?";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setInt(1, uid);
        ResultSet rs = stmt.executeQuery();
        List<Friend> friends = new ArrayList<>();
        while (rs.next()) {
            Friend friend = new Friend();
            friend.uid = rs.getInt("uid");
            friend.fid = rs.getInt("fid");
            friend.whenFriends = rs.getString("whenFriends");
            friends.add(friend);
        }
        return friends;
    }

    List<Friend> getUsersWhoHaveTargetUserAsFriend(int uid) throws SQLException {
        String query = "SELECT uid, fid, whenFriends FROM Friends WHERE fid = ?";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setInt(1, uid);
        ResultSet rs = stmt.executeQuery();
        List<Friend> friends = new ArrayList<>();
        while (rs.next()) {
            Friend friend = new Friend();
            friend.uid = rs.getInt("uid");
            friend.fid = rs.getInt("fid");
            friend.whenFriends = rs.getString("whenFriends");
            friends.add(friend);
        }
        return friends;
    }

    /* contribution_score.sql */
    List<Pair<Integer, Integer>> numPhotosEachUserHasUploaded() throws SQLException {

        String query = "SELECT COUNT(*), a.uid FROM Photos as p, Albums as a WHERE p.aid = a.aid GROUP BY a.uid";
        PreparedStatement stmt = conn.prepareStatement(query);
        ResultSet rs = stmt.executeQuery();
        List<Pair<Integer, Integer>> allnumphotos = new ArrayList<>();
        while (rs.next()) {
            Integer uid = (Integer) rs.getInt(2);
            Integer count = (Integer) rs.getInt(1);
            Pair<Integer, Integer> numphotos = new Pair<Integer, Integer>(uid, count);
            allnumphotos.add(numphotos);
        }
        return allnumphotos;
    }

    List<Pair<Integer, Integer>> numCommentsEachUserHasUploaded() throws SQLException {

        String query = "SELECT COUNT(*), c.uid FROM Comments as c GROUP BY c.uid";
        PreparedStatement stmt = conn.prepareStatement(query);
        ResultSet rs = stmt.executeQuery();
        List<Pair<Integer, Integer>> allnumcomments = new ArrayList<>();
        while (rs.next()) {
            Integer uid = rs.getInt(2);
            Integer count = rs.getInt(1);
            Pair<Integer, Integer> numcomments = new Pair<Integer, Integer>(uid, count);
            allnumcomments.add(numcomments);
        }
        return allnumcomments;
    }

    List<User> firstAndLastNameOfTopTenUsers(int[] uids) throws SQLException {

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
        List<User> toptenusers = new ArrayList<>();
        while (rs.next()) {
            User user = new User();
            user.firstName = rs.getString("firstName");
            user.lastName = rs.getString("lastName");
            toptenusers.add(user);
        }
        return toptenusers;

    }

    /* creating_entries */
    public int createPhoto(int aid, String caption, String url) throws SQLException {

        String query = "INSERT INTO Photos (aid, caption, url) VALUES (?, ?, ?)";
        
        PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
        stmt.setInt(1, aid);
        stmt.setString(2, caption);
        stmt.setString(3, url);
        stmt.executeUpdate();

        ResultSet rs = stmt.getGeneratedKeys();
        rs.next();
        int pid = rs.getInt(1);

        // Close the statement and result set
        stmt.close();
        rs.close();

        // Return the pid of the newly created photo
        return pid;
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
    public boolean checkEmailExists(String email) throws SQLException {
        String query = "SELECT u.email FROM Users AS u WHERE u.email = ?";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setString(1, email);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            return true;
        }
        return false;
    }

    // Create a new user with given user info
    public void createUser(String firstName, String lastName, String password, String gender, String hometown,
            String email, String dob) throws SQLException {
        String query = "INSERT INTO Users (firstName, lastName, password, gender, hometown, email, dob) VALUES (?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setString(1, firstName);
        stmt.setString(2, lastName);
        stmt.setString(3, password);
        if (gender.isBlank()) {
            stmt.setNull(4, java.sql.Types.VARCHAR);
        } else {
            stmt.setString(4, gender);
        }
        if (hometown.isBlank()) {
            stmt.setNull(5, java.sql.Types.VARCHAR);
        } else {
            stmt.setString(5, hometown);
        }
        stmt.setString(6, email);
        stmt.setString(7, dob);
        stmt.executeUpdate();
    }

    /* regarding_friends.sql */
    // Find users that match first and last name when searching for friend (given
    // first and last name)
    public List<Integer> findUsersByName(String firstName, String lastName) throws SQLException {
        String query = "SELECT u.uid FROM Users AS u WHERE u.firstName = ? AND u.lastName = ?";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setString(1, firstName);
        stmt.setString(2, lastName);
        ResultSet rs = stmt.executeQuery();
        List<Integer> uids = new ArrayList<>();
        while (rs.next()) {
            uids.add(rs.getInt("u.uid"));
        }
        return uids;
    }

    // Fetch all friends of a user (given user id)
    public List<Integer> getAllFriends(int uid) throws SQLException {
        String query = "SELECT f.fid FROM Friends AS f WHERE f.uid = ?";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setInt(1, uid);
        ResultSet rs = stmt.executeQuery();
        List<Integer> uids = new ArrayList<>();
        while (rs.next()) {
            uids.add(rs.getInt("f.fid"));
        }
        return uids;
    }

    // Fetch all users that have this user ID as a friend
    public List<Integer> getAllUsersWhoFriendedThisUser(int fid) throws SQLException {
        String query = "SELECT f.uid FROM Friends AS f WHERE f.fid = ?";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setInt(1, fid);
        ResultSet rs = stmt.executeQuery();
        List<Integer> uids = new ArrayList<>();
        while (rs.next()) {
            uids.add(rs.getInt("f.uid"));
        }
        return uids;
    }

    // Fetch all friends of friends of a user (given user id)
    public List<Integer> getAllFriendsOfFriends(int uid) throws SQLException {
        String query = "SELECT f2.fid FROM Friends AS f2 WHERE f2.uid IN (SELECT f1.fid FROM Friends AS f1 WHERE f1.uid = ?) AND f2.fid NOT IN (SELECT f1.fid FROM Friends AS f1 WHERE f1.uid = ?) GROUP BY f2.fid HAVING COUNT(*) > 1 ORDER BY COUNT(*) DESC";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setInt(1, uid);
        stmt.setInt(2, uid);
        ResultSet rs = stmt.executeQuery();
        List<Integer> uids = new ArrayList<>();
        while (rs.next()) {
            uids.add(rs.getInt("f2.fid"));
        }
        return uids;
    }

    /* render_photo.sql */
    public Photo fetchPhotoInfo(int photoID) throws SQLException {
        String sql = "SELECT p.url, p.caption, p.pid FROM Photos AS p WHERE p.pid = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, photoID);
        ResultSet rs = stmt.executeQuery();
        Photo photo = new Photo();
        while (rs.next()) {
            photo.url = rs.getString("url");
            photo.caption = rs.getString("caption");
            photo.pid = rs.getInt("pid");
        }

        return photo;
    }

    public User fetchPhotoUser(int photoID) throws SQLException {
        String sql = "SELECT u.uid, u.firstName, u.lastName FROM Users AS u WHERE u.uid IN(SELECT a.uid FROM Albums as a WHERE a.aid IN(SELECT aid FROM Photos WHERE pid = ?))";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, photoID);
        ResultSet rs = stmt.executeQuery();
        User user = new User();
        while (rs.next()) {
            user.firstName = rs.getString("firstName");
            user.lastName = rs.getString("lastName");
            user.uid = rs.getInt("uid");
        }
        return user;
    }

    public List<User> fetchPhotoLikers(int photoID) throws SQLException {
        String sql = "SELECT u.firstName, u.lastName, u.uid FROM Users AS u WHERE u.uid IN(SELECT l.uid FROM Likes AS l WHERE l.pid = ?)";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, photoID);
        ResultSet rs = stmt.executeQuery();
        List<User> users = new ArrayList<>();
        while (rs.next()) {
            User user = new User();
            user.firstName = rs.getString("firstName");
            user.lastName = rs.getString("lastName");
            user.uid = rs.getInt("uid");
            users.add(user);
        }
        return users;
    }

    public List<Pair<String, Comment>> fetchPhotoComments(int photoID) throws SQLException {
        String sql = "SELECT u.firstName, u.lastName, c.text, c.date, c.cid, c.uid FROM Users AS u, Comments AS c WHERE c.pid = ? AND u.uid = c.uid";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, photoID);
        ResultSet rs = stmt.executeQuery();
        List<Pair<String, Comment>> comments = new ArrayList<>();
        while (rs.next()) {
            Comment comment = new Comment();
            comment.text = rs.getString("c.text");
            comment.date = rs.getString("c.date");
            comment.cid = rs.getInt("c.cid");
            comment.uid = rs.getInt("c.uid");
            String commenter = rs.getString("u.firstName") + " " + rs.getString("u.lastName");
            Pair<String, Comment> full_comment = new Pair<String, Comment>(commenter, comment);
            comments.add(full_comment);
        }
        return comments;
    }

    public List<Tag> fetchPhotoTags(int photoID) throws SQLException {
        String sql = "SELECT t.word, t.pid FROM Tags as t WHERE t.pid = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, photoID);
        ResultSet rs = stmt.executeQuery();
        List<Tag> tags = new ArrayList<>();
        while (rs.next()) {
            Tag tag = new Tag(rs.getString("word"), rs.getInt("pid"));
            tags.add(tag);
        }
        return tags;
    }

    /* searching.sql */
    public List<List<Object>> searchCommentByText(String text) throws SQLException {
        String query = "SELECT c.text, u.firstName, u.lastName, COUNT(*) "
                + "FROM Comments AS c, Users AS u "
                + "WHERE c.text = ? AND c.uid = u.uid "
                + "GROUP BY c.uid "
                + "ORDER BY COUNT(*) DESC";
        PreparedStatement statement = conn.prepareStatement(query);
        statement.setString(1, text);
        ResultSet rs = statement.executeQuery();
        List<List<Object>> comments = new ArrayList<>();
        while (rs.next()) {
            List<Object> row = new ArrayList<>();
            String first_last = rs.getString("firstName") + " " + rs.getString("lastName");
            Comment comment = new Comment();
            comment.text = rs.getString("text");
            row.add(first_last);
            row.add(comment);
            Integer count = (Integer) rs.getInt(4);
            row.add(count);
            comments.add(row);
        }
        return comments;

    }

    public List<User> searchUserByName(String firstName, String lastName) throws SQLException {

        String query = "SELECT * FROM Users as u WHERE u.firstName = ? AND u.lastName = ?";
        PreparedStatement statement = conn.prepareStatement(query);
        statement.setString(1, firstName);
        statement.setString(2, lastName);
        ResultSet rs = statement.executeQuery();
        List<User> matching_users = new ArrayList<>();
        while (rs.next()) {
            User user = new User();
            user.firstName = rs.getString("firstName");
            user.lastName = rs.getString("lastName");
            matching_users.add(user);
        }
        return matching_users;
    }

    public List<Integer> getPhotoIdsByTag(String tag, int uid) throws SQLException {
        String query = "SELECT t.pid "
                + "FROM Tags as t "
                + "INNER JOIN Photos as p ON t.pid = p.pid "
                + "INNER JOIN Albums as a ON a.aid = p.aid "
                + "WHERE t.word = ? AND a.uid != ?";
        PreparedStatement statement = conn.prepareStatement(query);
        statement.setString(1, tag);
        statement.setInt(2, uid);
        ResultSet rs = statement.executeQuery();
        List<Integer> pids = new ArrayList<>();
        while (rs.next()) {
            pids.add(rs.getInt("t.pid"));
        }
        return pids;
    }

    public List<Integer> getOwnPhotoIdsByTag(int uid, String tag) throws SQLException {
        String query = "SELECT p.pid "
                + "FROM Photos AS p, Albums AS a "
                + "WHERE p.aid = a.aid AND a.uid = ? AND p.pid IN "
                + "(SELECT t.pid FROM Tags as t WHERE t.word = ?)";
        PreparedStatement statement = conn.prepareStatement(query);
        statement.setInt(1, uid);
        statement.setString(2, tag);
        ResultSet rs = statement.executeQuery();
        List<Integer> pids = new ArrayList<>();
        while (rs.next()) {
            pids.add(rs.getInt("pid"));
        }
        return pids;
    }

    public List<Integer> getPhotosByMultipleTags(String[] tags, int uid) throws SQLException {
        StringBuilder queryBuilder = new StringBuilder("SELECT t.pid FROM Tags as t WHERE ");
        for (int i = 0; i < tags.length; i++) {
            queryBuilder.append("t.word = ?");
            if (i < tags.length - 1) {
                queryBuilder.append(" OR ");
            }
        }
        queryBuilder.append(" AND t.pid NOT IN "
                + "(SELECT p.pid FROM Photos as p, Albums as a WHERE p.aid = a.aid AND a.uid = ?)"
                + "GROUP BY t.pid HAVING COUNT(*) = ?");
        String query = queryBuilder.toString();
        PreparedStatement statement = conn.prepareStatement(query);
        for (int i = 0; i < tags.length; i++) {
            statement.setString(i + 1, tags[i]);
        }
        statement.setInt(tags.length + 1, uid);
        statement.setInt(tags.length + 2, tags.length);
        ResultSet rs = statement.executeQuery();
        List<Integer> pids = new ArrayList<>();
        while (rs.next()) {
            pids.add(rs.getInt("t.pid"));
        }
        return pids;
    }

    public List<Integer> getPhotosByMultipleTagsAndUser(String[] tags, int uid) throws SQLException {
        StringBuilder queryBuilder = new StringBuilder("SELECT t.pid FROM Tags as t WHERE ");
        for (int i = 0; i < tags.length; i++) {
            queryBuilder.append("t.word = ?");
            if (i < tags.length - 1) {
                queryBuilder.append(" OR ");
            }
        }
        queryBuilder.append(" AND t.pid IN "
                + "(SELECT p.pid FROM Photos as p, Albums as a WHERE p.aid = a.aid AND a.uid = ?)"
                + "GROUP BY t.pid HAVING COUNT(*) = ?");
        String query = queryBuilder.toString();
        PreparedStatement statement = conn.prepareStatement(query);
        for (int i = 0; i < tags.length; i++) {
            statement.setString(i + 1, tags[i]);
        }
        statement.setInt(tags.length + 1, uid);
        statement.setInt(tags.length + 2, tags.length);
        ResultSet rs = statement.executeQuery();
        List<Integer> pids = new ArrayList<>();
        while (rs.next()) {
            pids.add(rs.getInt("t.pid"));
        }
        return pids;
    }

    public List<String> getMostPopularTags() throws SQLException {
        String query = "SELECT t.word "
                + "FROM Tags as t "
                + "GROUP BY t.word "
                + "ORDER BY COUNT(*) DESC";
        PreparedStatement statement = conn.prepareStatement(query);
        ResultSet rs = statement.executeQuery();
        List<String> tags = new ArrayList<>();
        while (rs.next()) {
            tags.add(rs.getString("t.word"));
        }
        return tags;
    }

    // dont think we need this as getPhotoIDsByTag does the same thing
    /*
     * public List<Integer> getPhotoIdsByTagWord(String tagWord) throws SQLException
     * {
     * String query = "SELECT t.pid "
     * + "FROM Tags as t "
     * + "WHERE t.word = ?";
     * PreparedStatement statement = conn.prepareStatement(query);
     * statement.setString(1, tagWord);
     * ResultSet rs = statement.executeQuery();
     * List<Integer> pids = new ArrayList<>();
     * while(rs.next()){
     * pids.add(rs.getInt("pid"));
     * }
     * return pids;
     * }
     */

    /* update.sql */

    // Update user info (given logged in user's uid what fields they want to change
    // (not all fields may be changed by a user, in which case the old values are
    // just passed in)
    public void updateUser(int uid, String firstName, String lastName, String hash_pass, String gender, String hometown,
            String dob)
            throws SQLException {
        String query = "UPDATE Users AS u SET firstName = ?, lastName = ?, dob = ?";

        int added_stuff = 4;
        if (gender != null) {
            query = query + ", gender = ?";
        }

        if (hometown != null) {
            query = query + ", hometown = ?";
        }

        if (!hash_pass.equals("********")) {
            System.out.println(" insert pass " + hash_pass);
            query = query + ", password = ?";
        }

        query = query + " WHERE u.uid = ?";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setString(1, firstName);
        stmt.setString(2, lastName);
        stmt.setString(3, dob);
        if (gender != null) {
            stmt.setString(added_stuff, gender);
            added_stuff++;
        }
        if (hometown != null) {
            stmt.setString(added_stuff, hometown);
            added_stuff++;
        }
        if (!hash_pass.equals("********")) {
            stmt.setString(added_stuff, hash_pass);
            added_stuff++;
        }

        stmt.setInt(added_stuff, uid);
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
    public void updateComment(int uid, String text, int pid, int cid) throws SQLException {
        String query = "UPDATE Comments AS c SET text = ? WHERE c.uid = ? AND c.pid = ? AND c.cid = ?";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setString(1, text);
        stmt.setInt(2, uid);
        stmt.setInt(3, pid);
        stmt.setInt(4, cid);
        stmt.executeUpdate();
    }

    // Update a tag if you want to change its word; otherwise delete it and/or
    // create a new one
    public void updateTag(int uid, int pid, String old_word, String new_word) throws SQLException {
        String query = "UPDATE Tags AS t SET word = ? WHERE t.pid = ? AND t.word = ? AND t.pid IN (SELECT p.pid FROM Photos AS p, Albums AS a WHERE p.aid = a.aid AND a.uid = ?)";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setString(1, new_word);
        stmt.setString(3, old_word);
        stmt.setInt(2, pid);
        stmt.setInt(4, uid);
        stmt.executeUpdate();
    }

    /* you_may_also_like.sql */
    List<Pair<Integer, Integer>> getTopTagsForUser(int uid) throws SQLException {
        String query = "SELECT t1.pid, COUNT(*) " +
                "FROM Tags AS t1 " +
                "INNER JOIN (" +
                "  SELECT t2.word " +
                "  FROM Tags AS t2 " +
                "  INNER JOIN (" +
                "    SELECT p.pid " +
                "    FROM Photos AS p " +
                "    INNER JOIN Albums AS a ON p.aid = a.aid " +
                "    WHERE a.uid = ?" +
                "  ) AS t3 ON t2.pid = t3.pid " +
                "  GROUP BY t2.word " +
                "  ORDER BY COUNT(*) DESC " +
                "  LIMIT 5" +
                ") AS t4 ON t1.word = t4.word " +
                "INNER JOIN Photos AS ph ON ph.pid = t1.pid " +
                "INNER JOIN Albums AS ab ON ph.aid = ab.aid " +
                "WHERE ab.uid != ? " +
                "GROUP BY t1.pid " +
                "ORDER BY COUNT(*) DESC";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setInt(1, uid);
        stmt.setInt(2, uid);
        ResultSet rs = stmt.executeQuery();
        List<Pair<Integer, Integer>> top_tags = new ArrayList<>();
        while (rs.next()) {
            top_tags.add(new Pair<Integer, Integer>(rs.getInt("pid"), rs.getInt(2)));
        }
        return top_tags;
    }
}
