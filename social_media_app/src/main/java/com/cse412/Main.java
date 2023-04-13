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

        /*
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

        /* get photo ids that match the searched */

        // all photos that belong to this tag

        /* render album names */

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
