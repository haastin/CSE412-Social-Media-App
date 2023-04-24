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
import javafx.scene.control.ComboBox;
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
    int curr_user; // we change this to be the logged in user
    boolean logged_in;

    public void start(Stage primaryStage) {
        
        /* create a login page to parse login information given */
        BorderPane rootPaneLogin = new BorderPane();
        GridPane centerPaneLogin = new GridPane();
        centerPaneLogin.setAlignment(Pos.CENTER);
        centerPaneLogin.setPadding(new Insets(10, 10, 10, 10));   // this is the spacing from the perimeter of the window
        centerPaneLogin.setHgap(10); // the spacing between objects horizontally
        centerPaneLogin.setVgap(10);  // the spacing between objects horizontally
        
        //Create 3 labels
  
        Label EmailLogin = new Label("EMAIL");
        Label Password = new Label("PASSWORD");
        Label Welcome = new Label("WELCOME TO OUR SOCIAL MEDIA APP");
        Button login = new Button("Log In");
        Button register = new Button("Register As New User");

        Welcome.setFont(new Font("Times New Roman", 20));   // double check the font--
        Welcome.setTextFill(Color.CRIMSON);

        //Create 2 text fields
    
        TextField EmailLoginField = new TextField();
        TextField PassField = new TextField();

   	    //add the 3 labels and 3 text fields accordingly
        rootPaneLogin.setCenter(centerPaneLogin);
        centerPaneLogin.add(EmailLogin, 0, 4);     
        centerPaneLogin.add(EmailLoginField, 0, 5);
        centerPaneLogin.add(Password, 0, 6);
        centerPaneLogin.add(PassField, 0, 7);
        centerPaneLogin.add(Welcome, 0, 0);
        centerPaneLogin.add(login, 0, 8);
        centerPaneLogin.add(register, 0 , 9);

        // it's like x and y coordinates, but use the window dimensions to kind of see how big a unit is

        // Create a scene and place it in the stage
    
        Scene scene1 = new Scene(rootPaneLogin, 700, 600);    // also x and y correlated
    
        primaryStage.setTitle("Social Media App"); // Set the stage title
        primaryStage.setScene(scene1); // Place the scene in the stage
        primaryStage.show(); // Display the stage

        /* if a user tries to insert something that already exists, a sql exception will be thrown, and the code in
         * the catch is executed, so an error box or whatever can be the code in the catch to address that.
         * 
         * the variables i created are dummy variables that will be populated with data from the labels, etc. So as long as
         * the variables are used with the labels, then everything in the DB side should go freely.
         */

         login.setOnAction(ev -> {
            
            /* login information */
            String login_with_email = EmailLoginField.getText();
            String hashed_password = PassField.getText();
            int status = -1;

            try{
                status = db.loginUser(login_with_email, hashed_password);
            }catch(SQLException e){
                e.printStackTrace();
            }
            if(status == -1){ 
                
                // display a messsage that shows that login failed
                Label loginFail = new Label("Email And/Or Password is Incorrect. Try Again.");
                loginFail.setTextFill(Color.CRIMSON);
                centerPaneLogin.add(loginFail, 0, 10);
                primaryStage.show();

            }
            else{
                curr_user = status;
                primaryStage.setScene(feedScene);
                //primaryStage.show();
            }

        });
        
        register.setOnAction(ev -> {
        
            /* set the stage for registering a new user */
            BorderPane rootPaneRegister = new BorderPane();
            GridPane centerPaneRegister = new GridPane();
            centerPaneRegister.setAlignment(Pos.CENTER);
            centerPaneRegister.setPadding(new Insets(10, 10, 10, 10));   // this is the spacing from the perimeter of the window
            centerPaneRegister.setHgap(10); // the spacing between objects horizontally
            centerPaneRegister.setVgap(10);  // the spacing between objects horizontally
            
            //Create 3 labels
    
            Label FirstName = new Label("FIRST NAME (*)");
            Label LastName = new Label("LAST NAME (*)");
            Label RegisterPassword = new Label("PASSWORD (*)");
            Label Gender = new Label("GENDER");
            Label Hometown = new Label("HOMETOWN");
            Label Email = new Label("EMAIL (*)");
            Label DOB = new Label("DATE OF BIRTH (*) (Format YYYY-MM-DD)");
            Label RegistrationWelcome = new Label("REGISTRATION");
            Button registerNow = new Button("Register");

            RegistrationWelcome.setFont(new Font("Times New Roman", 20));   // double check the font--
            RegistrationWelcome.setTextFill(Color.CRIMSON);

            //Create text fields and dropdowns
        
            TextField FirstNameField = new TextField();
            TextField LastNameField = new TextField();
            TextField RegisterPasswordField = new TextField();
            ComboBox GenderField = new ComboBox();
            GenderField.getItems().addAll(
                "Male",
                "Female",
                "Other"
            );
            TextField HometownField = new TextField();
            TextField EmailField = new TextField();
            TextField DOBField = new TextField();

            //add the labels and text fields accordingly
            rootPaneRegister.setCenter(centerPaneRegister);
            centerPaneRegister.add(FirstName, 0, 1);     
            centerPaneRegister.add(FirstNameField, 0, 2);
            centerPaneRegister.add(LastName, 0, 3);
            centerPaneRegister.add(LastNameField, 0, 4);
            centerPaneRegister.add(RegisterPassword, 0, 5);
            centerPaneRegister.add(RegisterPasswordField, 0, 6);
            centerPaneRegister.add(Gender, 0, 7);
            centerPaneRegister.add(GenderField, 0, 8);
            centerPaneRegister.add(Hometown, 0, 9);
            centerPaneRegister.add(HometownField, 0, 10);
            centerPaneRegister.add(Email, 0, 11);
            centerPaneRegister.add(EmailField, 0, 12);
            centerPaneRegister.add(DOB, 0, 13);
            centerPaneRegister.add(DOBField, 0, 14);
            centerPaneRegister.add(RegistrationWelcome, 0, 0);
            centerPaneRegister.add(registerNow, 0, 15);

            // it's like x and y coordinates, but use the window dimensions to kind of see how big a unit is

            // Create a scene and place it in the stage
        
            Scene scene2 = new Scene(rootPaneRegister, 700, 600);    // also x and y correlated
        
            primaryStage.setTitle("Social Media App"); // Set the stage title
            primaryStage.setScene(scene2); // Place the scene in the stage
            primaryStage.show(); // Display the stage
            
            /* if user presses register button, register the user */
            Label ErrorMessage = new Label("");
            ErrorMessage.setTextFill(Color.CRIMSON);
            centerPaneRegister.add(ErrorMessage, 0, 16);
            registerNow.setOnAction(eve -> {
            
                /* user registers */
                String firstName = FirstNameField.getText();
                String lastName = LastNameField.getText();
                String password_hashed = RegisterPasswordField.getText();
                String gender = "";
                gender += GenderField.getValue();

                // change gender to one character for database
                if (gender.equals("Male")) {
                    gender = "M";
                } else if (gender.equals("Female")) {
                    gender = "F";
                } else if (gender.equals("Other")) {
                    gender = "O";
                } else {
                    gender = "";
                }

                String hometown = HometownField.getText();
                String email = EmailField.getText();
                String dob = DOBField.getText();

                /* check if this email exists */
                boolean emailExists = true;
                try{
                    emailExists = db.checkEmailExists(email);
                    if(!emailExists) {
                        db.createUser(firstName, lastName, password_hashed, gender, hometown, email, dob);
                    } else {
                        
                        // if email exists, print that email is already in use
                        ErrorMessage.setText("Email is already in use. Try another email.");

                    }
                } catch(SQLException e){
                    ErrorMessage.setText("Invalid Email and/or Date of Birth Format. Try again.");
                }

            });

        });

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

        //get uids of the user(s) that match the searched name
        //render page for searching for users
        BorderPane rootPaneSearchUsers = new BorderPane();
        GridPane centerPaneSearchUsers = new GridPane();
        centerPaneSearchUsers.setAlignment(Pos.CENTER);
        centerPaneSearchUsers.setPadding(new Insets(10, 10, 10, 10));   // this is the spacing from the perimeter of the window
        centerPaneSearchUsers.setHgap(10); // the spacing between objects horizontally
        centerPaneSearchUsers.setVgap(10);  // the spacing between objects horizontally
        
        //Create labels and buttons
  
        Label SearchUsersFirstName = new Label("First Name:");
        Label SearchUsersLastName = new Label("Last Name:");
        Label SearchUserWelcome = new Label("Find Other Users!");
        Button SearchUserButton = new Button("Search");

        SearchUserWelcome.setFont(new Font("Times New Roman", 20));   // double check the font
        SearchUserWelcome.setTextFill(Color.CRIMSON);

        //Create 2 text field
    
        TextField SearchUserFirstNameField = new TextField();
        TextField SearchUserLastNameField = new TextField();

   	    //add the 3 labels and 3 text fields accordingly
        rootPaneSearchUsers.setCenter(centerPaneSearchUsers);
        centerPaneSearchUsers.add(SearchUserWelcome, 0, 0);     
        centerPaneSearchUsers.add(SearchUsersFirstName, 0, 1);
        centerPaneSearchUsers.add(SearchUsersLastName, 0 , 2);
        centerPaneSearchUsers.add(SearchUserFirstNameField, 0, 3);
        centerPaneSearchUsers.add(SearchUserLastNameField, 0, 4);
        centerPaneSearchUsers.add(SearchUserButton, 0, 5);

        // it's like x and y coordinates, but use the window dimensions to kind of see how big a unit is

        // Create a scene and place it in the stage
    
        Scene scene3 = new Scene(rootPaneSearchUsers, 700, 600);    // also x and y correlated
    
        primaryStage.setTitle("Social Media App"); // Set the stage title
        primaryStage.setScene(scene3); // Place the scene in the stage
        primaryStage.show(); // Display the stage

        SearchUserButton.setOnAction(ev -> {
            
            String search_firstname = SearchUserFirstNameField.getText();
            String search_lastname = SearchUserLastNameField.getText();
            List<Integer> uids_of_searched_name = new ArrayList<>();
            try{
                uids_of_searched_name = db.findUsersByName(search_firstname, search_lastname);
            }catch(SQLException e){
                e.printStackTrace();
            }
            /*for (Integer user : uids_of_searched_name) {
                System.out.println(user + " ");
            }*/

        });

        //get all of this user's friends
        //render page for seeing user friends
        BorderPane rootPaneSearchUserFriends = new BorderPane();
        GridPane centerPaneSearchUserFriends = new GridPane();
        centerPaneSearchUserFriends.setAlignment(Pos.CENTER);
        centerPaneSearchUserFriends.setPadding(new Insets(10, 10, 10, 10));   // this is the spacing from the perimeter of the window
        centerPaneSearchUserFriends.setHgap(10); // the spacing between objects horizontally
        centerPaneSearchUserFriends.setVgap(10);  // the spacing between objects horizontally
        
        //Create labels and buttons

        SearchUserWelcome.setFont(new Font("Times New Roman", 20));   // double check the font
        SearchUserWelcome.setTextFill(Color.CRIMSON);

        //Create 2 text field

   	    //add the 3 labels and 3 text fields accordingly
        rootPaneSearchUsers.setCenter(centerPaneSearchUsers);
        centerPaneSearchUsers.add(SearchUserWelcome, 0, 0);     
        centerPaneSearchUsers.add(SearchUsersFirstName, 0, 1);
        centerPaneSearchUsers.add(SearchUsersLastName, 0 , 2);
        centerPaneSearchUsers.add(SearchUserFirstNameField, 0, 3);
        centerPaneSearchUsers.add(SearchUserLastNameField, 0, 4);
        centerPaneSearchUsers.add(SearchUserButton, 0, 5);

        // it's like x and y coordinates, but use the window dimensions to kind of see how big a unit is

        // Create a scene and place it in the stage
    
        Scene sceneSearchUserFriends = new Scene(rootPaneSearchUserFriends, 700, 600);    // also x and y correlated
    
        primaryStage.setTitle("Social Media App"); // Set the stage title
        primaryStage.setScene(sceneSearchUserFriends); // Place the scene in the stage
        primaryStage.show(); // Display the stage
        
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


        /* browsing a profile*/

        //in the case where you want to view your own profile, you can make the target user uid the current user
        int target_user_uid = 1700;
        List<Album> this_users_albums = new ArrayList<>();
        try{
            this_users_albums = db.getAllAlbumsOfLoggedInUser(target_user_uid);
        }catch(SQLException e){
            e.printStackTrace();
        }
        /*for (Album album : this_users_albums) {
            System.out.print(album.aid + " ");
        }*/

        User user_info = new User();
        try {
            user_info = db.getAllUserInfo(target_user_uid);
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
            all_users_pids = db.getAllPhotosOfLoggedInUser(target_user_uid);
        }catch(SQLException e){
            e.printStackTrace();
        }
        /*for (Integer pids : all_users_pids) {
            System.out.print(pids + " ");
        }*/

        //retrieve all of a user's friends
        List<Friend> all_users_friends = new ArrayList<>();
        try{
            all_users_friends = db.getAllUsersFriend(target_user_uid);
        }catch(SQLException e){
            e.printStackTrace();
        }
        /*for (Friend fids : all_users_friends) {
            System.out.print(fids.fid + " ");
        }*/

        //all users who have the target user as a friend
        List<Friend> users_who_have_target_user_as_friend = new ArrayList<>();
        try{
            users_who_have_target_user_as_friend = db.getUsersWhoHaveTargetUserAsFriend(target_user_uid);
        }
        catch(SQLException e){
            e.printStackTrace();
        }
        /*for (Friend fids : users_who_have_target_user_as_friend) {
            System.out.print(fids.uid + " ");
        }*/



        /* searching */

        //search for a user 
        String search_user_firstName = "Shiro";
        String search_user_lastName = "Yoshiro";
        List<User> matched_user = new ArrayList<>();
        try{
            matched_user = db.searchUserByName(search_user_firstName, search_user_lastName);
        }catch(SQLException e){
            e.printStackTrace();
        }
        /*for (User match : matched_user) {
            System.out.print(match.firstName + " " + match.lastName);
        }*/

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

        /* creating_entries.sql */
        /*these vars need to be populated by labels/buttons above the db function calls, besides the holder for retrieved data 
        (none here since nothing is returned by the db function */
        /* 
        int put_in_aid = 5210;
        String new_caption = "im a gummy bear";
        String new_url = "resources/com/cse412/test.jpg";
        try{
        db.createPhoto(put_in_aid, new_caption, new_url);
        }
        catch(SQLException e){
            e.printStackTrace();
        }

        String new_albumName = "miracles";
        try{
            db.createAlbum(curr_user, new_albumName);
        }catch(SQLException e){
            e.printStackTrace();
        }
        
        int pid_commenting_on = 9990;
        String new_comment_text = "";
        try{
            db.postComment(pid_commenting_on, curr_user, new_comment_text);
        }catch(SQLException e){
            e.printStackTrace();
        }

        int pid_liking = 9990;
        try{
            db.recordLike(pid_liking, curr_user);
        }catch(SQLException e){
            e.printStackTrace();
        }
        
        String new_tag_word = "blah";
        int pid_tagging = 9990;
        try{
            db.createTag(pid_tagging, new_tag_word);
        } catch(SQLException e){
            e.printStackTrace();
        }

        int uid_of_user_you_want_friend = 1700;
        try{
            db.recordFriendship(curr_user, uid_of_user_you_want_friend);
        }catch(SQLException e){
            e.printStackTrace();
        }*/


        /* deletion.sql */

        //coomented this out because it deletes the test user when trying to test all my functions
        /* 
        try{
        db.deleteUser(curr_user);
        }
        catch(SQLException e){
            e.printStackTrace();
        }
        
        int deleting_aid = 5239;
        try{
            db.deleteAlbum(deleting_aid, curr_user);
        }catch(SQLException e){
            e.printStackTrace();
        }
        
        int deleting_cid = 0;
        try{
            db.deleteComment(deleting_cid, curr_user);
        }catch(SQLException e){
            e.printStackTrace();
        }

        int unlike_pid = 9990;
        try{
            db.deleteLike(curr_user, unlike_pid);
        }catch(SQLException e){
            e.printStackTrace();
        }

        int unfollow_uid = 1700;
        try{
            db.unfollowUser(curr_user, unfollow_uid);
        }catch(SQLException e){
            e.printStackTrace();
        }

        String delete_tag_word = "";
        int delete_tag_from_pid = 9990;
        try{
            db.deleteTag(delete_tag_from_pid, curr_user, delete_tag_word);
        }catch(SQLException e){
            e.printStackTrace();
        }*/

        /* update.sql */
         /*
        String update_firstName = "";
        String update_lastName = "";
        String update_gender = "";
        String update_hometown = "";
        String update_dob = ""; //need to enforce (or convert ourselves) that this matches the format of a MySQL DATE type variable
        try{
            db.updateUser(curr_user, update_firstName, update_lastName, update_gender, update_hometown, update_dob);
        }catch(SQLException e){
            e.printStackTrace();
        }

        String new_photo_caption = "";
        int pid_to_change = 9990;
        try{
            db.updatePhoto(curr_user, pid_to_change, new_photo_caption);
        }catch(SQLException e){
            e.printStackTrace();
        }

        String new_album_name = "";
        int aid_to_change = 5210;
        try{
            db.updateAlbum(curr_user, new_albumName, aid_to_change);
        }catch(SQLException e){
            e.printStackTrace();
        }

        String old_word = "";
        String new_word = "";
        int pid_of_tag = 9990;
        try{
            db.updateTag(curr_user, pid_of_tag, old_word, new_word);
        }catch(SQLException e){
            e.printStackTrace();
        }

        String update_text = "";
        int change_comment_on_pid = 9990;
        int change_comment_cid = 1;
        try{
            db.updateComment(curr_user, update_text, change_comment_on_pid, change_comment_cid);
        }catch(SQLException e){
            e.printStackTrace();
        }*/
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
