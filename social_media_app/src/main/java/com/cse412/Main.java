package com.cse412;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.util.Pair;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.TilePane;
import javafx.scene.text.Font;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Orientation;
import javafx.scene.paint.Color;
import java.sql.*;
import java.util.Random;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;

public class Main extends Application {

    private static Database db;

    public void start(Stage primaryStage) {

        int curr_user = 1; // we change this to be the logged in user

        /* browing_homepage.sql and render_photo.sql
         * this section is for rendering a photo. the variables before the try statement
         * are what
         * you can use in the UI
         */
        String url = "";
        String poster_firstname = "";
        String poster_lastname = "";
        String caption = "";
        List<Tag> tags = new ArrayList<>();
        List<Pair<String, Comment>> comments = new ArrayList<>(); //String is firstName + LastName, Comment has all the fields to render for a comment
        List<User> likers = new ArrayList<>();
        int total_likes = 0;

        
        int random_pid = 0;
        /*
         * code below gets a random pid from all users not logged in and randomly
         * chooses a row and fetches its pid
         */
        try {
            List<Integer> pids = db.getAllPidsOfUsersNotLoggedIn(curr_user);
            int rows = pids.size();
            Random rando = new Random();
            int random_row = rando.nextInt(rows);
            random_pid = pids.get(random_row);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        /* here we get all the info from the randomly fetched pic */
        try {
            Photo pic = db.fetchPhotoInfo(random_pid);
            comments = db.fetchPhotoComments(random_pid);
            likers = db.fetchPhotoLikers(random_pid);
            tags = db.fetchPhotoTags(random_pid);
            User poster = db.fetchPhotoUser(random_pid);

            poster_firstname = poster.firstName;
            poster_lastname = poster.lastName;

            caption = pic.caption;
            url = pic.url;

            total_likes = likers.size();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        /*System.out.println("URL: " + url);
        System.out.println("Poster first name: " + poster_firstname);
        System.out.println("Poster last name: " + poster_lastname);
        System.out.println("Caption: " + caption);
        System.out.println("Tags:");
        for (Tag tag : tags) {
            System.out.println("- " + tag.word);
        }
        System.out.println("Comments:");
        for (Pair<String, Comment> comment : comments) {
            System.out.println("- " + comment.getKey() + ": " + comment.getValue().text + comment.getValue().date);
        }
        System.out.println("Likes:");
        for (User liker : likers) {
            System.out.println("- " + liker.firstName + " " + liker.lastName);
        }
        System.out.println("Total likes: " + total_likes);*/



        /* list of top then users */
        List<Pair<Integer, Integer>> all_num_photos = new ArrayList<>();
        List<Pair<Integer, Integer>> all_num_comments = new ArrayList<>();
        try {
            all_num_comments = db.numCommentsEachUserHasUploaded();
            all_num_photos = db.numPhotosEachUserHasUploaded();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        Map<Integer, Integer> all_totals = new HashMap<Integer, Integer>();
        PriorityQueue<Pair<Integer, Integer>> sorted_totals = new PriorityQueue<>(
                (p1, p2) -> p2.getValue().compareTo(p1.getValue()));

        for (Pair<Integer, Integer> pair1 : all_num_photos) {
            all_totals.put(pair1.getKey(), pair1.getValue());
        }
        
        for (Pair<Integer, Integer> pair2 : all_num_comments) {
            Integer val = all_totals.putIfAbsent(pair2.getKey(), pair2.getValue());
            if(val != null){
                all_totals.put(pair2.getKey(), val + pair2.getValue());
            }
        }
    
        for (Map.Entry<Integer, Integer> entry : all_totals.entrySet()) {
            Pair<Integer, Integer> user_total = new Pair<Integer,Integer>(entry.getKey(), entry.getValue());
            sorted_totals.add(user_total);
        }
        
        int[] top_ten_uids = new int[10];
        int i = 0;
        if (sorted_totals != null) {
            while (i < 10 && !sorted_totals.isEmpty()) {
                top_ten_uids[i] = sorted_totals.poll().getKey();
                i++;
            }
        }
        List<User> top_ten_users = new ArrayList<>();
        try {
            for(i = 0; i < 10; i++){
                top_ten_users.add(db.getAllUserInfo(top_ten_uids[i]));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        /*for (User user : top_ten_users) {
            System.out.println(user.firstName + " " + user.lastName);
        }*/


        /* regarding friends */

        //get uids of the user(s) that mmatch the searched name
        String search_firstname = "Sato";
        String search_lastname = "Hiroshi";
        List<Integer> uids_of_searched_name = new ArrayList<>();
        try{
            uids_of_searched_name = db.findUsersByName(search_firstname, search_lastname);
        }catch(SQLException e){
            e.printStackTrace();
        }
        /*for (Integer user : uids_of_searched_name) {
            System.out.println(user + " ");
        }*/

        //get all of this user's friends
        List<Integer> this_users_friends = new ArrayList<>();
        try{
            this_users_friends = db.getAllFriends(curr_user);
        }catch(SQLException e){
            e.printStackTrace();
        }
        /*for (Integer user : this_users_friends) {
            System.out.println(user + " ");
        }*/

        //get all users that have this user as a friend
        List<Integer> friends_of_this_user = new ArrayList<>();
        try{
            friends_of_this_user = db.getAllUsersWhoFriendedThisUser(curr_user);
        }catch(SQLException e){
            e.printStackTrace();
        }
        /*for (Integer user : friends_of_this_user) {
            System.out.println(user + " ");
        }*/

        //get all friends of this user's friends
        List<Integer> friends_of_this_users_friends = new ArrayList<>();
        try{
            friends_of_this_users_friends = db.getAllFriendsOfFriends(curr_user);
        }catch(SQLException e){
            e.printStackTrace();
        }
        /*for (Integer user : friends_of_this_users_friends) {
            System.out.print(user + " ");
        }*/
        

        /* browsing own profile*/

        List<Album> this_users_albums = new ArrayList<>();
        try{
            this_users_albums = db.getAllAlbumsOfLoggedInUser(curr_user);
        }catch(SQLException e){
            e.printStackTrace();
        }
        /*for (Album album : this_users_albums) {
            System.out.print(album.aid + " ");
        }*/

        User user_info = new User();
        try {
            user_info = db.getAllUserInfo(curr_user);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        /*for (User user : top_ten_users) {
            System.out.println(user.firstName + " " + user.lastName);
        }*/

        //retrieving pids in an album
        int retrieve_pids_from_aid =  5210;
        List<Integer> pids_in_album = new ArrayList<>();
        try{
            pids_in_album = db.getAllPhotosInAnAlbum(retrieve_pids_from_aid);
        }catch(SQLException e){
            e.printStackTrace();
        }
        /*for (Integer pid : pids_in_album) {
            System.out.print(pid + " ");
        }*/

        //retrieving all pids belonging to a user
        List<Integer> all_users_pids = new ArrayList<>();
        try{
            all_users_pids = db.getAllPhotosOfLoggedInUser(1700);
        }catch(SQLException e){
            e.printStackTrace();
        }
        /*for (Integer pids : all_users_pids) {
            System.out.print(pids + " ");
        }*/



        /* searching */

        //search by comment
        String search_comment = "";
        List<List<Object>> matches = new ArrayList<>();
        try{
            matches = db.searchCommentByText(search_comment);
        }catch(SQLException e){
            e.printStackTrace();
        }
        /*for (List<Object> match : matches) {
            System.out.print("Match: ");
            for (Object element : match) {
                System.out.print(element + " ");
            }
            System.out.println();
        }*/

        //search for currently logged in user's photos that have a certain tag
        String search_my_tag = "best";
        List<Integer> my_photos_with_tag = new ArrayList<>();
        try{
            my_photos_with_tag = db.getOwnPhotoIdsByTag(curr_user, search_my_tag);
        }catch(SQLException e){
            e.printStackTrace();
        }
        /*for (Integer match : my_photos_with_tag) {
            System.out.print(match + " ");
        }*/

        //search all photos that have a certain tag
        String search_all_tag = "";
        List<Integer> all_photos_with_tag = new ArrayList<>();
        try{
            all_photos_with_tag = db.getPhotoIdsByTag(search_all_tag, curr_user);
        }catch(SQLException e){
            e.printStackTrace();
        }
        /*for (Integer match : all_photos_with_tag) {
            System.out.print(match + " ");
        }*/

        //search this user's photos by multiple tags
        String[] search_my_tags = new String[]{"best","light"};
        List<Integer> my_photos_with_tags = new ArrayList<>();
        try{
            my_photos_with_tags = db.getPhotosByMultipleTagsAndUser(search_my_tags, curr_user);
        }catch(SQLException e){
            e.printStackTrace();
        }
        /*for (Integer match : my_photos_with_tags) {
            System.out.print(match + " ");
        }*/

        //search all photos (not this user's) by multiple tags
        String[] search_all_tags = new String[]{"best","light"};
        List<Integer> all_photos_with_tags = new ArrayList<>();
        try{
            all_photos_with_tags = db.getPhotosByMultipleTags(search_all_tags, curr_user);
        }catch(SQLException e){
            e.printStackTrace();
        }
        /*for (Integer match : all_photos_with_tags) {
            System.out.print(match + " ");
        }*/

        //search most popular tags
        List<String> most_popular_tags = new ArrayList<>();
        try{
            most_popular_tags = db.getMostPopularTags();
        }catch(SQLException e){
            e.printStackTrace();
        }


        /* you may also like- returns list of pids (non-user posted pids) that have the top tags and how many of the top tags they have */
        List<Pair<Integer,Integer>> top_pics = new ArrayList<>();
        try{
            top_pics = db.getTopTagsForUser(1700);
        }catch(SQLException e){
            e.printStackTrace();
        }
       /*for(Pair<Integer,Integer> pic : top_pics){
            System.out.println(pic.getKey() + " matches " + pic.getValue() + " tags");
        }       */

    }

    public static void main(String[] args) {
        try {
            db = new Database();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        launch(args);
    }
}
