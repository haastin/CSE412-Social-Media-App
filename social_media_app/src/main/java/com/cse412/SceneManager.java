package com.cse412;

import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.util.Pair;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.ButtonBar.ButtonData;
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
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import javafx.collections.*;
import java.sql.*;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.List;
import java.util.Optional;
import java.util.ArrayList;

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
    public Scene searchTagsScene;
    private Stage stage;
    private Scene youmayAlsoLikeScene;
    private Scene pics_same_tag;
    private Scene pics_mutliple_tag;

    public SceneManager(Stage stage) {
        this.stage = stage;
    }

    public void switchToLogin() {

        stage.setScene(loginScene);
    }

    public void switchToFeed(boolean user_photos) {

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
            List<Integer> pids;
            if (!user_photos) {
                pids = Main.db.getAllPidsOfUsersNotLoggedIn(Main.curr_user);
            } else {
                pids = Main.db.getAllPhotosOfLoggedInUser(Main.curr_user);
            }
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

        Button ownPhotos;
        if (!user_photos) {
            ownPhotos = new Button("View Your Photos");
        } else {
            ownPhotos = new Button("View All Photos");
        }
        ownPhotos.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        ownPhotos.setStyle("-fx-background-color: TRANSPARENT");

        TextField search = new TextField();
        search.setPromptText("Search Here");
        search.setPrefWidth(250);
        search.setPrefHeight(40);

        ComboBox suggest = new ComboBox();
        suggest.setId("suggest");
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

        System.out.println(Main.curr_user);
        System.out.println(url);

        InputStream is = Main.class.getClassLoader().getResourceAsStream(url);
        Image image = new Image(is);

        ImageView view = new ImageView(image);

        view.setX(10); // double check these dimensions
        view.setY(10);

        view.setFitHeight(300);
        view.setFitWidth(500);

        view.setPreserveRatio(true);

        Button searchIt = new Button("Search");
        searchIt.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        searchIt.setStyle("-fx-background-color: LIGHTGREEN");

        Button clear = new Button("Clear");
        clear.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        clear.setStyle("-fx-background-color: LIGHTSALMON");

        ListView<String> commentsListView = new ListView<>();
        ObservableList<String> commentsList = FXCollections.observableArrayList();

        // Iterate through the comment pairs and add the formatted comments to the list
        for (Pair<String, Comment> commentPair : comments) {
            String commenter = commentPair.getKey();
            String rend_caption = commentPair.getValue().text;
            String datePosted = commentPair.getValue().date;

            String formattedComment = String.format("%s: %s (%s)", commenter, rend_caption, datePosted);
            commentsList.add(formattedComment);
        }

        commentsListView.setItems(commentsList);

        Label numLikes = new Label(total_likes + " Likes");

        numLikes.setFont(Font.font("Verdana", FontPosture.REGULAR, 15));
        numLikes.setTextFill(Color.TEAL);
        List<User> likers_copy = new ArrayList<>(likers);
        // Add an action listener to the label
        numLikes.setOnMouseClicked(event -> {
            // Create a dialog for displaying the list of likers
            Dialog<Void> dialog = new Dialog<>();
            dialog.setTitle("List of Likers");

            // Create a list view to display the likers
            ListView<String> listView = new ListView<>();

            // Map the likers to a list of their first and last names
            List<String> likerNames = likers_copy.stream().map(liker -> liker.firstName + " " + liker.lastName)
                    .collect(Collectors.toList());

            // Add the liker names to the list view
            listView.getItems().addAll(likerNames);

            // Add the list view to the dialog pane
            dialog.getDialogPane().setContent(listView);

            // Add a button to close the dialog
            ButtonType closeButton = new ButtonType("Close", ButtonData.CANCEL_CLOSE);
            dialog.getDialogPane().getButtonTypes().add(closeButton);

            // Show the dialog and wait for the user to close it
            dialog.showAndWait();
        });

        HBox hBox2 = new HBox();
        hBox2.setPadding(new Insets(10, 10, 10, 10));
        hBox2.setSpacing(5);

        VBox vBox2 = new VBox();
        vBox2.setPadding(new Insets(10, 10, 10, 20));
        vBox2.setSpacing(5);

        Button logOut = new Button("Log Out");
        logOut.setMaxSize(100.0, 100.0);

        Button next = new Button("Next pic");
        next.setMaxSize(100.0, 100.0);
        rootPane2.setBottom(next);

        HBox hBox3 = new HBox();
        hBox3.setPadding(new Insets(10, 10, 10, 10));
        hBox3.setSpacing(20);
        hBox3.setAlignment(Pos.CENTER);
        hBox3.getChildren().addAll(ownPhotos, suggest, logOut); // adding image to Hbox

        VBox vBox = new VBox();
        vBox.setPadding(new Insets(10, 10, 10, 20));
        vBox.setSpacing(5);
        // vBox.setAlignment(Pos.CENTER);

        Button likeButton = new Button("Like");
        int this_pic_pid = random_pid;
        likeButton.setOnAction(e -> {
            if (likeButton.getText().equals("Like")) {
                likeButton.setText("Liked");
                try {
                    Main.db.recordLike(this_pic_pid, Main.curr_user);
                } catch (SQLException b) {
                    b.printStackTrace();
                }
            }
        });

        // Create a button for commenting
        Button commentButton = new Button("Comment");

        // Set an action for the button
        commentButton.setOnAction(event -> {
            // Create a text input dialog for entering the caption
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Enter Caption");
            dialog.setHeaderText(null);
            dialog.setContentText("Caption:");

            // Show the dialog and wait for the user to enter a caption
            Optional<String> result = dialog.showAndWait();

            // If the user entered a caption, add it to the comments list
            result.ifPresent(new_caption -> {
                try {
                    Main.db.postComment(this_pic_pid, Main.curr_user, new_caption);
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                // code to add comment to database here
            });
        });

        VBox tagsSection = new VBox();
        tagsSection.setSpacing(10);
        tagsSection.setPadding(new Insets(10));

        Label tags_title = new Label("Tags");

        tags_title.setFont(Font.font("Times New Roman", FontPosture.REGULAR, 18));
        tags_title.setTextFill(Color.BLACK);
        tagsSection.getChildren().add(view);
        tagsSection.getChildren().add(tags_title);
        // Create the tag buttons
        for (Tag tag : tags) {
            Button tagButton = new Button(tag.word);
            tagButton.setOnAction(event -> {
                switchToTagSearch(false, tag.word);
            });
            tagsSection.getChildren().add(tagButton);
        }

        Button upload_button = new Button("Upload Image");

        HBox hBox4 = new HBox();
        hBox4.setPadding(new Insets(10, 10, 10, 10));
        hBox4.setSpacing(20);
        hBox4.setAlignment(Pos.CENTER);
        hBox4.getChildren().addAll(next, upload_button);

        vBox2.getChildren().addAll(numLikes, likeButton, commentButton, commentsListView);

        hBox2.setAlignment(Pos.CENTER_LEFT);
        hBox2.getChildren().addAll(tagsSection, vBox2); // adding image to Hbox

        vBox.getChildren().addAll(feed, hBox3, hBox2, hBox4);

        /*
         * for (int i = 0; i < comments.size(); i++) {
         * Pair<String, Comment> full_comment = comments.get(i);
         * String comm = full_comment.getKey() + " " + full_comment.getValue().text;
         * vBox.getChildren().add(new Label(comm));
         * }
         */
        rootPane2.setCenter(vBox);

        // Create a scene and place it in the stage
        Scene feedScene = new Scene(rootPane2, 700, 600); // also x and y correlated
        this.feedScene = feedScene;

        stage.setTitle("Social Media App"); // Set the stage title
        stage.setScene(feedScene); // Place the scene in the stage
        stage.show(); // Display the stage

        next.setOnAction((ActionEvent h) -> {
            this.switchToFeed(user_photos);
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
                    switchToUserFriends();

                }

                else if (suggest.getValue().toString().equals("Users Who Have You As A Friend")) {
                    switchTodisplayFriendsOfUsers();
                }

                else if (suggest.getValue().toString().equals("Recommended Friends")) {
                    switchToRecommendFriends();
                }

                else if (suggest.getValue().toString().equals("Search for Tags")) {
                    switchToMultipleTagSearchBasePage();
                }

                else if (suggest.getValue().toString().equals("Search for Comments")) {
                    stage.setScene(searchCommentsScene);
                    stage.show();
                }

                else if (suggest.getValue().toString().equals("Most Popular Tags")) {
                    stage.setScene(mostPopTagsScene); // change to most popular tags page
                    stage.show();
                }

                else if (suggest.getValue().toString().equals("Edit Your Page")) {
                    switchToOwnPage();
                }

                else if (suggest.getValue().toString().equals("You May Also Like")) {
                    switchToYouMayAlsoLike();
                }

            }
        };

        suggest.setOnAction(event);

        // button listener below

        searchIt.setOnAction((ActionEvent f) -> {
            // primaryStage.setScen suggest-scene); // based on the scene names of th
            // suggest pg
        });

        if (!user_photos) {
            ownPhotos.setOnAction((ActionEvent z) -> this.switchToFeed(true));
        } else {
            ownPhotos.setOnAction((ActionEvent z) -> this.switchToFeed(false));
        }

        logOut.setOnAction((ActionEvent h) -> {
            this.switchToLogin();
            // primaryStage.setScene(scene1); // based on the scene name of the welcome pg
        });

        // Set the action for the button
        upload_button.setOnAction(e -> {
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

                    try {
                        Main.db.createPhoto(aid_chosen, up_caption, title);
                    } catch (SQLException k) {
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

    void switchToMultipleTagSearchBasePage() {

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

        // set scene
        rootPaneSearchTags.setCenter(centerPaneSearchTags);
        centerPaneSearchTags.add(TagSearch, 0, 0);
        centerPaneSearchTags.add(TagSearchDirections, 0, 1);
        centerPaneSearchTags.add(TagSearchButton, 0, 3);
        centerPaneSearchTags.add(OwnTagPhotos, 0, 4);
        centerPaneSearchTags.add(TagSearchGoBack, 0, 5);
        centerPaneSearchTags.add(TagSearchField, 0, 2);

        Scene SearchTagScene = new Scene(rootPaneSearchTags, 700, 600);
        Main.sm.searchTagsScene = SearchTagScene;

        List<String> tags_to_search = new ArrayList<>();
        int photoToGetOthers = 0;
        int photoToGetOwn = 0;
        boolean showingOwnPhotos = false;


        OwnTagPhotos.setOnAction(ev -> {

            tags_to_search.clear();

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
            String[] search_all_tags = new String[tags_to_search.size()];
            int x = 0;
            for (String tag : tags_to_search) {
                search_all_tags[x] = tag;
                x++;
            }

            try {

                List<Integer> all_photos_with_tags = Main.db.getPhotosByMultipleTagsAndUser(search_all_tags, Main.curr_user);
                System.out.println(all_photos_with_tags.size());
                if (all_photos_with_tags.size() > 0) {
                    Main.sm.switchToMultipleTagSearch(true, all_photos_with_tags);
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }

        });

        TagSearchButton.setOnAction(ev -> {

            tags_to_search.clear();

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
            String[] search_all_tags = new String[tags_to_search.size()];
            int x = 0;
            for (String tag : tags_to_search) {
                search_all_tags[x] = tag;
                x++;
            }

            try {

                List<Integer> all_photos_with_tags = Main.db.getPhotosByMultipleTags(search_all_tags, Main.curr_user);
                if (all_photos_with_tags.size() > 0) {
                    Main.sm.switchToMultipleTagSearch(false, all_photos_with_tags);
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }
        });

        // if go back button pressed, go back to feed
        TagSearchGoBack.setOnAction(ev -> {

            Main.sm.switchToFeed(false);

        });

        stage.setScene(SearchTagScene);
    }

    void switchToMultipleTagSearch(boolean user_photos, List<Integer> all_photos_with_tags) {

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

        int rows = all_photos_with_tags.size();
        Random rando = new Random();
        int random_row = rando.nextInt(rows);
        random_pid = all_photos_with_tags.get(random_row);

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

        Button ownPhotos;
        if (!user_photos) {
            ownPhotos = new Button("Viewing Your Photos (Cannot switch to All Photos During a Search)");
        } else {
            ownPhotos = new Button("View All Photos (Cannot switch to Your Photos During a Search)");
        }
        ownPhotos.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        ownPhotos.setStyle("-fx-background-color: TRANSPARENT");

        InputStream is = Main.class.getClassLoader().getResourceAsStream(url);
        Image image = new Image(is);

        ImageView view = new ImageView(image);

        view.setX(10); // double check these dimensions
        view.setY(10);

        view.setFitHeight(300);
        view.setFitWidth(500);

        view.setPreserveRatio(true);

        ListView<String> commentsListView = new ListView<>();
        ObservableList<String> commentsList = FXCollections.observableArrayList();

        // Iterate through the comment pairs and add the formatted comments to the list
        for (Pair<String, Comment> commentPair : comments) {
            String commenter = commentPair.getKey();
            String rend_caption = commentPair.getValue().text;
            String datePosted = commentPair.getValue().date;

            String formattedComment = String.format("%s: %s (%s)", commenter, rend_caption, datePosted);
            commentsList.add(formattedComment);
        }

        commentsListView.setItems(commentsList);

        Label numLikes = new Label(total_likes + " Likes");

        numLikes.setFont(Font.font("Verdana", FontPosture.REGULAR, 15));
        numLikes.setTextFill(Color.TEAL);
        List<User> likers_copy = new ArrayList<>(likers);
        // Add an action listener to the label
        numLikes.setOnMouseClicked(event -> {
            // Create a dialog for displaying the list of likers
            Dialog<Void> dialog = new Dialog<>();
            dialog.setTitle("List of Likers");

            // Create a list view to display the likers
            ListView<String> listView = new ListView<>();

            // Map the likers to a list of their first and last names
            List<String> likerNames = likers_copy.stream().map(liker -> liker.firstName + " " + liker.lastName)
                    .collect(Collectors.toList());

            // Add the liker names to the list view
            listView.getItems().addAll(likerNames);

            // Add the list view to the dialog pane
            dialog.getDialogPane().setContent(listView);

            // Add a button to close the dialog
            ButtonType closeButton = new ButtonType("Close", ButtonData.CANCEL_CLOSE);
            dialog.getDialogPane().getButtonTypes().add(closeButton);

            // Show the dialog and wait for the user to close it
            dialog.showAndWait();
        });

        HBox hBox2 = new HBox();
        hBox2.setPadding(new Insets(10, 10, 10, 10));
        hBox2.setSpacing(5);

        VBox vBox2 = new VBox();
        vBox2.setPadding(new Insets(10, 10, 10, 20));
        vBox2.setSpacing(5);

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

        Button likeButton = new Button("Like");
        int this_pic_pid = random_pid;
        likeButton.setOnAction(e -> {
            if (likeButton.getText().equals("Like")) {
                likeButton.setText("Liked");
                try {
                    Main.db.recordLike(this_pic_pid, Main.curr_user);
                } catch (SQLException b) {
                    b.printStackTrace();
                }
            }
        });

        // Create a button for commenting
        Button commentButton = new Button("Comment");

        // Set an action for the button
        commentButton.setOnAction(event -> {
            // Create a text input dialog for entering the caption
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Enter Caption");
            dialog.setHeaderText(null);
            dialog.setContentText("Caption:");

            // Show the dialog and wait for the user to enter a caption
            Optional<String> result = dialog.showAndWait();

            // If the user entered a caption, add it to the comments list
            result.ifPresent(new_caption -> {
                try {
                    Main.db.postComment(this_pic_pid, Main.curr_user, new_caption);
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                // code to add comment to database here
            });
        });

        VBox tagsSection = new VBox();
        tagsSection.setSpacing(10);
        tagsSection.setPadding(new Insets(10));

        Label tags_title = new Label("Tags");

        tags_title.setFont(Font.font("Times New Roman", FontPosture.REGULAR, 18));
        tags_title.setTextFill(Color.BLACK);
        tagsSection.getChildren().add(view);
        tagsSection.getChildren().add(tags_title);
        // Create the tag buttons
        for (Tag tag : tags) {
            Button tagButton = new Button(tag.word);
            tagButton.setOnAction(event -> {
                switchToTagSearch(false, tag.word);
            });
            tagsSection.getChildren().add(tagButton);
        }

        Button goBack = new Button("Go Back To Main Feed");

        HBox hBox4 = new HBox();
        hBox4.setPadding(new Insets(10, 10, 10, 10));
        hBox4.setSpacing(20);
        hBox4.setAlignment(Pos.CENTER);
        hBox4.getChildren().addAll(next, goBack);

        vBox2.getChildren().addAll(numLikes, likeButton, commentButton, commentsListView);

        hBox2.setAlignment(Pos.CENTER_LEFT);
        hBox2.getChildren().addAll(tagsSection, vBox2); // adding image to Hbox

        vBox.getChildren().addAll(feed, hBox3, hBox2, hBox4);

        /*
         * for (int i = 0; i < comments.size(); i++) {
         * Pair<String, Comment> full_comment = comments.get(i);
         * String comm = full_comment.getKey() + " " + full_comment.getValue().text;
         * vBox.getChildren().add(new Label(comm));
         * }
         */
        rootPane2.setCenter(vBox);

        // Create a scene and place it in the stage
        Scene pics_multiple_tag = new Scene(rootPane2, 700, 600); // also x and y correlated
        this.pics_mutliple_tag = pics_multiple_tag;

        next.setOnAction((ActionEvent h) -> {
            this.switchToMultipleTagSearch(false, all_photos_with_tags);
        });

        logOut.setOnAction((ActionEvent h) -> {
            this.switchToLogin();
            // primaryStage.setScene(scene1); // based on the scene name of the welcome pg
        });

        goBack.setOnAction((ActionEvent e) -> this.switchBackToFeedPageSameState());

        // Add the button to a parent node or scene

        stage.setScene(pics_multiple_tag);

    }

    void switchToTagSearch() {
        stage.setScene(searchTagsScene);
    }

    public void switchToTagSearch(boolean user_photos, String word) {

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
            List<Integer> pids;
            if (!user_photos) {
                pids = Main.db.getPhotoIdsByTag(word, Main.curr_user);
            } else {
                pids = Main.db.getOwnPhotoIdsByTag(Main.curr_user, word);
            }
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

        Button ownPhotos;
        if (!user_photos) {
            ownPhotos = new Button("View Your Photos");
        } else {
            ownPhotos = new Button("View All Photos");
        }
        ownPhotos.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        ownPhotos.setStyle("-fx-background-color: TRANSPARENT");

        InputStream is = Main.class.getClassLoader().getResourceAsStream(url);
        Image image = new Image(is);

        ImageView view = new ImageView(image);

        view.setX(10); // double check these dimensions
        view.setY(10);

        view.setFitHeight(300);
        view.setFitWidth(500);

        view.setPreserveRatio(true);

        ListView<String> commentsListView = new ListView<>();
        ObservableList<String> commentsList = FXCollections.observableArrayList();

        // Iterate through the comment pairs and add the formatted comments to the list
        for (Pair<String, Comment> commentPair : comments) {
            String commenter = commentPair.getKey();
            String rend_caption = commentPair.getValue().text;
            String datePosted = commentPair.getValue().date;

            String formattedComment = String.format("%s: %s (%s)", commenter, rend_caption, datePosted);
            commentsList.add(formattedComment);
        }

        commentsListView.setItems(commentsList);

        Label numLikes = new Label(total_likes + " Likes");

        numLikes.setFont(Font.font("Verdana", FontPosture.REGULAR, 15));
        numLikes.setTextFill(Color.TEAL);
        List<User> likers_copy = new ArrayList<>(likers);
        // Add an action listener to the label
        numLikes.setOnMouseClicked(event -> {
            // Create a dialog for displaying the list of likers
            Dialog<Void> dialog = new Dialog<>();
            dialog.setTitle("List of Likers");

            // Create a list view to display the likers
            ListView<String> listView = new ListView<>();

            // Map the likers to a list of their first and last names
            List<String> likerNames = likers_copy.stream().map(liker -> liker.firstName + " " + liker.lastName)
                    .collect(Collectors.toList());

            // Add the liker names to the list view
            listView.getItems().addAll(likerNames);

            // Add the list view to the dialog pane
            dialog.getDialogPane().setContent(listView);

            // Add a button to close the dialog
            ButtonType closeButton = new ButtonType("Close", ButtonData.CANCEL_CLOSE);
            dialog.getDialogPane().getButtonTypes().add(closeButton);

            // Show the dialog and wait for the user to close it
            dialog.showAndWait();
        });

        HBox hBox2 = new HBox();
        hBox2.setPadding(new Insets(10, 10, 10, 10));
        hBox2.setSpacing(5);

        VBox vBox2 = new VBox();
        vBox2.setPadding(new Insets(10, 10, 10, 20));
        vBox2.setSpacing(5);

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

        Button likeButton = new Button("Like");
        int this_pic_pid = random_pid;
        likeButton.setOnAction(e -> {
            if (likeButton.getText().equals("Like")) {
                likeButton.setText("Liked");
                try {
                    Main.db.recordLike(this_pic_pid, Main.curr_user);
                } catch (SQLException b) {
                    b.printStackTrace();
                }
            }
        });

        // Create a button for commenting
        Button commentButton = new Button("Comment");

        // Set an action for the button
        commentButton.setOnAction(event -> {
            // Create a text input dialog for entering the caption
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Enter Caption");
            dialog.setHeaderText(null);
            dialog.setContentText("Caption:");

            // Show the dialog and wait for the user to enter a caption
            Optional<String> result = dialog.showAndWait();

            // If the user entered a caption, add it to the comments list
            result.ifPresent(new_caption -> {
                try {
                    Main.db.postComment(this_pic_pid, Main.curr_user, new_caption);
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                // code to add comment to database here
            });
        });

        VBox tagsSection = new VBox();
        tagsSection.setSpacing(10);
        tagsSection.setPadding(new Insets(10));

        Label tags_title = new Label("Tags");

        tags_title.setFont(Font.font("Times New Roman", FontPosture.REGULAR, 18));
        tags_title.setTextFill(Color.BLACK);
        tagsSection.getChildren().add(view);
        tagsSection.getChildren().add(tags_title);
        // Create the tag buttons
        for (Tag tag : tags) {
            Button tagButton = new Button(tag.word);
            tagButton.setOnAction(event -> {
                switchToTagSearch(user_photos, word);
            });
            tagsSection.getChildren().add(tagButton);
        }

        Button goBack = new Button("Go Back To Main Feed");

        HBox hBox4 = new HBox();
        hBox4.setPadding(new Insets(10, 10, 10, 10));
        hBox4.setSpacing(20);
        hBox4.setAlignment(Pos.CENTER);
        hBox4.getChildren().addAll(next, goBack);

        vBox2.getChildren().addAll(numLikes, likeButton, commentButton, commentsListView);

        hBox2.setAlignment(Pos.CENTER_LEFT);
        hBox2.getChildren().addAll(tagsSection, vBox2); // adding image to Hbox

        vBox.getChildren().addAll(feed, hBox3, hBox2, hBox4);

        /*
         * for (int i = 0; i < comments.size(); i++) {
         * Pair<String, Comment> full_comment = comments.get(i);
         * String comm = full_comment.getKey() + " " + full_comment.getValue().text;
         * vBox.getChildren().add(new Label(comm));
         * }
         */
        rootPane2.setCenter(vBox);

        // Create a scene and place it in the stage
        Scene pics_same_tag = new Scene(rootPane2, 700, 600); // also x and y correlated
        this.pics_same_tag = pics_same_tag;

        next.setOnAction((ActionEvent h) -> {
            this.switchToTagSearch(user_photos, word);
        });

        if (!user_photos) {
            ownPhotos.setOnAction((ActionEvent z) -> this.switchToTagSearch(true, word));
        } else {
            ownPhotos.setOnAction((ActionEvent z) -> this.switchToTagSearch(false, word));
        }

        logOut.setOnAction((ActionEvent h) -> {
            this.switchToLogin();
            // primaryStage.setScene(scene1); // based on the scene name of the welcome pg
        });

        goBack.setOnAction((ActionEvent e) -> this.switchBackToFeedPageSameState());

        // Add the button to a parent node or scene

        stage.setScene(pics_same_tag);
    }

    void switchToUserFriends() {
        BorderPane rootPaneSearchUserFriends = new BorderPane();
        GridPane centerPaneSearchUserFriends = new GridPane();
        centerPaneSearchUserFriends.setAlignment(Pos.CENTER);
        centerPaneSearchUserFriends.setPadding(new Insets(10, 10, 10, 10)); // this is the spacing from the perimeter of
                                                                            // the window
        centerPaneSearchUserFriends.setHgap(10); // the spacing between objects horizontally
        centerPaneSearchUserFriends.setVgap(10); // the spacing between objects horizontally

        // Create labels and buttons

        Label UserFriendWelcome = new Label("Your Friends");
        Button UserFriendsGoBack = new Button("Go Back");
        UserFriendWelcome.setFont(new Font("Times New Roman", 20)); // double check the font
        UserFriendWelcome.setTextFill(Color.CRIMSON);

        // add the 3 labels and 3 text fields accordingly
        rootPaneSearchUserFriends.setCenter(centerPaneSearchUserFriends);
        centerPaneSearchUserFriends.add(UserFriendWelcome, 0, 0);
        centerPaneSearchUserFriends.add(UserFriendsGoBack, 0, 11);

        // it's like x and y coordinates, but use the window dimensions to kind of see
        // how big a unit is

        // Create a scene and place it in the stage

        Scene sceneUserFriends = new Scene(rootPaneSearchUserFriends, 700, 600); // also x and y correlated

        List<Integer> this_users_friends = new ArrayList<>();
        try {
            this_users_friends = Main.db.getAllFriends(Main.curr_user);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println(this_users_friends.size());
        // put 10 friends on display
        int i = 1;
        for (Integer user : this_users_friends) {
            System.out.println(user);
            User u = null;
            try {
                u = Main.db.getAllUserInfo(user);
            } catch (SQLException e) {

            }
            centerPaneSearchUserFriends.add(new Label(u.firstName), 0, i);
            centerPaneSearchUserFriends.add(new Label(u.lastName), 1, i);
            i++;

            if (i >= 11) {
                break;
            }

        }

        Main.sm.displayUserFriendsScene = sceneUserFriends;

        UserFriendsGoBack.setOnAction(ev -> {

            Main.sm.switchToFeed(false);

        });
        stage.setScene(sceneUserFriends);
    }

    void switchBackToFeedPageSameState() {
        stage.setScene(feedScene);
    }

    void switchTodisplayFriendsOfUsers() {

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

        // get all users that have this user as a friend
        List<Integer> friends_of_this_user = new ArrayList<>();
        try {
            friends_of_this_user = Main.db.getAllUsersWhoFriendedThisUser(Main.curr_user);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // put friends of user on pane
        int i = 1;
        for (Integer user : friends_of_this_user) {

            User u = null;
            try {
                u = Main.db.getAllUserInfo(user);
            } catch (SQLException e) {

            }
            centerPaneFriendsOfUser.add(new Label(u.firstName), 0, i);
            centerPaneFriendsOfUser.add(new Label(u.lastName), 1, i);
            i++;

            if (i >= 11) {
                break;
            }

        }

        Main.sm.displayFriendsOfUsersScene = FriendsOfUsersScene;

        FriendsOfUserGoBack.setOnAction(ev -> {

            Main.sm.switchToFeed(false);

        });

        stage.setScene(displayFriendsOfUsersScene);
    }

    void switchToRecommendFriends() {

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

        // get all friends of this user's friends
        List<Integer> friends_of_this_users_friends = new ArrayList<>();
        try {
            friends_of_this_users_friends = Main.db.getAllFriendsOfFriends(Main.curr_user);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // add friend recommendations to pane
        int i = 1;
        for (Integer user : friends_of_this_users_friends) {

            User u = null;
            try {
                u = Main.db.getAllUserInfo(user);
            } catch (SQLException e) {

            }
            centerPaneFriendRec.add(new Label(u.firstName), 0, i);
            centerPaneFriendRec.add(new Label(u.lastName), 1, i);
            i++;

            if (i >= 11) {
                break;
            }

        }

        Main.sm.friendRecScene = FriendsRecScene;

        FriendsRecGoBack.setOnAction(ev -> {

            Main.sm.switchToFeed(false);

        });
        stage.setScene(FriendsRecScene);
    }

    void switchToOwnPage() {

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
        Button createalbumButton = new Button("Create New Album");
        // add labels and such to panes
        rootPaneUserPage.setCenter(centerPaneUserPage);
        centerPaneUserPage.add(YourPageWelcome, 0, 0);
        centerPaneUserPage.add(UserPageGoBack, 0, 17);
        centerPaneUserPage.add(ChangeUserInfo, 0, 18);
        centerPaneUserPage.add(createalbumButton, 0, 19);

        // create scene
        Scene UserPageScene = new Scene(rootPaneUserPage, 700, 600);

        // in the case where you want to view your own profile, you can make the target
        // user uid the current user
        int target_user_uid = Main.curr_user;
        List<Album> this_users_albums = new ArrayList<>();
        try {
            this_users_albums = Main.db.getAllAlbumsOfLoggedInUser(target_user_uid);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // put album names onto page
        int i = 7;
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
            user_info = Main.db.getAllUserInfo(target_user_uid);
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
        Main.sm.userProfileScene = UserPageScene;

        // retrieving pids in an album
        int retrieve_pids_from_aid = 5210;
        List<Integer> pids_in_album = new ArrayList<>();
        try {
            pids_in_album = Main.db.getAllPhotosInAnAlbum(retrieve_pids_from_aid);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        /*
         * for (Integer pid : pids_in_album) {
         * System.out.print(pid + " ");
         * }
         */

        // retrieving all pids belonging to a user
        List<Integer> all_users_pids = new ArrayList<>();
        try {
            all_users_pids = Main.db.getAllPhotosOfLoggedInUser(target_user_uid);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        /*
         * for (Integer pids : all_users_pids) {
         * System.out.print(pids + " ");
         * }
         */

        // let the user change their info if they press the changeinfo button
        ChangeUserInfo.setOnAction(ev -> {

            // create a new scene where the user can change their info
            // should look similar to the registration page
            BorderPane rootPaneChangeInfo = new BorderPane();
            GridPane centerPaneChangeInfo = new GridPane();
            centerPaneChangeInfo.setAlignment(Pos.CENTER);
            centerPaneChangeInfo.setPadding(new Insets(10, 10, 10, 10)); // this is the spacing from the perimeter of
                                                                         // the window
            centerPaneChangeInfo.setHgap(10); // the spacing between objects horizontally
            centerPaneChangeInfo.setVgap(10); // the spacing between objects horizontally

            // Create 3 labels

            Label FirstNameChangeInfo = new Label("FIRST NAME (*)");
            Label LastNameChangeInfo = new Label("LAST NAME (*)");
            Label PasswordChangeInfo = new Label("PASSWORD (*)");
            Label GenderChangeInfo = new Label("GENDER");
            Label HometownChangeInfo = new Label("HOMETOWN");
            Label DOBChangeInfo = new Label("DATE OF BIRTH (*) (Format YYYY-MM-DD)");
            Label ChangeInfoWelcome = new Label("Change User Info");
            Button ChangeInfoButton = new Button("Set Changes");

            ChangeInfoWelcome.setFont(new Font("Times New Roman", 20)); // double check the font--
            ChangeInfoWelcome.setTextFill(Color.CRIMSON);

            // Create text fields and dropdowns

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
                    "Other");
            TextField HometownFieldChangeInfo = new TextField();
            HometownFieldChangeInfo.setText(userHometown);
            TextField DOBFieldChangeInfo = new TextField();
            DOBFieldChangeInfo.setText(userDOB);

            // add the labels and text fields accordingly
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

            Scene sceneChangeInfo = new Scene(rootPaneChangeInfo, 700, 600); // also x and y correlated

            stage.setTitle("Social Media App"); // Set the stage title
            stage.setScene(sceneChangeInfo); // Place the scene in the stage
            stage.show(); // Display the stage

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
                String password_hashed = password_unhashed;
                String hometown = HometownFieldChangeInfo.getText();
                String dob = DOBFieldChangeInfo.getText();

                if (!password_unhashed.equals("********")) {

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
                }

                // try updating user info
                boolean success = true;
                try {

                    Main.db.updateUser(Main.curr_user, firstName, lastName, password_hashed, gender, hometown, dob);
                } catch (SQLException e) {
                    e.printStackTrace();
                    ErrorMessageChangeInfo.setText("Invalid Email and/or Date of Birth Format. Try again.");
                    success = false;
                }

                if (success) {
                    Main.sm.switchToFeed(false);
                }

            });

        });

        // Set the on-click action for the button
        createalbumButton.setOnAction(e -> {
            // Create a new dialog for creating an album
            Dialog<String> dialog = new Dialog<>();
            dialog.setTitle("Create New Album");

            // Set the buttons
            ButtonType confirmButtonType = new ButtonType("Create", ButtonData.OK_DONE);
            dialog.getDialogPane().getButtonTypes().addAll(confirmButtonType, ButtonType.CANCEL);

            // Create the input fields
            TextField albumNameField = new TextField();
            albumNameField.setPromptText("Album Name");

            // Add the input fields to the dialog
            VBox vbox = new VBox();
            vbox.getChildren().addAll(new Label("Enter the album name:"), albumNameField);
            dialog.getDialogPane().setContent(vbox);

            // Enable the Create button only when input is valid
            Node createButton = dialog.getDialogPane().lookupButton(confirmButtonType);
            createButton.setDisable(true);
            albumNameField.textProperty().addListener((observable, oldValue, newValue) -> {
                createButton.setDisable(newValue.trim().isEmpty());
            });

            // Convert the result to a new album name
            dialog.setResultConverter(dialogButton -> {
                if (dialogButton == confirmButtonType) {
                    return albumNameField.getText().trim();
                }
                return null;
            });

            // Show the dialog and wait for the user to create a new album
            Optional<String> result = dialog.showAndWait();

            // Set the new_albumName variable if a new album was created

            if (result.isPresent()) {
                String newAlbumName = result.get();
                try {
                    Main.db.createAlbum(Main.curr_user, newAlbumName);
                } catch (SQLException v) {
                    v.printStackTrace();
                }
            }
        });

        // go back to feedpage if goback button is pressed
        UserPageGoBack.setOnAction(ev -> {

            Main.sm.switchToFeed(false);

        });

        stage.setScene(userProfileScene);
        stage.show();
    }

    void switchToYouMayAlsoLike() {

        List<Pair<Integer, Integer>> top_pics = new ArrayList<>();
        try {
            top_pics = Main.db.getTopTagsForUser(Main.curr_user);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        BorderPane rootPaneYouMayAlsoLike = new BorderPane();
        GridPane centerPaneYouMayAlsoLike = new GridPane();

        centerPaneYouMayAlsoLike.setAlignment(Pos.CENTER); // centered alignment
        centerPaneYouMayAlsoLike.setPadding(new Insets(1, 1, 1, 1)); // this is the spacing from the perimeter of the
                                                                     // window
        centerPaneYouMayAlsoLike.setHgap(10); // the spacing between objects horizontally
        centerPaneYouMayAlsoLike.setVgap(10); // the spacing between objects horizontally

        Label YouMayAlsoLikeWelcome = new Label("You May Also Like");

        YouMayAlsoLikeWelcome.setFont(Font.font("Times New Roman", FontPosture.REGULAR, 25));
        YouMayAlsoLikeWelcome.setTextFill(Color.HOTPINK);

        int photoNum = 0;

        String urlYMAL = "";
        String poster_firstnameYMAL = "";
        String poster_lastnameYMAL = "";
        String captionYMAL = "";
        List<Tag> tagsYMAL = new ArrayList<>();
        List<Pair<String, Comment>> commentsYMAL = new ArrayList<>();

        List<User> likersYMAL = new ArrayList<>();
        int total_likesYMAL = 0;
        User posterYMAL = new User();
        ;
        Photo picYMAL = new Photo();

        try {
            picYMAL = Main.db.fetchPhotoInfo(top_pics.get(photoNum).getKey());
            commentsYMAL = Main.db.fetchPhotoComments(top_pics.get(photoNum).getKey());
            likersYMAL = Main.db.fetchPhotoLikers(top_pics.get(photoNum).getKey());
            tagsYMAL = Main.db.fetchPhotoTags(top_pics.get(photoNum).getKey());
            posterYMAL = Main.db.fetchPhotoUser(top_pics.get(photoNum).getKey());
        } catch (SQLException e) {

        }

        poster_firstnameYMAL = posterYMAL.firstName;
        poster_lastnameYMAL = posterYMAL.lastName;

        captionYMAL = picYMAL.caption;
        urlYMAL = picYMAL.url;

        total_likesYMAL = likersYMAL.size();

        InputStream isYMAL = Main.class.getClassLoader().getResourceAsStream(urlYMAL);
        Image imageYMAL = new Image(isYMAL);

        ImageView viewYMAL = new ImageView(imageYMAL);

        viewYMAL.setX(25);
        viewYMAL.setY(25);

        viewYMAL.setFitHeight(100);
        viewYMAL.setFitWidth(100);

        viewYMAL.setPreserveRatio(true);

        Label comment0YMAL = new Label("Comments");

        comment0YMAL.setFont(Font.font("Verdana", FontPosture.REGULAR, 15));
        comment0YMAL.setTextFill(Color.INDIGO);

        Label comment1YMAL = new Label("");
        comment1YMAL.setFont(Font.font("Verdana", FontPosture.REGULAR, 10));
        comment1YMAL.setTextFill(Color.BLACK);

        Label numLikesYMAL = new Label(total_likesYMAL + " Likes"); // NOT SURE WHERE TO GET LIKES FROM

        numLikesYMAL.setFont(Font.font("Verdana", FontPosture.REGULAR, 15));
        numLikesYMAL.setTextFill(Color.TEAL);

        HBox hBox2YMAL = new HBox();
        hBox2YMAL.setPadding(new Insets(10, 10, 10, 10));
        hBox2YMAL.setSpacing(5);

        hBox2YMAL.setAlignment(Pos.CENTER_RIGHT);
        hBox2YMAL.getChildren().add(viewYMAL);

        Button nextYMAL = new Button("Next pic");
        nextYMAL.setMaxSize(100.0, 100.0);
        rootPaneYouMayAlsoLike.setBottom(nextYMAL);

        VBox vBoxYMAL = new VBox();
        vBoxYMAL.setPadding(new Insets(10, 10, 10, 20));
        vBoxYMAL.setSpacing(5);
        // vBox.setAlignment(Pos.CENTER);
        vBoxYMAL.getChildren().addAll(hBox2YMAL, numLikesYMAL, comment0YMAL, comment1YMAL);

        for (int p = 0; p < commentsYMAL.size(); p++) {
            Pair<String, Comment> full_comment = commentsYMAL.get(p);
            String comm = full_comment.getKey() + " " + full_comment.getValue().text;
            vBoxYMAL.getChildren().add(new Label(comm));
        }

        rootPaneYouMayAlsoLike.setCenter(vBoxYMAL);

        Scene YouMayAlsoLikeScene = new Scene(rootPaneYouMayAlsoLike, 700, 600);
        Main.sm.youmayAlsoLikeScene = YouMayAlsoLikeScene;

        stage.setScene(YouMayAlsoLikeScene);
    }

}
