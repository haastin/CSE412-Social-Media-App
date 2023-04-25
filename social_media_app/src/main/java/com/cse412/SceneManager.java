package com.cse412;

import javafx.scene.Scene;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.FileChooser.ExtensionFilter;
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
import javafx.scene.control.ButtonBar.ButtonData;
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

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

import javafx.collections.*;
import javafx.scene.text.Text.*;
import javafx.scene.paint.*;
import javafx.scene.text.*;
import java.sql.*;
import java.util.Random;

import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Optional;
import java.util.PriorityQueue;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;

public class SceneManager {

    public Scene loginScene;
    public Scene feedScene;
    public Scene topTenUsersScene;
    public Scene searchForUsersScene;
    public Scene displayUserFriendsScene;
    public Scene displayFriendsOfUsersScene;
    public Scene friendRecScene;
    public Scene userProfileScene;
    public Scene searchCommentsScene;
    public Scene mostPopTagsScene;
    private Stage stage;

    public SceneManager(Stage stage) {
        this.stage = stage;
    }

    public void switchToLogin() {
        // Switch to scene1
        // Stage stage = (Stage) feedScene.getWindow();
        stage.setScene(loginScene);
    }

    public void switchToFeed() {
        // Switch to scene2
        // Stage stage = (Stage) loginScene.getWindow();

        /*
         * browing_homepage.sql and render_photo.sql
         * this section is for rendering a photo. the variables before the try statement
         * are what
         * you can use in the UI
         */
        String url = "";
        String poster_firstname = "";
        String poster_lastname = "";
        String caption = "";
        List<Tag> tags = new ArrayList<>();
        List<Pair<String, Comment>> comments = new ArrayList<>(); // String is firstName + LastName, Comment has all the
                                                                  // fields to render for a comment
        List<User> likers = new ArrayList<>();
        int total_likes = 0;

        int random_pid = 0;
        /*
         * code below gets a random pid from all users not logged in and randomly
         * chooses a row and fetches its pid
         */
        try {
            List<Integer> pids = Main.db.getAllPidsOfUsersNotLoggedIn(Main.curr_user);
            int rows = pids.size();
            Random rando = new Random();
            int random_row = rando.nextInt(rows);
            random_pid = pids.get(random_row);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println(random_pid);
        /* here we get all the info from the randomly fetched pic */
        try {
            Photo pic = Main.db.fetchPhotoInfo(random_pid);
            comments = Main.db.fetchPhotoComments(random_pid);
            likers = Main.db.fetchPhotoLikers(random_pid);
            tags = Main.db.fetchPhotoTags(random_pid);
            User poster = Main.db.fetchPhotoUser(random_pid);

            poster_firstname = poster.firstName;
            poster_lastname = poster.lastName;

            caption = pic.caption;
            url = pic.url;

            total_likes = likers.size();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        BorderPane rootPane2 = new BorderPane();
        GridPane centerPane2 = new GridPane();

        centerPane2.setAlignment(Pos.CENTER); // centered alignment
        centerPane2.setPadding(new Insets(1, 1, 1, 1)); // this is the spacing from the perimeter of the window
        centerPane2.setHgap(10); // the spacing between objects horizontally
        centerPane2.setVgap(10); // the spacing between objects horizontally

        Label feed = new Label("Feed");

        feed.setFont(Font.font("Times New Roman", FontPosture.REGULAR, 25));
        feed.setTextFill(Color.HOTPINK);

        Button ownPhotos = new Button("View Your Photos");
        ownPhotos.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        ownPhotos.setStyle("-fx-background-color: TRANSPARENT");

        TextField search = new TextField();
        search.setPromptText("Search Here");
        search.setPrefWidth(250);
        search.setPrefHeight(40);

        ComboBox suggest = new ComboBox();
        suggest.getItems().addAll(
                "Top Ten Users",
                "Most Popular Tags",
                "Your Friends",
                "Users Who Have You As A Friend",
                "Search for Users",
                "Search for Tags",
                "Search for Comments",
                "Recommended Friends",
                "Edit Your Page",
                "You May Also Like");

        suggest.setPromptText("Suggestions");
        /*
         * suggest.getEditor().textProperty().addListener(new ChangeListener<String>() {
         * 
         * @Override
         * public void changed(ObservableValue<? extends String> observable, String
         * oldValue, String newValue) {
         * System.out.println("yuh");
         * }
         * });
         */

        /*
         * URL url = getClass().getResource("/drawIcon.png");
         * Image image = ImageIO.read(url);
         * In case you want to create a javafx Image:
         * Image image = new Image("/drawIcon.png");
         */

        // =================================================================================

        /*
         * //Passing FileInputStream object as a parameter
         * FileInputStream inputstream = new FileInputStream("C:\\images\\image.jpg");
         * Image image = new Image(inputstream);
         * 
         * //Loading image from URL
         * //Image image = new Image(new FileInputStream("url for the image"));
         * -----------------------------------------------------------------------------
         * ----
         * 
         * //Creating an image
         * Image image = new Image(new FileInputStream("path of the image"));
         * 
         * //Setting the image view
         * ImageView imageView = new ImageView(image);
         */
        System.out.println(Main.curr_user);
        System.out.println(url);
        // String imagePath = "test.jpeg";
        InputStream is = Main.class.getClassLoader().getResourceAsStream(url);
        Image image = new Image(is);

        ImageView view = new ImageView(image);

        view.setX(25); // double check these dimensions
        view.setY(25);

        view.setFitHeight(100);
        view.setFitWidth(100);

        view.setPreserveRatio(true);

        /*
         * TextField temp = new TextField();
         * temp.setPromptText("(Temporary TextField --> In place of picture)");
         * temp.setPrefWidth(100);
         * temp.setPrefHeight(400);
         */

        Button searchIt = new Button("Search");
        searchIt.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        searchIt.setStyle("-fx-background-color: LIGHTGREEN");

        Button clear = new Button("Clear");
        clear.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        clear.setStyle("-fx-background-color: LIGHTSALMON");

        HBox hBox = new HBox();
        hBox.setPadding(new Insets(10, 10, 10, 10));
        hBox.setSpacing(5);
        hBox.setAlignment(Pos.CENTER);
        hBox.getChildren().addAll(suggest, search, searchIt, clear);

        Label comment0 = new Label("Comments");

        comment0.setFont(Font.font("Verdana", FontPosture.REGULAR, 15));
        comment0.setTextFill(Color.INDIGO);

        Label comment1 = new Label("");
        comment1.setFont(Font.font("Verdana", FontPosture.REGULAR, 10));
        comment1.setTextFill(Color.BLACK);

        /*
         * for (i = 0; i < sizeOf(comments); i++) {
         * comment1.setText(comments[i] + "\n");
         * }
         */

        /*
         * Label comment2 = new Label("[Insert Comment2]");
         * comment2.setFont(Font.font("Verdana", FontPosture.REGULAR, 10));
         * comment2.setTextFill(Color.TRANSPARENT);
         */

        /*
         * Random rand = new Random();
         * int upperbound = 301;
         * int randNum = rand.nextInt(upperbound);
         */

        Label numLikes = new Label(total_likes + " Likes");

        numLikes.setFont(Font.font("Verdana", FontPosture.REGULAR, 15));
        numLikes.setTextFill(Color.TEAL);

        HBox hBox2 = new HBox();
        hBox2.setPadding(new Insets(10, 10, 10, 10));
        hBox2.setSpacing(5);

        hBox2.setAlignment(Pos.CENTER_RIGHT);
        hBox2.getChildren().add(view); // adding image to Hbox

        Button logOut = new Button("Log Out");
        logOut.setMaxSize(100.0, 100.0);

        Button next = new Button("Next pic");
        next.setMaxSize(100.0, 100.0);
        rootPane2.setBottom(next);

        HBox hBox3 = new HBox();
        hBox3.setPadding(new Insets(10, 10, 10, 10));
        hBox3.setSpacing(20);
        hBox3.setAlignment(Pos.CENTER);
        hBox3.getChildren().addAll(ownPhotos, logOut); // adding image to Hbox

        VBox vBox = new VBox();
        vBox.setPadding(new Insets(10, 10, 10, 20));
        vBox.setSpacing(5);
        // vBox.setAlignment(Pos.CENTER);
        vBox.getChildren().addAll(feed, hBox3, hBox, hBox2, numLikes, comment0, comment1);

        for (int i = 0; i < comments.size(); i++) {
            Pair<String, Comment> full_comment = comments.get(i);
            String comm = full_comment.getKey() + " " + full_comment.getValue().text;
            vBox.getChildren().add(new Label(comm));
        }

        rootPane2.setCenter(vBox);

        // Create a scene and place it in the stage
        Scene feedScene = new Scene(rootPane2, 700, 600); // also x and y correlated
        this.feedScene = feedScene;

        stage.setTitle("Social Media App"); // Set the stage title
        stage.setScene(feedScene); // Place the scene in the stage
        stage.show(); // Display the stage

        next.setOnAction((ActionEvent h) -> {
            this.switchToFeed();
        });

        // comboBox listeners below

        EventHandler<ActionEvent> event = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {

                if (suggest.getValue().toString().equals("Top Ten Users")) {
                    stage.setScene(topTenUsersScene); // change to top ten users page
                    stage.show();
                }

                else if (suggest.getValue().toString().equals("Search for Users")) {
                    stage.setScene(searchForUsersScene);
                    stage.show();
                }

                else if (suggest.getValue().toString().equals("Your Friends")) {
                    stage.setScene(displayUserFriendsScene);
                    stage.show();

                }

                else if (suggest.getValue().toString().equals("Users Who Have You As A Friend")) {
                    stage.setScene(displayFriendsOfUsersScene);
                    stage.show();
                }

                else if (suggest.getValue().toString().equals("Recommended Friends")) {
                    stage.setScene(friendRecScene); // change to recommended friends page
                    stage.show();
                }

                else if (suggest.getValue().toString().equals("Search for Tags")) {
                    stage.setScene(feedScene);
                }

                else if (suggest.getValue().toString().equals("Search for Comments")) {
                    stage.setScene(searchCommentsScene);
                    stage.show();
                }

                else if (suggest.getValue().toString().equals("Most Popular Tags")) {
                    stage.setScene(mostPopTagsScene); // change to most popular tags page
                    stage.show();
                }

                else if (suggest.getValue().toString().equals("Your Page")) {
                    stage.setScene(userProfileScene);
                    stage.show();
                }

                else if (suggest.getValue().toString().equals("You May Also Like")) {
                    stage.setScene(feedScene); // change to you may also like page
                }

            }
        };

        suggest.setOnAction(event);

        // button listener below

        searchIt.setOnAction((ActionEvent f) -> {
            // primaryStage.setScen suggest-scene); // based on the scene names of th
            // suggest pg
        });

        ownPhotos.setOnAction(event);

        /*
         * clear.setOnAction((ActionEvent g) -> {
         * // suggest.setPromptText("Suggestions");
         * primaryStage.setScene(feedScene); // refreshes the page basically
         * 
         * });
         */

        logOut.setOnAction((ActionEvent h) -> {
            this.switchToLogin();
            // primaryStage.setScene(scene1); // based on the scene name of the welcome pg
        });

        // Create a Button object
        Button button = new Button("Select Image");
        rootPane2.setRight(button);

        // Set the action for the button
        button.setOnAction(e -> {
            // Create a FileChooser object
            FileChooser fileChooser = new FileChooser();

            // Set the title of the dialog box
            fileChooser.setTitle("Select Image");

            // Set the initial directory for the dialog box
            fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));

            // Set the file extensions that are allowed to be selected
            fileChooser.getExtensionFilters().addAll(
                    new ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif"));

            // Open the dialog box and wait for the user to select a file
            File selectedFile = fileChooser.showOpenDialog(stage);

            // Define the destination directory for the file
            File destDir = new File("src/main/java");

            // If the destination directory doesn't exist, create it
            if (!destDir.exists()) {
                destDir.mkdirs();
            }

            // Get the title of the selected file
            String title = selectedFile.getName();

            // Create a Path object for the destination directory and the selected file
            Path destPath = destDir.toPath().resolve(title);
            Path srcPath = selectedFile.toPath();

            // Copy the selected file to the destination directory

            // If a file was selected, do something with it
            if (selectedFile != null) {
                // Upload the file to your app or display it in a ImageView, for example:
                Image up_image = new Image(selectedFile.toURI().toString());
                ImageView imageView = new ImageView(up_image);
                int target_user_uid = Main.curr_user;
                List<Album> this_users_albums = new ArrayList<>();
                try {
                    this_users_albums = Main.db.getAllAlbumsOfLoggedInUser(target_user_uid);
                } catch (SQLException q) {
                    q.printStackTrace();
                }

                List<Album> albums = new ArrayList<>(this_users_albums);
                int aid_chosen = 0;
                String up_caption = "";

                // Create a dialog for choosing an album
                Dialog<Album> dialog = new Dialog<>();
                dialog.setTitle("Choose Album");

                // Set the buttons
                List<ButtonType> albumButtons = new ArrayList<>();
                for (Album album : this_users_albums) {
                    albumButtons.add(new ButtonType(album.albumName, ButtonData.LEFT));
                }
                dialog.getDialogPane().getButtonTypes().addAll(albumButtons);
                dialog.getDialogPane().getButtonTypes().addAll(ButtonType.CANCEL);

                // Set the result converter
                dialog.setResultConverter(dialogButton -> {
                    if (dialogButton == ButtonType.CANCEL) {
                        return null;
                    } else {
                        // Get the index of the selected album button
                        int index = dialog.getDialogPane().getButtonTypes().indexOf(dialogButton);

                        // Get the selected album
                        return albums.get(index);
                    }
                });

                // Show the dialog and wait for the user to choose an album
                Optional<Album> result = dialog.showAndWait();

                // Set the aid_chosen variable if an album was chosen
                if (result.isPresent()) {
                    Album chosenAlbum = result.get();
                    aid_chosen = chosenAlbum.aid;

                    // Create a dialog for entering a caption
                    TextInputDialog captionDialog = new TextInputDialog();
                    captionDialog.setTitle("Enter Caption");
                    captionDialog.setHeaderText("Enter a caption for your photo:");
                    captionDialog.setContentText("Caption:");

                    // Show the dialog and wait for the user to input a caption
                    Optional<String> captionResult = captionDialog.showAndWait();
                    if (captionResult.isPresent()) {
                        up_caption = captionResult.get();
                        // Do something with the caption, such as storing it with the photo in the
                        // chosen album
                    }

                    try{
                        Main.db.createPhoto(aid_chosen, up_caption, title);
                        }
                        catch(SQLException k){
                            k.printStackTrace();
                        }

                    try {
                        Files.copy(srcPath, destPath, StandardCopyOption.REPLACE_EXISTING);
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
                // Add the ImageView to a parent node or scene
            }

        });

        // Add the button to a parent node or scene

        stage.setScene(feedScene);
    }

}
