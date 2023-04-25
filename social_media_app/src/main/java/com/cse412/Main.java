package com.cse412;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.stage.Stage;
import javafx.util.Pair;
import javafx.scene.Scene;
import javafx.scene.Group;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Orientation;
import javafx.scene.paint.Color;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.io.FileInputStream;
import javafx.collections.*;
import javafx.scene.text.Text.*;
import javafx.scene.paint.*;
import javafx.scene.text.*;
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
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.io.InputStream;


public class Main extends Application {

    static Database db;
    static SceneManager sm;
    static int curr_user; // we change this to be the logged in user
    boolean logged_in = false;
    String tagToSearch;
    boolean clickedOnTag = false;

    public void start(Stage primaryStage) throws NoSuchAlgorithmException {

        sm = new SceneManager(primaryStage);
        
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
        sm.loginScene = scene1;
    
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
            String unhashed_password = PassField.getText();
            String hashed_password = "";

            // hash the password given
            try {

                MessageDigest digest = MessageDigest.getInstance("SHA-256");
                byte[] encodedHash = digest.digest(unhashed_password.getBytes());
                StringBuilder hexString = new StringBuilder();
                for (int i = 0; i < encodedHash.length; i++) {
                    String hex = Integer.toHexString(0xff & encodedHash[i]);
                    if (hex.length() == 1) {
                        hexString.append('0');
                    }
                    hexString.append(hex);
                }

                hashed_password = hexString.toString();
            
            } catch (NoSuchAlgorithmException e) {
                System.out.println("No Such Algorithm Exception Thrown");
            }

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
                sm.switchToFeed(false);
                logged_in = true;
                //primaryStage.setScene(feedScene);
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
                String password_unhashed = RegisterPasswordField.getText();
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
                String password_hashed = "";

                // hash the password given
                try {

                    MessageDigest digest = MessageDigest.getInstance("SHA-256");
                    byte[] encodedHash = digest.digest(password_unhashed.getBytes());
                    StringBuilder hexString = new StringBuilder();
                    for (int i = 0; i < encodedHash.length; i++) {
                        String hex = Integer.toHexString(0xff & encodedHash[i]);
                        if (hex.length() == 1) {
                            hexString.append('0');
                        }
                        hexString.append(hex);
                    }

                    password_hashed = hexString.toString();
                
                } catch (NoSuchAlgorithmException e) {
                    System.out.println("No Such Algorithm Exception Thrown");
                }

                /* check if this email exists */
                boolean emailExists = true;
                try{
                    emailExists = db.checkEmailExists(email);
                    if(!emailExists) {
                        db.createUser(firstName, lastName, password_hashed, gender, hometown, email, dob);
                        sm.switchToFeed(false);
                        logged_in = true;
                    } else {
                        
                        // if email exists, print that email is already in use
                        ErrorMessage.setText("Email is already in use. Try another email.");

                    }
                } catch(SQLException e){
                    ErrorMessage.setText("Invalid Email and/or Date of Birth Format. Try again.");
                }

            });

        });
                
                /* top ten users */
                
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
                    
                    // create page to display top ten users
                    BorderPane rootPaneTopTen = new BorderPane();
                    GridPane centerPaneTopTen = new GridPane();
                    centerPaneTopTen.setAlignment(Pos.CENTER);
                    centerPaneTopTen.setPadding(new Insets(10, 10, 10, 10));
                    centerPaneTopTen.setHgap(10);
                    centerPaneTopTen.setVgap(10);

                    // create labels
                    Label TopTenUsersWelcome = new Label("Top Ten Users");
                    Button GoBackTopTen = new Button("Go Back");

                    TopTenUsersWelcome.setFont(new Font("Times New Roman", 20));
                    TopTenUsersWelcome.setTextFill(Color.CRIMSON);

                    // add welcome label
                    rootPaneTopTen.setCenter(centerPaneTopTen);
                    centerPaneTopTen.add(TopTenUsersWelcome, 0, 0);

                    // add the top ten users to the pane
                    i = 1;
                    for (User user : top_ten_users) {
                        
                        centerPaneTopTen.add(new Label(user.firstName), 0, i);
                        centerPaneTopTen.add(new Label(user.lastName), 1, i);
                        i++;

                    }

                    // add button
                    centerPaneTopTen.add(GoBackTopTen, 0, 11);

                    // set scene
                    Scene SceneTopTen = new Scene(rootPaneTopTen, 700, 600);
                    sm.topTenUsersScene = SceneTopTen;

                    // go back to main feedpage if go back is pressed
                    GoBackTopTen.setOnAction (ev -> {

                        sm.switchToFeed(false);

                    });


                /* regarding friends */

                /* search for users */
                
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
                    Button GoBackSearchUsers = new Button("Go Back");

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
                    centerPaneSearchUsers.add(GoBackSearchUsers, 0, 16); 

                    // it's like x and y coordinates, but use the window dimensions to kind of see how big a unit is

                    // Create a scene and place it in the stage
                
                    Scene scene3 = new Scene(rootPaneSearchUsers, 700, 600);    // also x and y correlated
                
                    sm.searchForUsersScene = scene3;

                    SearchUserButton.setOnAction(ev -> {
                        
                        String search_firstname = SearchUserFirstNameField.getText();
                        String search_lastname = SearchUserLastNameField.getText();
                        List<Integer> uids_of_searched_name = new ArrayList<>();
                        try{
                            uids_of_searched_name = db.findUsersByName(search_firstname, search_lastname);
                        }catch(SQLException e){
                            e.printStackTrace();
                        }
                        
                        // display searched users on pane
                        int j = 6;
                        for (Integer user : uids_of_searched_name) {
                            
                            User u = null;
                            try {
                                u = db.getAllUserInfo(user);
                            } catch (SQLException e) {

                            }
                            centerPaneSearchUsers.add(new Label(u.firstName), 0, j);
                            centerPaneSearchUsers.add(new Label(u.lastName), 1, j);
                            j++;

                            if (j >= 16) {
                                break;
                            }

                        }

                    });

                    // go back to feedPage
                    GoBackSearchUsers.setOnAction(ev -> {

                        sm.switchToFeed(false);

                    });

                
                /* get user friends and display */ 
                
                    //render page for seeing user friends
                    BorderPane rootPaneSearchUserFriends = new BorderPane();
                    GridPane centerPaneSearchUserFriends = new GridPane();
                    centerPaneSearchUserFriends.setAlignment(Pos.CENTER);
                    centerPaneSearchUserFriends.setPadding(new Insets(10, 10, 10, 10));   // this is the spacing from the perimeter of the window
                    centerPaneSearchUserFriends.setHgap(10); // the spacing between objects horizontally
                    centerPaneSearchUserFriends.setVgap(10);  // the spacing between objects horizontally
                    
                    //Create labels and buttons
                    
                    Label UserFriendWelcome = new Label("Your Friends");
                    Button UserFriendsGoBack = new Button("Go Back"); 
                    UserFriendWelcome.setFont(new Font("Times New Roman", 20));   // double check the font
                    UserFriendWelcome.setTextFill(Color.CRIMSON);

                    //add the 3 labels and 3 text fields accordingly
                    rootPaneSearchUserFriends.setCenter(centerPaneSearchUserFriends);
                    centerPaneSearchUserFriends.add(UserFriendWelcome, 0, 0);     
                    centerPaneSearchUserFriends.add(UserFriendsGoBack, 0, 11);

                    // it's like x and y coordinates, but use the window dimensions to kind of see how big a unit is

                    // Create a scene and place it in the stage
                
                    Scene sceneUserFriends = new Scene(rootPaneSearchUserFriends, 700, 600);    // also x and y correlated
                    
                    
                    List<Integer> this_users_friends = new ArrayList<>();
                    try{
                        this_users_friends = db.getAllFriends(curr_user);
                    }catch(SQLException e){
                        e.printStackTrace();
                    }

                    // put 10 friends on display
                    i = 1;
                    for (Integer user : this_users_friends) {
                        
                        User u = null;
                        try {
                            u = db.getAllUserInfo(user);
                        } catch (SQLException e) {

                        }
                        centerPaneSearchUserFriends.add(new Label(u.firstName), 0, i);
                        centerPaneSearchUserFriends.add(new Label(u.lastName), 1, i);
                        i++;

                        if (i >= 11) {
                            break;
                        }

                    }

                    sm.displayUserFriendsScene = sceneUserFriends;

                    UserFriendsGoBack.setOnAction(ev -> {

                        sm.switchToFeed(false);

                    });

                /* display all users who have current user as friend */
                    
                    // render a page to show friends of user
                    BorderPane rootPaneFriendsOfUser = new BorderPane();
                    GridPane centerPaneFriendsOfUser = new GridPane();
                    centerPaneFriendsOfUser.setAlignment(Pos.CENTER);
                    centerPaneFriendsOfUser.setPadding(new Insets(10, 10, 10, 10));
                    centerPaneFriendsOfUser.setHgap(10);
                    centerPaneFriendsOfUser.setVgap(10);

                    // create labels and buttons
                    Label FriendsOfUsersWelcome = new Label("Users Who Have You As A Friend");
                    Button FriendsOfUserGoBack = new Button("Go Back");
                    FriendsOfUsersWelcome.setFont(new Font("Times New Roman", 20));
                    FriendsOfUsersWelcome.setTextFill(Color.CRIMSON);

                    // add to centerpane
                    rootPaneFriendsOfUser.setCenter(centerPaneFriendsOfUser);
                    centerPaneFriendsOfUser.add(FriendsOfUsersWelcome, 0, 0);
                    centerPaneFriendsOfUser.add(FriendsOfUserGoBack, 0, 11);

                    Scene FriendsOfUsersScene = new Scene(rootPaneFriendsOfUser, 700, 600);

                    //get all users that have this user as a friend
                    List<Integer> friends_of_this_user = new ArrayList<>();
                    try{
                        friends_of_this_user = db.getAllUsersWhoFriendedThisUser(curr_user);
                    }catch(SQLException e){
                        e.printStackTrace();
                    }

                    // put friends of user on pane
                    i = 1;
                    for (Integer user : friends_of_this_user) {
                        
                        User u = null;
                        try {
                            u = db.getAllUserInfo(user);
                        } catch (SQLException e) {

                        }
                        centerPaneFriendsOfUser.add(new Label(u.firstName), 0, i);
                        centerPaneFriendsOfUser.add(new Label(u.lastName), 1, i);
                        i++;

                        if (i >= 11) {
                            break;
                        }

                    }

                    sm.displayFriendsOfUsersScene = FriendsOfUsersScene;

                    FriendsOfUserGoBack.setOnAction(ev -> {

                        sm.switchToFeed(false);

                    });


                /* Friend Recommendations */

                    // render a page to show friend recommendations
                    BorderPane rootPaneFriendRec = new BorderPane();
                    GridPane centerPaneFriendRec = new GridPane();
                    centerPaneFriendRec.setAlignment(Pos.CENTER);
                    centerPaneFriendRec.setPadding(new Insets(10, 10, 10, 10));
                    centerPaneFriendRec.setHgap(10);
                    centerPaneFriendRec.setVgap(10);

                    // create labels and buttons
                    Label FriendsRecWelcome = new Label("Friend Recommendations");
                    Button FriendsRecGoBack = new Button("Go Back");
                    FriendsRecWelcome.setFont(new Font("Times New Roman", 20));
                    FriendsRecWelcome.setTextFill(Color.CRIMSON);

                    // add to centerpane
                    rootPaneFriendRec.setCenter(centerPaneFriendRec);
                    centerPaneFriendRec.add(FriendsRecWelcome, 0, 0);
                    centerPaneFriendRec.add(FriendsRecGoBack, 0, 11);

                    Scene FriendsRecScene = new Scene(rootPaneFriendRec, 700, 600);


                    //get all friends of this user's friends
                    List<Integer> friends_of_this_users_friends = new ArrayList<>();
                    try{
                        friends_of_this_users_friends = db.getAllFriendsOfFriends(curr_user);
                    }catch(SQLException e){
                        e.printStackTrace();
                    }

                    // add friend recommendations to pane
                    i = 1;
                    for (Integer user : friends_of_this_users_friends) {
                        
                        User u = null;
                        try {
                            u = db.getAllUserInfo(user);
                        } catch (SQLException e) {

                        }
                        centerPaneFriendRec.add(new Label(u.firstName), 0, i);
                        centerPaneFriendRec.add(new Label(u.lastName), 1, i);
                        i++;

                        if (i >= 11) {
                            break;
                        }

                    }

                    sm.friendRecScene = FriendsRecScene;

                    FriendsRecGoBack.setOnAction(ev -> {

                        sm.switchToFeed(false);

                    });


                /* browsing your profile */

                    // render a page for viewing your own profile
                    BorderPane rootPaneUserPage = new BorderPane();
                    GridPane centerPaneUserPage = new GridPane();

                    centerPaneUserPage.setAlignment(Pos.CENTER);
                    centerPaneUserPage.setPadding(new Insets(1, 1, 1, 1));
                    centerPaneUserPage.setHgap(10);
                    centerPaneUserPage.setVgap(10);

                    // create labels and buttons
                    Label YourPageWelcome = new Label("Your Page");
                    YourPageWelcome.setFont(Font.font("Times New Roman", FontPosture.REGULAR, 25));
                    YourPageWelcome.setTextFill(Color.HOTPINK);
                    Button UserPageGoBack = new Button("Go Back");
                    Button ChangeUserInfo = new Button("Change User Info");     

                    // add labels and such to panes
                    rootPaneUserPage.setCenter(centerPaneUserPage);
                    centerPaneUserPage.add(YourPageWelcome, 0, 0);
                    centerPaneUserPage.add(UserPageGoBack, 0, 17);
                    centerPaneUserPage.add(ChangeUserInfo, 0, 18);
                    
                    // create scene
                    Scene UserPageScene = new Scene(rootPaneUserPage, 700, 600);
                    
                    //in the case where you want to view your own profile, you can make the target user uid the current user
                    int target_user_uid = curr_user;
                    List<Album> this_users_albums = new ArrayList<>();
                    try{
                        this_users_albums = db.getAllAlbumsOfLoggedInUser(target_user_uid);
                    }catch(SQLException e){
                        e.printStackTrace();
                    }

                    // put album names onto page
                    i = 7;
                    for (Album album : this_users_albums) {
                        
                        centerPaneUserPage.add(new Label(album.albumName), 0, i);
                        i++;

                        if (i >= 17) {
                            break;
                        }

                    }

                    // put user info on pane
                    User user_info = new User();
                    try {
                        user_info = db.getAllUserInfo(target_user_uid);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

                    // get all the info from the user
                    String userFirstName = user_info.firstName;
                    String userLastName = user_info.lastName;
                    String userEmail = user_info.email;
                    String userGender = user_info.gender;
                    String userHometown = user_info.hometown;
                    String userDOB = user_info.dob;
                    
                    // add user info to centerPane
                    centerPaneUserPage.add(new Label("First Name: " + user_info.firstName), 0, 1);
                    centerPaneUserPage.add(new Label("Last Name: " + user_info.lastName), 0, 2);
                    centerPaneUserPage.add(new Label("Email: " + user_info.email), 0, 3);
                    centerPaneUserPage.add(new Label("Gender: " + user_info.gender), 0, 4);
                    centerPaneUserPage.add(new Label("Hometown: " + user_info.hometown), 0, 5);
                    centerPaneUserPage.add(new Label("Date of Birth: " + user_info.dob), 0, 6);

                    // display page
                    sm.userProfileScene = UserPageScene;

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

                    // let the user change their info if they press the changeinfo button
                    ChangeUserInfo.setOnAction(ev -> {

                        // create a new scene where the user can change their info
                        // should look similar to the registration page
                        BorderPane rootPaneChangeInfo = new BorderPane();
                        GridPane centerPaneChangeInfo = new GridPane();
                        centerPaneChangeInfo.setAlignment(Pos.CENTER);
                        centerPaneChangeInfo.setPadding(new Insets(10, 10, 10, 10));   // this is the spacing from the perimeter of the window
                        centerPaneChangeInfo.setHgap(10); // the spacing between objects horizontally
                        centerPaneChangeInfo.setVgap(10);  // the spacing between objects horizontally
                        
                        //Create 3 labels
                
                        Label FirstNameChangeInfo = new Label("FIRST NAME (*)");
                        Label LastNameChangeInfo = new Label("LAST NAME (*)");
                        Label PasswordChangeInfo = new Label("PASSWORD (*)");
                        Label GenderChangeInfo = new Label("GENDER");
                        Label HometownChangeInfo = new Label("HOMETOWN");
                        Label DOBChangeInfo = new Label("DATE OF BIRTH (*) (Format YYYY-MM-DD)");
                        Label ChangeInfoWelcome = new Label("Change User Info");
                        Button ChangeInfoButton = new Button("Set Changes");

                        ChangeInfoWelcome.setFont(new Font("Times New Roman", 20));   // double check the font--
                        ChangeInfoWelcome.setTextFill(Color.CRIMSON);

                        //Create text fields and dropdowns
                    
                        TextField FirstNameFieldChangeInfo = new TextField();
                        FirstNameFieldChangeInfo.setText(userFirstName);
                        TextField LastNameFieldChangeInfo = new TextField();
                        LastNameFieldChangeInfo.setText(userLastName);
                        TextField PasswordChangeInfoField = new TextField();
                        PasswordChangeInfoField.setText("********");
                        ComboBox GenderFieldChangeInfo = new ComboBox();
                        GenderFieldChangeInfo.getItems().addAll(
                            "Male",
                            "Female",
                            "Other"
                        );
                        TextField HometownFieldChangeInfo = new TextField();
                        HometownFieldChangeInfo.setText(userHometown);
                        TextField DOBFieldChangeInfo = new TextField();
                        DOBFieldChangeInfo.setText(userDOB);

                        //add the labels and text fields accordingly
                        rootPaneChangeInfo.setCenter(centerPaneChangeInfo);
                        centerPaneChangeInfo.add(FirstNameChangeInfo, 0, 1);     
                        centerPaneChangeInfo.add(FirstNameFieldChangeInfo, 0, 2);
                        centerPaneChangeInfo.add(LastNameChangeInfo, 0, 3);
                        centerPaneChangeInfo.add(LastNameFieldChangeInfo, 0, 4);
                        centerPaneChangeInfo.add(PasswordChangeInfo, 0, 5);
                        centerPaneChangeInfo.add(PasswordChangeInfoField, 0, 6);
                        centerPaneChangeInfo.add(GenderChangeInfo, 0, 7);
                        centerPaneChangeInfo.add(GenderFieldChangeInfo, 0, 8);
                        centerPaneChangeInfo.add(HometownChangeInfo, 0, 9);
                        centerPaneChangeInfo.add(HometownFieldChangeInfo, 0, 10);
                        centerPaneChangeInfo.add(DOBChangeInfo, 0, 11);
                        centerPaneChangeInfo.add(DOBFieldChangeInfo, 0, 12);
                        centerPaneChangeInfo.add(ChangeInfoWelcome, 0, 0);
                        centerPaneChangeInfo.add(ChangeInfoButton, 0, 13);

                        // Create a scene and place it in the stage
                    
                        Scene sceneChangeInfo = new Scene(rootPaneChangeInfo, 700, 600);    // also x and y correlated
                    
                        primaryStage.setTitle("Social Media App"); // Set the stage title
                        primaryStage.setScene(sceneChangeInfo); // Place the scene in the stage
                        primaryStage.show(); // Display the stage
                        
                        /* if user presses change info button, change info for the user */
                        Label ErrorMessageChangeInfo = new Label("");
                        ErrorMessageChangeInfo.setTextFill(Color.CRIMSON);
                        centerPaneChangeInfo.add(ErrorMessageChangeInfo, 0, 14);
                        
                        ChangeInfoButton.setOnAction(eve -> {
                        
                            /* user changes info */
                            String firstName = FirstNameFieldChangeInfo.getText();
                            String lastName = LastNameFieldChangeInfo.getText();
                            String password_unhashed = PasswordChangeInfoField.getText();
                            String gender = "";
                            gender += GenderFieldChangeInfo.getValue();

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

                            String hometown = HometownFieldChangeInfo.getText();
                            String dob = DOBFieldChangeInfo.getText();
                            String password_hashed = "";

                            // hash the password given
                            try {

                                MessageDigest digest = MessageDigest.getInstance("SHA-256");
                                byte[] encodedHash = digest.digest(password_unhashed.getBytes());
                                StringBuilder hexString = new StringBuilder();
                                for (int j = 0; j < encodedHash.length; j++) {
                                    String hex = Integer.toHexString(0xff & encodedHash[j]);
                                    if (hex.length() == 1) {
                                        hexString.append('0');
                                    }
                                    hexString.append(hex);
                                }

                                password_hashed = hexString.toString();
                            
                            } catch (NoSuchAlgorithmException e) {
                                System.out.println("No Such Algorithm Exception Thrown");
                            }

                            // try updating user info
                            boolean success = true;
                            try {
                                db.updateUser(curr_user, firstName, lastName, gender, hometown, dob);
                            } catch (SQLException e) {
                                ErrorMessageChangeInfo.setText("Invalid Email and/or Date of Birth Format. Try again.");
                                success = false;
                            }
                            
                            if (success) {
                                sm.switchToFeed(false);
                            }

                        });

                    });
                    
                    // go back to feedpage if goback button is pressed
                    UserPageGoBack.setOnAction(ev -> {

                        sm.switchToFeed(false);

                    });


                    /* 
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

                /* 
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

                /*
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

                /* searching for specific comments left by users */

                    // render page to display comments searched by user
                    BorderPane rootPaneSearchComments = new BorderPane();
                    GridPane centerPaneSearchComments = new GridPane();
                    centerPaneSearchComments.setAlignment(Pos.CENTER);
                    centerPaneSearchComments.setPadding(new Insets(10, 10, 10, 10));
                    centerPaneSearchComments.setHgap(10);
                    centerPaneSearchComments.setVgap(10);

                    // create labels and buttons for this page
                    Label SearchCommentsWelcome = new Label("Search for Users who Left a Certain Comment");
                    Label SearchForComment = new Label("Search For Comment:");
                    Button SearchForCommentButton = new Button("Search"); 
                    Button SearchCommentsGoBack = new Button("Go Back");
                    SearchCommentsWelcome.setFont(new Font("Times New Roman", 20));
                    SearchCommentsWelcome.setTextFill(Color.CRIMSON);

                    // create text field
                    TextField CommentToSearch = new TextField();

                    // add text fields, labels, and buttons to centerPane
                    rootPaneSearchComments.setCenter(centerPaneSearchComments);
                    centerPaneSearchComments.add(SearchCommentsWelcome, 0, 0);
                    centerPaneSearchComments.add(SearchForComment, 0, 1);
                    centerPaneSearchComments.add(CommentToSearch, 1, 1);
                    centerPaneSearchComments.add(SearchForCommentButton, 0, 2);
                    centerPaneSearchComments.add(SearchCommentsGoBack, 0, 13);

                    // create scene
                    Scene SearchCommentsScene = new Scene(rootPaneSearchComments, 700, 600);

                    sm.searchCommentsScene = SearchCommentsScene;

                    //search by comment when search button is pressed
                    SearchForCommentButton.setOnAction(ev -> {
                    
                        String search_comment = CommentToSearch.getText();
                        List<List<Object>> matches = new ArrayList<>();
                        try{
                            matches = db.searchCommentByText(search_comment);
                        }catch(SQLException e){
                            e.printStackTrace();
                        }

                        // place users that left comments on the panes
                        int k = 3;
                        for (List<Object> match : matches) {
                            
                            centerPaneSearchComments.add(new Label(match.toString()), 0, k);
                            k++;

                            if (k >= 13) {
                                break;
                            }

                        }

                    });

                    // go back to feedpage if goback button is pressed
                    SearchCommentsGoBack.setOnAction(ev -> {

                        sm.switchToFeed(false);

                    });

                /*
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
                /* 
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

            
            /* search for photos via tags */

                BorderPane rootPaneSearchTags = new BorderPane();
                GridPane centerPaneSearchTags = new GridPane();

                centerPaneSearchTags.setAlignment(Pos.CENTER); // centered alignment
                centerPaneSearchTags.setPadding(new Insets(1, 1, 1, 1)); // this is the spacing from the perimeter of the window
                centerPaneSearchTags.setHgap(10); // the spacing between objects horizontally
                centerPaneSearchTags.setVgap(10); // the spacing between objects horizontally

                Label TagSearch = new Label("Filter Photos via Tag");
                Label TagSearchDirections = new Label("Separate tags by space as so: red blue green yellow orange");
                TagSearch.setFont(Font.font("Times New Roman", FontPosture.REGULAR, 25));
                TagSearch.setTextFill(Color.HOTPINK);

                Button TagSearchButton = new Button("Search");
                Button OwnTagPhotos = new Button("View Only Your Photos Via Tag");
                Button TagSearchGoBack = new Button("Go Back");
                TextField TagSearchField = new TextField();
                
                // if someone clicked on a tag, fill the tag search bar with the tag
                if (clickedOnTag) {
                    TagSearchField.setText(tagToSearch);
                }

                // set scene
                rootPaneSearchTags.setCenter(centerPaneSearchTags);
                Scene SearchTagScene = new Scene(rootPaneSearchTags, 700, 600);
                sm.searchTagsScene = SearchTagScene;

                List<String> tags_to_search = new ArrayList<>();
                int photoToGetOthers = 0;
                int photoToGetOwn = 0;
                boolean showingOwnPhotos = false; 

                TagSearchButton.setOnAction(ev -> {

                    // extract tags from search field
                    String tagsFieldResult = TagSearchField.getText();
                    int n = 0;
                    for (int m = 0; m < tagsFieldResult.length(); m++) {
                        
                        if (tagsFieldResult.substring(m, m + 1).equals(" ")) {
                            tags_to_search.add(tagsFieldResult.substring(n, m));
                            n = m + 1;
                        } else if (m >= tagsFieldResult.length() - 1) {
                            tags_to_search.add(tagsFieldResult.substring(n, m + 1));
                        }

                    }

                    // populate an array with list of tags
                    String[] search_all_tags = new String[tags_to_search.size()];
                    n = 0;
                    for (String tag : tags_to_search) {
                        search_all_tags[n] = tag;
                        n++;
                    }

                    //search all photos (not this user's) by multiple tags
                    List<Integer> all_photos_with_tags = new ArrayList<>();
                    if (!showingOwnPhotos) {
                        try{
                            all_photos_with_tags = db.getPhotosByMultipleTags(search_all_tags, curr_user);
                        }catch(SQLException e){
                            e.printStackTrace();
                        }
                    } else {
                        try{
                            all_photos_with_tags = db.getPhotosByMultipleTagsAndUser(search_all_tags, curr_user);
                        }catch(SQLException e){
                            e.printStackTrace();
                        }
                    }

                    
                    String url = "";
                    String poster_firstname = "";
                    String poster_lastname = "";
                    String caption = "";
                    List<Tag> tags = new ArrayList<>();
                    List<Pair<String, Comment>> comments = new ArrayList<>();

                    List<User> likers = new ArrayList<>();
                    int total_likes = 0;
                    User poster = new User();;
                    Photo pic = new Photo();

                    if (!showingOwnPhotos) {
                        try {
                            pic = db.fetchPhotoInfo(all_photos_with_tags.get(photoToGetOthers));
                            comments = Main.db.fetchPhotoComments(all_photos_with_tags.get(photoToGetOthers));
                            likers = Main.db.fetchPhotoLikers(all_photos_with_tags.get(photoToGetOthers));
                            tags = Main.db.fetchPhotoTags(all_photos_with_tags.get(photoToGetOthers));
                            poster = Main.db.fetchPhotoUser(all_photos_with_tags.get(photoToGetOthers));
                        } catch (SQLException e) {

                        }
                    } else {
                        try {
                            pic = db.fetchPhotoInfo(all_photos_with_tags.get(photoToGetOwn));
                            comments = Main.db.fetchPhotoComments(all_photos_with_tags.get(photoToGetOwn));
                            likers = Main.db.fetchPhotoLikers(all_photos_with_tags.get(photoToGetOwn));
                            tags = Main.db.fetchPhotoTags(all_photos_with_tags.get(photoToGetOwn));
                            poster = Main.db.fetchPhotoUser(all_photos_with_tags.get(photoToGetOwn));
                        } catch (SQLException e) {

                        }
                    }

                    poster_firstname = poster.firstName;
                    poster_lastname = poster.lastName;

                    caption = pic.caption;
                    url = pic.url;

                    total_likes = likers.size();
                    
                    InputStream is = Main.class.getClassLoader().getResourceAsStream(url);
                    Image image = new Image(is);

                    ImageView view = new ImageView(image);

                    view.setX(25);
                    view.setY(25);

                    view.setFitHeight(100);
                    view.setFitWidth(100);

                    view.setPreserveRatio(true);

                    Label comment0 = new Label("Comments");

                    comment0.setFont(Font.font("Verdana", FontPosture.REGULAR, 15));
                    comment0.setTextFill(Color.INDIGO);

                    Label comment1 = new Label("");
                    comment1.setFont(Font.font("Verdana", FontPosture.REGULAR, 10));
                    comment1.setTextFill(Color.BLACK);
                    
                    Label numLikes = new Label(total_likes + " Likes");  // NOT SURE WHERE TO GET LIKES FROM

                    numLikes.setFont(Font.font("Verdana", FontPosture.REGULAR, 15));
                    numLikes.setTextFill(Color.TEAL);

                    HBox hBox2 = new HBox();
                    hBox2.setPadding(new Insets(10, 10, 10, 10));
                    hBox2.setSpacing(5);

                    hBox2.setAlignment(Pos.CENTER_RIGHT);
                    hBox2.getChildren().add(view);

                    Button next = new Button("Next pic");
                    next.setMaxSize(100.0, 100.0);
                    rootPaneSearchTags.setBottom(next);

                    VBox vBox = new VBox();
                    vBox.setPadding(new Insets(10, 10, 10, 20));
                    vBox.setSpacing(5);
                    // vBox.setAlignment(Pos.CENTER);
                    vBox.getChildren().addAll(hBox2, numLikes, comment0, comment1);

                    for (int p = 0; p < comments.size(); p++) {
                        Pair<String, Comment> full_comment = comments.get(p);
                        String comm = full_comment.getKey() + " " + full_comment.getValue().text;
                        vBox.getChildren().add(new Label(comm));
                    }

                    Scene showPhotosViaTagFilterScene = new Scene(rootPaneSearchTags, 700, 600);
                    primaryStage.setTitle("Social Media App");
                    primaryStage.setScene(showPhotosViaTagFilterScene);
                    primaryStage.show();

                    next.setOnAction(eve -> {
                        /*
                        String urlNext = "";
                        String poster_firstnameNext = "";
                        String poster_lastnameNext = "";
                        String captionNext = "";
                        List<Tag> tagsNext = new ArrayList<>();
                        List<Pair<String, Comment>> commentsNext = new ArrayList<>();

                        List<User> likersNext = new ArrayList<>();
                        int total_likesNext = 0;
                        User posterNext = new User();;
                        Photo picNext = new Photo();

                        if (!showingOwnPhotos) {
                            try {
                                picNext = db.fetchPhotoInfo(all_photos_with_tags.get(photoToGetOthers + 1));
                                commentsNext = Main.db.fetchPhotoComments(all_photos_with_tags.get(photoToGetOthers + 1));
                                likersNext = Main.db.fetchPhotoLikers(all_photos_with_tags.get(photoToGetOthers + 1));
                                tagsNext = Main.db.fetchPhotoTags(all_photos_with_tags.get(photoToGetOthers + 1));
                                posterNext = Main.db.fetchPhotoUser(all_photos_with_tags.get(photoToGetOthers + 1));
                            } catch (SQLException e) {

                            }
                        } else {
                            try {
                                picNext = db.fetchPhotoInfo(all_photos_with_tags.get(photoToGetOwn + 1));
                                commentsNext = Main.db.fetchPhotoComments(all_photos_with_tags.get(photoToGetOwn + 1));
                                likersNext = Main.db.fetchPhotoLikers(all_photos_with_tags.get(photoToGetOwn + 1));
                                tagsNext = Main.db.fetchPhotoTags(all_photos_with_tags.get(photoToGetOwn + 1));
                                posterNext = Main.db.fetchPhotoUser(all_photos_with_tags.get(photoToGetOwn + 1));
                            } catch (SQLException e) {

                            }
                        }

                        poster_firstnameNext = posterNext.firstName;
                        poster_lastnameNext = posterNext.lastName;

                        captionNext = picNext.caption;
                        urlNext = picNext.url;

                        total_likesNext = likersNext.size();
                        
                        InputStream isNext = Main.class.getClassLoader().getResourceAsStream(urlNext);
                        Image imageNext = new Image(isNext);

                        ImageView viewNext = new ImageView(imageNext);

                        viewNext.setX(25);
                        viewNext.setY(25);

                        viewNext.setFitHeight(100);
                        viewNext.setFitWidth(100);

                        viewNext.setPreserveRatio(true);

                        Label comment0Next = new Label("Comments");

                        comment0Next.setFont(Font.font("Verdana", FontPosture.REGULAR, 15));
                        comment0Next.setTextFill(Color.INDIGO);

                        Label comment1Next = new Label("");
                        comment1Next.setFont(Font.font("Verdana", FontPosture.REGULAR, 10));
                        comment1Next.setTextFill(Color.BLACK);
                        
                        Label numLikesNext = new Label(total_likesNext + " Likes");  // NOT SURE WHERE TO GET LIKES FROM

                        numLikesNext.setFont(Font.font("Verdana", FontPosture.REGULAR, 15));
                        numLikesNext.setTextFill(Color.TEAL);

                        HBox hBox2Next = new HBox();
                        hBox2Next.setPadding(new Insets(10, 10, 10, 10));
                        hBox2Next.setSpacing(5);

                        hBox2Next.setAlignment(Pos.CENTER_RIGHT);
                        hBox2Next.getChildren().add(view);

                        VBox vBoxNext = new VBox();
                        vBoxNext.setPadding(new Insets(10, 10, 10, 20));
                        vBoxNext.setSpacing(5);
                        // vBox.setAlignment(Pos.CENTER);
                        vBoxNext.getChildren().addAll(hBox2Next, numLikesNext, comment0Next, comment1Next);

                        for (int p = 0; p < comments.size(); p++) {
                            Pair<String, Comment> full_comment = comments.get(p);
                            String comm = full_comment.getKey() + " " + full_comment.getValue().text;
                            vBox.getChildren().add(new Label(comm));
                        }

                        Scene showPhotosViaTagFilterSceneNext = new Scene(rootPaneSearchTags, 700, 600);
                        primaryStage.setTitle("Social Media App");
                        primaryStage.setScene(showPhotosViaTagFilterSceneNext);
                        primaryStage.show();
                        */
                    });
                    
                }); 

                /*
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

            /* most popular tags */

                    // render a page to show friend recommendations
                    BorderPane rootPaneMostPopTags = new BorderPane();
                    GridPane centerPaneMostPopTags = new GridPane();
                    centerPaneMostPopTags.setAlignment(Pos.CENTER);
                    centerPaneMostPopTags.setPadding(new Insets(10, 10, 10, 10));
                    centerPaneMostPopTags.setHgap(10);
                    centerPaneMostPopTags.setVgap(10);

                    // create labels and buttons
                    Label MostPopTagsWelcome = new Label("Most Popular Tags");
                    Button MostPopTagsGoBack = new Button("Go Back");
                    MostPopTagsWelcome.setFont(new Font("Times New Roman", 20));
                    Button MostPopTag1Button = new Button("Go To Tag Photos");
                    Button MostPopTag2Button = new Button("Go To Tag Photos");
                    Button MostPopTag3Button = new Button("Go To Tag Photos");
                    Button MostPopTag4Button = new Button("Go To Tag Photos");
                    Button MostPopTag5Button = new Button("Go To Tag Photos");
                    Button MostPopTag6Button = new Button("Go To Tag Photos");
                    Button MostPopTag7Button = new Button("Go To Tag Photos");
                    Button MostPopTag8Button = new Button("Go To Tag Photos");
                    Button MostPopTag9Button = new Button("Go To Tag Photos");
                    Button MostPopTag10Button = new Button("Go To Tag Photos");
                    MostPopTagsWelcome.setTextFill(Color.CRIMSON);

                    // add to centerpane
                    rootPaneMostPopTags.setCenter(centerPaneMostPopTags);
                    centerPaneMostPopTags.add(MostPopTagsWelcome, 0, 0);
                    centerPaneMostPopTags.add(MostPopTag1Button, 1, 1); 
                    centerPaneMostPopTags.add(MostPopTag2Button, 1, 2);
                    centerPaneMostPopTags.add(MostPopTag3Button, 1, 3);
                    centerPaneMostPopTags.add(MostPopTag4Button, 1, 4);
                    centerPaneMostPopTags.add(MostPopTag5Button, 1, 5);
                    centerPaneMostPopTags.add(MostPopTag6Button, 1, 6);
                    centerPaneMostPopTags.add(MostPopTag7Button, 1, 7);
                    centerPaneMostPopTags.add(MostPopTag8Button, 1, 8);
                    centerPaneMostPopTags.add(MostPopTag9Button, 1, 9);
                    centerPaneMostPopTags.add(MostPopTag10Button, 1, 10);
                    centerPaneMostPopTags.add(MostPopTagsGoBack, 0, 11);

                    Scene MostPopTagsScene = new Scene(rootPaneMostPopTags, 700, 600);

                    //search most popular tags
                    List<String> most_popular_tags = new ArrayList<>();
                    try{
                        most_popular_tags = db.getMostPopularTags();
                    }catch(SQLException e){
                        e.printStackTrace();
                    }

                    i = 1;
                    String[] tags = new String[10];
                    for (String tag: most_popular_tags) {

                        centerPaneMostPopTags.add(new Label(tag), 0, i);
                        tags[i - 1] = tag;
                        i++;

                        if (i >= 11) {
                            break;
                        }

                    }

                    sm.mostPopTagsScene = MostPopTagsScene;

                    /* if a tag is selected, switch to photos of tag */

                    MostPopTag1Button.setOnAction(ev -> {

                        tagToSearch = tags[0];
                        clickedOnTag = true;
                        sm.switchToTagSearch();

                    });

                    MostPopTag2Button.setOnAction(ev -> {

                        tagToSearch = tags[1];
                        clickedOnTag = true;
                        sm.switchToTagSearch();

                    });

                    MostPopTag3Button.setOnAction(ev -> {

                        tagToSearch = tags[2];
                        clickedOnTag = true;
                        sm.switchToTagSearch();

                    });

                    MostPopTag4Button.setOnAction(ev -> {

                        tagToSearch = tags[3];
                        clickedOnTag = true;
                        sm.switchToTagSearch();

                    });

                    MostPopTag5Button.setOnAction(ev -> {

                        tagToSearch = tags[4];
                        clickedOnTag = true;
                        sm.switchToTagSearch();

                    });

                    MostPopTag6Button.setOnAction(ev -> {

                        tagToSearch = tags[5];
                        clickedOnTag = true;
                        sm.switchToTagSearch();

                    });

                    MostPopTag7Button.setOnAction(ev -> {

                        tagToSearch = tags[6];
                        clickedOnTag = true;
                        sm.switchToTagSearch();

                    });

                    MostPopTag8Button.setOnAction(ev -> {

                        tagToSearch = tags[7];
                        clickedOnTag = true;
                        sm.switchToTagSearch();

                    });

                    MostPopTag9Button.setOnAction(ev -> {

                        tagToSearch = tags[8];
                        clickedOnTag = true;
                        sm.switchToTagSearch();

                    });

                    MostPopTag10Button.setOnAction(ev -> {

                        tagToSearch = tags[9];
                        clickedOnTag = true;
                        sm.switchToTagSearch();

                    });


                    MostPopTagsGoBack.setOnAction(ev -> {

                       sm.switchToFeed(false); 

                    });
                    


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
