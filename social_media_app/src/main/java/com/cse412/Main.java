package com.cse412;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.util.Pair;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.paint.Color;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.sql.*;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.ArrayList;
import java.util.HashMap;
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
                        curr_user = db.loginUser(email, password_hashed);
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

                    Button buttonToAddFriend1 = new Button("Add Friend");
                    Button buttonToAddFriend2 = new Button("Add Friend");
                    Button buttonToAddFriend3 = new Button("Add Friend");
                    Button buttonToAddFriend4 = new Button("Add Friend");
                    Button buttonToAddFriend5 = new Button("Add Friend");
                    Button buttonToAddFriend6 = new Button("Add Friend");
                    Button buttonToAddFriend7 = new Button("Add Friend");
                    Button buttonToAddFriend8 = new Button("Add Friend");
                    Button buttonToAddFriend9 = new Button("Add Friend");
                    Button buttonToAddFriend10 = new Button("Add Friend");
                    

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

                        // add buttons to centerPane
                        centerPaneSearchUsers.add(buttonToAddFriend1, 2, 6);
                        centerPaneSearchUsers.add(buttonToAddFriend2, 2, 7);
                        centerPaneSearchUsers.add(buttonToAddFriend3, 2, 8);
                        centerPaneSearchUsers.add(buttonToAddFriend4, 2, 9);
                        centerPaneSearchUsers.add(buttonToAddFriend5, 2, 10);
                        centerPaneSearchUsers.add(buttonToAddFriend6, 2, 11);
                        centerPaneSearchUsers.add(buttonToAddFriend7, 2, 12);
                        centerPaneSearchUsers.add(buttonToAddFriend8, 2, 13);
                        centerPaneSearchUsers.add(buttonToAddFriend9, 2, 14);
                        centerPaneSearchUsers.add(buttonToAddFriend10, 2, 15);

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

                    // if a user presses on a button, friend the associated user
                    buttonToAddFriend1.setOnAction(ev -> {
                            
                        String search_firstname = SearchUserFirstNameField.getText();
                        String search_lastname = SearchUserLastNameField.getText();
                        List<Integer> uids_of_searched_name = new ArrayList<>();

                        try{
                            uids_of_searched_name = db.findUsersByName(search_firstname, search_lastname);
                            User u = null;

                            if (uids_of_searched_name.size() > 0) {
                                System.out.println(uids_of_searched_name.get(0));
                                u = db.getAllUserInfo(uids_of_searched_name.get(0));
                                System.out.println(u.uid);
                            }
                            
                            db.recordFriendship(curr_user, u.uid);

                        } catch (SQLException e) {
                            
                        }    

                    });

                    buttonToAddFriend2.setOnAction(ev -> {
                            
                        String search_firstname = SearchUserFirstNameField.getText();
                        String search_lastname = SearchUserLastNameField.getText();
                        List<Integer> uids_of_searched_name = new ArrayList<>();

                        try{
                            uids_of_searched_name = db.findUsersByName(search_firstname, search_lastname);
                            User u = null;

                            if (uids_of_searched_name.size() > 1) {
                                u = db.getAllUserInfo(uids_of_searched_name.get(1));
                            }

                            db.recordFriendship(curr_user, u.uid);

                        } catch (SQLException e) {
                            
                        }    

                    });

                    buttonToAddFriend3.setOnAction(ev -> {
                            
                        String search_firstname = SearchUserFirstNameField.getText();
                        String search_lastname = SearchUserLastNameField.getText();
                        List<Integer> uids_of_searched_name = new ArrayList<>();

                        try{
                            uids_of_searched_name = db.findUsersByName(search_firstname, search_lastname);
                            User u = null;

                            if (uids_of_searched_name.size() > 2) {
                                u = db.getAllUserInfo(uids_of_searched_name.get(2));
                            }

                            db.recordFriendship(curr_user, u.uid);

                        } catch (SQLException e) {
                            
                        }    

                    });

                    buttonToAddFriend4.setOnAction(ev -> {
                            
                        String search_firstname = SearchUserFirstNameField.getText();
                        String search_lastname = SearchUserLastNameField.getText();
                        List<Integer> uids_of_searched_name = new ArrayList<>();

                        try{
                            uids_of_searched_name = db.findUsersByName(search_firstname, search_lastname);
                            User u = null;

                            if (uids_of_searched_name.size() > 3) {
                                u = db.getAllUserInfo(uids_of_searched_name.get(3));
                            }

                            db.recordFriendship(curr_user, u.uid);

                        } catch (SQLException e) {
                            
                        }    

                    });

                    buttonToAddFriend5.setOnAction(ev -> {
                            
                        String search_firstname = SearchUserFirstNameField.getText();
                        String search_lastname = SearchUserLastNameField.getText();
                        List<Integer> uids_of_searched_name = new ArrayList<>();

                        try{
                            uids_of_searched_name = db.findUsersByName(search_firstname, search_lastname);
                            User u = null;

                            if (uids_of_searched_name.size() > 4) {
                                u = db.getAllUserInfo(uids_of_searched_name.get(4));
                            }

                            db.recordFriendship(curr_user, u.uid);

                        } catch (SQLException e) {
                            
                        }    

                    });

                    buttonToAddFriend6.setOnAction(ev -> {
                            
                        String search_firstname = SearchUserFirstNameField.getText();
                        String search_lastname = SearchUserLastNameField.getText();
                        List<Integer> uids_of_searched_name = new ArrayList<>();

                        try{
                            uids_of_searched_name = db.findUsersByName(search_firstname, search_lastname);
                            User u = null;

                            if (uids_of_searched_name.size() > 5) {
                                u = db.getAllUserInfo(uids_of_searched_name.get(5));
                            }

                            db.recordFriendship(curr_user, u.uid);

                        } catch (SQLException e) {
                            
                        }    

                    });

                    buttonToAddFriend7.setOnAction(ev -> {
                            
                        String search_firstname = SearchUserFirstNameField.getText();
                        String search_lastname = SearchUserLastNameField.getText();
                        List<Integer> uids_of_searched_name = new ArrayList<>();

                        try{
                            uids_of_searched_name = db.findUsersByName(search_firstname, search_lastname);
                            User u = null;

                            if (uids_of_searched_name.size() > 6) {
                                u = db.getAllUserInfo(uids_of_searched_name.get(6));
                            }

                            db.recordFriendship(curr_user, u.uid);

                        } catch (SQLException e) {
                            
                        }    

                    });
                    
                    buttonToAddFriend8.setOnAction(ev -> {
                            
                        String search_firstname = SearchUserFirstNameField.getText();
                        String search_lastname = SearchUserLastNameField.getText();
                        List<Integer> uids_of_searched_name = new ArrayList<>();

                        try{
                            uids_of_searched_name = db.findUsersByName(search_firstname, search_lastname);
                            User u = null;

                            if (uids_of_searched_name.size() > 7) {
                                u = db.getAllUserInfo(uids_of_searched_name.get(7));
                            }

                            db.recordFriendship(curr_user, u.uid);

                        } catch (SQLException e) {
                            
                        }    

                    });

                    buttonToAddFriend9.setOnAction(ev -> {
                            
                        String search_firstname = SearchUserFirstNameField.getText();
                        String search_lastname = SearchUserLastNameField.getText();
                        List<Integer> uids_of_searched_name = new ArrayList<>();

                        try{
                            uids_of_searched_name = db.findUsersByName(search_firstname, search_lastname);
                            User u = null;

                            if (uids_of_searched_name.size() > 8) {
                                u = db.getAllUserInfo(uids_of_searched_name.get(8));
                            }

                            db.recordFriendship(curr_user, u.uid);

                        } catch (SQLException e) {
                            
                        }    

                    });

                    buttonToAddFriend10.setOnAction(ev -> {
                            
                        String search_firstname = SearchUserFirstNameField.getText();
                        String search_lastname = SearchUserLastNameField.getText();
                        List<Integer> uids_of_searched_name = new ArrayList<>();

                        try{
                            uids_of_searched_name = db.findUsersByName(search_firstname, search_lastname);
                            User u = null;

                            if (uids_of_searched_name.size() > 9) {
                                u = db.getAllUserInfo(uids_of_searched_name.get(9));
                            }

                            db.recordFriendship(curr_user, u.uid);

                        } catch (SQLException e) {
                            
                        }    

                    });

                    // go back to feedPage
                    GoBackSearchUsers.setOnAction(ev -> {

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
                centerPaneSearchTags.add(TagSearch, 0, 0);
                centerPaneSearchTags.add(TagSearchDirections, 0, 1);
                centerPaneSearchTags.add(TagSearchButton, 0, 3);
                centerPaneSearchTags.add(OwnTagPhotos, 0, 4);
                centerPaneSearchTags.add(TagSearchGoBack, 0, 5);
                centerPaneSearchTags.add(TagSearchField, 0, 2);

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

                // if go back button pressed, go back to feed
                TagSearchGoBack.setOnAction(ev -> {

                    sm.switchToFeed(false);

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
            }
            /*for(Pair<Integer,Integer> pic : top_pics){
                    System.out.println(pic.getKey() + " matches " + pic.getValue() + " tags");
                }       */

                /* creating_entries.sql */
                /*these vars need to be populated by labels/buttons above the db function calls, besides the holder for retrieved data 
                (none here since nothing is returned by the db function */
                /* 
                
                
                String new_tag_word = "blah";
                int pid_tagging = 9990;
                try{
                    db.createTag(pid_tagging, new_tag_word);
                } catch(SQLException e){
                    e.printStackTrace();
                }


                /* deletion.sql */

                //coomented this out because it deletes the test user when trying to test all my functions
                /* 
                
                
                int deleting_aid = 5239;
                try{
                    db.deleteAlbum(deleting_aid, curr_user);
                }catch(SQLException e){
                    e.printStackTrace();
                }
                
                
                int unlike_pid = 9990;
                try{
                    db.deleteLike(curr_user, unlike_pid);
                }catch(SQLException e){
                    e.printStackTrace();
                }
            }*/
            
        
    

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
